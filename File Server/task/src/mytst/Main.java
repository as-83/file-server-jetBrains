package mytst;

import utils.FileUtil;
import utils.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    protected static final String PATH = "D:" + File.separator + "sul"  + File.separator + "data" + File.separator;

    protected static final String SERIALIZATION_FILE  = "filesById.data";

    private static final int PORT = 23456;
    public static final String ADDRESS = "127.0.0.1";
    protected static Map<Integer, String> filesMap;


    public static void main(String[] args) {
        /*filesMap = new ConcurrentHashMap<>();
        filesMap.put(1, "one");
        filesMap.put(2, "two");
        filesMap.put(3, "three");
        filesMap.put(4, "four");
*/
        try {
            //SerializationUtils.serialize(filesMap, PATH +  File.separator  + SERIALIZATION_FILE);
            Map<Integer, String> desMap =
                    (ConcurrentHashMap) SerializationUtils.deserialize(PATH +  File.separator + SERIALIZATION_FILE);

            desMap.entrySet().forEach(e -> System.out.println(e.getKey() + "     " + e.getValue()));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }





    }
}
