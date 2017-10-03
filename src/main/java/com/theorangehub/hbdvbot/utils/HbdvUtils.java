package com.theorangehub.hbdvbot.utils;

import com.theorangehub.hbdvbot.HbdvCommons;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Pattern;

@Slf4j
public class HbdvUtils {
    private static final Pattern regexPattern = Pattern.compile("[\\-\\[\\]/{}()*+?.\\\\^$|]");

    public static String escapeRegex(String input) {
        return regexPattern.matcher(input).replaceAll("\\$&");
    }

    public static String paste(String toSend) {
        try {
            RequestBody post = RequestBody.create(MediaType.parse("text/plain"), toSend);

            Request toPost = new Request.Builder()
                .url("https://hastebin.com/documents")
                .header("User-Agent", "Mantaro")
                .header("Content-Type", "text/plain")
                .post(post)
                .build();

            Response r = HbdvCommons.HTTP_CLIENT.newCall(toPost).execute();
            JSONObject response = new JSONObject(r.body().string());
            r.close();
            return "https://hastebin.com/" + response.getString("key");
        } catch(Exception e) {
            e.printStackTrace();
            return "Pastebin is unavaliable right now";
        }
    }

    public static String indexedToString(List<String> list) {
        StringJoiner joiner = new StringJoiner("\n");

        int i = 0;
        for (String item : list) {
            i++;
            joiner.add(i + "." + item);
        }

        return joiner.toString();
    }

    public static String toRoman(int value) {
        if (value < 1 || value > 3999) throw new IllegalArgumentException("Invalid Roman Number");
        StringBuilder builder = new StringBuilder();

        while (value >= 1000) {
            builder.append('M');
            value -= 1000;
        }

        while (value >= 900) {
            builder.append("CM");
            value -= 900;
        }

        while (value >= 500) {
            builder.append('D');
            value -= 500;
        }

        while (value >= 400) {
            builder.append("CD");
            value -= 400;
        }

        while (value >= 100) {
            builder.append('C');
            value -= 100;
        }

        while (value >= 90) {
            builder.append("XC");
            value -= 90;
        }

        while (value >= 50) {
            builder.append('L');
            value -= 50;
        }

        while (value >= 40) {
            builder.append("XL");
            value -= 40;
        }

        while (value >= 10) {
            builder.append('X');
            value -= 10;
        }

        while (value >= 9) {
            builder.append("IX");
            value -= 9;
        }

        while (value >= 5) {
            builder.append('V');
            value -= 5;
        }

        while (value >= 4) {
            builder.append("IV");
            value -= 4;
        }

        while (value >= 1) {
            builder.append('I');
            value -= 1;
        }

        return builder.toString();
    }

    public static String capitalize(String s) {
        if(s.length() == 0) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static Map<String, String> parse(String[] args) {
        Map<String, String> options = new LinkedHashMap<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-' || arg.charAt(0) == '/') { //This start with - or /
                arg = arg.substring(1);
                if (i + 1 >= args.length || args[i + 1].charAt(0) == '-' || args[i + 1].charAt(0) == '/') { //Next start with - (or last arg)
                    options.put(arg, null);
                } else {
                    options.put(arg, (args[i + 1]));
                    i++;
                }
            } else {
                options.merge(null, arg, (v1, v2) -> v1 + " " + v2);
            }
        }

        return options;
    }

    public static int clamp(int v, int min, int max) {
        return Math.max(Math.min(v, max), min);
    }
}