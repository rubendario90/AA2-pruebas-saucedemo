package saucedemo.tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import saucedemo.base.BaseTest;
import saucedemo.pages.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Módulo 1 - Inicio y cierre de sesión")
class LoginTest extends BaseTest {

    @Test
    @DisplayName("CP-01 Login con usuario válido muestra el catálogo")
    void loginValido() {
        LoginPage login = new LoginPage(driver).abrir().diligenciar(USUARIO_VALIDO, CLAVE_VALIDA);
        tomarEvidencia("1_formulario_diligenciado");
        InventoryPage catalogo = login.enviar();

        assertAll(
            () -> assertTrue(catalogo.urlActual().endsWith("/inventory.html"), "No llegó al catálogo"),
            () -> assertEquals("Products", catalogo.titulo())
        );
    }

    @ParameterizedTest(name = "CP-02.{index} usuario=''{0}'' clave=''{1}''")
    @CsvSource({
        // El mensaje lleva una coma, por eso va entre comillas simples
        "locked_out_user, secret_sauce,   'Epic sadface: Sorry, this user has been locked out.'",
        "standard_user,   claveMala,      Epic sadface: Username and password do not match any user in this service",
        "'',              secret_sauce,   Epic sadface: Username is required",
        "standard_user,   '',             Epic sadface: Password is required"
    })
    @DisplayName("CP-02 Login inválido muestra el mensaje de error correcto")
    void loginInvalido(String usuario, String clave, String mensajeEsperado) {
        LoginPage login = new LoginPage(driver).abrir().diligenciar(usuario, clave);
        tomarEvidencia("1_formulario_diligenciado");
        login.enviar();

        assertEquals(mensajeEsperado, login.mensajeError());
        assertTrue(login.estaVisible(), "Debería seguir en la pantalla de login");
    }

    @Test
    @DisplayName("CP-03 Cerrar sesión regresa a la pantalla de login")
    void cerrarSesion() {
        InventoryPage catalogo = iniciarSesionValida();
        LoginPage login = catalogo.cerrarSesion();

        assertTrue(login.estaVisible(), "No regresó a la pantalla de login");
    }
}
