package com.theorangehub.hbdvbot.commands;

import com.theorangehub.hbdvbot.HbdvBot;
import com.theorangehub.hbdvbot.commands.ficha.Atributo;
import com.theorangehub.hbdvbot.commands.ficha.FichaBaseCommand;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.utils.HbdvUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.util.Optional;

import static com.theorangehub.hbdvbot.HbdvCommons.HBDV_COLOR;

//atribs <FICHA> <ATRIBUTO> <AÇÃO> [QUANTIA]
/*
!atribs David sorte +1
!atribs Anna força -1
!atribs Tatsu sorte set 0
*/
@Command("atribs")
public class AtribsCmd extends FichaBaseCommand {
    public AtribsCmd() {
        super(CommandPermission.ADMINISTRADOR);
    }

    @Override
    protected void handle(GuildMessageReceivedEvent event, Ficha ficha, String[] args) throws Exception {
        if (args.length < 2) {
            onHelp(event);
            return;
        }

        Atributo atributo;
        try {
            atributo = Atributo.valueOf(args[0]);
        } catch (IllegalArgumentException ignored) {
            event.getChannel().sendMessage("\"" + args[1] + "\" não é um atributo válido!").queue();
            return;
        }

        String action = args[1];

        //region increment
        if (action.equals("incrementar") || action.equals("++") || action.equals("+1")) {
            modificar(event, ficha, atributo, +1);
            return;
        }
        //endregion

        //region decrement
        if (action.equals("decrementar") || action.equals("--") || action.equals("-1")) {
            modificar(event, ficha, atributo, -1);
            return;
        }
        //endregion

        if (args.length < 3) {
            onHelp(event);
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException ignored) {
            event.getChannel().sendMessage("\"" + args[3] + "\" não é um número válido!").queue();
            return;
        }

        //region add
        if (action.equals("adicionar") || action.equals("+=")) {
            modificar(event, ficha, atributo, amount);
            return;
        }
        //endregion

        //region subtract
        if (action.equals("subtrair") || action.equals("-=")) {
            modificar(event, ficha, atributo, -amount);
            return;
        }
        //endregion

        //region set
        if (action.equals("definir") || action.equals("set") || action.equals("=")) {
            definir(event, ficha, atributo, amount);
            return;
        }
        //endregion
    }

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return null;
    }

    private void definir(GuildMessageReceivedEvent event, Ficha ficha, Atributo atributo, Number valor) {
        Number valorAntigo = atributo.get(ficha);
        atributo.set(ficha, valor);
        ficha.save();
        Number valorNovo = atributo.get(ficha);

        event.getChannel().sendMessage(embed(ficha, atributo, valorAntigo, valorNovo, true)).queue();
    }

    private MessageEmbed embed(Ficha ficha, Atributo atributo, Number valorAntigo, Number valorNovo, boolean definido) {
        return new EmbedBuilder()
            .setAuthor(
                "Ficha: " + ficha.getNome(),
                null,
                HbdvBot.getInstance().getSelfUser().getEffectiveAvatarUrl()
            )
            .setThumbnail(ficha.getAvatar())
            .setDescription(
                "**Atributo " + HbdvUtils.capitalize(atributo.name()) + " " + (definido ? "definido" : "modificado") + "**\n" +
                    "**Valor Antigo**: " + valorAntigo + "\n" +
                    "**Valor Novo**: " + valorNovo
            )
            .setColor(ficha.getCor() != null ? Color.decode(ficha.getCor()) : HBDV_COLOR)
            .setFooter(
                "ID: " + ficha.getId() + " / Criado por: " + ficha.getNomeCriador(),
                Optional.ofNullable(HbdvBot.getInstance().getUserById(ficha.getCriador()))
                    .map(User::getEffectiveAvatarUrl)
                    .orElse(null)
            ).build();
    }

    private void modificar(GuildMessageReceivedEvent event, Ficha ficha, Atributo atributo, Number offset) {
        Number valorAntigo = atributo.get(ficha);
        atributo.set(ficha, valorAntigo.doubleValue() + offset.doubleValue());
        ficha.save();
        Number valorNovo = atributo.get(ficha);

        event.getChannel().sendMessage(embed(ficha, atributo, valorAntigo, valorNovo, false)).queue();
    }
}
