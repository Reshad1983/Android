package com.example.espdoodle;

public class Participant {
	private String name;
	private String time;
	
	public Participant(String name, String time) {
		this.name = name;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}
}
