package saucedemo.pages;

import org.openqa.selenium.*;
import java.util.List;

/** Page Object del carrito de compras. */
public class CartPage extends BasePage {
    private final By nombres       = By.cssSelector("[data-test='inventory-item-name']");
    private final By botonCheckout = By.cssSelector("[data-test='checkout']");

    public CartPage(WebDriver driver) { super(driver); }

    public List<String> productosEnCarrito() {
        titulo();   // espera a que cargue la página
        return driver.findElements(nombres).stream().map(WebElement::getText).toList();
    }

    public CheckoutPage irAlCheckout() {
        clic(botonCheckout);
        return new CheckoutPage(driver);
    }
}
