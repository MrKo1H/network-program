
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Subscriber{
    public  static int          BUFFER_SIZE = 1024;
    private static int          messageID = 0;
    private static String       clientID ;
    private static String       serverAddress;
    private static int          serverPort;
    private static String       location;

    protected Socket              connFd;

    public Subscriber(){
        loadConfig();
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getClientID() {
        return clientID;
    }

    public int getMessageID(){
        return messageID;
    }

    public void incMessageID(){
        messageID++;
    }

    public static int getBufferSize() {
        return BUFFER_SIZE;
    }

    public static String getLocation() {
        return location;
    }

    public void start(){};

    private void loadConfig(){
        try {
            Scanner scanner = new Scanner(new FileInputStream(new File("config")));
            Map<String, String> map = new HashMap<String ,String>();
            String key, value;
            while(scanner.hasNextLine()){
                key = scanner.next();
                value = scanner.next();
                map.put(key,value);
            }
            Subscriber.serverAddress = map.get("address");
            Subscriber.serverPort = Integer.parseInt(map.get("port"));
            Subscriber.clientID = map.get("client_id");
            Subscriber.location = map.get("location");

        } catch (FileNotFoundException ex){
            System.out.println(ex.fillInStackTrace());
            System.exit(1);
        }
    }
}
