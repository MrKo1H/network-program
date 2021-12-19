
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Menu extends Subscriber{

    public  Menu(){
        super();
    }

    public static void main(String[] argv){
        Menu menu = new Menu();
        menu.start();
    }

    private void printMenu() {
        System.out.print("1.Subscribe\n2.Unsubscribe\n3.Quit\nEnter number:");
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            printMenu();
            int cmd = scanner.nextInt();
            switch (cmd) {
                case 1:
                    subscribe();
                    break;
                case 2:
                    unSubscribe();
                    break;
                case 3:
                    quit();
                    return;
                default:
                    System.out.println("Invalid Command");
            }
        }
    }

    private void quit(){
        int n;
        byte[] buff = new byte[BUFFER_SIZE];

        n = PacketMessage.makeQuit(buff, 0, getClientID());
        try {
            this.connFd = new Socket(getServerAddress(), getServerPort());
            OutputStream out  = connFd.getOutputStream();
            out.write(buff, 0, n);
            System.exit(0);
        } catch (SocketException ex){

        } catch (IOException ex){
            System.out.println(ex.fillInStackTrace());
        }
    }

    private void subscribe(){
        String sensor;
        String[] topics= new String[BUFFER_SIZE];
        int len = 0;
        while(true){
            System.out.println("Press @ for subscribe");
            Scanner inputUser =  new Scanner(System.in);
            System.out.print("Subscribe to topic :");
            sensor = inputUser.nextLine();
            if( sensor.equals("@"))
                break;
            topics[len++] = PacketMessage.makeTopic(getLocation(), sensor);
        }
        try{
            this.connFd = new Socket(getServerAddress(), getServerPort());
         //   System.out.println("Connectd to server :" + this.connFd);

            InputStream in = connFd.getInputStream();
            OutputStream out  = connFd.getOutputStream();
            byte[] buff = new byte[BUFFER_SIZE];
            int bytes;
            bytes = PacketMessage.makeSubscribe(buff,getMessageID(),getClientID(), topics, len);
            out.write(buff, 0, bytes);
            incMessageID();
       //     System.out.println("Sent SUBSCRIBE");
            if( (bytes= in.read(buff)) != -1){
       //         System.out.println("Received SUBACK");
            }
            this.connFd.close();


        } catch (SocketException ex){

        }catch (IOException ex){
            System.out.println(ex.fillInStackTrace());
        }
    }
    public void menu(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("1.Subscribe\n2.Unsubscribe\n3.Disconnect");
            int cmd = scanner.nextInt();
            switch (cmd){
                case 1:
                    subscribe();
                    break;
                case 2:
                    unSubscribe();
                    break;
                case 3:
                    quit();
                    break;
                default:
                    System.out.println("Invalid");
            }
        }
    }

    public void unSubscribe(){
        String sensor;
        String[] topics= new String[BUFFER_SIZE];
        int len = 0;
        System.out.println("Press @ for unsubscribe");
        Scanner inputUser =  new Scanner(System.in);
        System.out.print("Unsubscribe to topic :");
        sensor = inputUser.nextLine();
        if( sensor.equals("@"))
            return;
        topics[len++] = PacketMessage.makeTopic(getLocation(), sensor);
        try {
            this.connFd = new Socket(getServerAddress(), getServerPort());
           // System.out.println("Connectd to server :" + this.connFd);
            InputStream in = connFd.getInputStream();
            OutputStream out = connFd.getOutputStream();
            byte[] buff = new byte[BUFFER_SIZE];
            int bytes;
            bytes = PacketMessage.makeUnsubscribe(buff, getMessageID(), getClientID(), topics, len);
            out.write(buff, 0, bytes);
            incMessageID();
          //  System.out.println("Sent UNSUBSCRIBE");
            if ((bytes = in.read(buff)) != -1) {
          //      System.out.println("Received UNSUBACK");
                switch (buff[5]) {
                    case 1:
                        System.out.println("Unsubscribe fail");
                        break;
                    case 0:
                        System.out.println("Unsubscribe success");
                        break;
                }
            }
            this.connFd.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
