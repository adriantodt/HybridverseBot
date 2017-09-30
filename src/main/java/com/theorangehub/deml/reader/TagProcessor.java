package com.theorangehub.deml.reader;

import com.theorangehub.deml.Tag;

public interface TagProcessor {
    default String accept(TagResolver resolver, Builder builder, Tag tag) {
        StringBuilder b = new StringBuilder();
        accept(resolver, builder, tag, b);
        return b.toString();
    }

    void accept(TagResolver resolver, Builder builder, Tag tag, StringBuilder input);
}
