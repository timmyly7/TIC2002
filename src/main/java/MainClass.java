import DTOs.Constant;
import DTOs.GeneralList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.*;
import exception.InvalidDateRangeException;
import exception.InvalidCommandException;

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
