package it.polito.tdp.nyc.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.nyc.db.NYCDao;
import it.polito.tdp.nyc.model.Evento.EventType;

public class Simulatore {

	//codaDegliEventi
	private PriorityQueue<Evento> coda;
	//input
	private String provider;
	private String quartierePartenza;
	private int nTecnici;
	//output
	private List<Tecnico> listaTec;
	private double durata;
	//statusMondo
	private NYCDao dao;
	private Graph<String,DefaultWeightedEdge> grafo;
	private String quartiereCorrente;
	private List<String> quartieriVisitati;
	private Map<String,Integer> mapHotSpotQuartieri;
	private int nHotSpotDaVisitare;
	private int nTecLiberi;
	
	public Simulatore(Graph<String,DefaultWeightedEdge> grafo,String provider,String quartierePartenza,int nTecnici) {
		this.grafo = grafo;
		this.provider = provider;
		this.quartierePartenza = quartierePartenza;
		this.nTecnici = nTecnici;
		this.dao = new NYCDao();
		this.quartiereCorrente = this.quartierePartenza;
		this.quartieriVisitati = new LinkedList<>();
		this.quartieriVisitati.add(quartierePartenza);
		this.mapHotSpotQuartieri = new HashMap<>();
		this.listaTec = new LinkedList<>();
	}
	
	public void init() {
		List<HotQ> listaHQ = dao.getHQ(this.provider);
		for(HotQ h : listaHQ) {
			this.mapHotSpotQuartieri.put(h.getQ(), h.getHot());
		}
		
		this.nTecLiberi = this.nTecnici;
		this.nHotSpotDaVisitare = this.mapHotSpotQuartieri.get(this.quartierePartenza);
		for(int i=0; i<this.nTecnici;i++) {
			listaTec.add(new Tecnico(i, 0));
		}
		
		//caricoLaCoda
		this.coda = new PriorityQueue<>();
		if(this.nHotSpotDaVisitare > this.nTecnici) {
			for(int i=0; i<this.nTecnici; i++) {
				if(Math.random()<0.9) {
					this.coda.add(new Evento(1, listaTec.get(i), 10, EventType.INIZIO_REV));
					this.nHotSpotDaVisitare--;
					this.nTecLiberi--;
					listaTec.get(i).incrementa();
				}else {
					this.coda.add(new Evento(1, listaTec.get(i), 25, EventType.INIZIO_REV));
					this.nHotSpotDaVisitare--;
					this.nTecLiberi--;
					listaTec.get(i).incrementa();
				}
			}
			
		}else {
			for(int i=0; i<this.nHotSpotDaVisitare; i++) {
				if(Math.random()<0.9) {
					this.coda.add(new Evento(1, listaTec.get(i), 10, EventType.INIZIO_REV));
					this.nHotSpotDaVisitare--;
					this.nTecLiberi--;
					listaTec.get(i).incrementa();
				}else {
					this.coda.add(new Evento(1, listaTec.get(i), 25, EventType.INIZIO_REV));
					this.nHotSpotDaVisitare--;
					this.nTecLiberi--;
					listaTec.get(i).incrementa();
				}
			}
			
		}
		System.out.println("CODA"+this.coda.size());
		
	}
	
	public void run() {
		System.out.println(this.quartiereCorrente);
		while(!this.coda.isEmpty()) {
			
			Evento e = this.coda.poll();
			System.out.println(e.getTime()+" "+e.getType()+" "+e.getTec().getId());
			this.durata = e.getTime();
			processEvent(e);
		}
		System.out.println(this.nHotSpotDaVisitare+" "+this.nTecLiberi+" "+this.nTecnici+" "+this.quartieriVisitati.size()+" "+this.grafo.vertexSet().size());
	}

	private void processEvent(Evento e) {
		switch(e.getType()) {
		case INIZIO_REV:
		 this.coda.add(new Evento(e.getTime()+e.getDurata(), e.getTec(), 0, EventType.FINE_REV));	
		break;
		
		case FINE_REV:
			if(this.nHotSpotDaVisitare > 0) { //sonoDispAncoraH
				int spostamentoH = (int) (Math.random()*10) + 10;
				if(Math.random() < 0.9) {
					this.coda.add(new Evento(e.getTime()+spostamentoH, e.getTec(), 10, EventType.INIZIO_REV));
					this.nHotSpotDaVisitare--;
					e.getTec().incrementa();
				}else {
					this.coda.add(new Evento(e.getTime()+spostamentoH, e.getTec(), 25, EventType.INIZIO_REV));
					this.nHotSpotDaVisitare--;
					e.getTec().incrementa();
				}
				
			}else if(this.nHotSpotDaVisitare == 0 && this.nTecLiberi < this.nTecnici-1) { //aspettoChegliAltriFiniscano
				  this.nTecLiberi++;
				  
			}else if(this.nHotSpotDaVisitare == 0 && this.nTecLiberi == this.nTecnici-1 && this.quartieriVisitati.size() < this.grafo.vertexSet().size()) {
				this.nTecLiberi++;
				System.out.println("SONO NEL CAMBIO QUARTIERE");
				System.out.println(this.quartiereCorrente);
				String prossimo = quartiereVic(quartieriVisitati, quartiereCorrente);
				System.out.println(prossimo);
				this.quartiereCorrente = prossimo;
				System.out.println(this.quartiereCorrente);
				this.quartieriVisitati.add(quartiereCorrente);
				this.nHotSpotDaVisitare = this.mapHotSpotQuartieri.get(quartiereCorrente);
				if(this.nHotSpotDaVisitare > this.nTecnici) {
					for(int i=0; i<this.nTecnici; i++) {
						if(Math.random()<0.9) {
							this.coda.add(new Evento(e.getTime(), listaTec.get(i), 10, EventType.INIZIO_REV));
							this.nHotSpotDaVisitare--;
							this.nTecLiberi--;
							listaTec.get(i).incrementa();
						}else {
							this.coda.add(new Evento(e.getTime(), listaTec.get(i), 25, EventType.INIZIO_REV));
							this.nHotSpotDaVisitare--;
							this.nTecLiberi--;
							listaTec.get(i).incrementa();
						}
					}
					
				}else {
					for(int i=0; i<this.nHotSpotDaVisitare; i++) {
						if(Math.random()<0.9) {
							this.coda.add(new Evento(e.getTime(), listaTec.get(i), 10, EventType.INIZIO_REV));
							this.nHotSpotDaVisitare--;
							this.nTecLiberi--;
							listaTec.get(i).incrementa();
						}else {
							this.coda.add(new Evento(e.getTime(), listaTec.get(i), 25, EventType.INIZIO_REV));
							this.nHotSpotDaVisitare--;
							this.nTecLiberi--;
							listaTec.get(i).incrementa();
						}
					}
					
				}
			}
			
		break;	
		}
		
	}
	
	public String quartiereVic(List<String> visitati,String corrente) {
		String quarV = null;
		double min = 100000000;
		
		for(String s : Graphs.neighborListOf(this.grafo, corrente)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(corrente, s)) < min && !visitati.contains(s)) {
				min = this.grafo.getEdgeWeight(this.grafo.getEdge(corrente, s)); 
			}
		}
		
		for(String s : Graphs.neighborListOf(this.grafo, corrente)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(corrente, s)) == min && !visitati.contains(s)) {
				quarV = s; 
				break;
			}
		}
		return quarV;
	}

	public PriorityQueue<Evento> getCoda() {
		return coda;
	}

	public void setCoda(PriorityQueue<Evento> coda) {
		this.coda = coda;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getQuartierePartenza() {
		return quartierePartenza;
	}

	public void setQuartierePartenza(String quartierePartenza) {
		this.quartierePartenza = quartierePartenza;
	}

	public int getnTecnici() {
		return nTecnici;
	}

	public void setnTecnici(int nTecnici) {
		this.nTecnici = nTecnici;
	}

	public List<Tecnico> getListaTec() {
		return listaTec;
	}

	public void setListaTec(List<Tecnico> listaTec) {
		this.listaTec = listaTec;
	}

	public double getDurata() {
		return durata;
	}

	public void setDurata(double durata) {
		this.durata = durata;
	}

	public NYCDao getDao() {
		return dao;
	}

	public void setDao(NYCDao dao) {
		this.dao = dao;
	}

	public Graph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<String, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public String getQuartiereCorrente() {
		return quartiereCorrente;
	}

	public void setQuartiereCorrente(String quartiereCorrente) {
		this.quartiereCorrente = quartiereCorrente;
	}

	public List<String> getQuartieriVisitati() {
		return quartieriVisitati;
	}

	public void setQuartieriVisitati(List<String> quartieriVisitati) {
		this.quartieriVisitati = quartieriVisitati;
	}

	public Map<String, Integer> getMapHotSpotQuartieri() {
		return mapHotSpotQuartieri;
	}

	public void setMapHotSpotQuartieri(Map<String, Integer> mapHotSpotQuartieri) {
		this.mapHotSpotQuartieri = mapHotSpotQuartieri;
	}

	public int getnHotSpotDaVisitare() {
		return nHotSpotDaVisitare;
	}

	public void setnHotSpotDaVisitare(int nHotSpotDaVisitare) {
		this.nHotSpotDaVisitare = nHotSpotDaVisitare;
	}

	public int getnTecLiberi() {
		return nTecLiberi;
	}

	public void setnTecLiberi(int nTecLiberi) {
		this.nTecLiberi = nTecLiberi;
	}
	
	
	
	
	
	
}
