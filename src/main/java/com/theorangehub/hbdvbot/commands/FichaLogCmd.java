package com.theorangehub.hbdvbot.commands;

import com.theorangehub.hbdvbot.commands.ficha.FichaHandler;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.modules.commands.SimpleCommand;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

@Command("fichalog")
public class FichaLogCmd extends SimpleCommand {
    public FichaLogCmd() {
        super(CommandPermission.ADMIN);
    }

    @Override
    protected void call(GuildMessageReceivedEvent event, String content) {
        Ficha ficha = HbdvData.db().getFichaLog(content);

        if (ficha == null) {
            event.getChannel().sendMessage(
                EmoteReference.ERROR + "Nenhum Log com esse ID"
            ).queue();
            return;
        }

        //event.getChannel().sendMessage(embed(event, ficha)).queue();
        FichaHandler.handleFicha(event, ficha);
    }

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return helpBuilder(event, "Logs de Ficha")
            .descrição("Mostra a Ficha em uma versão específica")
            .uso("fichalog <id>", "Mostra a ficha dessa versão.")
            .build();
    }
}