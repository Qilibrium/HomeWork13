public class Task {
    private int userId;
    private int id;
    private String title;
    private boolean completed;

    @Override
    public String toString() {
        return "Task{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
