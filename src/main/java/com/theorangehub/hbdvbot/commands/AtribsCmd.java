package com.theorangehub.hbdvbot.commands;

import br.com.brjdevs.java.utils.extensions.StreamUtils;
import br.com.brjdevs.java.utils.strings.StringUtils;
import com.rethinkdb.net.Cursor;
import com.theorangehub.hbdvbot.HbdvBot;
import com.theorangehub.hbdvbot.commands.atrib.Atributo;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import static com.rethinkdb.RethinkDB.r;
import static com.theorangehub.hbdvbot.commands.ficha.FichaEmbeds.defaultColor;

@Module
public class AtribsCmd {
    static class Handlers {
        static void definir(GuildMessageReceivedEvent event, Ficha ficha, Atributo atributo, Number valor) {
            Number valorAntigo = atributo.get(ficha);
            atributo.set(ficha, valor);
            ficha.save();
            Number valorNovo = atributo.get(ficha);

            event.getChannel().sendMessage(embed(ficha, atributo, valorAntigo, valorNovo, true)).queue();
        }

        static MessageEmbed embed(Ficha ficha, Atributo atributo, Number valorAntigo, Number valorNovo, boolean definido) {
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
                .setColor(ficha.getCor() != null ? Color.decode(ficha.getCor()) : defaultColor)
                .setFooter(
                    "ID: " + ficha.getId() + " / Criado por: " + ficha.getNomeCriador(),
                    Optional.ofNullable(HbdvBot.getInstance().getUserById(ficha.getCriador()))
                        .map(User::getEffectiveAvatarUrl)
                        .orElse(null)
                ).build();
        }

        static void modificar(GuildMessageReceivedEvent event, Ficha ficha, Atributo atributo, Number offset) {
            Number valorAntigo = atributo.get(ficha);
            atributo.set(ficha, valorAntigo.doubleValue() + offset.doubleValue());
            ficha.save();
            Number valorNovo = atributo.get(ficha);

            event.getChannel().sendMessage(embed(ficha, atributo, valorAntigo, valorNovo, false)).queue();
        }
    }

    @Event
    public static void atribs(CommandRegistry registry) {

        //atribs <FICHA> <ATRIBUTO> <AÇÃO> [QUANTIA]
        /*
        !atribs David sorte +1
		!atribs Anna força -1
		!atribs Tatsu sorte set 0
		*/
        registry.register("atribs", new SimpleCommand(CommandPermission.ADMIN) {
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
                    Ficha ficha = HbdvData.db().getFichaPorId(selector);

                    if (ficha == null) {
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
                                f -> Handlers.modificar(event, f, atributo, +1)
                            );

                            return;
                        }

                        ficha = fichas.get(0);
                    }

                    Handlers.modificar(event, ficha, atributo, +1);
                    return;
                }
                //endregion

                //region decrement
                if (action.equals("decrementar") || action.equals("--") || action.equals("-1")) {
                    Ficha ficha = HbdvData.db().getFichaPorId(selector);

                    if (ficha == null) {
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
                                f -> Handlers.modificar(event, f, atributo, -1)
                            );

                            return;
                        }

                        ficha = fichas.get(0);
                    }

                    Handlers.modificar(event, ficha, atributo, -1);
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
                    Ficha ficha = HbdvData.db().getFichaPorId(selector);

                    if (ficha == null) {
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
                                f -> Handlers.modificar(event, f, atributo, amount)
                            );

                            return;
                        }

                        ficha = fichas.get(0);
                    }

                    Handlers.modificar(event, ficha, atributo, amount);
                    return;
                }
                //endregion

                //region subtract
                if (action.equals("subtrair") || action.equals("-=")) {
                    Ficha ficha = HbdvData.db().getFichaPorId(selector);

                    if (ficha == null) {
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
                                f -> Handlers.modificar(event, f, atributo, -amount)
                            );

                            return;
                        }

                        ficha = fichas.get(0);
                    }

                    Handlers.modificar(event, ficha, atributo, -amount);
                    return;
                }
                //endregion

                //region set
                if (action.equals("definir") || action.equals("set") || action.equals("=")) {
                    Ficha ficha = HbdvData.db().getFichaPorId(selector);

                    if (ficha == null) {
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
                                f -> Handlers.definir(event, f, atributo, amount)
                            );

                            return;
                        }

                        ficha = fichas.get(0);
                    }

                    Handlers.definir(event, ficha, atributo, amount);
                    return;
                }
                //endregion
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }

            @Override
            protected String[] splitArgs(String content) {
                return StringUtils.efficientSplitArgs(content, 0);
            }

        });
    }

    @Event
    public static void ranking(CommandRegistry registry) {
        registry.register("ranking", new SimpleCommand() {
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
        });
    }
}
