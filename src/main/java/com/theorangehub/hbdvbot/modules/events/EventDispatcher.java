package com.theorangehub.hbdvbot.modules.events;

import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.commands.base.ICommand;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("Duplicates")
@Slf4j
public class EventDispatcher {
    public static Map<Class<?>, Object> instances = new HashMap<>();

    public static void createCommandsByCalls(Set<Method> methods, CommandRegistry registry) {
        methods.parallelStream().filter(method ->
            method.isAnnotationPresent(Command.class)
                && method.getParameterCount() == 0
                && ICommand.class.isAssignableFrom(method.getReturnType())
        ).forEach(method -> {
            try {
                ICommand command;

                Object instance = null;
                if (!Modifier.isStatic(method.getModifiers())) {
                    instance = getOrCreate(method.getDeclaringClass());
                }

                method.setAccessible(true);
                command = (ICommand) method.invoke(instance);

                Command meta = method.getAnnotation(Command.class);

                for (String k : meta.value()) {
                    registry.register(k, command);
                }
            } catch (InstantiationException | InvocationTargetException e) {
                log.error("Could not initialize a command: ", e);
            } catch (IllegalAccessException e) {
                log.error("Could not access a command class: ", e);
            }
        });
    }

    public static void dispatchEvents(Set<Method> methods, Object event) {
        methods.parallelStream()
            .filter(method -> method.getParameterCount() == 1
                && method.getParameterTypes()[0].isAssignableFrom(event.getClass())
            )
            .forEach(method -> {
                try {
                    Object instance = null;
                    if (!Modifier.isStatic(method.getModifiers())) {
                        instance = getOrCreate(method.getDeclaringClass());
                    }

                    method.setAccessible(true);
                    method.invoke(instance, event);
                } catch (InstantiationException | InvocationTargetException e) {
                    log.error("Could not initialize a command: ", e);
                } catch (IllegalAccessException e) {
                    log.error("Could not access a command class: ", e);
                }
            });
    }

    public static void instantiateCommands(Set<Class<? extends ICommand>> classes, CommandRegistry registry) {
        classes.parallelStream().forEach(c -> {
            try {
                ICommand instance = null;

                try {
                    instance = (ICommand) instances.get(c);
                } catch (ClassCastException e) {
                    log.error("Illegal instance {} detected. Removing and re-instantianting...", instances.remove(c));
                }

                if (instance == null) {
                    instance = c.newInstance();
                    instances.put(c, instance);
                }

                Command meta = c.getAnnotation(Command.class);

                if (meta == null) {
                    log.error("Command {} isn't annotated by @{} annotation", instance, Command.class.getSimpleName());
                    return;
                }

                for (String k : meta.value()) {
                    registry.register(k, instance);
                }
            } catch (InstantiationException e) {
                log.error("Could not initialize a command: ", e);
            } catch (IllegalAccessException e) {
                log.error("Could not access a command class: ", e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> T getOrCreate(Class<T> c) throws IllegalAccessException, InstantiationException {
        T obj = (T) instances.get(c);

        if (obj == null) {
            obj = c.newInstance();
            instances.put(c, obj);
        }

        return obj;
    }
}
