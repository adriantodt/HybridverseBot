package com.theorangehub.hbdvbot.commands;

import com.rethinkdb.serial.SerialUtils;
import com.theorangehub.hbdvbot.commands.wiki.EmbedJSON;
import com.theorangehub.hbdvbot.commands.wiki.EmbedJSON.EmbedField;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.WikiArtigo;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.NoArgsCommand;
import com.theorangehub.hbdvbot.utils.DiscordUtils;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import lombok.SneakyThrows;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Module
public class WikiCmd {
    @Event
    public static void ficha(CommandRegistry registry) {
        registry.register("wiki", new NoArgsCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content) {
                List<WikiArtigo> artigos = HbdvData.db().searchWiki(DiscordUtils.stripFormatting(content));
                Set<WikiArtigo> sortSet = new TreeSet<>(Comparator.comparing(WikiArtigo::getId).reversed());
                sortSet.addAll(artigos);
                artigos.clear();
                artigos.addAll(sortSet);

                if (artigos.isEmpty()) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhum Artigo \"" + content + "\""
                    ).queue();
                    return;
                }

                if (artigos.size() == 1) {
                    //event.getChannel().sendMessage(embed(event, fichas.get(0))).queue();
                    handle(event, artigos.get(0));
                    return;
                }

                EmbedBuilder builder = baseEmbed(event, "Selecione o Artigo:");

                int i = 0;
                for (WikiArtigo artigo : artigos) {
                    i++;

                    EmbedJSON embed = unserialize(artigo);

                    String title = embed.getAuthor() != null ? embed.getAuthor() : embed.getTitle() != null ? embed.getTitle() : "Unnamed article";

                    builder.addField("[" + i + "] " + title, searchHighlighting(content, pseudoDescription(embed)), true);

                    if (i > 6) break;
                }

                DiscordUtils.selectList(
                    event,
                    artigos.subList(0, i), //TODO CHECK THIS
                    builder.build(),
                    artigo -> handle(event, artigo)
                );
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }

            private void handle(GuildMessageReceivedEvent event, WikiArtigo artigo) {
                event.getChannel().sendMessage(
                    unserialize(artigo).decorate(event, baseEmbed(event, "#HBDVRPG Wiki")).build()
                ).queue();
            }

            private String near(String string, int index, int size, int range) {
                int bot = index - range / 2;
                int top = index + size + range / 2;
                int nBot = Math.max(0, bot);
                int nTop = Math.min(string.length(), top);
                StringBuilder s = new StringBuilder();

                if (bot == nBot) {
                    s.append("...");
                }

                s.append(string.substring(nBot + (bot == nBot ? 3 : 0), nTop - (top == nTop ? 3 : 0)));

                if (top == nTop) {
                    s.append("...");
                }

                return s.toString();
            }

            private String nonNull(String s) {
                return s == null ? "" : s;
            }

            private String pseudoDescription(EmbedJSON embed) {
                StringJoiner joiner = new StringJoiner("\n");

                if (embed.getDescription() != null) {
                    joiner.add(nonNull(embed.getDescription()));
                }

                for (EmbedField field : embed.getFields()) {
                    joiner.add(nonNull(field.getName()));
                    joiner.add(nonNull(field.getValue()));
                }

                if (embed.getFooter() != null) {
                    joiner.add(nonNull(embed.getFooter()));
                }

                return joiner.toString();
            }

            private String searchHighlighting(String content, String desc) {
                desc = DiscordUtils.stripFormatting(desc);
                int index = desc.indexOf(content);

                if (index == -1) {
                    if (desc.length() <= 256) return desc;
                    return desc.substring(253) + "...";
                }

                return StringUtils.replace(near(desc, index, content.length(), 256), content, "**" + content + "**");
            }

            @SneakyThrows
            private EmbedJSON unserialize(WikiArtigo artigo) {
                return SerialUtils.getMapper().readValue("{" + artigo.getEmbed() + "}", EmbedJSON.class);
            }
        });
    }
}
