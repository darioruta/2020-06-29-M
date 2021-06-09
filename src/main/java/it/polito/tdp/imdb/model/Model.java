package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	Map<Integer, Director> idMap;
	Map<Integer, Movie> movies; 
	Map<Integer,Actor> actors;
	Graph<Director, DefaultWeightedEdge> grafo;
	ImdbDAO dao;
	List<DirectorConPeso> migliore;
	
	
	public Model() {
		
		this.dao= new ImdbDAO();
		this.idMap = new HashMap<Integer, Director>();
		this.movies = new HashMap<Integer, Movie>();
		this.actors = new HashMap<Integer, Actor>();
		this.dao.listAllDirectors(idMap);
		this.dao.listAllActors(actors);
		this.dao.listAllMovies(movies);
	}
	
	public void creaGrafo(int anno) {
		
		
		this.grafo= new SimpleWeightedGraph<Director, DefaultWeightedEdge> (DefaultWeightedEdge.class);
		
		
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(anno));
		
		List<Adiacenza> archi = this.dao.getArchi(anno, idMap);
		
		for (Adiacenza aa : archi) {
			Graphs.addEdge(this.grafo,aa.getD1(), aa.getD2(),aa.getPeso());
		}
		
	}

	
	
	public class ComparatoreDecrescente implements Comparator<DirectorConPeso>{

		@Override
		public int compare(DirectorConPeso o1, DirectorConPeso o2) {
			// TODO Auto-generated method stub
			return -(o1.getPeso()-o2.getPeso());
		}
		
	}
	//stampare i vicini in ordine decrescente di peso
	public List<DirectorConPeso> getViciniConPeso1(Director partenza){
		List<Director> vicini = Graphs.neighborListOf(this.grafo, partenza);
		
		List<DirectorConPeso> result = new ArrayList<>();
		
		for (Director d : vicini) {
			result.add(new DirectorConPeso(d,(int)this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, d))));
		}
		
		Collections.sort(result, new ComparatoreDecrescente());
		
		return result;
	}
	
	private List<DirectorConPeso> getViciniConPeso(DirectorConPeso partenza) {
		
		List<Director> vicini = Graphs.neighborListOf(this.grafo, partenza.getDirector());
		
		List<DirectorConPeso> result = new ArrayList<>();
		
		for (Director d : vicini) {
			result.add(new DirectorConPeso(d,(int)this.grafo.getEdgeWeight(this.grafo.getEdge(partenza.getDirector(), d))));
		}
		
		Collections.sort(result, new ComparatoreDecrescente());
		
		return result;
	}

	
	public class ComparaReg implements Comparator<Director>{

		@Override
		public int compare(Director o1, Director o2) {
			// TODO Auto-generated method stub
			return o1.getId()-o2.getId();
		}
		
	}

	public List<Director> getVertici() {
		// TODO Auto-generated method stub
		List<Director> res = new ArrayList<Director>();
		
		for (Director d : this.grafo.vertexSet()) {
			res.add(d);
		}
		
		Collections.sort(res, new ComparaReg());
		
		return res;
	}
	
	public int getArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	public List<DirectorConPeso> getCammino(Director partenza, int maxAttori){
		this.migliore = new ArrayList<DirectorConPeso>();
		
		List<DirectorConPeso> parziale = new ArrayList<>();
		DirectorConPeso dTemp = new DirectorConPeso(partenza, 0);
		parziale.add(dTemp);
		cerca(parziale,  maxAttori);
		
		return migliore;
		
	}
	
	
	//Problema a riga 156 sia in questo metodo che in quello successivo (riga 184)

	private void cerca(List<DirectorConPeso> parziale, int maxAttori) {
	
		
		if(sommaPesi(parziale)> maxAttori) { 
			if(this.migliore.size()==0) {
				this.migliore= new ArrayList<>(parziale);
				return;
			} else if (parziale.size()> this.migliore.size()) {
				this.migliore= new ArrayList<>(parziale);
				return;
			}
		}
		

		for (DirectorConPeso dp : this.getViciniConPeso(parziale.get(parziale.size()-1))) {
			if(!parziale.contains(dp)) {
				parziale.add(dp);
				cerca(parziale, maxAttori);
				parziale.remove(parziale.size()-1);
				
			}
		}
		
		
		
	}
	
	/*private void cerca(List<DirectorConPeso> parziale, int maxAttori) {
	
		
		if(sommaPesi(parziale)> maxAttori) { //sono arrivato al fondo adesso decido se cambiare ramo
			if(this.migliore.size()==0) {
				this.migliore= new ArrayList<>(parziale);
				return;
			} else if (parziale.size()> this.migliore.size()) {
				this.migliore= new ArrayList<>(parziale);
				return;
			}
		}
		
		DirectorConPeso ultimo = parziale.get(parziale.size()-1);
		
		for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(ultimo.getDirector())) {
			
			DirectorConPeso prossimo = new DirectorConPeso (this.grafo.getEdgeTarget(e),(int) this.grafo.getEdgeWeight(e));
			
			if(!parziale.contains(prossimo)) {
				parziale.add(prossimo);
				cerca(parziale, maxAttori);
				parziale.remove(parziale.size()-1);
				
			}
		}
	}
	
	*/

	
	private int sommaPesi(List<DirectorConPeso> parziale) {
		
		int somma =0;
		
		for (DirectorConPeso dp : parziale) {
			
			somma+=dp.getPeso();
			
		}
		
		return somma;
	}


	
	

}
