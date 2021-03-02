import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BildbearbeitungGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
       try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Test.fxml"));
        VBox root = (VBox) loader.load();
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    } 
       catch(Exception e)    {
        System.out.println(e);
        e.printStackTrace();
    }
    }
    public static void main(String[] args) {
      launch(args);
    }
}
