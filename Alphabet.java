package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Song
 */
class Alphabet {

    /** Our alphabet. */
    private String characters;

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        characters = chars;

        for (int i = 0; i < chars.length(); i += 1) {
            char currentCharacter = chars.charAt(i);
            int currentCharacterFirstIndex = chars.indexOf(currentCharacter);
            int currentCharacterLastIndex = chars.lastIndexOf(currentCharacter);
            if (currentCharacterFirstIndex != currentCharacterLastIndex) {
                throw new EnigmaException("No character may be duplicated");
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return characters.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return characters.indexOf(ch) != -1;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw new EnigmaException("Character index must be"
                    + " greater or equal to 0 and smaller than"
                    + " the size of the alphabet");
        }
        return characters.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw new EnigmaException("Character must be in the alphabet");
        }
        return characters.indexOf(ch);
    }

}
