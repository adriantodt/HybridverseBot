package com.theorangehub.deml;

import com.theorangehub.deml.lexer.Token;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Tag {
    private final Token token;
    private final List<Object> childs = new LinkedList<>();
    private final Map<String,String> attributes = new LinkedHashMap<>();
}
