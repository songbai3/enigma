package enigma;

import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Song
 * credits: Piazza for showing me how to determine how many rotors are fixed
 * without iterating through all the rotors and doing complicated stuff.
 * numrotors - numpawls.
 * I know, so trivial! yet I spent 2 hours thinking about it. :(
 *
 * Changed convert(MSG) to accept spaces in messages, delegate the
 * task of removing spaces and printing in 5-letter blocks
 * to printMessageLine in MAIN.
 *
 * maybe this is an autograder test I can't pass . Lmao.
 */

class Machine {

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The number of rotor slots I have. */
    private final int _numRotors;

    /** The number of pawls I have. */
    private final int _numPawls;

    /** All the rotors I can possibly use. (apparently I can't
     * cast this shit to Rotors[], hmmmmm. wtf intellij? */
    private Object[] _allRotors;

    /** The rotors I currently have in my rotor slots. */
    private Rotor[] _rotorsInUse;

    /** The permutation of my plugboard. */
    private Permutation _plugboard;


    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = allRotors.toArray();
        _rotorsInUse = new Rotor[numRotors];

        if (numRotors <= 1) {
            throw new EnigmaException("Must have more than 1 rotor slot");
        }

        if (pawls < 0 || pawls >= numRotors) {
            throw new EnigmaException("Number of pawls must be equal to or"
                    + " greater than 0 and lesser than number of rotors");
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != numRotors()) {
            throw new EnigmaException("Number of rotors to be inserted "
                    + "must be the same as number of rotor slots");
        }

        for (int i = 0; i < rotors.length; i += 1) {
            boolean rotorWasInserted = false;
            for (int j = 0; j < _allRotors.length; j += 1) {
                if ((rotors[i]).equals(((Rotor) _allRotors[j]).name())) {
                    _rotorsInUse[i] = (Rotor) _allRotors[j];
                    rotorWasInserted = true;
                }
            }
            if (!rotorWasInserted) {
                throw new EnigmaException("Name of rotor to be inserted "
                        + "must be the same as that of a rotor in "
                        + "the set of all rotors");
            }
            for (int j = 0; j < i; j += 1) {
                if (_rotorsInUse[j] == _rotorsInUse[i]) {
                    throw new EnigmaException("You can only use"
                            + " each rotor once.");
                }
            }
        }

        if (!_rotorsInUse[0].reflecting()) {
            throw new EnigmaException("First rotor must be a reflector");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != (numRotors() - 1)) {
            throw new EnigmaException("Setting must be a string of "
                    + "numRotors-1 characters");
        }

        for (int i = 0; i < setting.length(); i += 1) {
            char currentSetting = setting.charAt(i);
            if (!_alphabet.contains(currentSetting)) {
                throw new EnigmaException("Each character in Setting"
                        + " must be a character in the alphabet");
            }
            _rotorsInUse[i + 1].set(currentSetting);
        }
    }

    /** Set my ringstellung setting according to RINGSTELLUNGSETTING,
     * which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRingstellung(String ringstellungSetting) {
        if (ringstellungSetting.length() != (numRotors() - 1)) {
            throw new EnigmaException("Ringstellung must be a string of "
                    + "numRotors-1 characters");
        }

        for (int i = 0; i < ringstellungSetting.length(); i += 1) {
            char currentRingstellungSetting = ringstellungSetting.charAt(i);
            if (!_alphabet.contains(currentRingstellungSetting)) {
                throw new EnigmaException("Each character in Setting"
                        + "must be a character in the alphabet");
            }
            _rotorsInUse[i + 1].setRingstellung(
                    _alphabet.toInt(currentRingstellungSetting));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        if (c < 0 || c >= _alphabet.size()) {
            throw new EnigmaException("Integer must be in the range "
                    + "0..alphabet size - 1.");
        }

        _rotorsInUse[_rotorsInUse.length - 1].setShouldMoveToTrue();

        for (int i = (numRotors() - 1);
             i >= (numRotors() - numPawls() + 1); i -= 1) {
            if (_rotorsInUse[i - 1].rotates()
                    && _rotorsInUse[i].rotates()
                    && _rotorsInUse[i].atNotch()) {
                _rotorsInUse[i - 1].setShouldMoveToTrue();
                _rotorsInUse[i].setShouldMoveToTrue();
            }
        }

        for (Rotor rotor : _rotorsInUse) {
            if (rotor.shouldMove()) {
                rotor.advance();
            }
        }

        int result = _plugboard.permute(c);

        for (int i = (numRotors() - 1); i >= 0; i -= 1) {
            result = _rotorsInUse[i].convertForward(result);
        }

        for (int i = 1; i < numRotors(); i += 1) {
            result = _rotorsInUse[i].convertBackward(result);
        }

        result = _plugboard.invert(result);
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";

        for (int i = 0; i < msg.length(); i += 1) {
            char character = msg.charAt(i);
            if (character == ' ') {
                result += character;
            } else if (!_alphabet.contains(character)) {
                throw new EnigmaException("Character must be in the alphabet");
            } else {
                char convertedCharacter =
                        _alphabet.toChar(convert(_alphabet.toInt(character)));
                result += convertedCharacter;
            }

        }

        return result;
    }

}
