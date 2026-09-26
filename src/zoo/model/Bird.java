package zoo.model;

import java.time.LocalDate;

public class Bird extends Animal {
    private double wingspanCm;

    public Bird(String id, String name, Gender gender, LocalDate dateOfBirth,
                Habitat habitat, HealthStatus healthStatus, FeedingSchedule feedingSchedule,
                double wingspanCm) {
        super(id, name, gender, dateOfBirth, habitat, healthStatus, feedingSchedule);
        this.wingspanCm = wingspanCm;
    }

    public double getWingspanCm() { return wingspanCm; }
    public void setWingspanCm(double wingspanCm) { this.wingspanCm = wingspanCm; }

    @Override
    public AnimalType getType() { return AnimalType.BIRD; }

    @Override
    public String getSpecies() { return "Chim"; }

    @Override
    public String getDietInfo() { return "Chế độ ăn (hạt/côn trùng) - " + getFeedingSchedule().getFoodType(); }

    @Override
    public String getExtraFieldForCsv() { return String.valueOf(wingspanCm); }
}
