package projectt3913;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class TlsClass {
    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 3 || (args.length == 2 && !"-o".equals(args[0]))) {
            System.err.println("Usage: java TlsClass [-o output.csv] input_directory");
            System.exit(1);
        }

        String inputDirectory = null; // Initialize inputDirectory as null
        String outputFilePath = null;

        if (args.length == 1) {
            inputDirectory = args[0];
        } else if (args.length == 3 && "-o".equals(args[0])) {
            outputFilePath = args[1];
            inputDirectory = args[2];
        } else {
            System.err.println("Invalid command line arguments.");
            System.exit(1);
        }

        if (inputDirectory == null) {
            System.err.println("Input directory not specified.");
            System.exit(2);
        }

        if (outputFilePath != null) {
            try {
                // Redirect standard output to the specified file
                PrintStream fileStream = new PrintStream(new FileOutputStream(outputFilePath));
                System.setOut(fileStream);
            } catch (IOException e) {
                System.err.println("Error opening output file: " + e.getMessage());
                System.exit(3);
            }
        }

        analyzeJavaFilesInDirectory(inputDirectory);
    }

    public static void analyzeJavaFilesInDirectory(String inputDirectory) throws IOException {
        Path dirPath = Paths.get(inputDirectory);

        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            System.err.println("Input directory not found: " + inputDirectory);
            System.exit(4);
        }

        Files.walk(dirPath)
             .filter(path -> path.toString().endsWith(".java"))
             .forEach(path -> {
                 try {
                     String content = new String(Files.readAllBytes(path));
                     int tlocCount = TlocClass.computeTLOC(content);
                     int tassertCount = TassertClass.calculateTAssert(path.toString());
                     double tcmp = (double) tlocCount / tassertCount;

                     String packagePattern = "package\\s+([a-zA-Z][a-zA-Z0-9_]*\\.*?);";
                     Matcher packageMatcher = Pattern.compile(packagePattern).matcher(content);
                     String packageName = packageMatcher.find() ? packageMatcher.group(1) : "";

                     String className = path.getFileName().toString().replace(".java", "");

                     System.out.println("chemin du fichier,nom du paquet,nom de la classe,tloc de la classe,tAssert,tCMP");
                     System.out.println(path.toAbsolutePath() + "," + packageName + "," + className + "," + tlocCount + "," + tassertCount + "," + tcmp);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             });
    }
}
