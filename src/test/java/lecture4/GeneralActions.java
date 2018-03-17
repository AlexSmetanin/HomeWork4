package lecture4;

import lecture4.utils.Properties;
import lecture4.model.ProductData;
import lecture4.utils.logging.CustomReporter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;


/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private String locator;
    private By by;
    private WebElement nextPageButton;



    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    /**
     * Logs in to Admin Panel.
     * @param login
     * @param password
     */
    public void login(String login, String password) {
        // TODO implement logging in to Admin Panel
        CustomReporter.logAction("User Login");
        driver.get(Properties.getBaseAdminUrl());
        wait.until(ExpectedConditions.elementToBeClickable(By.name("submitLogin")));
        driver.findElement(By.id("email")).sendKeys(login);
        driver.findElement(By.id("passwd")).sendKeys(password);
        driver.findElement(By.name("submitLogin")).click();
    }


    /**
     * Select menu item
     */
    public void selectOrdersItem(){
        CustomReporter.logAction("Select menu item");
        wait.until(ExpectedConditions.elementToBeClickable( By.cssSelector("span.employee_avatar_small")));
        WebElement orderTabElement = driver.findElement(By.id("subtab-AdminCatalog"));
        orderTabElement.click();
    }


    /**
     * Creat new product
     * @param newProduct
     */
    public void createProduct(ProductData newProduct) {
        // TODO implement product creation scenario
        // Click add new product button
        CustomReporter.logAction("Creat new product");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("page-header-desc-configuration-add")));
        driver.findElement(By.id("page-header-desc-configuration-add")).click();

        // Inputing product information
        CustomReporter.log("Input product information");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
        driver.findElement(By.id("form_step1_name_1")).sendKeys(newProduct.getName());
        driver.findElement(By.id("form_step1_qty_0_shortcut")).sendKeys(newProduct.getQty().toString());
        driver.findElement(By.id("form_step1_price_shortcut")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        driver.findElement(By.id("form_step1_price_shortcut")).sendKeys(newProduct.getPrice());

        // Click online swith
        CustomReporter.log("Click online switch");
        driver.findElement(By.className("switch-input")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl-message")));
        driver.findElement(By.className("growl-close")).click();

        // Ckick save button
        CustomReporter.log("Save product information");
        driver.findElement(By.id("submit")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl-message")));
        driver.findElement(By.className("growl-close")).click();
    }


    /**
     * Check  product visibility on website
     */
    // Open store main page and click "All products" link
     public void openStorePage() {
        CustomReporter.logAction("Check  product visibility on website");
        driver.get(Properties.getBaseUrl());
        waitForContentLoad();
        driver.findElement(By.xpath("//*[@href='http://prestashop-automation.qatestlab.com.ua/ru/2-home']")).click();
     }

    // Check product visibility in the product list
    public void checkVisibility(ProductData newProduct) {
        // if page count more than 1 click "Next" button
        do {
            waitForContentLoad();
            nextPageButton = driver.findElement(By.xpath("//a[@rel='next']"));
            if (nextPageButton.getAttribute("class").toString().equalsIgnoreCase("next js-search-link")) {
                nextPageButton.click();
            }
        } while(nextPageButton.getAttribute("class").toString().equalsIgnoreCase("next js-search-link"));

        CustomReporter.log("Check product visibility on the page");
        locator = "//*[text()='" + newProduct.getName() + "']";
        by = By.xpath(locator);
        Assert.assertTrue(driver.findElements(by).size() > 0, "Product not found.");
        driver.findElement(by).click();
    }

    // Check product name, quantity and price
    public void checkNamePriceQty(ProductData newProduct) {
        waitForContentLoad();

        CustomReporter.log("Check product name validity");
        Assert.assertEquals(driver.findElement(By.xpath("//div/h1")).getText(),newProduct.getName().toUpperCase(),
                "Product names are not equals.");

        CustomReporter.log("Check product price validity");
        String textPrice = driver.findElement(By.xpath("//div[@class='current-price']/span")).getText();
        String objectActual = textPrice.substring(0,textPrice.indexOf(",")+3);
        Assert.assertEquals(objectActual,newProduct.getPrice().toString(),"Product prices are not equals");

        CustomReporter.log("Check product quantity validity");
        String[] nalich = driver.findElement(By.xpath("//div[@class='product-quantities']/span")).getText().split(" ");
        Assert.assertEquals(nalich[0].toString(),newProduct.getQty().toString(),"Product quantity are not equals");
     }


    /**
     * Waits until page loader disappears from the page
     */
    public void waitForContentLoad() {
        // TODO implement generic method to wait until page content is loaded
        wait.until(ExpectedConditions.elementToBeClickable(By.name("submitNewsletter")));
    }
}
