package zoo.model;

import zoo.exception.ValidationException;

import java.time.LocalDate;

public class Mammal extends Animal {
    private double furLengthCm;

    public Mammal(String id, String name, Gender gender, LocalDate dateOfBirth,
                  Habitat habitat, HealthStatus healthStatus, FeedingSchedule feedingSchedule,
                  double furLengthCm) {
        super(id, name, gender, dateOfBirth, habitat, healthStatus, feedingSchedule);
        setFurLengthCm(furLengthCm);
    }

    public double getFurLengthCm() { return furLengthCm; }
    public void setFurLengthCm(double furLengthCm) {
        if (furLengthCm < 0) {
            throw new ValidationException("Độ dài lông không được âm");
        }
        this.furLengthCm = furLengthCm;
    }

    @Override
    public AnimalType getType() { return AnimalType.MAMMAL; }

    @Override
    public String getSpecies() { return "Thú"; }

    @Override
    public String getDietInfo() { return "Chế độ ăn (ăn cỏ/ăn thịt) - " + getFeedingSchedule().getFoodType(); }

    @Override
    public String getExtraFieldForCsv() { return String.valueOf(furLengthCm); }
}
