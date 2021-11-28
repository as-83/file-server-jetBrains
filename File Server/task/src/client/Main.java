package client;

import utils.FileUtil;
import utils.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    private static final int PORT = 23456;
    private static final String ADDRESS = "127.0.0.1";
    private static Scanner scanner = new Scanner(System.in);
    private static final String PATH = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "client" +
            File.separator + "data";
 //   private static final String PATH = "D:" + File.separator + "sul" + File.separator + "data" + File.separator + "client" + File.separator;



    public static void main(String[] args) {

        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        String option = scanner.nextLine();

        if (option.equals("exit")) {
            doRequest("exit", new byte[0]);
        } else {
            try {
                switch (option) {
                    case "1":
                        getFileFromServer();
                        break;
                    case "2":
                        saveFileOnServer();
                        break;
                    case "3":
                        deleteFileOnServer();
                        break;
                    default:
                        System.out.println("Incorrect input");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static void getFileFromServer() throws IOException {

        String request = "GET "+ getFileSearchingParams();

        Response response = doRequest(request, new byte[0]);//empty array
        if (response.getCode() == 200) {
            System.out.print("The file was downloaded! Specify a name for it: ");
            String name = scanner.nextLine();
            FileUtil.saveDownloadedFile(PATH, name, response.getContent());

        } else if (response.getCode() == 404) {
            System.out.println("The response says that this file is not found!");
        }

    }

    private static String getFileSearchingParams() {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        int nameOrId = Integer.parseInt(scanner.nextLine());
        String option = "BY_NAME";
        if (nameOrId == 1) {
            System.out.print("Enter filename: ");
        } else {
            System.out.print("Enter id: ");
            option = "BY_ID";
        }
        String fileNameOrId = scanner.nextLine();

        return  option + " " + fileNameOrId;

    }


    private static void saveFileOnServer() throws IOException {

        System.out.print("Enter name of the file: ");
        String fileToSand = scanner.nextLine();

        System.out.print("Enter name of the file to be saved on server: ");
        String newFileName = scanner.nextLine();
        if (newFileName.isEmpty()) {
            newFileName = fileToSand;
        }

        Response response = doRequest("PUT BY_NAME " + newFileName, FileUtil.getFile(PATH, fileToSand));
        if (response.getCode() == 200) {
            System.out.println("Response says that file is saved! ID = " + response.getId());
        } else if (response.getCode() == 403) {
            System.out.println("The response says that creating the file was forbidden!");
        }
    }

    private static void deleteFileOnServer() {
        String request = "DELETE "+ getFileSearchingParams();
        Response response = doRequest(request, new byte[0]);

        if (response.getCode() == 200) {
            System.out.println("The response says that this file was deleted successfully!");

        } else if (response.getCode() == 404) {
            System.out.println("The response says that this file is not found!");
        }
    }

    private static Response doRequest(String request, byte[] data) {
        Response response  = new Response(400, 0, new byte[0]);

        try( Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ) {

            outputStream.writeUTF(request);
            outputStream.writeInt(data.length);
            if (data.length > 0) {
                outputStream.write(data);
            }
            System.out.println("The request was sent.");
            if (!request.startsWith("exit")) {
                int code = inputStream.readInt();
                //inputStream.readUTF();
                int id = inputStream.readInt();
                //inputStream.readUTF();
                int inputDataSize = inputStream.readInt();
                //inputStream.readUTF();
                byte[] inputData = new byte[inputDataSize];

                if (inputDataSize > 0) {
                    inputStream.read(inputData, 0, inputDataSize);
                }
                response = new Response(code, id, inputData);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }
}
