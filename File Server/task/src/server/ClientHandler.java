package server;

import utils.FileUtil;
import utils.Response;
import utils.SerializationUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

public class ClientHandler implements Runnable {
    private  final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
             DataInputStream input = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
        ) {
            String request = input.readUTF();
            if (request.startsWith("exit")) {
                Main.isStopped = true;
                SerializationUtils.serialize(Main.filesMap, Main.PATH + File.separator + Main.SERIALIZATION_FILE);

                clientSocket.close();
            } else {
                int fileSize = input.readInt();
                Request requestObj = new Request(request);
                requestObj.setContentSize(fileSize);
                byte content[] = new byte[fileSize];
                if (fileSize > 0) {
                    input.read(content, 0, fileSize);
                }
                requestObj.setContent(content);

                Response response;

                switch (requestObj.getCommand()) {
                    case "PUT":
                        response = saveFile(requestObj);
                        output.writeInt(response.getCode());
                        output.writeInt(response.getId());
                        output.writeInt(0);
                        break;
                    case "GET":
                        response = getFile(requestObj);
                        output.writeInt(response.getCode());
                        output.writeInt(response.getId());
                        output.writeInt(response.getContent().length);

                        if (response.getCode() == 200) {

                            output.write(response.getContent(), 0, response.getContent().length);
                        }
                        break;
                    case "DELETE":
                        response = deleteFile(requestObj);
                        output.writeInt(response.getCode());
                        output.writeInt(response.getId());
                        output.writeInt(0);
                        break;
                    default:
                        System.out.println("Unknown command! Try again!");
                }
                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static Response saveFile(Request request) throws IOException {
        Response response = new Response() ;

        if (Main.filesMap.containsValue(request.getFileName())) {
            response.setCode(403);
            response.setId(-1);
        } else {
            Main.filesMap.put(Main.filesMap.size() + 1, request.getFileName());
            SerializationUtils.serialize(Main.filesMap, Main.PATH + File.separator + Main.SERIALIZATION_FILE);
            FileUtil.saveDownloadedFile(Main.PATH, request.getFileName(), request.getContent());
            response.setCode(200);
            response.setId(Main.filesMap.size());
        }
        return response;

    }

    private static Response getFile(Request request) throws IOException {
        Response response = new Response();
        String fileName = request.getFileName();
        if (fileName.isEmpty() && Main.filesMap.containsKey(request.getFileId())) {
            fileName = Main.filesMap.get(request.getFileId());
        }

        if (Main.filesMap.containsValue(fileName)) {
            byte[] content = FileUtil.getFile(Main.PATH, fileName);
            response.setContent(content);
            response.setCode(200);

        } else {
            response.setCode(404);
            response.setContent(new byte[0]);
        }
        return response;
    }

    private static Response deleteFile(Request request) {
        Response response = new Response();
        response.setCode(404);
        String fileName = request.getFileName();
        if (request.getOption().equals("BY_ID") && Main.filesMap.containsKey(request.getFileId())) {
            fileName = Main.filesMap.get(request.getFileId());
        } else if ((request.getOption().equals("BY_NAME") && !Main.filesMap.containsValue(fileName))
                || (request.getOption().equals("BY_ID") && !Main.filesMap.containsKey(request.getFileId()))) {
            return response;
        }

        File file = new File(Main.PATH + File.separator + fileName);
        try {
            Files.delete(file.toPath());
            Main.filesMap.values().remove(fileName);
            SerializationUtils.serialize(Main.filesMap, Main.PATH + File.separator + Main.SERIALIZATION_FILE);
            response.setCode(200);
            response.setId(-1);
        } catch (IOException e) {
            response.setCode(404);
            response.setId(-1);
            e.printStackTrace();
        }

        return response;

    }
}
