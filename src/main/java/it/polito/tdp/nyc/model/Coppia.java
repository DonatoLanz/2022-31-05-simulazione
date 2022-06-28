package it.polito.tdp.nyc.model;

public class Coppia {

	private String c1;
	private String c2;
	private double lat1;
	private double lon1;
	private double lat2;
	private double lon2;
	
	public Coppia(String c1, String c2, double lat1, double lon1, double lat2, double lon2) {
		super();
		this.c1 = c1;
		this.c2 = c2;
		this.lat1 = lat1;
		this.lon1 = lon1;
		this.lat2 = lat2;
		this.lon2 = lon2;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public double getLat1() {
		return lat1;
	}

	public void setLat1(double lat1) {
		this.lat1 = lat1;
	}

	public double getLon1() {
		return lon1;
	}

	public void setLon1(double lon1) {
		this.lon1 = lon1;
	}

	public double getLat2() {
		return lat2;
	}

	public void setLat2(double lat2) {
		this.lat2 = lat2;
	}

	public double getLon2() {
		return lon2;
	}

	public void setLon2(double lon2) {
		this.lon2 = lon2;
	}
	
	
	
	
	
	
}
