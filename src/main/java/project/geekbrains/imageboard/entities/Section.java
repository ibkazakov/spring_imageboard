package project.geekbrains.imageboard.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "section")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "section_name")
    private String section_name;

    @Column(name = "description")
    private String description;

    @Column(name = "message_counter")
    private Long message_counter;

    @OneToMany(mappedBy = "section")
    private List<MessageGroup> messageGroups;


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public Long getMessage_counter() {
        return message_counter;
    }

    public void setMessage_counter(Long message_counter) {
        this.message_counter = message_counter;
    }

    public List<MessageGroup> getMessageGroups() {
        return messageGroups;
    }

    public void setMessageGroups(List<MessageGroup> messageGroups) {
        this.messageGroups = messageGroups;
    }

}
