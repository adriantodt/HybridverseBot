package com.theorangehub.deml.reader;

import com.theorangehub.deml.Tag;

import java.util.Iterator;

public class TagTextAppender implements TagProcessor {
    public static TagTextAppender INSTANCE = new TagTextAppender();

    protected TagTextAppender() {
    }

    @Override
    public void accept(TagResolver resolver, Builder builder, Tag tag, StringBuilder input) {
        boolean first = true;
        for (Iterator<Object> iterator = tag.getChilds().iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();

            int length = input.length();

            if (obj instanceof Tag) {
                resolver.process(builder, (Tag) obj, input);
            } else {
                input.append(obj == null ? "" : obj.toString());
            }
        }
    }
}
