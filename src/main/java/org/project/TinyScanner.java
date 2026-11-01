package org.project;

public class TinyScanner {
    private enum State { START, IN_COMMENT, IN_NUMBER, IN_ID, IN_ASSIGN, DONE }

    private static final char EOF = '\0';
    private final String source;
    private int cursor = 0;
    private int line = 1;
    private int col = 0;

    public TinyScanner(String source) {
        this.source = source != null ? source : "";
    }

    public Token getToken() {
        StringBuilder lexeme = new StringBuilder();
        State state = State.START;
        TokenType tokenType = TokenType.ERROR;

        int startLine = line;
        int startCol = col + 1;

        while (state != State.DONE) {
            char c = nextChar();
            boolean save = true;

            switch (state) {
                case START:
                    startLine = line;
                    startCol = col;

                    if (Character.isDigit(c)) {
                        state = State.IN_NUMBER;
                    } else if (Character.isLetter(c)) {
                        state = State.IN_ID;
                    } else if (c == ':') {
                        state = State.IN_ASSIGN;
                    } else if (c == '=') {
                        state = State.DONE;
                        tokenType = TokenType.EQUAL;
                    } else if (Character.isWhitespace(c)) {
                        save = false;
                    } else if (c == '{') {
                        save = false;
                        state = State.IN_COMMENT;
                    } else if (c == EOF) {
                        save = false;
                        state = State.DONE;
                        tokenType = TokenType.ENDFILE;
                    } else {
                        state = State.DONE;
                        TokenType type = TokenType.lookup(String.valueOf(c));
                        if (type != TokenType.IDENTIFIER) {
                            tokenType = type;
                        } else {
                            String errMsg = "Invalid character '" + c + "': Not a valid TINY letter, digit, or supported symbol.";
                            return new Token(errMsg, TokenType.ERROR, startLine, startCol);
                        }
                    }
                    break;

                case IN_COMMENT:
                    save = false;
                    if (c == EOF) {
                        String errMsg = "Unclosed comment: Code block started with '{' but reached the end of the file without a closing '}'.";
                        return new Token(errMsg, TokenType.ERROR, startLine, startCol);
                    } else if (c == '}') {
                        state = State.START;
                    }
                    break;

                case IN_ASSIGN:
                    state = State.DONE;
                    if (c == '=') {
                        tokenType = TokenType.ASSIGN;
                    } else {
                        String errMsg = "Malformed assignment: Found ':', but expected '=' immediately after it to form ':='.";
                        return new Token(errMsg, TokenType.ERROR, startLine, startCol);
                    }
                    break;

                case IN_NUMBER:
                    if (!Character.isDigit(c)) {
                        if (c != EOF) retract();
                        save = false;
                        state = State.DONE;
                        tokenType = TokenType.NUMBER;
                    }
                    break;

                case IN_ID:
                    if (!Character.isLetter(c)) {
                        if (c != EOF) retract();
                        save = false;
                        state = State.DONE;
                        tokenType = TokenType.IDENTIFIER;
                    }
                    break;
            }

            if (save) lexeme.append(c);

            if (state == State.DONE && tokenType == TokenType.IDENTIFIER) {
                tokenType = TokenType.lookup(lexeme.toString());
            }
        }

        return new Token(lexeme.toString(), tokenType, startLine, startCol);
    }

    private char nextChar() {
        if (cursor >= source.length()) return EOF;
        char c = source.charAt(cursor++);
        if (c == '\n') {
            line++;
            col = 0;
        } else {
            col++;
        }
        return c;
    }

    private void retract() {
        if (cursor > 0) {
            cursor--;
            col--;
        }
    }
}