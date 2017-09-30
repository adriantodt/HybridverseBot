package com.theorangehub.deml;

import com.theorangehub.deml.lexer.DemlLexer;
import com.theorangehub.deml.parser.DemlParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Object> objects = new DemlParser(new DemlLexer("huh? is that a new HTML in the wild?\n" +
            "<embed>\n" +
            "    <author url=\"bout_me\" img=\"beutiful_me\">AdrianTodt</author>\n" +
            "    <title></title>\n" +
            "    <description>\n" +
            "        Oh hai, how's goin?\n" +
            "    </description>\n" +
            "</embed>\n" +
            "weeee")).parse();
    }
}
