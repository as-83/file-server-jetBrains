package utils;

public class Response {
    private int code;
    private int id;
    private byte[] content;

    public Response() {
    }

    public Response(int code, int id, byte[] content) {
        this.code = code;
        this.id = id;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
