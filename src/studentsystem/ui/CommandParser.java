package studentsystem.ui;

/**
 * Parses a raw input line into an array of tokens.
 *
 * <p>Tokens are separated by whitespace. Sequences enclosed in double quotes
 * are treated as a single token, allowing arguments that contain spaces
 * (e.g. {@code saveas "C:\My Files\data.xml"}).</p>
 */
public class CommandParser {

    /**
     * Splits the given input line into tokens.
     *
     * <ul>
     *   <li>Leading and trailing whitespace is ignored.</li>
     *   <li>Whitespace between tokens acts as a delimiter.</li>
     *   <li>Text enclosed in {@code "} double quotes forms a single token
     *       and may contain spaces; the quotes themselves are stripped.</li>
     * </ul>
     *
     * @param input raw input line from the user; may be {@code null} or blank
     * @return array of tokens, or an empty array if the input is blank
     */
    public String[] parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new String[0];
        }

        input = input.trim();
        java.util.List<String> tokens = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : input.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        return tokens.toArray(new String[0]);
    }
}
