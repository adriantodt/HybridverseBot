package com.theorangehub.hbdvbot.commands;

import com.theorangehub.hbdvbot.commands.ficha.FichaHandler;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.modules.commands.NoArgsCommand;
import com.theorangehub.hbdvbot.utils.DiscordUtils;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Comparator;
import java.util.List;

@Module
public class FichaCmd {

    @Event
    public static void ficha(CommandRegistry registry) {
        registry.register("ficha", new NoArgsCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content) {
                if (content.isEmpty()) {
                    onHelp(event);
                    return;
                }

                List<Ficha> fichas = HbdvData.db().getFichasPorNome(content);
                fichas.sort(Comparator.comparing(Ficha::displayToString));

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
                return helpBuilder(event, "Fichas")
                    .descrição("Procura e Mostra uma ficha")
                    .uso("ficha <nome>", "Procura uma ficha com esse nome.")
                    .build();
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
                        EmoteReference.ERROR + "Nenhum Log com esse ID"
                    ).queue();
                    return;
                }

                //event.getChannel().sendMessage(embed(event, ficha)).queue();
                FichaHandler.handleFicha(event, ficha);
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return helpBuilder(event, "Logs de Ficha")
                    .descrição("Mostra a Ficha em uma versão específica")
                    .uso("fichalog <id>", "Mostra a ficha dessa versão.")
                    .build();
            }
        });
    }
}
