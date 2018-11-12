import DTOs.GeneralList;
import DTOs.expenditure.ExpenditureDTO;
import DTOs.schedule.ScheduleDTO;
import DTOs.schedule.TaskDTO;
import DTOs.schedule.TodoDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import DTOs.Constant;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HelpFunctions {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private static String[] taskSheetName = {"Type","Schedule Id","Description","Start Date Time","End Date Time","Status"};
    private static String[] expenditureName = {"Type","Record Id","Description","Amount","Record Date"};
    public static Integer maxScheduleIdNumber = 1;
    public static Integer maxExpenditureIdNumber = 1;

  public static Object findObjectById(ArrayList<Object> objectArrayList,Integer objectId,String objectType, String actionType){
      boolean found = false;
      Object foundObject = null;

      for (Object object :objectArrayList) {
          switch (objectType){
              case Constant.OBJECT_TYPE_TODO:
                  if (object instanceof TodoDTO){
                      if (((TaskDTO) object).getId()== objectId){
                          foundObject = object;
                          found =true;
                      }
                  }
                  break;
              case Constant.OBJECT_TYPE_SCHEDULE:
                  if (object instanceof ScheduleDTO){
                      if (((ScheduleDTO) object).getId()== objectId){
                          foundObject = object;
                          found =true;
                      }
                  }
                  break;
              case Constant.OBJECT_TYPE_EXPENSE:
                  if (object instanceof ExpenditureDTO){
                      if (((ExpenditureDTO) object).getId()== objectId && ((ExpenditureDTO) object).getType() ==Constant.EXPENSE_TYPE){
                          foundObject = object;
                          found =true;
                      }
                  }
                  break;
              case Constant.OBJECT_TYPE_INCOME:
                  if (object instanceof ExpenditureDTO){
                      if (((ExpenditureDTO) object).getId()== objectId && ((ExpenditureDTO) object).getType() ==Constant.INCOME_TYPE){
                          foundObject = object;
                          found =true;
                      }
                  }
                  break;
          }
      }

      if (found){
//          System.out.println(Constant.ANSI_GREEN + "Your "+objectType+" record "+actionType+" found!");
          return foundObject;
      }else {

//          System.out.println(Constant.ANSI_RED + "Your "+objectType+" item cannot be found");
          return null;
      }
  }

    public static void createExcel(GeneralList generalList,String path) throws IOException, InvalidFormatException {
        Workbook workbook = new XSSFWorkbook();

        CreationHelper createHelper = workbook.getCreationHelper();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row

        if (!generalList.getTaskDTOS().isEmpty()){
            Sheet sheet = workbook.createSheet("Task");

            Row headerRow = sheet.createRow(0);



            for(int i = 0; i < 6; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(taskSheetName[i]);
                cell.setCellStyle(headerCellStyle);
            }

            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd hh:mm"));

            // Create Other rows and cells with employees data
            int rowNum = 1;
            for(TaskDTO taskDTO: generalList.getTaskDTOS()) {
                Row row = sheet.createRow(rowNum++);

                if (taskDTO instanceof ScheduleDTO){
                    ScheduleDTO scheduleDTO = (ScheduleDTO) taskDTO;
                    row.createCell(0)
                            .setCellValue("Schedule");

                    row.createCell(1)
                            .setCellValue(scheduleDTO.getId());

                    row.createCell(2)
                            .setCellValue(scheduleDTO.getDescription());

                    Cell startDateCell = row.createCell(3);
                    startDateCell.setCellValue(scheduleDTO.getStartDatetime());
                    startDateCell.setCellStyle(dateCellStyle);

                    Cell endDateCell = row.createCell(4);
                    endDateCell.setCellValue(scheduleDTO.getEndDatetime());
                    endDateCell.setCellStyle(dateCellStyle);

                    row.createCell(5)
                            .setCellValue(scheduleDTO.getStatus());
                }else if (taskDTO instanceof TodoDTO){
                    TodoDTO todoDTO = (TodoDTO) taskDTO;
                    row.createCell(0)
                            .setCellValue("Todo Task");

                    row.createCell(1)
                            .setCellValue(todoDTO.getId());

                    row.createCell(2)
                            .setCellValue(todoDTO.getDescription());

                    row.createCell(3).setCellValue("");

                    Cell endDateCell = row.createCell(4);
                    endDateCell.setCellValue(todoDTO.getDeadline());
                    endDateCell.setCellStyle(dateCellStyle);

                    row.createCell(5)
                            .setCellValue(todoDTO.getStatus());
                }

            }

            // Resize all columns to fit the content size
            for(int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file


        }
        if (!generalList.getExpenditureDTOS().isEmpty()){
            Sheet sheet = workbook.createSheet("Expenditure");

            Row headerRow = sheet.createRow(0);



            for(int i = 0; i < 5; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(expenditureName[i]);
                cell.setCellStyle(headerCellStyle);
            }

            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd hh:mm"));

            // Create Other rows and cells with employees data
            int rowNum = 1;
            for(ExpenditureDTO expenditureDTO: generalList.getExpenditureDTOS()) {
                Row row = sheet.createRow(rowNum++);
                if (expenditureDTO.getType()== Constant.EXPENSE_TYPE){
                    row.createCell(0)
                            .setCellValue("Expense");
                }else if (expenditureDTO.getType()== Constant.INCOME_TYPE){
                    row.createCell(0)
                            .setCellValue("Income");
                }


                row.createCell(1)
                        .setCellValue(expenditureDTO.getId());

                row.createCell(2)
                        .setCellValue(expenditureDTO.getDescription());
                row.createCell(3)
                        .setCellValue(expenditureDTO.getAmount());

                Cell startDateCell = row.createCell(4);
                startDateCell.setCellValue(expenditureDTO.getRecordDate());
                startDateCell.setCellStyle(dateCellStyle);


            } for(int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file


        }

        FileOutputStream fileOut = new FileOutputStream(path);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }


    /**
     *
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static GeneralList readFromExcelFile(String path) throws IOException, InvalidFormatException {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        GeneralList generalList = new GeneralList();
        Workbook workbook = WorkbookFactory.create(new File(path));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        // 1. You can obtain a sheetIterator and iterate over it
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        DataFormatter dataFormatter = new DataFormatter();
        java.util.List<TaskDTO> allTask = new ArrayList<>();
        List<ExpenditureDTO> allExpenseRecord = new ArrayList<>();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            System.out.println("=> " + sheet.getSheetName());
            Iterator<Row> rowIterator = sheet.rowIterator();
            if (sheet.getSheetName().equalsIgnoreCase("Task")) {
                String parentCode = sheet.getSheetName();
                String currentCate = null;
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    if (row.getRowNum() != 0) {
                        TaskDTO taskDTO = new TaskDTO();
                        int id =0;
                        String description ="";
                        Date startDate = null;
                        Date endDate= null;
                        int status=0;

                        int categoryIndicator;

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            Integer columnIndex = cell.getColumnIndex();
                            String cellValue = dataFormatter.formatCellValue(cell);


                            if (cellValue!=null && !cellValue.isEmpty()) {
                                if (columnIndex == 0 ){
                                    currentCate = cellValue;
                                }else {
                                    switch (columnIndex) {
                                        case 1:
                                            try {
                                                id = Integer.parseInt(cellValue);
                                                if(id>=maxScheduleIdNumber){
                                                    maxScheduleIdNumber= id+1;
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Pass in value is not Integer");
                                            }

                                            break;
                                        case 2:
                                            description = cellValue;
                                            break;
                                        case 3:
                                            try {
                                                startDate = simpleDateFormat.parse(cellValue);
                                            } catch (ParseException e) {
                                                System.out.println("Date format is not correct : " + e.getMessage());
                                            }
                                            break;
                                        case 4:
                                            try {
                                                endDate = simpleDateFormat.parse(cellValue);
                                            } catch (ParseException e) {
                                                System.out.println("Date format is not correct : " + e.getMessage());
                                            }
                                            break;
                                        case 5:
                                            status = Integer.parseInt(cellValue);


                                            if (currentCate.trim().equalsIgnoreCase("todo task")) {
                                                TodoDTO todoDTO = new TodoDTO(id, description, status, endDate);
                                                allTask.add(todoDTO);
                                            } else {
                                                ScheduleDTO scheduleDTO = new ScheduleDTO(id, description, status, startDate, endDate);
                                                allTask.add(scheduleDTO);
                                            }
                                            break;

                                    }
                                }
                            }



                        }

                    }
                }
            }else if (sheet.getSheetName().equalsIgnoreCase("Expenditure")) {
                String parentCode = sheet.getSheetName();
                String currentCate = null;
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    if (row.getRowNum() != 0) {
                        ExpenditureDTO expenditureDTO = new ExpenditureDTO();
                        int id =0;
                        String description ="";
                        double amount =0.0;
                        Date recordDate = null;



                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            Integer columnIndex = cell.getColumnIndex();
                            String cellValue = dataFormatter.formatCellValue(cell);


                            if (cellValue!=null && !cellValue.isEmpty()) {
                                if (columnIndex == 0 ){
                                    currentCate = cellValue;
                                }else {
                                    switch (columnIndex) {
                                        case 1:
                                            try {
                                                id = Integer.parseInt(cellValue);
                                                if(id>=maxExpenditureIdNumber){
                                                    maxExpenditureIdNumber= id+1;
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Pass in value is not Integer");
                                            }

                                            break;
                                        case 2:
                                            description = cellValue;
                                            break;
                                        case 3:
                                            try {
                                                amount = Double.parseDouble(cellValue);
                                            } catch (Exception e) {
                                                System.out.println("Amount is not valid : " + e.getMessage());
                                            }
                                            break;
                                        case 4:
                                            try {
                                                recordDate = simpleDateFormat.parse(cellValue);
                                            } catch (ParseException e) {
                                                System.out.println("Date format is not correct : " + e.getMessage());
                                            }
                                            expenditureDTO.setAmount(amount);
                                            expenditureDTO.setRecordDate(recordDate);
                                            expenditureDTO.setDescription(description);
                                            expenditureDTO.setId(id);
                                            if (currentCate.trim().equalsIgnoreCase("expense")) {
                                                expenditureDTO.setType(Constant.EXPENSE_TYPE);
                                            } else  if (currentCate.trim().equalsIgnoreCase("income")){
                                                expenditureDTO.setType(Constant.INCOME_TYPE);
                                            }
                                            allExpenseRecord.add(expenditureDTO);
                                            break;






                                    }
                                }
                            }



                        }

                    }
                }
            }
        }
        generalList.setExpenditureDTOS(allExpenseRecord);
        generalList.setTaskDTOS(allTask);
        return generalList;
    }


}