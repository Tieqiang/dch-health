package com.dch.util;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/4/8.
 */
public class ReadExcelToDb {
    //private static String excelDir = "F:\\skqRare\\excel";
    public static List<Map> readDirExcel(String excelDir) {
        List<Map> list = new ArrayList<>();
        try {
            File file = new File(excelDir);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    if(f.getName().indexOf("xls")>0||f.getName().indexOf("xlsx")>0){
                        InputStream is = new FileInputStream(f.getAbsolutePath());
                        // jxl提供的Workbook类
                        Workbook wb = readExcel(f.getAbsolutePath());
                        // Excel的页签数量
                        int sheet_size = wb.getNumberOfSheets();
                        Sheet sheet=wb.getSheetAt(0);
                        for(int i=0;i<sheet.getPhysicalNumberOfRows();i++){
                            Map map = new HashMap();
                            Row row = sheet.getRow(i);
                            for(int j=0;j<row.getPhysicalNumberOfCells();j++){
                                String value = (String) getCellFormatValue(row.getCell(j));
                                value = value.replace(" ","");
                                System.out.print(value+"===");
                                map.put("col_"+j,value);
                            }
                            list.add(map);
                            System.out.println();
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //读取excel
    public static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    BigDecimal bigDecimal = new BigDecimal(Double.valueOf(cellValue.toString()));
                    cellValue = bigDecimal.toString();
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }

    public static void main(String args[]){
        readDirExcel("F:\\skqRare\\excel");
    }
}
