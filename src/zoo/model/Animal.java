package zoo.model;

import zoo.exception.ValidationException;

import java.time.LocalDate;
import java.time.Period;

public abstract class Animal {
    private final String id;
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Habitat habitat;
    private HealthStatus healthStatus;
    private FeedingSchedule feedingSchedule;

    protected Animal(String id, String name, Gender gender, LocalDate dateOfBirth,
                      Habitat habitat, HealthStatus healthStatus, FeedingSchedule feedingSchedule) {
        if (id == null || id.isBlank()) {
            throw new ValidationException("Mã động vật không được để trống");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("Tên động vật không được để trống");
        }
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new ValidationException("Ngày sinh không được để trống hoặc ở tương lai");
        }
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.habitat = habitat;
        this.healthStatus = healthStatus;
        this.feedingSchedule = feedingSchedule;
    }

    public abstract AnimalType getType();
    public abstract String getSpecies();
    public abstract String getDietInfo();

    /** Subclass-specific extra value serialized into the CSV "extra" column. */
    public abstract String getExtraFieldForCsv();

    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Tên động vật không được để trống");
        }
        this.name = name;
    }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new ValidationException("Ngày sinh không được để trống hoặc ở tương lai");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public Habitat getHabitat() { return habitat; }
    public void setHabitat(Habitat habitat) { this.habitat = habitat; }

    public HealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }

    public FeedingSchedule getFeedingSchedule() { return feedingSchedule; }
    public void setFeedingSchedule(FeedingSchedule feedingSchedule) { this.feedingSchedule = feedingSchedule; }

    public int getAgeInYears() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s, %s, tuổi %d, sinh %s) - khu sống: %s - sức khỏe: %s - %s",
                id, name, getType(), gender, getAgeInYears(), dateOfBirth,
                habitat != null ? habitat.getName() : "Không rõ", healthStatus, getDietInfo());
    }
}
