package com.theorangehub.dml;

import com.theorangehub.dml.parser.DMLParser;
import com.theorangehub.dml.parser.lexer.DMLLexer;
import com.theorangehub.dml.reader.DefaultTagResolver;
import com.theorangehub.dml.reader.StringBuilderUtils;
import com.theorangehub.dml.reader.TagResolver;

import java.util.List;

public class DML {
    public static DMLBuilder parse(String text) {
        return parse(new DMLBuilder(), text);
    }

    private static void process(DMLBuilder builder, DMLLexer lexer, TagResolver resolver) {
        List<Object> objs = new DMLParser(lexer).parse();

        Tag root = new Tag(".root", null);
        root.getChilds().addAll(objs);

        System.out.println(root);

        resolver.process(builder, root, builder.getMessage().getStringBuilder());

        StringBuilderUtils.trim(builder.getMessage().getStringBuilder());
        StringBuilderUtils.trim(builder.getEmbed().getDescriptionBuilder());
    }

    public static DMLBuilder parse(DMLBuilder builder, String text) {
        process(builder, new DMLLexer(text), DefaultTagResolver.INSTANCE);
        return builder;
    }
}
