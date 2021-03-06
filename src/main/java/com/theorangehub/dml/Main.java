package com.theorangehub.dml;

import com.theorangehub.hbdvbot.utils.data.GsonDataManager;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.impl.MessageImpl;

public class Main {
    public static void main(String[] args) {
        Message message = DML.parse("huh? is that a new HTML in the wild?\n" +
            "<embed>\n" +
            "    <author\n" +
            "        url=\"https://adriantodt.theorangehub.com\"\n" +
            "        imgUrl=\"https://my-dedicated.is-probably-not.online/me.png\"\n" +
            "    >AdrianTodt</author>\n" +
            "    <title>Lel</title>\n" +
            "    <description>\n" +
            "        Oh hai, how's goin?\n" +
            "    </description>\n" +
            "    <field blank inline />\n" +
            "    <reactions name=\"Memes?\">\n" +
            "        <reaction ref=\"memes\" emote=\"kek:123\">More memes</reaction>\n" +
            "        <reaction ref=\"nomemes\" emote=\"nuu:000\">Less memes</reaction>\n" +
            "    </reactions>\n" +
            "</embed>\n" +
            "weeee~").build();

        System.out.println(GsonDataManager.GSON_PRETTY.toJson(((MessageImpl) message).toJSONObject().toMap()));
    }
}
