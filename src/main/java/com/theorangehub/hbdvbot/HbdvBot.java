package com.theorangehub.hbdvbot;

import br.com.brjdevs.java.utils.extensions.Async;
import br.com.brjdevs.java.utils.extensions.CollectionUtils;
import com.theorangehub.hbdvbot.core.listeners.command.CommandListener;
import com.theorangehub.hbdvbot.core.listeners.operations.InteractiveOperation;
import com.theorangehub.hbdvbot.core.listeners.operations.ReactionOperation;
import com.theorangehub.hbdvbot.data.Config;
import com.theorangehub.hbdvbot.data.HbdvData;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static br.com.brjdevs.java.utils.extensions.CollectionUtils.random;
import static com.theorangehub.hbdvbot.HbdvCommons.BOOT_QUOTES;
import static com.theorangehub.hbdvbot.HbdvCommons.RANDOM;

@Slf4j
public class HbdvBot implements JDA {
    public static final DataManager<List<String>> SPLASHES = new SimpleFileDataManager("assets/splashes.txt");
    @Getter
    private static HbdvBot instance;

    public static CommandRegistry getRegistry() {
        return CommandListener.PROCESSOR;
    }

    public static String getVersion() {
        if (isDevBuild()) {
            return "DEV" + new SimpleDateFormat("ddMMyyyy").format(new Date());
        }

        return "@version@";
    }

    public static boolean isDevBuild() {
        return Boolean.parseBoolean("@false@".replace("@", ""));
    }

    public static void main(String[] args) {
        try {
            new HbdvBot();
        } catch (Exception e) {
            DiscordLogBack.disable();
            log.error("Erro durante inicialização!", e);
            log.error("Não é possível continuar, desligando...");
            System.exit(-1);
        }
    }

    @Delegate
    @Getter
    private JDA jda;

    private HbdvBot() throws Exception {
        instance = this;

        SimpleLogToSLF4JAdapter.install();
        log.info("HbdvBot iniciando...");

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
            .setGame(Game.of(CollectionUtils.random(BOOT_QUOTES, RANDOM)))
            .buildBlocking();

        DiscordLogBack.enable();
        log.info("[-=-=-=-=-=- HBDVBOT INICIADO -=-=-=-=-=-]");
        log.info("HbdvBot v" + getVersion() + " (JDA " + JDAInfo.VERSION + ") iniciado.");

        log.info("[-=-=-=-=-=- INICIALIZAÇÃO  1 -=-=-=-=-=-]");

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

        log.info("Finalizada.");
        log.info("[-=-=-=-=-=- INICIALIZAÇÃO  2 -=-=-=-=-=-]");

        EventDispatcher.dispatch(events, new PostLoadEvent());

        jda.addEventListener(
            new CommandListener(),
            InteractiveOperation.listener(),
            ReactionOperation.listener()
        );

        log.info("Finalizada. {} comandos carregados.", CommandListener.PROCESSOR.commands().size());

        //Free Instances
        EventDispatcher.instances.clear();
    }
}
