package ruokajajuoma;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Listaluokka, joka ylläpitää yksittäisiä juomatieto-olioita
 * CRC-määritelmä:
 * Vastuualueet:
 * - (Ei tiedä pääohjelmasta, ruoista, ostopaikoista, linkeistä tai käyttöliittymästä)                      
 * - Osaa etsiä ja lajitella
 * - Osaa tallentaa tiedostoon
 * - Pitää yllä varsinaista listaa juomien tiedoista
 * @author Matti Parikka
 * @version 0.9, 3 May 2019
 *
 */
public class Juomientiedot {

    private ArrayList<Juomatieto> tiedot = new ArrayList<Juomatieto>();  //käytetään fiksussa versiossa
    private String tiedostonOletusNimi = "juomienTiedot";
    private boolean muutettu = false;
    
    
    /**
     * Lisätään juomatieto taulukkoon.
     * @param juomatieto lisättävä ruokaolio
     * @example
     * <pre name="test">
     * Juomientiedot juomientiedot  = new Juomientiedot();
     * Juomatieto punaviini = new Juomatieto(), valkoviini = new Juomatieto();
     * juomientiedot.getLkm() === 0;
     * juomientiedot.lisaa(punaviini); juomientiedot.getLkm() === 1;
     * juomientiedot.lisaa(valkoviini); juomientiedot.getLkm() === 2;
     * juomientiedot.lisaa(punaviini); juomientiedot.getLkm() === 3;
     * juomientiedot.anna(0) === punaviini;
     * juomientiedot.anna(1) === valkoviini;
     * juomientiedot.anna(2) === punaviini;
     * juomientiedot.anna(1) == punaviini === false;
     * juomientiedot.anna(1) == valkoviini === true;
     * juomientiedot.lisaa(punaviini); juomientiedot.getLkm() === 4;
     * juomientiedot.lisaa(punaviini); juomientiedot.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Juomatieto juomatieto) {
        this.tiedot.add(juomatieto);
        this.muutettu = true;
    }
    
    /**
     * @param juomatieto ruoka, jota mahdollisesti muutetaan tai lisätään se listalle.
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     * Juomientiedot juomientiedot = new Juomientiedot();
     * Juomatieto juoma1 = new Juomatieto(), juoma2 = new Juomatieto();
     * juoma1.rekisteroi(); juoma2.rekisteroi();
     * juomientiedot.getLkm() === 0;
     * juomientiedot.korvaaTaiLisaa(juoma1); juomientiedot.getLkm() === 1;
     * juomientiedot.korvaaTaiLisaa(juoma2); juomientiedot.getLkm() === 2;
     * Juomatieto juoma3 = juoma1.clone();
     * juoma3.setNimi("punkku");
     * juomientiedot.anna(0) == juoma1 === true;
     * juomientiedot.korvaaTaiLisaa(juoma3); juomientiedot.getLkm() === 2;
     * juomientiedot.anna(0) == juoma3 === true;
     * juomientiedot.anna(0) == juoma1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Juomatieto juomatieto) {
        int id = juomatieto.getIdNumero();
        for (int i = 0; i < tiedot.size(); i++) {
            if (tiedot.get(i).getIdNumero() == id) {
                tiedot.set(i, juomatieto);
                muutettu = true;
                return;
            }
        }
        lisaa(juomatieto);
    }
    
    
    /**
     * Tarkistetaan onko useammalla, kuin yhdellä juomalla sama ostopaikka
     * @param ostoId ostopaikan id
     * @return true, jos saman ostopaikan juomia on vähintään 2.
     */
    public boolean tarkistaPaikat(int ostoId) {
        int lkm = 0;
        for (var alkio : tiedot) {
            if ( alkio.getOstoId() == ostoId) lkm++;
        }
        return (lkm > 1);
    }
    
    
    /**
     * Poistetaan juomatieto
     * @param juomatieto poistettava juomatieto
     * @return true/false
     * @example
     * <pre name="test">
     *  Juomientiedot juomientiedot = new Juomientiedot();
     *  Juomatieto juoma21 = new Juomatieto(); juoma21.taytaViini();
     *  Juomatieto juoma11 = new Juomatieto(); juoma11.taytaViini();
     *  Juomatieto juoma22 = new Juomatieto(); juoma22.taytaViini();
     *  Juomatieto juoma12 = new Juomatieto(); juoma12.taytaViini();
     *  Juomatieto juoma23 = new Juomatieto(); juoma23.taytaViini();
     *  juomientiedot.lisaa(juoma21);
     *  juomientiedot.lisaa(juoma11);
     *  juomientiedot.lisaa(juoma22);
     *  juomientiedot.lisaa(juoma12);
     *  juomientiedot.poista(juoma23) === false; 
     *  juomientiedot.getLkm() === 4;
     *  juomientiedot.poista(juoma11) === true;   
     *  juomientiedot.getLkm() === 3;
     * </pre>
     */
    public boolean poista(Juomatieto juomatieto) {
        boolean ret = tiedot.remove(juomatieto);
        if (ret) muutettu = true;
        return ret;
    }   
    /*
     * Versio, jossa ruokajuomasta kutsutaan juomatiedon id-numeron perusteella.
     public boolean poista(int id) {
        boolean ret = false;
        for (int i = 0; i < tiedot.size(); i++) {
            if (tiedot.get(i).getIdNumero() == id) {
                tiedot.remove(tiedot.get(i));
                muutettu = true;
                ret = true;
            }    
        }
        return ret;
    }
     */
    
       
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
     *  Juomientiedot juomientiedot = new Juomientiedot();
     *  Juomatieto juomatieto1 = new Juomatieto(), juomatieto2 = new Juomatieto();
     *  juomatieto1.taytaViini();
     *  juomatieto2.taytaViini();
     *  String hakemisto = "testijuomat";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  juomientiedot.lueTiedostosta(tiedNimi); 
     *  juomientiedot.lisaa(juomatieto1);
     *  juomientiedot.lisaa(juomatieto2);
     *  juomientiedot.tallenna();
     *  juomientiedot = new Juomientiedot();            // Poistetaan vanhat luomalla uusi
     *  juomientiedot.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.

     *  juomientiedot.anna(0).compareTo(juomatieto1);
     *  juomientiedot.anna(1).compareTo(juomatieto2);
     *  juomientiedot.getLkm() === 2;
     *  
     *  juomientiedot.lisaa(juomatieto2);
     *  juomientiedot.tallenna();
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

                Juomatieto juomatieto = new Juomatieto();
                juomatieto.parse(rivi);
                lisaa(juomatieto);
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
     * Tallennetaan tiedot tekstimuodossa tiedostoon. Testit tiedoston lukemisen yhteydessä.
     * Tiedostoon otsikkoriveiksi:
     *  ";jid = juoman id"
     *  ";jid|juoman nimi|oid|hinta per pullo|apupaikka"
     */
    public void tallenna() {
        if (!muutettu) return; // TODO: versioseuranta jos tarpeen
        
        File fbackup = new File(getBakTiedostoNimi());
        File ftiedosto = new File(getTiedostoNimi());
        fbackup.delete();
        ftiedosto.renameTo(fbackup);
        
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftiedosto, true))) { //tarvitseeko getCanonicalPath??
            fo.println(";jid = juoman id");
            fo.println(";jid|juoman nimi|oid|hinta per pullo|apupaikka");
            
            for (int i = 0; i < tiedot.size(); i++) {
                fo.println(tiedot.get(i).toString());
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
        return this.tiedot.size();
    }
    
    
    /**
     * @param i kysyttävä indeksi
     * @return Palauttaa viitteen juomatietoon, joka on taulukossa paikassa i.
     */
    public Juomatieto anna(int i) {
        return this.tiedot.get(i);
    }
    
    
    /**
     * Haetaan tietyn id-numeron juomatieto
     * @param idNro id-numero
     * @return palauttaa juomantiedon, jonka id-nro on i
     * <pre name="test">
     * Juomientiedot juomientiedot = new Juomientiedot();
     * Juomatieto juomatieto1 = new Juomatieto(), juomatieto2 = new Juomatieto(), juomatieto3 = new Juomatieto();
     * juomatieto1.rekisteroi(); juomatieto2.rekisteroi(); juomatieto3.rekisteroi();
     * int id1 = juomatieto1.getIdNumero();
     * juomientiedot.lisaa(juomatieto1); juomientiedot.lisaa(juomatieto2); juomientiedot.lisaa(juomatieto3);
     * juomientiedot.annaTietoId(id1  ) == juomatieto1 === true;
     * juomientiedot.annaTietoId(id1+1) == juomatieto2 === true;
     * juomientiedot.annaTietoId(id1+2) == juomatieto3 === true;
     * </pre>
     */
    public Juomatieto annaTietoId(int idNro) {
        //Juomatieto tieto = new Juomatieto();
        for (var alkio : tiedot)
            if(alkio.getIdNumero() == idNro) return alkio;
        return null;
    }
    
    
    /**
     * Haetaan tietyn nimen perusteella id. Null jos ei ole
     * @param hakuehto nimi
     * @return palauttaa juomantiedon, jonka id-nro on i
     */
    public Juomatieto annaTietoNimella(String hakuehto) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
        Juomatieto uusi = new Juomatieto();
        for (var alkio : tiedot)
            if (WildChars.onkoSamat(alkio.getNimi(), ehto)) return uusi = alkio;
        return uusi;
    }
    
    
    /**
     * @param hakuehto nimi, jonka perusteella etsitään
     * @return lista löytyneistä alkioista
     * @example
     * <pre name="test">
     * #import java.util.*;
     *   Juomientiedot juomientiedot = new Juomientiedot();
     *   Juomatieto juomatieto1 = new Juomatieto(); juomatieto1.parse("1|Kaiken Malbec Arg|1|9.0|Alko");
     *   Juomatieto juomatieto2 = new Juomatieto(); juomatieto2.parse("2|Don David Malbec Arg|1|11.0|Alko");
     *   Juomatieto juomatieto3 = new Juomatieto(); juomatieto3.parse("3|Tsingtao olut Kiina|2|1.0|CM");
     *   Juomatieto juomatieto4 = new Juomatieto(); juomatieto4.parse("6|Punaviini|1|22.0|Alko");
     *   Juomatieto juomatieto5 = new Juomatieto(); juomatieto5.parse("7|Valkoviini|1|23.0|Aalko");
     *   juomientiedot.lisaa(juomatieto1); juomientiedot.lisaa(juomatieto2); juomientiedot.lisaa(juomatieto3); juomientiedot.lisaa(juomatieto4); juomientiedot.lisaa(juomatieto5);
     *   List<Juomatieto> loytyneet; 
     *   loytyneet = (List<Juomatieto>)juomientiedot.etsi("*c*"); 
     *   loytyneet.size() === 2; 
     *   loytyneet.get(0) == juomatieto2 === true; 
     *   loytyneet.get(1) == juomatieto1 === true; 
     *     
     *   loytyneet = (List<Juomatieto>)juomientiedot.etsi("*viini*"); 
     *   loytyneet.size() === 2; 
     *   loytyneet.get(0) == juomatieto4 === true; 
     *   loytyneet.get(1) == juomatieto5 === true;
     *     
     *   loytyneet = (List<Juomatieto>)juomientiedot.etsi(null); 
     *   loytyneet.size() === 5; 
     * </pre> 
     */
    public Collection<Juomatieto> etsi(String hakuehto) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto; //jos mitään ei ole hakukentässä, jää tähti voimaan
        List<Juomatieto> loytyneet = new ArrayList<Juomatieto>();   
        for (int i = 0; i < tiedot.size(); i++) {
            if (WildChars.onkoSamat(tiedot.get(i).getNimi(), ehto)) loytyneet.add(tiedot.get(i)); // etsitään ruokia vain nimen perusteella eikä muulle ole tarvetta
        }
        Collections.sort(loytyneet);
        return loytyneet; 
    }


    /**
     * @param hakuehto nimi, jonka perusteella etsitään
     * @return lista löytyneistä alkioista
     * @example
     * <pre name="test">
     * #import java.util.*;
     *   Juomientiedot juomientiedot = new Juomientiedot();
     *   Juomatieto juomatieto1 = new Juomatieto(); juomatieto1.parse("1|Kaiken Malbec Arg|1|9.0|Alko");
     *   Juomatieto juomatieto2 = new Juomatieto(); juomatieto2.parse("2|Don David Malbec Arg|1|11.0|Alko");
     *   Juomatieto juomatieto3 = new Juomatieto(); juomatieto3.parse("3|Tsingtao olut Kiina|2|1.0|CM");
     *   Juomatieto juomatieto4 = new Juomatieto(); juomatieto4.parse("6|Punaviini|1|22.0|Alko");
     *   Juomatieto juomatieto5 = new Juomatieto(); juomatieto5.parse("7|Valkoviini|1|23.0|Aalko");
     *   juomientiedot.lisaa(juomatieto1); juomientiedot.lisaa(juomatieto2); juomientiedot.lisaa(juomatieto3); juomientiedot.lisaa(juomatieto4); juomientiedot.lisaa(juomatieto5);
     *   List<Integer> loytyneet; 
     *   loytyneet = (List<Integer>)juomientiedot.etsiIdt("*c*"); 
     *   loytyneet.size() === 2; 
     *   loytyneet.get(0) == 1 === true; 
     *   loytyneet.get(1) == 2 === true; 
     *     
     *   loytyneet = (List<Integer>)juomientiedot.etsiIdt("*viini*"); 
     *   loytyneet.size() === 2; 
     *   loytyneet.get(0) == 6 === true; 
     *   loytyneet.get(1) == 7 === true;
     *     
     *   loytyneet = (List<Integer>)juomientiedot.etsiIdt(null); 
     *   loytyneet.size() === 5; 
     * </pre> 
     */
    public Collection<Integer> etsiIdt(String hakuehto) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto; //jos mitään ei ole hakukentässä, jää tähti voimaan
        Collection<Integer> loytyneet = new ArrayList<Integer>();   
        for (int i = 0; i < tiedot.size(); i++) {
            if (WildChars.onkoSamat(tiedot.get(i).getNimi(), ehto)) loytyneet.add(tiedot.get(i).getIdNumero()); // etsitään ruokia vain nimen perusteella eikä muulle ole tarvetta
        }
        return loytyneet; 
    }

    
    /**
     * Testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Juomientiedot juomientiedot = new Juomientiedot();
        
        Juomatieto punaviini = new Juomatieto();
        Juomatieto valkoviini = new Juomatieto();
    
        punaviini.rekisteroi();
        valkoviini.rekisteroi();

        punaviini.taytaViini();
        valkoviini.taytaViini();
        
        juomientiedot.lisaa(punaviini);
        juomientiedot.lisaa(valkoviini);
        
        System.out.println("========================= Testataan Juomientiedot-luokkaa ==========================");
        for (int i = 0; i < juomientiedot.getLkm(); i++) {
            Juomatieto Juomatieto = juomientiedot.anna(i);
            System.out.println("Juomatieto paikassa: " + i);
            Juomatieto.tulosta(System.out);
        }
   
        
    }
}


