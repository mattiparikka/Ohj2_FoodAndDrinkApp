/**
 * 
 */
package ruokajuoma.fx;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ruokajajuoma.Juomatieto;

/**
 * Controller luokka juoman lisäämiseen
 * @author Matti Parikka
 * @version 0.9, 10 May 2019
 *
 */
public class LisaaJuomaController implements ModalControllerInterface<Juomatieto>,Initializable {

    
    @FXML private Label labelVirhe;
    @FXML private TextField editNimi;
    @FXML private TextField editPaikka;
    @FXML private TextField editHinta;

    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        // TODO Auto-generated method stub
        // Katso harrastuksien tietojen täyttöön mallia luento 21 edit-taulukosta, noin 1h kohdalta
        
        
    }
    
    
    @FXML private void handleOk() {
        /* Jos jostain syystä ruoan nimi voisi olla null. Alustetaan kuitenkin aina uusi juoma nimi = "";
        if (juomatietoKohdalla == null) return;
        if (juomatietoKohdalla.getNimi() == null || juomatietoKohdalla.getNimi().trim().contentEquals("")) {
            naytaVirhe("Ruoan nimi ei voi olla tyhjä");
            return;
            // TODO: tarkista miksi nimi voisi joskus olla null???
        }
        ModalController.closeStage(labelVirhe);
        */
        
        if (juomatietoKohdalla != null && juomatietoKohdalla.getNimi().trim().contentEquals("")) {
            naytaVirhe("Juoman nimi ei voi olla tyhjä");
            return;
        }
        if (juomatietoKohdalla.getTempPaikka().trim().contentEquals("")) {
            naytaVirhe("Ostopaikan nimi ei voi olla tyhjä");
            return;
        }
        if (labelVirhe.getText().contentEquals("")) ModalController.closeStage(labelVirhe); // päästetään käyttäjä pois ikkunasta, jos ei ole virheitä
        return; // muuten palataan odottamaan virheiden korjausta      
    }
    

    @FXML private void handleCancel() {
        juomatietoKohdalla = null; // palautetaan null, jolloin ei päädytä käyttämään kloonia.
        ModalController.closeStage(labelVirhe);
    }
    
    
    // ========================================================================
    
    private Juomatieto juomatietoKohdalla;
    
    // vanha controller: fi.jyu.mit.fxgui.ModalController
    
    /**
     * @param modalityStage mille stagelle ollaan modaalisia, null = sovellus
     * @param oletus minkä olion dataa näytetään oletuksena
     * @return null, jos painetaan cancel, muuten tietue
     */
    public static Juomatieto kysyJuomatieto(Stage modalityStage, Juomatieto oletus) {
        return ModalController.showModal(
                LisaaJuomaController.class.getResource("LisaaJuomaView.fxml"),
                "JuomatietoContoller testia varten",
                modalityStage, oletus, null
                );
    }
    
    
    /**
     * Alustetaan ja aletaan kuuntelemaan jos käyttäjä tekee muutoksia
     */
    private void alusta() {
        
        editNimi.setOnKeyReleased( e -> kasitteleMuutosNimeen((TextField)e.getSource()));
        editPaikka.setOnKeyReleased( e -> kasitteleMuutosPaikkaan((TextField)e.getSource()));
        editHinta.setOnKeyReleased( e -> kasitteleMuutosHintaan((TextField)e.getSource()));
    }
    
    
    /**
     * Käsitellään muutos nimeen. Virhekäsittely koodissa mukana, mutta nimi ei voi loogisesti olla virheellinen.
     * @param edit muutettava kenttä
     */
    private void kasitteleMuutosNimeen(TextField edit) {
        if (juomatietoKohdalla == null ) return;
        String s = edit.getText();
        String virhe = null;
        virhe = juomatietoKohdalla.setNimi(s);

        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            //edit.getStyleClass().removeAll("virhe"); TODO: ei vielä tyylitiedostoa
            naytaVirhe(virhe);
        }
        else {
            Dialogs.setToolTipText(edit, virhe);
            //edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }  
    }
    
    
    /**
     * Käsitellään muutos hintaan. Virheentarkistus, jos syötetään jotain muuta, kuin lukuarvo.
     * @param edit muutettava kenttä
     */
    //TODO: voisi korvata edes switch casella....
    private void kasitteleMuutosHintaan(TextField edit) {
        if (juomatietoKohdalla == null ) return;
        String s = edit.getText();
        String virhe = null;
        virhe = juomatietoKohdalla.aseta(s);
        
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            //edit.getStyleClass().removeAll("virhe"); TODO: ei vielä tyylitiedostoa
            naytaVirhe(virhe);
        }
        else {
            Dialogs.setToolTipText(edit, virhe);
            //edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }  
    }
    
    
    /**
     * Käsitellään muutos paikkaan.
     * @param edit muutettava kenttä
     */
    //TODO: voisi korvata edes switch casella....
    private void kasitteleMuutosPaikkaan(TextField edit) {
        if (juomatietoKohdalla == null ) return;
        String s = edit.getText();
        String virhe = null;
        virhe = juomatietoKohdalla.setTempPaikka(s); 
        //TODO: ei toimi, jos mitään ei muuteta, kaatuu ruokajuomassa null
        
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            //edit.getStyleClass().removeAll("virhe"); TODO: ei vielä tyylitiedostoa
            naytaVirhe(virhe);
        }
        else {
            Dialogs.setToolTipText(edit, virhe);
            //edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }  
    }
    
    
    @Override
    public Juomatieto getResult() {
        return juomatietoKohdalla;
    }


    @Override
    public void handleShown() {
        editNimi.requestFocus(); // ei tarvitse klikata, että teksti on valittuna nimeä varten.
        
    }
    
        
    @Override
    public void setDefault(Juomatieto oletus) {
        juomatietoKohdalla = oletus;
        naytaJuomatieto(juomatietoKohdalla);        
    }
    
    
    /**
     * Haetaan juoman tiedot tekstikenttiin
     * @param juomatieto naytettava juomatieto
     */
    public void naytaJuomatieto(Juomatieto juomatieto) {
        if (juomatieto == null) return;
        editNimi.setText(juomatieto.getNimi());
        editPaikka.setText(juomatieto.getTempPaikka());
        editHinta.setText(juomatieto.getHinta());
    }
    
    
    /**
     * Virheenkäsittely, jos on syötetty väärässä muodossa
     * @param virhe virheteksti 
     */
    public void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            //labelVirhe.getStyleClass().removeAll("virhe"); vaatii tyylitiedoston
            //TODO: tähän voisi sitten stylejä käyttää
            return;
        }
        labelVirhe.setText(virhe);
        //labelVirhe.getStyleClass().add("virhe"); vaatii tyylitiedoston käyttöä
        // TODO: ja tähän voisi sitten punataustaa stylellä laittaa.       
    }
    

}
