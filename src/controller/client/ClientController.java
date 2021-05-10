package controller.client;

import applikacja.Main;
import applikacja.SceneManager;

/**
 * ClientController
 *
 * Klasa kontrolera obsługująca panel klienta.
 *
 * @author Katarzyna Śliwa
 */

public class ClientController {

    /**
     * Metoda renderująca panel logowania.
     */
    public void returnOnAction() {
        SceneManager.renderScene("login");
    }

    /**
     * Metoda renderująca panel menu.
     */
    public void menuOnAction() {
        SceneManager.renderScene("menu");
    }

    /**
     * Metoda renderująca panel sali.
     */
    public void roomOnAction() {
        SceneManager.renderScene("room");
    }

    /**
     * Metoda inicjalizująca, ustawiająca zmienną logged, gdy jesteśmy na panelu klienta
     */
    public void initialize() {
        Main.logged = "klient";
    }
}
