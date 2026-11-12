package saucedemo.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

/** Acciones comunes a todas las páginas: esperas explícitas, escribir, hacer clic, leer texto. */
public abstract class BasePage {
    protected static final String URL_BASE = "https://www.saucedemo.com";
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    // Elementos presentes en todas las páginas internas
    private final By titulo       = By.cssSelector("[data-test='title']");
    private final By mensajeError = By.cssSelector("[data-test='error']");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement visible(By localizador) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
    }

    protected void clic(By localizador) {
        wait.until(ExpectedConditions.elementToBeClickable(localizador)).click();
    }

    protected void escribir(By localizador, String texto) {
        WebElement campo = visible(localizador);
        campo.clear();
        campo.sendKeys(texto);
    }

    protected boolean existe(By localizador) {
        return !driver.findElements(localizador).isEmpty();
    }

    public String titulo()       { return visible(titulo).getText(); }
    public String mensajeError() { return visible(mensajeError).getText(); }
    public String urlActual()    { return driver.getCurrentUrl(); }
}
