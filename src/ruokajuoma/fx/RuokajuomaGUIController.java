package ruokajuoma.fx;




import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.Initializable;

import java.util.ArrayList;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
//import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
//import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import ruokajajuoma.*;


    
/**
 * Luokka käyttöliittymän tapahtumien käsittelyyn
 * 
 * @author Matti Parikka
 * @version 0.9, 10 May 2019
 *
 */
public class RuokajuomaGUIController implements Initializable {

     @FXML private ComboBoxChooser<String> ehdot;
     @FXML private TextField hakuehto;
     @FXML private TextField reseptiKentta;
     @FXML private ListChooser<Ruoka> ruokaChooser;    
     @FXML private ScrollPane panelJuomat; //tilapäisnimi vaiheen 5 tietojen näyttämisen vuoksi.
     @FXML private TextField editNimi;
     @FXML private StringGrid<Juomatieto> tableJuomat;
     
     
     @FXML private Label mainVirhe;
     @FXML private TextField juomaHaku;
     @FXML private ListChooser<Juomatieto> juomaChooser;
     
     @Override
     public void initialize(URL url, ResourceBundle bundle) {
         alusta();
     }
     
     
     @FXML void handleApua() {
         avustus();
         //Dialogs.showMessageDialog("Vielä ei osata näyttää apua");
     }

     
     @FXML void handleHakuehto() {
         /*
         //String hakukentta = ehdot.getSelectedText();
         String ehto = hakuehto.getText(); 
         if ( ehto.isEmpty() )
             //naytaVirhe(null); Tehtava vastaava aliohjelma
             Dialogs.showMessageDialog("Vielä ei osata hakea mitään");
         else
             //naytaVirhe("Ei osata vielä hakea " + hakukentta + ": " + ehto);
             Dialogs.showMessageDialog("Vielä ei osata hakea mitään");
         */
         hae(0);
     }
     
     @FXML void handleJuomaHaku(){
         haeJuomat(0);
     }

     
     @FXML void handleLisaaJuoma() {
         lisaaJuoma();
         //Dialogs.showMessageDialog("Vielä ei osata lisätä juomaa");
         //ModalController.showModal(RuokajuomaGUIController.class.getResource("LisaaJuomaView.fxml"), "Juoma", null, "");
         
     }
     
     
     @FXML void handleLisaaUusiJuoma() {
         uusiJuoma();
     }

     
     @FXML void handleLisaaRuoka() {
         //Dialogs.showMessageDialog("Vielä ei osata lisätä ruokaa");
         //ModalController.showModal(RuokajuomaGUIController.class.getResource("LisaaRuokaView.fxml"), "Ruoka", null, "");
         uusiRuoka();
     }
     
     
     @FXML void handleMuokkaaRuoka() {
         // lisätään lopussa, jotta voidaan muokata jälkikäteen ruoan nimeä
         muokkaa();
     }
     
     
     @FXML void handleMuokkaaJuoma() {
         muokkaaJuoma();
         
         // lisätään lopussa, jotta voidaan muokata jälkikäteen ruoan nimeä
     }
     
     @FXML void handleMuokkaaPaikka() {
         // lisätään lopussa, jotta voidaan muokata jälkikäteen ruoan nimeä
     }

     
     @FXML void handleLopeta() {
            tallenna();
            Platform.exit();
     }

     
     @FXML void handlePoistaJuoma() {
         poistaJuoma();
         //Dialogs.showMessageDialog("Vielä ei osata poistaa juomaa");
     }

     
     @FXML void handlePoistaJuomaKaikilta() {
         poistaJuomaListalta();
         //Dialogs.showMessageDialog("Vielä ei osata poistaa juomaa kaikilta");
     }  

     
     @FXML void handlePoistaRuoka() {
         poistaRuoka();
         //Dialogs.showMessageDialog("Vielä ei osata poistaa ruokaa");
     }

     
     @FXML void handleTallenna() {
         tallenna();
     }
     

     @FXML void handleTietoja() {
         //Dialogs.showMessageDialog("Ei osata vielä tietoja");
         ModalController.showModal(RuokajuomaGUIController.class.getResource("AboutView.fxml"), "Ruokajuoma", null, "");
     }
     



     //============================================ Ei käyttöliittymän aliohjelmia ======================================
     
     
     private Ruokajuoma ruokajuoma;
     private Ruoka ruokaKohdalla;
     private Juomatieto juomaKohdalla;
     //private TextArea areaJuomat = new TextArea(); // Ei käytetty 5.1, mutta juomien näyttöä varten.
 
     
     /**
      * Alustetaan listChooser. Lisätään tarvittaessa textArea-muokkaus haluttuun kenttään, jotta voidaan testata juomien tietoja.
      */
     private void alusta() {
         ruokaChooser.clear();
         ruokaChooser.addSelectionListener(e -> naytaRuoka());
         juomaChooser.clear();
         juomaChooser.addSelectionListener(e -> valitseJuoma());

         tableJuomat.setColumnWidth(0, 250);
         tableJuomat.setColumnWidth(1, 200);
         tableJuomat.setColumnWidth(2, 50);
         //this.panelJuomat.setContent(areaJuomat); // käytetään näyttämään juomien tietoja vaiheessa 5.
         //tableJuomat.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaJuoma(); } );
     }
     
     
     /**
      * Asetetaan valittu juoma tämän hetkiseksi juomaksi
      */
     private void valitseJuoma() {
         juomaKohdalla = juomaChooser.getSelectedObject();
         if (juomaKohdalla == null) return;
     }
     
     
     /**
      * Näyttää listalta valitun ruoan tiedot reseptikentässä. 
      */
     private void naytaRuoka() {
         ruokaKohdalla = ruokaChooser.getSelectedObject();
         
         if (ruokaKohdalla == null) return;         
         reseptiKentta.setText(ruokaKohdalla.getNimi());         
         naytaJuomat(ruokaKohdalla);
         /*
         areaJuomat.setText("");
         try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaJuomat)) {
             tulosta(os, ruokaKohdalla);
         }
         */
     }
          
     
     /**
      * Haetaan ruokaa vastaavat juomat keskellä olevaan StringGridiin. 
      */
     private void naytaJuomat(Ruoka ruoka) {
         tableJuomat.clear();
         
         if (ruoka == null) return;
         naytaVirhe(null);
         ArrayList<Juomatieto> juomat = ruokajuoma.annaJuomatRuoalle(ruoka);
         if (juomat.size() == 0) return;
         for (Juomatieto juoma : juomat) {
             naytaJuoma(juoma);
         }    
     }
     
     /**
      * Näyttää yhden juoman tiedot StringGridin rivillä.
      */
     private void naytaJuoma(Juomatieto juoma) {
         //eka tyhmä versio
         //String[] rivi = juoma.toString().split("\\|");
         //tableJuomat.add(juoma, rivi[1], rivi[2], rivi[3]);
         
         //toinen, vähän tyhmä versio vielä, mutta ei ole montaa kenttää.
         String[] rivi = new String[3];
         rivi[0] = juoma.getNimi();
         rivi[1] = ruokajuoma.annaOstopaikka(juoma);
         rivi[2] = juoma.getHinta();
         
         tableJuomat.add(juoma, rivi); 
     }
     
     
     /**
      * Lisätään uusi ruoka listalle.
      */
     private void uusiRuoka() {
         Ruoka uusi = new Ruoka();
         uusi = LisaaRuokaController.kysyRuoka(null, uusi);
         if (uusi == null ) return; //ei lisätä mitään, jos käyttäjä on syöttänyt tyhjän nimen tai painanut cancel.
         uusi.rekisteroi(); 
         //uusi.taytaKala(); // TODO: Korvataan myöhemmin dialogivaihtoehdolla, jossa täytetään tiedot.         
         ruokajuoma.lisaa(uusi);
         hae(uusi.getIdNumero());
     }
     
     
     /**
      * Lisätään uusi juoma oikealla olevaan juomien listchooseriin
      */
     private void uusiJuoma() { 
         Juomatieto uusi = new Juomatieto();
         uusi = LisaaJuomaController.kysyJuomatieto(null, uusi);
         if (uusi == null ) return;
         uusi.rekisteroi();
         ruokajuoma.korvaaTaiLisaa(uusi);
         haeJuomat(uusi.getIdNumero());
     }
     
     
     /**
      * Lisätään oikean laidan juomien listchooserissa valittuna oleva juoma vasemman laidan ruokien listchooserissa
      * valittuna olleelle ruoalle. Antaa virheilmoituksen, jos ruokaa tai juomaa ei ole valittu tai jos 
      * kyseinen juoma on jo lisätty ruoalle.
      */
     private void lisaaJuoma() {
         if (ruokaKohdalla == null) {
             naytaVirhe("valitse ruoka ensin");
             return; 
         }
         if (juomaKohdalla == null) {
             naytaVirhe("valitse juoma oikeanpuoleiselta listalta ensin");
             return; 
         } 
         ArrayList<Juomatieto> juomat = ruokajuoma.annaJuomatRuoalle(ruokaKohdalla);
         for (int i = 0; i < juomat.size(); i++) {             
             if (juomat.get(i).getIdNumero() == juomaKohdalla.getIdNumero() ) {
                 naytaVirhe("Juoma on jo lisätty tälle ruoalle");
                 return; //  juoma on jo lisätty + virhekäsittely
             }
         } // muuten jatketaan luomalla uusi ruoka & juoma pari
         naytaVirhe(null);
         Juoma juomapari = new Juoma(ruokaKohdalla.getIdNumero(), juomaKohdalla.getIdNumero());
         ruokajuoma.lisaa(juomapari);
         naytaJuomat(ruokaKohdalla);
     }
     
     
     /**
      * Näyttää virhetekstin ikkunan alalaidassa. Styleclassit ei vielä käytössä.
      * @param virhe virheteksti 
      */
     public void naytaVirhe(String virhe) {
         if ( virhe == null || virhe.isEmpty()) {
             mainVirhe.setText("");
             //labelVirhe.getStyleClass().removeAll("virhe"); vaatii tyylitiedoston
             //TODO: tähän voisi sitten stylejä käyttää
             return;
         }
         mainVirhe.setText(virhe);
         //labelVirhe.getStyleClass().add("virhe"); vaatii tyylitiedoston käyttöä
         // TODO: ja tähän voisi sitten punataustaa stylellä laittaa.
         
     }
     
     
     /**
      * Poistetaan valitulta ruoalta StringGridissä valittuna ollut ruoka. Ei poista juomaa muilta
      * ruoilta tai oikeanpuoleiselta juomalistalta. Päivittää ruokalistan hakutulokset.
      */
     private void poistaJuoma() {
         int rivi = tableJuomat.getRowNr();
         if (rivi < 0 ) return; // ei voida poistaa olemattomalta riviltä
         Juomatieto juomatieto = tableJuomat.getObject();
         if (juomatieto == null) return; // vieläkään ei ole mitään poistettavaa
         if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko juoma \"" + juomatieto.getNimi() + "\" tältä ruoalta?", "Kyllä", "Ei") ) return;
         ruokajuoma.poistaJuoma(juomatieto, ruokaKohdalla);
         naytaJuomat(ruokaKohdalla);
         hae(0);
     }
     

     /**
      * Poistetaan juomalistalla valittuna ollut juoma kaikilta ruoilta ja listalta. Päivittää kaikki listat.
      */
     private void poistaJuomaListalta() {
         Juomatieto juoma = juomaKohdalla;
         if (juoma == null) return;
         if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko juoma \"" + juoma.getNimi() + "\" listalta ja kaikilta ruoilta. ", "Kyllä", "Ei") ) return;
         ruokajuoma.poista(juoma);
         int index = juomaChooser.getSelectedIndex();
         haeJuomat(0);
         hae(0);
         juomaChooser.setSelectedIndex(index);
         naytaJuomat(ruokaKohdalla);
         
     }

     
     /**
      * Poistetaan valittu ruoka ruokalistalta. Ruokajuoma huolehtii, että myös kaikkien juomien linkki
      * tähän ruokaan poistuu.
      */
     private void poistaRuoka() {
         Ruoka ruoka = ruokaKohdalla;
         if (ruoka == null) return;
         if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko ruoka \"" + ruoka.getNimi() + "\" listalta. Poistaminen ei vaikuta muiden ruokien juomiin.", "Kyllä", "Ei") ) return;
         ruokajuoma.poista(ruoka);
         int index = ruokaChooser.getSelectedIndex();
         hae(0);
         ruokaChooser.setSelectedIndex(index);
     }
     
     
     /**
      * Muokataan ruoan tietoja. Muokattavaksi viedään klooni, jolla korvataan alkuperäinen, jos muutoksia tehtiin.
      * Jos ei muutoksia, alkuperäinen muuttamaton jää voimaan.
      */
     private void muokkaa() {       
         if (ruokaKohdalla == null) return;
         try {
         Ruoka ruokaKopio = LisaaRuokaController.kysyRuoka(null, ruokaKohdalla.clone()); //Tämä palauttaa null jos painetaan cancel, muuten viite olioon, joka tässä tapauksessa klooni.
         if (ruokaKopio == null) return; //ruokaKopio = muokatun ruoan olio
         ruokajuoma.korvaaTaiLisaa(ruokaKopio);
         hae(ruokaKopio.getIdNumero()); // päivitetään listChooser.
         } catch (CloneNotSupportedException e) {
             System.err.println("jotain vikaa kloonissa");
         }        
         
     }
     
     
     /**
      * Muokataan juoman tietoja. Muokattavaksi viedään klooni, jolla korvataan alkuperäinen, jos muutoksia tehtiin.
      * Jos ei muutoksia, alkuperäinen muuttamaton jää voimaan.
      */
     private void muokkaaJuoma() {
         juomaKohdalla = juomaChooser.getSelectedObject();
         if (juomaKohdalla == null) return;

         try {
         Juomatieto kopio = LisaaJuomaController.kysyJuomatieto(null, juomaKohdalla.clone()); //Tämä palauttaa null jos painetaan cancel, muuten viite olioon, joka tässä tapauksessa klooni.
         if (kopio == null) return; //ruokaKopio = muokatun ruoan olio
         ruokajuoma.korvaaTaiLisaa(kopio);
         naytaJuomat(ruokaKohdalla);
         haeJuomat(kopio.getIdNumero());
         
         } catch (CloneNotSupportedException e) {
             System.err.println("jotain vikaa kloonissa");
         }
     }
          
     
     /**
      * Haetaan ruoan tiedot listChooseriin.
      * Haetaan aina ruokia, eli tänne tulee aina ruoan ID-numero.
      * Jos haetaan juoman nimellä, niin listaan tulee ruokia, joiden pari juoma on. 
      * Tässä vaiheessa minnekään ei näytetä listaa juomista.
      * @param idNumero Haettavan ruoan id-numero.
      */
     private void hae(final int idNumero) {
         int apuId = idNumero;
         if (apuId < 1) {
             Ruoka kohdalla = ruokaKohdalla;
             if (kohdalla != null) apuId = kohdalla.getIdNumero(); 
         }
         
         int k = ehdot.getSelectionModel().getSelectedIndex(); // 0 = ruoka, 1 = juoma
         String ehto = hakuehto.getText();
         if (ehto.indexOf('*') < 0 ) ehto = '*' + ehto + '*'; //jos käyttäjä ei ole antanut tähteä, lisätään se hakuehtoon. Muuten oletetaan
                                                              // että käyttäjä tietää tähden olevan oikeassa paikassa.
         ruokaChooser.clear();
         
         int index = 0;
         var ruoat = ruokajuoma.etsi(ehto, k); // lista hakuehtoja vastaavia ruokia
         int i = 0;
         for (Ruoka ruoka : ruoat) {
             if (ruoka.getIdNumero() == apuId) index = i; // tarkistetaan onko tämä ruoka, joka halutaan olevan valittuna etsimisen jälkeen
             ruokaChooser.add(ruoka.getNimi(), ruoka);
             i++;
         }
         ruokaChooser.setSelectedIndex(index);
     }
     
     
     /**
      * Haetaan juomien tiedot listChooseriin.
      * @param idNumero Haettavan juoman id-numero.
      */
     private void haeJuomat(final int idNumero) {
         int apuId = idNumero;
         if (apuId < 1) {
             Juomatieto kohdalla = juomaKohdalla;
             if (kohdalla != null) apuId = kohdalla.getIdNumero(); 
         }
         String ehto = juomaHaku.getText();
         if (ehto.indexOf('*') < 0 ) ehto = '*' + ehto + '*';
                 
         juomaChooser.clear();
         int index = 0;
         var juomat = ruokajuoma.etsiJuomat(ehto);
         int i = 0;
         for (Juomatieto juoma : juomat) {
             if (juoma.getIdNumero() == apuId) index = i; // tarkistetaan onko tämä ruoka, joka halutaan olevan valittuna etsimisen jälkeen
             juomaChooser.add(juoma.getNimi(), juoma);
             i++;
         }
         juomaChooser.setSelectedIndex(index);         
     }
        
     
     /**
      * Tallennetaan tiedot
      */
     private void tallenna() {
         ruokajuoma.tallenna();
         //Dialogs.showMessageDialog("Vielä ei osata tallentaa");
     }
     
     
     /**
      * Tarkistetaan onko tallennus tehty
      * @return true jos saa sulkea sovelluksen, false jos ei
      */
     public boolean voikoSulkea() {
         tallenna();
         return true;
     }


    /**
     * Asetetaan viite käytettävään ruokajuomaolioon
     * @param ruokajuoma käytettävä olio
     */
    public void setRuokajuoma(Ruokajuoma ruokajuoma) {
        this.ruokajuoma = ruokajuoma;
        hae(0);
        haeJuomat(0);
    }
    
    
   /**
    * Tulostaa ruoan juomatietoja TextAreaan.
    * @param os tietovirta, mihin tulostetaan
    * @param ruoka ruoka, jonka juomatietoja tulostetaan.
    */
   public void tulosta(PrintStream os, final Ruoka ruoka) {
        os.println("Lisätyt juomat ruoalle: ");
        ruoka.tulosta(os);
        os.println("-------------------------------------------");
        
        ArrayList<Juomatieto> juomat = ruokajuoma.annaJuomatRuoalle(ruoka);
        for (Juomatieto juoma : juomat) {
            ruokajuoma.tulostaJuomatiedot(juoma, os);
            os.println("-------------------------------------------");
          //juoma.tulosta(os);
        }
    }
    
   
    /**
     * Näytetään ohjelman avustus eli suunnitelma erillisessä selaimessa.
     */     
    private void avustus() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2019k/ht/parmatta");
                desktop.browse(uri);
        } catch (URISyntaxException e) {
                return;
        } catch (IOException e) {
               return;
        }
    }
     
    
}
