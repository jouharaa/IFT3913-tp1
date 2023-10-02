package projectt3913;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class TropCompClass {
    private static double threshold = 0.1; // Default threshold value (50%)

    public static void main(String[] args) throws IOException {
        String inputDirectory = null;
        String outputFilePath = null;
        for (int i = 0; i < args.length; i++) {
            if ("-t".equals(args[i]) && i < args.length - 1) {
                try {
                    threshold = Double.parseDouble(args[i + 1]);
                    i++; // Increment i to skip the threshold value in the next iteration
                } catch (NumberFormatException e) {
                    System.err.println("Invalid threshold value: " + args[i + 1]);
                    System.exit(1);
                }
            } else if ("-o".equals(args[i]) && i < args.length - 1) {
                outputFilePath = args[i + 1];
                i++; // Increment i to skip the output file path in the next iteration
            } else {
                inputDirectory = args[i];
            }
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

                        double ratio = (double) tassertCount / tlocCount;

                        String packagePattern = "package\\s+([a-zA-Z][a-zA-Z0-9_]*\\.*?);";
                        Matcher packageMatcher = Pattern.compile(packagePattern).matcher(content);
                        String packageName = packageMatcher.find() ? packageMatcher.group(1) : "";

                        String className = path.getFileName().toString().replace(".java", "");

                        System.out.println(
                                "chemin du fichier,nom du paquet,nom de la classe,tloc de la classe,tAssert,tCMP,Ratio");

                        System.out.println(path.toAbsolutePath() + "," + packageName + "," + className + "," + tlocCount
                                + "," + tassertCount + "," + tcmp + "," + ratio);

                        if (ratio > threshold) {
                            System.out.println("Suspicious class with Ratio > " + threshold + "%");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
