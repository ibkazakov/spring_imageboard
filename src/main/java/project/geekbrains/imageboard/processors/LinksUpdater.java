package project.geekbrains.imageboard.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.geekbrains.imageboard.entities.Message;
import project.geekbrains.imageboard.repositories.MessageRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

@Service
public class LinksUpdater {


    private MessageRepository messageRepository;

    public LinksUpdater(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    private static String addLinkQuery = "SELECT m FROM Message m JOIN " +
            "FETCH m.group g JOIN FETCH g.section s WHERE " +
            "s.id = :section_id AND m.number = :num";


    public void addNewLinks(List<Long> parsedNums, Message message) {
        Long section_id = message.getGroup().getSection().getId();
        for(Long num : parsedNums) {
            Collection<Message> messages = messageRepository.getMessagesByNumber(num);
            for(Message current_message : messages) {
                Long current_section_id = current_message.getGroup().getSection().getId();
                if (section_id == current_section_id) {
                    current_message.getAnswers().getMessages().add(message);
                }
            }
        }
    }
}
