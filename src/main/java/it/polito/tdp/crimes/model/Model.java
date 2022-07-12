package it.polito.tdp.crimes.model;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	Graph<District, DefaultWeightedEdge> grafo;
	List<District> distretti;
	
	
	
	public Model () {
		this.dao = new EventsDao();
	}
	
	public List<Integer> getYears (){
		return this.dao.getYears();
	}
	
	public void creaGrafo(int anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.distretti = this.dao.getDistretti(anno);
		
		Graphs.addAllVertices(this.grafo, this.distretti);
		
		for(District d1 : distretti) {
			for(District d2: distretti) {
				if(!d1.equals(d2)) {
					
					DefaultWeightedEdge e = grafo.getEdge(d2, d1);
					if(e == null) {
						
						double peso = LatLngTool.distance(d1.getCoordinate(), d2.getCoordinate(), LengthUnit.KILOMETER);
						
						Adiacenza a = new Adiacenza(d1,d2, peso);
						
						Graphs.addEdge(this.grafo, d1, d2, peso);
					}
				}
			}
		}
	}

	public Graph<District, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	
	public void simula(int anno, int mese, int giorno, int agenti) {
		List<DistrettiCrimini> crimini = this.dao.getDistrettoMenoCrimini(anno);
		
		int min = Integer.MAX_VALUE;
		int idDistretto = 0;
		for(DistrettiCrimini dc : crimini) {
			if(dc.getNumeroCrimini() < min) {
				min = dc.getNumeroCrimini();
				idDistretto = dc.getDistrictID();
			}
		}
		
		District centrale = null;
		
		for(District d : this.distretti) {
			if(d.getDistrictId() == idDistretto)
				centrale = d;
		}
		
		List<Event> criminiGiorno = this.dao.getEventiGiorno(anno, mese, giorno);
		
	}
}
