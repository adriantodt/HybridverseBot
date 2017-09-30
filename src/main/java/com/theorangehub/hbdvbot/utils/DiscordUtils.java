package com.theorangehub.hbdvbot.utils;

import com.theorangehub.hbdvbot.core.listeners.operations.InteractiveOperation;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.utils.tuple.Pair;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordUtils {
    private static class FormatToken {
        public final String format;
        public final int start;

        public FormatToken(String format, int start) {
            this.format = format;
            this.start = start;
        }
    }

    private static final String[] keys = new String[]{"*", "_", "`", "~~"};

    public static <T> Pair<String, Integer> embedList(List<T> list, Function<T, String> toString) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String s = toString.apply(list.get(i));
            if (b.length() + s.length() + 5 > MessageEmbed.TEXT_MAX_LENGTH) return Pair.of(b.toString(), i);
            b.append("**").append(i + 1).append(".** ");
            b.append(s);
            b.append("\n");
        }

        return Pair.of(b.toString(), list.size());
    }

    public static void selectInt(GuildMessageReceivedEvent event, int max, IntConsumer valueConsumer) {
        InteractiveOperation.builder()
            .channel(event.getChannel())
            .initialTimeout(20, TimeUnit.SECONDS)
            .onMessage(e -> {
                if (!e.getAuthor().equals(event.getAuthor())) return false;

                try {
                    int choose = Integer.parseInt(e.getMessage().getContent());
                    if (choose < 1 || choose >= max) return false;
                    e.getMessage().delete().queue();
                    valueConsumer.accept(choose);
                    return true;
                } catch (Exception ignored) {}
                return false;
            })
            .forceCreate();
    }

    public static <T> void selectList(GuildMessageReceivedEvent event, List<T> list, Function<T, String> toString, Function<String, MessageEmbed> toEmbed, Consumer<T> valueConsumer) {
        Pair<String, Integer> r = embedList(list, toString);
        Message message = event.getChannel().sendMessage(toEmbed.apply(r.getLeft())).complete();
        selectInt(event, r.getRight() + 1, i -> {
            message.delete().queue();
            valueConsumer.accept(list.get(i - 1));
        });
    }

    public static <T> void selectList(GuildMessageReceivedEvent event, List<T> list, MessageEmbed embed, Consumer<T> valueConsumer) {
        Message message = event.getChannel().sendMessage(embed).complete();
        selectInt(event, list.size() + 1, i -> {
            message.delete().queue();
            valueConsumer.accept(list.get(i - 1));
        });
    }

    public static String stripFormatting(String strippedContent) {
        //all the formatting keys to keep track of

        //find all tokens (formatting strings described above)
        TreeSet<FormatToken> tokens = new TreeSet<>(Comparator.comparingInt(t1 -> t1.start));
        for (String key : keys) {
            Matcher matcher = Pattern.compile(Pattern.quote(key)).matcher(strippedContent);
            while (matcher.find()) {
                tokens.add(new FormatToken(key, matcher.start()));
            }
        }

        //iterate over all tokens, find all matching pairs, and add them to the list toRemove
        Stack<FormatToken> stack = new Stack<>();
        List<FormatToken> toRemove = new ArrayList<>();
        boolean inBlock = false;
        for (FormatToken token : tokens) {
            if (stack.empty() || !stack.peek().format.equals(token.format) || stack.peek().start + token.format.length() == token.start) {
                //we are at opening tag
                if (!inBlock) {
                    //we are outside of block -> handle normally
                    if (token.format.equals("`")) {
                        //block start... invalidate all previous tags
                        stack.clear();
                        inBlock = true;
                    }
                    stack.push(token);
                } else if (token.format.equals("`")) {
                    //we are inside of a block -> handle only block tag
                    stack.push(token);
                }
            } else if (!stack.empty()) {
                //we found a matching close-tag
                toRemove.add(stack.pop());
                toRemove.add(token);
                if (token.format.equals("`") && stack.empty()) {
                    //close tag closed the block
                    inBlock = false;
                }
            }
        }

        //sort tags to remove by their start-index and iteratively build the remaining string
        toRemove.sort(Comparator.comparingInt(t -> t.start));
        StringBuilder out = new StringBuilder();
        int currIndex = 0;
        for (FormatToken formatToken : toRemove) {
            if (currIndex < formatToken.start) {
                out.append(strippedContent.substring(currIndex, formatToken.start));
            }
            currIndex = formatToken.start + formatToken.format.length();
        }
        if (currIndex < strippedContent.length()) {
            out.append(strippedContent.substring(currIndex));
        }
        //return the stripped text, escape all remaining formatting characters (did not have matching open/close before or were left/right of block
        return StringUtils.replaceEach(
            out.toString(),
            new String[]{
                "*", "_", "~"
            }, new String[]{
                "\\*", "\\_", "\\~"
            }
        );
    }
}
