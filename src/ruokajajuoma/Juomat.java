package ruokajajuoma;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Listaluokka, joka ylläpitää yksittäisiä juomaolioita
 * CRC-määritelmä:
 * Vastuualueet:
 * - (Ei tiedä pääohjelmasta tai käyttöliittymästä) 
 * - (Ei tiedä juomatiedoista tai ruoista)                        
 * - Pitää yllä varsinaista listaa ruokia ja juomia yhdistävistä linkkiolioista
 * - Osaa lisätä ja poistaa linkin juoman ja yhden tai useamman ruoan väliltä
 * - lukee ja kirjoittaa juomia tiedostoon
 * @author Matti Parikka
 * @version 0.9, 3 May 2019
 *
 */
public class Juomat {
    
    
    private ArrayList<Juoma> parit = new ArrayList<Juoma>();
    private String tiedostonOletusNimi = "ruokienjuomat";
    private boolean muutettu = false;
    
    
    /**
     * Lisätään ruokajuomapari listalle. Testi poistamisen yhteydessä.
     * @param juoma lisättäväjuomapari
     */
    public void lisaa(Juoma juoma) {  
        this.parit.add(juoma);
        this.muutettu = true;
    }
    
    
    /**
     * Poistetaan linkkiolio valitun ruoan ja juoman väliltä
     * @param juomaId juomatieto, joka poistetaan ruoalta
     * @param ruokaId ruoka, jolta poistetaan
     * @return true/false
     * @example
     * <pre name="test">
     *  Juomat juomat = new Juomat();
     *  Juoma juoma21 = new Juoma(2,1); 
     *  Juoma juoma11 = new Juoma(1,1); 
     *  Juoma juoma22 = new Juoma(2,2); 
     *  Juoma juoma12 = new Juoma(1,2); 
     *  juomat.lisaa(juoma21);
     *  juomat.lisaa(juoma11);
     *  juomat.lisaa(juoma22);
     *  juomat.lisaa(juoma12);
     *  juomat.poista(2,3) === false; 
     *  juomat.getLkm() === 4;
     *  juomat.poista(1,1) === true;   
     *  juomat.getLkm() === 3;
     * </pre>
     */
    public boolean poista(int juomaId, int ruokaId) {
        for (int i = 0; i < parit.size(); i++) {
            if (parit.get(i).getRuoanId() == ruokaId && parit.get(i).getJuomanId() == juomaId) {
                parit.remove(parit.get(i));                
                muutettu = true;
                return true; // koska voi olla vain yksi olio, jolla on kyseinen ruoka & juoma pari, lähdetään
                             // silmukasta ja metodista samantien pois poiston jälkeen. Tällöin ei tarvitse korjata i--;
            }    
        }
        return false;
    }
    
    
    /**
     * Poistetaan kaikki poistettavaan ruokaan liittyvät linkkioliot, jolloin mikään juoma ei ole enää poistetun 
     * ruoan pari. Ei poista juomia muilta ruoilta eli ei vaikuta Juomatietoon.
     * Testissä tiedot muodossa ruoan id | juoman id.
     * @param ruoka poistettavan ruoan id
     * @return true/false
     * @example
     * <pre name="test">
     *  Juomat juomat = new Juomat();
     *  Juoma juoma21 = new Juoma(2,1); 
     *  Juoma juoma11 = new Juoma(1,1); 
     *  Juoma juoma22 = new Juoma(2,2); 
     *  Juoma juoma12 = new Juoma(1,2); 
     *  juomat.lisaa(juoma21);
     *  juomat.lisaa(juoma11);
     *  juomat.lisaa(juoma22);
     *  juomat.lisaa(juoma12);
     *  juomat.poistaRuokaJuomilta(2) === true; 
     *  juomat.getLkm() === 2;
     *  juomat.poistaRuokaJuomilta(1) === true;   
     *  juomat.getLkm() === 0;
     * </pre>
     */
    public boolean poistaRuokaJuomilta(int ruoka) { 
        boolean ret = false;
        for (int i = 0; i < parit.size(); i++) {
            if (parit.get(i).getRuoanId() == ruoka) {
                parit.remove(parit.get(i));
                muutettu = true;
                ret = true;
                i--;
            }    
        }
        return ret;
    }
    
    
    /**
     * Poistetaan linkkioliot id:n juoman ja kaikkien ruokien väliltä. Mikään ruoka ei siis enää voi olla id:ä tuodun juoman pari.
     * Juomatieto poistettava erikseen.
     * @param id poistettavan juoman id
     * @return true/false
     * @example
     * <pre name="test">
     *  Juomat juomat = new Juomat();
     *  Juoma juoma21 = new Juoma(2,1); 
     *  Juoma juoma11 = new Juoma(1,1); 
     *  Juoma juoma22 = new Juoma(2,2); 
     *  Juoma juoma12 = new Juoma(1,2); 
     *  juomat.lisaa(juoma21);
     *  juomat.lisaa(juoma11);
     *  juomat.lisaa(juoma22);
     *  juomat.lisaa(juoma12);
     *  juomat.poistaRuokaJuomilta(2) === true; 
     *  juomat.getLkm() === 2;
     *  juomat.poistaRuokaJuomilta(1) === true;   
     *  juomat.getLkm() === 0;
     * </pre>
     */
    public boolean poistaJuomaRuoilta(int id) {
        boolean ret = false;
        for (int i = 0; i < parit.size(); i++) {
            if (parit.get(i).getJuomanId() == id) {
                parit.remove(parit.get(i));
                muutettu = true;
                ret = true;
                i--;
            }    
        }
        return ret;
    }
    
       
    /**
     * @return palautetaan tiedoston oletusnimi
     */
    public String getTiedostonOletusNimi() {
        return this.tiedostonOletusNimi;
    }
    
    
    /**
     * @param nimi oletustiedostonimeksi asetettava nimi.
     * @return palautetaan tiedoston oletusnimi
     */
    public String setTiedostonOletusNimi(String nimi) {
        return this.tiedostonOletusNimi = nimi;
    }
    
    
    /**
     * Luetaan tiedostoa ja käytetään oletustiedostonimeä.
     */
    public void lueTiedostosta() {
        lueTiedostosta(tiedostonOletusNimi);
    }
    
    
    /**
     * Luetaan ruokien tiedot tiedostosta. Voisi yhdistäsä kaksi lueTiedostosta yhdeksi, koska nimi on tässä 
     * versiossa aina sama. Jätetään kutienkin, jos tulee tarvetta muuttaa eli avata muita listoja = "profiileja".
     * @param tiedosto luetun tiedoston nimi
     * @example
     * <pre name="test">
     * #import java.io.File;
     *  Juomat juomat = new Juomat();
     *  Juoma juoma1 = new Juoma(1,1), juoma2 = new Juoma(2,3);
     *  String hakemisto = "testilinkit";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  juomat.lueTiedostosta(tiedNimi); 
     *  juomat.lisaa(juoma1);
     *  juomat.lisaa(juoma2);
     *  juomat.tallenna();
     *  juomat = new Juomat();            // Poistetaan vanhat luomalla uusi
     *  juomat.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.

     *  juomat.anna(0).toString() === juoma1.toString();
     *  juomat.anna(1).toString() === juoma2.toString();
     *  juomat.getLkm() === 2;
     *  
     *  juomat.lisaa(juoma2);
     *  juomat.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tiedosto) {     
        //setTiedostonOletusNimi(tiedosto);
        this.tiedostonOletusNimi = tiedosto;
        
        try (Scanner fi = new Scanner(new FileInputStream(new File(getTiedostoNimi())))) {
            while (fi.hasNext()) {
                String rivi = fi.nextLine();
                if ("".equals(rivi) || rivi.charAt(0) == ';') continue;

                Juoma juoma = new Juoma(0,0);
                juoma.parse(rivi);
                lisaa(juoma);
            }
            this.muutettu = false;
            
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }   
    }
    
    
    /**
     * @return palautetaan tiedostonimi .dat -päätteellä.
     */
    public String getTiedostoNimi() {
        return this.tiedostonOletusNimi + ".dat";
    }
    
    
    /**
     * @return palautetaan tiedostonimi .bak -päätteellä.
     */
    public String getBakTiedostoNimi() {
        return this.tiedostonOletusNimi + ".bak";
    }
    
    
    /**
     * Tallennetaan tiedostoon tekstimuodossa. Testit lueTiedostosta -metodin yhteydessä.
     */
    public void tallenna() {
        //
        if (!muutettu) return; // TODO: versioseuranta
        
        File fbackup = new File(getBakTiedostoNimi());
        File ftiedosto = new File(getTiedostoNimi());
        fbackup.delete();
        ftiedosto.renameTo(fbackup);
        
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftiedosto, true))) { //tarvitseeko getCanonicalPath??
            fo.println(";ruokien ja juomien yhteys id:llä");
            fo.println(";rid|;jid");
            
            for (int i = 0; i < parit.size(); i++) {
                fo.println(parit.get(i).toString());
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        this.muutettu = false;
    }
    
    
    /**
     * @return kuinka monta ruokaa on Ruoat-luokan taulukossa.
     */
    public int getLkm() {
        return this.parit.size();
    }
    
    
    /**
     * @param i kysyttävä indeksi
     * @return Palauttaa viitteen juomatietoon, joka on taulukossa paikassa i.
     */
    public Juoma anna(int i) {
        return this.parit.get(i);
    }
    
    
    /**
     * Palauttaa ruokajuomaparit tietyn ruoan id:n perusteella eli kaikki ne parit, joiden ruoan id on annettu id.
     * @param idNro Ruoan id-numero. Huom. ei ole sama asia, kuin taulukon indeksi
     * @return id-numerolla olevat parit
     */
    public ArrayList<Juoma> annaJuomatRuoalle(int idNro){
        var loydetyt = new ArrayList<Juoma>();
        for (Juoma tieto : parit)
               if(tieto.getRuoanId() == idNro) loydetyt.add(tieto);
        return loydetyt;
    }
    
    
    /**
     * Palauttaa ruokajuomaparit tietyn juoman id:n perusteella eli kaikki ne parit, joiden juoman id on annettu id.
     * @param idNro halutun juoman id-numero. Huom. ei ole sama asia, kuin taulukon indeksi
     * @return id-numerolla olevat parit
     */
    public ArrayList<Juoma> annaRuoatJuomalle(int idNro){
        var loydetyt = new ArrayList<Juoma>();
        for (Juoma tieto : parit)
               if(tieto.getJuomanId() == idNro) loydetyt.add(tieto);
        return loydetyt;
    }
    

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        //
    }

}
