package zoo.file;

import zoo.model.Animal;

import java.io.IOException;
import java.util.List;

public interface AnimalFileHandler {
    void save(List<Animal> animals, String filePath) throws IOException;
    List<Animal> load(String filePath) throws IOException;
}
