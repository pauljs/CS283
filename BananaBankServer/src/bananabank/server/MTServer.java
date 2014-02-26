package bananabank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class MTServer {

	public static void main(String[] args) throws IOException{
		ArrayList<WorkerThread> list = new ArrayList<WorkerThread>();
		try {
			ServerSocket ss = new ServerSocket(2000); //port 2000 for BananaBanks
			BananaBank bank = new BananaBank("accounts.txt");
			
			for(;;) { // infinite loop
				Socket cs = ss.accept();
				WorkerThread thread = new WorkerThread(cs, bank);
				list.add(thread);
				thread.start();				
			}
		} catch(IOException e) {
			//we get here after the serverSocket is closed
			System.out.println("Bank is now closing");
		}
		
		for(WorkerThread thread : list) {
			try {
				thread.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
