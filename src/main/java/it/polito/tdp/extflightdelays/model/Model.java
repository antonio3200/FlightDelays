package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private SimpleWeightedGraph<Airport,DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer,Airport> idMap;
	
	public Model() {
		this.dao= new ExtFlightDelaysDAO();
		idMap= new HashMap<Integer,Airport>();
		dao.loadAllAirports(idMap);
	}
	
	public void creaGrafo(int x) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//Graphs.addAllVertices(grafo, idMap.values());
		Graphs.addAllVertices(grafo, dao.getVertici(idMap, x));
		
		//aggiungo gli archi
		
		for(Rotta r : dao.getRotte(idMap)) {
			if(this.grafo.containsVertex(r.getA1()) && this.grafo.containsVertex(r.getA2())) {
				DefaultWeightedEdge e= this.grafo.getEdge(r.getA1(), r.getA2());
				if(e==null) {
					Graphs.addEdgeWithVertices(grafo, r.getA1(), r.getA2(),r.getN());
				}
				else {
					double pesoArco= this.grafo.getEdgeWeight(e);
					double pesoNuovo= pesoArco+ r.getN();
					this.grafo.setEdgeWeight(e, pesoNuovo);
				}
			}
		}
		System.out.println("Grafo creato");
		System.out.println("# Vertici= " +grafo.vertexSet().size());
		System.out.println("# Archi= " +grafo.edgeSet().size());
	}

	public  Set<Airport> getVertici() {
		
		return this.grafo.vertexSet();
	}

}
