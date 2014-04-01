package udpgroupchat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;


import udpgroupchat.client.Client;

//FOR NETCAT- do "nc -u localhost 20000" because port is 20000
public class WorkerThread extends Thread {

	private DatagramPacket rxPacket;
	public static DatagramSocket socket;

	public WorkerThread(DatagramPacket packet, DatagramSocket socket) {
		this.rxPacket = packet;
		this.socket = socket;
	}

	@Override
	public void run() {
		// convert the rxPacket's payload to a string
		String payload = new String(rxPacket.getData(), 0, rxPacket.getLength())
				.trim();

		// dispatch request handler functions based on the payload's prefix

//		
//		if (payload.startsWith("REGISTER")) {
//			onRegisterRequested(payload);
//			return;
//		}

		if (payload.startsWith("UNREGISTER")) {
			onUnregisterRequested(payload);
			return;
		}

		if (payload.startsWith("SEND")) {
			onSendRequested(payload);
			return;
		}
		
		if (payload.startsWith("JOIN")) {
			onJoinRequested(payload);
			return;
		}
		
		if (payload.startsWith("POLL")) {
			onPollRequested(payload);
			return;
		}
		
		if (payload.startsWith("IPCHANGE")) {
			onIPChangeRequested(payload);
			return;
		}
		
		if (payload.startsWith("ACK")) {
			onAckRequested(payload);
			return;
		}
		
		if(payload.startsWith("SHUTDOWN")) {
			socket.close();
			//MTServer.shutdownStream = w;
			return;
		}
		
		

		//
		// implement other request handlers here...
		//

		// if we got here, it must have been a bad request, so we tell the
		// client about it
		onBadRequest(payload);
	}

	// send a string, wrapped in a UDP packet, to the specified remote endpoint
	public void send(String payload, InetAddress address, int port)
			throws IOException {
		DatagramPacket txPacket = new DatagramPacket(payload.getBytes(),
				payload.length(), address, port);
		this.socket.send(txPacket);
	}

	/*
	private void onRegisterRequested(String payload) {
		// get the address of the sender from the rxPacket
		InetAddress address = this.rxPacket.getAddress();
		// get the port of the sender from the rxPacket
		int port = this.rxPacket.getPort();

		// create a client object, and put it in the map that assigns names
		// to client objects
		Server.clientEndPoints.add(new ClientEndPoint(address, port, null));
		// note that calling clientEndPoints.add() with the same endpoint info
		// (address and port)
		// multiple times will not add multiple instances of ClientEndPoint to
		// the set, because ClientEndPoint.hashCode() is overridden. See
		// http://docs.oracle.com/javase/7/docs/api/java/util/Set.html for
		// details.

		// tell client we're OK
		try {
			send("REGISTERED\n", this.rxPacket.getAddress(),
					this.rxPacket.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/

	private void onUnregisterRequested(String payload) {
		// unregister id group
		/*
		ClientEndPoint clientEndPoint = new ClientEndPoint(
				this.rxPacket.getAddress(), this.rxPacket.getPort(), null);
		*/
		
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken();
		int uniqueId = Integer.parseInt(st.nextToken());
		String groupName = st.nextToken();
		
		if(Server.groupsToClientsMap.get(groupName) == null) {
			try {
				send("GROUP DOES NOT EXIST\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if(Server.idToClientMap.get(uniqueId) == null) {
			try {
				send("CLIENT DOES NOT EXIST\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		// check if client is in the set of registered clientEndPoints
		ClientEndPoint client = Server.idToClientMap.get(uniqueId);
		if (client != null && Server.groupsToClientsMap.get(groupName).contains(client)) {
			// yes, remove it
			Server.groupsToClientsMap.get(groupName).remove(client);
			try {
				send("UNREGISTERED\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// no, send back a message
			try {
				send("CLIENT NOT REGISTERED\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void onSendRequested(String payload) {
		// the message is comes after "SEND" in the payload
		// SEND uniqueID group message
		
		//send id and update ip address every time
		InetAddress address = this.rxPacket.getAddress();
		// get the port of the sender from the rxPacket
		int port = this.rxPacket.getPort();

		// create a client object, and put it in the map that assigns names
		// to client objects
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken();
		int uniqueID = Integer.parseInt(st.nextToken());
		String groupName = st.nextToken();
		if(!Server.groupsToClientsMap.get(groupName).contains(Server.idToClientMap.get(uniqueID))) {
			try {
				send("NOT JOINED IN GROUP\n", this.rxPacket.getAddress(),
						this.rxPacket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		String message = payload.substring("SEND".length() + ("  " + uniqueID).length() + groupName.length() + 1,
				payload.length()).trim();
		ArrayList<ClientEndPoint> clients = Server.groupsToClientsMap.get(groupName);
		ClientEndPoint curClient = Server.idToClientMap.get(uniqueID);
		for (ClientEndPoint clientEndPoint : clients) {
			if(clientEndPoint != curClient) {
				Server.clientToMessages.get(clientEndPoint).add(message);
			}
			/*send("MESSAGE: " + message + "\n", clientEndPoint.address,
					clientEndPoint.port);*/
		}
		try {
			send("SENT: " + message + "\n", address,
					port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void onJoinRequested(String payload) {
		// the message is comes after "JOIN" in the payload
		//In format: "uniqueID groupName"
		
		//QUESITONS:
		//1. Should we have a Hashmap of group name to list of people in group?
		//2. Thus should we edit Register to register to a certain group and
		//   send to send to people in the certain group?
		// get the address of the sender from the rxPacket
		
		InetAddress address = this.rxPacket.getAddress();
		// get the port of the sender from the rxPacket
		int port = this.rxPacket.getPort();

		// create a client object, and put it in the map that assigns names
		// to client objects
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken();
		int uniqueID = Integer.parseInt(st.nextToken());
		String groupName = st.nextToken();
		if(Server.groupsToClientsMap.get(groupName) == null) {
			Server.groupsToClientsMap.put(groupName, new ArrayList<ClientEndPoint>());
		}
		
		ClientEndPoint newClient = new ClientEndPoint(address, port, null);
		if(Server.idToClientMap.get(uniqueID) == null) {
			Server.idToClientMap.put(uniqueID, newClient);
			Server.groupsToClientsMap.get(groupName).add(Server.idToClientMap.get(uniqueID));
			Server.clientToMessages.put(Server.idToClientMap.get(uniqueID), new ArrayList<String>());
		} else {			int index = Server.groupsToClientsMap.get(groupName).indexOf(Server.idToClientMap.get(uniqueID));
			if(index != -1) {
				Server.groupsToClientsMap.get(groupName).remove(Server.groupsToClientsMap.get(groupName).indexOf(Server.idToClientMap.get(uniqueID)));
			}
			ArrayList<String> messages = Server.clientToMessages.get(Server.idToClientMap.get(uniqueID));
			Server.idToClientMap.put(uniqueID, newClient);
			Server.groupsToClientsMap.get(groupName).add(Server.idToClientMap.get(uniqueID));
			Server.clientToMessages.put(Server.idToClientMap.get(uniqueID), messages);
		}
		
			
		//Server.clientEndPoints.add(new ClientEndPoint(address, port));
		// note that calling clientEndPoints.add() with the same endpoint info
		// (address and port)
		// multiple times will not add multiple instances of ClientEndPoint to
		// the set, because ClientEndPoint.hashCode() is overridden. See
		// http://docs.oracle.com/javase/7/docs/api/java/util/Set.html for
		// details.

		// tell client we're OK
		try {
			send("JOINED\n", this.rxPacket.getAddress(),
					this.rxPacket.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void onPollRequested(String payload) {
		// the message is comes after "POLL" in the payload
		//POLL id group
		
		//QUESTIONS REGARDING POLL:
		//1. I assume we will need a HashMap for messages waiting to be sent
		//   How do we keep track of which messages need to be sent to which people?
		
		InetAddress address = this.rxPacket.getAddress();
		// get the port of the sender from the rxPacket
		int port = this.rxPacket.getPort();

		// create a client object, and put it in the map that assigns names
		// to client objects
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken();
		int uniqueId = Integer.parseInt(st.nextToken());
		final ClientEndPoint client = Server.idToClientMap.get(uniqueId);
		final ArrayList<String> messages = Server.clientToMessages.get(client);
		
		sendMessages(client, messages);
		/*
		for(String str : messages) {
			String message = payload.substring("SEND".length() + 1,
					str.length()).trim();
			
			try {
				send("MESSAGE: " + message + "\n", client.address,
						client.port);
				
				TimerTask timerTask = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						sendMessages2(client, messages);
					}
				};
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		*/
	}
	
	public void sendMessages2(ClientEndPoint client,
			ArrayList<String> messages) {
		// TODO Auto-generated method stub
		sendMessages(client, messages);
		Timer timer = new Timer();
		timer.schedule(client.timerTask, 8000);
	}
	
	private void sendMessages(ClientEndPoint client, ArrayList<String> messages) {
		// TODO Auto-generated method stub
		if(messages.size() != 0) {
			String payload = messages.get(0);
			String message = payload.trim();
			
			try {
				send("MESSAGE: " + message + "\n", client.address,
						client.port);
				
//				TimerTask timerTask = new TimerTask() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						
//					}
//					
//				};
				
				Timer timer = new Timer();
				client.timerTask = new ClientTimerTask(client, Server.clientToMessages.get(client));
				timer.schedule(client.timerTask, 8000);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void onIPChangeRequested(String payload) {
		// the message is comes after "IPChange" in the payload
		//QUESTIONS REGARDING POLL:
		//1. This seems like to do this we should change its values in on the HashMaps?
		//2. Should I have them have arguments pass for this such as
		//   1. New IP Address
		//   2. Old IP Address as proof of correct client?
		
	}
	
	private void onAckRequested(String payload) {
		// the message is comes after "Shutdown" in the payload
		// ACK uniqueID StringSent
		//must manually send this ack
		
		//QUESTIONS REGARDING POLL:
		//1. If shutdown is sent, should we unregister everyone?
		//2. If Shutdown is sent, should it be similar as to the shutdown in the previous project?
		
		InetAddress address = this.rxPacket.getAddress();
		// get the port of the sender from the rxPacket
		int port = this.rxPacket.getPort();

		// create a client object, and put it in the map that assigns names
		// to client objects
		StringTokenizer st = new StringTokenizer(payload);
		st.nextToken();
		int uniqueId = Integer.parseInt(st.nextToken());
		String message = st.nextToken();
		ClientEndPoint client = Server.idToClientMap.get(uniqueId);
		client.timerTask.cancel();
		Server.clientToMessages.get(client).remove(message);
		ArrayList<String> messages = Server.clientToMessages.get(client);
		sendMessages(client, messages);
	}


	private void onBadRequest(String payload) {
		try {
			send("BAD REQUEST\n", this.rxPacket.getAddress(),
					this.rxPacket.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
