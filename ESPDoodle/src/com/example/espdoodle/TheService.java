package com.example.espdoodle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class TheService extends Service {

	public static final int CONNECT = 0;
	public static final int ECHO = 1;
	public static final int CONCLUDE = 2;
	public static final int OPEN_MEETING = 3;
	public static final int CHECK_OPEN_MEETING = 4;
	public static final int SELECT_TIME = 5;
	public static final int CHECK_MEETING_RESPOND = 6;
	public static final int CLOSE_MEETING = 7; 
	public static final int CHECK_CLOSED_MEETING = 8;

	public static final String USER = "user";
	public static final String HOST_IP = "IP";
	public static final String PORT = "port";
	public static final String BROADCAST_FILTER = "boadcast_filter";
	public static final String ARGUMENTS = "arguments";
	public static final String WHAT = "what";
	
	public static final String RESULT = "result";
	public static final int SUCCESS = 0;
	public static final int FAILED = 1;
	
	public static final String MEETING_ID = "meetingID";
	public static final String MESSAGE ="message";
	public static final String TIMESLOTS = "timeslots";
	public static final String PARTICIPANTS = "participants";
	public static final String SELECTED_TIME = "selectedTime";
	//public static final String REQUEST_CLOSED = "requestClosed";
	
	private int connectionAttempts;
	private int counter;

	private Looper mServiceLooper;
	private CommunicationHandler mServiceHandler;

	public void onCreate() {
		connectionAttempts = 0;
		counter = 0;
		HandlerThread thread = new HandlerThread("TheServiceWorkerThread",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		mServiceLooper = thread.getLooper();
		mServiceHandler = new CommunicationHandler(mServiceLooper);

		Log.d(getClass().getName(), "Servie started");
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Message msg = mServiceHandler.obtainMessage();
		msg.what = intent.getExtras().getInt(WHAT);
		msg.setData(intent.getBundleExtra(ARGUMENTS));
		mServiceHandler.sendMessage(msg);

		Log.d(getClass().getName(), "Servie received start command");
		return START_STICKY;
	}

	/*
	 * onBind has to be defined, it is a hook method in the abstract class
	 * service. If it is not defined the class remains abstract.
	 */
	public android.os.IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * The rest of the program is the inner class CommunicationHandler
	 * 
	 * The CommunicationHandler is the Handler for the service. It is defined as
	 * an inner class in order to use some of the variables of the Service
	 * class. The Service method onStartCommand that gets called when an
	 * Activity starts the service uses the method sendMessage(msg) with a msg
	 * built from the data in the intent. The message is received by the method
	 * handleMessage. This method uses TCP sockets to communicate via the
	 * network.
	 */
	
	private final class CommunicationHandler extends Handler {
		private Scanner in;
		private BufferedInputStream inBuffer;
		private PrintWriter out;
		private Socket socket;
		private String user;
		private XMLHelper xmlBuilder = XMLHelper.BUILDER;

		public CommunicationHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			switch (msg.what) {

			case CONNECT: {
				int timeout = 2000;
				String host = msg.getData().getString(HOST_IP);
				int port = msg.getData().getInt(PORT);
				user = msg.getData().getString(USER);
				String receiver = msg.getData().getString(BROADCAST_FILTER);
				boolean error = false;
				
				// Construct intent used to send message back to activity
				Intent intent = new Intent(receiver);
				intent.putExtra(RESULT, FAILED);
				try {
					// Establish a connection with the server
					socket = new Socket();
					SocketAddress socketAddress = new InetSocketAddress(host, port);
					socket.connect(socketAddress, timeout);
					socket.setSoTimeout(timeout);
					
					// Create input channel
					inBuffer = new BufferedInputStream(socket.getInputStream());
					in = new Scanner(inBuffer);
					
					// Create output channel
					out = new PrintWriter(socket.getOutputStream(), true);
					out.println(user);
					out.flush();

					String fromServer = readLineFromServer();
					String expectedResult = xmlBuilder.loginRespondXML(user);
					if (!fromServer.equals(expectedResult)) {
						Log.d(getClass().getName(), "Wrong answer from server at login");
						throw new java.net.UnknownHostException();
					} else {
						
						Log.d(getClass().getName(), "Login successful");
					}

				} catch (java.net.UnknownHostException e) {
					//Toast.makeText(ESPDoodleService.this, "Unknown host", Toast.LENGTH_SHORT).show();
					error = true;
				} catch (java.io.IOException e) {
					//Toast.makeText(ESPDoodleService.this, "IOException", Toast.LENGTH_SHORT).show();
					error = true;
				}
				if(error && connectionAttempts < 3){
					connectionAttempts++;
					Message retryMsg = mServiceHandler.obtainMessage();
					retryMsg.what = CONNECT;
					retryMsg.setData(msg.getData());
					
					msg.what = intent.getExtras().getInt(WHAT);
					msg.setData(intent.getBundleExtra(ARGUMENTS));
					counter++;
					mServiceHandler.sendMessageDelayed(retryMsg, 2000);
					Toast.makeText(getApplicationContext(), "Error when trying to connect, attempt: " + connectionAttempts, Toast.LENGTH_SHORT).show();
					
				} else {
					Intent i = new Intent(receiver);
					int result = error ? FAILED: SUCCESS;
					i.putExtra(RESULT, result);
					sendBroadcast(i);
				}
				if(error){
					stopSelf();
				}
				break;
			}

			case ECHO: {
				// Got from the activity something to send to the server
				String data = "[" + user + "]: " + msg.getData().getString("data");
				out.println(data);
				// wait to receive an acknowledgment from the server
				String back = in.nextLine();
				//sendNotification("Sent: " + data + ", Got: " + back + ".");
				break;
			}

			case CONCLUDE: {
				try {
					out.close();
					in.close();
					socket.close();
					Log.d(getClass().getName(), "Closing service");
					stopSelf();
				} catch (java.io.IOException e) {
					Toast.makeText(TheService.this, "IOException", Toast.LENGTH_SHORT).show();
					stopSelf();
				}
				break;
			}

			case OPEN_MEETING: {
				Log.d(getClass().getName(), "Service asked to create meeting");
				
				Bundle bundle = msg.getData();
				String meetingId = bundle.getString(MEETING_ID);
				String[] timeslots = bundle.getStringArray(TIMESLOTS);
				String[] participants = bundle.getStringArray(PARTICIPANTS);
				String receiver = bundle.getString(BROADCAST_FILTER);
	
				out.println(xmlBuilder.openMeetingXML(meetingId, "Select a preferred time", timeslots, participants));
				out.flush();

				Intent intent = new Intent(receiver);
				intent.putExtra(WHAT, OPEN_MEETING);
				Bundle bundel = new Bundle();
				
				String fromServer = readLineFromServer();
				String expectedResult = xmlBuilder.openMeetingResponseXML(); 
				
				if (!fromServer.equals(expectedResult)) {
					Log.d(getClass().getName(), "Wrong answer from server when created new meeting");
					bundle.putInt(RESULT, FAILED);
				} else {
					Log.d(getClass().getName(), "Succes creating new meeting");
					bundle.putInt(RESULT, SUCCESS);
				}
				
				intent.putExtras(bundel);
				sendBroadcast(intent);
				break;
			}

			case CHECK_OPEN_MEETING: {
				Log.d(getClass().getName(),"Service asked to get open requests");
				String receiver = msg.getData().getString(BROADCAST_FILTER);
				Intent intent = new Intent(receiver);
				intent.putExtra(WHAT, CHECK_OPEN_MEETING);
				Bundle bundle = new Bundle();
				
				String checkOpenRequestsXML = xmlBuilder.checkOpenRequestsXML();
				out.println(checkOpenRequestsXML);
				out.flush();

				ArrayList<String> result = new ArrayList<String>();
				String tmp = readLineFromServer();
				while (!tmp.equals("")) {
					result.add(tmp);
					tmp = readLineFromServer();
				}
				Log.d(getClass().getName(), "Got " + result.size() + " open request from server");

				if(result.size() == 0){
					bundle.putInt(RESULT, FAILED);
					intent.putExtras(bundle);
					sendBroadcast(intent);
				} else{
					OpenMeeting[] openMeetings = xmlBuilder.pasreOpenRequests(result);
					for(OpenMeeting meeting : openMeetings){
						bundle.putInt(RESULT, SUCCESS);
						bundle.putString(MEETING_ID, meeting.getMeetingId());
						bundle.putString(MESSAGE, meeting.getMessage());
						bundle.putStringArray(TIMESLOTS, meeting.getTimeslots());
						intent.putExtras(bundle);
						sendBroadcast(intent);
					}
				}	
				break;
			}
			
			case SELECT_TIME: {
				Log.d(getClass().getName(), "Service asked to store selected timeslot");
				
				String meetingId = msg.getData().getString(MEETING_ID);
				String time = msg.getData().getString(SELECTED_TIME);
				String receiver = msg.getData().getString(BROADCAST_FILTER);
				
				Intent intent = new Intent(receiver);
				intent.putExtra(WHAT, SELECT_TIME);
				Bundle bundle = new Bundle();
				
				String selectTimeXML = xmlBuilder.selectTimeXML(meetingId, time);
				out.println(selectTimeXML);
				out.flush();
			
				String fromServer = readLineFromServer();
				String expextedResult = xmlBuilder.selectTimeResponseXML(meetingId, time);
				
				if(!fromServer.equals(expextedResult)){
					Log.d(getClass().getName(), "Wrong answer from server when saving selected time");
					bundle.putInt(RESULT, FAILED);
				} else{
					Log.d(getClass().getName(), "Success saving selected time to server");
					bundle.putInt(RESULT, SUCCESS);
					bundle.putString(MEETING_ID, meetingId);
				}
				
				intent.putExtras(bundle);
				sendBroadcast(intent);
				break;
			}
			
			case CHECK_MEETING_RESPOND: {
				Log.d(getClass().getName(), "Service asked to get meeting respons");
				
				String meetingId = msg.getData().getString(MEETING_ID);
				String receiver = msg.getData().getString(BROADCAST_FILTER);
				
				out.println(xmlBuilder.checkMeetingRespondXML(meetingId));
				out.flush();
				
				ArrayList<String> respond = new ArrayList<String>();
				String tmp = readLineFromServer();
				while (!tmp.contains(xmlBuilder.numberOfRespondSubStringXML()) && !tmp.equals("")) {
					respond.add(tmp);
					tmp = readLineFromServer();
				}
				
				if(tmp.equals("") || respond.size() == 0){
					if(respond.size() == 0){
						Log.d(getClass().getName(), "No responds for meeting found");
					} else {
						Log.d(getClass().getName(), "Wrong answer from server when checking meeting responds");
					}
					Intent intent = new Intent(receiver);
					intent.putExtra(WHAT, CHECK_MEETING_RESPOND);
					Bundle bundle = new Bundle();
					bundle.putInt(RESULT, FAILED);
					intent.putExtras(bundle);
					sendBroadcast(intent);
				} else {
					Participant[] participants = xmlBuilder.parseCheckMeetingRespond(respond);
					for(Participant participant : participants){
						Intent intent = new Intent(receiver);
						intent.putExtra(WHAT, CHECK_MEETING_RESPOND);
						Bundle bundle = new Bundle();
						bundle.putInt(RESULT, SUCCESS);
						bundle.putString(SELECTED_TIME, participant.getTime());
						intent.putExtras(bundle);
						sendBroadcast(intent);
					}
				}
				break;
			}
			
			case CLOSE_MEETING :{
				Log.d(getClass().getName(), "Service asked to close meeting");
				String meetingId = msg.getData().getString(MEETING_ID);
				String time = msg.getData().getString(SELECTED_TIME);
				String receiver = msg.getData().getString(BROADCAST_FILTER);
				
				out.println(xmlBuilder.closeMeetingXML(meetingId, time));
				out.flush();

				Intent intent = new Intent(receiver);
				intent.putExtra(WHAT, CLOSE_MEETING);
				Bundle bundle = new Bundle();
				
				
				// Check if the selected time has been stored
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				out.println(xmlBuilder.checkMeetingRespondXML(meetingId));
				out.flush();
				
				ArrayList<String> respond = new ArrayList<String>();
				String tmp = readLineFromServer();
				while (!tmp.contains(xmlBuilder.numberOfRespondSubStringXML()) && !tmp.equals("")) {
					respond.add(tmp);
					tmp = readLineFromServer();
				}
				
				if(tmp.contains(xmlBuilder.numberOfRespondSubStringXML())){
					// if this is the first line sent by the server it means that no meeting was found
					// so the selected time has been saved
					bundle.putInt(RESULT, SUCCESS);
					bundle.putString(MEETING_ID, meetingId);
				} else {
					bundle.putInt(RESULT, FAILED);
				}
				
				intent.putExtras(bundle);
				sendBroadcast(intent);
				break;
			}

			case CHECK_CLOSED_MEETING:{
				Log.d(getClass().getName(), "Service asked to get planned meetings");
				
				String receiver = msg.getData().getString(BROADCAST_FILTER);
				String sendToService=xmlBuilder.checkClosedMeetingXML();
				out.println(sendToService);
				
				ArrayList<String> responds = new ArrayList<String>();
				String tmp = readLineFromServer();
				while (!tmp.equals("")) {
					responds.add(tmp);
					tmp = readLineFromServer();
				}
				
				if(responds.size() > 0){
					XMLHelper.ClosedMeeting[] closedMeetings = xmlBuilder.pasreCheckClosedMeeting(responds);
					for(XMLHelper.ClosedMeeting meeting : closedMeetings){
						String[] result = {meeting.getMeetingId(), meeting.getTime()};
						Intent intent = new Intent(receiver);
						intent.putExtra(RESULT, SUCCESS);
						Bundle bundle = new Bundle();
						bundle.putString(MEETING_ID, result[0]);
						bundle.putString(SELECTED_TIME, result[1]);
						intent.putExtras(bundle);
						sendBroadcast(intent);
					}
				} else {
					Intent intent = new Intent(receiver);
					intent.putExtra(RESULT, FAILED);
					sendBroadcast(intent);
				}
				break;
			}
			}
		}

		private String readLineFromServer() {
			String line = "";
			try {
				line = in.nextLine();
			} catch (NoSuchElementException e) {
				//Create a new instance if the scanner because the previous will be
				//exhausted if this is thrown, and won't read more data.
				in = new Scanner(inBuffer);
			}	
			return line;
		}

		/*
		 * This method prepares a notification that calls an Activity.
		 */

		private void sendNotification(String text, String title, String ticker, Intent notifyIntent) {
			// String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			int icon = R.drawable.ic_launcher; // icon from resources
			int mId = 1;
			Context context = getApplicationContext(); // application Context

			CharSequence tickerText = ticker;
			CharSequence contentTitle = title;
			CharSequence contentText = text;

			NotificationCompat.Builder notification = new NotificationCompat.Builder(
					getApplicationContext()).setSmallIcon(icon)
					.setContentTitle(contentTitle).setTicker(tickerText)
					.setContentText(contentText);

			/* the notification will call an activity with this intent */
			//Intent notifyIntent = new Intent();
			//notifyIntent.setClass(context, c);

			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
							| Notification.FLAG_AUTO_CANCEL);

			/* complete configuration and notify */
			notification.setContentIntent(contentIntent);

			// mId allows you to update the notification later on.
			mNotificationManager.notify(mId, notification.build());
		}
	}
}
