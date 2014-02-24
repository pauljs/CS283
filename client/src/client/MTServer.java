package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class MTServer {

	public static void main(String[] args) throws IOException{
		ServerSocket ss = new ServerSocket(4444);
		for(;;) { // infinite loop
			Socket cs = ss.accept();
			new WorkerThread(cs).start();
		}
	}
}
