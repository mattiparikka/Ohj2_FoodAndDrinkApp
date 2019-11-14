package ruokajajuoma;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import fi.jyu.mit.ohj2.WildChars;


/**
 * CRC-määritelmä:
 * Vastuualueet:
 *   - Pitää yllä listaa ruoista eli osaa lisätä ja poistaa ruoan.
 *   - Osaa etsiä ja lajitella
 *   - Lukee ja kirjoittaa ruokia tiedostoon
 * Avustajat:
 *   - Ruoka-luokka
 * Ruokien listauksen ylläpito. Osaa lisätä ruokia ohjelmaan.
 * @author Matti Parikka
 * @version 0.9, 3 May 2019
 *
 */
public class Ruoat {

    private static final int MAX_RUOKIA = 6;
    private Ruoka[] alkiot = new Ruoka[MAX_RUOKIA];
    private int lkm = 0;
    private boolean muutettu = false;
    private String tiedostonOletusNimi = "ruoka";
    
    
    /**
     * Lisätään ruoka ruokien taulukkoon.
     * @param ruoka lisättävä ruokaolio
     * @example
     * <pre name="test">
     * Ruoat ruoat = new Ruoat();
     * Ruoka savukala = new Ruoka(), lakritsikala = new Ruoka();
     * ruoat.getLkm() === 0;
     * ruoat.lisaa(savukala); ruoat.getLkm() === 1;
     * ruoat.lisaa(lakritsikala); ruoat.getLkm() === 2;
     * ruoat.lisaa(savukala); ruoat.getLkm() === 3;
     * ruoat.anna(0) === savukala;
     * ruoat.anna(1) === lakritsikala;
     * ruoat.anna(2) === savukala;
     * ruoat.anna(1) == savukala === false;
     * ruoat.anna(1) == lakritsikala === true;
     * ruoat.lisaa(savukala); ruoat.getLkm() === 4;
     * ruoat.lisaa(savukala); ruoat.getLkm() === 5;
     * ruoat.lisaa(savukala); ruoat.getLkm() === 6;
     * ruoat.getSize()=== 6;
     * ruoat.lisaa(savukala); ruoat.getLkm() === 7;
     * ruoat.getSize()=== 11;
     * </pre>
     */
    public void lisaa(Ruoka ruoka) {
        if (lkm == alkiot.length) {
            alkiot = Arrays.copyOf(alkiot, alkiot.length+5);
        }
        this.alkiot[lkm] = ruoka;
        this.lkm++;
        this.muutettu = true;
    }
    
    
    /**
     * @return palautetaan testejä varten tietorakenteen koko. Ei siis vain käytetyt alkiot.
     */
    public int getSize() {
        return alkiot.length;
    }
    
    /**
     * @param ruoka ruoka, jota mahdollisesti muutetaan tai lisätään se listalle.
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     * Ruoat ruoat = new Ruoat();
     * Ruoka ruoka1 = new Ruoka(), ruoka2 = new Ruoka();
     * ruoka1.rekisteroi(); ruoka2.rekisteroi();
     * ruoat.getLkm() === 0;
     * ruoat.korvaaTaiLisaa(ruoka1); ruoat.getLkm() === 1;
     * ruoat.korvaaTaiLisaa(ruoka2); ruoat.getLkm() === 2;
     * Ruoka ruoka3 = ruoka1.clone();
     * ruoka3.setNimi("lihapata");
     * ruoat.anna(0) == ruoka1 === true;
     * ruoat.korvaaTaiLisaa(ruoka3); ruoat.getLkm() === 2;
     * ruoat.anna(0) == ruoka3 === true;
     * ruoat.anna(0) == ruoka1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Ruoka ruoka) {
        int id = ruoka.getIdNumero();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getIdNumero() == id) {
                alkiot[i] = ruoka;
                muutettu = true;
                return;
            }
        }
        lisaa(ruoka);
    }

    
    /**
     * Poistetaan ruoka listalta. Korjataan ruokalista siirtämällä poistettavan ruoan jälkeiset alkiot
     * lähemmäs alkua.
     * @param id poistettavan ruoan id
     * @return 0 (nok) tai 1 (ok)
     * @example
     * <pre name="test">
     * Ruoat ruoat = new Ruoat();
     * Ruoka ruoka1 = new Ruoka(), ruoka2 = new Ruoka(), ruoka3 = new Ruoka();
     * ruoka1.rekisteroi(); ruoka2.rekisteroi(); ruoka3.rekisteroi();
     * int id1 = ruoka1.getIdNumero();
     * ruoat.lisaa(ruoka1); ruoat.lisaa(ruoka2); ruoat.lisaa(ruoka3);
     * ruoat.poista(id1+1) === 1;
     * ruoat.annaTietoId(id1+1) === null; ruoat.getLkm() === 2;
     * ruoat.poista(id1) === 1; ruoat.getLkm() === 1;
     * ruoat.poista(id1+3) === 0; ruoat.getLkm() === 1;
     * </pre>
     */
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    }
    
    
    /**
     * Etsitään poistettavan ruoan paikka taulukossa, jotta voidaan järjestää lopputaulukko sen jälkeen
     * @param id etsittävä id
     * @return -1, jos ei löydy. Muuten id:n paikka.

     * <pre name="test">

     * Ruoat ruoat = new Ruoat();
     * Ruoka savukala1 = new Ruoka(), savukala2 = new Ruoka(), savukala3 = new Ruoka();
     * savukala1.rekisteroi(); savukala2.rekisteroi(); savukala3.rekisteroi();
     * int id1 = savukala1.getIdNumero();
     * ruoat.lisaa(savukala1); ruoat.lisaa(savukala2); ruoat.lisaa(savukala3);
     * ruoat.etsiId(id1+1) === 1;
     * ruoat.etsiId(id1+2) === 2;
     * </pre> 
     */
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getIdNumero()) return i; 
        return -1; 
    } 
    
    
    /**
     * @param hakuehto nimi, jonka perusteella etsitään
     * @return lista löytyneistä alkioista
     * @example
     * <pre name="test">
     * #import java.util.*;
     *   Ruoat ruoat = new Ruoat();
     *   Ruoka ruoka1 = new Ruoka(); ruoka1.parse("1|Lakritsikala");
     *   Ruoka ruoka2 = new Ruoka(); ruoka2.parse("2|Savukala ja kevatsipulikastike");
     *   Ruoka ruoka3 = new Ruoka(); ruoka3.parse("3|Teriyakilohi");
     *   Ruoka ruoka4 = new Ruoka(); ruoka4.parse("4|Korealainen bulgogi");
     *   Ruoka ruoka5 = new Ruoka(); ruoka5.parse("5|Savukala 760");
     *   ruoat.lisaa(ruoka1); ruoat.lisaa(ruoka2); ruoat.lisaa(ruoka3); ruoat.lisaa(ruoka4); ruoat.lisaa(ruoka5);
     *   List<Ruoka> loytyneet; 
     *   loytyneet = (List<Ruoka>)ruoat.etsi("*s*"); 
     *   loytyneet.size() === 3; 
     *   loytyneet.get(0) == ruoka1 === true; 
     *   loytyneet.get(1) == ruoka5 === true; 
     *   loytyneet.get(2) == ruoka2 === true;
     *     
     *   loytyneet = (List<Ruoka>)ruoat.etsi("*savu*"); 
     *   loytyneet.size() === 2; 
     *   loytyneet.get(0) == ruoka5 === true; 
     *   loytyneet.get(1) == ruoka2 === true;
     *     
     *   loytyneet = (List<Ruoka>)ruoat.etsi(null); 
     *   loytyneet.size() === 5; 
     * </pre> 
     */
    public Collection<Ruoka> etsi(String hakuehto) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto; //jos mitään ei ole hakukentässä, jää tähti voimaan
        List<Ruoka> loytyneet = new ArrayList<Ruoka>();   
        for (int i = 0; i < lkm; i++) {
            if (WildChars.onkoSamat(alkiot[i].getNimi(), ehto)) loytyneet.add(alkiot[i]); // etsitään ruokia vain nimen perusteella eikä muulle ole tarvetta
        }
        Collections.sort(loytyneet);
        return loytyneet; 
    }
    
    
    /**
     * @return kuinka monta ruokaa on Ruoat-luokan taulukossa.
     */
    public int getLkm() {
        return this.lkm;
    }
    
    
    /**
     * @param i kysyttävä indeksi
     * @return Palauttaa viitteen ruokaan, jonka indeksi on i.
     * @throws IndexOutOfBoundsException jos ei ole sallitulla alueella.
     */
    public Ruoka anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i) 
            throw new IndexOutOfBoundsException("Antamasi indeksi ei ole sallittu: " +i);
        return this.alkiot[i];
    }
    
    
    /**
     * Haetaan tietyn id-numeron ruoka
     * @param idNro id-numero
     * @return palauttaa Ruoan, jonka id-nro on i
     * <pre name="test">
     * Ruoat ruoat = new Ruoat();
     * Ruoka ruoka1 = new Ruoka(), ruoka2 = new Ruoka(), ruoka3 = new Ruoka();
     * ruoka1.rekisteroi(); ruoka2.rekisteroi(); ruoka3.rekisteroi();
     * int id1 = ruoka1.getIdNumero();
     * ruoat.lisaa(ruoka1); ruoat.lisaa(ruoka2); ruoat.lisaa(ruoka3);
     * ruoat.annaTietoId(id1  ) == ruoka1 === true;
     * ruoat.annaTietoId(id1+1) == ruoka2 === true;
     * ruoat.annaTietoId(id1+2) == ruoka3 === true;
     */
    public Ruoka annaTietoId(int idNro) {
        //Ruoka tieto = new Ruoka();
        for (int i = 0; i < lkm; i++) {
            if(alkiot[i].getIdNumero() == idNro) return alkiot[i];
        }
        return null;

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
     * Luetaan ruokien tiedot tiedostosta.
     * @param tiedosto luetun tiedoston nimi
     * @example
     * <pre name="test">
     * #import java.io.File;
     *  Ruoat ruoat = new Ruoat();
     *  Ruoka ruoka1 = new Ruoka(), ruoka2 = new Ruoka();
     *  ruoka1.taytaKala();
     *  ruoka2.taytaKala();
     *  String hakemisto = "testiruoat";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  ruoat.lueTiedostosta(tiedNimi); 
     *  ruoat.lisaa(ruoka1);
     *  ruoat.lisaa(ruoka2);
     *  ruoat.tallenna();
     *  ruoat = new Ruoat();            // Poistetaan vanhat luomalla uusi
     *  ruoat.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.

     *  ruoat.anna(0).compareTo(ruoka1);
     *  ruoat.anna(1).compareTo(ruoka2);
     *  ruoat.getLkm() === 2;
     *  ruoat.getSize() === 6;
     *  
     *  ruoat.lisaa(ruoka2);
     *  ruoat.tallenna();
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

                Ruoka ruoka = new Ruoka();
                ruoka.parse(rivi);
                lisaa(ruoka);
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
     * Tallennetaan
     */
    public void tallenna() {
        if (!muutettu) return; // TODO: versioseuranta jos tarpeen
        
        File fbackup = new File(getBakTiedostoNimi());
        File ftiedosto = new File(getTiedostoNimi());
        fbackup.delete();
        ftiedosto.renameTo(fbackup);
        
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftiedosto, true))) { //tarvitseeko getCanonicalPath??
            fo.println(";rid = ruoan id");
            fo.println(";rid|ruoka");
            
            for (int i = 0; i < lkm; i++) {
                fo.println(alkiot[i].toString());
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        this.muutettu = false;
    }

    
    /**
     * Testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ruoat ruoat = new Ruoat();
        
        Ruoka savukala = new Ruoka();
        Ruoka lakritsikala = new Ruoka();
    
        savukala.rekisteroi();
        lakritsikala.rekisteroi();

        savukala.taytaKala();
        lakritsikala.taytaKala();
        
        //try {
        ruoat.lisaa(savukala);
        ruoat.lisaa(lakritsikala);
        
        System.out.println("========================= Testataan Ruoat-luokkaa ==========================");
        for (int i = 0; i < ruoat.getLkm(); i++) {
            Ruoka ruoka = ruoat.anna(i);
            System.out.println("Ruoka paikassa: " + i);
            ruoka.tulosta(System.out);
        }
        /*
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
        */
    }
}
