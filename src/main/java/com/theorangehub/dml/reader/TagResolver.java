package com.theorangehub.dml.reader;

import com.theorangehub.dml.DMLBuilder;
import com.theorangehub.dml.Tag;

public interface TagResolver {
    TagProcessor get(String tagName);

    TagProcessor defaultProcessor();

    default TagProcessor get(Tag tag) {
        return get(tag.getName());
    }

    default String process(DMLBuilder builder, Tag tag) {
        return get(tag).accept(this, builder, tag);
    }

    default void process(DMLBuilder builder, Tag tag, StringBuilder input) {
        get(tag).accept(this, builder, tag, input);
    }
}
