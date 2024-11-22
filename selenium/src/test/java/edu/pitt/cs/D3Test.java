package edu.pitt.cs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class D3Test {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @Before
    public void setUp() {
        // Create ChromeOptions for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run without a GUI
        options.addArguments("--disable-gpu"); // Disable GPU acceleration for compatibility
        options.addArguments("--window-size=1920,1080"); // Set default resolution for headless Chrome
        options.addArguments("--no-sandbox"); // Required for some CI environments
        options.addArguments("--disable-dev-shm-usage"); // Optimize shared memory for CI

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait set globally
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void tEST1LINKS() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");

        WebElement resetLink = driver.findElement(By.xpath("//a[text()='Reset']"));
        String hrefValue = resetLink.getAttribute("href");
        assertThat(hrefValue, is("http://localhost:8080/reset"));
        System.out.println("Reset link points to: " + hrefValue);
    }

    @Test
    public void tEST2RESET() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=true\";document.cookie = \"2=true\";document.cookie = \"3=true\";");
        driver.findElement(By.linkText("Reset")).click();
        assertThat(driver.findElement(By.xpath("//*[@id=\"cat-id1\"]")).getText(), is("ID 1. Jennyanydots"));
        assertThat(driver.findElement(By.xpath("//*[@id=\"cat-id2\"]")).getText(), is("ID 2. Old Deuteronomy"));
        assertThat(driver.findElement(By.xpath("//*[@id=\"cat-id3\"]")).getText(), is("ID 3. Mistoffelees"));
    }

    @Test
    public void tEST3CATALOG() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
        driver.findElement(By.linkText("Catalog")).click();

        WebElement element = driver.findElement(By.xpath("(//img)[2]"));
        String attribute = element.getAttribute("src");
        vars.put("secondImageSrc", attribute);
        System.out.println(vars.get("secondImageSrc").toString());
        assertEquals(vars.get("secondImageSrc").toString(), "http://localhost:8080/images/cat2.jpg");
    }

    @Test
    public void tEST4LISTING() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
        driver.findElement(By.linkText("Catalog")).click();
        {
            List<WebElement> elements = driver.findElements(By.xpath("//ul/li[1]"));
            assert (elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.xpath("//ul/li[2]"));
            assert (elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.xpath("//ul/li[3]"));
            assert (elements.size() > 0);
        }
        assertThat(driver.findElement(By.xpath("//ul[@class='list-group']/li[3]")).getText(), is("ID 3. Mistoffelees"));
    }

    @Test
    public void tEST5RENTACAT() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
        driver.findElement(By.linkText("Rent-A-Cat")).click();
        {
            List<WebElement> elements = driver.findElements(By.cssSelector(".form-group:nth-child(3) .btn"));
            assert (elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.cssSelector(".form-group:nth-child(4) .btn"));
            assert (elements.size() > 0);
        }
    }

    @Test
    public void tEST6RENT() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
        driver.findElement(By.linkText("Rent-A-Cat")).click();
        driver.findElement(By.id("rentID")).click();
        driver.findElement(By.id("rentID")).sendKeys("1");
        driver.findElement(By.cssSelector(".form-group:nth-child(3) .btn")).click();
        assertThat(driver.findElement(By.xpath("//ul[@class='list-group']/li[1]")).getText(), is("Rented out"));
        assertThat(driver.findElement(By.xpath("//ul[@class='list-group']/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
        assertThat(driver.findElement(By.xpath("//ul[@class='list-group']/li[3]")).getText(), is("ID 3. Mistoffelees"));
        assertThat(driver.findElement(By.id("rentResult")).getText(), is("Success!"));
    }

    @Test
    public void tEST7RETURN() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=true\";document.cookie = \"3=false\";");
        driver.findElement(By.linkText("Rent-A-Cat")).click();
        driver.findElement(By.id("returnID")).click();
        driver.findElement(By.id("returnID")).sendKeys("2");
        driver.findElement(By.cssSelector(".form-group:nth-child(4) .btn")).click();
        assertThat(driver.findElement(By.xpath("//*[@id=\"cat-id1\"]")).getText(), is("ID 1. Jennyanydots"));
        assertThat(driver.findElement(By.xpath("//*[@id=\"cat-id2\"]")).getText(), is("ID 2. Old Deuteronomy"));
        assertThat(driver.findElement(By.xpath("//*[@id=\"cat-id3\"]")).getText(), is("ID 3. Mistoffelees"));
        assertThat(driver.findElement(By.xpath("//*[@id=\"returnResult\"]")).getText(), is("Success!"));
    }

    @Test
    public void tEST8FEEDACAT() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
        driver.findElement(By.linkText("Feed-A-Cat")).click();
        {
            List<WebElement> elements = driver.findElements(By.xpath("//button[contains(.,'Feed')]"));
            assert (elements.size() > 0);
        }
    }

    @Test
    public void tEST10GREETACAT() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
        driver.findElement(By.linkText("Greet-A-Cat")).click();
        assertThat(driver.findElement(By.xpath("//*[@id=\"greeting\"]/h4")).getText(), is("Meow!Meow!Meow!"));
    }

    @Test
    public void tEST11GREETACATWITHNAME() {
        driver.get("http://localhost:8080/");
        js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
        driver.get("http://localhost:8080/greet-a-cat/Jennyanydots");
        assertThat(driver.findElement(By.xpath("//*[@id=\"greeting\"]/h4")).getText(), is("Meow! from Jennyanydots."));
    }
}
