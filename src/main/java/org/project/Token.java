package org.project;

public class Token {
    private final String value;
    private final TokenType type;
    private final int line;
    private final int col;

    public Token(String value, TokenType type, int line, int col) {
        this.value = value;
        this.type = type;
        this.line = line;
        this.col = col;
    }

    public String getValue() { return value; }
    public TokenType getType() { return type; }

    /**
     * Required format: "value, tokentype"
     */
    public String toFileOutput() {
        return value + ", " + type.name();
    }

    @Override
    public String toString() {
        if (type == TokenType.ERROR) {
            return String.format("ERROR at [Line %d:%d]: %s", line, col, value);
        }
        return String.format("[L%02d:C%02d] %-15s | %s", line, col, value, type.name());
    }
}