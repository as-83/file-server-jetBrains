package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    private static final int PORT = 23456;
    public static final String ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
        System.out.println("Client started!");
       try( Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            ) {


           outputStream.writeUTF("Give me everything you have!");
           System.out.println("Sent: Give me everything you have!");

           String serverAnswer = inputStream.readUTF();
           System.out.println("Received: " + serverAnswer);


       } catch (UnknownHostException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }

    }
}
