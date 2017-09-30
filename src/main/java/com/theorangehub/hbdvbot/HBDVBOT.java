package com.theorangehub.hbdvbot;

import br.com.brjdevs.java.utils.extensions.Async;
import com.theorangehub.hbdvbot.core.listeners.command.CommandListener;
import com.theorangehub.hbdvbot.core.listeners.operations.InteractiveOperation;
import com.theorangehub.hbdvbot.core.listeners.operations.ReactionOperation;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.Config;
import com.theorangehub.hbdvbot.log.DiscordLogBack;
import com.theorangehub.hbdvbot.log.SimpleLogToSLF4JAdapter;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.events.EventDispatcher;
import com.theorangehub.hbdvbot.modules.events.PostLoadEvent;
import com.theorangehub.hbdvbot.utils.data.DataManager;
import com.theorangehub.hbdvbot.utils.data.SimpleFileDataManager;
import lombok.Getter;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Game;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static br.com.brjdevs.java.utils.extensions.CollectionUtils.random;

@Slf4j
public class HBDVBOT implements JDA {
    public static final DataManager<List<String>> SPLASHES = new SimpleFileDataManager("assets/splashes.txt");
    public static final String VERSION = "@version@";
    private static final Random RANDOM = new Random();

    @Getter
    private static HBDVBOT instance;

    public static CommandRegistry getRegistry() {
        return CommandListener.PROCESSOR;
    }

    public static void main(String[] args) {
        try {
            new HBDVBOT();
        } catch (Exception e) {
            DiscordLogBack.disable();
            log.error("Could not complete Main Thread routine!", e);
            log.error("Cannot continue! Exiting program...");
            System.exit(-1);
        }
    }

    @Delegate
    @Getter
    private JDA jda;

    private HBDVBOT() throws Exception {
        instance = this;

        SimpleLogToSLF4JAdapter.install();
        log.info("HybridverseBot starting...");

        Config config = HbdvData.config().get();

        //Let's see if we find a class.
        Future<Set<Class<?>>> classes = Async.future("Classes Lookup", () ->
            new Reflections(
                "com.theorangehub.hbdvbot.commands",
                new MethodAnnotationsScanner(),
                new TypeAnnotationsScanner(),
                new SubTypesScanner()
            ).getTypesAnnotatedWith(Module.class)
        );

        jda = new JDABuilder(AccountType.BOT)
            .setToken(config.token)
            .setAutoReconnect(true)
            .setCorePoolSize(5)
            .setGame(Game.of("Hold on to your seatbelts!"))
            .buildBlocking();

        DiscordLogBack.enable();
        log.info("[-=-=-=-=-=- HBDVBOT STARTED -=-=-=-=-=-]");
        log.info("Started bot instance.");
        log.info("Started HybridverseBot " + VERSION + " on JDA " + JDAInfo.VERSION);

        Async.task("Splash Thread",
            () -> {
                String newStatus = random(SPLASHES.get(), RANDOM);

                jda.getPresence().setGame(Game.of(config.prefix + "help | " + newStatus));
                log.debug("Changed status to: " + newStatus);
            }, 1, TimeUnit.MINUTES
        );

        HbdvData.config().save();

        Set<Method> events = new Reflections(
            classes.get(),
            new MethodAnnotationsScanner()
        ).getMethodsAnnotatedWith(Event.class);

        EventDispatcher.dispatch(events, getRegistry());

        log.info("Finished loading.");

        EventDispatcher.dispatch(events, new PostLoadEvent());

        jda.addEventListener(
            new CommandListener(),
            InteractiveOperation.listener(),
            ReactionOperation.listener()
        );

        log.info("Loaded " + CommandListener.PROCESSOR.commands().size() + " commands.");

        //Free Instances
        EventDispatcher.instances.clear();
    }
}
