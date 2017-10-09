package com.theorangehub.hbdvbot.commands.info;

import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.utils.CommandUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

@Accessors(fluent = true)
public class HelpBuilder {
    @Getter
    private final EmbedBuilder embed;
    @Setter
    private Color cor;
    @Setter
    private String descrição;
    private List<String> usos = new LinkedList<>();

    public HelpBuilder(GuildMessageReceivedEvent event, String name, CommandPermission permission) {
        embed = CommandUtils.helpEmbed(event, name, permission);
    }

    public MessageEmbed build() {
        if (cor != null) embed.setColor(cor);
        if (descrição != null) {
            embed.addField("Descrição:", descrição, false);
        }
        if (!usos.isEmpty()) {
            embed.addField(
                "Usos:",
                String.join("\n", usos),
                false
            );
        }

        return embed.build();
    }

    public HelpBuilder uso(String comando, String descrição) {
        usos.add("`" + HbdvData.config().get().getPrefix() + comando + "` - " + descrição);
        return this;
    }
}
