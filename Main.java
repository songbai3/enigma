package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Song
 * credits I looked at the Oracle documentation for what a Scanner is.
 * https://www.javatpoint.com/java-tokens
 * cs61a.org/study-guide/regex/ for regex stuff
 * https://stackoverflow.com/questions/37428441/too-many-characters-in-
 * character-literal-trying-to-check-if-my-value-is-not-wi
 * https://stackoverflow.com/questions/513832/how-do-i-compare-strings-in-java
 * https://stackoverflow.com/questions/6257316/illegal-escape-character
 * https://stackoverflow.com/questions/10317603/use-variables-in-pattern-matcher
 * piazza guy for clarifying cycles must be separated by spaces
 *  example: (cccc)(ccc) is not ok.
 *  https://stackoverflow.com/questions/23419087/stringutils-isblank-vs-
 *  string-isempty
 *
 *  notes:
 *  use hasNext to end scanner while loop from getting next thing
 *  use if statements to check for right format
 *  except for when wanna check type of next thing
 *
 *
 */

public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        if (!_input.hasNextLine()) {
            _output.println();
        } else {
            String settingLine = _input.nextLine();
            setUp(machine, settingLine);
            while (_input.hasNextLine()) {
                String currentLine = _input.nextLine();
                if (currentLine.isBlank()) {
                    _output.println();
                } else if (currentLine.contains("*")) {
                    setUp(machine, currentLine);
                } else {
                    printMessageLine(machine.convert(currentLine));
                    _output.println();
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            if (!_config.hasNext()) {
                throw new EnigmaException("There must be a configuration file");
            }

            String machineAlphabet = _config.next();
            if (machineAlphabet.contains("(")
                    || machineAlphabet.contains(")")
                    || machineAlphabet.contains("*")) {
                throw new EnigmaException("Alphabet must not "
                        + "contain '(',  ')' or '*'");
            }
            _alphabet = new Alphabet(machineAlphabet);

            if (!_config.hasNextInt()) {
                throw new EnigmaException("Number of rotors "
                        + "must be an integer");
            }
            int numRotors = _config.nextInt();

            if (!_config.hasNextInt()) {
                throw new EnigmaException("Number of pawls must be an integer");
            }
            int numPawls = _config.nextInt();

            ArrayList<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }

            return new Machine(_alphabet, numRotors, numPawls, allRotors);

        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            if (!_config.hasNext()) {
                throw new EnigmaException("No more rotors to be read.");
            }

            String rotorName = _config.next();
            if (rotorName.contains("(")
                    || rotorName.contains(")")) {
                throw new EnigmaException("Rotor name cannot "
                        + "contain '(' or ')'.");
            }

            String rotorTypeAndNotchLocations = _config.next();
            if (rotorTypeAndNotchLocations.contains("(")
                    || rotorTypeAndNotchLocations.contains(")")) {
                throw new EnigmaException("Rotor type and notch "
                        + "locations name cannot contain '(' or ')'.");
            }

            char rotorType = rotorTypeAndNotchLocations.charAt(0);
            if (rotorType != 'M'
                    && rotorType != 'N'
                    && rotorType != 'R') {
                throw new EnigmaException("Rotor type must be either M "
                        + "(moving), N (non-moving), or R (reflecting).");
            }

            String notchLocations = "";
            if (rotorTypeAndNotchLocations.length() > 1) {
                notchLocations = rotorTypeAndNotchLocations.substring(1);
            }
            for (int i = 0; i < notchLocations.length(); i += 1) {
                if (!_alphabet.contains(notchLocations.charAt(i))) {
                    throw new EnigmaException("Notches must be in alphabet");
                }
            }

            String cycles = "";
            while (_config.hasNext("[(].+[)]")) {
                String nextCycle = _config.next();
                nextCycle = nextCycle.replaceAll("[)][(]", ") (");
                cycles += nextCycle + " ";
            }

            Permutation rotorPermutation = new Permutation(cycles, _alphabet);

            if (rotorType == 'M') {
                return new MovingRotor(rotorName,
                        rotorPermutation, notchLocations);
            } else if (rotorType == 'N') {
                return new FixedRotor(rotorName, rotorPermutation);
            } else {
                return new Reflector(rotorName, rotorPermutation);
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner settingScanner = new Scanner(settings);

        if (!settingScanner.hasNext()) {
            throw new EnigmaException("Must have setting string.");
        }

        if (!settingScanner.hasNext("[*]")) {
            throw new EnigmaException("There must be a setting. "
                    + "Setting string must begin with a '*'");
        }
        settingScanner.next();

        String[] rotorsToInsert = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i += 1) {
            String currentRotor = settingScanner.next();
            rotorsToInsert[i] = currentRotor;
        }
        M.insertRotors(rotorsToInsert);

        String rotorSettingString = settingScanner.next();
        if (rotorSettingString.length() != M.numRotors() - 1) {
            throw new EnigmaException("Rotor setting string must contain"
                    + "numRotors-1 characters");
        }
        for (int i = 0; i < rotorSettingString.length(); i += 1) {
            if (!_alphabet.contains(rotorSettingString.charAt(i))) {
                throw new EnigmaException("Each character in the setting "
                        + "string must be in the alphabet");
            }
        }
        M.setRotors(rotorSettingString);

        if (settingScanner.hasNext("\\w+")) {
            String ringstellungSettingString = settingScanner.next();
            if (ringstellungSettingString.length()
                    != M.numRotors() - 1) {
                throw new EnigmaException("The ringstelling string must "
                        + "contain (numRotors - 1) characters");
            }
            for (int i = 0; i < ringstellungSettingString.length(); i += 1) {
                if (!_alphabet.contains(ringstellungSettingString.charAt(i))) {
                    throw new EnigmaException("Each character in the "
                            + "ringstellung string must be in the alphabet");
                }
            }
            M.setRingstellung(ringstellungSettingString);
        }

        String plugboardString = "";
        while (settingScanner.hasNext("[(].+[)]")) {
            String nextPlugboardCycle = settingScanner.next();
            nextPlugboardCycle = nextPlugboardCycle.replaceAll("[)][(]", ") (");
            plugboardString += nextPlugboardCycle + " ";
        }
        M.setPlugboard(new Permutation(plugboardString, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg = msg.replaceAll(" ", "");
        for (int i = 0; i < msg.length(); i += 1) {
            _output.print(msg.charAt(i));
            if (mod(i, 5) == 4) {
                if (i != (msg.length() - 1)) {
                    _output.print(" ");
                }
            }
        }
    }

    /** Return the value A modulo B. (I defined this myself). */
    int mod(int a, int b) {
        int r = a % b;
        if (r < 0) {
            r += b;
        }
        return r;
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
