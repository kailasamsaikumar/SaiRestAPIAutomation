package Modules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utils.BaseApi;

public class UrBuddiAPI extends BaseApi {
  public JsonObject getLoginToken(String url,JsonObject payload) {
    this.url = url;
    createPayload(payload);
    return executePostRequest(200);
  }

  public JsonObject addEmployee(String url,String token,JsonObject payload) {
    this.url = url;
    baseHeaders.put("Authorization", token);
    baseHeaders.put("Tenantid", "bef53bd4-55fb-4ef0-93f9-553f644805ee");
    createPayload(payload);
    return executePostRequest(200);
  }

  public JsonObject applyLeave(String url, String token,JsonObject payload) {
    this.url = url;
    baseHeaders.put("Authorization", token);
    baseHeaders.put("Tenantid", "bef53bd4-55fb-4ef0-93f9-553f644805ee");
    createPayload(payload);
    return executePostRequest(200);
  }

  public JsonElement getLeaveId(String url, String token) {
    this.url = url;
    baseHeaders.put("Authorization", token);
    baseHeaders.put("Tenantid", "bef53bd4-55fb-4ef0-93f9-553f644805ee");
    return executeGetRequest(200);
  }

  public JsonObject approveOrRejectLeave(String url, String token, JsonObject payload) {
    this.url = url;
    baseHeaders.put("Authorization", token);
    baseHeaders.put("Tenantid", "bef53bd4-55fb-4ef0-93f9-553f644805ee");
    createPayload(payload);
    return executePutRequest(200);
  }

  public JsonObject addResourcesToEmployee(String url, String token, JsonObject payload) {
    this.url = url;
    baseHeaders.put("Authorization", token);
    baseHeaders.put("Tenantid", "bef53bd4-55fb-4ef0-93f9-553f644805ee");
    createPayload(payload);
    return executePostRequest(200);
  }
}
