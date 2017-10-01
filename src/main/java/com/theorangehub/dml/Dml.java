package com.theorangehub.dml;

import com.theorangehub.dml.lexer.DmlLexer;
import com.theorangehub.dml.parser.DmlParser;
import com.theorangehub.dml.reader.Builder;
import com.theorangehub.dml.reader.DefaultTagResolver;
import com.theorangehub.dml.reader.StringBuilderUtils;
import net.dv8tion.jda.core.entities.Message;

import java.util.List;

public class Dml {
    public static Message parse(String text) {
        List<Object> objs = new DmlParser(new DmlLexer(text)).parse();

        Tag root = new Tag("root", null);
        root.getChilds().addAll(objs);

        Builder builder = new Builder();
        DefaultTagResolver.INSTANCE.process(builder, root, builder.getMessage().getStringBuilder());

        StringBuilderUtils.trim(builder.getMessage().getStringBuilder());
        StringBuilderUtils.trim(builder.getEmbed().getDescriptionBuilder());

        return builder.build();
    }
}
