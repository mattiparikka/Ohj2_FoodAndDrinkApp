package testiohjelmat;

/**
 * Satunnaisen kastikkeen luonti "taytaKala" rakennustelinealiohjelmalle.
 * @author matti
 * @version 28 Feb 2019
 *
 */
public class SatunnaisKastike {
    
    /**
     * Arvotaan satunnainen kokonaisluku halutulle välille.
     * @param ala arvotun luvun alaraja
     * @param yla arvotun luvun yläraja
     * @return satunnaisen kokonaisluvun.
     */
    public static double random(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return Math.round(n);
    }

}
