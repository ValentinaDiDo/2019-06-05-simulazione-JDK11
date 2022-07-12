package it.polito.tdp.crimes.model;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.Evento.EventType;

public class Simulator {

	//DATI IN INGRESSO
	Graph<District, DefaultWeightedEdge> grafo;
	List<Event> crimini; //CRIMINI AVVENUTI NEL GIORNO SELEZIONATO
	District centrale;
	int AGENTI; //NUMERO DI AGENTI
	Map<Integer, District> distretti;
	
	//DATI IN USCITA
	int eventi_Mal_Gestiti;
	
	//STATO DEL MONDO
	//int agenti_In_Centrale; //inizialmente AGENTI
	Map<District, Integer> mapAgenti; //AGENTI X DISTRETTO
	
	//QUEUE
	PriorityQueue<Evento> queue;

	public Simulator(Graph<District, DefaultWeightedEdge> grafo, List<Event> crimini, District centrale, int aGENTI) {
		super();
		this.grafo = grafo;
		this.crimini = crimini;
		this.centrale = centrale;
		AGENTI = aGENTI;
		
	}
	
	public void init() {
		this.eventi_Mal_Gestiti = 0;
		
		this.distretti = new TreeMap<>();
		
		this.mapAgenti = new TreeMap<>();
		for(District d : this.grafo.vertexSet()) {
			this.mapAgenti.put(d, 0);
			this.distretti.put(d.getDistrictId(), d);
		}
		
		this.mapAgenti.put(this.centrale, this.AGENTI); //INSERISCO TUTTI GLI AGENTI NELLA CENTRALE DI PARTENZA
		
		//INSERISCO GLI EVENTI NELLA QUEUE
		for(Event e : this.crimini) {
			Evento evento = new Evento(e.getReported_date(),e, EventType.CRIMINE);
			this.queue.add(evento);
				
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = queue.poll();
			processEvent(e);
		}
	}
	
	public void processEvent(Evento e) {
		
		switch(e.getType()) {
		
		
		
		case CRIMINE:
			
			District destinazione = this.distretti.get(e.getCrimine().getDistrict_id());
			
			District partenza = null;
			
			partenza = cercaAgente(e.getCrimine().getDistrict_id());
			
			if(partenza != null) {
				//DIMINUISCO IL NUMERO DI AGENTI NELLA PARTENZA
				this.mapAgenti.put(partenza, this.mapAgenti.get(partenza)-1);
				
				//CALCOLO TEMPO DI SPOSTAMENTO
				long secondi = 0;
				double distanza = 0;
				if(partenza.equals(destinazione)) {
					distanza = 0;
				}else{
					distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, partenza));
				}
				
				secondi = (long) (distanza/(60/3.6)); //MI NUOVO A 60 KM/H -> calcolo il tempo in secondi
				
				//CREO IL NUOVO EVENTO
				Evento nuovo = new Evento(e.getData().plusSeconds(secondi), e.getCrimine(), EventType.ARRIVA_AGENTE);
				this.queue.add(nuovo);
			}else {
				// NON HO PIU' AGENTI A DISPOSIZIONE -> EVENTO MAL GESTITO
				this.eventi_Mal_Gestiti++;
			}
			
			break;
			
		case ARRIVA_AGENTE:
				
				//CALCOLO IL TEMPO DI GESTIONE DEL CRIMINE
				int durata = getDurata(e.getCrimine());
				//CREO IL NUOVO EVENTO GESTITO, IN MODO TALE DA AGGIORNARE LE STATISTICHE
				long secondi = durata*3600;
				Evento nuovo = new Evento(e.getData().plusSeconds(secondi), e.getCrimine(), EventType.GESTITO);
				this.queue.add(nuovo);
				//VERIFICO SE MAL GESTITO
				if(e.getData().isAfter( e.getCrimine().getReported_date().plusMinutes(15))) {
					this.eventi_Mal_Gestiti++;
				}
			break;
			
		case GESTITO:
			District distretto = this.distretti.get(e.getCrimine().getDistrict_id());
			//AUMENTO IL NUMERO DI AGENTI NELLA DESTINAZIONE UNA VOLTA CHE L'AGENTE E' ARRIVATO
			this.mapAgenti.put(distretto, this.mapAgenti.get(distretto)+1);
			break;
		}
		
	}
	//SELEZIONO AGENTE LIBERO PIU VICINO
	public District cercaAgente(int district_id) {
		District scelto = null;
		
		District destinazione = this.distretti.get(district_id);
		
		double distanza = Double.MAX_VALUE;
		
		for(District d : this.grafo.vertexSet()) {
			
			if(d.equals(destinazione)) {
				
				if( this.mapAgenti.get(d) > 0) { //	CONTROLLO DALLA MAPPA AGENTI SE IL DISTRETTO D HA AGENTI A DISPOSIZIONE
					distanza = 0.0;
					scelto = d;
				}
			}else {
				double distanzaProv = this.grafo.getEdgeWeight(this.grafo.getEdge(d, destinazione));
				if(distanzaProv < distanza && this.mapAgenti.get(d) > 0 ) {
					distanza = distanzaProv;
					scelto = d;
				}
			}
			
		}
	
		
		return scelto;
	}
	public int getDurata(Event e) {
		int durata = 0;
		
		if(e.getOffense_category_id().compareTo("all_other_crimes")==0) {
			//DURATA 1 O 2 ORE
			double p = Math.random();
			
			if(p<0.5) {
				durata = 1;
			}else
				durata = 2;
		}else {
			durata = 2;
		}
		
		return durata;
	}
}
