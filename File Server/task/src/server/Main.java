package server;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static Set<String> files = new HashSet<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean isRunning = true;
        while (isRunning) {
            String [] commands = scanner.nextLine().split(" ");
            switch (commands[0]){
                case "add": 
                    add(commands[1]); break;
                case "get":
                    get(commands[1]); break;
                case "delete":
                    delete(commands[1]); break;
                case "exit": 
                    isRunning = false; break;
                default:
                    System.out.println("Unknown command! Try again!");
            }

        }
    }

    private static void add(String fileName) {
        if (!fileName.matches("(file[1-9])||(file10)") || files.contains(fileName)) {
            System.out.println("Cannot add the file " + fileName);
        } else {
            files.add(fileName);
            System.out.println("The file " + fileName + " added successfully");
        }

    }

    private static void get(String fileName) {
        if (files.contains(fileName)) {
            System.out.println("The file " + fileName + " was sent");
        } else {
            System.out.println("The file " + fileName + " not found");
        }

    }

    private static void delete(String fileName) {
        if (files.contains(fileName)) {
            files.remove(fileName);
            System.out.println("The file " + fileName + " was deleted");
        } else {
            System.out.println("The file " + fileName + " not found");
        }

    }

}
