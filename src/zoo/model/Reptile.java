package zoo.model;

import java.time.LocalDate;

public class Reptile extends Animal {
    private boolean venomous;

    public Reptile(String id, String name, Gender gender, LocalDate dateOfBirth,
                   Habitat habitat, HealthStatus healthStatus, FeedingSchedule feedingSchedule,
                   boolean venomous) {
        super(id, name, gender, dateOfBirth, habitat, healthStatus, feedingSchedule);
        this.venomous = venomous;
    }

    public boolean isVenomous() { return venomous; }
    public void setVenomous(boolean venomous) { this.venomous = venomous; }

    @Override
    public AnimalType getType() { return AnimalType.REPTILE; }

    @Override
    public String getSpecies() { return "Bò sát"; }

    @Override
    public String getDietInfo() {
        return (venomous ? "Chế độ ăn (loài săn mồi có nọc độc) - " : "Chế độ ăn - ") + getFeedingSchedule().getFoodType();
    }

    @Override
    public String getExtraFieldForCsv() { return String.valueOf(venomous); }
}
