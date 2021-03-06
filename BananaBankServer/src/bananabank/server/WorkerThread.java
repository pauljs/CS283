package bananabank.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.StringTokenizer;


public class WorkerThread extends Thread {
	
	Socket cs;
	BananaBank bank;
	ServerSocket ss;
	
	public WorkerThread(Socket cs, ServerSocket ss, BananaBank bank) {
		this.cs = cs;
		this.ss = ss;
		this.bank = bank;
	}
	
	@Override
	public void run() {
		System.out.println("Worker thread starting");
		try {
			
			//System.out.println("ServerSocket created");
			BufferedReader r = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			PrintStream w = new PrintStream(cs.getOutputStream());
			//System.out.println("Client connected");
					
			String line;
			while((line = r.readLine()) != null) {
				System.out.println("Received: " + line);
				if(line.startsWith("SHUTDOWN")) {
					ss.close();
					MTServer.shutdownStream = w;
					return;
				}

				String str = bankSystem(line);
				System.out.println(str);
				w.println(str);
				
				//w.flush();
			}				
			w.close(); //implicitly closes everything when closes PrintWriter
			//System.out.println("Close disconnected");
		} catch (IOException e) {
			System.out.println("Error occurred, terminating");
			e.printStackTrace();
		} 
		/*finally {
			if(!cs.isClosed()) {
				try {
					cs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Worker thread exiting");*/
	}

	private String bankSystem(String line) {
		/*if(line.equals("SHUTDOWN\n")) {//shutdown
			Collection<Account> accounts = bank.getAllAccounts();
			int totalAmount = 0;
			for(Account account : accounts) {
				totalAmount += account.getBalance();
			}
			line = "" + totalAmount;
			return line; //DO SOMETHING
		}*/
		StringTokenizer st = new StringTokenizer(line);
		int transferAmount = Integer.parseInt(st.nextToken());
		int srcAccountNumber = Integer.parseInt(st.nextToken());
		int dstAccountNumber = Integer.parseInt(st.nextToken());
		//int indexSpace1 = findSpace(0, line);
		//int indexSpace2 = findSpace(indexSpace1 + 1, line);
		//int transferAmount = Integer.parseInt(line.substring(0, indexSpace1));
		//int accountFromNumber = Integer.parseInt(line.substring(indexSpace1 + 1, indexSpace2));
		//int dstAccountNumber = Integer.parseInt(line.substring(indexSpace2 + 1, findNewLine(indexSpace2 + 1, line)));
		int smallerAccountNumber, largerAccountNumber;
		if(srcAccountNumber < dstAccountNumber) {
			smallerAccountNumber = srcAccountNumber;
			largerAccountNumber = dstAccountNumber;
		} else {
			smallerAccountNumber = dstAccountNumber;
			largerAccountNumber = srcAccountNumber;
		}
		synchronized (bank.getAccount(smallerAccountNumber)){// synchronizing both
			synchronized (bank.getAccount(largerAccountNumber)) {
				Account accountFrom = bank.getAccount(srcAccountNumber);
				Account dstAccount = bank.getAccount(dstAccountNumber);
				if(accountFrom == null) {
					line = "Invalid source account";
				} else if (dstAccount == null) {
					line = "Invalid destination account";
				} else { //Accounts exist
					accountFrom.transferTo(transferAmount, dstAccount); //MAKE SYNCHRONIZED
					line = transferAmount + " transferred from account " + srcAccountNumber + " to account " + dstAccountNumber;
				}
			}
		}
		
		return line;	
	}

	/*private int findNewLine(int i, String line) {
		for(; i < line.length(); ++i) {
			if(line.substring(i).equals("\n")) {
				return i;
			}
		}
		return -1; //means no space
	}

	private int findSpace(int i, String line) {
		for(; i < line.length(); ++i) {
			if(line.charAt(i) == ' ') {
				return i;
			}
		}
		return -1; //means no space
	}*/
}
