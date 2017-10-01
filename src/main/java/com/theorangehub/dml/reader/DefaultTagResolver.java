package com.theorangehub.dml.reader;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

public enum DefaultTagResolver implements TagResolver {
    INSTANCE;

    private final TagProcessor defaultProcessor;
    private final Map<String, TagProcessor> processors;

    {
        defaultProcessor = TagTextAppender.INSTANCE;
        processors = new LinkedHashMap<>();

        //<embed>
        {
            TagProcessor processor = (resolver, builder, tag, input) -> {
                Map<String, String> attrs = tag.getAttributes();

                if (attrs.containsKey("color")) {
                    String color = attrs.get("color").toLowerCase();
                    Color c = null;
                    if (color.matches("(#|0x)[0123456789abcdef]{1,6}")) {
                        try {
                            c = Color.decode(color);
                        } catch (Exception ignored) {}
                    }

                    if (c == null) {
                        try {
                            c = (Color) Color.class.getField(color).get(null);
                        } catch (Exception ignored) {}
                    }

                    if (c != null) {
                        builder.getEmbed().setColor(c);
                    }
                }

                StringBuilder newBuilder = builder.getEmbed().getDescriptionBuilder();
                defaultProcessor.accept(resolver, builder, tag, newBuilder);
            };
            processors.put("embed", processor);
        }

        //<author>
        {
            TagProcessor processor = (resolver, builder, tag, input) -> {
                Map<String, String> attrs = tag.getAttributes();
                String url = attrs.get("url");
                String imgUrl = attrs.get("imgUrl");

                String name = defaultProcessor.accept(resolver, builder, tag);

                builder.getEmbed().setAuthor(name, url, imgUrl);
            };
            processors.put("author", processor);
        }

        //<description>
        {
            TagProcessor processor = (resolver, builder, tag, input) -> {
                StringBuilder newBuilder = builder.getEmbed().getDescriptionBuilder();
                StringBuilderUtils.clear(newBuilder);
                defaultProcessor.accept(resolver, builder, tag, newBuilder);
            };
            processors.put("description", processor);
        }

        //<p>
        {
            TagProcessor processor = (resolver, builder, tag, input) -> {
                defaultProcessor.accept(resolver, builder, tag, input);
                input.append('\n');
            };
            processors.put("p", processor);
        }

        //<br>
        {
            TagProcessor processor = (resolver, builder, tag, input) -> {
                input.append('\n');
            };
            processors.put("br", processor);
        }

        //<title>
        {
            TagProcessor processor = (resolver, builder, tag, input) -> {
                builder.getEmbed().setTitle(defaultProcessor.accept(resolver, builder, tag), tag.getAttributes().get("href"));
            };
            processors.put("title", processor);
        }

        //<i> <em>
        {
            TagProcessor processor = new TagTextDecorator("*");
            processors.put("i", processor);
            processors.put("em", processor);
        }

        //<b> <strong>
        {
            TagProcessor processor = new TagTextDecorator("**");
            processors.put("b", processor);
            processors.put("strong", processor);
        }

        //<a>
        {
            TagProcessor processor = (resolver, builder, tag, input) -> {
                String href = tag.getAttributes().get("href");
                String text = defaultProcessor.accept(resolver, builder, tag);

                if (href == null || href.isEmpty()) {
                    input.append(text);
                } else {
                    input.append("[").append(href).append("](").append(text).append(")");
                }
            };
            processors.put("a", processor);
        }
    }

    public Map<String, TagProcessor> getProcessors() {
        return processors;
    }

    @Override
    public TagProcessor get(String tagName) {
        return processors.getOrDefault(tagName, defaultProcessor);
    }

    @Override
    public TagProcessor defaultProcessor() {
        return defaultProcessor;
    }
}
