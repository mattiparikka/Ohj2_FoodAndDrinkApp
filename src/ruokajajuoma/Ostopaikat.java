package ruokajajuoma;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Listaluokka, joka ylläpitää yksittäisiä ostopaikkatieto-olioita
 * CRC-määritelmä:
 * Vastuualueet:
 * - (Ei tiedä pääohjelmasta, ruoista, juomista, linkeistä tai käyttöliittymästä)                    
 * - Osaa etsiä ja lajitella
 * - Osaa tallentaa tiedostoon
 * - Pitää yllä varsinaista listaa ostopaikkaolioista
 * @author Matti Parikka
 * @version 0.9, 3 May 2019
 *
 */
public class Ostopaikat {

    private ArrayList<Ostopaikkatieto> tiedot = new ArrayList<Ostopaikkatieto>();  //käytetään fiksussa versiossa
    private String tiedostonOletusNimi = "ostopaikka";
    boolean muutettu = false;
    
    
    /**
     * Lisätään ostopaikkatieto taulukkoon.
     * @param ostopaikkatieto lisättävä ruokaolio
     * @example
     * <pre name="test">
     * Ostopaikat ostopaikat  = new Ostopaikat();
     * Ostopaikkatieto kauppaCM = new Ostopaikkatieto(), kauppaSM = new Ostopaikkatieto();
     * ostopaikat.getLkm() === 0;
     * ostopaikat.lisaa(kauppaCM); ostopaikat.getLkm() === 1;
     * ostopaikat.lisaa(kauppaSM); ostopaikat.getLkm() === 2;
     * ostopaikat.lisaa(kauppaCM); ostopaikat.getLkm() === 3;
     * ostopaikat.anna(0) === kauppaCM;
     * ostopaikat.anna(1) === kauppaSM;
     * ostopaikat.anna(2) === kauppaCM;
     * ostopaikat.anna(1) == kauppaCM === false;
     * ostopaikat.anna(1) == kauppaSM === true;
     * ostopaikat.lisaa(kauppaCM); ostopaikat.getLkm() === 4;
     * ostopaikat.lisaa(kauppaCM); ostopaikat.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Ostopaikkatieto ostopaikkatieto) {
        this.tiedot.add(ostopaikkatieto); 
        this.muutettu = true;
    }
    
    
    /**
     * Poistetaan ostopaikkatieto
     * @param id poistettava ostopaikkatieto
     * @return true/false
     * @example
     * <pre name="test">
     *  Ostopaikat ostopaikat = new Ostopaikat();
     *  Ostopaikkatieto paikka21 = new Ostopaikkatieto(); paikka21.rekisteroi();
     *  Ostopaikkatieto paikka11 = new Ostopaikkatieto(); paikka11.rekisteroi();
     *  Ostopaikkatieto paikka22 = new Ostopaikkatieto(); paikka22.rekisteroi();
     *  Ostopaikkatieto paikka12 = new Ostopaikkatieto(); paikka12.rekisteroi();
     *  Ostopaikkatieto paikka23 = new Ostopaikkatieto(); paikka23.rekisteroi();
     *  ostopaikat.lisaa(paikka21);
     *  ostopaikat.lisaa(paikka11);
     *  ostopaikat.lisaa(paikka22);
     *  ostopaikat.lisaa(paikka12);
     *  ostopaikat.poista(paikka23.getIdNumero()) === false; 
     *  ostopaikat.getLkm() === 4;
     *  ostopaikat.poista(paikka11.getIdNumero()) === true;   
     *  ostopaikat.getLkm() === 3;
     * </pre>
     */
    public boolean poista(int id) {
        boolean ret = false;
        for (int i = 0; i < tiedot.size(); i++) {
            if (tiedot.get(i).getIdNumero() == id) {
                tiedot.remove(tiedot.get(i));
                muutettu = true;
                ret = true;
                break; // vain yksi ostopaikkatieto voi vastata yhtä ID:ä, joten silmukkaa on löytymisen jälkeen turha jatkaa
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
     * Luetaan ruokien tiedot tiedostosta.
     * @param tiedosto luetun tiedoston nimi
     * @example
     * <pre name="test">
     * #import java.io.File;
     *  Ostopaikat ostopaikat = new Ostopaikat();
     *  Ostopaikkatieto ostopaikkatieto1 = new Ostopaikkatieto(), ostopaikkatieto2 = new Ostopaikkatieto();
     *  ostopaikkatieto1.taytaPaikka();
     *  ostopaikkatieto2.taytaPaikka();
     *  String hakemisto = "testipaikat";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  ostopaikat.lueTiedostosta(tiedNimi); 
     *  ostopaikat.lisaa(ostopaikkatieto1);
     *  ostopaikat.lisaa(ostopaikkatieto2);
     *  ostopaikat.tallenna();
     *  ostopaikat = new Ostopaikat();            // Poistetaan vanhat luomalla uusi
     *  ostopaikat.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.

     *  ostopaikat.anna(0).toString() === ostopaikkatieto1.toString();
     *  ostopaikat.anna(1).toString() === ostopaikkatieto2.toString();
     *  ostopaikat.getLkm() === 2;
     *  
     *  ostopaikat.lisaa(ostopaikkatieto2);
     *  ostopaikat.tallenna();
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

                Ostopaikkatieto ostopaikkatieto = new Ostopaikkatieto();
                ostopaikkatieto.parse(rivi);
                lisaa(ostopaikkatieto);
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
     * Tallennetaan tiedostoon. Testit lueTidostosta -metodin yhteydessä.
     */
    public void tallenna() {
        if (!muutettu) return; // TODO: versioseuranta
        
        File fbackup = new File(getBakTiedostoNimi());
        File ftiedosto = new File(getTiedostoNimi());
        fbackup.delete();
        ftiedosto.renameTo(fbackup);
        
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftiedosto, true))) { //tarvitseeko getCanonicalPath??
            fo.println(";oid = ostopaikan id");
            fo.println(";oid|ostopaikka");
            
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
     * @return Palauttaa viitteen ostopaikkatietoon, joka on taulukossa paikassa i.
     */
    public Ostopaikkatieto anna(int i) {
        return this.tiedot.get(i);
    }
    
    
    /**
     * Haetaan tietyn id-numeron ostopaikkatieto
     * @param idNro id-numero
     * @return palauttaa juomantiedon, jonka id-nro on i
     * <pre name="test">
     * Ostopaikat ostopaikat = new Ostopaikat();
     * Ostopaikkatieto ostopaikkatieto1 = new Ostopaikkatieto(), ostopaikkatieto2 = new Ostopaikkatieto(), ostopaikkatieto3 = new Ostopaikkatieto();
     * ostopaikkatieto1.rekisteroi(); ostopaikkatieto2.rekisteroi(); ostopaikkatieto3.rekisteroi();
     * int id1 = ostopaikkatieto1.getIdNumero();
     * ostopaikat.lisaa(ostopaikkatieto1); ostopaikat.lisaa(ostopaikkatieto2); ostopaikat.lisaa(ostopaikkatieto3);
     * ostopaikat.annaTietoId(id1  ) == ostopaikkatieto1 === true;
     * ostopaikat.annaTietoId(id1+1) == ostopaikkatieto2 === true;
     * ostopaikat.annaTietoId(id1+2) == ostopaikkatieto3 === true;
     * </pre>
     */
    public Ostopaikkatieto annaTietoId(int idNro) {
        //Ostopaikkatieto tieto = new Ostopaikkatieto();
        for (var alkio : tiedot)
            if(alkio.getIdNumero() == idNro) return alkio;//tieto = alkio;
        //return tieto;
        return null;
    }
    
    
    /**
     * Haetaan tietyn nimisen ostopaikkatiedon id. Null jos ei ole
     * @param nimi nimi
     * @return palauttaa juomantiedon, jonka id-nro on i
     */
    public Ostopaikkatieto annaTietoNimella(String nimi) {
        //Ostopaikkatieto tieto = new Ostopaikkatieto();
        for (var alkio : tiedot)
            if(nimi.equalsIgnoreCase(alkio.getNimi())) return alkio;//tieto = alkio;
        //return tieto;
        return null;
    }
    

    /**
     * Testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ostopaikat ostopaikat = new Ostopaikat();
        
        Ostopaikkatieto kauppaCM = new Ostopaikkatieto();
        Ostopaikkatieto kauppaSM = new Ostopaikkatieto();
    
        kauppaCM.rekisteroi();
        kauppaSM.rekisteroi();

        kauppaCM.taytaPaikka();
        kauppaSM.taytaPaikka();
        
        ostopaikat.lisaa(kauppaCM);
        ostopaikat.lisaa(kauppaSM);
        
        System.out.println("========================= Testataan Ostopaikat-luokkaa ==========================");
        for (int i = 0; i < ostopaikat.getLkm(); i++) {
            Ostopaikkatieto Ostopaikkatieto = ostopaikat.anna(i);
            System.out.println("Ostopaikkatieto paikassa: " + i);
            Ostopaikkatieto.tulosta(System.out);
        }
        
        System.out.println(ostopaikat.getLkm());
   
        
    }


}
