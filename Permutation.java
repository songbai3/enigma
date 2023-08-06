package enigma;


/** Represents a permutation of a range of integers starting
 * at 0 corresponding to the characters of an alphabet.
 *  @author Song
 * credits: Thank you to Mr. Todd Yu (TA) for explaining how
 * I can put the cycle into a string[] as individual strings
 * and how to replace characters in strings.
 * https://www.geeksforgeeks.org/split-string-java-examples/
 */
class Permutation {

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private String[] _cycles;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String temporaryCycles = cycles.replaceAll("[()]", "");
        _cycles = temporaryCycles.split(" ");
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String[] temporaryCycles = new String[_cycles.length + 1];
        System.arraycopy(_cycles, 0, temporaryCycles, 0, _cycles.length);
        temporaryCycles[temporaryCycles.length - 1] = cycle;
        _cycles = temporaryCycles;
    }

    /** Return the value A modulo B. (I defined this myself). */
    public int mod(int a, int b) {
        int r = a % b;
        if (r < 0) {
            r += b;
        }
        return r;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char inputCharacter = alphabet().toChar(wrap(p));
        for (int i = 0; i < _cycles.length; i += 1) {
            for (int j = 0; j < _cycles[i].length(); j += 1) {
                if (inputCharacter == _cycles[i].charAt(j)) {
                    char returnCharacter = _cycles[i].charAt
                            (mod((j + 1), _cycles[i].length()));
                    return alphabet().toInt(returnCharacter);
                }
            }
        }

        return wrap(p);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char inputCharacter = alphabet().toChar(wrap(c));
        for (int i = 0; i < _cycles.length; i += 1) {
            for (int j = 0; j < _cycles[i].length(); j += 1) {
                if (inputCharacter == _cycles[i].charAt(j)) {
                    char returnCharacter = _cycles[i].charAt
                            (mod((j - 1), _cycles[i].length()));
                    return alphabet().toInt(returnCharacter);
                }
            }
        }

        return wrap(c);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return alphabet().toChar(permute(alphabet().toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return alphabet().toChar(invert(alphabet().toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int numberOfCharactersInCycles = 0;
        for (String cycle : _cycles) {
            if (cycle.length() >= 2) {
                numberOfCharactersInCycles += cycle.length();
            }
        }
        return numberOfCharactersInCycles == alphabet().size();
    }

}
