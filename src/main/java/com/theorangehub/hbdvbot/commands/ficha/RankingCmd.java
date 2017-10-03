package com.theorangehub.hbdvbot.commands.ficha;

import br.com.brjdevs.java.utils.extensions.StreamUtils;
import com.rethinkdb.net.Cursor;
import com.theorangehub.hbdvbot.commands.ficha.helpers.Atributo;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.commands.SimpleCommand;
import com.theorangehub.hbdvbot.utils.HbdvUtils;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import static com.rethinkdb.RethinkDB.r;

@Command("ranking")
public class RankingCmd extends SimpleCommand {
    @Override
    protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
        Atributo atributo;

        try {
            atributo = Atributo.valueOf(content);
        } catch (IllegalArgumentException ignored) {
            event.getChannel().sendMessage("\"" + content + "\" não é um atributo válido!").queue();
            return;
        }

        Cursor<Ficha> cursor = r.table("fichas").orderBy().optArg("index", r.desc(atributo.name())).run(HbdvData.conn(), Ficha.class);
        List<Ficha> fichas = cursor.toList();

        Iterator<String> values = fichas.stream()
            .map(StreamUtils.index())
            .map(ficha -> {
                Ficha f = ficha.getValue();
                int i = ficha.getIndex();
                Number value = atributo.get(f);

                if (value instanceof Double || value instanceof Float) {
                    return String.format("%d. **%s**: %,.2f", i + 1, f.getNome(), value.doubleValue());
                }

                return String.format("%d. **%s**: %,d", i + 1, f.getNome(), value.longValue());
            })
            .iterator();

        StringJoiner joiner = new StringJoiner("\n");

        while (values.hasNext()) {
            String next = values.next();
            if (joiner.length() + 1 + next.length() < 2000) {
                joiner.add(next);
                continue;
            }

            break;
        }

        String avg = String.format("%,.2f", fichas.stream().mapToDouble(value -> atributo.get(value).doubleValue()).average().orElse(0));

        event.getChannel().sendMessage(
            baseEmbed(event, "Ranking: " + HbdvUtils.capitalize(atributo.name()))
                .setDescription(joiner.toString())
                .setFooter(
                    "Média: " + avg + " / Requerido por " + event.getMember().getEffectiveName(),
                    event.getAuthor().getEffectiveAvatarUrl()
                )
                .build()
        ).queue();
    }

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return null;
    }
}
