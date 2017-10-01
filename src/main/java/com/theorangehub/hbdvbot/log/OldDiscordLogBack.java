package com.theorangehub.hbdvbot.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.theorangehub.hbdvbot.HbdvBot;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.utils.HbdvUtils;
import net.dv8tion.jda.core.entities.TextChannel;

import static com.theorangehub.hbdvbot.utils.commands.EmoteReference.WARNING;

public class OldDiscordLogBack extends AppenderBase<ILoggingEvent> {
    private static boolean enabled = false;

    public static void disable() {
        enabled = false;
    }

    public static void enable() {
        enabled = true;
    }

    private static TextChannel consoleChannel() {
        return HbdvBot.getInstance().getTextChannelById(HbdvData.config().get().consoleChannel);
    }

    private PatternLayout patternLayout;
    private ILoggingEvent previousEvent;

    @Override
    protected void append(ILoggingEvent event) {
        if (!enabled) return;
        if (!event.getLevel().isGreaterOrEqual(Level.INFO)) return;
        String toSend = patternLayout.doLayout(event);
        if (previousEvent != null && event.getMessage().equals(previousEvent.getMessage())) return;
        if (toSend.length() > 1920)
            toSend = WARNING + "Received a message but it was too long, Hastebin: " + HbdvUtils.paste(toSend);
        consoleChannel().sendMessage(toSend).queue();
        previousEvent = event;
    }

    @Override
    public void start() {
        patternLayout = new PatternLayout();
        patternLayout.setContext(getContext());
        patternLayout.setPattern("[`%d{HH:mm:ss}`] [`%t/%level`] [`%logger{0}`]: %msg");
        patternLayout.start();

        super.start();
    }
}