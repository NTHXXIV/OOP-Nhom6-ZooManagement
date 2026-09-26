package zoo.ui;

import zoo.file.AnimalFileHandler;
import zoo.file.CsvAnimalFileHandler;
import zoo.repository.AnimalRepository;
import zoo.repository.InMemoryAnimalRepository;
import zoo.service.ZooService;

import java.io.IOException;
import java.util.Scanner;

public class ZooApplication {

    public static void main(String[] args) {
        AnimalRepository repository = new InMemoryAnimalRepository();
        AnimalFileHandler fileHandler = new CsvAnimalFileHandler();
        ZooService service = new ZooService(repository, fileHandler);
        ConsoleIO console = new ConsoleIO();
        AnimalPrompter animalPrompter = new AnimalPrompter(console, service);
        Scanner sc = new Scanner(System.in);

        boolean running = true;
        while (running) {
            console.printMainMenu();
            int choice = console.readChoice(sc);
            try {
                switch (choice) {
                    case 1 -> animalPrompter.addAnimal(sc);
                    case 2 -> animalPrompter.deleteAnimal(sc);
                    case 3 -> animalPrompter.editAnimal(sc);
                    case 4 -> animalPrompter.searchAnimals(sc);
                    case 5 -> animalPrompter.listAll();
                    case 6 -> animalPrompter.saveToFile(sc);
                    case 7 -> animalPrompter.loadFromFile(sc);
                    case 0 -> running = false;
                    default -> console.showError("Lựa chọn không hợp lệ.");
                }
            } catch (IOException | RuntimeException e) {
                console.showError(e.getMessage());
            }
        }
        console.showMessage("Tạm biệt.");
    }
}
