package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static Set<String> files = new HashSet<>();
    static Scanner scanner = new Scanner(System.in);
    private static final int PORT = 23456;
    public static final String ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
        System.out.println("Server started!");
        interactWithUser(); //stage 1 implementation
        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            try (Socket socket = serverSocket.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                 ) {

                String inputString = input.readUTF();
                System.out.println("Received: " + inputString);
                output.writeUTF("All files were sent!");
                System.out.println("Sent: All files were sent!");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void interactWithUser() {
        boolean isStopped = true;
        while (!isStopped) {
            String [] commands = scanner.nextLine().split(" ");
            switch (commands[0]){
                case "add":
                    add(commands[1]); break;
                case "get":
                    get(commands[1]); break;
                case "delete":
                    delete(commands[1]); break;
                case "exit":
                    isStopped = false; break;
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
