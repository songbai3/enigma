package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Song
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    Alphabet a1 = new Alphabet();

    Permutation p1 = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
            + "(DFG) (IV) (JZ) (S)", a1);
    Permutation p2 = new Permutation("(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)", a1);
    Permutation p3 = new Permutation("(AR) (BD) (CO) (EJ) (FN) (GT) (HK) "
            + "(IV) (LM) (PW) (QZ) (SX) (UY)", a1);

    @Test
    public void permuteIntTest() {
        assertEquals(p1.permute(0), 4);
        assertEquals(p1.permute(1), 10);
        assertEquals(p1.permute(24), 2);
        assertEquals(p1.permute(18), 18);
        assertEquals(p1.permute(8), 21);
        assertEquals(p1.permute(21), 8);
        assertEquals(p1.permute(3), 5);
    }

    @Test
    public void invertIntTest() {
        assertEquals(p1.invert(4), 0);
        assertEquals(p1.invert(10), 1);
        assertEquals(p1.invert(2), 24);
        assertEquals(p1.invert(18), 18);
        assertEquals(p1.invert(21), 8);
        assertEquals(p1.invert(8), 21);
        assertEquals(p1.invert(5), 3);
    }

    @Test
    public void permuteCharTest() {
        assertEquals(p1.permute('A'), 'E');
        assertEquals(p1.permute('B'), 'K');
        assertEquals(p1.permute('Y'), 'C');
        assertEquals(p1.permute('S'), 'S');
        assertEquals(p1.permute('I'), 'V');
        assertEquals(p1.permute('V'), 'I');
        assertEquals(p1.permute('D'), 'F');
    }

    @Test
    public void invertCharTest() {
        assertEquals(p1.invert('E'), 'A');
        assertEquals(p1.invert('K'), 'B');
        assertEquals(p1.invert('C'), 'Y');
        assertEquals(p1.invert('S'), 'S');
        assertEquals(p1.invert('V'), 'I');
        assertEquals(p1.invert('I'), 'V');
        assertEquals(p1.invert('F'), 'D');
    }

    @Test
    public void derangementTest() {
        assertEquals(p1.derangement(), false);
        assertEquals(p2.derangement(), true);
        assertEquals(p3.derangement(), true);
    }
}
