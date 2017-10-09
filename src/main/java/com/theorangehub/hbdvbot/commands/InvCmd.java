package com.theorangehub.hbdvbot.commands;

import com.theorangehub.hbdvbot.commands.ficha.FichaBaseCommand;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

//inv <FICHA> <AÇÃO> [VALOR]
/*
!inv David add Faca
!inv Anna rm 1
!inv Tatsu clear
*/
@Command("inv")
public class InvCmd extends FichaBaseCommand {
    public InvCmd() {
        super(CommandPermission.ADMINISTRADOR);
    }

    @Override
    protected void handle(GuildMessageReceivedEvent event, Ficha ficha, String[] args) throws Exception {
        String action = args[0];
        //region clear
        if (action.equals("limpar") || action.equals("clear")) {
            ficha.getInventário().clear();
            event.getChannel().sendMessage(EmoteReference.CORRECT + "Inventário limpo!").queue();
            return;
        }
        //endregion

        if (args.length < 2) {
            onHelp(event);
            return;
        }

        String param = args[1];

        //region add
        if (action.equals("adicionar") || action.equals("add")) {
            ficha.getInventário().add(param);
            event.getChannel().sendMessage(EmoteReference.CORRECT + "Item adicionado!").queue();
            return;
        }
        //endregion

        //region rm
        if (action.equals("remover") || action.equals("rm")) {
            int index;
            try {
                index = Integer.parseInt(param);
            } catch (NumberFormatException e) {
                event.getChannel().sendMessageFormat(EmoteReference.ERROR + "`" + param + "` não é um número válido!").queue();
                return;
            }

            List<String> inv = ficha.getInventário();

            if (index < 1 || index > inv.size()) {
                event.getChannel().sendMessageFormat(EmoteReference.ERROR + "O item #" + index + " não existe!").queue();
            }

            event.getChannel().sendMessage(EmoteReference.CORRECT + "Item `" + inv.remove(index - 1) + "` removido!").queue();
            return;
        }
        //endregion

        onHelp(event);
    }

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return null;
    }
}
