package com.macauslot.recruitmentadmin.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 多表合并导出Excel文件,单表用需套一层List
 *
 * @author jim.deng
 */
public class ExcelUtil {

    private static final short BLACK = 8;

    public static void exportExcel(List<String> tableNameList, List<String[]> titleList, List<List<String[]>> resourceList, OutputStream outputStream) throws IOException {
        exportExcel(tableNameList, titleList, resourceList, outputStream, null);

    }

    /**
     * 文件导出方法.
     *
     * @param tableNameList 表名集合
     * @param titleList     多表标题集合
     * @param resourceList  多表数据集合, 其内部为List<String[]> 集合类型, 要导出的具体数据集合.
     * @param outputStream  输出流.
     * @param conditionList 本次打印的过滤条件
     * @throws IOException
     */
    public static void exportExcel(List<String> tableNameList, List<String[]> titleList, List<List<String[]>> resourceList, OutputStream outputStream, List<ArrayList<String>> conditionList)
            throws IOException {


        // 创建一个内存Excel对象.
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // 创建一个表格.
        SXSSFSheet sheet = workbook.createSheet("sheet1");

        //excel當前渲染行號
        int RowNum = 0;

        //表名樣式
        CellStyle tableNameStyle = getTableNameStyle(workbook);
        //获取列头样式对象
        CellStyle headerStyle = getHeaderStyle(workbook);
        //单元格样式对象
        CellStyle bodyStyle = getBodyStyle(workbook);
        //左样式对象
        CellStyle leftStyle = getLeftStyle(workbook);


        for (int k = 0; k < resourceList.size(); k++) {
            CellRangeAddress c1 = new CellRangeAddress(RowNum, RowNum, 0, 2);
            sheet.addMergedRegion(c1);
            SXSSFRow tableName = sheet.createRow(RowNum++);//表与表之间空两行
            SXSSFCell tableCell = tableName.createCell(0);
//            tableCell.setCellStyle(tableNameStyle);
            setRegionStyle(sheet, c1, tableNameStyle);
            tableCell.setCellValue(tableNameList.get(k));

            if (conditionList != null && conditionList.size() > k) {
                for (String col : conditionList.get(k)) {
                    // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
                    CellRangeAddress c2 = new CellRangeAddress(RowNum, RowNum, 0, 2);
                    sheet.addMergedRegion(c2);
                    SXSSFRow tableNameCondition = sheet.createRow(RowNum++);
                    SXSSFCell conditionCell = tableNameCondition.createCell(0);
//                    conditionCell.setCellStyle(leftStyle);
                    setRegionStyle(sheet, c2, leftStyle);
                    conditionCell.setCellValue(col);
                }
            }

            // 创建表头
            // 获取表头内容.
            String[] thead = titleList.get(k);
            SXSSFRow headerRow = sheet.createRow(RowNum++);

            sheet.setColumnWidth(0, 4000);//序号列宽
            // 设置列宽
            for (int i = 1; i < thead.length + 1; i++) { //多了序号列
                sheet.setColumnWidth(i, 6000);
            }


            SXSSFCell headerCell0 = headerRow.createCell(0);//插入序号Title
            headerCell0.setCellStyle(headerStyle);
            headerCell0.setCellValue("序號");

            // 定义表头内容.
            for (int i = 0; i < thead.length; i++) {
                // 创建一个单元格
                SXSSFCell headerCell = headerRow.createCell(i + 1);//空出一列,插入序号列
                headerCell.setCellStyle(headerStyle);
                headerCell.setCellValue(thead[i]);
            }


            // 表体内容.
            List<String[]> tbody = resourceList.get(k);

            for (int row = 0; row < tbody.size(); row++) {
                // 输出的行数据
                String[] rowTemp = tbody.get(row);


                // 创建行
                SXSSFRow bodyRow = sheet.createRow(RowNum++);

                SXSSFCell id = bodyRow.createCell(0);//序号列
                id.setCellStyle(bodyStyle);
                id.setCellValue(row + 1);
                // 循环创建列
                for (int col = 0; col < rowTemp.length; col++) {
                    SXSSFCell bodyCell = bodyRow.createCell(col + 1);//空出一列,插入序号列
                    bodyCell.setCellStyle(bodyStyle);
                    bodyCell.setCellValue(rowTemp[col]);
                }
            }

            sheet.createRow(RowNum++);//表间距一行

        }


        // 将内存创建的excel对象,输出到文件中.
        workbook.write(outputStream);

    }

    /**
     * 文件导出方法2.
     *
     * @param tableNameList    表名集合
     * @param titleList        多表标题集合
     * @param resourceList     多表数据集合, 其内部为List<String[]> 集合类型, 要导出的具体数据集合.
     * @param outputStream     输出流.
     * @param conditionList    本次打印的过滤条件
     *                         listIndex        第几个表(1开始)
     * @param mergeColNameList 要合并的列名们(标题名的子集)
     *                         mergeCondition   满足此列的上下两行内容相同条件,就合并
     * @throws IOException
     */
    public static void exportExcel(List<String> tableNameList,
                                   List<String[]> titleList,
                                   List<List<String[]>> resourceList,
                                   OutputStream outputStream,
                                   List<ArrayList<String>> conditionList,
//                                   int listIndex,
                                   String[] mergeColNameList
//                                   String mergeCondition
    ) throws IOException {


        // 创建一个内存Excel对象.
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // 创建一个表格.
        SXSSFSheet sheet = workbook.createSheet("sheet1");

        //excel當前渲染行號
        int RowNum = 0;

        //表名樣式
        CellStyle tableNameStyle = getTableNameStyle(workbook);
        //获取列头样式对象
        CellStyle headerStyle = getHeaderStyle(workbook);
        //单元格样式对象
        CellStyle bodyStyle = getBodyStyle(workbook);
        //左样式对象
        CellStyle leftStyle = getLeftStyle(workbook);


        for (int k = 0; k < resourceList.size(); k++) {
            CellRangeAddress c1 = new CellRangeAddress(RowNum, RowNum, 0, 2);
            sheet.addMergedRegion(c1);
            SXSSFRow tableName = sheet.createRow(RowNum++);//表与表之间空两行
            SXSSFCell tableCell = tableName.createCell(0);
            setRegionStyle(sheet, c1, tableNameStyle);
            tableCell.setCellValue(tableNameList.get(k));

            if (conditionList != null && conditionList.size() > k) {
                for (String col : conditionList.get(k)) {
                    // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
                    CellRangeAddress c2 = new CellRangeAddress(RowNum, RowNum, 0, 2);
                    sheet.addMergedRegion(c2);
                    SXSSFRow tableNameCondition = sheet.createRow(RowNum++);
                    SXSSFCell conditionCell = tableNameCondition.createCell(0);
                    setRegionStyle(sheet, c2, leftStyle);
                    conditionCell.setCellValue(col);
                }
            }

            // 创建表头
            // 获取表头内容.
            String[] thead = titleList.get(k);
            SXSSFRow headerRow = sheet.createRow(RowNum++);

            sheet.setColumnWidth(0, 4000);//序号列宽
            // 设置列宽
            for (int i = 1; i < thead.length + 1; i++) { //多了序号列
                sheet.setColumnWidth(i, 6000);
            }


            SXSSFCell headerCell0 = headerRow.createCell(0);//插入序号Title
            headerCell0.setCellStyle(headerStyle);
            headerCell0.setCellValue("序號");

            // 定义表头内容.
            for (int i = 0; i < thead.length; i++) {
                // 创建一个单元格
                SXSSFCell headerCell = headerRow.createCell(i + 1);//空出一列,插入序号列
                headerCell.setCellStyle(headerStyle);
                headerCell.setCellValue(thead[i]);
            }


            //分析合并列是哪些
            List<Integer> mergeIndexList = new ArrayList<>();

            for (int i = 0; i < thead.length; i++) {
                for (String mergeColName : mergeColNameList) {
                    if (thead[i].equals(mergeColName)) {
                        mergeIndexList.add(i);
                        break;
                    }
                }
            }
//            mergeIndexList.forEach(System.err::println);


            // 表体内容.
            List<String[]> tbody = resourceList.get(k);

            Integer beforeTbodyRowNum = RowNum;

            Integer oldRowNum = RowNum;

            Integer nextOldRowNum = 0;

            for (int row = 0, fakeRow = 1, rowLen = tbody.size(); row < rowLen; row++) {
                // 输出的行数据
                String[] rowTemp = tbody.get(row);
                // 创建行
                SXSSFRow bodyRow = sheet.createRow(RowNum);


                if (RowNum == nextOldRowNum) {
                    oldRowNum = RowNum;
                }
                if (oldRowNum == RowNum) {
                    // 下面的新的行数据
                    String[] nextRowTemp;
                    p1:
                    for (int j = 1; row + j < rowLen; j++) {
                        nextRowTemp = tbody.get(row + j);
                        for (Integer mergeIndex : mergeIndexList) {
                            if (!rowTemp[mergeIndex].equals(nextRowTemp[mergeIndex])) {
                                //当前行与下行只要有一处不相等,对当前行addCell.
                                nextOldRowNum = beforeTbodyRowNum + row + j;
                                break p1;
                            }
                        }

                        if ((row + j == rowLen - 1)) {
                            nextOldRowNum = beforeTbodyRowNum + rowLen;
                        }

                    }
                }


                if (oldRowNum == RowNum) {
                    SXSSFCell id = bodyRow.createCell(0);//序号列
                    id.setCellStyle(bodyStyle);
                    id.setCellValue(fakeRow++);
                    if (nextOldRowNum != beforeTbodyRowNum + rowLen) {
                        mergeRow(sheet, oldRowNum, nextOldRowNum - 1, 0);
                    }
                } else if (row == rowLen - 1) {
                    mergeRowWithStyle(sheet, oldRowNum, RowNum, 0, bodyStyle);
                }


                // 循环创建列
                for (int col = 0; col < rowTemp.length; col++) {


                    if (!mergeIndexList.contains(col)) {
                        SXSSFCell bodyCell = bodyRow.createCell(col + 1);//空出一列,插入序号列
                        bodyCell.setCellStyle(bodyStyle);
                        bodyCell.setCellValue(rowTemp[col]);
                    } else if (oldRowNum == RowNum) {
                        SXSSFCell bodyCell = bodyRow.createCell(col + 1);//空出一列,插入序号列
                        bodyCell.setCellStyle(bodyStyle);
                        bodyCell.setCellValue(rowTemp[col]);
                        mergeRow(sheet, oldRowNum, nextOldRowNum - 1, col + 1);
                    } else {
                        SXSSFCell bodyCell = bodyRow.createCell(col + 1);//空出一列,插入序号列
                        bodyCell.setCellStyle(bodyStyle);
                    }


                }


                RowNum++;


            }
            sheet.createRow(RowNum++);//表间距一行
        }

        // 将内存创建的excel对象,输出到文件中.
        workbook.write(outputStream);

    }

    private static void mergeRow(SXSSFSheet sheet, Integer oldRowNum, int rowNum, int col) {
        if (oldRowNum < rowNum) {
            // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
//            System.err.println("起始行" + oldRowNum + "   截至行" + (rowNum) + "   起始列和截至列" + col);
            CellRangeAddress cra = new CellRangeAddress(oldRowNum, rowNum, col, col);
            sheet.addMergedRegion(cra);
        }
    }

    private static void mergeRowWithStyle(SXSSFSheet sheet, Integer oldRowNum, int rowNum, int col, CellStyle bodyStyle) {
        if (oldRowNum < rowNum) {
            CellRangeAddress cra = new CellRangeAddress(oldRowNum, rowNum, col, col);
            sheet.addMergedRegion(cra);
            setRegionStyle(sheet, cra, bodyStyle);
        }
    }


    /*
     * 列头单元格样式
     */
    private static CellStyle getHeaderStyle(SXSSFWorkbook workbook) {


        // 设置字体
        Font font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 11);
        //字体加粗
        font.setBold(true);
        //设置字体名字
//        font.setFontName("Courier New");
        font.setFontName("宋体");
        //设置样式;
        CellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(BLACK);
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(BLACK);
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(BLACK);
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(BLACK);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        //设置单元格背景颜色
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    private static CellStyle getBodyStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
//        font.setFontName("Courier New");
//        font.setFontName("微软雅黑");
        font.setFontName("宋体");
        //设置样式;
        CellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(BLACK);
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(BLACK);
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(BLACK);
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(BLACK);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(true);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /*
     * 表名单元格样式
     */
    private static CellStyle getTableNameStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 14);
        //字体加粗
        font.setBold(true);
        //设置字体名字
//        font.setFontName("Courier New");
//        font.setFontName("微软雅黑");
        font.setFontName("宋体");
        //设置样式;
        CellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(BLACK);
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(BLACK);
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(BLACK);
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(BLACK);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置单元格背景颜色
        style.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }


    /*
     * 靠左单元格样式
     */
    private static CellStyle getLeftStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
//        font.setFontName("Courier New");
//        font.setFontName("微软雅黑");
        font.setFontName("宋体");
        //设置样式;
        CellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(BLACK);
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(BLACK);
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(BLACK);
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(BLACK);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为 靠左 对齐;
        style.setAlignment(HorizontalAlignment.LEFT);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * @param @param sheet
     * @param @param region
     * @param @param cs    设定文件
     * @return void    返回类型
     * @throws
     * @Title: setRegionStyle
     * @Description: TODO(合并单元格后边框不显示问题)
     * @author: GMY
     * @date: 2018年5月10日 上午10:46:00
     */
    public static void setRegionStyle(Sheet sheet, CellRangeAddress region, CellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = CellUtil.getRow(i, sheet);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell col = CellUtil.getCell(row, (short) j);
                col.setCellStyle(cs);
            }
        }
    }
}

