package bananabank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class MTServer {

	public static void main(String[] args) throws IOException{
		try {
			ServerSocket ss = new ServerSocket(2000); //port 2000 for BananaBanks
			BananaBank bank = new BananaBank("accounts.txt");
			for(;;) { // infinite loop
				Socket cs = ss.accept();
				new WorkerThread(cs, bank).start();
			}
		} catch(IOException e) {
			//we get here after the serverSocket is closed
			System.out.println("Bank is now closed");
		}
	}
}
