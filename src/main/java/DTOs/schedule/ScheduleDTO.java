package DTOs.schedule;

import DTOs.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleDTO extends TaskDTO {

    private Date startDatetime;
    private Date endDatetime;

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public ScheduleDTO(Integer id, String description, Integer status, Date startDatetime, Date endDatetime) {
        super(id, description, status);
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    public ScheduleDTO(Date startDatetime, Date endDatetime) {
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    public ScheduleDTO() {
    }


    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm");
        String message = "Schedule Event " +getId()+" --> Description : "+getDescription()+
                "  start at " + simpleDateFormat.format(startDatetime) +
                ", end at =" + simpleDateFormat.format(endDatetime);
        if (endDatetime.before(new Date())){
            message = Constant.ANSI_YELLOW+message+" This event is already over.";
        };
        return message;
    }
}
