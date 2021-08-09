package com.macauslot.recruitmentadmin.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class PoiUtil {

	  private final static String xls = "xls";
	  private final static String xlsx = "xlsx";
	  private final static String DATE_FORMAT = "yyyy-MM-dd";

	 public static List getBankListByExcel(InputStream in, String fileName) throws Exception {
	        List list = new ArrayList<>();
	        //创建Excel工作薄
	        Workbook work = getWorkbook(in, fileName);
	        if (null == work) {
	            throw new Exception("创建Excel工作薄为空！");
	        }
	        Sheet sheet = null;
	        Row row = null;
	        Cell cell = null;

	        for (int i = 0; i < work.getNumberOfSheets(); i++) {
	            sheet = work.getSheetAt(i);
	            if (sheet == null) {
	                continue;
	            }

	            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
	                row = sheet.getRow(j);
	                if (row == null || row.getFirstCellNum() == j) {
	                    continue;
	                }

	                List<Object> li = new ArrayList<>();
	                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
	                    cell = row.getCell(y);
	                    li.add(cell);
	                }
	                list.add(li);
	            }
	        }
	        work.close();
	        return list;
	    }

	    /**
	     * 判断文件格式
	     *
	     * @param inStr
	     * @param fileName
	     * @return
	     * @throws Exception
	     */
	    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
	        Workbook workbook = null;
	        String fileType = fileName.substring(fileName.lastIndexOf("."));
	        if (".xls".equals(fileType)) {
	            workbook = new HSSFWorkbook(inStr);
	        } else if (".xlsx".equals(fileType)) {
	            workbook = new XSSFWorkbook(inStr);
	        } else {
	            throw new Exception("请上传excel文件！");
	        }
	        return workbook;
	    }



	    public static List<List<String>> readExcel(MultipartFile file) throws IOException {
	      //檢查檔案
	      checkFile(file);
	      //獲得Workbook工作薄物件
	      Workbook workbook = getWorkBook(file);

	      List<List<String>> result = new ArrayList<List<String>>();

	      if(workbook != null){
	          Sheet sheet = workbook.getSheetAt(0);
	          //獲得當前sheet的開始行
	          int firstRowNum = sheet.getFirstRowNum();
	          //獲得當前sheet的結束行
	          int lastRowNum = sheet.getLastRowNum();
	          //迴圈所有行
	          for(int rowNum = firstRowNum;rowNum <= lastRowNum;rowNum++){
	            //獲得當前行
	            Row row = sheet.getRow(rowNum);
	            if(row == null){
	              continue;
	            }
	            List<String> rowList = new ArrayList<String>();
	            //獲得當前行的開始列
	            int firstCellNum = row.getFirstCellNum();
	            //獲得當前行的列數
	            int lastCellNum = row.getPhysicalNumberOfCells();
//	            String[] cells = new String[row.getPhysicalNumberOfCells()];
	            //迴圈當前行
	            for(int cellNum = firstCellNum; cellNum <= lastCellNum;cellNum++){
	              Cell cell = row.getCell(cellNum);
	              String cellValue = getCellValue(cell);
	              rowList.add(cellValue);
	            }
	            result.add(rowList);
	          }
	        workbook.close();
	      }
	      return result;
	    }

	    //校驗檔案是否合法
	    public static void checkFile(MultipartFile file) throws IOException{
	      //判斷檔案是否存在
	      if(null == file){
	        throw new FileNotFoundException("檔案不存在！");
	      }
	      //獲得檔名
	      String fileName = file.getOriginalFilename();
	      //判斷檔案是否是excel檔案
	      if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){
	        throw new IOException(fileName + "不是excel檔案");
	      }
	    }
	    public static Workbook getWorkBook(MultipartFile file) {
	      //獲得檔名
	      String fileName = file.getOriginalFilename();
	      //建立Workbook工作薄物件，表示整個excel
	      Workbook workbook = null;
	      try {
	        //獲取excel檔案的io流
	        InputStream is = file.getInputStream();
	        //根據檔案字尾名不同(xls和xlsx)獲得不同的Workbook實現類物件
	        if(fileName.endsWith(xls)){
	          //2003
	          workbook = new HSSFWorkbook(is);
	        }else if(fileName.endsWith(xlsx)){
	          //2007
	          workbook = new XSSFWorkbook(is);
	        }
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	      return workbook;
	    }
	    public static String getCellValue(Cell cell){
	      String cellValue = "";
	      if(cell == null){
	        return cellValue;
	      }
	      //如果當前單元格內容為日期型別，需要特殊處理
	      String dataFormatString = cell.getCellStyle().getDataFormatString();
	      if(dataFormatString.equals("m/d/yy")){
	        cellValue = new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
	        return cellValue;
	      }
	      //把數字當成String來讀，避免出現1讀成1.0的情況
	      if(cell.getCellTypeEnum() == CellType.NUMERIC){
	        cell.setCellType(CellType.STRING);
	      }
	      //判斷資料的型別
	      switch (cell.getCellTypeEnum()){
	        case NUMERIC: //數字
	          cellValue = String.valueOf(cell.getNumericCellValue());
	          break;
	        case STRING: //字串
	          cellValue = String.valueOf(cell.getStringCellValue());
	          break;
	        case BOOLEAN: //Boolean
	          cellValue = String.valueOf(cell.getBooleanCellValue());
	          break;
	        case FORMULA: //公式
	          cellValue = String.valueOf(cell.getCellFormula());
	          break;
	        case BLANK: //空值
	          cellValue = "";
	          break;
	        case ERROR: //故障
	          cellValue = "非法字元";
	          break;
	        default:
	          cellValue = "未知型別";
	          break;
	      }
	      return cellValue;
	    }
}
