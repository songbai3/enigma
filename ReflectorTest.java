package enigma;

import org.junit.Test;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Reflector class.
 *  @author Song
 */

public class ReflectorTest {

    Alphabet a1 = new Alphabet();

    Permutation p1 = new Permutation("(AE) (BN) (CK) (DQ) (FU) "
            + "(GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", a1);

    Rotor r1 = new Reflector("r1", p1);

    @Test
    public void r1Test() {
        assertEquals(r1.rotates(), false);
        assertEquals(r1.reflecting(), true);
        assertEquals(r1.permutation().derangement(), true);

        assertEquals(r1.convertForward(0), 4);
        assertEquals(r1.convertBackward(0), 4);
        assertEquals(r1.convertForward(4), 0);
        assertEquals(r1.convertBackward(4), 0);

        assertEquals(r1.convertForward(1), 13);
        assertEquals(r1.convertBackward(1), 13);
        assertEquals(r1.convertForward(13), 1);
        assertEquals(r1.convertBackward(13), 1);

        assertEquals(r1.convertForward(5), 20);
        assertEquals(r1.convertBackward(5), 20);
        assertEquals(r1.convertForward(20), 5);
        assertEquals(r1.convertBackward(20), 5);
    }

}
