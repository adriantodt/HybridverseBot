package com.theorangehub.hbdvbot.commands;

import br.com.brjdevs.java.utils.strings.StringUtils;
import com.theorangehub.hbdvbot.HbdvBot;
import com.theorangehub.hbdvbot.commands.atrib.Atributo;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.modules.commands.SimpleCommand;
import com.theorangehub.hbdvbot.utils.DiscordUtils;
import com.theorangehub.hbdvbot.utils.HbdvUtils;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import static com.theorangehub.hbdvbot.HbdvCommons.HBDV_COLOR;

//atribs <FICHA> <ATRIBUTO> <AÇÃO> [QUANTIA]
/*
!atribs David sorte +1
!atribs Anna força -1
!atribs Tatsu sorte set 0
*/
@Command("atribs")
public class AtribsCmd extends SimpleCommand {
    public AtribsCmd() {
        super(CommandPermission.ADMIN);
    }

    @Override
    protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
        if (args.length < 3) {
            onHelp(event);
            return;
        }

        String selector = args[0], action = args[2];
        Atributo atributo;

        try {
            atributo = Atributo.valueOf(args[1]);
        } catch (IllegalArgumentException ignored) {
            event.getChannel().sendMessage("\"" + args[1] + "\" não é um atributo válido!").queue();
            return;
        }

        //region increment
        if (action.equals("incrementar") || action.equals("++") || action.equals("+1")) {
            Ficha ficha;

            List<Ficha> fichas = HbdvData.db().getFichasPorNome(selector);

            if (fichas.isEmpty()) {
                event.getChannel().sendMessage(
                    EmoteReference.ERROR + "Nenhum Personagem com o Nome/ID de \"" + selector + "\""
                ).queue();

                return;
            }

            if (fichas.size() > 1) {
                DiscordUtils.selectList(event, fichas,
                    Ficha::displayToString,
                    s -> baseEmbed(event, "Selecione a Ficha:").setDescription(s).build(),
                    f -> modificar(event, f, atributo, +1)
                );

                return;
            }

            ficha = fichas.get(0);

            modificar(event, ficha, atributo, +1);
            return;
        }
        //endregion

        //region decrement
        if (action.equals("decrementar") || action.equals("--") || action.equals("-1")) {
            Ficha ficha;

            List<Ficha> fichas = HbdvData.db().getFichasPorNome(selector);

            if (fichas.isEmpty()) {
                event.getChannel().sendMessage(
                    EmoteReference.ERROR + "Nenhum Personagem com o Nome/ID de \"" + selector + "\""
                ).queue();

                return;
            }

            if (fichas.size() > 1) {
                DiscordUtils.selectList(event, fichas,
                    Ficha::displayToString,
                    s -> baseEmbed(event, "Selecione a Ficha:").setDescription(s).build(),
                    f -> modificar(event, f, atributo, -1)
                );

                return;
            }

            ficha = fichas.get(0);

            modificar(event, ficha, atributo, -1);
            return;
        }
        //endregion

        if (args.length < 4) {
            onHelp(event);
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[3]);
        } catch (NumberFormatException ignored) {
            event.getChannel().sendMessage("\"" + args[3] + "\" não é um número válido!").queue();
            return;
        }

        //region add
        if (action.equals("adicionar") || action.equals("+=")) {
            Ficha ficha;

            List<Ficha> fichas = HbdvData.db().getFichasPorNome(selector);

            if (fichas.isEmpty()) {
                event.getChannel().sendMessage(
                    EmoteReference.ERROR + "Nenhum Personagem com o Nome/ID de \"" + selector + "\""
                ).queue();

                return;
            }

            if (fichas.size() > 1) {
                DiscordUtils.selectList(event, fichas,
                    Ficha::displayToString,
                    s -> baseEmbed(event, "Selecione a Ficha:").setDescription(s).build(),
                    f -> modificar(event, f, atributo, amount)
                );

                return;
            }

            ficha = fichas.get(0);

            modificar(event, ficha, atributo, amount);
            return;
        }
        //endregion

        //region subtract
        if (action.equals("subtrair") || action.equals("-=")) {
            Ficha ficha;

            List<Ficha> fichas = HbdvData.db().getFichasPorNome(selector);

            if (fichas.isEmpty()) {
                event.getChannel().sendMessage(
                    EmoteReference.ERROR + "Nenhum Personagem com o Nome/ID de \"" + selector + "\""
                ).queue();

                return;
            }

            if (fichas.size() > 1) {
                DiscordUtils.selectList(event, fichas,
                    Ficha::displayToString,
                    s -> baseEmbed(event, "Selecione a Ficha:").setDescription(s).build(),
                    f -> modificar(event, f, atributo, -amount)
                );

                return;
            }

            ficha = fichas.get(0);

            modificar(event, ficha, atributo, -amount);
            return;
        }
        //endregion

        //region set
        if (action.equals("definir") || action.equals("set") || action.equals("=")) {
            Ficha ficha;

            List<Ficha> fichas = HbdvData.db().getFichasPorNome(selector);

            if (fichas.isEmpty()) {
                event.getChannel().sendMessage(
                    EmoteReference.ERROR + "Nenhum Personagem com o Nome/ID de \"" + selector + "\""
                ).queue();

                return;
            }

            if (fichas.size() > 1) {
                DiscordUtils.selectList(event, fichas,
                    Ficha::displayToString,
                    s -> baseEmbed(event, "Selecione a Ficha:").setDescription(s).build(),
                    f -> definir(event, f, atributo, amount)
                );

                return;
            }

            ficha = fichas.get(0);

            definir(event, ficha, atributo, amount);
            return;
        }
        //endregion
    }

    @Override
    protected String[] splitArgs(String content) {
        return StringUtils.efficientSplitArgs(content, 0);
    }

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return null;
    }

    void definir(GuildMessageReceivedEvent event, Ficha ficha, Atributo atributo, Number valor) {
        Number valorAntigo = atributo.get(ficha);
        atributo.set(ficha, valor);
        ficha.save();
        Number valorNovo = atributo.get(ficha);

        event.getChannel().sendMessage(embed(ficha, atributo, valorAntigo, valorNovo, true)).queue();
    }

    MessageEmbed embed(Ficha ficha, Atributo atributo, Number valorAntigo, Number valorNovo, boolean definido) {
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

    void modificar(GuildMessageReceivedEvent event, Ficha ficha, Atributo atributo, Number offset) {
        Number valorAntigo = atributo.get(ficha);
        atributo.set(ficha, valorAntigo.doubleValue() + offset.doubleValue());
        ficha.save();
        Number valorNovo = atributo.get(ficha);

        event.getChannel().sendMessage(embed(ficha, atributo, valorAntigo, valorNovo, false)).queue();
    }

}
