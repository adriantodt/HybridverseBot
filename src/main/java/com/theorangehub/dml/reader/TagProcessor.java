package com.theorangehub.dml.reader;

import com.theorangehub.dml.Tag;

public interface TagProcessor {
    default String accept(TagResolver resolver, Builder builder, Tag tag) {
        StringBuilder b = new StringBuilder();
        accept(resolver, builder, tag, b);
        return b.toString();
    }

    void accept(TagResolver resolver, Builder builder, Tag tag, StringBuilder input);
}
