package com.dch.util;

import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/4/9.
 */
public class ProduceExcel {

    public static void produceExcel(List<Map> resultList,String producePath){
        //第一步创建workbook
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("用户信息");

        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();

        //第四步创建单元格
        HSSFCell cell = row.createCell(0); //第一个单元格
        cell.setCellValue("项目承担单位");
        cell.setCellStyle(style);

        cell = row.createCell(1);         //第二个单元格
        cell.setCellValue("用户名");
        cell.setCellStyle(style);

        cell = row.createCell(2);         //第3个单元格
        cell.setCellValue("登陆名");
        cell.setCellStyle(style);

        cell = row.createCell(3);         //第4个单元格
        cell.setCellValue("密码");
        cell.setCellStyle(style);
        int k=1;
        for(Map map:resultList){
            int size = map.size();
            row = sheet.createRow(k);
            for(int i=0;i<size;i++){
                row.createCell(i).setCellValue(map.get("col_"+i)==null?"":map.get("col_"+i).toString());
            }
            k++;
        }
        //第六步将生成excel文件保存到指定路径下
         try {
            String excelPath = producePath + File.separator + "user.xls";
            FileOutputStream fout = new FileOutputStream(excelPath);
            wb.write(fout);
            fout.close();
           } catch (IOException e) {
                e.printStackTrace();
           }
        System.out.println("Excel文件生成成功...");
    }

    public static void produceTableExcel(List<String> titles,List resultList,String excelPath){
        //第一步创建workbook
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("导出数据");

        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        //第四步创建单元格
        for(int i=0;i<titles.size();i++){
            HSSFCell cell = row.createCell(i); //第一个单元格
            cell.setCellValue(titles.get(i));
            cell.setCellStyle(style);
        }
        int size = resultList.size();
        for(int i=0;i<size; i++){
            Object[] params = (Object[])resultList.get(i);
            row = sheet.createRow(i+1);
            for(int j=0;j<params.length;j++){
                row.createCell(j).setCellValue(params[j]==null?"":params[j].toString());
            }
        }
        //第六步将生成excel文件保存到指定路径下
        try {
            FileOutputStream fout = new FileOutputStream(excelPath);
            wb.write(fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
