package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class ReportManager {
  @BeforeSuite
  public void saveResultsWithDate() {
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
    String dateString = dateFormat.format(currentDate);
    File currentFolderPath = new File(System.getProperty("user.dir")+"//reports//allure-results");
    if(currentFolderPath.exists()) {
      File updatedFolderPath = new File(System.getProperty("user.dir")+"//reports//OldReport-"+dateString);
      currentFolderPath.renameTo(updatedFolderPath);
    }
  }

  public static void generateAllureReport() throws Exception {
    File directory = new File(System.getProperty("user.dir") + "//reports");
    System.setProperty("user.dir", directory.getAbsolutePath());
    // Execute the 'allure serve' command
    ProcessBuilder processBuilder = new ProcessBuilder("C://Program Files//allure-2.25.0//allure-2.25.0//bin//allure.bat", "serve", "allure-results");

    // Set the working directory to the specified directory
    processBuilder.directory(directory);

    // Start the process
    Process process = processBuilder.start();
    Thread.sleep(5000);
    process.destroy();
  }
}
