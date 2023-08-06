package enigma;

import org.junit.Test;

import static org.junit.Assert.*;


/** The suite of all JUnit tests for the Alphabet class.
 *  @author Song
 */
public class AlphabetTest {

    Alphabet a1 = new Alphabet();

    @Test
    public void sizeTest() {
        assertEquals(a1.size(), 26);
    }

    @Test
    public void containsTest() {
        assertEquals(a1.contains('A'), true);
        assertEquals(a1.contains('R'), true);
        assertEquals(a1.contains('S'), true);
        assertEquals(a1.contains('E'), true);
        assertEquals(a1.contains('h'), false);
        assertEquals(a1.contains('o'), false);
        assertEquals(a1.contains('l'), false);
        assertEquals(a1.contains('e'), false);
    }

    @Test
    public void toCharTest() {
        assertEquals(a1.toChar(0), 'A');
        assertEquals(a1.toChar(2), 'C');
        assertEquals(a1.toChar(15), 'P');
        assertEquals(a1.toChar(22), 'W');
    }

    @Test
    public void toIntTest() {
        assertEquals(a1.toInt('A'), 0);
        assertEquals(a1.toInt('F'), 5);
        assertEquals(a1.toInt('P'), 15);
        assertEquals(a1.toInt('W'), 22);

    }

}
