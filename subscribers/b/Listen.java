
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Listen extends Subscriber{
    public Listen(){
        super();
    }

    public void start(){
        try{
            this.connFd = new Socket(getServerAddress(),getServerPort());
            InputStream in = connFd.getInputStream();
            OutputStream out = connFd.getOutputStream();
            byte[] recvBuff = new byte[BUFFER_SIZE];
            byte[] sentBuff = new byte[BUFFER_SIZE];

            int n_read = 0, n_write = 0;
         //   System.out.println("Listen publish at " + connFd);

            // sent connect pkt to server
            n_write = PacketMessage.makeConnect(sentBuff,getClientID());
            out.write(sentBuff, 0, n_write);

           // System.out.println("Sent CONNECT");

            while(true){
                if( (n_read = in.read(recvBuff, 0, recvBuff.length)) != -1){
                    n_write = 0;
                    switch (PacketMessage.getMessageType(recvBuff)){
                        case 2: // connack
                            //System.out.println("Received CONNACK");
                            //System.out.println("Return code :" + PacketMessage.recvConnack(recvBuff));
                            break;
                        case 3: // publish
                            String[] msg = PacketMessage.recvPublish(recvBuff, n_read);
                            System.out.println("Topic name:" + msg[1]);
                            System.out.println("Payload:" + msg[2]);

                            n_write = PacketMessage.makePuback(sentBuff, getMessageID());
                            incMessageID();
                            break;
                        case 10:
                            System.out.print("disconnect");
                            System.exit(0);
                            return;
                    }
                    if( n_write > 0)
                        out.write(sentBuff, 0, n_write);
                }
            }

        } catch (SocketException ex){

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String argv[]){
        Listen listen = new Listen();
        listen.start();
    }
}
