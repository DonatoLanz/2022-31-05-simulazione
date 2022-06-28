package it.polito.tdp.nyc.model;

public class Tecnico {

	private int id;
	private int nHotSpot;

	public Tecnico(int id, int nHotSpot) {
		super();
		this.id = id;
		this.nHotSpot = nHotSpot;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getnHotSpot() {
		return nHotSpot;
	}

	public void setnHotSpot(int nHotSpot) {
		this.nHotSpot = nHotSpot;
	}
	
	public void incrementa() {
		this.nHotSpot++;
	}
	
}
