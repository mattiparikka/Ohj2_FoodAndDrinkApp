package ruokajajuoma.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import ruokajajuoma.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2019.05.04 12:11:10 // Generated by ComTest
 *
 */
@SuppressWarnings("all")
public class OstopaikkatietoTest {



  // Generated by ComTest BEGIN
  /** testRekisteroi32 */
  @Test
  public void testRekisteroi32() {    // Ostopaikkatieto: 32
    Ostopaikkatieto kauppaCM = new Ostopaikkatieto(); 
    assertEquals("From: Ostopaikkatieto line: 34", 0, kauppaCM.getIdNumero()); 
    kauppaCM.rekisteroi(); 
    kauppaCM.rekisteroi(); 
    Ostopaikkatieto kauppaSM = new Ostopaikkatieto(); 
    kauppaSM.rekisteroi(); 
    kauppaSM.rekisteroi(); 
    kauppaSM.rekisteroi(); 
    int n1 = kauppaCM.getIdNumero(); 
    int n2 = kauppaSM.getIdNumero(); 
    assertEquals("From: Ostopaikkatieto line: 43", n2-1, n1); 
  } // Generated by ComTest END
}