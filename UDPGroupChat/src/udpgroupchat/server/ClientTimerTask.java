package udpgroupchat.server;

import java.util.ArrayList;
import java.util.TimerTask;

public class ClientTimerTask extends TimerTask {
	
	protected final ClientEndPoint client;
	protected final ArrayList<String> messages;

	public ClientTimerTask(ClientEndPoint client, ArrayList<String> messages) {
		this.client = client;
		this.messages = messages;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		WorkerThread thread = new WorkerThread(null, null);
		thread.sendMessages2(client, messages);
	}

}
