package com.theorangehub.deml.reader;

import com.theorangehub.deml.Tag;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.List;
import java.util.StringJoiner;

public class OldReader {
    private final StringJoiner descriptionBuilder;
    private final EmbedBuilder embedBuilder;
    private final StringJoiner messageBuilder;

    public OldReader() {
        this(new StringJoiner(" "), new StringJoiner(" "), new EmbedBuilder());
    }

    public OldReader(StringJoiner messageBuilder, StringJoiner descriptionBuilder, EmbedBuilder embedBuilder) {
        this.messageBuilder = messageBuilder;
        this.descriptionBuilder = descriptionBuilder;
        this.embedBuilder = embedBuilder;
    }

    public void accept(Object object, boolean insideDeml) {
        if (object instanceof Tag) {
            Tag tag = (Tag) object;

            switch (tag.getToken().getString()) {
                case "deml":
                case "description":
                case "embed": {
                    for (Object o : tag.getChilds()) {
                        accept(o, true);
                    }
                    return;
                }

                case "title": {

                }

                case "message": {
                    for (Object o : tag.getChilds()) {
                        accept(o, false);
                    }
                    return;
                }

                case "b":
                case "strong": {
                    handleSimpleDecorator(tag, "**%s**", insideDeml);
                    return;
                }

                case "i":
                case "em": {
                    handleSimpleDecorator(tag, "*%s*", insideDeml);
                    return;
                }

                case "a": {
                    String href = tag.getAttributes().get("href");

                    if (href == null) {
                        handleSimpleDecorator(tag, "%s", insideDeml);
                        break;
                    }

                    OldReader wrapper = wrappedRead(tag, insideDeml);

                    charInput(insideDeml).add("[" + href + "](" + wrapper.charInput(insideDeml) + ")");
                    return;
                }

                default: {
                    handleSimpleDecorator(tag, "%s", insideDeml);
                    return;
                }
            }

            return;
        }

        charInput(insideDeml).add(String.valueOf(object));
    }

    public void accept(List<Object> objects, boolean insideDeml) {
        for (Object object : objects) {
            accept(object, insideDeml);
        }
    }

    public void accept(Object[] objects, boolean insideDeml) {
        for (Object object : objects) {
            accept(object, insideDeml);
        }
    }

    private StringJoiner charInput(boolean insideDeml) {
        return insideDeml ? descriptionBuilder : messageBuilder;
    }

    public MessageEmbed getEmbed() {
        return embedBuilder.build();
    }

    public String getMessage() {
        return messageBuilder.toString();
    }

    private void handleSimpleDecorator(Tag tag, String format, boolean insideDeml) {
        OldReader wrapper = wrappedRead(tag, insideDeml);

        charInput(insideDeml).add(String.format(format, wrapper.charInput(insideDeml).toString()));
    }

    public String process(Tag tag) {
        return null;
    }

    private OldReader wrappedRead(Tag tag, boolean insideDeml) {
        OldReader wrapper = new OldReader(new StringJoiner(" "), new StringJoiner(" "), embedBuilder);
        wrapper.accept(tag.getChilds(), insideDeml);
        return wrapper;
    }
}
