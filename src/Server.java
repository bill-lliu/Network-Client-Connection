/* [ChatServer.java]
 * Server set up to connect multiple clients and allow them to chat with each other
 * @author Bill Liu and Harpal Mangat
 * April 15, 2019
 */

//imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//main class for the server
class ChatServer {
  
	ServerSocket serverSock;// server socket for connection
	static Boolean running = true;  // controls if the server is accepting clients
  
	/** Main
	 * @param args parameters from command line
	 */
	public static void main(String[] args) { 
		new ChatServer().go(); //start the server
	}
	
	/** Go
	 * Starts the server
	 */
	public void go() { 
		System.out.println("Waiting for a client connection..");
    
		Socket client = null;//hold the client connection
		
		try {
			serverSock = new ServerSocket(5000);  //assigns an port to the server
			serverSock.setSoTimeout(60000);  //the server will run for 60 seconds
			while(running) {  //this loops to accept multiple clients
				client = serverSock.accept();  //wait for connection
				System.out.println("Client " + client + " connected");
				//Note: you might want to keep references to all clients if you plan to broadcast messages
				//Also: Queues are good tools to buffer incoming/outgoing messages
				Thread t = new Thread(new ConnectionHandler(client)); //create a thread for the new client and pass in the socket
				t.start(); //start the new thread
				
				//added to the loop is a function that returns the message to all designated receivers
				for (each connection) {
					check if this connection deserves the message
						send message to that connection
				}
				
				
				
				
				
				
				
				
				
			}
		} catch(Exception e) { 
			//System.out.println("Error accepting connection");
			//close all and quit
			try {
				client.close();
			} catch (Exception e1) { 
				System.out.println("Failed to close socket");
			}
			System.exit(-1);
		}
	}
  
	//***** Inner class - thread for client connection
	class ConnectionHandler implements Runnable {
		private PrintWriter output; //assign printwriter to network stream
		private BufferedReader input; //Stream for network input
		private Socket client;  //keeps track of the client socket
		private boolean running;
		/* ConnectionHandler
		 * Constructor
		 * @param the socket belonging to this client connection
		 */    
		ConnectionHandler(Socket s) { 
			this.client = s;  //constructor assigns client to this    
			try {  //assign all connections to client
				this.output = new PrintWriter(client.getOutputStream());
				InputStreamReader stream = new InputStreamReader(client.getInputStream());
				this.input = new BufferedReader(stream);
			} catch(IOException e) {
				e.printStackTrace();        
			}
			running=true;
		} //end of constructor


		/* run
		 * executed on start of thread
		 */
		public void run() {  

			//Get a message from the client
			String msg="";
			while(running) {  // loop unit a message is received        
				try {
					if (input.ready()) { //check for an incoming message
						msg = input.readLine();  //get a message from the client
						output.println(msg); //echo the message back to the client ** This needs changing for multiple clients
						output.flush();             
						System.out.println(client + ": " + msg);
					}
				} catch (IOException e) { 
					System.out.println("Failed to receive msg from the client");
					e.printStackTrace();
				}
			}
			
			//Send a message to the client
			output.println("We got your message! Goodbye.");
			output.flush(); 
			
			//close the socket when the runtime of the server is complete
			try {
				input.close();
				output.close();
				client.close();
			} catch (Exception e) { 
				System.out.println("Failed to close socket");//if the socket does not close or if no clients connected within the time frame
			}
		} // end of run()
		
		
		
		
		
		/*read
		 * continually runs to check if a client has said anything, and adds it to a queue
		 */
		//public void read() {	
		//}
		
		
		
		
  	} //end of inner class
} //end of Class