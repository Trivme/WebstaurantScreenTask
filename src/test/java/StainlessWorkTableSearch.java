import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    }

    @AfterEach
    void tearDown() throws Exception {
        driver.quit();
    }
    @Test
    void SearchTest() throws InterruptedException {
        String searchValue = "stainless work table";

        WebElement searchBox = driver.findElement(By.id("searchval"));
        WebElement searchBtn = driver.findElement(By.cssSelector("[value='Search']"));
        searchBox.sendKeys(searchValue);
        searchBtn.click();
        // compare the expected title of result page with actual title
        assertTrue(driver.findElement(By.xpath("//div[@id='search-elastic']//*[contains(text(),'Search Results for:')]")).getText().toLowerCase().contains(searchValue));

        //get pagination length
        int paginationLength;
        if(driver.findElements(By.cssSelector(".pagination ul li")).isEmpty()) {
            paginationLength = 1;
        } else {
            List <WebElement> pagination = driver.findElements(By.cssSelector(".pagination ul li"));
            WebElement lastPage = pagination.get(pagination.size() - 2);
            paginationLength = Integer.parseInt(lastPage.getText());
        }
        System.out.println(paginationLength);

        // check all result
        for(int i = 1; i <= paginationLength; i++) {
            Thread.sleep(1000);// wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#product_listing")));
            List<WebElement> pageSearchResults = driver.findElements(By.cssSelector("div.details > a.description"));

            System.out.println("!!!!!!!!Number of elements on page " + i + ": " + pageSearchResults.size());
            for(WebElement element: pageSearchResults) {
                assertTrue(element.getText().contains("Work Table"), element.getText());
            }
            WebElement nextBtn = driver.findElement(By.cssSelector(".icon-right-open"));
            nextBtn.click();

        }






        /*
        * for(for (WebElement ele: PageSearchResults){}
         * WebElement nextBtn =
         * driver.findElement(By.xpath("//*[@class='icon-right-open']"));
         *
         * List<WebElement> AllSearchResults =
         * driver.findElements(By.xpath("//div[@id]//a[@class='description']")); for(int
         * i=1; i<=9; i++) { //AllSearchResults.addAll(driver.findElements(By.xpath(
         * "//div[@id]//a[@class='description']"))); nextBtn.click(); }
         *
         * //int numberOfLinks = AllSearchResults.size();
         * //System.out.println(numberOfLinks); for(WebElement element:
         * AllSearchResults) System.out.println(element.getText());
         *
         * // xpath: //div[@id="product_listing"]//a[@href] - wrong //
         *
         * //List<WebElement> pagination =
         * driver.findElements(By.xpath("//div[@id='paging']//a"));;
         */


    }




}
