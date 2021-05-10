package applikacja;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dialogs {

    public static Optional<ButtonType> confirmLogOutDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Czy na pewno wylogować?");
        ImageView icon = new ImageView("icons/kitchen/loginDialog.png");
        icon.setPreserveRatio(true);
        icon.setFitHeight(50);

        alert.setGraphic(icon);
        return alert.showAndWait();
    }

    public static String changeFoodTypeDialog() {
        List<String> choices = new ArrayList<>();

        choices.add("Przystawka");
        choices.add("Zupa");
        choices.add("Danie Główne");
        choices.add("Deser");
        choices.add("Napój");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);

        dialog.setTitle("Wybierz Typ Dania");
        dialog.setHeaderText("Wybierz nowy typ dania:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static void errorInRemoving(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wystąpił błąd");
        alert.setHeaderText("Nie można wykonać operacji");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

}
