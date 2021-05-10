package controller.hall;

import applikacja.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class currentOrderController {

    public Button backButton;

    public void backAction(ActionEvent actionEvent) {

        SceneManager.renderScene("hall");
    }
}
