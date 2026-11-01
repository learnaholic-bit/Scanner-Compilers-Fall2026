package org.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: Missing input file argument.");
            System.err.println("Usage: TinyScanner <input_file> [output_file]");
            return;
        }

        Path inputPath = Paths.get(args[0]);
        Path outputPath = Paths.get(args.length > 1 ? args[1] : "output.txt");

        if (!Files.exists(inputPath)) {
            System.err.println("Error: The file '" + inputPath.toAbsolutePath() + "' does not exist.");
            return;
        }

        try {
            String code = Files.readString(inputPath);
            TinyScanner scanner = new TinyScanner(code);
            List<String> fileLines = new ArrayList<>();

            System.out.println("TINY Scanner Started...");
            System.out.println("-------------------------------------------------");

            Token token;
            while (true) {
                token = scanner.getToken();

                System.out.println(token);

                if (token.getType() != TokenType.ENDFILE) {
                    fileLines.add(token.toFileOutput());
                }

                if (token.getType() == TokenType.ERROR) {
                    System.err.println("SCANNER ABORTED DUE TO LEXICAL ERROR.");
                    break;
                }

                if (token.getType() == TokenType.ENDFILE) {
                    break;
                }
            }

            Files.write(outputPath, fileLines);
            System.out.println("-------------------------------------------------");
            System.out.println("Tokens saved to: " + outputPath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("File processing error: " + e.getMessage());
        }
    }
}