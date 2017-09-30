package com.theorangehub.deml;

import com.theorangehub.deml.lexer.Position;

public class SyntaxException extends RuntimeException {
    public SyntaxException(String msg) {
        super(msg);
    }

    public SyntaxException(String msg, Position position) {
        this(msg + position);
    }

    public SyntaxException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SyntaxException(String s, Position position, Throwable throwable) {
        super(s + position, throwable);
    }

    public SyntaxException(Throwable throwable) {
        super(throwable);
    }
}
