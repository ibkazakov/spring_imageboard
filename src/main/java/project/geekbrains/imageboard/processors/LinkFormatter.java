package project.geekbrains.imageboard.processors;

import java.util.ArrayList;
import java.util.List;

public class LinkFormatter {
    private static final int BASE_STATEMENT = 0;
    private static final int STATEMENT_BRACKET_1 = 1;
    private static final int STATEMENT_BRACKET_2 = 2;
    private static final int STATEMENT_DIGIT = 3;

    private int statement = BASE_STATEMENT;

    private StringBuilder doneStore = new StringBuilder();

    private StringBuilder digitStore = new StringBuilder();
    private int receivedBrackets = 0;

    private List<Long> parsedNums = new ArrayList<Long>();

    public List<Long> getParsedNums() {
        return parsedNums;
    }

    private String source;

    public LinkFormatter(String source) {
        this.source = source;
    }


    private void printLink() {
        String number = digitStore.toString();
        digitStore.setLength(0);
        receivedBrackets = 0;
        doneStore.append("<em>");
        doneStore.append("<a class=\"post-link\" href=\"#p" + number +"\">");
        doneStore.append("&nbsp;");
        doneStore.append("<span class=\"link-text\">");
        doneStore.append("**");
        doneStore.append(number);
        doneStore.append("</span>");
        doneStore.append("&nbsp;");
        doneStore.append("</a>");
        doneStore.append("</em>");

        parsedNums.add(Long.parseLong(number));
    }

    private void flushBrackets() {
        for(int i = 0; i < receivedBrackets; i++) {
            doneStore.append(">");
        }
        receivedBrackets = 0;
    }

    private void nextStatement(char letter) {
        switch (statement) {
            case BASE_STATEMENT:
                fromBase(letter);
                break;
            case STATEMENT_BRACKET_1:
                fromBracket1(letter);
                break;
            case STATEMENT_BRACKET_2:
                fromBracket2(letter);
                break;
            case STATEMENT_DIGIT:
                fromDigit(letter);
                break;
        }
    }

    private void fromBase(char letter) {
        if (letter == '*') {
            statement = STATEMENT_BRACKET_1;
            receivedBrackets = 1;
            return;
        }
        doneStore.append(letter);
    }

    private void fromBracket1(char letter) {
        if (letter == '*') {
            statement = STATEMENT_BRACKET_2;
            receivedBrackets = 2;
            return;
        }
        statement = BASE_STATEMENT;
        flushBrackets();
        doneStore.append(letter);
    }

    private void fromBracket2(char letter) {
        if (Character.isDigit(letter)) {
            statement = STATEMENT_DIGIT;
            receivedBrackets = 0;
            digitStore.append(letter);
            return;
        }
        statement = BASE_STATEMENT;
        flushBrackets();
        doneStore.append(letter);
    }

    private void fromDigit(char letter) {
        if(Character.isDigit(letter)) {
            digitStore.append(letter);
            return;
        }
        statement = BASE_STATEMENT;
        printLink();
        doneStore.append(letter);
    }

    public String format() {
        for(int i = 0; i < source.length(); i++) {
            nextStatement(source.charAt(i));
        }
        flushBrackets();
        if (digitStore.length() != 0) {
            printLink();
        }
        return doneStore.toString();
    }
}
