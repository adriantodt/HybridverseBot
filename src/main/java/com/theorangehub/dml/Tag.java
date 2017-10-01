package com.theorangehub.dml;

import com.theorangehub.dml.lexer.Position;
import com.theorangehub.dml.lexer.Token;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Tag {
    private final Map<String, String> attributes = new LinkedHashMap<>();
    private final List<Object> childs = new LinkedList<>();
    private final String name;
    private final Position position;

    public Tag(Token token) {
        this(token.getString(), token.getPosition());
    }

    public Tag(String name, Position position) {
        this.name = name;
        this.position = position;
    }
}
