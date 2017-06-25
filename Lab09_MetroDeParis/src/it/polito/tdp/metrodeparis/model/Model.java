package it.polito.tdp.metrodeparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;


import it.polito.tdp.metrodeparis.dao.MetroDAO;

public class Model {
	
	private WeightedGraph<Fermata, DefaultWeightedEdge> grafo;
	private List <Fermata> fermate;
	private Map <Fermata,Fermata>  alberoVisita;
	private Map <Integer,Linea>mappaLinee=null;
	private int tempoPercorrenza;
	private List<DefaultWeightedEdge> listaArchi;
	private double tempoTot;
	
	

	public List<Fermata> getStazioni() {
		if(this.fermate==null) {
			MetroDAO dao = new MetroDAO() ;
			this.fermate = dao.getAllFermate();
		}
		return this.fermate ;
	}
	
	public void creaGrafo(){
		
		this.grafo = new WeightedMultigraph <Fermata, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(grafo, this.getStazioni());
		
		MetroDAO dao = new MetroDAO() ;
		
		//aggiungo archi
		for (Fermata f1 : grafo.vertexSet()){
			List<Fermata> adiacenti = dao.listAdiacenti(f1) ;
				for (Fermata f2 : adiacenti){
					DefaultWeightedEdge ed = grafo.addEdge(f1, f2);
					 grafo.setEdgeWeight(ed, this.calcolaPeso(f1,f2));
					
				}
		}
		
		System.out.println(grafo);
	}

	public double calcolaDistanza(Fermata f1, Fermata f2) {
		
		return LatLngTool.distance(f1.getCoords() , f2.getCoords(), LengthUnit.KILOMETER);
	}
	
	
	public List<Fermata> getRaggiungibili(Fermata partenza) {
		
		//CONTROLLO INIZIALE GRAFO : controllo se il grafo è stato creato, se no lo creo
		if (grafo == null){
			this.creaGrafo();
		}
		
		//1 : creo iteratore che esplora in larghezza il grafo e segna ogni volta l'arco e il nodo superato, dove posso gia inserire il nodo di partenza per scoprire i nodi raggiungibili
		BreadthFirstIterator<Fermata, DefaultWeightedEdge> bfi = new BreadthFirstIterator<Fermata, DefaultWeightedEdge>(grafo, partenza) ;
		
		//2 : creo una lista dove salvare i risultati
		List<Fermata> list = new ArrayList<>() ;
		
		//1b : creo un albero dove segnare il nodo di partenza e nodo successivo per trovare il percorso
		Map<Fermata ,Fermata> albero = new HashMap<>() ;
		
		//2b : inserisco nella mappa appena creata il primo nodo di partenza
		albero.put(partenza, null) ;
		
		//3b: aggiungo un Traversal Listener (FermataTraversalListener) che sente ogni passaggio dell'iteratore e a cui passiamo il grafo e l'albero 
		// e con cui riempiamo l'albero(mappa) con coppie di nodo partenza e nodo successivo, per poi ricostruire tutto il cammino
		bfi.addTraversalListener(new FermataTraversalListener(grafo, albero));
		
		//3 : finchè l'iteratore ha un nodo next che ha visitato, salvo questo nodo nella lista di risultati
		while(bfi.hasNext()) {
			list.add(bfi.next()) ;
		}
		
		this.alberoVisita=albero;
		
		
		
		//4: ritorno la lista di risultati
		return list ;
	}

	public List<Fermata> getPercorso(Fermata destinazione) {
		List<Fermata> percorso = new ArrayList<Fermata>();
	
		Fermata c = destinazione;
		
		while (c!=null){
			percorso.add(c);
			c = alberoVisita.get(c); // mappa<c,b> .. "c" è la chiave, ed è anche il nodo che è collegato grazie a "b" che viene prima
			//così con il metodo get(c) ritorna il nodo da cui proviene "c", andando al contrario fino ad arrivare al nodo di partenza
		}
		return percorso;
	}
	
	public double calcolaPeso(Fermata f1, Fermata f2) {
		
		if(mappaLinee==null){
			MetroDAO mDAO = new MetroDAO();
			mappaLinee=mDAO.getListaLinee();
		}
		
		double result;
		if(f1.getIdFermata()==f2.getIdFermata()){
			result=(mappaLinee.get(f2.getLinea()).getIntervallo())/60;  //trasformo in ore
		}
		else{
		double distanza=LatLngTool.distance(f1.getCoords(),f2.getCoords(), LengthUnit.KILOMETER);
		double velocitaLinea= mappaLinee.get(f1.getLinea()).getVelocita();
		result=distanza/velocitaLinea;
		}
		return  result;
	}
	
	public String calcolaPercorsoMinimo(Fermata partenza, Fermata arrivo){
		
		
		DijkstraShortestPath<Fermata, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<Fermata, DefaultWeightedEdge>(grafo, partenza, arrivo);
		
		List<DefaultWeightedEdge> listaArchi;
		double tempoTot;
		
		listaArchi = dijkstra.getPathEdgeList();
		tempoTot = dijkstra.getPathLength();

		if (listaArchi == null)
			throw new RuntimeException("Non è stato possiible crare un percorso.");

		// Nel calcolo del tempo non tengo conto della prima e dell'ultima fermata
		if (listaArchi.size() - 1 > 0) {
			tempoTot += (listaArchi.size() - 1) * 30;
		}
		
		StringBuilder risultato = new StringBuilder();
		risultato.append("Percorso: [ ");

		for (DefaultWeightedEdge ed : listaArchi) {
			risultato.append(grafo.getEdgeTarget(ed).getNome());
			risultato.append(", ");
		}
		risultato.setLength(risultato.length() - 2);
		risultato.append("]");
		
		
		return risultato.toString();
	}
	
	
	public String getPercorsoEdgeList() {
		
		if (listaArchi == null)
			throw new RuntimeException("Non è stato creato alcun percorso.");

		StringBuilder risultato = new StringBuilder();
		risultato.append("Percorso: [ ");

		for (DefaultWeightedEdge ed : listaArchi) {
			risultato.append(grafo.getEdgeTarget(ed).getNome());
			risultato.append(", ");
		}
		risultato.setLength(risultato.length() - 2);
		risultato.append("]");

		return risultato.toString();
	}
	
	
	


}
