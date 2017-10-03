package com.theorangehub.hbdvbot.commands;

import com.theorangehub.dml.DML;
import com.theorangehub.dml.DMLBuilder;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.db.ManagedDatabase;
import com.theorangehub.hbdvbot.data.entities.WikiArtigo;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.NoArgsCommand;
import com.theorangehub.hbdvbot.modules.commands.SimpleCommand;
import com.theorangehub.hbdvbot.utils.DiscordUtils;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

import static br.com.brjdevs.java.utils.strings.StringUtils.SPLIT_PATTERN;
import static br.com.brjdevs.java.utils.strings.StringUtils.efficientSplitArgs;

@SuppressWarnings("Duplicates")
@Module
public class WikiCmd {
    @Event
    public static void wiki(CommandRegistry registry) {
        registry.register("wiki", new SimpleCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                switch (args[0]) {
                    case "get": {
                        get(event, args[1]);
                        return;
                    }
                    case "search": {
                        search(event, args[1]);
                        return;
                    }
                    case "last":
                    case "latest": {
                        latest(event);
                        return;
                    }
                }

                onHelp(event);
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }

            private void get(GuildMessageReceivedEvent event, String content) {
                List<WikiArtigo> artigos = HbdvData.db().getWikiByPageId(content);

                if (artigos.isEmpty()) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhum Artigo com esse link."
                    ).queue();
                    return;
                }

                handleSelection(event, artigos, null);
            }

            private void handle(GuildMessageReceivedEvent event, WikiArtigo artigo) {
                event.getChannel().sendMessage(
                    DML.parse(new DMLBuilder() {
                        @Override
                        protected EmbedBuilder newEmbedBuilder() {
                            return baseEmbed(event, "#HBDVRPG Wiki");
                        }
                    }, artigo.getPage()).build()
                ).queue();
            }

            private void handleSelection(GuildMessageReceivedEvent event, List<WikiArtigo> artigos, String content) {
                if (artigos.isEmpty()) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhum Artigo disponível."
                    ).queue();
                    return;
                }

                if (artigos.size() == 1) {
                    //event.getChannel().sendMessage(page(event, fichas.get(0))).queue();
                    handle(event, artigos.get(0));
                    return;
                }

                EmbedBuilder builder = baseEmbed(event, "Selecione o Artigo:");

                int i = 0;
                for (WikiArtigo artigo : artigos) {
                    i++;

                    MessageEmbed embed = DML.parse(new DMLBuilder() {
                        @Override
                        protected EmbedBuilder newEmbedBuilder() {
                            return super.newEmbedBuilder()
                                .setColor(event.getMember().getColor())
                                .setFooter("Requerido por " + event.getMember().getEffectiveName(), event.getAuthor().getEffectiveAvatarUrl());
                        }
                    }, artigo.getPage()).buildEmbed();

                    String title = embed.getAuthor() != null && embed.getAuthor().getName() != null ? embed.getAuthor().getName() :
                        embed.getTitle() != null ? embed.getTitle() : "Sem Título";

                    builder.addField("[" + i + "] " + title, searchHighlighting(content, pseudoDescription(embed)), true);

                    if (i > 6) break;
                }

                DiscordUtils.selectList(
                    event,
                    artigos.subList(0, i),
                    builder.build(),
                    artigo -> handle(event, artigo)
                );
            }

            private void latest(GuildMessageReceivedEvent event) {
                List<WikiArtigo> artigos = HbdvData.db().getLastWiki();

                handleSelection(event, artigos, null);
            }

            private void search(GuildMessageReceivedEvent event, String content) {
                List<WikiArtigo> artigos = HbdvData.db().searchWiki(DiscordUtils.stripFormatting(content));
                artigos.sort(Comparator.comparing(WikiArtigo::getId).reversed());

                if (artigos.isEmpty()) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhum Artigo com o nome \"" + content + "\"."
                    ).queue();
                    return;
                }

                handleSelection(event, artigos, content);
            }

            @Override
            protected String[] splitArgs(String content) {
                return efficientSplitArgs(content, 2);
            }
        });
    }

    @Event
    public static void wikiadd(CommandRegistry registry) {
        registry.register("wikiadd", new SimpleCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                if (args.length < 2) {
                    onHelp(event);
                    return;
                }

                WikiArtigo artigo = new WikiArtigo();
                artigo.setId(String.valueOf(ManagedDatabase.ID_WORKER.generate()));
                artigo.setPage(args[1]);
                artigo.setPageId(args[0]);
                artigo.save();

                event.getChannel().sendMessage(
                    DML.parse(new DMLBuilder() {
                        @Override
                        protected MessageBuilder newMessageBuilder() {
                            return super.newMessageBuilder()
                                .append(EmoteReference.CORRECT).append("**Artigo publicado!**\n\n")
                                .append("**ID**: `").append(artigo.getId()).append("`\n")
                                .append("**Preview**:");
                        }

                        @Override
                        protected EmbedBuilder newEmbedBuilder() {
                            return baseEmbed(event, "#HBDVRPG Wiki");
                        }
                    }, artigo.getPage()).build()
                ).queue();
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }

            @Override
            protected String[] splitArgs(String content) {
                return SPLIT_PATTERN.split(content, 2);
            }
        });
    }

    @Event
    public static void wikirm(CommandRegistry registry) {
        registry.register("wikirm", new NoArgsCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content) {
                List<WikiArtigo> artigos = HbdvData.db().getWikiByPageId(content);

                if (artigos == null) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhum Artigo com esse link."
                    ).queue();
                    return;
                }

                handleSelection(event, artigos, content);
            }

            private void handle(GuildMessageReceivedEvent event, WikiArtigo artigo) {
                artigo.delete();
                event.getChannel().sendMessage(EmoteReference.CORRECT + "Artigo `" + artigo.getId() + "` removido.").queue();
            }

            private void handleSelection(GuildMessageReceivedEvent event, List<WikiArtigo> artigos, String content) {
                if (artigos.isEmpty()) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhum Artigo disponível."
                    ).queue();
                    return;
                }

                if (artigos.size() == 1) {
                    //event.getChannel().sendMessage(page(event, fichas.get(0))).queue();
                    handle(event, artigos.get(0));
                    return;
                }

                EmbedBuilder builder = baseEmbed(event, "Selecione o Artigo:");

                int i = 0;
                for (WikiArtigo artigo : artigos) {
                    i++;

                    MessageEmbed embed = DML.parse(new DMLBuilder() {
                        @Override
                        protected EmbedBuilder newEmbedBuilder() {
                            return super.newEmbedBuilder()
                                .setColor(event.getMember().getColor())
                                .setFooter("Requerido por " + event.getMember().getEffectiveName(), event.getAuthor().getEffectiveAvatarUrl());
                        }
                    }, artigo.getPage()).buildEmbed();

                    String title = embed.getAuthor() != null && embed.getAuthor().getName() != null ? embed.getAuthor().getName() :
                        embed.getTitle() != null ? embed.getTitle() : "Sem Título";

                    builder.addField("[" + i + "] " + title, searchHighlighting(content, pseudoDescription(embed)), true);

                    if (i > 6) break;
                }

                DiscordUtils.selectList(
                    event,
                    artigos.subList(0, i),
                    builder.build(),
                    artigo -> handle(event, artigo)
                );
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }
        });
    }

    private static String near(String string, int index, int size, int range) {
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

    private static String pseudoDescription(MessageEmbed embed) {
        StringJoiner joiner = new StringJoiner("\n");

        if (embed.getDescription() != null) {
            joiner.add(embed.getDescription());
        }

        for (Field field : embed.getFields()) {
            if (field.getName() != null) {
                joiner.add(field.getName());
            }

            if (field.getValue() != null) {
                joiner.add(field.getValue());
            }
        }

        if (embed.getFooter() != null && embed.getFooter().getText() != null) {
            joiner.add(embed.getFooter().getText());
        }

        return joiner.toString();
    }

    private static String searchHighlighting(String content, String desc) {
        desc = DiscordUtils.stripFormatting(desc);
        int index = content == null ? -1 : desc.indexOf(content);

        if (index == -1) {
            if (desc.length() <= 256) return desc;
            return desc.substring(253) + "...";
        }

        return StringUtils.replace(near(desc, index, content.length(), 256), content, "**" + content + "**");
    }
}
