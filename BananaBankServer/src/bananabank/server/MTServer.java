package bananabank.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;



public class MTServer {

	public static PrintStream shutdownStream;
	public static void main(String[] args) throws IOException{
		ArrayList<WorkerThread> list = new ArrayList<WorkerThread>();
		BananaBank bank = new BananaBank("accounts.txt");
		try {
			ServerSocket ss = new ServerSocket(2000); //port 2000 for BananaBanks
			
			
			for(;;) { // infinite loop
				Socket cs = ss.accept();
				WorkerThread thread = new WorkerThread(cs, ss, bank);
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
		Collection<Account> accounts = bank.getAllAccounts();
		int totalAmount = 0;
		for(Account account : accounts) {
			totalAmount += account.getBalance();
		}
		shutdownStream.println(totalAmount);
		bank.save("answer.txt");  //save to file
	}
}
