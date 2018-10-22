package DTOs.schedule;

import DTOs.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TodoDTO extends TaskDTO{

    private Date deadline;

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public TodoDTO(Integer id, String description, Integer status, Date deadline) {
        super(id, description, status);
        this.deadline = deadline;
    }

    public TodoDTO(Date deadline) {
        this.deadline = deadline;
    }

    public TodoDTO() {
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm");
        String message = "Todo Event id = " + getId() +" --> Description : "+getDescription()+
                "Deadline = " + simpleDateFormat.format(deadline) ;

        if (getStatus() ==Constant.STATUS_DONE){
            message  = Constant.ANSI_GREEN+ message + " This todo is already done!";
        }
        if (deadline.before(new Date())){
            message = Constant.ANSI_RED+message+" This event is pass deadline already.";
        };
        return message;

    }
}
