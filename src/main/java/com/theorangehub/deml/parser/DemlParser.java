package com.theorangehub.deml.parser;

import com.theorangehub.deml.SyntaxException;
import com.theorangehub.deml.Tag;
import com.theorangehub.deml.lexer.DemlLexer;
import com.theorangehub.deml.lexer.Token;
import com.theorangehub.deml.lexer.TokenType;

import java.util.LinkedList;
import java.util.List;

public class DemlParser extends Parser {
    public DemlParser(DemlLexer lexer) {
        super(lexer);
    }

    private Object parseInner() {
        Token t = eat();

        switch (t.getType()) {
            case EOF: {
                return null;
            }

            case SLASH: {
                Token name = eat(TokenType.IDENTIFIER);
                eat(TokenType.GT);
                return new ClosingTag(name);
            }

            case IDENTIFIER: {
                return parseTag(t);
            }

            default: {
                throw new SyntaxException("Unexpected " + t, t.getPosition());
            }
        }
    }


    private Tag parseTag(Token starting) {
        Tag tag = new Tag(starting);

        loop:
        while (true) {
            Token t = eat();

            switch (t.getType()) {
                case GT: {
                    break loop;
                }

                case SLASH: {
                    eat(TokenType.GT);
                    return tag;
                }

                case IDENTIFIER: {
                    if (nextIs(TokenType.EQUALS)) {
                        eat();
                        tag.getAttributes().put(t.getString(), eat(TokenType.STRING).getString());
                    } else {
                        tag.getAttributes().put(t.getString(), t.getString());
                    }

                    break;
                }

                default: {
                    throw new SyntaxException("Unexpected " + t, t.getPosition());
                }
            }
        }

        while (true) {
            Object child = parseOuter(false);

            if (child instanceof ClosingTag) {
                ClosingTag closingTag = (ClosingTag) child;

                if (!closingTag.getToken().getString().equals(tag.getToken().getString())) {
                    throw new SyntaxException("Unexpected closing tag " + closingTag, closingTag.getToken().getPosition());
                }

                break;
            }

            tag.getChilds().add(child);
        }

        return tag;
    }

    private Object parseOuter(boolean root) {
        Token t = eat();

        switch (t.getType()) {
            case TEXT: {
                return t.getString();
            }

            case LT: {
                return parseInner();
            }

            case EOF: {
                if (root) return null;
            }

            default: {
                throw new SyntaxException("Unexpected " + t, t.getPosition());
            }
        }
    }

    public List<Object> parse() {
        List<Object> result = new LinkedList<>();

        while (true) {
            Object obj = parseOuter(true);
            if (obj == null) break;
            result.add(obj);
        }

        return result;
    }
}
