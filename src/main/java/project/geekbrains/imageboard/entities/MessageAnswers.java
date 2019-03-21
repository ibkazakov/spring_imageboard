package project.geekbrains.imageboard.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "message_answers")
public class MessageAnswers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Message rootMessage;


    @ManyToMany
    @JoinTable(name = "ans_join",
            joinColumns = @JoinColumn(name = "answers_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "message_id", referencedColumnName = "ID"))
    private List<Message> messages;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Message getRootMessage() {
        return rootMessage;
    }

    public void setRootMessage(Message rootMessage) {
        this.rootMessage = rootMessage;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
