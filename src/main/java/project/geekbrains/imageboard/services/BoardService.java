package project.geekbrains.imageboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.geekbrains.imageboard.entities.*;
import project.geekbrains.imageboard.processors.MessageProcessor;
import project.geekbrains.imageboard.repositories.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BoardService {
    private GroupsRepository groupsRepository;
    private MessageRepository messageRepository;
    private SectionRepository sectionRepository;
    private MessageImageRepository imageRepository;
    private MessageAnswersRepository answersRepository;

    @Autowired
    public void setGroupsRepository(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public void setSectionRepository(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Autowired
    public void setAnswersRepository(MessageAnswersRepository answersRepository) {
        this.answersRepository = answersRepository;
    }

    @Autowired
    public void setImageRepository(MessageImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<MessageGroup> getAllGroups(Long section_id) {
        return sectionRepository.findById(section_id).get().getMessageGroups();
    }

    public MessageGroup getGroupById(Long id) {
        return groupsRepository.findById(id).get();
    }

    public void createNewGroup(Message firstMessage, Long section_id) {

        Section section = sectionRepository.findById(section_id).get();

        Date currentDate = new Date();
        firstMessage.setMessage_date(currentDate);
        MessageGroup newGroup = new MessageGroup();

        newGroup.setSection(section);


        newGroup.setGroup_date(currentDate);

        firstMessage.setGroup(newGroup);

        firstMessage.setNumber(nextNumber(section));

        MessageProcessor.processMessage(firstMessage, messageRepository);

        newGroup.setTitle(firstMessage.getTitle());
        groupsRepository.save(newGroup);


        MessageImage image = firstMessage.getImage();
        if (image != null) {
            imageRepository.save(image);
        }

        MessageAnswers messageAnswers = new MessageAnswers();
        messageAnswers.setMessages(new ArrayList<Message>());
        messageAnswers.setRootMessage(firstMessage);
        firstMessage.setAnswers(messageAnswers);

        answersRepository.save(messageAnswers);

        messageRepository.save(firstMessage);
    }

    public List<Message> getThreadMessages(Long id) {
        MessageGroup thread = groupsRepository.findById(id).get();
        return thread.getMessages();
    }

    public void addMessage(Long groupId, Message message) {

        Date currentDate = new Date();
        message.setMessage_date(currentDate);
        MessageGroup group = groupsRepository.findById(groupId).get();

        Section section = group.getSection();

        message.setGroup(group);

        message.setNumber(nextNumber(section));



        MessageImage image = message.getImage();
        if (image != null) {
            imageRepository.save(image);
        }

        MessageProcessor.processMessage(message, messageRepository);

        MessageAnswers messageAnswers = new MessageAnswers();
        messageAnswers.setMessages(new ArrayList<Message>());
        messageAnswers.setRootMessage(message);
        message.setAnswers(messageAnswers);

        messageRepository.findById(2L).get().getAnswers().getMessages().add(message);

        answersRepository.save(messageAnswers);

        messageRepository.save(message);
    }

    private Long nextNumber(Section section) {
        Long number = section.getMessage_counter();
        section.setMessage_counter(number + 1);
        sectionRepository.save(section);
        return number;
    }

    public void addSection(Section section) {
        sectionRepository.save(section);
    }

    public List<Section> getAllSections() {
        return (List<Section>) sectionRepository.findAll();
    }


}
