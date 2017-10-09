package com.theorangehub.dml.reader;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import java.util.function.Function;

@Getter
@Setter
@Accessors(chain = true)
public class OverrideTagResolver implements TagResolver {
    private TagProcessor defaultProcessor;
    private Function<String, TagProcessor> nameResolver;
    private TagResolver parent;
    private Function<TagProcessor, TagProcessor> tagResolver;

    @Nonnull
    @Override
    public TagProcessor get(String tagName) {
        TagProcessor processor = null;
        if (nameResolver != null) processor = nameResolver.apply(tagName);
        if (processor != null) return processor;

        if (parent != null) processor = parent.get(tagName);
        if (processor != null && tagResolver != null) processor = tagResolver.apply(processor);

        if (processor != null) return processor;
        throw new IllegalStateException("null processor");
    }

    @Nonnull
    @Override
    public TagProcessor defaultProcessor() {
        if (defaultProcessor != null) return defaultProcessor;
        if (parent != null) return parent.defaultProcessor();
        throw new IllegalStateException("null default processor");
    }
}
