package com.example.espdoodle;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class XMLHelper {
	
	public static XMLHelper BUILDER = new XMLHelper();
	
	public String loginRespondXML(String user){
		return "<Accepted connection from " + user + "/>";
	}

	public String openMeetingXML(String meetingId, String message, String[] timeslots, String[] participants){
		
		String xml = 
				"<OpenMeeting>" + 
					"<MeetingId \"" + meetingId + "\"/>" + 
					"<Message text = \"" + message + "\"/>";
		
		for(String timeslot : timeslots){
			xml = xml + "<time = \"" + timeslot + "\"/>";
		}
	
		xml = xml + "<Participant numberOfParticipant =\"" + participants.length + "\">";
		
		for(String participant : participants){
			xml = xml + "<name = \"" + participant + "\"/>";
		}
		
		xml = xml +
				"</Participant>" + 
			"<OpenMeeting/>";
		
		return xml;
	}
	
	public String openMeetingResponseXML(){
		return "<Request to start meeting is accepted/>";
	}
	
	public String checkOpenRequestsXML(){
		return "<CheckOpenMeeting/>";
	}
	
	public OpenMeeting[] pasreOpenRequests(ArrayList<String> openRequests){
		OpenMeeting[] requests = new OpenMeeting[openRequests.size()];
		int i = 0;
		
		for(String request : openRequests){	
			String[] timeslots = new String[3];
			StringTokenizer tk = new StringTokenizer(request, "\"");
			
			tk.nextToken();
			String meetingId = tk.nextToken();
			
			tk.nextToken();
			String message = tk.nextToken();
			
			tk.nextToken();
			timeslots[0] = tk.nextToken();
			tk.nextToken();
			timeslots[1] = tk.nextToken();
			tk.nextToken();
			timeslots[2] = tk.nextToken();
			
			
			requests[i] = new OpenMeeting(meetingId, message, timeslots);
			i++;
		}
		
		return requests;
	}
	
	public String selectTimeXML(String meetingId, String time){
		String xml =
			"<SelectedTime>" + 
				"<MeetingId \"" + meetingId + "\"/>" + 
				"<time = \"" + time + "\"/>" + 
			"<SelectedTime/>";
			
		return xml;
	}
	
	public String selectTimeResponseXML(String meetingId, String time){
		String xml =
			"<reply>" +
				"<SelectedTime>" + 
					"<MeetingId \"" + meetingId + "\"/>" +
					"<time = \"" + time + "\"/>" +
				"<SelectedTime/>" + 
			"<reply/>";
		
		return xml;
	}
	
	public String checkMeetingRespondXML(String meetingId){
		String xml =
			"<CheckMeetingRespond>" +
				"<MeetingId \"" + meetingId +"\"/>" +
			"<CheckMeetingRespond/>";
		
		return xml;
	}
	
	public String numberOfRespondSubStringXML(){
		return "<NumberOfRespond ";
	}
	
	public String closeMeetingXML(String meetingId, String time){
		String xml =
			"<CloseMeeting>" +
				"<MeetingId \"" + meetingId + "\"/>" +
				"<SelectedTime time = \"" + time + "\">" +
			"<CloseMeeting/>";

		return xml;
	}
	
	public Participant[] parseCheckMeetingRespond(ArrayList<String> responds){
		Participant[] participants = new Participant[responds.size()];
		
		int i = 0;
		for(String respond : responds){
			if(respond.contains(numberOfRespondSubStringXML())){
				break;
			}
			StringTokenizer tk = new StringTokenizer(respond, "\"");
			
			tk.nextToken();
			tk.nextToken();
			tk.nextToken();
			String name = tk.nextToken();
			
			tk.nextToken();
			String time = tk.nextToken();
			
			Participant participant = new Participant(name, time);
			participants[i] = participant;
			i++;
		}
		
		return participants;
	}
	
	public String checkClosedMeetingXML(){
		return "<CheckClosedMeeting/>";
	}
	
	public ClosedMeeting[] pasreCheckClosedMeeting(ArrayList<String> responds){
		ClosedMeeting[] meetings = new ClosedMeeting[responds.size()];
		
		int i = 0;
		for(String meeting : responds){
			StringTokenizer tk = new StringTokenizer(meeting, "\"");
			tk.nextToken();
			String meetingId = tk.nextToken();
			tk.nextToken();
			String time = tk.nextToken();
			
			meetings[i] = new ClosedMeeting(meetingId, time);
			i++;
		}
		
		return meetings;
	}
	
	public class ClosedMeeting{
		private String meetingId;
		private String time;
		
		public ClosedMeeting(String meetingId, String time) {
			this.meetingId = meetingId;
			this.time = time;
		}

		public String getMeetingId() {
			return meetingId;
		}

		public String getTime() {
			return time;
		}
	}
}
