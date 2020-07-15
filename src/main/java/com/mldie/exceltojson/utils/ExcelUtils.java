package com.mldie.exceltojson.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.ArrayList;

public class ExcelUtils {
    public static ArrayList<JSONObject> excelToShopIdList(InputStream inputStream) {
        ArrayList<JSONObject> JsonList = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            //总行数
            int rowLength = sheet.getLastRowNum();
                        System.out.println("总行数有多少行" + rowLength);
            //工作表的列那一行
//            Row row = sheet.getRow(0);
            Row row = sheet.getRow(1);
            //总列数
            int colLength = row.getLastCellNum();
            System.out.println("总列数有多少列" + colLength);

            //获得表头的key
            ArrayList<String> colKeyList = new ArrayList<>();
            for (int i = 0; i < colLength; i++) {
                Cell cell = sheet.getRow(1).getCell(i);
                if(cell != null) {
                    cell.setCellType(CellType.STRING);

                    colKeyList.add(cell.getStringCellValue().trim());
                } else {
                    colKeyList.add("");
                }
            }
            System.out.println("所有的key值为：" + colKeyList.toString());

            //得到指定的单元格
            for (int i = 2; i < rowLength - 2; i++) {
//                ExcelBean jiFenExcel = new ExcelBean();
                row = sheet.getRow(i);
//                cell = row.getCell(0);
//                Cell keyCell = row.getCell(0);
//                Cell valueCell = row.getCell(1);

                JSONObject rowJson = new JSONObject();
                for (int j = 0; j < colLength; j++) {
                    //列： 0key    1value
                    Cell cell = row.getCell(j);
                    //                    System.out.print(cell + ",");
                    if (cell != null && colKeyList.get(j) != null && !"".equals(colKeyList.get(j))) {
//                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellType(CellType.STRING);
                        String data = cell.getStringCellValue();
                        data = data.trim();
                        rowJson.put(colKeyList.get(j), data);
                        //                        System.out.print(data);
                        //                        if (StringUtils.isNumeric(data)) {
//                        if (j == 0) {
//                            jiFenExcel.setKey(data);
//                        } else if (j == 1) {
//                            jiFenExcel.setValue(data);
//                        }
                        //                        }
                    }
                }
                JsonList.add(rowJson);
//                if (keyCell != null && valueCell != null) {
//                    keyCell.setCellType(CellType.STRING);
//                    valueCell.setCellType(CellType.STRING);
//                    String keyData = keyCell.getStringCellValue().trim();
//                    String valueData =valueCell.getStringCellValue().trim();
//                    fileJson.put(keyData, valueData);
//                }

                //                System.out.println("====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonList;
    }
}
