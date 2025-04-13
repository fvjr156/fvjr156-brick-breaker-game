import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class CodeDump {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get user input for the directory and output file name
        System.out.print("Enter the relative path to the directory containing .java files: ");
        String dirPath = scanner.nextLine();

        System.out.print("Enter the name of the output file (e.g., CombinedOutput.txt): ");
        String outputFileName = scanner.nextLine();

        Path directory = Paths.get(dirPath);
        Path outputFile = Paths.get(outputFileName);

        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            System.out.println("Error: The specified directory does not exist or is not a directory.");
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            Files.walk(directory)
                    .filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".java"))
                    .forEach(javaFile -> {
                        try {
                            writer.write("===== " + javaFile.getFileName() + " =====\n");
                            Files.lines(javaFile).forEach(line -> {
                                try {
                                    writer.write(line + "\n");
                                } catch (IOException e) {
                                    System.err.println("Error writing line from " + javaFile + ": " + e.getMessage());
                                }
                            });
                            writer.write("\n\n"); // Separate each file's content
                        } catch (IOException e) {
                            System.err.println("Error processing file " + javaFile + ": " + e.getMessage());
                        }
                    });

            System.out.println("Code dump completed. Output written to " + outputFile.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error creating/writing to output file: " + e.getMessage());
        }
    }
}
