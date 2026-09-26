package zoo.ui;

import zoo.exception.ValidationException;
import zoo.model.Animal;
import zoo.model.AnimalType;
import zoo.model.Bird;
import zoo.model.FeedingSchedule;
import zoo.model.Gender;
import zoo.model.Habitat;
import zoo.model.HealthStatus;
import zoo.model.Mammal;
import zoo.model.Reptile;
import zoo.service.ZooService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Console I/O + flow for Animal use cases (add/delete/edit/search/list/save/load). No persistence logic itself. */
public class AnimalPrompter {
    private final ConsoleIO console;
    private final ZooService service;

    public AnimalPrompter(ConsoleIO console, ZooService service) {
        this.console = console;
        this.service = service;
    }

    public void addAnimal(Scanner sc) {
        String id = promptUniqueAnimalId(sc);
        Animal a = promptNewAnimal(sc, id);
        service.addAnimal(a);
        console.showMessage("Đã thêm động vật.");
    }

    public void deleteAnimal(Scanner sc) {
        String id = console.promptText(sc, "Mã động vật cần xóa");
        service.deleteAnimal(id);
        console.showMessage("Đã xóa động vật.");
    }

    public void editAnimal(Scanner sc) {
        String id = console.promptText(sc, "Mã động vật cần sửa");
        Animal a = service.getAnimal(id);
        console.showMessage("Hiện tại: " + a);
        console.showMessage("Để trống nếu muốn giữ nguyên giá trị cũ.");

        String name = console.promptText(sc, "Tên mới");
        if (!name.isBlank()) a.setName(name);

        String dob = console.promptText(sc, "Ngày sinh mới (yyyy-mm-dd)");
        if (!dob.isBlank()) a.setDateOfBirth(LocalDate.parse(dob));

        a.setHealthStatus(promptHealthStatusOptional(sc, a.getHealthStatus()));

        service.updateAnimal(a);
        console.showMessage("Đã cập nhật động vật.");
    }

    public void searchAnimals(Scanner sc) {
        console.showMessage("Tìm theo: 1) tên  2) loài  3) tình trạng sức khỏe");
        int mode = console.readChoice(sc);
        List<Animal> results = switch (mode) {
            case 1 -> service.searchByName(console.promptText(sc, "Tên chứa"));
            case 2 -> service.searchBySpecies(console.promptText(sc, "Loài chứa"));
            case 3 -> service.searchByHealthStatus(promptHealthStatus(sc));
            default -> List.of();
        };
        displayList(results);
    }

    public void listAll() {
        displayList(service.getAll());
    }

    public void saveToFile(Scanner sc) throws IOException {
        String path = console.promptText(sc, "Đường dẫn file để lưu (vd: animals_sample.csv)");
        service.saveToFile(path);
        console.showMessage("Đã lưu.");
    }

    public void loadFromFile(Scanner sc) throws IOException {
        String path = console.promptText(sc, "Đường dẫn file để tải (vd: animals_sample.csv)");
        service.loadFromFile(path);
        console.showMessage("Đã tải xong.");
    }

    private String promptUniqueAnimalId(Scanner sc) {
        String id = console.promptText(sc, "Mã động vật");
        while (service.animalIdExists(id)) {
            console.showError("Mã động vật đã tồn tại: " + id + ". Vui lòng nhập mã khác.");
            id = console.promptText(sc, "Mã động vật");
        }
        return id;
    }

    private Animal promptNewAnimal(Scanner sc, String id) {
        String name = console.promptText(sc, "Tên");

        AnimalType type = promptAnimalType(sc);
        Gender gender = promptGender(sc);
        LocalDate dob = LocalDate.parse(console.promptText(sc, "Ngày sinh (yyyy-mm-dd)"));

        String habitatId = console.promptText(sc, "Mã khu vực sống");
        String habitatName = console.promptText(sc, "Tên khu vực sống");
        String habitatType = console.promptText(sc, "Loại khu vực sống (vd: Savanna)");
        int habitatCapacity = Integer.parseInt(console.promptText(sc, "Sức chứa khu vực sống"));
        Habitat habitat = new Habitat(habitatId, habitatName, habitatType, habitatCapacity);

        HealthStatus healthStatus = promptHealthStatus(sc);

        String foodType = console.promptText(sc, "Loại thức ăn");
        int timesPerDay = Integer.parseInt(console.promptText(sc, "Số lần cho ăn mỗi ngày"));
        List<LocalTime> feedingTimes = new ArrayList<>();
        for (int i = 0; i < timesPerDay; i++) {
            feedingTimes.add(LocalTime.parse(console.promptText(sc, "Giờ cho ăn #" + (i + 1) + " (HH:mm)")));
        }
        FeedingSchedule feedingSchedule = new FeedingSchedule(foodType, timesPerDay, feedingTimes);

        return switch (type) {
            case MAMMAL -> new Mammal(id, name, gender, dob, habitat, healthStatus, feedingSchedule,
                    Double.parseDouble(console.promptText(sc, "Độ dài lông (cm)")));
            case BIRD -> new Bird(id, name, gender, dob, habitat, healthStatus, feedingSchedule,
                    Double.parseDouble(console.promptText(sc, "Sải cánh (cm)")));
            case REPTILE -> new Reptile(id, name, gender, dob, habitat, healthStatus, feedingSchedule,
                    Boolean.parseBoolean(console.promptText(sc, "Có nọc độc (true/false)")));
        };
    }

    private AnimalType promptAnimalType(Scanner sc) {
        System.out.println("Loại động vật:");
        System.out.println("  1. Thú (Mammal)");
        System.out.println("  2. Chim (Bird)");
        System.out.println("  3. Bò sát (Reptile)");
        System.out.print("Chọn (1-3): ");
        return switch (console.readChoice(sc)) {
            case 1 -> AnimalType.MAMMAL;
            case 2 -> AnimalType.BIRD;
            case 3 -> AnimalType.REPTILE;
            default -> throw new ValidationException("Lựa chọn loại động vật không hợp lệ");
        };
    }

    private Gender promptGender(Scanner sc) {
        System.out.println("Giới tính:");
        System.out.println("  1. Đực");
        System.out.println("  2. Cái");
        System.out.println("  3. Không rõ");
        System.out.print("Chọn (1-3): ");
        return switch (console.readChoice(sc)) {
            case 1 -> Gender.MALE;
            case 2 -> Gender.FEMALE;
            case 3 -> Gender.UNKNOWN;
            default -> throw new ValidationException("Lựa chọn giới tính không hợp lệ");
        };
    }

    private void printHealthStatusOptions() {
        System.out.println("Tình trạng sức khỏe:");
        System.out.println("  1. Khỏe mạnh");
        System.out.println("  2. Đang bị bệnh");
        System.out.println("  3. Bị thương");
        System.out.println("  4. Đang điều trị");
        System.out.println("  5. Cách ly");
    }

    private HealthStatus mapHealthStatusChoice(int choice) {
        return switch (choice) {
            case 1 -> HealthStatus.HEALTHY;
            case 2 -> HealthStatus.SICK;
            case 3 -> HealthStatus.INJURED;
            case 4 -> HealthStatus.UNDER_TREATMENT;
            case 5 -> HealthStatus.QUARANTINE;
            default -> throw new ValidationException("Lựa chọn tình trạng sức khỏe không hợp lệ");
        };
    }

    private HealthStatus promptHealthStatus(Scanner sc) {
        printHealthStatusOptions();
        System.out.print("Chọn (1-5): ");
        return mapHealthStatusChoice(console.readChoice(sc));
    }

    /** Returns {@code current} when the user picks 0 (keep unchanged). */
    private HealthStatus promptHealthStatusOptional(Scanner sc, HealthStatus current) {
        System.out.println("Tình trạng sức khỏe hiện tại: " + current);
        System.out.println("  0. Giữ nguyên");
        printHealthStatusOptions();
        System.out.print("Chọn (0-5): ");
        int choice = console.readChoice(sc);
        return choice == 0 ? current : mapHealthStatusChoice(choice);
    }

    private void displayAnimal(Animal a) {
        System.out.println(a);
    }

    private void displayList(List<Animal> animals) {
        if (animals.isEmpty()) {
            System.out.println("(không tìm thấy động vật nào)");
            return;
        }
        for (Animal a : animals) {
            displayAnimal(a);
        }
    }
}
