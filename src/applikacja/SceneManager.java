package applikacja;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Objects;


public class SceneManager {

    private static Stage stage;
    private static Hashtable<String, String> view = new Hashtable<>();

    public static void addScene(String name, String path) {
        view.put(name, path);
    }

    public static void removeScene(String name) {
        view.remove(name);
    }


    public static void renderScene(String name) {
        String path = "";

        try {
            path = view.get(name);
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getClassLoader().getResource(path)));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            // Umieszczanie każdego nowego załadowanego okana na środku ekranu
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            stage.setX((screenSize.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenSize.getHeight() - stage.getHeight()) / 2);

        } catch (IOException ex) {

            System.out.println("Nie udalo sie załadować scieżki: " + path);
            System.err.println(ex.getMessage());
            System.err.println(ex.getCause().toString());

        } catch (RuntimeException ex) {
            System.err.println("Za długi czas otwierania");
            ex.printStackTrace();
        }
    }

    public static void setStage(Stage _stage) {
        stage = _stage;
    }


}
