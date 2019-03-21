package project.geekbrains.imageboard.repositories;

import org.springframework.data.repository.CrudRepository;
import project.geekbrains.imageboard.entities.MessageAnswers;

public interface MessageAnswersRepository extends CrudRepository<MessageAnswers, Long> {
}
