import DTOs.Constant;
import DTOs.Helper;
import DTOs.expenditure.ExpenditureDTO;
import DTOs.schedule.ScheduleDTO;
import DTOs.schedule.TaskDTO;
import DTOs.schedule.TodoDTO;
import com.sun.media.sound.InvalidDataException;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import javafx.concurrent.Task;
import Exception.InvalidDateRangeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ScannerProcess {

    public static void processIncomingMessage(Scanner scanner) throws InvalidDataException, InvalidDateRangeException {
        String input = scanner.nextLine();
        if (!input.equalsIgnoreCase("exit")){

                String[] commentList = input.split("-");
                if (commentList.length > 1
                        ){
                    switch (commentList[0].trim().toLowerCase()) {
                        case "show":
                            processShowComment(commentList);
                            break;
                        case "create":
                            processCreateComment(commentList);
                            break;
                        case "done":
                            processDoneComment(commentList);
                            break;
                    }
                }else {
                    System.out.println("This is not a valid comment : '"+input+"'; Please enter a valid comment. You can enter "+Constant.ANSI_PURPLE+"'help'"+ Constant.ANSI_RESET+"to see the comment list");
                }


            processIncomingMessage(scanner);

        }else {
            System.out.println("End of user input, programme stops");
        }
    }

    private static void processDoneComment(String[] commentList){
        Integer id = 0;
        try {
            id = Integer.parseInt(commentList[1]);
            boolean flag = false;
            for (TaskDTO taskDTO : MainClass.listStorage.getTaskDTOS()) {
                if (taskDTO.getId() == id && taskDTO instanceof TodoDTO){
                    flag = true;
                    taskDTO.setStatus(Constant.STATUS_DONE);
                }
            }
            if (flag){
                System.out.println(Constant.ANSI_RESET+"Todo record update successfully!");
            }else {
                System.out.println("Id not found, please enter correct Id");
            }
        }catch (Exception e){
            System.out.println("Id is not valid");
        }


    }
    private static void processShowComment(String[] commentList) throws InvalidDateRangeException {
        String type = commentList[1];
        List<TaskDTO> taskList = MainClass.listStorage.getTaskDTOS();
        List<ExpenditureDTO> expenseList = MainClass.listStorage.getExpenditureDTOS();
        boolean dateFormatError = false;
        Helper helper = new Helper();
        if (commentList.length > 2) {
            try {
                helper = processDateRangeComment(commentList[2]);
            } catch (InvalidDataException e) {
                System.out.println("Date range is wrongly passed. Please check your input again and follow the correct date format.");
                dateFormatError = true;
            }
        }
        if (!dateFormatError) {
            switch (type.trim().toLowerCase()) {
                case "todo":
                    List<TodoDTO> todoList = new ArrayList<>();
                    for (TaskDTO taskDTO : taskList) {
                        if (taskDTO instanceof TodoDTO) {
                            if (helper.getDateFilterType() != null) {
                                switch (helper.getDateFilterType()) {
                                    case 0:
                                        if (((TodoDTO) taskDTO).getDeadline().before(helper.getEndDate()) && ((TodoDTO) taskDTO).getDeadline().after(helper.getStartDate())) {
                                            todoList.add((TodoDTO) taskDTO);
                                        }
                                        break;
                                    case 1:
                                        if (((TodoDTO) taskDTO).getDeadline().after(helper.getStartDate())) {
                                            todoList.add((TodoDTO) taskDTO);
                                        }
                                        break;
                                    case 2:
                                        if (((TodoDTO) taskDTO).getDeadline().before(helper.getStartDate())) {
                                            todoList.add((TodoDTO) taskDTO);
                                        }
                                        break;
                                }
                            } else {
                                todoList.add((TodoDTO) taskDTO);
                            }

                        }

                    }


                    if (!todoList.isEmpty()) {
                        todoList.forEach(todoDTO -> {

                            System.out.println(Constant.ANSI_BLUE + todoDTO);
                        });
                    } else {
                        System.out.println(Constant.ANSI_BLACK + "There is no todo event available.");
                    }
                    break;
                case "schedule":
                    List<ScheduleDTO> scheduleList = new ArrayList<>();
                    for (TaskDTO taskDTO : taskList) {
                        if (taskDTO instanceof ScheduleDTO) {
                            if (helper.getDateFilterType() != null) {
                                switch (helper.getDateFilterType()) {
                                    case 0:
                                        if (((ScheduleDTO) taskDTO).getStartDatetime().before(helper.getEndDate()) && ((ScheduleDTO) taskDTO).getStartDatetime().after(helper.getStartDate())) {
                                            scheduleList.add((ScheduleDTO) taskDTO);
                                        }
                                        break;
                                    case 1:
                                        if (((ScheduleDTO) taskDTO).getStartDatetime().after(helper.getStartDate())) {
                                            scheduleList.add((ScheduleDTO) taskDTO);
                                        }
                                        break;
                                    case 2:
                                        if (((ScheduleDTO) taskDTO).getStartDatetime().before(helper.getStartDate())) {
                                            scheduleList.add((ScheduleDTO) taskDTO);
                                        }
                                        break;
                                }
                            } else {
                                scheduleList.add((ScheduleDTO) taskDTO);
                            }
                        }
                    }
                    if (!scheduleList.isEmpty()) {
                        scheduleList.forEach(scheduleDTO -> System.out.println(Constant.ANSI_BLUE + scheduleDTO.toString()));
                    } else {
                        System.out.println(Constant.ANSI_BLACK + "There is no schedule event available.");
                    }
                    break;

                case "task":
                    if (!taskList.isEmpty()) {
                        if (helper.getDateFilterType() != null) {
                            taskList = dateRangeListProcess(taskList, helper);
                        } else {
                            taskList.forEach(taskDTO -> {
                                System.out.println(taskDTO.toString());
                            });
                        }
                    } else {
                        System.out.println(Constant.ANSI_BLACK + "There is no task available.");
                    }

                    break;
                case "expense":
                    List<ExpenditureDTO> expenditureDTOS = new ArrayList<>();

                    if (expenseList!=null){
                        if (helper.getDateFilterType() != null) {
                          //  taskList = dateRangeListProcess(taskList, helper);
                        } else {
                            expenseList.forEach(expenditureDTO -> {
                               if (expenditureDTO.getType()==Constant.EXPENSE_TYPE){
                                   expenditureDTOS.add(expenditureDTO);
                               }
                            });
                        }
                        if (!expenditureDTOS.isEmpty())
                        expenditureDTOS.forEach(expenditureDTO -> System.out.println(expenditureDTO.toString()));
                        else System.out.println(Constant.ANSI_BLACK + "There is no expense record available.");
                    }else     System.out.println(Constant.ANSI_BLACK + "There is no expense record available.");
                    break;
                case "income":
                    List<ExpenditureDTO> expenditureIncomeDTOS = new ArrayList<>();

                    if (expenseList!=null){
                        if (helper.getDateFilterType() != null) {
                            //  taskList = dateRangeListProcess(taskList, helper);
                        } else {
                            expenseList.forEach(expenditureDTO -> {
                                if (expenditureDTO.getType()==Constant.INCOME_TYPE){
                                    expenditureIncomeDTOS.add(expenditureDTO);
                                }
                            });
                        }
                        if (!expenditureIncomeDTOS.isEmpty())
                        expenditureIncomeDTOS.forEach(expenditureDTO -> System.out.println(expenditureDTO.toString()));
                        else     System.out.println(Constant.ANSI_BLACK + "There is no income record available.");
                    }else     System.out.println(Constant.ANSI_BLACK + "There is no income record available.");
                    break;
            }
        }
    }

    private static List<TaskDTO> dateRangeListProcess(List<TaskDTO> taskList,Helper helper){
        List<TaskDTO> taskDTOS = new ArrayList<>();
        for (TaskDTO taskDTO : taskList) {
            if (taskDTO instanceof ScheduleDTO){
                if (helper.getDateFilterType()!=null){
                    switch (helper.getDateFilterType()){
                        case 0:
                            if ( ((ScheduleDTO) taskDTO).getStartDatetime().before(helper.getEndDate()) && ((ScheduleDTO) taskDTO).getStartDatetime().after(helper.getStartDate())){
                                taskDTOS.add(taskDTO);
                            }
                            break;
                        case 1 :
                            if (((ScheduleDTO) taskDTO).getStartDatetime().after(helper.getStartDate())){
                                taskDTOS.add(taskDTO);
                            }
                            break;
                        case 2:
                            if (((ScheduleDTO) taskDTO).getStartDatetime().before(helper.getStartDate())){
                                taskDTOS.add( taskDTO);
                            }
                            break;
                    }
                }else {
                    taskDTOS.add(taskDTO);
                }
            }else if (taskDTO instanceof TodoDTO){
                if (helper.getDateFilterType()!=null){
                    switch (helper.getDateFilterType()){
                        case 0:
                            if (((TodoDTO) taskDTO).getDeadline().before(helper.getEndDate()) && ((TodoDTO) taskDTO).getDeadline().after(helper.getStartDate())){
                                taskDTOS.add( taskDTO);
                            }
                            break;
                        case 1 :
                            if (((TodoDTO) taskDTO).getDeadline().after(helper.getStartDate())){
                                taskDTOS.add(taskDTO);
                            }
                            break;
                        case 2:
                            if (((TodoDTO) taskDTO).getDeadline().before(helper.getStartDate())){
                                taskDTOS.add( taskDTO);
                            }
                            break;
                    }
                }else {
                    taskDTOS.add( taskDTO);
                }

            }
        }

        return taskDTOS;
    }
    private static void processCreateComment(String[] commentList){
        if (commentList.length>2){
            String type = commentList[1];
            String[] createStatement;
            String createString;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd hh:mm");
            SimpleDateFormat simpleDateFormatExpense = new SimpleDateFormat("yyyyMMdd");
            switch (type.trim().toLowerCase()){
                case "todo":
                    createString = commentList[2];
                    createStatement = createString.split("\\|");
                    TodoDTO todoDTO = new TodoDTO();

                    try {
                        todoDTO.setId(MainClass.maxScheduleIdNumber);
                        MainClass.maxScheduleIdNumber++;
                        todoDTO.setDescription(createStatement[0]);
                        todoDTO.setDeadline(simpleDateFormat.parse(createStatement[1]));
                        todoDTO.setStatus(Constant.STATUS_NOT_DONE);
                        MainClass.listStorage.getTaskDTOS().add(todoDTO);
                        System.out.println("Your todo record added successfully!");
                    } catch (ParseException e) {
                        System.out.println("Date format is wrong, please pass in the correct date format, example 20181231 10:30");
                    }

                    break;
                case "schedule":
                    createString = commentList[2];
                    createStatement = createString.split("\\|");
                    ScheduleDTO scheduleDTO = new ScheduleDTO();
                    if (createStatement.length>2){
                        try {
                            scheduleDTO.setId(MainClass.maxScheduleIdNumber);
                            MainClass.maxScheduleIdNumber++;
                            scheduleDTO.setDescription(createStatement[0]);
                            scheduleDTO.setStartDatetime(simpleDateFormat.parse(createStatement[1]));
                            scheduleDTO.setEndDatetime(simpleDateFormat.parse(createStatement[2]));
                            MainClass.listStorage.getTaskDTOS().add(scheduleDTO);
                            System.out.println("Your schedule record added successfully!");
                        } catch (ParseException e) {
                            System.out.println("Date format is wrong, please pass in the correct date format, example 20181231 10:30");
                        }
                    }else {
                        System.out.println("Your comment format is not correct, please check again!");
                    }
                    break;

                case "expense":
                    createString = commentList[2];
                    createStatement = createString.split("\\|");
                    ExpenditureDTO expenseDTO = new ExpenditureDTO();
                    if (createStatement.length>2){
                        try {
                            expenseDTO.setId(MainClass.maxExpenditureIdNumber);
                            MainClass.maxExpenditureIdNumber++;
                            expenseDTO.setDescription(createStatement[0]);
                            expenseDTO.setAmount(Double.parseDouble(createStatement[1]));
                            expenseDTO.setRecordDate(simpleDateFormatExpense.parse(createStatement[2]));
                            expenseDTO.setType(Constant.EXPENSE_TYPE);

                            MainClass.listStorage.getExpenditureDTOS().add(expenseDTO);
                            System.out.println("Your expenditure record added successfully!");
                        } catch (ParseException e) {
                            System.out.println("You input information is not in the correct format, please check again");
                        }
                    }else {
                        System.out.println("Your comment format is not correct, please check again!");
                    }
                    break;

                case "income":
                    createString = commentList[2];
                    createStatement = createString.split("\\|");
                    ExpenditureDTO incomeDTO = new ExpenditureDTO();
                    if (createStatement.length>2){
                        try {
                            incomeDTO.setId(MainClass.maxExpenditureIdNumber);
                            MainClass.maxExpenditureIdNumber++;
                            incomeDTO.setDescription(createStatement[0]);
                            incomeDTO.setAmount(Double.parseDouble(createStatement[1]));
                            incomeDTO.setRecordDate(simpleDateFormatExpense.parse(createStatement[2]));
                            incomeDTO.setType(Constant.INCOME_TYPE);

                            MainClass.listStorage.getExpenditureDTOS().add(incomeDTO);
                            System.out.println("Your expenditure record added successfully!");
                        } catch (ParseException e) {
                            System.out.println("You input information is not in the correct format, please check again");
                        }
                    }else {
                        System.out.println("Your comment format is not correct, please check again!");
                    }
                    break;
                    default:
                        System.out.println("Your comment is not correct, please check again!");
            }
        }else {
            System.out.println("Input create statement is not correct, please check again");
        }


    }

    private static Helper processDateRangeComment( String dateComment) throws InvalidDataException {
        Helper helper = new Helper();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhh:mm");
        if (dateComment.startsWith("From") && dateComment.contains("To")){
            dateComment = dateComment.replace("From","");
            dateComment = dateComment.replace("To","|");
            String[] dateList = dateComment.split("|");

            try {
                Date fromDate = simpleDateFormat.parse(dateList[0].trim());
                Date toDate = simpleDateFormat.parse(dateList[1].trim());

                helper.setDateFilterType(Constant.SEARCH_DATE_TYPE_FROM);
                helper.setStartDate(fromDate);
                helper.setEndDate(toDate);
            } catch (ParseException e) {
                System.out.println("Date range is wrongly passed. Please check your input again and follow the correct date format.");
            }
        }else if(dateComment.startsWith("Before")){
            dateComment = dateComment.replace("Before","");
            try {
                Date beforeDate = simpleDateFormat.parse(dateComment.trim());

                helper.setDateFilterType(Constant.SEARCH_DATE_TYPE_BEFORE);
                helper.setStartDate(beforeDate);
            } catch (ParseException e) {
                System.out.println("Date range is wrongly passed. Please check your input again and follow the correct date format.");
            }
        }else if (dateComment.startsWith("After")){
            dateComment = dateComment.replace("After","");
            try {
                Date afterDate = simpleDateFormat.parse(dateComment.trim());

                helper.setDateFilterType(Constant.SEARCH_DATE_TYPE_AFTER);
                helper.setStartDate(afterDate);
            } catch (ParseException e) {
                System.out.println("Date range is wrongly passed. Please check your input again and follow the correct date format.");
            }
        }else {
            System.out.println("Search date format is not match with current method we provide, will return list without search date applied.");
        }
        return helper;
    }

}
