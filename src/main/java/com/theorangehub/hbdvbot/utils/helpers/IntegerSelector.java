package com.theorangehub.hbdvbot.utils.helpers;

import com.theorangehub.hbdvbot.core.listeners.operations.InteractiveOperation;
import com.theorangehub.hbdvbot.utils.TimeAmount;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.*;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class IntegerSelector {
    private final GuildMessageReceivedEvent event;
    private String channelId;
    private TimeAmount increasingTimeout;
    private TimeAmount initialTimeout;
    private int min = 0, max = Integer.MAX_VALUE;

    public Future<Integer> build() {
        CompletableFuture<Integer> result = new CompletableFuture<>();
        InteractiveOperation.builder()
            .channel(channelId)
            .initialTimeout(initialTimeout)
            .increasingTimeout(increasingTimeout)
            .onMessage(e -> {
                if (!e.getAuthor().equals(event.getAuthor())) return false;

                try {
                    int choose = Integer.parseInt(e.getMessage().getContent());
                    if (choose < min || choose > max) return false;
                    e.getMessage().delete().queue();
                    result.complete(choose);
                    return true;
                } catch (Exception ignored) {}
                return false;
            })
            .onRemoved(() -> result.completeExceptionally(new CancellationException()))
            .onTimeout(() -> result.completeExceptionally(new TimeoutException()))
            .forceCreate();
        return result;
    }

    public IntegerSelector increasingTimeout(long amount, TimeUnit unit) {
        this.increasingTimeout = new TimeAmount(amount, unit);
        return this;
    }

    public IntegerSelector initialTimeout(long amount, TimeUnit unit) {
        this.initialTimeout = new TimeAmount(amount, unit);
        return this;
    }

    public IntegerSelector timeout(long amount, TimeUnit unit) {
        this.initialTimeout = new TimeAmount(amount, unit);
        this.increasingTimeout = new TimeAmount(amount, unit);
        return this;
    }
}
