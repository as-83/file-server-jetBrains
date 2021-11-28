package server;

import utils.FileUtil;
import utils.Response;
import utils.SerializationUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Server
public class Main {
    protected static final String PATH = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data";
   // protected static final String PATH = "D:" + File.separator + "sul"  + File.separator + "data" + File.separator;

    protected static final String SERIALIZATION_FILE  = "filesById.data";

    private static final int PORT = 23456;
    public static final String ADDRESS = "127.0.0.1";
    protected static Map<Integer, String> filesMap;
    protected volatile static boolean isStopped = false;
    public static void main(String[] args) {
        if (FileUtil.isFileExist(SERIALIZATION_FILE, PATH) && FileUtil.fileHasContent(SERIALIZATION_FILE, PATH)) {
            try {
                filesMap = (ConcurrentHashMap<Integer, String>) SerializationUtils.deserialize(PATH + File.separator + SERIALIZATION_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            filesMap = new ConcurrentHashMap<>();
        }


        System.out.println("Server started!");
        ExecutorService executor = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {

                while (!isStopped) {

                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(socket);
                    executor.submit(clientHandler);
                    Thread.sleep(50);

                }
            executor.shutdown();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}


