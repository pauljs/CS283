package client;
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
					w.println(line.toUpperCase());
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
}
