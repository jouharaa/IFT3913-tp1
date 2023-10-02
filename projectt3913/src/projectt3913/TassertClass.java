package projectt3913;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class TassertClass {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TASSERT <path-to-java-test-file>");
            System.exit(1);
        }

        String filePath = args[0];
        try {
            int tAssert = calculateTAssert(filePath);
            System.out.println(tAssert);
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier: " + e.getMessage());
            System.exit(1);
        }
    }

    public static int calculateTAssert(String filePath) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                count += countAssertionsInLine(line);
            }
        }
        return count;
    }

    public static int countAssertionsInLine(String line) {
        String[] assertions = {
                "assertEquals", "assertFalse", "assertTrue",
                "assertNotNull", "assertNull", "assertSame",
                "assertNotSame", "assertArrayEquals", "assertThrows", "fail", "assertNotEquals"
        };

        int count = 0;
        for (String assertion : assertions) {
            Pattern pattern = Pattern.compile("\\b" + assertion + "\\b");
            if (pattern.matcher(line).find()) count++;
        }
        return count;
    }
};
