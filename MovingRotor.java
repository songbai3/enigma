package enigma;


/** Class that represents a rotating rotor in the enigma machine.
 *  @author Song
 */
class MovingRotor extends Rotor {

    /** The notches of this moving rotor. */
    private String _notches;

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;

        for (int i = 0; i < _notches.length(); i += 1) {
            if (!alphabet().contains(_notches.charAt(i))) {
                throw new EnigmaException("Notches must be in the alphabet.");
            }
        }
    }


    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i += 1) {
            if (alphabet().toChar(setting()) == _notches.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set(setting() + 1);
        setShouldMoveToFalse();
    }

    @Override
    public String toString() {
        return "Moving Rotor " + name();
    }

}
