package com.theorangehub.hbdvbot.commands;

import com.theorangehub.hbdvbot.commands.ficha.FichaHandler;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.modules.commands.NoArgsCommand;
import com.theorangehub.hbdvbot.utils.DiscordUtils;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import com.theorangehub.hbdvbot.data.HbdvData;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Module
public class FichaCmd {

    @Event
    public static void ficha(CommandRegistry registry) {
        registry.register("ficha", new NoArgsCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content) {
                List<Ficha> fichas = HbdvData.db().getFichasPorNome(content);
                Set<Ficha> sortSet = new TreeSet<>(Comparator.comparing(Ficha::displayToString));
                sortSet.addAll(fichas);
                fichas.clear();
                fichas.addAll(sortSet);

                if (fichas.isEmpty()) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhum Personagem com o Nome de \"" + content + "\""
                    ).queue();
                    return;
                }

                if (fichas.size() == 1) {
                    //event.getChannel().sendMessage(embed(event, fichas.get(0))).queue();
                    FichaHandler.handleFicha(event, fichas.get(0));
                    return;
                }

                DiscordUtils.selectList(event, fichas,
                    Ficha::displayToString,
                    s -> baseEmbed(event, "Selecione a Ficha:").setDescription(s).build(),
                    ficha -> FichaHandler.handleFicha(event, ficha)
                    //event.getChannel().sendMessage(embed(event, ficha)).queue()
                );
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }
        });
    }

    @Event
    public static void fichalog(CommandRegistry registry) {
        registry.register("fichalog", new NoArgsCommand(CommandPermission.ADMIN) {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content) {
                Ficha ficha = HbdvData.db().getFichaLog(content);

                if (ficha == null) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhuma Ficha com esse ID"
                    ).queue();
                    return;
                }

                //event.getChannel().sendMessage(embed(event, ficha)).queue();
                FichaHandler.handleFicha(event, ficha);
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }
        });
    }
}
