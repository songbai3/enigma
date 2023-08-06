package enigma;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;


/** The suite of all JUnit tests for the Machine class.
 *  @author Song
 *  credits: https://www.geeksforgeeks.org/java-util-arraylist-add-method
 *  -java/ for showing that you need a main function to create an arraylist
 *
 *  https://stackoverflow.com/questions/27553538/why-need-main-method-
 *  in-order-to-use-arraylist-methods-in-the-class
 */

public class MachineTest {

    @Test
    public void main() {
        Alphabet a1 = new Alphabet();

        Permutation p1 = new Permutation("(AE) (BN) (CK) (DQ) (FU) "
                + "(GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", a1);
        Permutation p2 = new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) "
                + "(HIX)", a1);
        Permutation p3 = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV) (JZ) (S)", a1);
        Permutation p4 = new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) "
                + "(BJ) (GR) (NT) (A) (Q)", a1);
        Permutation p5 = new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) "
                + "(N)", a1);

        Rotor r1 = new Reflector("B", p1);
        Rotor r2 = new FixedRotor("Beta", p2);
        Rotor r3 = new MovingRotor("I", p3, "Q");
        Rotor r4 = new MovingRotor("II", p4, "E");
        Rotor r5 = new MovingRotor("III", p5, "V");

        ArrayList<Rotor> rotorsArray = new ArrayList<Rotor>(5);

        rotorsArray.add(r1);
        rotorsArray.add(r2);
        rotorsArray.add(r3);
        rotorsArray.add(r4);
        rotorsArray.add(r5);

        Machine m1 = new Machine(a1, 5, 3, rotorsArray);

        String[] rotorNames = {"B", "Beta", "I", "II", "III"};
        m1.insertRotors(rotorNames);
        m1.setRotors("AAAA");

        Permutation plugboard = new Permutation("(TD) (KC) (JZ)", a1);
        m1.setPlugboard(plugboard);

        assertEquals(m1.convert("I WAS SCARED OF CODING IN JAVA"),
                "H GJN BOKDWA LB FKUCMU TJ ZUIO");
        assertEquals(m1.convert("I WAS SCARED OF USING GIT"),
                "X TYQ FBDZRG BY FZCAS YRU");
        assertEquals(m1.convert("AND STARTING ALL THESE PROJECTS"),
                "UAA FWOAGFKO CJG MUMOP CHTAVRSA");
    }
}



