package project.geekbrains.imageboard.processors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import project.geekbrains.imageboard.entities.Message;
import project.geekbrains.imageboard.entities.Section;
import project.geekbrains.imageboard.repositories.MessageRepository;

import javax.persistence.*;
import java.util.List;

public class MessageProcessor {
    private static final int MAX_TITLE = 50;



    public static void processMessage(Message message, MessageRepository messageRepository) {
        truncateTitle(message);
        transformText(message, messageRepository);
    }

    private static void truncateTitle(Message message) {
        String title = message.getTitle();
        if (title.length() > MAX_TITLE) {
            System.out.println("THIS");
            String newTitle = title.substring(0, MAX_TITLE);
            message.setTitle(newTitle);
        }
    }

    private static void transformText(Message message, MessageRepository messageRepository) {
        removeTags(message);
        divideLines(message);
        formatLinks(message, messageRepository);
        formatTags(message);
    }

    private static void removeTags(Message message) {
        String deTagedString = StringUtils.replaceEach(message.getText(),
                new String[]{"&", "<", ">", "\"", "'", "/"},
                new String[]{"&amp;", "&lt;", "&gt;", "&quot;", "&#x27;", "&#x2F;"});
        message.setText(deTagedString);
    }

    private static void divideLines(Message message) {
        String dividedLines = StringUtils.replace(message.getText(), "\n", "<br>");
        message.setText(dividedLines);
    }

    private static void formatTags(Message message) {
        String source = message.getText();
        TagFormatter formatter = new TagFormatter(source);
        message.setText(formatter.format());
    }

    private static void formatLinks(Message message, MessageRepository messageRepository) {
        String source = message.getText();
        LinkFormatter formatter = new LinkFormatter(source);
        message.setText(formatter.format());
        addNewLinks(formatter.getParsedNums(), message, messageRepository);
    }

    private static void addNewLinks(List<Long> parsedNums, Message message,
                                    MessageRepository messageRepository) {

        new LinksUpdater(messageRepository).addNewLinks(parsedNums, message);

    }
}
