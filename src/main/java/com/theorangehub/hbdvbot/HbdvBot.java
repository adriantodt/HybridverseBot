package com.theorangehub.hbdvbot;

import br.com.brjdevs.java.utils.extensions.Async;
import br.com.brjdevs.java.utils.extensions.CollectionUtils;
import com.theorangehub.hbdvbot.core.init.BotInitializer;
import com.theorangehub.hbdvbot.core.listeners.command.CommandListener;
import com.theorangehub.hbdvbot.core.listeners.operations.InteractiveOperation;
import com.theorangehub.hbdvbot.core.listeners.operations.ReactionOperation;
import com.theorangehub.hbdvbot.data.Config;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.log.DiscordLogBack;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static br.com.brjdevs.java.utils.extensions.CollectionUtils.random;
import static com.theorangehub.hbdvbot.HbdvCommons.*;

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
        return DEV_MODE;
    }

    public static void main(String[] args) {
        try {
            new HbdvBot();
        } catch (Exception e) {
            DiscordLogBack.disable();
            log.error("Erro durante inicialização!", e);
            log.error("Não é possível continuar, abortando...");
            System.exit(-1);
        }
    }

    @Delegate
    @Getter
    private JDA jda;

    private HbdvBot() throws Exception {
        instance = this;
        log.info("HbdvBot iniciando...");

        Config config = HbdvData.config().get();

        BotInitializer init = new BotInitializer();
        Async.thread(init::makeCommands);
        Async.thread(init::makeModules);

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

        EventDispatcher.dispatchInvocations(init.getCommands(), getRegistry());
        EventDispatcher.dispatchEvents(init.getMethods(), getRegistry());

        log.info("Finalizado.");
        log.info("[-=-=-=-=-=- INICIALIZAÇÃO  2 -=-=-=-=-=-]");

        EventDispatcher.dispatchEvents(init.getMethods(), new PostLoadEvent());

        jda.addEventListener(
            new CommandListener(),
            InteractiveOperation.listener(),
            ReactionOperation.listener()
        );

        log.info("Finalizado; {} comandos carregados.", CommandListener.PROCESSOR.commands().size());

        //Free Instances
        EventDispatcher.instances.clear();
    }
}
