package project.geekbrains.imageboard.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.geekbrains.imageboard.entities.Message;

import java.util.Collection;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    @Query("SELECT m FROM Message m JOIN FETCH m.group g JOIN FETCH g.section s WHERE s.id = :section_id AND m.number = :num")
    Collection<Message> getRootLinkedMessage(Long section_id, int num);

    Collection<Message> getMessagesByNumber(Long number);

}
