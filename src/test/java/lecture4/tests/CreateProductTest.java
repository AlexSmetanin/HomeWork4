package lecture4.tests;

import lecture4.BaseTest;
import lecture4.model.ProductData;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {
    private ProductData newProduct;

    @DataProvider
    public Object[][] loginData() {
        return new Object[][]{
            {"webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw"},
        };
    }

    @Test(dataProvider = "loginData")
    public void createNewProduct(String login, String password) {
        // TODO implement test for product creation
        actions.login(login, password);
        actions.selectOrdersItem();
        newProduct = ProductData.generate();
        actions.createProduct(newProduct);
    }

    @Test(dependsOnMethods={"createNewProduct"})
    public void checkProductVisibility() {
        // TODO implement logic to check product visibility on website
        actions.openStorePage();
        actions.checkVisibility(newProduct);
        actions.checkNamePriceQty(newProduct);
    }

}
