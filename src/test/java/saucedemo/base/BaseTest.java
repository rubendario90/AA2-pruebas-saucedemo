package saucedemo.base;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import saucedemo.pages.InventoryPage;
import saucedemo.pages.LoginPage;
import java.nio.file.*;
import java.util.Map;

/**
 * Configuración común de todas las pruebas:
 * abre Chrome antes de cada prueba, lo cierra después y guarda evidencias.
 */
public abstract class BaseTest {
    protected static final String USUARIO_VALIDO = "standard_user";
    protected static final String CLAVE_VALIDA   = "secret_sauce";

    protected WebDriver driver;
    private Path carpetaEvidencias;

    @BeforeEach
    void iniciarNavegador(TestInfo info) {
        ChromeOptions opciones = new ChromeOptions();
        // Evita la ventana de Chrome "cambia tu contraseña", que tapa la página
        opciones.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        opciones.addArguments("--disable-features=PasswordLeakDetection", "--window-size=1366,900");
        // mvn test -Dheadless=true  ejecuta sin abrir la ventana del navegador
        if (Boolean.getBoolean("headless")) opciones.addArguments("--headless=new");

        driver = new ChromeDriver(opciones);   // Selenium Manager gestiona el driver
        carpetaEvidencias = Paths.get("evidencias",
                info.getTestClass().map(Class::getSimpleName).orElse("General"),
                limpiar(info.getDisplayName()));
    }

    @AfterEach
    void cerrarNavegador() {
        tomarEvidencia("resultado_final");
        if (driver != null) driver.quit();
    }

    /** Atajo reutilizable: abre SauceDemo e inicia sesión con el usuario válido. */
    protected InventoryPage iniciarSesionValida() {
        return new LoginPage(driver).abrir().iniciarSesion(USUARIO_VALIDO, CLAVE_VALIDA);
    }

    /** Guarda una captura del navegador con el nombre del paso indicado. */
    protected void tomarEvidencia(String paso) {
        try {
            Files.createDirectories(carpetaEvidencias);
            Path destino = carpetaEvidencias.resolve(limpiar(paso) + ".png");
            Path origen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.err.println("No se pudo guardar la captura: " + e.getMessage());
        }
    }

    private static String limpiar(String texto) {
        return texto.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
