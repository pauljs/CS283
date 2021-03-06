package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class WorkerThreadClient extends Thread {

	int port;
	
	public WorkerThreadClient(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		long start = System.nanoTime();
		while(System.nanoTime() - start <= Math.pow(10, 9)) { //go to run config. put in 4444, make two threads in stuff inwhile loop
			Socket s = null;
			try {
				s = new Socket("localhost", port);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println("Client is connected to the server");
			PrintStream w = null;
			try {
				w = new PrintStream(s.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println("Client Receiving");
			w.println("hello world, how are you doing today? World: I am doing well, thank you for asking!");
			BufferedReader r;
			try {
				r = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String line;
	//			while((line = r.readLine()) != null) {
	//				System.out.print(".");
	//				
	//			}
				line = r.readLine();
				System.out.print(".");
				w.close();//implicitly closes everything when closes PrintWriter
				//System.out.println("Close disconnected");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(!s.isClosed()) {
					try {
						s.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//System.out.println("");
			}
			//System.out.println("Client finished");
		}
	}
}
