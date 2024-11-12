package utils;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import com.github.javafaker.Address;
import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import com.github.javafaker.Internet;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ReusableFunctions extends BaseApi {
  Faker faker = new Faker();

  public String createDate(String date) throws Exception {
    SimpleDateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a");
    Date dateInstance = inputFormat.parse(date);
    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = outputFormat.format(dateInstance);
    System.out.println("Input Date: " + date);
    System.out.println("Output Date: " + formattedDate);
    return formattedDate;
  }

  public String getTimeNowInSec() {
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
    return formatter.format(date);
  }

  public String getDate(String dateFormat, int days) {
    LocalDate today = LocalDate.now();
    LocalDate threeMonthsAgo = today.plusDays(days);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    return threeMonthsAgo.format(formatter);
  }

  public long getDifference(String startDate, String endDate) {
    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);
    long diffInDays = ChronoUnit.DAYS.between(start, end);
    System.out.println(diffInDays);
    return diffInDays;
  }

  public Address getAddress() {
    return faker.address();
  }

  public Internet getInternet() {
    return faker.internet();
  }

  public DateAndTime getDate() {
    return faker.date();
  }

  public String getPhoneNumber() {
    return faker.phoneNumber().cellPhone();
  }

  public Object[] generateDynamicObject(String loginEndpoint, JsonObject payload,String addEmployeeEndpoint,JsonObject addEmployeePayload,JsonObject newEmployeeLoginPayload,String leaveEndpoint,JsonObject updatedLeaveDetailsPayload,String leaveIdEndpoint,JsonObject leadLoginPayload,String leaveApprovalEndpoint,JsonObject leaveApprovalPayload,String resourcesEndpoint,JsonObject updatedEmployeeDevices) {
    return new Object[]{loginEndpoint,payload,addEmployeeEndpoint,addEmployeePayload,newEmployeeLoginPayload,leaveEndpoint,updatedLeaveDetailsPayload,leaveIdEndpoint,leadLoginPayload,leaveApprovalEndpoint,leaveApprovalPayload,resourcesEndpoint,updatedEmployeeDevices};
  }
}
