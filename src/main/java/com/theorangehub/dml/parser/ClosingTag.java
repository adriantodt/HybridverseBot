package com.theorangehub.dml.parser;

import com.theorangehub.dml.parser.lexer.Position;
import com.theorangehub.dml.parser.lexer.Token;
import lombok.Data;

@Data
public class ClosingTag {
    private final String name;
    private final Position position;

    public ClosingTag(Token token) {
        this(token.getString(), token.getPosition());
    }

    public ClosingTag(String name, Position position) {
        this.name = name;
        this.position = position;
    }
}
