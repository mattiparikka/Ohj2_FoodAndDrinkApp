package ruokajuoma.fx;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import ruokajajuoma.*;

/**
 * Pääohjelman luokka, josta ohjelma laitetaan käyntiin. Asettaa "näyttämön" ja tyylitiedoston. Suljettaessa varmistetaan voiko sulkea eli
 * onko tiedot tallennettu.
 * @author Matti Parikka
 * @version 0.9, 3 May 2019
 *
 */
public class RuokajuomaMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("RuokajuomaGUIView.fxml"));
            final Pane root = ldr.load();
            final RuokajuomaGUIController ruokajuomaCtrl = (RuokajuomaGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("ruokajuoma.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ruokajuoma");

            Ruokajuoma ruokajuoma = new Ruokajuoma();
            ruokajuoma.lueTiedostosta();
            
            
            ruokajuomaCtrl.setRuokajuoma(ruokajuoma);
            
            primaryStage.setOnCloseRequest((event) -> {
                if ( !ruokajuomaCtrl.voikoSulkea() ) event.consume();
            });
            
            primaryStage.show();
            
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}