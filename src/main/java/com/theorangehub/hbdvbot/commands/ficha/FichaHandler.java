package com.theorangehub.hbdvbot.commands.ficha;

import com.theorangehub.hbdvbot.core.listeners.operations.ReactionOperation;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class FichaHandler {
    private static final Map<String, Function<FichaEmbeds, MessageEmbed>> initialEmojis = new LinkedHashMap<>();

    static {
        initialEmojis.put("\uD83D\uDDF3", FichaEmbeds::atributosEmbed);
        initialEmojis.put("\uD83C\uDFC5", FichaEmbeds::ranksEmbed);
        initialEmojis.put("\uD83D\uDCB0", FichaEmbeds::carteiraEmbed);
        initialEmojis.put("\uD83D\uDCD5", FichaEmbeds::historiaEmbed);
        initialEmojis.put("hbdvround:328180364608667650", FichaEmbeds::partiesEmbed);
        initialEmojis.put("\uD83C\uDCCF", FichaEmbeds::miscEmbed);
        initialEmojis.put("\uD83D\uDCF7", FichaEmbeds::avatarEmbed);
    }

    public static void handleFicha(GuildMessageReceivedEvent event, Ficha ficha) {
        new FichaHandler(event, ficha);
    }

    private final FichaEmbeds embeds;
    private final GuildMessageReceivedEvent event;
    private final Message message;

    private FichaHandler(GuildMessageReceivedEvent event, Ficha ficha) {
        this.event = event;
        this.embeds = new FichaEmbeds(ficha);
        this.message = event.getChannel().sendMessage(embeds.initialEmbed()).complete();
        handleInitialEmojis();
    }

    private void handleInitialEmojis() {
        ReactionOperation.builder()
            .message(message)
            .timeout(2, TimeUnit.MINUTES)
            .addReactions(initialEmojis.keySet().toArray(new String[0]))
            .onReaction(e -> {
                if (!e.getUser().equals(event.getAuthor()))
                    return false;

                ReactionEmote emote = e.getReactionEmote();

                Function<FichaEmbeds, MessageEmbed> function = initialEmojis.get(
                    emote.isEmote() ? emote.getName() + ":" + emote.getId() : emote.getName()
                );
                if (function == null) return false;
                message.editMessage(function.apply(embeds)).queue();
                handleSubEmojis();

                return true;
            })
            .forceCreate();
    }

    private void handleSubEmojis() {
        ReactionOperation.builder()
            .message(message)
            .timeout(2, TimeUnit.MINUTES)
            .addReactions("\u2b05")
            .onReaction(e -> {
                if (!e.getUser().equals(event.getAuthor()) || !e.getReactionEmote().getName().equals("\u2b05"))
                    return false;

                message.editMessage(embeds.initialEmbed()).queue();
                handleInitialEmojis();

                return true;
            })
            .forceCreate();
    }
}
