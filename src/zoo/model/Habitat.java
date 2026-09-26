package zoo.model;

public class Habitat {
    private final String habitatId;
    private String name;
    private String type;
    private int capacity;

    public Habitat(String habitatId, String name, String type, int capacity) {
        this.habitatId = habitatId;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
    }

    public String getHabitatId() { return habitatId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return name + " (" + type + ", sức chứa " + capacity + ")";
    }
}
