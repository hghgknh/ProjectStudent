package studentsystem.ui;

public class CommandParser {
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
