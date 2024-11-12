package tests;

import Modules.UrBuddiAPI;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.FileManager;
import utils.ReportManager;
import utils.ReusableFunctions;

@Epic("Normal Execution")
@Feature("Crud operation")
public class CrudAPITest extends ReportManager {

  UrBuddiAPI urBuddiAPI = new UrBuddiAPI();
  ReusableFunctions reusableFunctions = new ReusableFunctions();
  FileManager fileManager = new FileManager();
  public String bearerToken = "bearer ";
  public String hrTokenResponse, employeeTokenResponse, leadTokenResponse, emailId, firstName, lastName, url, birthDate, employeeId,leaveStartDate,leaveEndDate, leaveStartDateMonthYear, leaveEndDateMonthYear, leaveId, joiningDate, password, phoneNumber, location;
  public long leaveDaysCount;
  public String reportingPersonEmailID = "twllead@optimworks.com";
  String endPointURIs =
      System.getProperty("user.dir") + "\\src\\test\\java\\resources\\endpoints.json";
  String loginPayloads =
      System.getProperty("user.dir") + "\\src\\test\\java\\resources\\login.json";
  String employeePayloads =
      System.getProperty("user.dir") + "\\src\\test\\java\\resources\\employee.json";
  String employeeLeavePayloads =
      System.getProperty("user.dir") + "\\src\\test\\java\\resources\\leaves.json";
  String employeeDevicePayloads =
      System.getProperty("user.dir") + "\\src\\test\\java\\resources\\devices.json";
  String leaveApprovalPayloads =
      System.getProperty("user.dir") + "\\src\\test\\java\\resources\\approveOrRejectLeave.json";
  List<String> randomEmployeeDetails = new ArrayList<>();
  List<String> employeeLoginDetails = new ArrayList<>();
  List<String> employeeLeaveDetails = new ArrayList<>();
  List<String> employeeDeviceDetails = new ArrayList<>();
  List<JsonObject> employeeDetailsList = new ArrayList<>();
  List<JsonObject> employeeDevicesList = new ArrayList<>();
  List<JsonObject> employeeLeavesList = new ArrayList<>();

  @BeforeClass
  public void createData() throws Exception {
    employeeId = reusableFunctions.getTimeNowInSec();
    firstName = reusableFunctions.getAddress().firstName();
    lastName = reusableFunctions.getAddress().lastName();
    phoneNumber = reusableFunctions.getPhoneNumber();
    location = reusableFunctions.getAddress().city();
    birthDate = reusableFunctions.getDate().birthday(21, 26).toInstant().toString().split("T")[0];
    joiningDate = reusableFunctions.getDate().birthday(1, 16).toInstant().toString().split("T")[0];
    emailId = firstName + employeeId + "@yopmail.com";
    password = reusableFunctions.getInternet().password(8, 9);

    randomEmployeeDetails.add(firstName);
    randomEmployeeDetails.add(lastName);
    randomEmployeeDetails.add(employeeId);
    randomEmployeeDetails.add(phoneNumber);
    randomEmployeeDetails.add(emailId);
    randomEmployeeDetails.add(birthDate);
    randomEmployeeDetails.add(joiningDate);
    randomEmployeeDetails.add(password);
    randomEmployeeDetails.add(location);

    JsonObject employeeContactDetailsPayload = fileManager.getJsonObjectData(employeePayloads).getAsJsonObject()
        .get("employeeContactDetails")
        .getAsJsonObject();
    fileManager.updateJsonFile(employeePayloads, employeeContactDetailsPayload, randomEmployeeDetails, "employeeContactDetails");
    employeeContactDetailsPayload = fileManager.getJsonObjectData(employeePayloads).getAsJsonObject()
        .get("employeeContactDetails")
        .getAsJsonObject();
    JsonObject employeeQualificationDetailsPayload = fileManager.getJsonObjectData(employeePayloads)
        .getAsJsonObject()
        .get("employeeQualificationDetails").getAsJsonObject();
    employeeDetailsList.add(employeeContactDetailsPayload);
    employeeDetailsList.add(employeeQualificationDetailsPayload);
    fileManager.mergeJsonObjects(employeePayloads, employeeDetailsList,
        "employeeFullDetails");

    String domainName = "optimworks";
    String deviceToken = "";
    employeeLoginDetails.add(emailId);
    employeeLoginDetails.add(password);
    employeeLoginDetails.add(deviceToken);
    employeeLoginDetails.add(domainName);

    JsonObject employeeLoginPayload = fileManager.getJsonObjectData(loginPayloads).getAsJsonObject()
        .get("employeeLoginInfo")
        .getAsJsonObject();
    fileManager.updateJsonFile(loginPayloads, employeeLoginPayload, employeeLoginDetails,
        "employeeLoginInfo");

    leaveStartDate = reusableFunctions.getDate("yyyy-MM-dd", 2);
    leaveStartDateMonthYear = reusableFunctions.getDate("yyyy-M", 2);
    leaveEndDate = reusableFunctions.getDate("yyyy-MM-dd", 8);
    leaveEndDateMonthYear = reusableFunctions.getDate("yyyy-M", 8);
    leaveDaysCount = reusableFunctions.getDifference(leaveStartDate, leaveEndDate);
    employeeLeaveDetails.add(employeeId);
    employeeLeaveDetails.add(emailId);
    employeeLeaveDetails.add(reportingPersonEmailID);
    employeeLeaveDetails.add(leaveStartDate);
    employeeLeaveDetails.add(leaveEndDate);
    employeeLeaveDetails.add(String.valueOf(leaveDaysCount));

    JsonObject leaveDatesPayload = fileManager.getJsonObjectData(employeeLeavePayloads).getAsJsonObject()
        .get("leaveDates")
        .getAsJsonObject();
    fileManager.updateJsonFile(employeeLeavePayloads, leaveDatesPayload, employeeLeaveDetails,
        "leaveDates");
    JsonObject leaveReasonPayload = fileManager.getJsonObjectData(employeeLeavePayloads)
        .getAsJsonObject()
        .get("leaveReason").getAsJsonObject();
    leaveDatesPayload = fileManager.getJsonObjectData(employeeLeavePayloads).getAsJsonObject()
        .get("leaveDates")
        .getAsJsonObject();
    employeeLeavesList.add(leaveDatesPayload);
    employeeLeavesList.add(leaveReasonPayload);
    fileManager.mergeJsonObjects(employeeLeavePayloads, employeeLeavesList, "leaveInfo");


    employeeDeviceDetails.add(employeeId);
    employeeDeviceDetails.add(joiningDate);
    employeeDeviceDetails.add(employeeId);

    JsonObject devicesAllocatedDatePayload = fileManager.getJsonObjectData(employeeDevicePayloads).getAsJsonObject()
        .get("deviceAllocatedDate")
        .getAsJsonObject();
    fileManager.updateJsonFile(employeeDevicePayloads, devicesAllocatedDatePayload, employeeDeviceDetails, "deviceAllocatedDate");
    JsonObject employeeDevicePayload = fileManager.getJsonObjectData(employeeDevicePayloads).getAsJsonObject()
        .get("devices").getAsJsonObject();
    devicesAllocatedDatePayload = fileManager.getJsonObjectData(employeeDevicePayloads).getAsJsonObject()
        .get("deviceAllocatedDate")
        .getAsJsonObject();
    employeeDevicesList.add(devicesAllocatedDatePayload);
    employeeDevicesList.add(employeeDevicePayload);

    fileManager.mergeJsonObjects(employeeDevicePayloads, employeeDevicesList,
        "devicesInfo");
  }

  @Test
  public void urBuddiLoginWithHR() throws IOException {
    url = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
        + fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("loginEndpoint")
        .getAsString();
    JsonObject payload = fileManager.getJsonObjectData(loginPayloads).getAsJsonObject()
        .get("hrLoginInfo")
        .getAsJsonObject();
    JsonObject response = urBuddiAPI.getLoginToken(url, payload);
    hrTokenResponse = response.get("token").getAsString();
  }

  @Test(dependsOnMethods = "urBuddiLoginWithHR")
  public void addEmployee() throws Exception {
    SoftAssert softAssert = new SoftAssert();
    url = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
        + fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("addEmployeeURI")
        .getAsString();
    JsonObject payload = fileManager.getJsonObjectData(employeePayloads).getAsJsonObject()
        .get("employeeFullDetails")
        .getAsJsonObject();
    JsonObject addEmployeeResponse = urBuddiAPI.addEmployee(url, bearerToken + hrTokenResponse, payload);
    System.out.println("add employee response : "+addEmployeeResponse);
    softAssert.assertEquals(addEmployeeResponse.get("message").getAsString(),"success","Failed to add employee");
    softAssert.assertAll();
  }

  @Test(dependsOnMethods = "addEmployee")
  public void urBuddiLoginWithEmployee() throws IOException {
    url = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
        + fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("loginEndpoint")
        .getAsString();
    JsonObject newEmployeeLoginPayload = fileManager.getJsonObjectData(loginPayloads).getAsJsonObject()
        .get("employeeLoginInfo")
        .getAsJsonObject();
    JsonObject response = urBuddiAPI.getLoginToken(url, newEmployeeLoginPayload);
    employeeTokenResponse = bearerToken + response.get("token").getAsString();
    System.out.println("employee token response is : " + employeeTokenResponse);
  }

  @Test(dependsOnMethods = "urBuddiLoginWithEmployee")
  public void applyLeave() throws Exception {
    SoftAssert softAssert = new SoftAssert();
    url = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
        + "/v1/" + employeeId + "/leave";
    JsonObject payload = fileManager.getJsonObjectData(employeeLeavePayloads).getAsJsonObject()
        .get("leaveInfo").getAsJsonObject();
    System.out.println("employee token response is : " + employeeTokenResponse);
    JsonObject response = urBuddiAPI.applyLeave(url, employeeTokenResponse, payload);
    System.out.println("employee leave response : "+response);
    softAssert.assertEquals(response.get("detail").getAsString(),"leave applied successfully","Failed to apply a leave");
    softAssert.assertAll();
  }

  @Test(dependsOnMethods = "applyLeave")
  public void getLeaveId() throws IOException {
    SoftAssert softAssert = new SoftAssert();
    url = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
        + "/v1/" + employeeId + "/leaves?start_month="
        + leaveStartDateMonthYear.trim() + "&end_month=" + leaveEndDateMonthYear.trim();
    JsonElement response = urBuddiAPI.getLeaveId(url, employeeTokenResponse);
    leaveId = response.getAsJsonArray().get(0).getAsJsonObject().get("leave_id").getAsString();
    System.out.println("leave details response : "+response);
    softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("first_name").getAsString(),firstName,"First name is not matching");
    softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("last_name").getAsString(),lastName,"Last name is not matching");
    softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("employee_email").getAsString(),emailId,"Employee email is not matching");
    softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("hr_email").getAsString(),reportingPersonEmailID,"Reporting email is not matching");
    softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("start_date").getAsString(),leaveStartDate,"Start date is not matching");
    softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("end_date").getAsString(),leaveEndDate,"End date is not matching");
    softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("days").getAsInt(),leaveDaysCount,"Leave days count is not matching");
    softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("current_month_leaves").getAsInt(),response.getAsJsonArray().size(),"Current month leaves not matching");
    softAssert.assertAll();

  }

  @Test(dependsOnMethods = "getLeaveId")
  public void urBuddiLoginWithLead() throws IOException {
    url = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
        + fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("loginEndpoint")
        .getAsString();
    JsonObject payload = fileManager.getJsonObjectData(loginPayloads).getAsJsonObject()
        .get("leadLoginInfo")
        .getAsJsonObject();
    JsonObject response = urBuddiAPI.getLoginToken(url, payload);
    leadTokenResponse = response.get("token").getAsString();
  }

  @Test(dependsOnMethods = "urBuddiLoginWithLead")
  public void ApproveLeave() throws IOException {
    SoftAssert softAssert = new SoftAssert();
    url = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
        + "/v1/TWL01/leave/" + leaveId;
    JsonObject payload = fileManager.getJsonObjectData(leaveApprovalPayloads).getAsJsonObject()
        .get("approveLeaveDetails")
        .getAsJsonObject();
    JsonObject response = urBuddiAPI.approveOrRejectLeave(url, bearerToken + leadTokenResponse,
        payload);
    System.out.println("Respone is : " + response);
    softAssert.assertEquals(response.get("message").getAsString(),"success","Failed to approve leave");
    softAssert.assertAll();
  }

  @Test(dependsOnMethods = "ApproveLeave")
  public void addResourceToEmployee() throws IOException {
    SoftAssert softAssert = new SoftAssert();
    url = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()+"/v1/TWL02/resource";
    JsonObject payload = fileManager.getJsonObjectData(employeeDevicePayloads).getAsJsonObject()
        .get("devicesInfo")
        .getAsJsonObject();
    JsonObject response = urBuddiAPI.addResourcesToEmployee(url, bearerToken + hrTokenResponse,payload);
    System.out.println("Respone is : " + response);
    softAssert.assertEquals(response.get("detail").getAsString(),"success","Failed to add a device");
    softAssert.assertAll();
  }
}
