package zoo.model;

import zoo.exception.ValidationException;

public class Habitat {
    private final String habitatId;
    private String name;
    private String type;
    private int capacity;

    public Habitat(String habitatId, String name, String type, int capacity) {
        this.habitatId = habitatId;
        setName(name);
        setType(type);
        setCapacity(capacity);
    }

    public String getHabitatId() { return habitatId; }
    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Tên khu vực sống không được để trống");
        }
        this.name = name;
    }
    public String getType() { return type; }
    public void setType(String type) {
        if (type == null || type.isBlank()) {
            throw new ValidationException("Loại khu vực sống không được để trống");
        }
        this.type = type;
    }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new ValidationException("Sức chứa khu vực sống phải lớn hơn 0");
        }
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return name + " (" + type + ", sức chứa " + capacity + ")";
    }
}
