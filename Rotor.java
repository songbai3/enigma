package enigma;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Song
 * credits a dude on piazza said we could try overriding the
 * toString method. I think this might be helpful in debugging.
 */
class Rotor {

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The current setting of the rotor. */
    private int _setting;

    /** Boolean: if the rotor should move when keys are pressed. */
    private boolean _shouldMove;

    /** The current Ringstellung of the rotor. */
    private int _ringstellung;

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _ringstellung = 0;
        _shouldMove = false;
    }

    /** Return the value A modulo B. (I defined this myself). */
    int mod(int a, int b) {
        int r = a % b;
        if (r < 0) {
            r += b;
        }
        return r;
    }

    /** Return whether the rotor should move. */
    Boolean shouldMove() {
        return _shouldMove;
    }

    /** set shouldMove to true. */
    void setShouldMoveToTrue() {
        _shouldMove = true;
    }

    /** set shouldMove to false. */
    void setShouldMoveToFalse() {
        _shouldMove = false;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Return my current setting. */
    int ringstellung() {
        return _ringstellung;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = mod(posn, alphabet().size());
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        set(alphabet().toInt(cposn));
    }

    /** Set ringstellung() to character RINGSTELLUNG. */
    void setRingstellung(int ringstellung) {
        _ringstellung = ringstellung;
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        if (p < 0 || p >= size()) {
            throw new EnigmaException("Integer must be in the range "
                    + "0..alphabet size - 1.");
        }

        int thruEntryContact = mod(
                p + (setting() - ringstellung()),
                alphabet().size());
        int thruPermutation = permutation().permute(thruEntryContact);
        int thruExitContact = mod(
                thruPermutation - (setting() - ringstellung()),
                alphabet().size());
        return thruExitContact;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        if (e < 0 || e >= size()) {
            throw new EnigmaException("Integer must be in the range "
                    + "0..alphabet size - 1.");
        }

        int thruEntryContact = mod(
                e + (setting() - ringstellung()),
                alphabet().size());
        int thruPermutation = permutation().invert(thruEntryContact);
        int thruExitContact = mod(
                thruPermutation - (setting() - ringstellung()),
                alphabet().size());
        return thruExitContact;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

}
