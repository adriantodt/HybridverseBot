package com.theorangehub.hbdvbot.commands.wiki;

import lombok.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

@Data
public class EmbedJSON {
    @Data
    public static class EmbedField {
        private boolean inline;
        private String name;
        private String value;
    }

    private String author;
    private String authorImg;
    private String authorUrl;
    private String color;
    private String description;
    private List<EmbedJSON.EmbedField> fields = Collections.emptyList();
    private String footer;
    private String footerImg;
    private String image;
    private String thumbnail;
    private String title;
    private String titleUrl;

    public MessageEmbed gen(GuildMessageReceivedEvent event) {
        return decorate(event, new EmbedBuilder()).build();
    }
    public EmbedBuilder decorate(GuildMessageReceivedEvent event, EmbedBuilder embed) {
        if (this.title != null) embed.setTitle(this.title, this.titleUrl);
        if (this.description != null) embed.setDescription(this.description);
        if (this.author != null) embed.setAuthor(this.author, this.authorUrl, this.authorImg);
        if (this.footer != null) embed.setFooter(this.footer, this.footerImg);
        if (this.image != null) embed.setImage(this.image);
        if (this.thumbnail != null) embed.setThumbnail(this.thumbnail);

        if (this.color != null) {
            Color c = null;

            try {
                c = (Color) Color.class.getField(this.color).get(null);
            } catch (Exception e) {
                String colorLower = this.color.toLowerCase();
                if (colorLower.equals("member")) {
                    c = event.getMember().getColor();
                } else if (colorLower.matches("#?(0x)?[0123456789abcdef]{1,6}")) {
                    try {
                        c = Color.decode(colorLower.startsWith("0x") ? colorLower : "0x" + colorLower);
                    } catch (Exception ignored) {}
                }
            }

            if (c != null) {
                embed.setColor(c);
            }
        }

        for (EmbedField f : this.fields) {
            if (f == null) {
                embed.addBlankField(false);
            } else if (f.value == null) {
                embed.addBlankField(f.inline);
            } else {
                embed.addField(f.name == null ? "" : f.name, f.value, f.inline);
            }
        }

        return embed;
    }
}
