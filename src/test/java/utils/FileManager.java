package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileManager {
  ObjectMapper objectMapper = new ObjectMapper();
  public JsonNode readJsonFile(String filePath) throws IOException {
    return objectMapper.readTree(new File(filePath));
  }

  public JsonObject getJsonObjectData(String filePath) throws IOException {
    JsonNode rootNode = readJsonFile(filePath);
    BaseApi baseApi = new BaseApi();
    return baseApi.getJsonResponse(rootNode.toString());
  }

  public void updateJsonFile(String filePath,JsonObject data) throws IOException {
    JsonNode rootNode = readJsonFile(filePath);
    int i = 0;
    for (String key : data.keySet()) {
      ((ObjectNode) rootNode).put(key, "new value");;
    }
    objectMapper.writeValue(new File(filePath), rootNode);
  }

  public void updateJsonFile(String filePath,JsonObject data, List<String> fakerData,String jsonObjectName) throws IOException {
    JsonNode rootNode = readJsonFile(filePath);
    int i = 0;
    for (String key : data.keySet()) {
      JsonNode jsonPath = rootNode.path(jsonObjectName);
      ((ObjectNode) jsonPath).put(key, fakerData.get(i));
      i++;
    }
    objectMapper.writeValue(new File(filePath), rootNode);
  }

  public void mergeJsonObjects(String filePath,String filePath2,JsonObject jsonData,String jsonObject) throws IOException {
    ObjectNode jsonNode = (ObjectNode) readJsonFile(filePath);
    ObjectNode employeeInfo = (ObjectNode) jsonNode.get(jsonObject);

    for (String key : jsonData.keySet()) {
      if (jsonData.get(key).isJsonNull()) {
        employeeInfo.set(key, null);
      } else {
        employeeInfo.put(key, jsonData.get(key).getAsString());
      }
    }
    System.out.println("Json response is: " + jsonNode);
    objectMapper.writeValue(new File(filePath2), jsonNode);
  }

  public void mergeJsonObjects(String filePath,JsonObject jsonData,String jsonObject) throws IOException {
    ObjectNode jsonNode = (ObjectNode) readJsonFile(filePath);
    ObjectNode employeeInfo = (ObjectNode) jsonNode.get(jsonObject);

    for (String key : jsonData.keySet()) {
      if (jsonData.get(key).isJsonNull()) {
        employeeInfo.set(key, null);
      } else {
        employeeInfo.put(key, jsonData.get(key).getAsString());
      }

    }
    System.out.println("Json response is: " + jsonNode);
    objectMapper.writeValue(new File(filePath), jsonNode);
  }

  public void mergeJsonObjects(String filePath,JsonObject jsonData,JsonObject jsonData1,String jsonObject) throws IOException {
    ObjectNode jsonNode = (ObjectNode) readJsonFile(filePath);
    ObjectNode employeeInfo = (ObjectNode) jsonNode.get(jsonObject);
    System.out.println("Json data is : "+jsonData);
    System.out.println("Json data1 is : "+jsonData1);

    for (String key : jsonData.keySet()) {
      if (jsonData.get(key).isJsonNull()) {
        employeeInfo.set(key, null);
      } else {
        employeeInfo.put(key, jsonData.get(key).getAsString());
      }
    }
    for (String key : jsonData1.keySet()) {
      if (jsonData1.get(key).isJsonNull()) {
        employeeInfo.set(key, null);
      } else {
        employeeInfo.put(key, jsonData1.get(key).getAsString());
      }
    }
    System.out.println("Json response is: " + jsonNode);
    objectMapper.writeValue(new File(filePath), jsonNode);
  }

  public void mergeJsonObjects(String filePath,List<JsonObject> jsonData,String jsonObject) throws IOException {
    ObjectNode jsonNode = (ObjectNode) readJsonFile(filePath);
    ObjectNode employeeInfo = (ObjectNode) jsonNode.get(jsonObject);

    for(JsonObject jsonResponse : jsonData) {
      System.out.println("Json data response is : "+jsonResponse);
      for (String key : jsonResponse.keySet()) {
        if (jsonResponse.get(key).isJsonNull()) {
          employeeInfo.set(key, null);
        } else {
          employeeInfo.put(key, jsonResponse.get(key).getAsString());
        }
      }
    }
    System.out.println("Json response is: " + jsonNode);
    objectMapper.writeValue(new File(filePath), jsonNode);
  }

  public void createExcelFile(String sheetName, List<String> headers, Object[][] data,
      File imagePath) {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet(sheetName);
    Row headerRow = sheet.createRow(0);
    for (int i = 0; i < headers.size(); i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers.get(i));
    }
    for (int i = 0; i < data.length; i++) {
      Row row = sheet.createRow(i + 1);
      for (int j = 0; j < data[i].length; j++) {
        Cell cell = row.createCell(j);
        cell.setCellValue(String.valueOf(data[i][j]));
      }
    }
    try (FileOutputStream fileOut = new FileOutputStream(imagePath)) {
      workbook.write(fileOut);
      System.out.println("Excel file created successfully.");
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      workbook.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Object[][] getExcelData(String fileName,String sheetName) throws IOException {
    FileInputStream inputStream = new FileInputStream(fileName);
    Workbook workbook = new XSSFWorkbook(inputStream);
    Sheet sheet = workbook.getSheet(sheetName);

    int numRows = sheet.getLastRowNum();
    int numCols = sheet.getRow(0).getPhysicalNumberOfCells();
    System.out.println("number of rows is : "+numRows);
    System.out.println("number of columns is : "+numCols);
    Object[][] data = new Object[numRows][numCols];
    for(int i=1;i<=numRows;i++) {
      Row row = sheet.getRow(i);
      for(int j=0;j<numCols;j++) {
        Cell cell = row.getCell(j);
        data[i-1][j] = cell.getStringCellValue();
        System.out.println("row value is : "+i+" and column value is : "+j+": and cell value is : "+cell.getStringCellValue());
      }
    }
    return data;
  }
}
