package DTOs;

import DTOs.expenditure.ExpenditureDTO;
import DTOs.schedule.ScheduleDTO;
import DTOs.schedule.TaskDTO;
import DTOs.schedule.TodoDTO;

import java.util.Date;
import java.util.List;

public class GeneralList {
    private List<TaskDTO> taskDTOS;
    private List<ExpenditureDTO> expenditureDTOS;

    public List<TaskDTO> getTaskDTOS() {
        return taskDTOS;
    }

    public List<ExpenditureDTO> getExpenditureDTOS() {
        return expenditureDTOS;
    }

    public void setExpenditureDTOS(List<ExpenditureDTO> expenditureDTOS) {
        this.expenditureDTOS = expenditureDTOS;
    }

    public void setTaskDTOS(List<TaskDTO> taskDTOS) {
        this.taskDTOS = taskDTOS;
    }

    public GeneralList(List<TaskDTO> taskDTOS, List<ExpenditureDTO> expenditureDTOS) {
        this.taskDTOS = taskDTOS;
        this.expenditureDTOS = expenditureDTOS;
    }

    public GeneralList() {
    }

    @Override
    public String toString() {
        Double totalExpense = 0.0;
        Double totalIncome = 0.0;

        for (ExpenditureDTO expenditureDTO : expenditureDTOS) {
            if (expenditureDTO.getType() == Constant.EXPENSE_TYPE){
                totalExpense+= expenditureDTO.getAmount();
            }else if (expenditureDTO.getType() == Constant.INCOME_TYPE){
                totalIncome+=expenditureDTO.getAmount();
            }
        }
        Integer numberOfTodoEvent = 0;
        Integer numberOfScheduleEvent = 0;
        Integer numberOfPassedTodoEvent=0;
        Integer numberOfTodoEventNotDone = 0;
        Integer numberOfPassedScheduleEvent=0;
        for (TaskDTO taskDTO : taskDTOS) {
            if (taskDTO instanceof TodoDTO){
                Date deadline = ((TodoDTO) taskDTO).getDeadline();
                if (deadline.before(new Date())){
                    numberOfPassedTodoEvent++;
                }
                if (taskDTO.getStatus() == Constant.STATUS_NOT_DONE){
                    numberOfTodoEventNotDone++;
                }
                numberOfTodoEvent++;
            }else if (taskDTO instanceof ScheduleDTO){
                Date endDate = ((ScheduleDTO) taskDTO).getEndDatetime();
                if (endDate.before(new Date())){
                    numberOfPassedScheduleEvent++;
                }
                numberOfScheduleEvent++;
            }
        }
        return Constant.ANSI_GREEN+"Summary Information : You have total \n"
                +expenditureDTOS.size() +" expenditure records, total recorded expenses is $"+totalExpense +", total income is $"+totalIncome+".\n" +
                numberOfTodoEvent +" todo events, there are "+numberOfTodoEventNotDone+" pending todo events and "+numberOfPassedTodoEvent +" already passed deadline.\n" +
                numberOfScheduleEvent +" schedule events, there are "+numberOfPassedScheduleEvent +" already passed end date.";
    }
}
