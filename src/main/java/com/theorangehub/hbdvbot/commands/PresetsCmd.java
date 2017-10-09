package com.theorangehub.hbdvbot.commands;

import com.theorangehub.hbdvbot.commands.ficha.Atributo;
import com.theorangehub.hbdvbot.commands.ficha.FichaBaseCommand;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.Dados;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.data.entities.helper.Const;
import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.utils.HbdvUtils;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import gnu.trove.list.TIntList;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//presets <FICHA> <AÇÃO> [MODIFICADORES...]
/*
!tentar David criarmagia
!tentar Zac receberdano --resistência 8 --dado 12
!tentar Tatsu sorte set 0
*/
@Command("presets")
public class PresetsCmd extends FichaBaseCommand {
    @Override
    protected void handle(GuildMessageReceivedEvent event, Ficha ficha, String[] args) {
        String action = args[0];
        String[] extraArgs = args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);

        Dados dados = HbdvData.db().getDadosPorId(action);

        if (dados == null) {
            event.getChannel().sendMessage(
                EmoteReference.ERROR + "Nenhuma Ação com o Nome/ID de \"" + action + "\""
            ).queue();

            return;
        }

        if (extraArgs.length != 0) {
            for (Entry<String, String> entries : HbdvUtils.parse(extraArgs).entrySet()) {
                String k = entries.getKey(), v = entries.getValue();

                if (k == null || v == null) continue;
                k = k.startsWith("-") ? k.substring(1) : k;

                Atributo atributo = null;

                try {
                    atributo = Atributo.valueOf(k);
                } catch (IllegalArgumentException ignored) {}

                if (atributo != null) {
                    try {
                        atributo.set(ficha, Double.parseDouble(v));
                    } catch (NumberFormatException ignored) {}
                } else {
                    try {
                        dados.getConsts().put(k, new Const().setValue(Double.valueOf(v)).setType("const"));
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        TIntList resultados = dados.calculate(ficha);

        event.getChannel().sendMessage(
            EmoteReference.DICE + "**Resultados**: " + IntStream.of(resultados.toArray())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "))
        ).queue();
    }

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return null;
    }
}
