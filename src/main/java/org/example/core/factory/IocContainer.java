package org.example.core.factory;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;
import java.util.*;

public class IocContainer {


    private final Map<String, Class<?>> components = new HashMap<>();

    private final Map<String, Class<?>> proxyComponents = new HashMap<>();

    private final Map<String, List<String>> dependencies = new HashMap<>();

    private final Map<String, Object> instances = new HashMap<>();


    protected void init() {
        components.forEach((key, value) -> {
            Constructor<?>[] declaredConstructors = value.getDeclaredConstructors();
            if (declaredConstructors.length != 1) {
                throw new RuntimeException("Only one constructor is allowed");
            }
            Constructor<?> constructor = declaredConstructors[0];
            List<String> dependencyNames = new ArrayList<>();
            for (Class<?> parameterType : constructor.getParameterTypes()) {
                dependencyNames.add(parameterType.getSimpleName());
            }
            dependencies.put(key, dependencyNames);
        });

        //create instances with dependencies
        checkCycle(dependencies);
        for (String name : components.keySet()) {
            addInstance(name, components, dependencies, instances);
        }
    }


    private void addInstance(String name, Map<String, Class<?>> components, Map<String, List<String>> dependencies, Map<String, Object> instances) {
        if (instances.containsKey(name)) {
            return;
        }
        Class<?> component = components.get(name);
        List<String> dependencyNames = dependencies.get(name);
        Object[] dependenciesInstances = new Object[dependencyNames.size()];
        for (int i = 0; i < dependencyNames.size(); i++) {
            String dependencyName = dependencyNames.get(i);
            addInstance(dependencyName, components, dependencies, instances);
            dependenciesInstances[i] = instances.get(dependencyName);
        }
        try {
            Object o = component.getConstructor(dependencies.get(name).stream().map(components::get).toArray(Class[]::new)).newInstance(dependenciesInstances);

            if (proxyComponents.containsKey(name)) {
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(component);
                Class<?> aClass = proxyComponents.get(name);

                if (aClass.getDeclaredConstructors().length != 1) {
                    throw new RuntimeException("Only one constructor is allowed");
                }
                Object proxyInstance = aClass.getDeclaredConstructors()[0].newInstance(o);

                enhancer.setCallback((Callback) proxyInstance);


                Class<?>[] types = components.entrySet().stream().filter(
                        e -> dependencyNames.contains(e.getKey())
                ).map(Map.Entry::getValue).toArray(Class[]::new);

                Object[] depValues = instances.entrySet().stream().filter(
                        e -> dependencyNames.contains(e.getKey())
                ).map(Map.Entry::getValue).toArray(Object[]::new);

                o = enhancer.create(types, depValues);
            }

            instances.put(name, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCycle(Map<String, List<String>> dependencies) {
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();
        for (String node : dependencies.keySet()) {
            if (dfs(dependencies, visited, visiting, node)) {
                List<String> cycle = new ArrayList<>(visiting);
                Collections.reverse(cycle);
                cycle.add(node);
                throw new RuntimeException("Circular dependency found: " + String.join(" -> ", cycle));
            }
        }
    }

    private boolean dfs(Map<String, List<String>> dependencies, Set<String> visited, Set<String> visiting, String node) {
        if (visiting.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }
        visiting.add(node);
        for (String neighbor : dependencies.getOrDefault(node, Collections.emptyList())) {
            if (dfs(dependencies, visited, visiting, neighbor)) {
                return true;
            }
        }
        visiting.remove(node);
        visited.add(node);
        return false;
    }

    public <T> T getComponent(String name) {
        return (T) instances.get(name);
    }

    public Map<String, Class<?>> getComponents() {
        return components;
    }

    public Map<String, List<String>> getDependencies() {
        return dependencies;
    }

    public Map<String, Object> getInstances() {
        return instances;
    }


    protected void setComponents(Map<String, Class<?>> components) {
        this.components.putAll(components);
    }

    protected void setProxyComponents(Map<String, Class<?>> proxyComponents) {
        this.proxyComponents.putAll(proxyComponents);
    }


}
