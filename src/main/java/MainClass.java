import DTOs.Constant;
import DTOs.GeneralList;
import DTOs.expenditure.ExpenditureDTO;
import DTOs.schedule.ScheduleDTO;
import DTOs.schedule.TaskDTO;
import DTOs.schedule.TodoDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import Exception.InvalidDateRangeException;
import Exception.InvalidCommandException;

public class MainClass {

//    public static List<TaskDTO> allTaskList = new ArrayList<>();

    public static GeneralList listStorage = new GeneralList(new ArrayList<>(),new ArrayList<>());

    public static String excelPath ="C:\\Users\\Timmy\\Desktop\\poi-generated-file.xlsx";

    public static void main(String[] args){

        try {
            Scanner sc = new Scanner(System.in);

            System.out.println(Constant.ANSI_CYAN+"Welcome to Timmy's application, you can type in the comment to do following functions:\n" +
                    "1.Scheduler \n2.Expense Recorder\nIf you need more help, please type " +
                    Constant.ANSI_PURPLE+"'help'" +
                    Constant.ANSI_CYAN+" for more description.\n");
            listStorage =  HelpFunctions.readFromExcelFile(excelPath);
            Scanner scanner = new Scanner(System.in);



            System.out.println(listStorage.toString() +"\n");
            System.out.println(Constant.ANSI_RESET+"Please key in your comment: ");

            ScannerProcess.processIncomingMessage(scanner);


            HelpFunctions.createExcel(listStorage,excelPath);
        }catch (IOException e){
            e.printStackTrace();
        }catch (InvalidFormatException e){
            e.printStackTrace();
        }catch (InvalidDateRangeException e){
            e.printStackTrace();
        }catch (InvalidCommandException e){
            e.printStackTrace();
        }





    }








}
