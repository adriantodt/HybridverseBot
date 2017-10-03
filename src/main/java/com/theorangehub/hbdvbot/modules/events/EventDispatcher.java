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

@Slf4j
public class EventDispatcher {
    public static Map<Class<?>, Object> instances = new HashMap<>();

    public static void dispatchEvents(Set<Method> methods, Object event) {
        methods.parallelStream().filter(method -> method.getParameterCount() == 1 && method.getParameterTypes()[0]
            .isAssignableFrom(event.getClass()))
            .forEach(method -> {
                try {
                    Object instance = null;
                    if (!Modifier.isStatic(method.getModifiers())) {
                        instance = instances.get(method.getDeclaringClass());

                        if (instance == null) {
                            instance = method.getDeclaringClass().newInstance();
                            instances.put(method.getDeclaringClass(), instance);
                        }
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

    public static void dispatchInvocations(Set<Class<? extends ICommand>> classes, CommandRegistry registry) {
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
}
