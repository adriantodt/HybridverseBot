package com.theorangehub.deml.reader;

import com.theorangehub.deml.Tag;

public interface TagResolver {
    TagProcessor get(String tagName);

    TagProcessor defaultProcessor();

    default TagProcessor get(Tag tag) {
        return get(tag.getToken().getString());
    }

    default String process(Builder builder, Tag tag) {
        return get(tag).accept(this, builder, tag);
    }

    default void process(Builder builder, Tag tag, StringBuilder input) {
        get(tag).accept(this, builder, tag, input);
    }
}
