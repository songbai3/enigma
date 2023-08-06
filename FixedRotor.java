package enigma;


/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Song
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }

    @Override
    public String toString() {
        return "Fixed Rotor " + name();
    }
}
