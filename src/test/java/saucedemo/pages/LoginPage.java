package saucedemo.pages;

import org.openqa.selenium.*;

/** Page Object de la pantalla de inicio de sesión. */
public class LoginPage extends BasePage {
    private final By campoUsuario = By.cssSelector("[data-test='username']");
    private final By campoClave   = By.cssSelector("[data-test='password']");
    private final By botonLogin   = By.cssSelector("[data-test='login-button']");

    public LoginPage(WebDriver driver) { super(driver); }

    public LoginPage abrir() {
        driver.get(URL_BASE);
        visible(campoUsuario);
        return this;
    }

    /** Escribe usuario y clave sin enviar el formulario. */
    public LoginPage diligenciar(String usuario, String clave) {
        escribir(campoUsuario, usuario);
        escribir(campoClave, clave);
        return this;
    }

    public InventoryPage enviar() {
        clic(botonLogin);
        return new InventoryPage(driver);
    }

    public InventoryPage iniciarSesion(String usuario, String clave) {
        return diligenciar(usuario, clave).enviar();
    }

    public boolean estaVisible() { return existe(botonLogin); }
}
