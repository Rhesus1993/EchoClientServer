/**
 * Created by Riley Shelton on 10/2/2016.
 * Created by Josh Melton on 10/2/2016.
 */

import java.io.*;
import java.net.*;

//multi treaded echo java server

public class MultiThreadedEchoServer {

    ServerSocket myServerSocket;
    //defines the server as active
    boolean ServerOn = true;

    public MultiThreadedEchoServer() {
        //cerates a socket on the designated port
        try {
            myServerSocket = new ServerSocket(23657);
            System.out.println("Waiting for incoming connections....");
        }
        catch(IOException ioe) {
            System.out.println("Could not create server socket on port 23657. Quitting.");
            System.exit(-1);
        }

        //loop while the server is on accepts connections
        while(ServerOn) {
            try {
                // Accept incoming connections.
                Socket clientSocket = myServerSocket.accept();
                // Start a Service thread
                ClientServiceThread clientThread = new ClientServiceThread(clientSocket);
                clientThread.start();
            }
            catch(IOException ioe) {
                System.out.println("Exception encountered on accept.");
                ioe.printStackTrace();
            }
        }
    }
    public static void main (String[] args) {
        new MultiThreadedEchoServer();
    }

    class ClientServiceThread extends Thread {
        Socket myClientSocket;
        boolean Running = true;

        ClientServiceThread(Socket socket) {
            myClientSocket = socket;

        }

        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;

            // Print out details of this connection
            System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());

            try {
                in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));

                while(Running) {
                    // read incoming stream
                    String clientCommand = in.readLine();
                    System.out.println("Client Says : " + clientCommand);

                    if(clientCommand.equalsIgnoreCase("q")) {
                        // Special command. Quit this thread
                        Running = false;
                        System.out.print("Stopping client thread for client : ");
                    }  else {
                        // Process it
                        out.println(clientCommand);
                        out.flush();
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                // Clean up
                try {
                    in.close();
                    out.close();
                    myClientSocket.close();
                    System.out.println("...Stopped");
                }
                catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
}