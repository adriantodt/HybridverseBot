package com.theorangehub.hbdvbot.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.theorangehub.hbdvbot.HBDVBOT;
import com.theorangehub.hbdvbot.data.HbdvData;
import lombok.SneakyThrows;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.MessageBuilder.SplitPolicy;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DiscordLogBack extends AppenderBase<ILoggingEvent> {
    private static BlockingQueue<ILoggingEvent> eventQueue = new LinkedBlockingQueue<>();
    private static DiscordLogBack instance;
    private PatternLayout layout;

    private ILoggingEvent previousEvent;
    private Thread thread;

    {
    	instance = this;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (event.getLevel().isGreaterOrEqual(Level.INFO)) {
            eventQueue.offer(event);
        }
    }

    @Override
    public void start() {
        layout = new PatternLayout();

        //Configuration
        layout.setContext(getContext());
        layout.setPattern("[`%d{HH:mm:ss}`] [`%t/%level`] [`%logger{0}`]: %msg");
        layout.start();

        super.start();
    }

    public static void enable() {
        instance.startThread(HBDVBOT.getInstance().getTextChannelById(HbdvData.config().get().consoleChannel));
    }

    public static void disable() {
        if (instance == null || instance.thread == null) return;
        instance.thread.interrupt();
    }

    public void startThread(MessageChannel channel) {
        thread = new Thread("DiscordLogBack Thread") {
            @SuppressWarnings("InfiniteLoopStatement")
            @Override
            @SneakyThrows
            public void run() {
                ILoggingEvent event;

                while (true) {
                    event = eventQueue.take();

                    StringJoiner joiner = new StringJoiner("\n");
                    joiner.add(layout.doLayout(event));

                    long start = System.currentTimeMillis();
                    while (true) {
                        event = eventQueue.poll(500, TimeUnit.MILLISECONDS);
                        if (event == null) break;
                        joiner.add(layout.doLayout(event));
                        if (System.currentTimeMillis() > start + 1000) break;
                    }

                    for (Message message : new MessageBuilder().append(joiner.toString()).buildAll(SplitPolicy.SPACE)) {
                        channel.sendMessage(message).queue();
                    }
                }
            }
        };

        thread.start();
    }
}