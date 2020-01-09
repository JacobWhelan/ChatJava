
// Java implementation of  Server side 

import java.io.*;
import java.net.*;
import java.util.Vector;

// Server class 
public class Server {


    // vector for "Address book" of users
    static Vector<ClientHandler> addressBook = new Vector<>(); 


    public static void main(String[] args) throws IOException {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);
        System.out.println("Server running and looking for connections on port 5056...");

        // running infinite loop for getting client request
        while(true) {
            Socket socket = null;

            try {
                // socket object to receive incoming client requests
                socket = ss.accept();

                System.out.println("A new client is connected : " + socket);


                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new ClientHandler object
                ClientHandler CL = new ClientHandler(socket, dis, dos);

                // create a new thread object
                Thread t = new Thread(CL);

                // add user to Address Book
                addressBook.add(CL);

                // Invoking the start() method
                t.start();

            } catch (Exception e) {
                socket.close();
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler implements Runnable {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket socket;    
    private String username;

    public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public Socket getSocket() {
        return this.socket;
    }


    @Override
    public void run() {
        String received = "";
        String toSend = "";
            
        try {
                dos.writeUTF(toSend);
                dos.writeUTF("\nPlease enter your username: ");
                received = dis.readUTF();
                setUsername(received);
                dos.writeUTF("\nHello " + getUsername() + ", hope your day is going great!");
                received = dis.readUTF();

            while ( !(received.toLowerCase().equals("bye")) ) {
                toSend = getUsername() + ": " + received;
                for(ClientHandler user : Server.addressBook) {
                    if (user.getSocket() != getSocket()) {
                        user.dos.writeUTF(toSend);
                    }
                }
                received = dis.readUTF();
        
            }

            this.dos.close();
            this.dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
