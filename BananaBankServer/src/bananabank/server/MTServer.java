package bananabank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class MTServer {

	public static void main(String[] args) throws IOException{
		try {
			ServerSocket ss = new ServerSocket(4444);
			for(;;) { // infinite loop
				Socket cs = ss.accept();
				new WorkerThread(cs).start();
			}
		} catch(IOException e) {
			//we get here after the serverSocket is closed
		}
	}
}
