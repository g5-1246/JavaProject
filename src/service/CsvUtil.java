package service;

import java.util.ArrayList;
import java.util.List;

final class CsvUtil {

    private CsvUtil() {
    }

    static String quote(String value) {

        if (value == null) {
            value = "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    static List<String> parse(String line) {

        List<String> values = new ArrayList<>();

        StringBuilder current = new StringBuilder();

        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {

            char ch = line.charAt(i);

            if (ch == '"') {

                if (quoted && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    current.append('"');
                    i++;

                } else {

                    quoted = !quoted;
                }

            } else if (ch == ',' && !quoted) {

                values.add(current.toString());
                current.setLength(0);

            } else {

                current.append(ch);
            }
        }

        values.add(current.toString());

        return values;
    }
}