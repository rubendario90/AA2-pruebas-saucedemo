package saucedemo.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

/** Page Object del catálogo de productos. */
public class InventoryPage extends BasePage {
    private final By productos      = By.cssSelector("[data-test='inventory-item']");
    private final By nombres        = By.cssSelector("[data-test='inventory-item-name']");
    private final By precios        = By.cssSelector("[data-test='inventory-item-price']");
    private final By selectOrden    = By.cssSelector("[data-test='product-sort-container']");
    private final By contadorCarrito = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By enlaceCarrito  = By.cssSelector("[data-test='shopping-cart-link']");
    private final By botonMenu      = By.id("react-burger-menu-btn");
    private final By enlaceLogout   = By.cssSelector("[data-test='logout-sidebar-link']");

    public InventoryPage(WebDriver driver) { super(driver); }

    public int cantidadProductos() {
        visible(productos);
        return driver.findElements(productos).size();
    }

    public List<String> nombresProductos() {
        visible(nombres);
        return driver.findElements(nombres).stream().map(WebElement::getText).toList();
    }

    public List<Double> preciosProductos() {
        visible(precios);
        return driver.findElements(precios).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    /** Ordena el catálogo. Valores: az, za, lohi, hilo. */
    public InventoryPage ordenarPor(String valor) {
        new Select(visible(selectOrden)).selectByValue(valor);
        return this;
    }

    /** Busca el producto por su nombre visible y pulsa su botón "Add to cart". */
    public InventoryPage agregarAlCarrito(String nombreProducto) {
        botonDelProducto(nombreProducto, "add-to-cart").click();
        return this;
    }

    public InventoryPage quitarDelCarrito(String nombreProducto) {
        botonDelProducto(nombreProducto, "remove").click();
        return this;
    }

    /** Número que muestra el ícono del carrito; 0 si no hay contador. */
    public int contadorCarrito() {
        return existe(contadorCarrito)
                ? Integer.parseInt(driver.findElement(contadorCarrito).getText())
                : 0;
    }

    public CartPage irAlCarrito() {
        clic(enlaceCarrito);
        return new CartPage(driver);
    }

    public LoginPage cerrarSesion() {
        clic(botonMenu);
        clic(enlaceLogout);
        return new LoginPage(driver);
    }

    private WebElement botonDelProducto(String nombreProducto, String prefijoBoton) {
        visible(productos);
        return driver.findElements(productos).stream()
                .filter(p -> p.findElement(nombres).getText().equals(nombreProducto))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No existe el producto: " + nombreProducto))
                .findElement(By.cssSelector("button[data-test^='" + prefijoBoton + "']"));
    }
}
