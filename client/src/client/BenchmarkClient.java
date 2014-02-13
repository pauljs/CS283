package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class BenchmarkClient {
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		long start = System.nanoTime();
		int port = Integer.parseInt(args[0]);
		while(System.nanoTime() - start <= Math.pow(10, 9)) { //go to run config. put in 4444, make two threads in stuff inwhile loop
			new WorkerThreadClient(port).start();
			new WorkerThreadClient(port).start();
		}
		System.out.println("DONE");
	}
}
		
		
		
		/*
		s = new Socket("localhost", 3333);
		System.out.println("Client is connected to the server");
		w = new PrintStream(s.getOutputStream());
		start = System.nanoTime();
		while(System.nanoTime() - start <= Math.pow(10, 9)) {
			w.println("hello world");
			
			System.out.println("Client Receiving");
			BufferedReader r;
			try {
				r = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String line;
				while((line = r.readLine()) != null) {
					System.out.print(".");
					
				}
				w.close();//implicitly closes everything when closes PrintWriter
				System.out.println("Close disconnected");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(!s.isClosed()) {
					s.close();
				}
				System.out.println("");
			}
			System.out.println("Client finished");
		}
	}
} //on command line do telnet localhost 3333
//type something in it
	*/