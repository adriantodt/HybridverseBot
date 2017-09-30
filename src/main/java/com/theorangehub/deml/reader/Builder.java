package com.theorangehub.deml.reader;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class Builder {
    private MessageBuilder message;
    private EmbedBuilder embed;

    public MessageBuilder getMessage() {
        if (message == null) message = new MessageBuilder();
        return message;
    }

    public Builder setMessage(MessageBuilder message) {
        this.message = message;
        return this;
    }

    public EmbedBuilder getEmbed() {
        if (embed == null) embed = new EmbedBuilder();
        return embed;
    }

    public Builder setEmbed(EmbedBuilder embed) {
        this.embed = embed;
        return this;
    }

    public Message build() {
        MessageBuilder message = getMessage();
        if (embed != null) message.setEmbed(embed.build());
        return message.build();
    }
}
