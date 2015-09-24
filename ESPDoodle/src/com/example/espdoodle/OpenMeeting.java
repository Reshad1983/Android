package com.example.espdoodle;

public class OpenMeeting{
	private String meetingId;
	private String message;
	private String[] timeslots;
	
	public OpenMeeting(String meetingId, String message, String[] timeslots){
		this.meetingId = meetingId;
		this.message = message;
		this.timeslots = timeslots.clone();
	}

	public String getMeetingId() {
		return meetingId;
	}

	public String getMessage() {
		return message;
	}

	public String[] getTimeslots() {
		return timeslots;
	}

	@Override
	public String toString() {
		return meetingId;
	}

	@Override
	public boolean equals(Object o) {
		boolean same = false;
		if(o != null && o instanceof OpenMeeting){
			OpenMeeting another = (OpenMeeting)o;
			same = getMeetingId().equals(another.getMeetingId());
		}
		return same;
	}

}