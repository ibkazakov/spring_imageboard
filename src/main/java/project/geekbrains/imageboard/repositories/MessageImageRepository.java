package project.geekbrains.imageboard.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.geekbrains.imageboard.entities.MessageImage;

@Repository
public interface MessageImageRepository extends CrudRepository<MessageImage, Long> {
}
