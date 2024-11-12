package utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

public class BaseApi {

  protected static String url;
  protected HashMap<String, String> baseHeaders = new HashMap<>();
  protected HashMap<String, String> payload = new HashMap<>();
  Response response;


  public JsonObject executePostRequest(int statusCode) {
    response = getPostRequestResponse();
    validateStatusCode(statusCode);
    getResponseTime();
    String postResponse = response.asString();
    return getJsonResponse(postResponse);
  }

  public JsonObject executePutRequest(int statusCode) {
    response = getPutRequestResponse();
    validateStatusCode(statusCode);
    getResponseTime();
    String postResponse = response.asString();
    return getJsonResponse(postResponse);
  }

  public JsonElement executeGetRequest(int statusCode) {
    response = getGetRequestResponse();
    validateStatusCode(statusCode);
    getResponseTime();
    String getResponse = response.asString();
    return getJsonArrayResponse(getResponse);
  }

  public JsonElement getJsonArrayResponse(String response) {
    return JsonParser.parseString(response);
  }

  public JsonObject getJsonResponse(String response) {
    return JsonParser.parseString(response).getAsJsonObject();
  }

  public Response getPostRequestResponse() {
    return RestAssured.given().contentType(ContentType.JSON).headers(baseHeaders).body(payload)
        .when().post(url).then().extract()
        .response();
  }

  public Response getPutRequestResponse() {
    return RestAssured.given().contentType(ContentType.JSON).headers(baseHeaders).body(payload)
        .when().put(url).then().extract()
        .response();
  }

  public Response getGetRequestResponse() {
    return RestAssured.given().contentType(ContentType.JSON).headers(baseHeaders).body(payload)
        .when().get(url).then().extract()
        .response();
  }

  public void getResponseTime() {
    long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
    System.out.println("Response Time is : " + responseTime);
  }

  public void validateStatusCode(int statusCode) {
    int responseStatusCode = response.getStatusCode();
    System.out.println("json response message is : " + response.prettyPrint());
    if (responseStatusCode != statusCode) {

      Assert.assertEquals(responseStatusCode, statusCode,
          "Failed with status code: " + statusCode + "");
    }
  }

  public void createPayload(JsonObject data) {
    for (String key : data.keySet()) {
      if(data.get(key).isJsonNull()) {
        payload.put(key,null);
      } else {
        payload.put(key, data.get(key).getAsString());
      }
    }
  }
}
