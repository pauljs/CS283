package bananabank.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class WorkerThread extends Thread {
	
	Socket cs;
	
	public WorkerThread(Socket cs) {
		this.cs = cs;
	}
	
	@Override
	public void run() {
		System.out.println("Worker thread starting");
		try {
			
			System.out.println("ServerSocket created");
			BufferedReader r = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			PrintStream w = new PrintStream(cs.getOutputStream());
				System.out.println("Client connected");
						
				String line;
				while((line = r.readLine()) != null) {
					System.out.println("Received: " + line);
					String str = bankSystem(line);
					w.println(line);
					//w.flush();
				}				
				w.close(); //implicitly closes everything when closes PrintWriter
				System.out.println("Close disconnected");
		} catch (IOException e) {
			System.out.println("Error occurred, terminating");
			e.printStackTrace();
		} finally {
			if(!cs.isClosed()) {
				try {
					cs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Worker thread exiting");
	}

	private String bankSystem(String line) {
		if(line.equals("SHUTDOWN\n")) {
			return line; //DO SOMETHING
		}
		int indexSpace1 = findSpace(0, line);
		int indexSpace2 = findSpace(indexSpace1 + 1, line);
		int transferAmount = Integer.parseInt(line.substring(0, indexSpace1));
		int accountFromNumber = Integer.parseInt(line.substring(indexSpace1 + 1, indexSpace2));
		int dstAccountNumber = Integer.parseInt(line.substring(indexSpace2 + 1, findNewLine(indexSpace2 + 1, line)));
		BananaBank bank = null;
		try {
			bank = new BananaBank("accounts.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized (bank.getAccount(accountFromNumber)){
			synchronized (bank.getAccount(dstAccountNumber)) {
				Account accountFrom = bank.getAccount(accountFromNumber);
				Account dstAccount = bank.getAccount(dstAccountNumber);
				if(accountFrom == null) {
					line = "Account from number does not exist";
				} else if (dstAccount == null) {
					line = "Account to number does not exist";
				} else { //Accounts exist
					accountFrom.transferTo(transferAmount, dstAccount); //MAKE SYNCHRONIZED
					line = transferAmount + " transferred from account " + accountFromNumber + " to account " + dstAccountNumber + "\n";
				}
			}
		}
		

		return line;	
	}

	private int findNewLine(int i, String line) {
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
	}
}
