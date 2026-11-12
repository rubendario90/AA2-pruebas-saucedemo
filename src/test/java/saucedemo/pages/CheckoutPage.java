package saucedemo.pages;

import org.openqa.selenium.*;

/** Page Object del proceso de pago: datos del comprador, resumen y confirmación. */
public class CheckoutPage extends BasePage {
    // Paso 1: información del comprador
    private final By campoNombre    = By.cssSelector("[data-test='firstName']");
    private final By campoApellido  = By.cssSelector("[data-test='lastName']");
    private final By campoPostal    = By.cssSelector("[data-test='postalCode']");
    private final By botonContinuar = By.cssSelector("[data-test='continue']");
    // Paso 2: resumen de la orden
    private final By subtotal       = By.cssSelector("[data-test='subtotal-label']");
    private final By impuesto       = By.cssSelector("[data-test='tax-label']");
    private final By total          = By.cssSelector("[data-test='total-label']");
    private final By botonFinalizar = By.cssSelector("[data-test='finish']");
    // Paso 3: confirmación
    private final By mensajeFinal   = By.cssSelector("[data-test='complete-header']");

    public CheckoutPage(WebDriver driver) { super(driver); }

    public CheckoutPage diligenciarDatos(String nombre, String apellido, String postal) {
        escribir(campoNombre, nombre);
        escribir(campoApellido, apellido);
        escribir(campoPostal, postal);
        return this;
    }

    public CheckoutPage continuar() {
        clic(botonContinuar);
        return this;
    }

    public double subtotal() { return montoDe(subtotal); }
    public double impuesto() { return montoDe(impuesto); }
    public double total()    { return montoDe(total); }

    public CheckoutPage finalizar() {
        clic(botonFinalizar);
        return this;
    }

    public String mensajeConfirmacion() { return visible(mensajeFinal).getText(); }

    /** Convierte un texto como "Item total: $39.98" en el número 39.98. */
    private double montoDe(By localizador) {
        String texto = visible(localizador).getText();
        return Double.parseDouble(texto.substring(texto.indexOf('$') + 1));
    }
}
