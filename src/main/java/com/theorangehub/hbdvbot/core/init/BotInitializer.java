package com.theorangehub.hbdvbot.core.init;

import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.base.ICommand;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class BotInitializer {
    private CompletableFuture<Set<Class<? extends ICommand>>> commandsGetter = new CompletableFuture<>();
    private CompletableFuture<Set<Method>> methodsGetter = new CompletableFuture<>();
    private CompletableFuture<Reflections> reflectionGetter = new CompletableFuture<>();

    @SneakyThrows
    public Set<Class<? extends ICommand>> getCommands() {
        return commandsGetter.get();
    }

    @SneakyThrows
    public Set<Method> getMethods() {
        return methodsGetter.get();
    }

    public void makeCommands() {
        try {
            Reflections r = new Reflections("com.theorangehub.hbdvbot");

            reflectionGetter.complete(r);

            Set<Class<? extends ICommand>> cmds = r.getSubTypesOf(ICommand.class);

            cmds.removeIf(c -> !c.isAnnotationPresent(Command.class));

            commandsGetter.complete(cmds);
        } catch (Exception e) {
            reflectionGetter.completeExceptionally(e);
            commandsGetter.completeExceptionally(e);
        }
    }

    public void makeModules() {
        try {
            Reflections r = reflectionGetter.get();

            methodsGetter.complete(
                new Reflections(r.getTypesAnnotatedWith(Module.class), new MethodAnnotationsScanner())
                    .getMethodsAnnotatedWith(Event.class)
            );

        } catch (Exception e) {
            methodsGetter.completeExceptionally(e);
        }
    }
}
