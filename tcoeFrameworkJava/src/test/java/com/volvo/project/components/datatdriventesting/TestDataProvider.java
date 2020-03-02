package com.volvo.project.components.datatdriventesting;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;


import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TestDataProvider {
    /*
        to achieve parallel testing , parallel parameter has to be set to true
     */
    @DataProvider(name = "staticExcelDataProvider", parallel = false)
    public Object[][] TestDataProviderReader(final Method testMethod) {
        String excel = "/testdata/";
        DataProviderArguments dataFile = testMethod.getAnnotation(DataProviderArguments.class);
        if (dataFile != null)
            excel += dataFile.file();
        else
            excel += "LoginWithIncorrectPasswords.xlsx";
        ExcelLibrary excelReader = new ExcelLibrary();
        int totalNumberOfRows = excelReader.getRowCount(excel);
        Object[][] array = new Object[totalNumberOfRows][];
        for (int row = 1; row <= totalNumberOfRows; row++) {
            array[row - 1][0] = excelReader.readFromExcel(row, 0, excel);
            array[row - 1][1] = excelReader.readFromExcel(row, 1, excel);
        }
        return array;
    }

    @DataProvider(name = "getDataFromFile", parallel = false)
    public static Object[][] getDataFromLocalFile(final Method testMethod) {
        DataProviderArguments dataFile = testMethod.getAnnotation(DataProviderArguments.class);
        ExcelLibrary excelReader = new ExcelLibrary();
        return excelReader.readFromExcel(dataFile.file());
    }

    @DataProvider(name = "getExcelDataFromFile", parallel = false)
    public static Object[][] getDataFromExcelTab(final Method testMethod) {
        ExcelDataProvider dataFile = testMethod.getAnnotation(ExcelDataProvider.class);
        ExcelLibrary excelReader = new ExcelLibrary();
        return excelReader.readFromExcelTab(dataFile.fileName(), dataFile.tab());
    }

    @DataProvider(name = "getDataFromExcelTabAsMap", parallel = false)
    public static Iterator<Object[]> getDataFromExcelTabAsMap(final Method testMethod) {
        ExcelDataProvider dataFile = testMethod.getAnnotation(ExcelDataProvider.class);
        ExcelLibrary excelReader = new ExcelLibrary();
        Object[][] excelResults = excelReader.readFromExcelTabWithHeaders(dataFile.fileName(), dataFile.tab());
        List<Object[]> excelResultsAsMap = new ArrayList<>();
        for (int i = 1; i < excelResults.length; i++) {
            Map<Object, Object> map = new HashMap<>();
            for (int j = 0; j < excelResults[0].length; j++) {
                map.put(excelResults[0][j], excelResults[i][j]);
            }
            excelResultsAsMap.add(new Object[]{map});
        }
        return excelResultsAsMap.iterator();
    }

    @DataProvider(name = "getDataFromJson", parallel = false)
    public static Iterator<Object[]> getDataFromJson(final Method testMethod) {
        DataProviderArguments dataFile = testMethod.getAnnotation(DataProviderArguments.class);
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader("src/test/resources/testdata/" + dataFile.file());
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj.get("dataSet");
            ArrayList<Object[]> jsonResultAsList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonResultAsList.add(new Object[]{jsonArray.get(i)});
            }
            return jsonResultAsList.iterator();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}