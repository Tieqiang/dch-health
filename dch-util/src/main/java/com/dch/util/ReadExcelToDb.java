package com.dch.util;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

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
                    list.addAll(readExcelFile(f,null));
                }
            }else{
                list = readExcelFile(file,null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static List<Map> readExcelFile(File f,Map titleMap) throws Exception{
        List<Map> list = new ArrayList<>();
        LinkedHashMap colMap = new LinkedHashMap();
        if(f.getName().indexOf("xls")>0||f.getName().indexOf("xlsx")>0){
            // jxl提供的Workbook类
            Workbook wb = readExcel(f.getAbsolutePath());
            Sheet sheet=wb.getSheetAt(0);
            for(int i=0;i<sheet.getPhysicalNumberOfRows();i++){
                LinkedHashMap map = new LinkedHashMap();
                Row row = sheet.getRow(i);
                if(i<1 && !titleMap.isEmpty()){
                    for(int j=0;j<row.getPhysicalNumberOfCells();j++){
                        colMap.put("col_"+j,titleMap.get(getCellFormatValue(row.getCell(j))));
                    }
                }else{
                    for(int j=0;j<row.getPhysicalNumberOfCells();j++){
                        Object value = getCellFormatValue(row.getCell(j));
                        if(value instanceof String){
                            value = value.toString().replace(" ","");
                        }
                        map.put(colMap.get("col_"+j),value);
                    }
                    list.add(map);
                }
            }
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
                    cellValue = Double.valueOf(cellValue.toString());
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
}
