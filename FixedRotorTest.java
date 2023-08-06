package enigma;


import org.junit.Test;

import static org.junit.Assert.*;


/** The suite of all JUnit tests for the FixedRotor class.
 *  @author Song
 */

public class FixedRotorTest {

    Alphabet a1 = new Alphabet();

    Permutation p1 = new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", a1);

    Rotor r1 = new FixedRotor("r1", p1);

    @Test
    public void r1Test() {
        assertEquals(r1.rotates(), false);
        assertEquals(r1.reflecting(), false);

        assertEquals(r1.convertForward(0), 11);
        assertEquals(r1.convertBackward(11), 0);

        assertEquals(r1.convertForward(4), 21);
        assertEquals(r1.convertBackward(21), 4);

        assertEquals(r1.convertForward(17), 0);
        assertEquals(r1.convertBackward(0), 17);

        assertEquals(r1.convertForward(7), 8);
        assertEquals(r1.convertBackward(8), 7);

        assertEquals(r1.convertForward(23), 7);
        assertEquals(r1.convertBackward(7), 23);
    }

}
