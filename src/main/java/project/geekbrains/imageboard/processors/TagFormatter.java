package project.geekbrains.imageboard.processors;

import java.util.Stack;

public class TagFormatter {
    private static final int BASE_STATEMENT = 0;
    private static final int STATEMENT_OPEN = 1;
    private static final int STATEMENT_OPEN_PLUS = 2;
    private static final int STATEMENT_OPEN_PLUS_X = 3;
    private static final int STATEMENT_OPEN_MINUS = 4;
    private static final int STATEMENT_OPEN_MINUS_X = 5;
    private static final int STATEMENT_NEXT_LITERALLY = 6;

    private int statement = BASE_STATEMENT;

    private StringBuilder doneStore = new StringBuilder();
    private StringBuilder reserved = new StringBuilder();

    private String source;

    private char nextTag;

    private Stack<Character> stack = new Stack<Character>();

    public TagFormatter(String source) {
        this.source = source;
    }

    private void nextStatement(char letter) {
        if (statement == STATEMENT_NEXT_LITERALLY) {
            flushReserved();
            doneStore.append(letter);
            statement = BASE_STATEMENT;
            return;
        }
        if (letter == '|') {
            statement = STATEMENT_NEXT_LITERALLY;
            return;
        }
        switch (statement) {
            case BASE_STATEMENT:
                fromBase(letter);
                break;
            case STATEMENT_OPEN:
                fromOpen(letter);
                break;
            case STATEMENT_OPEN_PLUS:
                fromOpenPlus(letter);
                break;
            case STATEMENT_OPEN_PLUS_X:
                fromOpenPlusX(letter);
                break;
            case STATEMENT_OPEN_MINUS:
                fromOpenMinus(letter);
                break;
            case STATEMENT_OPEN_MINUS_X:
                fromOpenMinusX(letter);
                break;
        }
    }

    private void fromBase(char letter) {
        if (letter == '[') {
            statement = STATEMENT_OPEN;
            reserved.append('[');
            return;
        }
        doneStore.append(letter);
    }

    private void fromOpen(char letter) {
        if (letter == '+') {
            statement = STATEMENT_OPEN_PLUS;
            reserved.append('+');
            return;
        }
        if (letter == '-') {
            statement = STATEMENT_OPEN_MINUS;
            reserved.append('-');
            return;
        }
        statement = BASE_STATEMENT;
        flushReserved();
        doneStore.append(letter);
    }

    private void fromOpenPlus(char letter) {
        switch (letter) {
            case 'i':
            case 'b':
            case 's':
                nextTag = letter;
                statement = STATEMENT_OPEN_PLUS_X;
                reserved.append(letter);
                break;
            default:
                statement = BASE_STATEMENT;
                flushReserved();
                doneStore.append(letter);
        }
    }

    private void fromOpenMinus(char letter) {
        switch (letter) {
            case 'i':
            case 'b':
            case 's':
                nextTag = letter;
                statement = STATEMENT_OPEN_MINUS_X;
                reserved.append(letter);
                break;
            default:
                statement = BASE_STATEMENT;
                flushReserved();
                doneStore.append(letter);
        }
    }

    private void fromOpenPlusX(char letter) {
        if (letter == ']') {
            statement = BASE_STATEMENT;
            openTag();
            return;
        }
        statement = BASE_STATEMENT;
        flushReserved();
        doneStore.append(letter);
    }

    private void fromOpenMinusX(char letter) {
        if (letter == ']') {
            statement = BASE_STATEMENT;
            closeTag();
            return;
        }
        statement = BASE_STATEMENT;
        flushReserved();
        doneStore.append(letter);
    }

    private void openTag() {
        reserved.setLength(0);
        stack.push(nextTag);
        doneStore.append("<" + nextTag + ">");
    }

    private void closeTag() {
        reserved.setLength(0);
        if (!stack.empty()) {
            char currentX = stack.peek();
            if (currentX == nextTag) {
                stack.pop();
                doneStore.append("</" + nextTag + ">");
            }
        }
    }

    private void endCloseTags() {
        while (!stack.empty()) {
            char currentX = stack.pop();
            doneStore.append("</" + currentX + ">");
        }
    }

    private void flushReserved() {
        doneStore.append(reserved.toString());
        reserved.setLength(0);
    }

    public String format() {
        for(int i = 0; i < source.length(); i++) {
            nextStatement(source.charAt(i));
        }
        flushReserved();
        endCloseTags();
        return doneStore.toString();
    }
}
