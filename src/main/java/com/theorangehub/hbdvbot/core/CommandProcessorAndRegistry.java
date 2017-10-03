package com.theorangehub.hbdvbot.core;

import com.rethinkdb.gen.exc.ReqlError;
import com.theorangehub.dml.SyntaxException;
import com.theorangehub.hbdvbot.commands.info.helpers.CommandStatsManager;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.commands.base.ICommand;
import com.theorangehub.hbdvbot.utils.Snow64;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static br.com.brjdevs.java.utils.strings.StringUtils.splitArgs;

@Slf4j
public class CommandProcessorAndRegistry implements CommandRegistry {
    @Getter
    private static int commandCount = 0;

    private final Map<String, ICommand> commands = new HashMap<>();

    @Override
    public Map<String, ICommand> commands() {
        return commands;
    }

    @Override
    public void register(String s, ICommand c) {
        commands.putIfAbsent(s, c);
    }

    public void run(GuildMessageReceivedEvent event) {
        String rawCmd = event.getMessage().getRawContent();
        String prefix = HbdvData.config().get().prefix;

        if (!rawCmd.startsWith(prefix)) return;

        rawCmd = rawCmd.substring(prefix.length());

        String[] parts = splitArgs(rawCmd, 2);
        String cmdName = parts[0], content = parts[1];

        ICommand cmd = commands.get(cmdName);
        if (cmd == null) return;

        if (!cmd.permission().test(event.getMember())) {
            event.getChannel().sendMessage(EmoteReference.STOP + "Você não tem permissão para usar esse comando.")
                .queue();
            return;
        }

        commandCount++;

        try {
            cmd.run(event, cmdName, content);
        } catch (SyntaxException e) {
            event.getChannel().sendMessage(
                EmoteReference.ERROR + "Aparentemente alguma página da Wiki pegou fogo... " + EmoteReference.THINKING
            ).queue();
            log.warn("DML TÁ PEGANDO FOGO BIXO.\n", e);
        } catch (ReqlError e) {
            event.getChannel().sendMessage(
                EmoteReference.ERROR + "Aparentemente nosso banco de dados pegou fogo... " + EmoteReference.THINKING
            ).queue();
            log.warn("<@217747278071463937> RethinkDB TÁ PEGANDO FOGO BIXO.\n", e);
        } catch (Exception e) {
            String id = Snow64.toSnow64(event.getMessage().getIdLong());

            event.getChannel().sendMessage(
                EmoteReference.ERROR + "Erro durante a Execução. (Error ID: ``" + id + "``)"
            ).queue();

            log.warn("Unexpected Exception on Command ``"
                + event.getMessage().getRawContent()
                + "`` (Error ID: ``" + id + "``)", e
            );
        }

        CommandStatsManager.log(cmdName);

        log.trace(
            "Command invoked: {}, by {}#{} with timestamp {}",
            cmdName,
            event.getAuthor().getName(),
            event.getAuthor().getDiscriminator(),
            new Date(System.currentTimeMillis())
        );
    }
}