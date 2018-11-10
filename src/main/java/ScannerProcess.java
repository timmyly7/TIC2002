import DTOs.Constant;
import DTOs.Helper;
import DTOs.expenditure.ExpenditureDTO;
import DTOs.schedule.ScheduleDTO;
import DTOs.schedule.TaskDTO;
import DTOs.schedule.TodoDTO;
import com.sun.media.sound.InvalidDataException;
import Exception.InvalidDateRangeException;
import Exception.InvalidCommandException;
import com.sun.media.sound.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ScannerProcess {

    public static void processIncomingMessage(Scanner scanner) throws InvalidDataException, InvalidDateRangeException, InvalidCommandException {
        String input = scanner.nextLine();
        if (!input.equalsIgnoreCase("exit")){

                String[] commentList = input.split("-");

                    switch (commentList[0].trim().toLowerCase()) {
                        case "show":
                            processShowCommand(commentList);
                            break;
                        case "add":
                            processCreateCommand(commentList);
                            break;
                        case "done":
                            processDoneCommand(commentList);
                            break;
                        case "del":
                            processDeleteCommand(commentList);
                            break;
                        case "edit":
                            processUpdateCommand(commentList);
                            break;
                        case "export":
                            processExportCommand(commentList);
                            break;
                        case "import":
                            processReadCommand(commentList);
                            break;
                        case "help":
                            processHelpCommand(commentList);
                            break;
                            default:
                                System.out.println("This is not a valid comment : '"+input+"'; Please enter a valid comment. You can enter "+Constant.ANSI_PURPLE+"'help'"+ Constant.ANSI_RESET+"to see the comment list");

                    }



            processIncomingMessage(scanner);

        }else {
            System.out.println("End of user input, programme stops");
        }
    }
    private static void processUpdateCommand(String[] commentList){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd hh:mm");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");
            String type = commentList[1];
            Integer id = Integer.parseInt(commentList[2].trim());
            boolean error = false;

            ArrayList<Object> taskList = new ArrayList<>();
            MainClass.listStorage.getTaskDTOS().forEach(taskDTO -> {
                taskList.add(taskDTO);
            });

            ArrayList<Object> expenseList = new ArrayList<>();
            MainClass.listStorage.getExpenditureDTOS().forEach(expenditureDTO -> {
                expenseList.add(expenditureDTO);
            });

            String[] updateStatement;
            String updateString;
            Object foundObject = null;

            if (commentList.length > 3) {
                switch (type.trim().toLowerCase()) {
                    case "todo":
                        updateString = commentList[3];
                        updateStatement = updateString.split("\\|");
                        String deadline = updateStatement[0];
                        String description = updateStatement[1];
                        foundObject = HelpFunctions.findObjectById(taskList, id, Constant.OBJECT_TYPE_TODO, Constant.ACTION_TYPE_EDIT);
                        if (foundObject != null) {
                            TodoDTO todoDTO = (TodoDTO) foundObject;
                            if (!deadline.equalsIgnoreCase("x")) {
                                try {
                                    Date date = simpleDateFormat.parse(deadline);
                                    todoDTO.setDeadline(date);
                                } catch (ParseException e) {
                                    System.out.println(Constant.ANSI_RED + "Date Format is not valid");
                                    error= true;
                                }
                            }
                            if (!description.equalsIgnoreCase("x")) {
                                todoDTO.setDescription(description);
                            }
                            try {
                                if (!error){
                                    MainClass.listStorage.getTaskDTOS().remove(foundObject);
                                    MainClass.listStorage.getTaskDTOS().add(todoDTO);
                                    System.out.println(Constant.ANSI_GREEN+"Edit todo record successful");
                                }else {
                                    System.out.println(Constant.ANSI_RED + "Update record failed, please check again!");
                                }

                            }catch (Exception e){
                                System.out.println(Constant.ANSI_RED + "Error happend during saving, please check again!");
                            }
                        }
                        break;
                    case "schedule":
                        updateString = commentList[3];
                        updateStatement = updateString.split("\\|");
                        String startDate = updateStatement[0];
                        String endDate = updateStatement[1];
                        String scheduleDesc = updateStatement[2];
                        foundObject = HelpFunctions.findObjectById(taskList, id, Constant.OBJECT_TYPE_SCHEDULE, Constant.ACTION_TYPE_EDIT);
                        if (foundObject != null) {
                            ScheduleDTO scheduleDTO = (ScheduleDTO) foundObject;
                            if (!startDate.equalsIgnoreCase("x")) {
                                try {
                                    Date date = simpleDateFormat.parse(startDate);
                                    scheduleDTO.setStartDatetime(date);
                                } catch (ParseException e) {
                                    System.out.println(Constant.ANSI_RED + "Date Format is not valid");
                                    error = true;
                                }
                            }
                            if (!endDate.equalsIgnoreCase("x")) {
                                try {
                                    Date date = simpleDateFormat.parse(endDate);
                                    scheduleDTO.setEndDatetime(date);
                                } catch (ParseException e) {
                                    System.out.println(Constant.ANSI_RED + "Date Format is not valid");
                                    error = true;
                                }
                            }
                            if (!scheduleDesc.equalsIgnoreCase("x")) {
                                scheduleDTO.setDescription(scheduleDesc);
                            }
                            try {
                                if (!error) {
                                    MainClass.listStorage.getTaskDTOS().remove(foundObject);
                                    MainClass.listStorage.getTaskDTOS().add(scheduleDTO);
                                    System.out.println(Constant.ANSI_GREEN + "Edit schedule record successful");
                                }else {
                                    System.out.println(Constant.ANSI_RED + "Update record failed, please check again!");
                                }
                            }catch (Exception e){
                                System.out.println(Constant.ANSI_RED + "Error happend during saving, please check again!");
                            }
                        }
                        break;

                    case "expense":
                        updateString = commentList[3];
                        updateStatement = updateString.split("\\|");
                        String expenseRecordDate = updateStatement[0];
                        String expenseDesc = updateStatement[1];
                        String expAmount = updateStatement[2];
                        foundObject = HelpFunctions.findObjectById(expenseList, id, Constant.OBJECT_TYPE_EXPENSE, Constant.ACTION_TYPE_EDIT);
                        if (foundObject != null) {
                            ExpenditureDTO expenditureDTO = (ExpenditureDTO) foundObject;
                            if (!expenseRecordDate.equalsIgnoreCase("x")) {
                                try {
                                    Date date = simpleDateFormat2.parse(expenseRecordDate);
                                    expenditureDTO.setRecordDate(date);
                                } catch (ParseException e) {
                                    System.out.println(Constant.ANSI_RED + "Date Format is not valid");
                                    error = true;
                                }
                            }
                            if (!expenseDesc.equalsIgnoreCase("x")) {
                                expenditureDTO.setDescription(expenseDesc);
                            }
                            if (!expAmount.equalsIgnoreCase("x")) {
                                try {
                                    expenditureDTO.setAmount(Double.parseDouble(expAmount));
                                }catch (NumberFormatException e){
                                    System.out.println(Constant.ANSI_RED +"Amount format is not valid, please check again");
                                    error = true;
                                }
                            }
                            try {
                                if (!error) {
                                MainClass.listStorage.getExpenditureDTOS().remove(foundObject);
                                MainClass.listStorage.getExpenditureDTOS().add(expenditureDTO);
                                System.out.println(Constant.ANSI_GREEN+"Edit expense record successful");
                                }else {
                                    System.out.println(Constant.ANSI_RED + "Update record failed, please check again!");
                                }
                            }catch (Exception e){
                                System.out.println(Constant.ANSI_RED + "Error happended during saving, please check again!");
                            }
                        }
                        break;
                    case "income":
                        updateString = commentList[3];
                        updateStatement = updateString.split("\\|");
                        String incomeRecordDate = updateStatement[0];
                        String incomeDesc = updateStatement[1];
                        String incomeAmount = updateStatement[2];
                        foundObject = HelpFunctions.findObjectById(expenseList, id, Constant.OBJECT_TYPE_INCOME, Constant.ACTION_TYPE_EDIT);
                        if (foundObject != null) {
                            ExpenditureDTO expenditureDTO = (ExpenditureDTO) foundObject;
                            if (!incomeRecordDate.equalsIgnoreCase("x")) {
                                try {
                                    Date date = simpleDateFormat2.parse(incomeRecordDate);
                                    expenditureDTO.setRecordDate(date);
                                } catch (ParseException e) {
                                    System.out.println(Constant.ANSI_RED + "Date Format is not valid");
                                    error=true;
                                }
                            }
                            if (!incomeDesc.equalsIgnoreCase("x")) {
                                expenditureDTO.setDescription(incomeDesc);
                            }
                            if (!incomeAmount.equalsIgnoreCase("x")) {
                                try {
                                    expenditureDTO.setAmount(Double.parseDouble(incomeAmount));
                                }catch (NumberFormatException e){
                                    System.out.println(Constant.ANSI_RED +"Amount format is not valid, please check again");
                                    error = true;
                                }
                            }
                            try {
                                if (!error) {
                                MainClass.listStorage.getExpenditureDTOS().remove(foundObject);
                                MainClass.listStorage.getExpenditureDTOS().add(expenditureDTO);
                                System.out.println(Constant.ANSI_GREEN+"Edit income record successful");
                                }else {
                                    System.out.println(Constant.ANSI_RED + "Update record failed, please check again!");
                                }
                            }catch (Exception e){
                                System.out.println(Constant.ANSI_RED + "Error happended during saving, please check again!");
                            }
                        }
                        break;
                }
            } else {
                System.out.println(Constant.ANSI_RED + "Invalid update command, please check you syntax!");
            }
        }catch (NumberFormatException e){
            System.out.println(Constant.ANSI_RED+"Invalid update command, please check your syntax!");
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(Constant.ANSI_RED+"Missing update statement,please check your syntax!");
        }
    }
    private static void processDoneCommand(String[] commentList){
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
    private static void processShowCommand(String[] commentList) {
        try {


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

                        if (expenseList != null) {
                            if (helper.getDateFilterType() != null) {
                                //  taskList = dateRangeListProcess(taskList, helper);
                            } else {
                                expenseList.forEach(expenditureDTO -> {
                                    if (expenditureDTO.getType() == Constant.EXPENSE_TYPE) {
                                        expenditureDTOS.add(expenditureDTO);
                                    }
                                });
                            }
                            if (!expenditureDTOS.isEmpty())
                                expenditureDTOS.forEach(expenditureDTO -> System.out.println(expenditureDTO.toString()));
                            else System.out.println(Constant.ANSI_BLACK + "There is no expense record available.");
                        } else System.out.println(Constant.ANSI_BLACK + "There is no expense record available.");
                        break;
                    case "income":
                        List<ExpenditureDTO> expenditureIncomeDTOS = new ArrayList<>();

                        if (expenseList != null) {
                            if (helper.getDateFilterType() != null) {
                                //  taskList = dateRangeListProcess(taskList, helper);
                            } else {
                                expenseList.forEach(expenditureDTO -> {
                                    if (expenditureDTO.getType() == Constant.INCOME_TYPE) {
                                        expenditureIncomeDTOS.add(expenditureDTO);
                                    }
                                });
                            }
                            if (!expenditureIncomeDTOS.isEmpty())
                                expenditureIncomeDTOS.forEach(expenditureDTO -> System.out.println(expenditureDTO.toString()));
                            else System.out.println(Constant.ANSI_BLACK + "There is no income record available.");
                        } else System.out.println(Constant.ANSI_BLACK + "There is no income record available.");
                        break;
                }
            }
        }catch (Exception e){

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
    private static void processCreateCommand(String[] commentList){
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
                        todoDTO.setId(HelpFunctions.maxScheduleIdNumber);
                        HelpFunctions.maxScheduleIdNumber++;
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
                            scheduleDTO.setId(HelpFunctions.maxScheduleIdNumber);
                            HelpFunctions.maxScheduleIdNumber++;
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
                            expenseDTO.setId(HelpFunctions.maxExpenditureIdNumber);
                            HelpFunctions.maxExpenditureIdNumber++;
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
                            incomeDTO.setId(HelpFunctions.maxExpenditureIdNumber);
                            HelpFunctions.maxExpenditureIdNumber++;
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

    private static void processDeleteCommand(String[] commentList){
        if (commentList.length>1){
            String type = commentList[1];
            Object foundObject = null;
            switch (type.trim().toLowerCase()){
                case "todo":

                    try {
                        ArrayList<Object> todoList = new ArrayList<>();
                        MainClass.listStorage.getTaskDTOS().forEach(taskDTO -> {
                            todoList.add(taskDTO);
                        });
                        foundObject = HelpFunctions.findObjectById(todoList,Integer.parseInt(commentList[2]),Constant.OBJECT_TYPE_TODO,Constant.ACTION_TYPE_DELETE);

                       if (foundObject !=null){
                           MainClass.listStorage.getTaskDTOS().remove(foundObject);
                       }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                case "schedule":
                    try {

                        ArrayList<Object> scheduleList = new ArrayList<>();
                        MainClass.listStorage.getTaskDTOS().forEach(taskDTO -> {
                            scheduleList.add(taskDTO);
                        });
                        foundObject = HelpFunctions.findObjectById(scheduleList,Integer.parseInt(commentList[2]),Constant.OBJECT_TYPE_SCHEDULE,Constant.ACTION_TYPE_DELETE);
                        if (foundObject!=null){
                            MainClass.listStorage.getTaskDTOS().remove(foundObject);
                        }

                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }
                    break;

                case "expense":
                    try {
                        ArrayList<Object> expenseList = new ArrayList<>();
                        MainClass.listStorage.getExpenditureDTOS().forEach(expenditureDTO -> {
                            expenseList.add(expenditureDTO);
                        });
                        foundObject = HelpFunctions.findObjectById(expenseList,Integer.parseInt(commentList[2]),Constant.OBJECT_TYPE_EXPENSE,Constant.ACTION_TYPE_DELETE);
                        if (foundObject!=null){
                            MainClass.listStorage.getExpenditureDTOS().remove((ExpenditureDTO) foundObject);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                    break;

                case "income":
                    try {
                        ArrayList<Object> incomeList = new ArrayList<>();
                        MainClass.listStorage.getExpenditureDTOS().forEach(expenditureDTO -> {
                            incomeList.add(expenditureDTO);
                        });
                        foundObject = HelpFunctions.findObjectById(incomeList,Integer.parseInt(commentList[2]),Constant.OBJECT_TYPE_INCOME,Constant.ACTION_TYPE_DELETE);

                        if (foundObject!=null){
                            MainClass.listStorage.getExpenditureDTOS().remove((ExpenditureDTO) foundObject);
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Your comment is not correct, please check again!");
            }
        }else {
            System.out.println("Input create statement is not correct, please check again");
        }
    }

    private static void processExportCommand(String[] commentList) {
        String path = commentList[1];
        String fileName = commentList[2];
        if (path != null && path.length() > 0 && path.charAt(path.length() - 1) == ' ') {
            path = path.substring(0, path.length() - 1);
        }
        try {
            HelpFunctions.createExcel(MainClass.listStorage, path+ File.separator+fileName+".xlsx");
            System.out.println(Constant.ANSI_GREEN+"Successfully export data to "+path);
        } catch (IOException e) {
            System.out.println(Constant.ANSI_RED+"Cannot export excel file, please check your path");
        } catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
            System.out.println(Constant.ANSI_RED+"Cannot export excel file, please check your path");
        }

    }
    private static void processReadCommand(String[] commentList){
        String path = commentList[1];
        String fileName = commentList[2];
        if (path != null && path.length() > 0 && path.charAt(path.length() - 1) == ' ') {
            path = path.substring(0, path.length() - 1);
        }
        try {
            MainClass.listStorage =   HelpFunctions.readFromExcelFile(path+ File.separator+fileName+".xlsx");
            System.out.println(Constant.ANSI_GREEN+"Successfully import data, your list will be refreshed.");
        } catch (IOException e) {
            System.out.println(Constant.ANSI_RED+"Cannot read excel file, please check your path");
        } catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
            System.out.println(Constant.ANSI_RED+"Cannot read excel file, please check your path");
        }
    }

    private static void processHelpCommand(String[] commentList){
        if (commentList.length==1){
            System.out.println("Help command list :\n 1.help -all : show all the commands." +
                    "\n 2.help -show : show command to display records." +
                    "\n 3.help -add : add command to add records" +
                    "\n 4.help -edit : edit command to update records" +
                    "\n 5.help -export : export command to export records to file" +
                    "\n 6.help -import : import command to import records from file");
        }else {
            String type = commentList[1];
            switch (type){
                case "show" :
                    System.out.println("This command allows user to see the record base on type\n" +
                            "Show command syntax : Show -{type} -(daterange)\n" +
                            "type is mandatory field as list below :\n" +
                            "1.schedule\n" +
                            "2.todo\n" +
                            "3.income\n" +
                            "4.expense\n" +
                            "daterange is not mandatory, user can enter date range as below to see record in the time period ,date format is yyyyMMdd hh:mm like '20180101 08:30'\n" +
                            "1:from {date} to {date2}\n" +
                            "2.before {date}\n" +
                            "3.after {date}\n" +
                            "Example command of show command : 'show -todo -after 20180101 08:30'");
                    break;
                case "add" :
                    System.out.println("This command allows user to add the record base on type\n" +
                            "Add command syntax : Add -{type} -{input}\n" +
                            "type and input are mandatory field as list below:\n" +
                            "1.schedule   --> input format {description}|{startDate}|{endDate} , date format is is yyyyMMdd hh:mm like '20180101 08:30' \n" +
                            "2.todo\n --> input format {description}|{deadline}, date format is is yyyyMMdd hh:mm like '20180101 08:30' \n" +
                            "3.income and expense --> input format {description}|{amount}|{recordDate} , date format is is yyyyMMdd hh:mm like '20180101'\n" +
                            "Example command of add command : 'add -schedule -doing work|20180101 08:30|20180101 09:30'");
                    break;

                case "edit" :
                    System.out.println("This command allows user to update the record base on type\n" +
                            "Edit command syntax : edit -{type} -{id} -{input}\n" +
                            "type,id and input are mandatory fields as list below:\n" +
                            "1.schedule   --> input format {description}|{startDate}|{endDate} , date format is is yyyyMMdd hh:mm like '20180101 08:30' \n" +
                            "2.todo --> input format {description}|{deadline}, date format is is yyyyMMdd hh:mm like '20180101 08:30' \n" +
                            "3.income and expense --> input format {description}|{amount}|{recordDate} , date format is is yyyyMMdd hh:mm like '20180101'\n" +
                            "IMPORTANT : if you dont want to skip some of the input field, can use x to skip that field, like {description}|x|{recordDate}, then the amount will not updated\n"+
                            "Example command of edit command : 'edit -schedule -5 -go work|x|x'");
                    break;
                case "export" :
                    System.out.println("This command allows user to export the records to specific path\n" +
                            "Edit command syntax : export -{path} -{filename}\n" +
                            "path and filename are mandatory fields as list below:\n" +
                            "Example command of export command : 'export -C:\\Users\\Timmy\\Desktop\\School\\2018\\Sem 1\\TIC 2002 Java -record file'");
                    break;

                case "import" :
                    System.out.println("This command allows user to import the records from specific path\n" +
                            "Edit command syntax : import -{path} -{filename}\n" +
                            "path and filename are mandatory fields as list below:\n" +
                            "Example command of export command : 'import -C:\\Users\\Timmy\\Desktop\\School\\2018\\Sem 1\\TIC 2002 Java -record file'");
                    break;
            }
        }



    }
    private static Helper processDateRangeComment( String dateComment) throws InvalidDataException {
        Helper helper = new Helper();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd hh:mm");
        if (dateComment.startsWith("from") && dateComment.contains("to")){
            dateComment = dateComment.replace("from","");
            dateComment = dateComment.replace("to","|");
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
        }else if(dateComment.startsWith("before")){
            dateComment = dateComment.replace("before","");
            try {
                Date beforeDate = simpleDateFormat.parse(dateComment.trim());

                helper.setDateFilterType(Constant.SEARCH_DATE_TYPE_BEFORE);
                helper.setStartDate(beforeDate);
            } catch (ParseException e) {
                System.out.println("Date range is wrongly passed. Please check your input again and follow the correct date format.");
            }
        }else if (dateComment.startsWith("after")){
            dateComment = dateComment.replace("after","");
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
