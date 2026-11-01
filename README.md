# TINY Language Scanner (Lexical Analyzer)

A fast, lightweight, and robust Lexical Analyzer for the TINY programming language implemented in Java. It parses TINY source programs, removes comments and whitespace, tracks token positions, and categorizes tokens into a standardized token stream.

---

## Project Overview

The scanner reads raw TINY source code character-by-character using a deterministic finite-state machine (DFA) approach. 

- **Input:** Raw `.txt` TINY source file.
- **Output:** Formatted token output saved to a target file as `value, tokentype` (one token per line), with formatted live traces printed to standard output.

### Supported Language Specification

| Category | Elements / Patterns | Token Type |
| :--- | :--- | :--- |
| **Reserved Words** | `if`, `then`, `else`, `end`, `repeat`, `until`, `read`, `write` | `IF`, `THEN`, `ELSE`, `END`, `REPEAT`, `UNTIL`, `READ`, `WRITE` |
| **Special Symbols** | `+`, `-`, `*`, `/`, `=`, `<`, `(`, `)`, `;` | `PLUS`, `MINUS`, `MULT`, `DIV`, `EQUAL`, `LESSTHAN`, `OPENBRACKET`, `CLOSEDBRACKET`, `SEMICOLON` |
| **Assignment** | `:=` | `ASSIGN` |
| **Identifiers** | Sequences of letters (e.g., `fact`, `x`, `count`) | `IDENTIFIER` |
| **Numbers** | Sequences of digits (e.g., `0`, `42`, `100`) | `NUMBER` |
| **Comments** | Delimited by curly braces `{ ... }` | Stripped (Ignored) |
| **Whitespace** | Spaces, tabs, and newlines | Stripped (Ignored) |

---

## How to Run the Scanner

### Option 1: Native Windows Executable (No Java Installation Needed)
A pre-bundled standalone executable is located in `TinyScanner/`:

```cmd
cd TinyScanner
TinyScanner.exe <input_file_path> [output_file_path]
```

**Example:**
```cmd
TinyScanner.exe ..\test_files\test1.txt output.txt
```

> *Note:* If `[output_file_path]` is omitted, tokens are automatically saved to `output.txt`.

### Option 2: Standalone JAR File (Cross-Platform)
Requires Java 14+:

```cmd
java -jar tiny-scanner.jar <input_file_path> [output_file_path]
```

---

## Building the Project

An automated build script ([build.bat](build.bat)) is provided to compile the Java source, generate the JAR, and package a self-contained Windows executable image using JDK's `jpackage`.

### Requirements
- **JDK 14 or higher** (tested on JDK 23) with `javac`, `jar`, and `jpackage` in your system `PATH`.

### Build Command
Simply run:
```cmd
build.bat
```
or double-click `build.bat` in File Explorer.

The script performs 4 automated steps:
1. Cleans previous build artifacts.
2. Compiles `.java` sources from `src/main/java/org/project/` into `bin/`.
3. Packages an executable JAR into an isolated staging directory.
4. Generates the standalone `TinyScanner.exe` (with console subsystem enabled) into `TinyScanner/`.

---

## Project Structure

```text
Scanner/
│
├── build.bat                   # Automated build & packaging script
├── pom.xml                     # Maven configuration file
├── README.md                   # Project documentation
├── tiny-scanner.jar            # Packaged executable JAR
│
├── TinyScanner/                # Native Windows standalone deliverable
│   ├── TinyScanner.exe         # Console executable launcher
│   ├── app/                    # Packaged application JAR & config
│   └── runtime/                # Stripped, bundled Java runtime
│
├── src/main/java/org/project/  # Java source code
│   ├── Main.java               # CLI entry point, argument parsing & file I/O
│   ├── TinyScanner.java        # State machine scanner implementation
│   ├── Token.java              # Token model with line/column metadata
│   └── TokenType.java          # Token classification enum & lookup dictionary
│
└── test_files/                 # Verification test suite
    ├── test1.txt / output1.txt # Standard program (Factorial with IF-THEN-ELSE)
    ├── test2.txt / output2.txt # Comprehensive keyword & symbol coverage
    ├── test3.txt / output3.txt # Error test: Invalid character detection ($)
    ├── test4.txt / output4.txt # Error test: Malformed assignment detection (':')
    └── test5.txt / output5.txt # Error test: Unclosed comment detection ('{')
```
