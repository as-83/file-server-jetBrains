package server;

public class Request {
    private String command;
    private String option;
    private String fileName;
    private int fileId;
    private  int contentSize;

    private byte[] content = new byte[0];

    public Request(String request) {
        String [] requestParts = request.split(" ");
        command = requestParts[0];
        option = requestParts[1];
        if (option.equals("BY_NAME")) {
            fileName = requestParts[2];
        } else {
            fileId = Integer.parseInt(requestParts[2]);
            fileName = "";
        }

    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getContentSize() {
        return contentSize;
    }

    public void setContentSize(int contentSize) {
        this.contentSize = contentSize;
    }
}
