import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StainlessWorkTableSearch {

    private static WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() throws Exception {
        driver = new ChromeDriver();
        driver.get("https://www.webstaurantstore.com");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

    }

    @AfterEach
    void tearDown() throws Exception {
       driver.quit();
    }

    @Test
    void SearchTest() throws InterruptedException {
        String searchValue = "stainless work table";
        String checkValue = "Table";

        WebElement searchBox = driver.findElement(By.id("searchval"));
        WebElement searchBtn = driver.findElement(By.cssSelector("[value='Search']"));
        searchBox.sendKeys(searchValue);
        searchBtn.click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        // compare the expected title of result page with actual title
        assertTrue(driver.findElement(By.xpath("//div[@id='search-elastic']//*[contains(text(),'Search Results for:')]"))
                .getText().toLowerCase().contains(searchValue));

        //get pagination length
        int paginationLength;
        if(driver.findElements(By.cssSelector(".pagination ul li")).isEmpty()) {
            paginationLength = 1;
        } else {
            List <WebElement> pagination = driver.findElements(By.cssSelector(".pagination ul li"));
            WebElement lastPage = pagination.get(pagination.size() - 2);
            paginationLength = Integer.parseInt(lastPage.getText());
        }

        WebElement lastItem = null;
        // check all result on each paginate
        for(int i = 1; i <= paginationLength; i++) {
            (new WebDriverWait(driver, 3)).until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("#product_listing")));
            List<WebElement> pageSearchResults = driver.findElements(By.cssSelector("div.details > a.description"));
            lastItem = pageSearchResults.get(pageSearchResults.size()-1);
            for(WebElement element: pageSearchResults) {
                assertTrue(element.getText().contains(checkValue), lastItem.getText());
            }
            WebElement nextBtn = driver.findElement(By.cssSelector(".icon-right-open"));
            nextBtn.click();
        }

        //Add the last item into the cart
        assert lastItem != null;
        lastItem.click();
        (new WebDriverWait(driver, 3)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='buyButton']")));
        driver.findElement(By.xpath("//input[@id='buyButton']")).click();
        (new WebDriverWait(driver, 3)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='subject']/div[2]/div/a[1]/button")));
        driver.findElement(By.xpath("//*[@id='subject']/div[2]/div/a[1]/button")).click();

        //Empty cart
        (new WebDriverWait(driver, 3)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='main']/div[3]/form/div/div[1]/div/a")));
        driver.findElement(By.xpath("//*[@id='main']/div[3]/form/div/div[1]/div/a")).click();
        (new WebDriverWait(driver, 3)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(),'Empty Cart')]")));
        driver.findElement(By.xpath("//button[contains(text(),'Empty Cart')]")).click();
        (new WebDriverWait(driver, 3)).until(
                ExpectedConditions.textToBePresentInElementLocated(By.xpath("//p[@class='header-1']"), "Your cart is empty."));
        //(new WebDriverWait(driver, 3)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@class='header-1']")));

    }
}
