package org.project;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {
    IF, THEN, ELSE, END, REPEAT, UNTIL, READ, WRITE,
    IDENTIFIER, NUMBER,
    SEMICOLON, ASSIGN, LESSTHAN, EQUAL,
    PLUS, MINUS, MULT, DIV,
    OPENBRACKET, CLOSEDBRACKET,
    ENDFILE, ERROR;

    private static final Map<String, TokenType> DICTIONARY = new HashMap<>();

    static {
        // Reserved Words
        DICTIONARY.put("if", IF);
        DICTIONARY.put("then", THEN);
        DICTIONARY.put("else", ELSE);
        DICTIONARY.put("end", END);
        DICTIONARY.put("repeat", REPEAT);
        DICTIONARY.put("until", UNTIL);
        DICTIONARY.put("read", READ);
        DICTIONARY.put("write", WRITE);

        // Special Symbols
        DICTIONARY.put(";", SEMICOLON);
        DICTIONARY.put("<", LESSTHAN);
        DICTIONARY.put("=", EQUAL);
        DICTIONARY.put("+", PLUS);
        DICTIONARY.put("-", MINUS);
        DICTIONARY.put("*", MULT);
        DICTIONARY.put("/", DIV);
        DICTIONARY.put("(", OPENBRACKET);
        DICTIONARY.put(")", CLOSEDBRACKET);
    }

    public static TokenType lookup(String lexeme) {
        return DICTIONARY.getOrDefault(lexeme.toLowerCase(), IDENTIFIER);
    }
}