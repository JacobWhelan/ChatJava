// Java implementation for a client  
  
import java.io.*; 
import java.net.*; 
import java.util.Scanner;

import com.sun.corba.se.spi.orbutil.fsm.Input; 
  
// Client class 
public class Client { 
    public static void main(String[] args) throws IOException  { 
        try { 
            Scanner input = new Scanner(System.in);
              
            // getting localhost ip 
            InetAddress ip = InetAddress.getByName("localhost");
      
            // establish the connection with server port 5056 
            Socket socket = new Socket(ip, 5056); 
      
            // obtaining input and out streams 
            DataInputStream dis = new DataInputStream(socket.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); 
      
            // Introduction with ClientHandler
            System.out.println(dis.readUTF());
            System.out.println(dis.readUTF());
            dos.writeUTF(input.nextLine());
            System.out.println(dis.readUTF());
            
            // send message thread
            Thread sendMessage = new Thread(new Runnable(){ 
            @Override
            public void run() { 
                while (true){ 
  
                    // read the message to deliver. 
                    String line = input.nextLine(); 
                      
                    try { 
                        // write on the output stream 
                        dos.writeUTF(line); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } 

                } 
            } 
        }); 
            
            // receive message thread 
        Thread receiveMessage = new Thread(new Runnable() { 
            @Override
            public void run() { 

                while (true) { 
                    try { 
                        // read the message sent to this client 
                        String msg = dis.readUTF(); 
                        System.out.println(msg); 
                    } catch (IOException e) {   
                        e.printStackTrace(); 
                    } 
                }
            } 
        }); 

            receiveMessage.start();
            sendMessage.start();

        }catch(Exception e){ 
            e.printStackTrace(); 
        } 
    } 

} 
