package zoo.file;

import zoo.model.Animal;

import java.util.List;

public record LoadResult(List<Animal> animals, List<String> skippedRows) {
}
