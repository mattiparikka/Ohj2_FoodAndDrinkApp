package ruokajajuoma;

import static testiohjelmat.SatunnaisKastike.random;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Juoman tiedot sisältävä luokka. Sisältää linkin ostopaikkaan, mutta ei tiedä ostopaikoista mitään. 
 * CRC-määritelmä:
 * Vastuualueet:
 * - (Ei tiedä pääohjelmasta, ruoista, ostopaikoista, linkeistä tai käyttöliittymästä) 
 * - Tietää juoman tietojen id:t                        
 * - Osaa antaa merkkijonona i:n id:n tiedot
 * - Osaa laittaa merkkijonon i:neksi id:n tiedoksi
 * - (Osaa muuttaa merkkijonon tiedoiksi)
 * - (Osaa verrata itseään toiseen vastaavaan olioon)
 * - (Osaa kloonata itsensä)
 * @author Matti Parikka
 * @version 0.9, 10 May 2019
 *
 */
public class Juomatieto implements Cloneable, Comparable<Juomatieto>{

    private int idNumero; // juoman id numero
    private String nimi = "";  // juoman nimi
    private static int seuraavaNro = 1; //seuraavan juoman id-numero
    private int ostopaikka = 0;
    private double hinta;  // muutettu doubleksi, random generaattori rakennustelineelle ei enää toimi. TODO: korjaa?
    private String opaikka = "Anna ostopaikka"; // tilapäisatribuutti ennen Ostopaikat-luokan kunnon toteutusta
    
    private String SALLITUT = "0123456789.,";
    

    /**
     * @return juoman uusi ID-numero.
     * @example
     * <pre name="test">
     *   Juomatieto punaviini = new Juomatieto();
     *   punaviini.getIdNumero() === 0;
     *   punaviini.rekisteroi();
     *   punaviini.rekisteroi();
     *   Juomatieto valkoviini = new Juomatieto();
     *   valkoviini.rekisteroi();
     *   valkoviini.rekisteroi();
     *   valkoviini.rekisteroi();
     *   int n1 = punaviini.getIdNumero();
     *   int n2 = valkoviini.getIdNumero();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        if ( this.idNumero != 0 ) return this.idNumero;
        this.idNumero = seuraavaNro;
        seuraavaNro++;
        return this.idNumero;
    }

    
    @Override
    public int compareTo(Juomatieto juoma) {
        return nimi.compareToIgnoreCase(juoma.getNimi());
    }
    
    @Override
    public Juomatieto clone() throws CloneNotSupportedException {
        Juomatieto uusi = (Juomatieto)super.clone();
        return uusi; // voisi olla return (Ruoka)super.clone();
    }
    
    /*
    public String anna(int n) {
        switch ( n ) {
        case 0: return "" + idNumero;
        case 1: return "" + nimi;
        case 2: return "" + hinta;
        case 3: return "" + ostopaikka;
        
        
        }
    }
    */
    
    /** Kierrätetään tyhmässä versiossa juomacontrollerista saatu tieto temp atribuutin kautta paikkaoliolle.
     * @param paikka ostopaikka tekstinä
     * @return null
     */
    public String setTempPaikka(String paikka) {
        this.opaikka = paikka;
        return null;
    }
    
    
    /** Tyhmä versio
     * @return tilapäispaikka tekstimuotona
     */
    public String getTempPaikka() {
        return this.opaikka;
    }
    
    
    /**
     * @param jono asetettava uusi nimi
     * @return asetettu nimi
     */
    public String setHinta(String jono) {
        String uusiS = jono.trim();
        StringBuilder uusi = new StringBuilder(uusiS);
        
        // vaihdetaan pilkut pisteiksi, jotta vältetään turhat virheet käyttäjän puolelta.
        int piste = uusi.indexOf(",");
        while (piste != -1) {
            uusi.replace(piste, piste+1, ".");
            piste = uusi.indexOf(",");
        }
        this.hinta = Mjonot.erotaDouble(uusi, 0.0); 
        return null;                
    }  
    
    
    /**
     * Asetetaan merkkijonosta poimitut numerot hinnaksi, jos on syötetty oikeassa muodossa
     * @param jono syöte
     * @return null jos onnistui, virheteksti jos syötteessä vikaa.
     */
    public String aseta(String jono) {
        String uusiS = jono.trim();
        if (!tarkista(uusiS)) return "Hinta voi olla vain numeroita pisteellä tai pilkulla erotettuna, esim. 23.45";
       
        StringBuilder uusi = new StringBuilder(uusiS);       
        // vaihdetaan pilkut pisteiksi, jotta vältetään turhat virheet käyttäjän puolelta.
        int piste = uusi.indexOf(",");
        while (piste != -1) {
            uusi.replace(piste, piste+1, ".");
            piste = uusi.indexOf(",");
        }
        this.hinta = Mjonot.erotaDouble(uusi, 0.0); 
        return null;      
    }
    
    
    /**
     * Tarkistetaan kelpaako jono eli onko kaikki merkit sallittuja
     * @param jono tutkittava jono
     * @return true/false
     */
    public boolean tarkista(String jono) {
        int valimerkkeja = 0;
        
        for (int i = 0; i < jono.length(); i++) {
            char merkki = jono.charAt(i);
            if (SALLITUT.indexOf(merkki) < 0) return false;    // katsotaan antaako indexOf alle 0 eli löytyykö jonon i merkki jonosta SALLITUT.
            if (merkki == '.' || merkki == ',') valimerkkeja++; //lasketaan onko käyttäjä antanut liikaa pilkkuja tai pisteitä
        }
        if (valimerkkeja > 1) return false;
        return true;
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
     * Asettaa idNumeron tiedoston perusteella ja varmistaa, että seuraavaNro on päivittynyt riittävän isoksi.
     * @param idNro tiedostoa luettaessa saatava idNumero
     */
    public void setIdNumero(int idNro) {
        idNumero = idNro;
        if (idNro >= seuraavaNro) seuraavaNro = idNumero + 1;
    }
    
    
    /**
     * Luetaan Juomientiedot.dat tyyppistä tiedostoa.
     * @param rivi merkkijono, jossa ruoan idnumero ja nimi.
     */
    public void parse(String rivi) {
        StringBuilder jono = new StringBuilder(rivi);
        setIdNumero(Mjonot.erota(jono, '|', idNumero)); // katso id numeron ja seuraavan osuus
        this.nimi = Mjonot.erota(jono, '|', nimi);  
        this.ostopaikka = Mjonot.erota(jono, '|', ostopaikka);
        this.hinta = Mjonot.erota(jono, '|', hinta);
        this.opaikka = Mjonot.erota(jono, '|', opaikka);
        //TODO: tiedostosta lukuun lisättävä opaikka, jotta avatessa pysyy oikea ostopaikka temppinä! 
    }
    
    
    @Override
    public String toString() {
        return this.idNumero + "|" + this.nimi + "|" + this.ostopaikka + "|" + this.hinta + "|" + this.opaikka;
    }
    
    
    /**
     * Asetetaan ostopaikan id juomatiedolle
     * @param id ostopaikan id
     * @return ostopaikan id
     */
    public int asetaOstopaikka(int id) {
        return this.ostopaikka = id;
    }
    
    
    /**
     * Palauttaa juoman ostopaikan id-numeron.
     * @return palauttaa juoman ostopaikan ID-numeron.
     */
    public int getOstoId() {
        return this.ostopaikka;
    }
    
    
    /**
     * Palauttaa juoman id-numeron.
     * @return palauttaa juoman oman ID-numeron.
     */
    public int getIdNumero() {
        return this.idNumero;
    }
    
    
    /**
     * Palauttaa juoman nimen.
     * @return juoman nimi
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /** Tyhmään versioon getMetodi
     * @return s
     */
    public String getHinta() {
        return ""+this.hinta;
    }
    
    
    /**
     * Tulostaa juoman tietoja tietovirtaan.
     * @param out tietovirta, johon tulostetaan.
     */
    public void tulosta(PrintStream out) {
        out.println("Juoman nimi: " + this.nimi);
        out.println("Juoman id: " + String.format("%03d", this.idNumero));
        out.println("Hinta: " +  this.hinta + " eur");
        if (ostopaikka == 0) out.println("Juoman ostopaikka: " + this.opaikka);
        else out.print("");
    }
    
    
    /**
     * Tulostetaan juoman tietoja tietovirtaan.
     * @param os tietovirta, johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * "Rakennusteline" mallitietojen täyttämiseksi ja ohjelman toiminnan varmistamiseksi.
     */
    public void taytaViini() {
        this.nimi = "Punaviini, Argentiina, bin " + random(1,1000);
        this.opaikka = "Alko";
        this.hinta = random(3,500);
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        
        Juomatieto punaviini = new Juomatieto();
        Juomatieto valkoviini = new Juomatieto();
        
        punaviini.rekisteroi();
        valkoviini.rekisteroi();
        
        punaviini.tulosta(System.out);
        valkoviini.tulosta(System.out);
        
        punaviini.taytaViini();
        valkoviini.taytaViini();
        
        punaviini.tulosta(System.out);
        valkoviini.tulosta(System.out);

    }



}
