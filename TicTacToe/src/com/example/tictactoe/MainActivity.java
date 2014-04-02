package com.example.tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

//import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected static final int TICTACTOE_PORT = 20000;
	protected static final String SERVER_ADDRESS = "10.67.11.253";//54.186.162.168//ec2-54-186-162-168.uswest-2.compute-amazonaws.com
	public static final int MAX_PACKET_SIZE = 512;
	public DatagramSocket socket;
	
	public Button button11;
	public Button button12;
	public Button button13;
	public Button button21;
	public Button button22;
	public Button button23;
	public Button button31;
	public Button button32;
	public Button button33;
	public Button pollButton1;
	public Button sendButton;
	public EditText groupNameEditText;
	public EditText idEditText;
	public EditText letterEditText;
	public boolean firstPull;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		
		button11 = (Button) findViewById(R.id.button11);
		button12 = (Button) findViewById(R.id.button12);
		button13 = (Button) findViewById(R.id.button13);
		button21 = (Button) findViewById(R.id.button21);
		button22 = (Button) findViewById(R.id.button22);
		button23 = (Button) findViewById(R.id.button23);
		button31 = (Button) findViewById(R.id.button31);
		button32 = (Button) findViewById(R.id.button32);
		button33 = (Button) findViewById(R.id.button33);
		sendButton = (Button) findViewById(R.id.sendButton1);
		pollButton1 = (Button) findViewById(R.id.buttonPoll);
		groupNameEditText = (EditText) findViewById(R.id.groupNameEditText);
		idEditText = (EditText) findViewById(R.id.idEditText);
		letterEditText = (EditText) findViewById(R.id.letterEditText);
		firstPull = false;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main, container,
//					false);
//			return rootView;
//		}
//	}
	
	public void sendButtonOnClick(View v) {
		String idString = idEditText.getText().toString();
		if(idString.equals("")) {
			Toast.makeText(getApplicationContext(), "Need Id!", Toast.LENGTH_SHORT).show();
		} else {
			String groupNameString = groupNameEditText.getText().toString();
			String letterString = letterEditText.getText().toString();
			if(groupNameString.equals("") || letterString.equals("")) {
				Toast.makeText(getApplicationContext(), "Need Id, Group Name, and Letter!", Toast.LENGTH_SHORT).show();
			} else {
				sendButton.setEnabled(false);
				System.out.println("WHY");
				TicTacToeAsyncTask taskRegister = new TicTacToeAsyncTask();
				taskRegister.execute("REGISTER," + idString);
				TicTacToeAsyncTask taskJoin = new TicTacToeAsyncTask();
				taskJoin.execute("JOIN," + idString + "," + groupNameString + "," + letterString);
			}
		}
	}
	
	public void pollButton1OnClick(View v) {
		pollButton1.setEnabled(false);
		TicTacToeAsyncTask taskRegister = new TicTacToeAsyncTask();
		taskRegister.execute("POLL," + idEditText.getText().toString() + "," + groupNameEditText.getText().toString() + "," + letterEditText.getText().toString());
	}
	
	private void startTicTacToeAsyncTask(String dash, String string) {
		if(!dash.equals("-")) {
			Toast.makeText(getApplicationContext(), "Already Taken!", Toast.LENGTH_SHORT).show();
		} else {
			enableAllButtons(false);
			pollButton1.setEnabled(true);
			changeButtonTexts(letterEditText.getText().toString(), Integer.parseInt(string));
			String idString = idEditText.getText().toString();
			String groupNameString = groupNameEditText.getText().toString();
			String letterString = letterEditText.getText().toString();
			if(idString.equals("") || groupNameString.equals("") || letterString.equals("")) {
				Toast.makeText(getApplicationContext(), "Need Id, Group Name, and Letter!", Toast.LENGTH_SHORT).show();
			} else {
				TicTacToeAsyncTask task = new TicTacToeAsyncTask();
				task.execute("SEND," + idEditText.getText().toString() + "," + groupNameEditText.getText().toString() + "," + letterEditText.getText().toString() + string);
			}
		}
	}
	
	public void button11OnClick(View v) {
		String letter11 = button11.getText().toString();
		startTicTacToeAsyncTask(letter11, "11");
	}
	
	public void button12OnClick(View v) {
		String letter12 = button12.getText().toString();
		startTicTacToeAsyncTask(letter12, "12");
	}
	
	public void button13OnClick(View v) {
		String letter13 = button13.getText().toString();
		startTicTacToeAsyncTask(letter13, "13");
	}
	
	public void button21OnClick(View v) {
		String letter21 = button23.getText().toString();
		startTicTacToeAsyncTask(letter21, "21");
	}
	
	public void button22OnClick(View v) {
		String letter22 = button22.getText().toString();
		startTicTacToeAsyncTask(letter22, "22");
	}
	
	public void button23OnClick(View v) {
		String letter23 = button23.getText().toString();
		startTicTacToeAsyncTask(letter23, "23");
	}
	
	public void button31OnClick(View v) {
		String letter31 = button31.getText().toString();
		startTicTacToeAsyncTask(letter31, "31");
	}
	
	public void button32OnClick(View v) {
		String letter32 = button11.getText().toString();
		startTicTacToeAsyncTask(letter32, "32");
	}
	
	public void button33OnClick(View v) {
		String letter33 = button33.getText().toString();
		startTicTacToeAsyncTask(letter33, "33");
	}
	
	private class TicTacToeAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected String doInBackground(String... params) {
//			String rval = null;
//			Socket socket = null;
//			BufferedReader reader = null;
			String payload = null;

			try {
//				int TICTACTOE_PORT = 20000;
//				String SERVER_ADDRESS = "ec2-54-186-162-168.uswest-2.compute-amazonaws.com";
				System.out.println("blah");
				InetSocketAddress serverSocketAddress = new InetSocketAddress(
						MainActivity.SERVER_ADDRESS, MainActivity.TICTACTOE_PORT);
				System.out.println("ahs");

				// create a datagram socket, let the OS bind to an ephemeral UDP
				// port. See
				// http://docs.oracle.com/javase/tutorial/networking/datagrams/ for
				// details.
				System.out.println("before socket");
				

				// send "REGISTER" to the server
				//
				// create an UDP packet that we'll send to the server
				System.out.println("before txpacket");
				String str = params[0];
				System.out.println(str);
				DatagramPacket txPacket = new DatagramPacket(params[0].getBytes(),
						params[0].length(), serverSocketAddress);
				// send the packet through the socket to the server
				System.out.println("before send");
				socket.send(txPacket);
				System.out.println("after send");
				// receive the server's response
				//
				// create an empty UDP packet
				byte[] buf = new byte[MainActivity.MAX_PACKET_SIZE];
				DatagramPacket rxPacket = new DatagramPacket(buf, buf.length);
				// call receive (this will populate the packet with the received
				// data, and the other endpoint's info)
				System.out.println("about to receive");
				if(params[0].startsWith("ACK")) {
					String nullString = null;
					return nullString;
				} else {
					socket.receive(rxPacket);
					// print the payload
					payload = new String(rxPacket.getData(), 0,
							rxPacket.getLength());
					System.out.println(payload);
				}
													
			} catch (IOException e) {
				System.out.println("blah3");
				// we jump out here if there's an error, or if the worker thread (or
				// someone else) closed the socket
				e.printStackTrace();
			} 
//			finally {
//				if (socket != null && !socket.isClosed()) {
//					System.out.println("blah4");
//					//socket.close();
//				}
//			}
			System.out.println("blah2");
			return payload;

		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result==null) {
				Toast.makeText(getApplicationContext(), "ACK", Toast.LENGTH_SHORT).show();						
			}
			else if(result.startsWith("ERROR") || result.startsWith("RROR")) {
				//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				System.out.println("bad");
			} 
			else if(result.startsWith("REGISTERED")){
				Toast.makeText(getApplicationContext(), "Registered!", Toast.LENGTH_SHORT).show();
				//sendButton.setEnabled(true);
			} 
			else if(result.startsWith("JOINED1")) { //joined first 
				firstPull = true;
				Toast.makeText(getApplicationContext(), "Joined First!", Toast.LENGTH_SHORT).show();
				enableAllButtons(true);
				sendButton.setEnabled(false);
				pollButton1.setEnabled(false);
			}
			else if(result.startsWith("JOINED2")) { //joined second
				Toast.makeText(getApplicationContext(), "Joined Second!", Toast.LENGTH_SHORT).show();
				enableAllButtons(false);
				pollButton1.setEnabled(true);
			} else if(result.startsWith("Sent")) {
				Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT).show();
				pollButton1.setEnabled(true);
			} else { //POLL
				System.out.println("WRONG");
				if(result.equals("")) {
					Toast.makeText(getApplicationContext(), "Waiting on other player... Try again later", Toast.LENGTH_SHORT).show();
				} else { //must ack back
					//make new asynctask("ACK id");
					if(firstPull) {
						firstPull = false;
						TicTacToeAsyncTask task = new TicTacToeAsyncTask();
						task.execute("ACK," + idEditText.getText().toString());
						TicTacToeAsyncTask taskRegister = new TicTacToeAsyncTask();
						taskRegister.execute("POLL," + idEditText.getText().toString() + "," + groupNameEditText.getText().toString() + "," + letterEditText.getText().toString());
					} else {
						firstPull = true;
						enableAllButtons(true);
						pollButton1.setEnabled(false);
						TicTacToeAsyncTask task = new TicTacToeAsyncTask();
						task.execute("ACK," + idEditText.getText().toString());
						String letter = result.substring(0, 1);
						int rowCol = Integer.parseInt(result.substring(1,3));
						changeButtonTexts(letter, rowCol);
						checkForEndGame();
						//do necessary edits to text of button and disable. then enable poll
					}
				}
			}
		}

		private void checkForEndGame() {
			// TODO Auto-generated method stub
			String letter11 = button11.getText().toString();
			String letter12 = button12.getText().toString();
			String letter13 = button13.getText().toString();
			String letter21 = button21.getText().toString();
			String letter22 = button22.getText().toString();
			String letter23 = button23.getText().toString();
			String letter31 = button31.getText().toString();
			String letter32 = button32.getText().toString();
			String letter33 = button33.getText().toString();
			checkForEndGameHelper(letter11, letter12, letter13);
			checkForEndGameHelper(letter21, letter22, letter23);
			checkForEndGameHelper(letter31, letter32, letter33);
			checkForEndGameHelper(letter11, letter21, letter31);
			checkForEndGameHelper(letter12, letter22, letter32);
			checkForEndGameHelper(letter13, letter23, letter13);
			checkForEndGameHelper(letter11, letter22, letter33);
			checkForEndGameHelper(letter13, letter22, letter31);
		}

		private void checkForEndGameHelper(String letter1, String letter2,
				String letter3) {
			// TODO Auto-generated method stub
			if(letter1.equals(letter2) && letter1.equals(letter3) && !letter1.equals("-")) {
				String gameStr = "Error";
				if(letter1.equals(letterEditText.getText().toString())) {
					gameStr = "You Win!";
				} else {
					gameStr = "You Lose!";
				}
				Toast.makeText(getApplicationContext(), gameStr, Toast.LENGTH_SHORT).show();
				
			} else if(tieGameCheck()) {
				Toast.makeText(getApplicationContext(), "You Tied!", Toast.LENGTH_SHORT).show();
			}
		}

		private boolean tieGameCheck() {
			// TODO Auto-generated method stub
			String letter11 = button11.getText().toString();
			String letter12 = button12.getText().toString();
			String letter13 = button13.getText().toString();
			String letter21 = button21.getText().toString();
			String letter22 = button22.getText().toString();
			String letter23 = button23.getText().toString();
			String letter31 = button31.getText().toString();
			String letter32 = button32.getText().toString();
			String letter33 = button33.getText().toString();
			return !(letter11.equals("-") || letter12.equals("-") || letter13.equals("-") || letter21.equals("-") || letter22.equals("-") || letter23.equals("-") || letter31.equals("-") || letter32.equals("-") || letter33.equals("-"));
		}


	}
	
	private void changeButtonTexts(String letter, int rowCol) {
		if(rowCol == 11) {
			button11.setText(letter);
		} else if (rowCol == 12) {
			button12.setText(letter);
		} else if (rowCol == 13) {
			button13.setText(letter);
		} else if (rowCol == 21) {
			button21.setText(letter);
		} else if (rowCol == 22) {
			button22.setText(letter);
		} else if (rowCol == 23) {
			button23.setText(letter);
		} else if (rowCol == 31) {
			button31.setText(letter);
		} else if (rowCol == 32) {
			button32.setText(letter);
		} else if (rowCol == 33) {
			button33.setText(letter);
		} else {
			Toast.makeText(getApplicationContext(), "Error in rowCol", Toast.LENGTH_SHORT).show();
		}
	}

	public void enableAllButtons(boolean b) {
		// TODO Auto-generated method stub
		button11.setEnabled(b);
		button11.setEnabled(b);
		button12.setEnabled(b);
		button13.setEnabled(b);
		button21.setEnabled(b);
		button22.setEnabled(b);
		button23.setEnabled(b);
		button31.setEnabled(b);
		button32.setEnabled(b);
		button33.setEnabled(b);
	} 

}
