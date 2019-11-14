package ruokajajuoma;


import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
  * CRC-määritelmä:
 * Vastuualueet:
 *   - Huolehtii Ruoka- ja Juoma-luokkien yhteistyöstä ja välittää tietoja tarvittaessa. 
 *   - Lukee ja kirjoittaa tiedostoon pyytämällä apua avustajaltaan.
 * Avustajat:
 *   - Ruoka-luokka
 *   - Juoma-luokka
 *   - Ruoat-luokka
 *   - Juomat-luokka
 *   - Juomientiedot-luokka
 *   - Ostopaikat-luokka
 *   - Juomatieto-luokka
 *   - Ostopaikkatieto-luokka
 * @author Matti Parikka
 * @version 0.9, 7 May 2019
* Testien alustus
 * @example
 * <pre name="testJAVA">
 *  private Ruokajuoma ruokajuoma;
 *  private Ruoka ruoka1;
 *  private Ruoka ruoka2;
 *  private int rid1;
 *  private int rid2;
 *  private Juomatieto jt21;
 *  private Juomatieto jt11;
 *  private Juomatieto jt22;
 *  private Juomatieto jt12;
 *  private Juomatieto jt23;
 *  private Juoma jp1;
 *  private Juoma jp2;
 *  private Juoma jp3;
 *  private Ostopaikkatieto op1;
 *  private Ostopaikkatieto op2;
 * 
 *  public void alustaTestiin() {
 *    ruokajuoma = new Ruokajuoma();
 *    ruoka1 = new Ruoka(); ruoka1.taytaKala(); ruoka1.rekisteroi();
 *    ruoka2 = new Ruoka(); ruoka2.taytaKala(); ruoka2.rekisteroi();
 *    rid1 = ruoka1.getIdNumero();
 *    rid2 = ruoka2.getIdNumero();
 *    jt21 = new Juomatieto(); jt21.rekisteroi(); jt21.taytaViini();
 *    jt11 = new Juomatieto(); jt11.rekisteroi(); jt11.taytaViini();
 *    jt22 = new Juomatieto(); jt22.rekisteroi(); jt22.taytaViini();
 *    jt12 = new Juomatieto(); jt12.rekisteroi(); jt12.taytaViini();
 *    jt23 = new Juomatieto(); jt23.rekisteroi(); jt23.taytaViini();
 *    ruokajuoma.lisaa(ruoka1);
 *    ruokajuoma.lisaa(ruoka2);
 *    ruokajuoma.lisaa(jt21);
 *    ruokajuoma.lisaa(jt11);
 *    ruokajuoma.lisaa(jt22);
 *    ruokajuoma.lisaa(jt12);
 *    ruokajuoma.lisaa(jt23);
 *    jp1 = new Juoma(ruoka1.getIdNumero(), jt12.getIdNumero());
 *    jp2 = new Juoma(ruoka1.getIdNumero(), jt11.getIdNumero());
 *    jp3 = new Juoma(ruoka2.getIdNumero(), jt21.getIdNumero());
 *    ruokajuoma.lisaa(jp1);
 *    ruokajuoma.lisaa(jp2);
 *    ruokajuoma.lisaa(jp3);
 *    op1 = new Ostopaikkatieto(); op1.taytaPaikka(); op1.rekisteroi();
 *    op2 = new Ostopaikkatieto(); op2.taytaPaikka(); op2.rekisteroi();
 *  }
 * </pre>
 */
public class Ruokajuoma {

    private Ruoat ruoat = new Ruoat();
    private Juomientiedot juomientiedot = new Juomientiedot();
    private Juomat juomat = new Juomat();
    private Ostopaikat paikat = new Ostopaikat();
    
    
    /**
     * @return ruokien lukumäärä. Käytetään ruoat-luokan metodia apuna. 
     */
    public int getRuokienLkm() {
        return ruoat.getLkm();
    }
    
    
    /**
     * @return juomatietojen lukumäärä. Käytetään juomatiedot-luokan metodia apuna. 
     */
    public int getJuomatietojenLkm() {
        return juomientiedot.getLkm();
    }
    
    
    /**
     * @return juomien lukumäärä. Käytetään juomat-luokan metodia apuna. 
     */
    public int getJuomienLkm() {
        return juomat.getLkm();
    }
    
    
    /**
     * @param i halutun ruoan indeksi
     * @return viite halutun ruoan indeksiin
     * @throws IndexOutOfBoundsException poikkeus, jos pyydetään indeksiä, joka ei ole vielä käytössä
     */
    public Ruoka annaRuoka(int i) throws IndexOutOfBoundsException {
        return ruoat.anna(i);
    }
    
    
    /**
     * @param i halutun juomatiedon indeksi taulukossa
     * @return viite halutun juomatiedon indeksiin
     */
    public Juomatieto annaJuomatieto(int i)  {
        return juomientiedot.anna(i);
    }
    
    
    /**
     * @param i halutun juomaparin indeksi taulukossa
     * @return viite halutun juomaparin indeksiin
     */
    public Juoma annaJuomat(int i)  {
        return juomat.anna(i);
    }
    
    
    /**
     * Asetetaan tiedostojen oletusnimet.
     * @param nimi uusi nimi tiedostolle
     */
    
    public void setTiedostoNimet(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemisto = "";
        if (!nimi.isEmpty()) hakemisto = nimi + "/";
        ruoat.setTiedostonOletusNimi(hakemisto + "ruoka");
        juomientiedot.setTiedostonOletusNimi(hakemisto + "juomienTiedot");
        juomat.setTiedostonOletusNimi(hakemisto + "ruokienjuomat");
        paikat.setTiedostonOletusNimi(hakemisto + "ostopaikka");
    }
    
    
    /**
     * Luetaan tietoja tiedostosta. Tyhjennetään attribuuttien tiedot aluksi.
     * Testit jokaisen tallentavan ja lukevan luokan sisällä.
     */
    public void lueTiedostosta() {
        this.ruoat = new Ruoat();
        this.juomientiedot = new Juomientiedot();
        this.juomat = new Juomat();
        this.paikat = new Ostopaikat();
        
        setTiedostoNimet("dat"); 
        ruoat.lueTiedostosta();   // antaa avatessa kaikista FileNotFoundException, koska kansio on ensimmäisellä kerralla tyhjä.
        juomientiedot.lueTiedostosta();
        juomat.lueTiedostosta();
        paikat.lueTiedostosta();
        
    }
    
    
    /**
     * Tallentaa ohjelman tiedot tiedostoon.
     */
    public void tallenna() {
        
        //ehkä try catch jokaisen ympärille erikseen.
        ruoat.tallenna();
        juomientiedot.tallenna();
        juomat.tallenna();
        paikat.tallenna();
    }
    

    /**
     * @param ruoka ruoka, jonka juomat halutaan saada.
     * @return taulukko juomapareista
     * @example
     * <pre name="test">
     * #import java.util.*;
     *  alustaTestiin();
     *  Ruoka ruoka3 = new Ruoka();
     *  ruoka3.rekisteroi();
     *  ruokajuoma.lisaa(ruoka3);
     * 
     *  ArrayList<Juomatieto> loytyneet;
     *  loytyneet = ruokajuoma.annaJuomatRuoalle(ruoka3);
     *  loytyneet.size() === 0;
     *  loytyneet = ruokajuoma.annaJuomatRuoalle(ruoka1);
     *  loytyneet.size() === 2;
     *  loytyneet.get(0) == jt12 === true;
     *  loytyneet.get(1) == jt11 === true;
     *  loytyneet = ruokajuoma.annaJuomatRuoalle(ruoka2);
     *  loytyneet.size() === 1;
     *  loytyneet.get(0) == jt21 === true;
     * </pre>
     */
    public ArrayList<Juomatieto> annaJuomatRuoalle(Ruoka ruoka){
        ArrayList<Juomatieto> juomatiedot = new ArrayList<Juomatieto>();   
        ArrayList<Juoma> juoma = juomat.annaJuomatRuoalle(ruoka.getIdNumero());
        for (Juoma alkio : juoma) {
             juomatiedot.add(juomientiedot.annaTietoId(alkio.getJuomanId()));
        }
        return juomatiedot;
    }
    
    
    /**
     * @param juomatieto juomatieto, jonka ruokaparit halutaan saada.
     * @return taulukko juomapareista
     */
    public ArrayList<Ruoka> annaRuoatJuomalle(Juomatieto juomatieto){
        ArrayList<Ruoka> ruokalista = new ArrayList<Ruoka>();
        
        ArrayList<Juoma> juoma = juomat.annaRuoatJuomalle(juomatieto.getIdNumero());
                        // esim. getJuomanId => 1, 2, 56
        
        
        for (Juoma alkio : juoma) {
             ruokalista.add(ruoat.annaTietoId(alkio.getRuoanId()));
        }
        
        // uuteen taulukkoon lisätään viite juomatieto-olioon, jonka id-numero on sama, kuin taulukossa olevien olioiden juoma-idt
        return ruokalista;
    }
     

    
    /**
     * Palauttaa listan ruokaolioista. Ei sisällä duplikaatteja. 
     * @param juomatietoIdt lista juomatietojen id-numeroista
     * @return collection ruoka-olioista, jotka ovat linkkioliolla liitettynä annettuihin juomatieto id-numeroihin.
     */
    public Collection<Ruoka> annaRuoat(Collection<Integer> juomatietoIdt){
        List<Ruoka> ruokalista = new ArrayList<Ruoka>(); // tyhjä lista ruokia varten
        ArrayList<Juoma> juomaOliot = new ArrayList<Juoma>();      // tyhjä lista linkkiolioita varten
        
        ArrayList<Integer> ruokaIdt = new ArrayList<Integer>();
        for (Integer id : juomatietoIdt) {
            //boolean listalle = true; oltava sisemmässä silmukassa.
            juomaOliot = juomat.annaRuoatJuomalle(id); // jokaista juoma id:ä vastaavat oliot 
            for (Juoma pari : juomaOliot) {
                boolean listalle = true; // jokainen pari voi yrittää päästä listalle.
                if (ruokaIdt.size() == 0) { // jos koko on 0 voidaan ruokaId lisätä suoraan listalle.
                    ruokaIdt.add(pari.getRuoanId());
                    continue;
                }
                for (int i = 0; i < ruokaIdt.size(); i++) {
                    if (pari.getRuoanId() != ruokaIdt.get(i)) continue;
                    listalle = false;
                    break; //lähdetään silmukasta, koska löytyi jo ruoka, joka on ruokaId-listalla
                }
                if (listalle) ruokaIdt.add(pari.getRuoanId());
            }
        }
        // nyt pitäisi olla taulukollinen ruokaID inttejä, jotka ovat uniikkeja
        // tällöin ei tule montaa kertaa samoja ruokia listalle.
        for (Integer alkio : ruokaIdt) {
             ruokalista.add(ruoat.annaTietoId(alkio));
        }
        Collections.sort(ruokalista);
        return ruokalista;
    }
    
    /**
     * @param ruoka ruoka, jonka juomat halutaan saada.
     * @return taulukko juomapareista
    
    public ArrayList<Juoma> annaJuomatRuoalle(Ruoka ruoka){
        return juomat.annaJuomatRuoalle(ruoka.getIdNumero());
    }
     */
        
    
    /**
     * @param juomatieto juomatieto, jonka ruokaparit halutaan saada.
     * @return taulukko ruokapareista
    
    public ArrayList<Juoma> annaRuoatJuomalle(Juomatieto juomatieto){
        return juomat.annaJuomatRuoalle(juomatieto.getIdNumero());
    }
     */
    
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

     * </pre>
     */
    public void lisaa(Ruoka ruoka) {
        ruoat.lisaa(ruoka);
    }
    
    
    /**
     * @param juomatieto lisättävä juomatieto. 
     */
    public void lisaa(Juomatieto juomatieto) {
        juomientiedot.lisaa(juomatieto);
    }
    
    
    /**
     * @param juoma lisättävä juomapari. 
     */
    public void lisaa(Juoma juoma) {
        juomat.lisaa(juoma);
    }
    
    
    /**
     * @param ostopaikka lisättävä ostopaikka. 
     */
    public void lisaa(Ostopaikkatieto ostopaikka) {
        paikat.lisaa(ostopaikka);
    }
    
    
    /**
     * @param ruoka korvattava tai lisattava ruoka.
     * @example
     * <pre name="test">
     *  alustaTestiin();
     *  ruokajuoma.etsi("*",0).size() === 2;
     *  ruokajuoma.korvaaTaiLisaa(ruoka1);
     *  ruokajuoma.etsi("*",0).size() === 2;
     * </pre>
     */
    public void korvaaTaiLisaa(Ruoka ruoka) {
        ruoat.korvaaTaiLisaa(ruoka);
    }
    
    
    /**
     * @param juomatieto korvattava tai lisattava ruoka.
     */
    public void korvaaTaiLisaa(Juomatieto juomatieto) {
        String temp = juomatieto.getTempPaikka().trim(); //mahdollinen uusi ostopaikan nimi
        Ostopaikkatieto paikka = paikat.annaTietoNimella(temp); // null, jos nimellä ei löydy
                
        if (juomatieto.getOstoId() != 0) { // juomalle on joskus linkitetty jokin ostopaikka
            String olioPaikanNimi = annaOstopaikka(juomatieto).trim(); // linkitetyn paikan nimi
            if (temp.equalsIgnoreCase(olioPaikanNimi)) {
                juomientiedot.korvaaTaiLisaa(juomatieto);
                return; // nimi ei ole muuttunut eli käydään korvaamassa mahdollisesti muut muuttuneet tiedot ja jatketaan pois
            }            
            // on jokin olio, mutta sen nimi ei ole sama, kuin mukana tuotu nimi.
        }
        // onko nimi jonkin toisen olion nimi? Tarkistettu alussa.
        // jos paikka != null -> laitetaan tämän olioksi
        if (paikka != null) juomatieto.asetaOstopaikka(paikka.getIdNumero()); //temp nimellä löytyy ostopaikkaolio
        else {
            Ostopaikkatieto uusi = new Ostopaikkatieto();
            uusi.rekisteroi();
            paikat.lisaa(uusi);
            uusi.setNimi(temp);
            juomatieto.asetaOstopaikka(uusi.getIdNumero());
             // muuten tehdään uusi ostopaikka
        }
        juomientiedot.korvaaTaiLisaa(juomatieto);
        return;
        // ei ole vielä olemassa mitään linkitettyä oliota eli on tehtävä uusi. 
        // onko nimi jonkin toisen olion nimi? Tämä tarkistettu alussa
        // luodaan uusi ja laitetaan tämän olioksi
    }
    
    
    /**
     * Poistetaan yksi juoma vain valitulta ruoalta.
     * Poisto tapahtuu poistamalla juoma-linkkiolio valitun juomatiedon ja valitun ruoan väliltä
     * @param juomatieto juoma, joka poistetaan valitulta ruoalta
     * @param ruoka ruoka, jolta juoma poistetaan
     */
    public void poistaJuoma(Juomatieto juomatieto, Ruoka ruoka) {
        int jid = juomatieto.getIdNumero();
        int rid = ruoka.getIdNumero();
        juomat.poista(jid, rid);
    }
    
    
    /**
     * Poistetaan ruoka listalta. Poistetaan myös kaikkien juomien linkki tähän ruokaan poistamalla
     * linkkioliot, joiden ruokaId on tämä
     * @param ruoka poistettava ruoka
     * @return 1 jos onnistui, 0 jos ei.
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaTestiin();
     *   ruokajuoma.etsi("*",0).size() === 2;
     *   ruokajuoma.annaRuoatJuomalle(jt11).size() === 1;
     *   ruokajuoma.poista(ruoka1) === 1;
     *   ruokajuoma.etsi("*",0).size() === 1;
     *   ruokajuoma.annaRuoatJuomalle(jt11).size() === 0;
     *   ruokajuoma.annaRuoatJuomalle(jt21).size() === 1;
     * </pre>
     */
    public int poista(Ruoka ruoka) {
        if (ruoka == null) return 0;
        int tulos = ruoat.poista(ruoka.getIdNumero());
        juomat.poistaRuokaJuomilta(ruoka.getIdNumero());
        return tulos;
    }
    
    
    /**
     * Poistetaan juomatieto kaikilta ruoilta ja sen tiedot sisältävä olio.
     * @param juoma poistettava juomatieto
     * @return poistettavan id
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaTestiin();
     *   ruokajuoma.etsi("*",0).size() === 2;
     *   ruokajuoma.annaJuomatRuoalle(ruoka1).size() === 2;
     *   int tark = jt11.getIdNumero();
     *   ruokajuoma.poista(jt11) === tark; 
     *   ruokajuoma.etsi("*",0).size() === 2;
     *   ruokajuoma.annaJuomatRuoalle(ruoka1).size() === 1;
     *   ruokajuoma.annaJuomatRuoalle(ruoka2).size() === 1;
     * </pre>
     */
    public int poista(Juomatieto juoma) {
        if (juoma == null) return 0;
        
        //poistettava kaikki linkkioliot, joiden Juoma-ID on id
        int id = juoma.getIdNumero();       
        juomat.poistaJuomaRuoilta(id); 
        
        /*
         * Tarkistetaan mikä ostopaikka id poistettavalla juomalla oli.
         * Tarkistetaan onko muilla juomatiedoilla samaa osto id:ä eli oliko kauppa uniikki tapaus.
         * Jos oli, poistetaan ostopaikkatieto.
         */
        int oId = juoma.getOstoId();
        if (!juomientiedot.tarkistaPaikat(oId)) paikat.poista(oId);
        
        // poistetaan juomatiedot sisältävä olio
        juomientiedot.poista(juoma); 
        

         return id;
    }
    
    
    /**
     * Palauttaa aina collectionin ruokia, koska vain ruokia näytetään listauksen perusteella, riippumatta
     * hakuehdosta. Jos haetaan juomia, näytetään ne ruoat, joiden pari kyseinen juoma on.
     * @param nimi etsittävä nimi
     * @param k kenttä, jonka perusteella etsitään. 0 = ruoka, 1 = juoma
     * @return lista ruokia
     * @example
     * <pre name="test">
      * #import java.util.*;
     *   #THROWS CloneNotSupportedException
     *   alustaTestiin();
     *   Ruoka ruoka3 = new Ruoka(); ruoka3.rekisteroi();
     *   ruoka3.setNimi("Kanakeitto");
     *   ruokajuoma.lisaa(ruoka3);
     *   Collection<Ruoka> loytyneet = ruokajuoma.etsi("*keitto*",0);
     *   loytyneet.size() === 1;
     * </pre>
     */
    public Collection<Ruoka> etsi(String nimi, int k) {
        if (k == 0) return ruoat.etsi(nimi);
        
        /*
         * ArrayList<Ruoka> annaRuoatJuomalle(Juomatieto juomatieto)
         */
        //return annaRuoatJuomalle(juomientiedot.annaTietoNimella(nimi));
        return annaRuoat(juomientiedot.etsiIdt(nimi));
    }
    
    
    /**
     * @param nimi etsittävän juomatiedon nimi eli "juoman nimi"
     * @return collection löytyneistä juomatiedoista, jotka vastaavat annettua nimeä tai nimen osaa
     */
    public Collection<Juomatieto> etsiJuomat(String nimi){
        return juomientiedot.etsi(nimi);
    }
    
    
    /**
     * Tulostaa juomatietoja tietovirtaan.
     * @param juomatieto mikä juomatieto halutaan tulostaa
     * @param out tietovirta, johon tulostetaan.
     */
    public void tulostaJuomatiedot(Juomatieto juomatieto, PrintStream out) {
        juomatieto.tulosta(out);
        var paikka = paikat.annaTietoId(juomatieto.getOstoId());
        paikka.tulosta(out);
    }

    
    /**
     * Tulostetaan juomatietoja tietovirtaan.
     * @param juomatieto mikä juomatieto halutaan tulostaa
     * @param os tietovirta, johon tulostetaan
     */
    public void tulostaJuomatiedot(Juomatieto juomatieto, OutputStream os) {
        tulostaJuomatiedot(juomatieto, new PrintStream(os));
    }
    
    
    /**
     * Palautetaan jonkin juoman ostopaikan nimi tekstinä
     * @param juomatieto juomatieto, jonka ostopaikan nimi halutaan palauttaa
     * @return ostopaikan nimi
     */
    public String annaOstopaikka(Juomatieto juomatieto) {
        var paikka = paikat.annaTietoId(juomatieto.getOstoId());
        return paikka.getNimi();
    }
    
    
    /**
     * @param juomatieto juomatieto, jonka ostopaikkaolio palautetaan
     * @return olio
     */
    public Ostopaikkatieto annaOstoOlio(Juomatieto juomatieto) {
        var paikka = paikat.annaTietoId(juomatieto.getOstoId());
        return paikka;
    }
 
    
    /**
     * Mahdollisuus luoda juomatiedot GUIControllerin puolelta tällä metodilla. Vaatii kuitenkin Controlleriluokan muuttamisen atribuuttien osalta.
     * @param ruoka ruoka, jolle juomatieto luodaan
     * @return luotu juomatieto
     */
    public Juomatieto luoJuomatieto(Ruoka ruoka) {
        
        Ostopaikkatieto kauppa = new Ostopaikkatieto();
        kauppa.rekisteroi();
        kauppa.taytaPaikka(); // TODO: korvataan myöhemmin dialogivaihtoehdolla, jossa täytetään tiedot.
        lisaa(kauppa);
        
        Juomatieto juoma = new Juomatieto();
        juoma.rekisteroi();
        juoma.taytaViini();  // TODO: korvataan myöhemmin dialogivaihtoehdolla, jossa täytetään tiedot.
        juoma.asetaOstopaikka(kauppa.getIdNumero());
        lisaa(juoma);
        
        Juoma juomapari = new Juoma(ruoka.getIdNumero(), juoma.getIdNumero());
        lisaa(juomapari);
        return juoma;
    }
    
    
    /**
     * Testataan ruokajuomaluokkaa ja muiden luokkien toimintaa.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ruokajuoma ruokajuoma = new Ruokajuoma();
        
        System.out.println("=============================== Testataan Ruokajuoma-luokkaa, lisätään ruokia ====================================");
        Ruoka savukala = new Ruoka();
        Ruoka lakritsikala = new Ruoka();
    
        savukala.rekisteroi();
        lakritsikala.rekisteroi();

        savukala.taytaKala();
        lakritsikala.taytaKala();
        
        
        //try { //lisätty poikkeuskäsittelyn vuoksi
        ruokajuoma.lisaa(savukala);
        ruokajuoma.lisaa(lakritsikala);

        
        for (int i = 0; i < ruokajuoma.getRuokienLkm(); i++) {
            Ruoka ruoka = ruokajuoma.annaRuoka(i);
            System.out.println("  Ruoka paikassa: " + i);
            ruoka.tulosta(System.out);
        }
        /*
        } catch (SailoException ex) { //lisätty poikkeuskäsittelyn vuoksi
            System.out.println(ex.getMessage());
            }*/
        
        System.out.println("================================ Testataan Ruokajuoma-luokkaa, lisätään juomia ==================================");
        
        Juomatieto punaviini = new Juomatieto();
        Juomatieto valkoviini = new Juomatieto();
        
        punaviini.rekisteroi();
        valkoviini.rekisteroi();
        
        punaviini.taytaViini();
        valkoviini.taytaViini();
        
        //punaviini.tulosta(System.out);
        //valkoviini.tulosta(System.out);
        
        ruokajuoma.lisaa(punaviini);
        ruokajuoma.lisaa(valkoviini);
        
        for (int i = 0; i < ruokajuoma.getJuomatietojenLkm(); i++) {

            Juomatieto juomantieto = ruokajuoma.annaJuomatieto(i);
            System.out.println("  Juomatieto paikassa: " + i);
            juomantieto.tulosta(System.out);
        }
        
        System.out.println("============================= Testataan Ruokajuoma-luokkaa, lisätään juoma ruoalle ==============================");
        
        Juoma punaSavulle = new Juoma(savukala.getIdNumero(), punaviini.getIdNumero());
        //punaSavulle.luoPari(savukala.getIdNumero(), punaviini.getIdNumero());
        Juoma valkoSavulle = new Juoma(savukala.getIdNumero(), valkoviini.getIdNumero());
        //valkoSavulle.luoPari(savukala.getIdNumero(), valkoviini.getIdNumero());
        Juoma valkoLakritsille = new Juoma(lakritsikala.getIdNumero(), valkoviini.getIdNumero());
        //valkoLakritsille.luoPari(lakritsikala.getIdNumero(), valkoviini.getIdNumero()); //tehty oletusmuodostajalla, joka ei ota parametrejä.
        
        //punaSavulle.tulosta(System.out);
        //valkoSavulle.tulosta(System.out);
        //valkoLakritsille.tulosta(System.out);
        
        ruokajuoma.lisaa(punaSavulle);
        ruokajuoma.lisaa(valkoSavulle);
        ruokajuoma.lisaa(valkoLakritsille);
        
        for (int i = 0; i < ruokajuoma.getJuomienLkm(); i++) {

            Juoma juoma = ruokajuoma.annaJuomat(i);
            System.out.println("  Juoma paikassa: " + i);
            juoma.tulosta(System.out);
        }
        
        
        System.out.println("=================== Testataan Ruokajuoma-luokkaa, tulostetaan yhden ruoan juomaolion tiedot ======================");
        
        Juomat parit = new Juomat();
        //parit.lisaa(punaSavulle);
        //parit.lisaa(valkoSavulle);
        
        
        ArrayList<Juoma> savukalalle = parit.annaJuomatRuoalle(savukala.getIdNumero());
        
        for (Juoma alkio : savukalalle) alkio.tulosta(System.out);
        
        System.out.println("========================= Testataan Ruokajuoma-luokkaa, tulostetaan yhden ruoan juomat ==========================");
        System.out.println("Tulostetaan ruoat juomalle : " + savukala.getNimi());
        ArrayList<Juomatieto> parit2 = ruokajuoma.annaJuomatRuoalle(savukala);
        
       for (Juomatieto alkio : parit2) alkio.tulosta(System.out);
       
       System.out.println("========================= Testataan Ruokajuoma-luokkaa, tulostetaan yhden juoman ruoat ==========================");
       
       ArrayList<Ruoka> parit3 = ruokajuoma.annaRuoatJuomalle(valkoviini);
       System.out.println("Tulostetaan ruoat juomalle: " + valkoviini.getNimi());
       for (Ruoka alkio : parit3) alkio.tulosta(System.out);
       
       System.out.println("========================= Testataan Ruokajuoma-luokkaa, asetetaan ostopaikka juomalle ==========================");
       
       System.out.println("...luodaan uus ostopaikka...");
       Ostopaikkatieto cm = new Ostopaikkatieto();
       cm.rekisteroi();
       cm.taytaPaikka();
       
       cm.tulosta(System.out);
       ruokajuoma.lisaa(cm);
       
       System.out.println("...luodaan juomatieto ilman linkitettyä ostopaikkaa...");
       
       Juomatieto tsingtao = new Juomatieto();
       tsingtao.rekisteroi();
       tsingtao.taytaViini();
       tsingtao.tulosta(System.out);
       
       System.out.println("...liimataan ostopaikka ja tulostetaan juomatiedon kautta, oletuspaikka ei tulostu...");
       
       tsingtao.asetaOstopaikka(cm.getIdNumero());
       ruokajuoma.lisaa(tsingtao);
       tsingtao.tulosta(System.out);
       
       System.out.println("...tulostetaan ruokajuoman kautta, liimattu paikka tulostuu...");
       ruokajuoma.tulostaJuomatiedot(tsingtao, System.out);
          
    }

}
