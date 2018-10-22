package DTOs.schedule;

public class TaskDTO {

    private Integer id;
    private String description;
    //0 - is not done, 1-is done
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public TaskDTO(Integer id, String description, Integer status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public TaskDTO() {
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
