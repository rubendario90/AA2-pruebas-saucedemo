package saucedemo.tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import saucedemo.base.BaseTest;
import saucedemo.pages.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Módulo 3 - Proceso de compra (checkout)")
class CheckoutTest extends BaseTest {

    @Test
    @DisplayName("CP-09 Compra completa de principio a fin")
    void compraCompleta() {
        CheckoutPage checkout = iniciarSesionValida()
                .agregarAlCarrito("Sauce Labs Backpack")      // $29.99
                .agregarAlCarrito("Sauce Labs Bike Light")    // $9.99
                .irAlCarrito()
                .irAlCheckout()
                .diligenciarDatos("Ana", "Pérez", "110111");
        tomarEvidencia("1_datos_comprador");

        checkout.continuar();
        tomarEvidencia("2_resumen_orden");
        assertAll("Resumen de la orden",
            () -> assertEquals(39.98, checkout.subtotal(), 0.001, "Subtotal incorrecto"),
            () -> assertEquals(checkout.subtotal() + checkout.impuesto(), checkout.total(), 0.01,
                    "El total no es subtotal + impuesto")
        );

        checkout.finalizar();
        assertEquals("Thank you for your order!", checkout.mensajeConfirmacion());
    }

    @ParameterizedTest(name = "CP-10.{index} nombre=''{0}'' apellido=''{1}'' postal=''{2}''")
    @CsvFileSource(resources = "/datos_checkout_invalido.csv", numLinesToSkip = 1)
    @DisplayName("CP-10 Checkout con datos incompletos muestra el error correcto")
    void checkoutDatosIncompletos(String nombre, String apellido, String postal, String errorEsperado) {
        CheckoutPage checkout = iniciarSesionValida()
                .agregarAlCarrito("Sauce Labs Onesie")
                .irAlCarrito()
                .irAlCheckout()
                .diligenciarDatos(vacioSiNulo(nombre), vacioSiNulo(apellido), vacioSiNulo(postal))
                .continuar();

        assertEquals(errorEsperado, checkout.mensajeError());
    }

    /** En el CSV una celda vacía llega como null; el formulario necesita texto vacío. */
    private static String vacioSiNulo(String valor) {
        return valor == null ? "" : valor;
    }
}
