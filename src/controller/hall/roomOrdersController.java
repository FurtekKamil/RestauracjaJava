package controller.hall;

import applikacja.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class roomOrdersController {

    public void reserveOnClicked(MouseEvent mouseEvent) {
        //System.out.println(mouseEvent.getPickResult().getIntersectedNode().getId());
        MakeOrderController.id_stolika=Integer.parseInt(mouseEvent.getPickResult().getIntersectedNode().getId().substring(1));
        SceneManager.renderScene("makeOrder");
    }

    public void returnReserve(ActionEvent actionEvent) {
        SceneManager.renderScene("hall");
    }
}
