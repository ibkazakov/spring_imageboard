package project.geekbrains.imageboard.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.geekbrains.imageboard.entities.MessageGroup;

@Repository
public interface GroupsRepository extends CrudRepository<MessageGroup, Long> {
}
