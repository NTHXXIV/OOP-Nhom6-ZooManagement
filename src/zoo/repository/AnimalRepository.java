package zoo.repository;

import zoo.model.Animal;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface AnimalRepository {
    void add(Animal animal);
    void remove(String id);
    void update(Animal animal);
    Optional<Animal> findById(String id);
    List<Animal> search(Predicate<Animal> criteria);
    List<Animal> getAll();
    void replaceAll(List<Animal> animals);
    boolean exists(String id);
}
