package projectt3913;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class TlocClass {

    // Private constructor to prevent instantiation
    public TlocClass() {
        // No implementation needed
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Veuillez fournir le nom du fichier source Java.");
            System.exit(1);
        }

        String fileName = args[0];
        int result = computeTLOC(fileName);
        System.out.println(result); // Print the TLOC
    }

    public static int computeTLOC(String content) {
        int tloc = 0;
        boolean inBlockComment = false;
        String[] lines = content.split("\n");

        for (String line : lines) {
            line = line.trim(); // Remove leading and trailing whitespaces

            if (line.isEmpty()) {
                continue; // Skip empty lines
            }

            if (line.startsWith("/*")) {
                inBlockComment = true;
            }

            if (!inBlockComment && !line.startsWith("//")) {
                ++tloc; // Count line of code
            }

            if (inBlockComment && line.endsWith("*/")) {
                inBlockComment = false;
            }
        }

        return tloc;
    }
}
