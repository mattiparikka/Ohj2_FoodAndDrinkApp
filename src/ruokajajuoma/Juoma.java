package ruokajajuoma;

import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * "Liimaolio" ruoka ja juomatieto olioiden yhteenliittämiseksi
 * CRC-määritelmä:
 * Vastuualueet:
 * - (Ei tiedä pääohjelmasta tai käyttöliittymästä) 
 * - (Ei tiedä juomatiedoista tai ruoista)                        
 * - Osaa antaa kokonaislukuna i:n id:n tiedot          
 * - Osaa laittaa kokonaisluvun i:neksi id:n tiedoksi                                                   
 * - Osaa muuttaa merkkijonon tiedoiksi
 * @author Matti Parikka
 * @version 0.9, 3 May 2019
 *
 */
public class Juoma {
    
    private int ruoanId; // = ruoka id-nro
    private int juomanId; // = juomatieto id-nro
    
    
    /**
     * Asetetaan ruoaka ja juoma pariksi näiden omien olioiden id-numeroiden avulla (Ruoka ja Juomatieto -olioiden id:t)
     * @param ruoka Ruokaolion Id-numero
     * @param juoma Juomatieto-olion Id-numero
     */
    public void luoPari(int ruoka, int juoma) {
        this.ruoanId = ruoka;
        this.juomanId = juoma;
    }
    
    
    /**
     * Asetetaan ruoaka ja juoma pariksi näiden omien olioiden id-numeroiden avulla (Ruoka ja Juomatieto -olioiden id:t)
     * @param ruoka Ruokaolion Id-numero
     * @param juoma Juomatieto-olion Id-numero
     */
    public Juoma (int ruoka, int juoma) {
        this.ruoanId = ruoka;
        this.juomanId = juoma;
    }
    
    
    /**
     * @return palautetaan tämän ruokajuomaparin ruoan id.
     */
    public int getRuoanId() {
        return this.ruoanId;
    }
    
    
    /**
     * @return @return palautetaan tämän ruokajuomaparin juoman id.
     */
    public int getJuomanId() {
        return this.juomanId;
    }
    
    
    /**
     * Luetaan ostopaikka.dat tyyppistä tiedostoa.
     * @param rivi merkkijono, jossa ruoan idnumero ja nimi.
     */
    public void parse(String rivi) {
        StringBuilder jono = new StringBuilder(rivi);
        this.ruoanId = Mjonot.erota(jono, '|', ruoanId); // katso id numeron ja seuraavan osuus
        this.juomanId = Mjonot.erota(jono, '|', juomanId);  
    }
    
    
    @Override
    public String toString() {
        return this.ruoanId + "|" + this.juomanId;
    }
    
    
    /**
     * Tulostaa ruoan tietoja tietovirtaan.
     * @param out tietovirta, johon tulostetaan.
     */
    public void tulosta(PrintStream out) {
        out.println("Ruoan id: " + String.format("%03d", this.ruoanId));
        out.println("Juoman id: " + String.format("%03d", this.juomanId));
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
