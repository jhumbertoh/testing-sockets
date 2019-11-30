import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.nio.*;

public class Server
{
    private static Socket socket;

    public static void main(String[] args)
    {
        try
        {
            int port = 20140;
			String ip = "127.0.0.1";
            
			InetAddress bindAddr=null;
            if(!ip.equals("0.0.0.0"))
                bindAddr = InetAddress.getByName(ip);
            else
                bindAddr = null;
            ServerSocket serverSocket = new ServerSocket(port,1,bindAddr);
			
            socket = serverSocket.accept();
			
			System.out.println("> Server Started on Port: "+port);
			System.out.println("Connect Client: <"+socket.getInetAddress().getHostName()+" ["+socket.getInetAddress().getHostAddress()+"] >");
				
			InputStream is = socket.getInputStream();
            BufferedInputStream in = new BufferedInputStream(is);
			String rec = null;
 
			 //Sending the request to the client.			
			 String requestMessage = "Esta es una solicitud de mensaje";
			 byte[] buffer = requestMessage.getBytes();
			 byte[] data = packMessage(buffer);
			 
			 OutputStream os = socket.getOutputStream();
             /*OutputStreamWriter osw = new OutputStreamWriter(os);
             BufferedWriter bw = new BufferedWriter(osw);
			 bw.write(requestMessage);
             bw.flush();*/
			 os.write(data);
             os.flush();
			 
			 System.out.println("Message sent to the client is "+requestMessage);
			
            //Server is running always. This is done using this while(true) loop
			while(true)
            {
				rec = readInputStream(in);
				System.out.println("Message received from client is "+rec);
            }
        }
        catch (Exception e)
        {
			e.printStackTrace();
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(Exception e){}
        }
    }
	
	private static String readInputStream(BufferedInputStream _in)
    throws IOException {
        String data = "";
        int s = _in.read();
        if(s==-1)
            return null;
        data += ""+(char)s;
        int len = _in.available();
        System.out.println("Len got : "+len);
        if(len > 0) {
            byte[] byteData = new byte[len];
            _in.read(byteData);
            data += new String(byteData);
        }
        return data;
    }
	

	private static byte[] packMessage(byte[] buffer){
            int msgLenght = buffer.length;
            byte[] data = new byte[msgLenght + getHeaderLenght()];
            data[0] = (byte)(msgLenght / 256);
            data[1] = (byte)(msgLenght % 256);
			System.arraycopy(buffer, 0, data, getHeaderLenght(), msgLenght);
            return data;
    }
	
	private static int getHeaderLenght(){
		return 2;
	}
}
