package ruokajajuoma;

import static testiohjelmat.SatunnaisKastike.random;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Listaluokka, joka ylläpitää yksittäisiä juomatieto-olioita
 * CRC-määritelmä:
 * Vastuualueet:
 * - (Ei tiedä pääohjelmasta, ruoista, juomista, linkeistä tai käyttöliittymästä) 
 * - Tietää ostopaikan tietojen id:t                        
 * - Osaa antaa merkkijonona i:n id:n tiedot
 * - Osaa laittaa merkkijonon i:neksi id:n tiedoksi
 * @author Matti Parikka
 * @version 0.9, 10 May 2019
 *
 */
public class Ostopaikkatieto {
    private int idNumero; // ostopaikan id numero
    private String nimi = "";  // ostopaikan nimi
    private static int seuraavaNro = 1; //seuraavan ostopaikan id-numero
    
    
    
    /**
     * @return ostopaikan uusi ID-numero.
     * @example
     * <pre name="test">
     *   Ostopaikkatieto kauppaCM = new Ostopaikkatieto();
     *   kauppaCM.getIdNumero() === 0;
     *   kauppaCM.rekisteroi();
     *   kauppaCM.rekisteroi();
     *   Ostopaikkatieto kauppaSM = new Ostopaikkatieto();
     *   kauppaSM.rekisteroi();
     *   kauppaSM.rekisteroi();
     *   kauppaSM.rekisteroi();
     *   int n1 = kauppaCM.getIdNumero();
     *   int n2 = kauppaSM.getIdNumero();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        if ( this.idNumero != 0 ) return this.idNumero;
        this.idNumero = seuraavaNro;
        seuraavaNro++;
        return this.idNumero;
    }
    
    
    /**
     * Asettaa idNumeron tiedoston perusteella ja varmistaa, että seuraavaNro on päivittynyt riittävän isoksi.
     * @param idNro tiedostoa luettaessa saatava idNumero
     */
    public void setIdNumero(int idNro) {
        idNumero = idNro;
        if (idNro >= seuraavaNro) seuraavaNro = idNumero + 1;
    }
    
    /**
     * Luetaan ostopaikka.dat tyyppistä tiedostoa.
     * @param rivi merkkijono, jossa ruoan idnumero ja nimi.
     */
    public void parse(String rivi) {
        StringBuilder jono = new StringBuilder(rivi);
        setIdNumero(Mjonot.erota(jono, '|', idNumero)); // katso id numeron ja seuraavan osuus
        this.nimi = Mjonot.erota(jono, '|', nimi);  
    }
    
    
    @Override
    public String toString() {
        return this.idNumero + "|" + this.nimi;
    }
    
    
    /**
     * Palauttaa ostopaikan id-numeron.
     * @return palauttaa ostopaikan oman ID-numeron.
     */
    public int getIdNumero() {
        return this.idNumero;
    }
    
    
    /**
     * Palauttaa ostopaikan nimen.
     * @return ostopaikan nimi
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * @param nimi asetettava uusi nimi
     * @return asetettu nimi
     */
    public String setNimi(String nimi) {
        this.nimi = nimi; 
        return null;                
    }
    
    /**
     * Tulostaa ostopaikan tietoja tietovirtaan.
     * @param out tietovirta, johon tulostetaan.
     */
    public void tulosta(PrintStream out) {
        out.println("Ostopaikan nimi: " + this.nimi);
        out.println("Ostopaikan id: " + String.format("%03d", this.idNumero));
    }
    
    
    /**
     * Tulostetaan ostopaikan tietoja tietovirtaan.
     * @param os tietovirta, johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * "Rakennusteline" mallitietojen täyttämiseksi ja ohjelman toiminnan varmistamiseksi.
     */
    public void taytaPaikka() {
        this.nimi = "Hypermarket juomaosasto " + random(1,1000);
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        
        Ostopaikkatieto kauppaCM = new Ostopaikkatieto();
        Ostopaikkatieto kauppaSM = new Ostopaikkatieto();
        
        kauppaCM.rekisteroi();
        kauppaSM.rekisteroi();
        
        kauppaCM.tulosta(System.out);
        kauppaSM.tulosta(System.out);
        
        kauppaCM.taytaPaikka();
        kauppaSM.taytaPaikka();
        
        kauppaCM.tulosta(System.out);
        kauppaSM.tulosta(System.out);

    }


}
