package it.polito.tdp.nyc.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private Graph<String,DefaultWeightedEdge> grafo;
	private NYCDao dao;
	
	public Model() {
		this.dao = new NYCDao();
	}
	
	public String creaGrafo(String provider) {
		this.grafo = new SimpleWeightedGraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<String> vertici = dao.getCity(provider);
		Graphs.addAllVertices(this.grafo, vertici);
		
		List<Coppia> coppie = dao.getCoppie(provider);
		for(Coppia c : coppie) {
			LatLng ll1 = new LatLng(c.getLat1(), c.getLon1());
			LatLng ll2 = new LatLng(c.getLat2(), c.getLon2());
			double peso = LatLngTool.distance(ll1, ll2, LengthUnit.KILOMETER);
			Graphs.addEdgeWithVertices(this.grafo, c.getC1(), c.getC2(), peso);
		}
		return "#VERTICI: "+this.grafo.vertexSet().size()+"\n#ARCHI: "+this.grafo.edgeSet().size();
	}
	
	public List<String> getV(){
		List<String> v = new LinkedList<>(this.grafo.vertexSet());
		return v;
	}
	
	
	public List<Dist> getAdiacenti(String city){
		List<Dist> adiacenti = new LinkedList<>();
		
		for(String c : Graphs.neighborListOf(this.grafo, city)) {
			Dist d = new Dist(c, this.grafo.getEdgeWeight(this.grafo.getEdge(c, city)));
			adiacenti.add(d);
		}
		Collections.sort(adiacenti);
		return adiacenti;
	}
	
	
	
	
	
	
	public Graph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<String, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public NYCDao getDao() {
		return dao;
	}

	public void setDao(NYCDao dao) {
		this.dao = dao;
	}

	public List<String> getProviders(){
		return dao.getProviders();
	}
}
