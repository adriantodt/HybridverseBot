package com.theorangehub.dml.reader;

import com.theorangehub.dml.Tag;

import java.util.Iterator;

public class TagTextAppender implements TagProcessor {
    public static TagTextAppender INSTANCE = new TagTextAppender();

    protected TagTextAppender() {
    }

    @Override
    public void accept(TagResolver resolver, Builder builder, Tag tag, StringBuilder input) {
        for (Iterator<Object> iterator = tag.getChilds().iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();

            int length = input.length();

            if (obj instanceof Tag) {
                resolver.process(builder, (Tag) obj, input);
            } else {
                input.append(obj == null ? "" : obj.toString());
            }
            if (input.length() > 1 && length > 0 && input.length() > length) {
                if (Character.isWhitespace(input.charAt(length - 1)) && Character.isWhitespace(input.charAt(length))) {
                    input.deleteCharAt(length);
                }
            }
        }
    }
}
