package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * Test suite kerho-ohjelmalle
 * @author vesal
 * @version 3.1.2019
 */
@RunWith(Suite.class)
@SuiteClasses({
    ruokajajuoma.test.JuomatietoTest.class,
    ruokajajuoma.test.JuomatTest.class,
    ruokajajuoma.test.JuomientiedotTest.class,
    ruokajajuoma.test.OstopaikatTest.class,
    ruokajajuoma.test.OstopaikkatietoTest.class,
    ruokajajuoma.test.RuoatTest.class,    
    ruokajajuoma.test.RuokajuomaTest.class,
    ruokajajuoma.test.RuokaTest.class
    })



public class AllTests {
    //
}
