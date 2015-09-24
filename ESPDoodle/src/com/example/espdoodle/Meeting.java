package com.example.espdoodle;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Meeting implements Parcelable {

	public static final String PARCELABLE_MEETING = "parceableMeeting";
	public static final String MEETING_ID = "meetingID";
	public static final String TIMESLOTS = "timeslots";
	public static final String PARTICIPANTS = "participants";
	public static final String REQUEST_CLOSED = "requestClosed";
	
	private String meetingID;
	private boolean requestClosed;
	private String[] timeslots;
	private String[] participants;
	
	public Meeting(String meetingID, String[] timeslots, String[] participants){
		if(timeslots.length != 3 || participants.length != 3){
			throw new IllegalArgumentException();
		}
		setMeetingID(meetingID);
		setTimeslots(timeslots);
		setParticipants(participants);
		setRequestClosed(false);
	}
	
	public Meeting(Parcel source){
		readFromParcebal(source);
	}
	
	public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>() {

		@Override
		public Meeting createFromParcel(Parcel source) {
			return new Meeting(source);
		}

		@Override
		public Meeting[] newArray(int size) {
			return new Meeting[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parc, int flags) {
		Bundle bundle = new Bundle();
		bundle.putString(MEETING_ID, getMeetingID());
		bundle.putStringArray(TIMESLOTS, getTimeslots());
		bundle.putStringArray(PARTICIPANTS, getParticipants());
		bundle.putBoolean(REQUEST_CLOSED, isRequestClosed());
		parc.writeBundle(bundle);
	}
	
	public void readFromParcebal(Parcel  in){
		Bundle bundle = in.readBundle();
		setMeetingID(bundle.getString(MEETING_ID));
		setTimeslots(bundle.getStringArray(TIMESLOTS));
		setParticipants(bundle.getStringArray(PARTICIPANTS));
		setRequestClosed(bundle.getBoolean(REQUEST_CLOSED));
	}
	
	public String getMeetingID(){
		return meetingID;
	}
	
	public void setMeetingID(String meetingID){
		if(meetingID == null){
			throw new IllegalArgumentException();
		}
		this.meetingID = String.copyValueOf(meetingID.toCharArray());
	}
	
	public String[] getTimeslots(){
		return timeslots.clone();
	}
	
	public void setTimeslots(String[] timeslots){
		if(containsNulls(timeslots) || timeslots.length != 3){
			throw new IllegalArgumentException();
		}
		this.timeslots = timeslots.clone();
	}
	
	public String[] getParticipants(){
		return participants.clone();
	}
	
	public void setParticipants(String[] participants){
		if(containsNulls(participants) || participants.length != 3){
			throw new IllegalArgumentException();
		}
		this.participants = participants.clone();
	}
	
	public boolean isRequestClosed(){
		return requestClosed;
	}
	
	public void setRequestClosed(boolean requestClosed){
		this.requestClosed = requestClosed;
	}
	
	private boolean containsNulls(String[] source){
		
		if(source == null){
			return true;
		}
		
		for(int i = 0; i < source.length; i++){
			if(source[i] == null){
				return true;
			}
		}
		
		return false;
	}
}
