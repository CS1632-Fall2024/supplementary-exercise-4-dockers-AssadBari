package edu.pitt.cs;

// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.After;
import static org.junit.Assert.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConnectTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;

  @Before
  public void setUp() {
    // Create ChromeOptions for headless mode
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless"); // Run without a GUI
    options.addArguments("--disable-gpu"); // Disable GPU acceleration for compatibility
    options.addArguments("--window-size=1920,1080"); // Set default resolution
    options.addArguments("--no-sandbox"); // Required for some CI environments
    options.addArguments("--disable-dev-shm-usage"); // Optimize shared memory for CI

    driver = new ChromeDriver(options);
    js = (JavascriptExecutor) driver;
    vars = new HashMap<>();
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void testConnection() {
    // Test that the webserver is ready to service an HTTP request
    driver.get("http://localhost:8080/");
  }
}
