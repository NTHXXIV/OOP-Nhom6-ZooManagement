package zoo.repository;

import zoo.exception.ValidationException;
import zoo.model.Animal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class InMemoryAnimalRepository implements AnimalRepository {
    private final Map<String, Animal> animals = new LinkedHashMap<>();

    @Override
    public void add(Animal animal) {
        if (animals.containsKey(animal.getId())) {
            throw new ValidationException("Mã động vật đã tồn tại: " + animal.getId());
        }
        animals.put(animal.getId(), animal);
    }

    @Override
    public void remove(String id) {
        if (animals.remove(id) == null) {
            throw new ValidationException("Không tìm thấy mã động vật: " + id);
        }
    }

    @Override
    public void update(Animal animal) {
        if (!animals.containsKey(animal.getId())) {
            throw new ValidationException("Không tìm thấy mã động vật: " + animal.getId());
        }
        animals.put(animal.getId(), animal);
    }

    @Override
    public Optional<Animal> findById(String id) {
        return Optional.ofNullable(animals.get(id));
    }

    @Override
    public List<Animal> search(Predicate<Animal> criteria) {
        List<Animal> result = new ArrayList<>();
        for (Animal a : animals.values()) {
            if (criteria.test(a)) {
                result.add(a);
            }
        }
        return result;
    }

    @Override
    public List<Animal> getAll() {
        return new ArrayList<>(animals.values());
    }

    @Override
    public void replaceAll(List<Animal> newAnimals) {
        animals.clear();
        for (Animal a : newAnimals) {
            animals.put(a.getId(), a);
        }
    }

    @Override
    public boolean exists(String id) {
        return animals.containsKey(id);
    }
}
