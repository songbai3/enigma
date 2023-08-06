package enigma;


import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the MovingRotor class.
 *  @author Song
 */
public class MovingRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                         ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                         ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }

    Alphabet a1 = new Alphabet();

    Permutation p1 = new Permutation("(AELTPHQXRU) "
            + "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)", a1);

    Rotor r1 = new MovingRotor("r1", p1, "Q");

    @Test
    public void r1Test() {
        assertEquals(r1.rotates(), true);
        assertEquals(r1.reflecting(), false);
        assertEquals(r1.convertForward(0), 4);
        assertEquals(r1.convertBackward(10), 1);
        assertEquals(r1.convertForward(3), 5);
        assertEquals(r1.convertBackward(12), 2);
        assertEquals(r1.convertForward(18), 18);
        assertEquals(r1.convertBackward(18), 18);


        r1.set(3);


        assertEquals(r1.convertForward(0), 2);
        assertEquals(r1.convertBackward(2), 0);

        assertEquals(r1.convertForward(13), 20);
        assertEquals(r1.convertBackward(20), 13);

        assertEquals(r1.convertForward(24), 7);
        assertEquals(r1.convertBackward(7), 24);


        r1.set(15);
        r1.setRingstellung(7);


        assertEquals(r1.convertForward(0), 13);
        assertEquals(r1.convertBackward(13), 0);

        assertEquals(r1.convertForward(7), 25);
        assertEquals(r1.convertBackward(25), 7);

        assertEquals(r1.convertForward(22), 3);
        assertEquals(r1.convertBackward(3), 22);

    }

}
