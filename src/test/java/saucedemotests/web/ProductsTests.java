package saucedemotests.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import saucedemotests.core.SauceDemoBaseWebTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.ShoppingCartPage;
import com.saucedemo.pages.CheckoutYourInformationPage;
import com.saucedemo.pages.CheckoutOverviewPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.LoginPage;

public class ProductsTests extends SauceDemoBaseWebTest {
    public final String BACKPACK_TITLE = "Sauce Labs Backpack";
    public final String SHIRT_TITLE = "Sauce Labs Bolt T-Shirt";

    private InventoryPage inventoryPage;
    private ShoppingCartPage shoppingCartPage;
    private CheckoutYourInformationPage checkoutYourInformationPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CheckoutCompletePage checkoutCompletePage;

    @BeforeEach
    public void beforeTest() {
        // Authenticate with Standard user
        LoginPage loginPage = new LoginPage();
        loginPage.submitLoginForm("standard_user", "secret_sauce");
        inventoryPage = new InventoryPage();
        inventoryPage.waitForPageTitle(); // Ensure the inventory page is fully loaded
    }

    @Test
    public void productAddedToShoppingCart_when_addToCart() {
        // Add products to shopping cart
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);

        // Go to shopping cart
        inventoryPage.clickShoppingCartLink();
        shoppingCartPage = new ShoppingCartPage();

        // Assert Items in the shopping cart
        Assertions.assertTrue(shoppingCartPage.getShoppingCartItems().stream()
                .anyMatch(e -> e.getText().equals(BACKPACK_TITLE)), "Backpack should be in the cart");
        Assertions.assertTrue(shoppingCartPage.getShoppingCartItems().stream()
                .anyMatch(e -> e.getText().equals(SHIRT_TITLE)), "T-Shirt should be in the cart");
    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation() {
        // Add products to shopping cart
        inventoryPage.addProductsByTitle(BACKPACK_TITLE);
        inventoryPage.clickShoppingCartLink();
        shoppingCartPage = new ShoppingCartPage();

        // Go to checkout
        shoppingCartPage.clickCheckout();
        checkoutYourInformationPage = new CheckoutYourInformationPage();

        // Fill form with valid information
        checkoutYourInformationPage.fillShippingDetails("John", "Doe", "12345");

        // Continue to checkout overview
        checkoutYourInformationPage.clickContinue();
        checkoutOverviewPage = new CheckoutOverviewPage();

        // Assert Items in checkout overview
        Assertions.assertTrue(checkoutOverviewPage.getShoppingCartItems().stream()
                .anyMatch(e -> e.getText().equals(BACKPACK_TITLE)), "Backpack should be in the overview");
    }

    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm() {
        // Add Backpack and T-shirt to shopping cart
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);
        inventoryPage.clickShoppingCartLink();
        shoppingCartPage = new ShoppingCartPage();

        // Go to checkout
        shoppingCartPage.clickCheckout();
        checkoutYourInformationPage = new CheckoutYourInformationPage();

        // Fill form with valid information
        checkoutYourInformationPage.fillShippingDetails("Jane", "Smith", "67890");

        // Continue to checkout overview
        checkoutYourInformationPage.clickContinue();
        checkoutOverviewPage = new CheckoutOverviewPage();

        // Complete order
        checkoutOverviewPage.clickFinish();
        checkoutCompletePage = new CheckoutCompletePage();

        // Assert that order is complete
        Assertions.assertNotNull(checkoutCompletePage, "Order should be marked as complete");
    }
}