import java.time.LocalTime;

public class Task {

    private String title;
    private LocalTime time;
    private String priority;
    private boolean completed;

    public Task(String title, LocalTime time, String priority) {
        this.title = title;
        this.time = time;
        this.priority = priority;
        this.completed = false;
    }

    public void toggleCompleted() {
        completed = !completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getTitle() {
        return title;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getPriority() {
        return priority;
    }
}


