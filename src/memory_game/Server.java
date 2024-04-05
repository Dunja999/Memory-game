package memory_game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private final static int port = 11111;
	private ServerSocket serverSocket;
	
	   public Server() throws IOException{
	    serverSocket = new ServerSocket(port);
		 System.out.println("Server osluskuje na portu: " + port);
		  execute();
	   }
	   
	   public void execute() {
		   
		   while(true) {		
				try {
					Socket prviIgrac = serverSocket.accept();
					Socket drugiIgrac = serverSocket.accept();
					
					new serverThread(prviIgrac, drugiIgrac).start();
					
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}	
		}

	   
	   }



