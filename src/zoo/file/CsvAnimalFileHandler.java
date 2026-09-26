package zoo.file;

import zoo.model.Animal;
import zoo.model.AnimalType;
import zoo.model.Bird;
import zoo.model.FeedingSchedule;
import zoo.model.Gender;
import zoo.model.Habitat;
import zoo.model.HealthStatus;
import zoo.model.Mammal;
import zoo.model.Reptile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stores animal records as CSV. One row per animal, denormalized habitat fields
 * so the file stays self-contained (no separate habitat file to join).
 */
public class CsvAnimalFileHandler implements AnimalFileHandler {
    private static final String DELIMITER = ",";
    private static final String HEADER =
            "id,type,name,gender,dob,habitatId,habitatName,habitatType,habitatCapacity," +
            "healthStatus,foodType,timesPerDay,feedingTimes,extraField";

    @Override
    public void save(List<Animal> animals, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(HEADER);
            writer.newLine();
            for (Animal a : animals) {
                writer.write(toCsvRow(a));
                writer.newLine();
            }
        }
    }

    @Override
    public LoadResult load(String filePath) throws IOException {
        List<Animal> animals = new ArrayList<>();
        List<String> skippedRows = new ArrayList<>();
        Set<String> seenIds = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;
                try {
                    Animal a = fromCsvRow(line);
                    if (!seenIds.add(a.getId())) {
                        skippedRows.add("Dòng " + lineNumber + ": trùng mã động vật " + a.getId());
                        continue;
                    }
                    animals.add(a);
                } catch (RuntimeException e) {
                    skippedRows.add("Dòng " + lineNumber + ": " + e.getMessage());
                }
            }
        }
        return new LoadResult(animals, skippedRows);
    }

    private String toCsvRow(Animal a) {
        Habitat h = a.getHabitat();
        FeedingSchedule fs = a.getFeedingSchedule();
        return String.join(DELIMITER,
                a.getId(),
                a.getType().name(),
                a.getName(),
                a.getGender().name(),
                a.getDateOfBirth().toString(),
                h != null ? h.getHabitatId() : "",
                h != null ? h.getName() : "",
                h != null ? h.getType() : "",
                h != null ? String.valueOf(h.getCapacity()) : "0",
                a.getHealthStatus().name(),
                fs.getFoodType(),
                String.valueOf(fs.getTimesPerDay()),
                fs.toCsvField(),
                a.getExtraFieldForCsv()
        );
    }

    private Animal fromCsvRow(String line) {
        String[] f = line.split(DELIMITER, -1);
        String id = f[0];
        AnimalType type = AnimalType.valueOf(f[1]);
        String name = f[2];
        Gender gender = Gender.valueOf(f[3]);
        LocalDate dob = LocalDate.parse(f[4]);
        Habitat habitat = new Habitat(f[5], f[6], f[7], Integer.parseInt(f[8]));
        HealthStatus healthStatus = HealthStatus.valueOf(f[9]);
        String foodType = f[10];
        int timesPerDay = Integer.parseInt(f[11]);
        String feedingTimesField = f[12];
        String extra = f[13];

        FeedingSchedule feedingSchedule = FeedingSchedule.fromCsvFields(foodType, timesPerDay, feedingTimesField);

        return switch (type) {
            case MAMMAL -> new Mammal(id, name, gender, dob, habitat, healthStatus, feedingSchedule,
                    Double.parseDouble(extra));
            case BIRD -> new Bird(id, name, gender, dob, habitat, healthStatus, feedingSchedule,
                    Double.parseDouble(extra));
            case REPTILE -> new Reptile(id, name, gender, dob, habitat, healthStatus, feedingSchedule,
                    Boolean.parseBoolean(extra));
        };
    }
}
