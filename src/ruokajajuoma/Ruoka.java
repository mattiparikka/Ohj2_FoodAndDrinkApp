package ruokajajuoma;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

import static testiohjelmat.SatunnaisKastike.*;

/**
 * CRC-määritelmä:
 * Vastuualueet:
 *   - Osaa antaa merkkijonona i:n ruoan id:n tiedot
 *   - Osaa laittaa merkkijonon i:neksi id:n tiedoiksi.
 *   - Osaa verrata itseään toiseen samanlaiseen olioon.
 * Avustajat:
 *   - Ei ole.
 * @author Matti Parikka
 * @version 0.9, 10 May 2019
 *
 */
public class Ruoka implements Cloneable, Comparable<Ruoka> {

    private int idNumero;
    private String nimi = "";
    private static int seuraavaNro = 1;
    
    
    /**
     * @return ruoan uusi ID-numero.
     * @example
     * <pre name="test">
     *   Ruoka savukala = new Ruoka();
     *   savukala.getIdNumero() === 0;
     *   savukala.rekisteroi();
     *   savukala.rekisteroi();
     *   Ruoka lakritsikala = new Ruoka();
     *   lakritsikala.rekisteroi();
     *   lakritsikala.rekisteroi();
     *   lakritsikala.rekisteroi();
     *   int n1 = savukala.getIdNumero();
     *   int n2 = lakritsikala.getIdNumero();
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
     * Palauttaa ruoan id-numeron.
     * @return palauttaa ruoan oman ID-numeron.
     */
    public int getIdNumero() {
        return this.idNumero;
    }
    
    
    /**
     * Palauttaa ruoan nimen.
     * @return ruoan nimi
     */
    public String getNimi() {
        return this.nimi;
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
     * @param nimi ruoalle asetettava uusi nimi
     * @return asetettu nimi
     */
    public String setNimi(String nimi) {
        this.nimi = nimi; 
        return null;                
    }
    
    @Override
    public Ruoka clone() throws CloneNotSupportedException {
        Ruoka uusi = (Ruoka)super.clone();
        return uusi; // voisi olla return (Ruoka)super.clone();
    }
    
    
    /**
     * @param rivi merkkijono, jossa ruoan idnumero ja nimi.
     */
    public void parse(String rivi) {
        StringBuilder jono = new StringBuilder(rivi);
        setIdNumero(Mjonot.erota(jono, '|', idNumero)); // katso id numeron ja seuraavan osuus
        this.nimi = Mjonot.erota(jono, '|', nimi);  
    }
    
    
    /**
     * Tulostaa ruoan tietoja tietovirtaan.
     * @param out tietovirta, johon tulostetaan.
     */
    public void tulosta(PrintStream out) {
        out.println("Ruoan nimi: " + this.nimi);
        out.println("Ruoan id: " + String.format("%03d", this.idNumero));
    }
    
    
    /**
     * Tulostetaan ruoan tietoja tietovirtaan.
     * @param os tietovirta, johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    @Override
    public String toString() {
        return this.idNumero + "|" + this.nimi;
    }
    
    
    /**
     * "Rakennusteline" mallitietojen täyttämiseksi ja ohjelman toiminnan varmistamiseksi.
     */
    public void taytaKala() {
        this.nimi = "Savukala " + random(1,1000);
    }
    
    
    @Override
    public int compareTo(Ruoka ruoka) {
        
        return nimi.compareTo(ruoka.getNimi());
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ruoka savukala = new Ruoka();
        Ruoka lakritsikala = new Ruoka();
        
        savukala.rekisteroi();
        lakritsikala.rekisteroi();
        
        savukala.tulosta(System.out);
        lakritsikala.tulosta(System.out);
     
        savukala.taytaKala();
        lakritsikala.taytaKala();

        savukala.tulosta(System.out);
        lakritsikala.tulosta(System.out);
        
    }









}
