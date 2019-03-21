package project.geekbrains.imageboard.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    // unique throw one section
    @Column(name = "number")
    private Long number;

    @Column(name = "title")
    private String title;

    @Column(name = "text", columnDefinition = "text")
    private String text;

    @Column(name = "message_date")
    private Date message_date;

    @Column(name = "username")
    private String username;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private MessageGroup group;


    @OneToOne
    @JoinColumn(name = "image_id")
    private MessageImage image;


    @OneToOne
    @JoinColumn(name = "answers_id")
    private MessageAnswers answers;


    public void setImage(MessageImage image) {
        this.image = image;
    }

    public MessageImage getImage() {
        return image;
    }
    

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getMessage_date() {
        return message_date;
    }

    public void setMessage_date(Date message_date) {
        this.message_date = message_date;
    }

    public MessageGroup getGroup() {
        return group;
    }

    public void setGroup(MessageGroup group) {
        this.group = group;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public MessageAnswers getAnswers() {
        return answers;
    }

    public void setAnswers(MessageAnswers answers) {
        this.answers = answers;
    }
}
