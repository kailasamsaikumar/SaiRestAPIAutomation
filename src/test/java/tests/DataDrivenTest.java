package tests;

import Modules.UrBuddiAPI;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.FileManager;
import utils.ReportManager;
import utils.ReusableFunctions;

@Epic("DataDrivenAPi")
@Feature("Crud operation")
public class DataDrivenTest extends ReportManager {
    public String hrTokenResponse, employeeTokenResponse, leadTokenResponse, emailId, firstName, lastName, url, birthDate, employeeId, leaveStartDateMonthYear, leaveEndDateMonthYear, leaveId, joiningDate, password, phoneNumber, location, reportName, sheetName;
    UrBuddiAPI urBuddiAPI = new UrBuddiAPI();
    ReusableFunctions reusableFunctions = new ReusableFunctions();
    FileManager fileManager = new FileManager();
    List<Object[]> objectList = new ArrayList<>();
    List<List<Object[]>> dataList = new ArrayList();
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

    @BeforeClass
    public void createExcelFileWithData() throws IOException, InterruptedException {
        reportName = "report_" + reusableFunctions.getTimeNowInSec();
        sheetName = "API_INFO";
        List<String> headers = Arrays.asList("loginEndPoint", "hrloginPayload", "addEmployeeEndpoint", "addEmployeePayload",
                "employeeLoginPayload", "applyLeaveEndpoint", "applyLeavePayload", "leaveIdEndpoint", "leadLoginPayload", "approveLeaveEndpoint", "approveLeavePayload", "resourceEndpoint",
                "resourcePayload");
        Collections.addAll(headers);
        String loginEndpoint = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
                + fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("loginEndpoint")
                .getAsString();
        JsonObject hrLoginPayload = fileManager.getJsonObjectData(loginPayloads).getAsJsonObject()
                .get("hrLoginInfo")
                .getAsJsonObject();
        String addEmployeeEndpoint = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
                + fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("addEmployeeURI")
                .getAsString();

        for (int i = 0; i < 3; i++) {
            List<String> randomEmployeeDetails = new ArrayList<>();
            List<String> employeeLoginDetails = new ArrayList<>();
            List<String> employeeLeaveDetails = new ArrayList<>();
            List<String> employeeDeviceDetails = new ArrayList<>();
            List<JsonObject> employeeDetailsList = new ArrayList<>();
            List<JsonObject> employeeDevicesList = new ArrayList<>();
            List<JsonObject> employeeLeavesList = new ArrayList<>();
            String employeeId = reusableFunctions.getTimeNowInSec();
            Thread.sleep(3000);
            System.out.println("employee id is : " + employeeId);
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

            JsonObject addEmployeePayload = fileManager.getJsonObjectData(employeePayloads).getAsJsonObject().get("employeeFullDetails")
                    .getAsJsonObject();

            employeeLoginPayload = fileManager.getJsonObjectData(loginPayloads).getAsJsonObject()
                    .get("employeeLoginInfo")
                    .getAsJsonObject();

            String leaveEndpoint = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
                    + "/v1/" + employeeId + "/leave";

            String leaveStartDate = reusableFunctions.getDate("yyyy-MM-dd", 2);
            leaveStartDateMonthYear = reusableFunctions.getDate("yyyy-M", 2);
            String leaveEndDate = reusableFunctions.getDate("yyyy-MM-dd", 8);
            leaveEndDateMonthYear = reusableFunctions.getDate("yyyy-M ", 8);
            long leaveDaysCount = reusableFunctions.getDifference(leaveStartDate, leaveEndDate);
            employeeLeaveDetails.add(employeeId);
            employeeLeaveDetails.add(emailId);
            employeeLeaveDetails.add("twllead@optimworks.com");
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

            JsonObject applyLeavePayload = fileManager.getJsonObjectData(employeeLeavePayloads)
                    .getAsJsonObject()
                    .get("leaveInfo").getAsJsonObject();

            String leaveIdEndpoint = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
                    + "/v1/" + employeeId + "/leaves?start_month="
                    + leaveStartDateMonthYear.trim() + "&end_month=" + leaveEndDateMonthYear.trim();


            JsonObject leadLoginPayload = fileManager.getJsonObjectData(loginPayloads).getAsJsonObject()
                    .get("leadLoginInfo")
                    .getAsJsonObject();

            String leaveApprovalEndpoint = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString()
                    + "/v1/TWL01/leave/";
            JsonObject leaveApprovalPayload = fileManager.getJsonObjectData(leaveApprovalPayloads).getAsJsonObject()
                    .get("approveLeaveDetails")
                    .getAsJsonObject();

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

            String resourcesEndpoint = fileManager.getJsonObjectData(endPointURIs).getAsJsonObject().get("baseURI").getAsString() + "/v1/TWL02/resource";
            JsonObject addResourcePayload = fileManager.getJsonObjectData(employeeDevicePayloads).getAsJsonObject()
                    .get("devicesInfo")
                    .getAsJsonObject();


            Object[] excelData = reusableFunctions.generateDynamicObject(loginEndpoint, hrLoginPayload, addEmployeeEndpoint, addEmployeePayload, employeeLoginPayload, leaveEndpoint, applyLeavePayload, leaveIdEndpoint, leadLoginPayload, leaveApprovalEndpoint, leaveApprovalPayload, resourcesEndpoint, addResourcePayload);
            objectList.add(excelData);
        }
        System.out.println("Object list is : " + dataList.add(objectList));

        Object[][] twoDimensionalArray = new Object[objectList.size()][];
        objectList.toArray(twoDimensionalArray);
        File directoryPath = new File((System.getProperty("user.dir")
                + "\\src\\test\\java\\resources"));
        final String forwardSlashDelimiter = "\\";
        File imagePath = new File(directoryPath + forwardSlashDelimiter + reportName + ".xlsx");

        fileManager.createExcelFile(sheetName, headers, twoDimensionalArray, imagePath);
    }

    @DataProvider(name = "urBuddi")
    public Object[][] getExcelData() throws IOException {
        String filePath = System.getProperty("user.dir")
                + "\\src\\test\\java\\resources\\" + reportName + ".xlsx";
        Object[][] data = fileManager.getExcelData(filePath, sheetName);
        System.out.println("excel data is : " + data);
        return data;
    }

    @Test(dataProvider = "urBuddi")
    public void executeApisFromExcel(String loginEndpoint, String payload, String addEmployeeEndpoint, String addEmployeePayload, String newEmployeeLoginPayload, String leaveEndpoint, String updatedLeaveDetailsPayload, String leaveIdEndpoint, String leadLoginPayload, String leaveApprovalEndpoint, String leaveApprovalPayload, String resourcesEndpoint, String updatedEmployeeDevices) {
        System.out.println("login end pont is : " + loginEndpoint);
        SoftAssert softAssert = new SoftAssert();

        JsonObject hrResponse = urBuddiAPI.getLoginToken(loginEndpoint, reusableFunctions.getJsonResponse(payload));
        hrTokenResponse = "bearer " + hrResponse.get("token").getAsString();

        JsonObject addEmployeeResponse = urBuddiAPI.addEmployee(addEmployeeEndpoint, hrTokenResponse, reusableFunctions.getJsonResponse(addEmployeePayload));
        System.out.println("Add employee response : " + addEmployeeResponse);
        softAssert.assertEquals(addEmployeeResponse.get("message").getAsString(), "success", "Failed to add an employee");

        JsonObject addResourceResponse = urBuddiAPI.addResourcesToEmployee(resourcesEndpoint, hrTokenResponse, reusableFunctions.getJsonResponse(updatedEmployeeDevices));
        System.out.println("Add resource response : " + addResourceResponse);
        softAssert.assertEquals(addResourceResponse.get("detail").getAsString(), "success", "Failed to add a device to an employee");

        JsonObject employeeResponse = urBuddiAPI.getLoginToken(loginEndpoint, reusableFunctions.getJsonResponse(newEmployeeLoginPayload));
        employeeTokenResponse = "bearer " + employeeResponse.get("token").getAsString();

        JsonObject employeeLeaveResponse = urBuddiAPI.applyLeave(leaveEndpoint, employeeTokenResponse, reusableFunctions.getJsonResponse(updatedLeaveDetailsPayload));
        System.out.println("Employee leave response : " + employeeLeaveResponse);
        softAssert.assertEquals(employeeLeaveResponse.get("detail").getAsString(), "leave applied successfully", "Failed to apply a leave");

        JsonElement response = urBuddiAPI.getLeaveId(leaveIdEndpoint, employeeTokenResponse);
        leaveId = response.getAsJsonArray().get(0).getAsJsonObject().get("leave_id").getAsString();
        System.out.println("Leave id : " + leaveId);
        System.out.println("first name is : "+reusableFunctions.getJsonResponse(updatedLeaveDetailsPayload).get("employee_email"));
        softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("first_name"), reusableFunctions.getJsonResponse(addEmployeePayload).get("first_name"), "First name is not matching");
        softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("last_name"), reusableFunctions.getJsonResponse(addEmployeePayload).get("last_name"), "Last name is not matching");
        softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("employee_email"), reusableFunctions.getJsonResponse(updatedLeaveDetailsPayload).get("employee_email"), "Employee email is not matching");
        softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("hr_email"), reusableFunctions.getJsonResponse(updatedLeaveDetailsPayload).get("hr_email"), "Reporting email is not matching");
        softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("start_date"), reusableFunctions.getJsonResponse(updatedLeaveDetailsPayload).get("start_date"), "Start date is not matching");
        softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("end_date"), reusableFunctions.getJsonResponse(updatedLeaveDetailsPayload).get("end_date"), "End date is not matching");
        softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("days").getAsInt(), reusableFunctions.getJsonResponse(updatedLeaveDetailsPayload).get("days").getAsInt(), "Leave days count is not matching");
        softAssert.assertEquals(response.getAsJsonArray().get(0).getAsJsonObject().get("current_month_leaves").getAsInt(), response.getAsJsonArray().size(), "Current month leaves not matching");

        JsonObject leadResponse = urBuddiAPI.getLoginToken(loginEndpoint, reusableFunctions.getJsonResponse(leadLoginPayload));
        leadTokenResponse = "bearer " + leadResponse.get("token").getAsString();

        System.out.println("Leave approval end point is : " + leaveApprovalEndpoint + "/" + leaveId);

        JsonObject approveLeaveResponse = urBuddiAPI.approveOrRejectLeave(leaveApprovalEndpoint + leaveId, leadTokenResponse, reusableFunctions.getJsonResponse(leaveApprovalPayload));
        System.out.println("Leave approval response : " + approveLeaveResponse);
        softAssert.assertEquals(approveLeaveResponse.get("message").getAsString(),"success","Failed to approve leave");

        softAssert.assertAll();

    }
}
