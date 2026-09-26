package zoo.service;

import zoo.exception.ValidationException;
import zoo.file.AnimalFileHandler;
import zoo.file.LoadResult;
import zoo.model.Animal;
import zoo.model.HealthStatus;
import zoo.repository.AnimalRepository;

import java.io.IOException;
import java.util.List;

public class ZooService {
    private final AnimalRepository repository;
    private final AnimalFileHandler fileHandler;

    public ZooService(AnimalRepository repository, AnimalFileHandler fileHandler) {
        this.repository = repository;
        this.fileHandler = fileHandler;
    }

    public void addAnimal(Animal animal) {
        repository.add(animal);
    }

    public boolean animalIdExists(String id) {
        return repository.exists(id);
    }

    public void deleteAnimal(String id) {
        repository.remove(id);
    }

    public void updateAnimal(Animal animal) {
        if (!repository.exists(animal.getId())) {
            throw new ValidationException("Không tìm thấy mã động vật: " + animal.getId());
        }
        repository.update(animal);
    }

    public Animal getAnimal(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy mã động vật: " + id));
    }

    public List<Animal> searchById(String keyword) {
        String lower = keyword.toLowerCase();
        return repository.search(a -> a.getId().toLowerCase().contains(lower));
    }

    public List<Animal> searchByName(String keyword) {
        String lower = keyword.toLowerCase();
        return repository.search(a -> a.getName().toLowerCase().contains(lower));
    }

    public List<Animal> searchBySpecies(String species) {
        String lower = species.toLowerCase();
        return repository.search(a -> a.getSpecies().toLowerCase().contains(lower));
    }

    public List<Animal> searchByHabitat(String keyword) {
        String lower = keyword.toLowerCase();
        return repository.search(a -> a.getHabitat() != null && a.getHabitat().getName().toLowerCase().contains(lower));
    }

    public List<Animal> searchByHealthStatus(HealthStatus status) {
        return repository.search(a -> a.getHealthStatus() == status);
    }

    public List<Animal> getAll() {
        return repository.getAll();
    }

    public void saveToFile(String filePath) throws IOException {
        fileHandler.save(repository.getAll(), filePath);
    }

    public LoadResult loadFromFile(String filePath) throws IOException {
        LoadResult result = fileHandler.load(filePath);
        repository.replaceAll(result.animals());
        return result;
    }
}
