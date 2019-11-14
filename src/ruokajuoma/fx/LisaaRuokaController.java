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
import ruokajajuoma.Ruoka;

/**
 * Controller luokka ruoan lisäämiseen
 * @author Matti Parikka
 * @version 0.9, 10 May 2019
 *
 */
public class LisaaRuokaController implements ModalControllerInterface<Ruoka>,Initializable {

    
    @FXML private Label labelVirhe;
    @FXML private TextField editNimi;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        // TODO Auto-generated method stub
        // Katso harrastuksien tietojen täyttöön mallia luento 21 edit-taulukosta, noin 1h kohdalta
        
        
    }
    
    
    @FXML private void handleOk() {
        if (ruokaKohdalla != null && ruokaKohdalla.getNimi().trim().contentEquals("")) {
            naytaVirhe("Ruoan nimi ei voi olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }
    

    @FXML private void handleCancel() {
        ruokaKohdalla = null; // palautetaan null, jolloin ei päädytä käyttämään kloonia.
        ModalController.closeStage(labelVirhe);
    }
    
    
    // ========================================================================
    
    private Ruoka ruokaKohdalla;
    
    // vanha controller: fi.jyu.mit.fxgui.ModalController
    
    /**
     * @param modalityStage mille stagelle ollaan modaalisia, null = sovellus
     * @param oletus minkä olion dataa näytetään oletuksena
     * @return null, jos painetaan cancel, muuten tietue
     */
    public static Ruoka kysyRuoka(Stage modalityStage, Ruoka oletus) {
        return ModalController.showModal(
                LisaaRuokaController.class.getResource("LisaaRuokaView.fxml"),
                "RuokaContoller testia varten",
                modalityStage, oletus, null
                );
    }
    
    
    /**
     * Alustetaan ja aletaan kuuntelemaan jos käyttäjä tekee muutoksia
     */
    private void alusta() {
        editNimi.setOnKeyReleased( e -> kasitteleMuutosRuokaan((TextField)e.getSource()));
    }


    @Override
    public Ruoka getResult() {
        return ruokaKohdalla;
    }


    @Override
    public void handleShown() {
        editNimi.requestFocus(); // ei tarvitse klikata, että teksti on valittuna nimeä varten.
        
    }
    
    
    /**
     * Käsitellään muutos nimeen. Virhekäsittely koodissa mukana, mutta nimi ei voi loogisesti olla virheellinen.
     * @param edit muutettava kenttä
     */
    private void kasitteleMuutosRuokaan(TextField edit) {
        if (ruokaKohdalla == null ) return;
        String s = edit.getText();
        String virhe = null;
        virhe = ruokaKohdalla.setNimi(s);
        
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
    public void setDefault(Ruoka oletus) {
        ruokaKohdalla = oletus;
        naytaRuoka(ruokaKohdalla);
        
    }
    
    
    /**
     * @param ruoka naytettava ruoka
     */
    public void naytaRuoka(Ruoka ruoka) {
        if (ruoka == null) return;
        editNimi.setText(ruoka.getNimi());
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
