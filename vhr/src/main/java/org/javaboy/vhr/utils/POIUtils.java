package org.javaboy.vhr.utils;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.javaboy.vhr.model.Employee;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//用于数据库导出成表格
public class POIUtils {

    public static ResponseEntity<byte[]> employee2Excel(List<Employee> list) {
        //创建excel文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建文档摘要,文档的基本信息
        workbook.createInformationProperties();
        //获取文档信息并配置
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //docInfo.setCategory("员工信息");//类别
        //docInfo.setManager("myvhr");//管理员
        //docInfo.setCompany("myvhr");//公司
        //摘要信息
        SummaryInformation sumInfo =workbook.getSummaryInformation();
        sumInfo.setTitle("员工信息表");
        sumInfo.setAuthor("wangxiaowang");

        //处理数据
        HSSFSheet sheet = workbook.createSheet("员工信息表");//下面的表单sheet1、sheet2那种
        HSSFRow tittleRow = sheet.createRow(0);//从第0行
        HSSFCell c0 = tittleRow.createCell(0);//第0列
        c0.setCellValue("编号");//设置列名
            //样式
        HSSFCellStyle headerStyle=workbook.createCellStyle();//创建样式
        headerStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());//背景颜色
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        sheet.setColumnWidth(0,5*256);//此处仅处理0列，不想复制了
        c0.setCellStyle(headerStyle);
            //第二列 及其后面的
        HSSFCell c1 = tittleRow.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("姓名");
        HSSFCell c2 = tittleRow.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("性别");
        HSSFCell c3 = tittleRow.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("工号");
        HSSFCell c4 = tittleRow.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("出生日期");
        HSSFCell c5 = tittleRow.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("身份证号");
        HSSFCell c6 = tittleRow.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("婚姻状况");
        HSSFCell c7 = tittleRow.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("民族");
        HSSFCell c8 = tittleRow.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("籍贯");
        HSSFCell c9 = tittleRow.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("政治面貌");
        HSSFCell c10 = tittleRow.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("电子邮件");
        HSSFCell c11 = tittleRow.createCell(11);
        c11.setCellValue("电话号码");
        c11.setCellStyle(headerStyle);
        HSSFCell c12 = tittleRow.createCell(12);
        c12.setCellValue("略");
        c12.setCellStyle(headerStyle);
        HSSFCell c13 = tittleRow.createCell(13);
        c13.setCellValue("略");
        c13.setCellStyle(headerStyle);
        HSSFCell c14 = tittleRow.createCell(14);
        c14.setCellValue("略");
        c14.setCellStyle(headerStyle);
        HSSFCell c15 = tittleRow.createCell(15);
        c15.setCellValue("略");
        c15.setCellStyle(headerStyle);
        HSSFCell c16 = tittleRow.createCell(16);
        c16.setCellValue("略");
        c16.setCellStyle(headerStyle);
        HSSFCell c17 = tittleRow.createCell(17);
        c17.setCellValue("略");
        c17.setCellStyle(headerStyle);
        HSSFCell c18 = tittleRow.createCell(18);
        c18.setCellValue("略");
        c18.setCellStyle(headerStyle);
        HSSFCell c19 = tittleRow.createCell(19);
        c19.setCellValue("略");
        c19.setCellStyle(headerStyle);
        HSSFCell c20 = tittleRow.createCell(20);
        c20.setCellValue("略");
        c20.setCellStyle(headerStyle);
        HSSFCell c21 = tittleRow.createCell(21);
        c21.setCellValue("略");
        c21.setCellStyle(headerStyle);
        HSSFCell c22 = tittleRow.createCell(22);
        c22.setCellValue("略");
        c22.setCellStyle(headerStyle);
        HSSFCell c23 = tittleRow.createCell(23);
        c23.setCellValue("略");
        c23.setCellStyle(headerStyle);
        HSSFCell c24 = tittleRow.createCell(24);
        c24.setCellValue("略");
        c24.setCellStyle(headerStyle);
        //真正开始处理数据
        for(int i=0;i<list.size();i++){
            Employee emp=list.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(emp.getId());
            row.createCell(1).setCellValue(emp.getName());
            //略，其他同此。24列
        }

        //以上属于生成文件，以下属于下载
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HttpHeaders headers=new HttpHeaders();
        try{
            headers.setContentDispositionFormData("attachment",new String("员工表.xls".getBytes("utf-8"),"ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(byteArrayOutputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(),headers, HttpStatus.CREATED);
    }

    public static List<Employee> excel2Employee(MultipartFile file) {
        List<Employee> list=new ArrayList<>();
        Employee employee;
        try{
            HSSFWorkbook workbook=new HSSFWorkbook(file.getInputStream());
            int numberOfSheets = workbook.getNumberOfSheets();
            for(int i=0;i<numberOfSheets;i++){
                HSSFSheet sheet = workbook.getSheetAt(i);
                int numberOfRows = sheet.getPhysicalNumberOfRows();
                for(int j=0;j<numberOfRows;j++){
                    if(j==0){
                        continue;//第一行是标题
                    }
                    HSSFRow row = sheet.getRow(j);
                    if(row==null) continue;//防止空行
                    employee=new Employee();
                    int numberOfCellscells = row.getPhysicalNumberOfCells();
                    for(int k=0;k<numberOfCellscells;k++){
                        HSSFCell cell = row.getCell(k);
                        //有可能是日期格式，此处略去。只处理字符串（偷懒xls里没日期)
                        if(cell.getCellType()== CellType.STRING){
                            String cellValue = cell.getStringCellValue();
                            if(cellValue==null) continue;
                            switch (k) {
                                //根据列数决定value是哪个字段并生成对象
                                case 1:
                                    employee.setName(cellValue);
                                    break;
                                case 2:
                                    employee.setWorkID(cellValue);
                                    break;///往下的略去
                            }
                        }
                    }
                    list.add(employee);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }
}
