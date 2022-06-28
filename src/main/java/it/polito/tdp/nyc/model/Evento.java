package it.polito.tdp.nyc.model;

public class Evento implements Comparable<Evento>  {

	public enum EventType{
		INIZIO_REV,
		FINE_REV
	}
	
	private int time;
	private Tecnico tec;
	private int durata;
	private EventType type;
	
	
	public Evento(int time, Tecnico tec, int durata, EventType type) {
		super();
		this.time = time;
		this.tec = tec;
		this.durata = durata;
		this.type = type;
	}












	public int getTime() {
		return time;
	}












	public void setTime(int time) {
		this.time = time;
	}












	public Tecnico getTec() {
		return tec;
	}












	public void setTec(Tecnico tec) {
		this.tec = tec;
	}












	public int getDurata() {
		return durata;
	}












	public void setDurata(int durata) {
		this.durata = durata;
	}












	public EventType getType() {
		return type;
	}












	public void setType(EventType type) {
		this.type = type;
	}












	@Override
	public int compareTo(Evento o) {
		return this.time-o.time;
	}
	
	
	
	
}
