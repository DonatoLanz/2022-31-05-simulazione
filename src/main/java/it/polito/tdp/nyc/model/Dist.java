package it.polito.tdp.nyc.model;

public class Dist implements Comparable<Dist>{

	private String c1;
	private double dist;
	
	public Dist(String c1, double dist) {
		super();
		this.c1 = c1;
		this.dist = dist;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	@Override
	public int compareTo(Dist o) {
		return (int)(this.dist*100 - o.dist*100) ;
	}
	
	
	
}
