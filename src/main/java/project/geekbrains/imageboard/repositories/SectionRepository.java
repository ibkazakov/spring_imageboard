package project.geekbrains.imageboard.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.geekbrains.imageboard.entities.Section;

@Repository
public interface SectionRepository extends CrudRepository<Section, Long> {
}
