package zoo.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FeedingSchedule {
    private String foodType;
    private int timesPerDay;
    private List<LocalTime> feedingTimes;

    public FeedingSchedule(String foodType, int timesPerDay, List<LocalTime> feedingTimes) {
        this.foodType = foodType;
        this.timesPerDay = timesPerDay;
        this.feedingTimes = feedingTimes != null ? feedingTimes : new ArrayList<>();
    }

    public String getFoodType() { return foodType; }
    public void setFoodType(String foodType) { this.foodType = foodType; }
    public int getTimesPerDay() { return timesPerDay; }
    public void setTimesPerDay(int timesPerDay) { this.timesPerDay = timesPerDay; }
    public List<LocalTime> getFeedingTimes() { return feedingTimes; }
    public void setFeedingTimes(List<LocalTime> feedingTimes) { this.feedingTimes = feedingTimes; }

    public String toCsvField() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < feedingTimes.size(); i++) {
            if (i > 0) sb.append(';');
            sb.append(feedingTimes.get(i));
        }
        return sb.toString();
    }

    public static FeedingSchedule fromCsvFields(String foodType, int timesPerDay, String timesField) {
        List<LocalTime> times = new ArrayList<>();
        if (timesField != null && !timesField.isBlank()) {
            for (String t : timesField.split(";")) {
                times.add(LocalTime.parse(t.trim()));
            }
        }
        return new FeedingSchedule(foodType, timesPerDay, times);
    }

    @Override
    public String toString() {
        return foodType + " x" + timesPerDay + "/ngày " + feedingTimes;
    }
}
