package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

import com.javadocmd.simplelatlng.LatLng;

public class Evento implements Comparable<Evento> {

	
	public enum EventType{
		CRIMINE,
		ARRIVA_AGENTE,
		GESTITO
		
	}
	
	private LocalDateTime data;
	private Event crimine;
	private EventType type;
	public Evento(LocalDateTime data, Event crimine, EventType type) {
		super();
		this.data = data;
		this.crimine = crimine;
		this.type = type;
	}
	public LocalDateTime getData() {
		return data;
	}
	public Event getCrimine() {
		return crimine;
	}
	public EventType getType() {
		return type;
	}
	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return this.data.compareTo(o.data);
	}
	
	
}
