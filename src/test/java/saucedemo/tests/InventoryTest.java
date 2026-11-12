package saucedemo.tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import saucedemo.base.BaseTest;
import saucedemo.pages.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Módulo 2 - Catálogo de productos y carrito")
class InventoryTest extends BaseTest {

    @Test
    @DisplayName("CP-04 El catálogo muestra los 6 productos")
    void catalogoMuestraSeisProductos() {
        InventoryPage catalogo = iniciarSesionValida();
        assertEquals(6, catalogo.cantidadProductos());
    }

    @ParameterizedTest(name = "CP-05 Ordenar por nombre ''{0}''")
    @CsvSource({"az, false", "za, true"})
    @DisplayName("CP-05 Ordenar por nombre")
    void ordenarPorNombre(String opcion, boolean descendente) {
        InventoryPage catalogo = iniciarSesionValida().ordenarPor(opcion);

        List<String> obtenidos = catalogo.nombresProductos();
        List<String> esperados = new ArrayList<>(obtenidos);
        esperados.sort(descendente ? Comparator.reverseOrder() : Comparator.naturalOrder());
        assertEquals(esperados, obtenidos, "Los productos no están ordenados por nombre");
    }

    @ParameterizedTest(name = "CP-06 Ordenar por precio ''{0}''")
    @CsvSource({"lohi, false", "hilo, true"})
    @DisplayName("CP-06 Ordenar por precio")
    void ordenarPorPrecio(String opcion, boolean descendente) {
        InventoryPage catalogo = iniciarSesionValida().ordenarPor(opcion);

        List<Double> obtenidos = catalogo.preciosProductos();
        List<Double> esperados = new ArrayList<>(obtenidos);
        esperados.sort(descendente ? Comparator.reverseOrder() : Comparator.naturalOrder());
        assertEquals(esperados, obtenidos, "Los productos no están ordenados por precio");
    }

    @Test
    @DisplayName("CP-07 Agregar y quitar productos actualiza el contador del carrito")
    void agregarYQuitarProductos() {
        InventoryPage catalogo = iniciarSesionValida()
                .agregarAlCarrito("Sauce Labs Backpack")
                .agregarAlCarrito("Sauce Labs Bike Light");
        tomarEvidencia("1_dos_productos_agregados");
        assertEquals(2, catalogo.contadorCarrito());

        catalogo.quitarDelCarrito("Sauce Labs Backpack");
        assertEquals(1, catalogo.contadorCarrito());

        catalogo.quitarDelCarrito("Sauce Labs Bike Light");
        assertEquals(0, catalogo.contadorCarrito(), "El contador debería desaparecer");
    }

    @Test
    @DisplayName("CP-08 El carrito contiene los productos agregados")
    void carritoContieneProductos() {
        CartPage carrito = iniciarSesionValida()
                .agregarAlCarrito("Sauce Labs Backpack")
                .agregarAlCarrito("Sauce Labs Fleece Jacket")
                .irAlCarrito();

        assertEquals(List.of("Sauce Labs Backpack", "Sauce Labs Fleece Jacket"),
                carrito.productosEnCarrito());
    }
}
