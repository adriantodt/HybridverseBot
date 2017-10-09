package com.theorangehub.hbdvbot.commands;

import com.theorangehub.hbdvbot.commands.ficha.FichaBaseCommand;
import com.theorangehub.hbdvbot.commands.ficha.FichaHandler;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.Command;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

@Command("ficha")
public class FichaCmd extends FichaBaseCommand {

    @Override
    protected void handle(GuildMessageReceivedEvent event, Ficha ficha, String[] args) {
        FichaHandler.handleFicha(event, ficha);
    }

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return helpBuilder(event, "Fichas")
            .descrição("Procura e Mostra uma ficha")
            .uso("ficha <nome>", "Procura uma ficha com esse nome.")
            .build();
    }
}
