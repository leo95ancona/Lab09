package it.polito.tdp.metrodeparis.model;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;



public class FermataTraversalListener implements TraversalListener<Fermata, DefaultWeightedEdge> {
	
	
	private Graph<Fermata, DefaultWeightedEdge> graph ;
	private Map<Fermata,Fermata> map ;
	
	public FermataTraversalListener(Graph<Fermata, DefaultWeightedEdge> graph, java.util.Map<Fermata, Fermata> map) {
		super();
		this.graph = graph;
		this.map = map;
	}

	@Override
	public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edgeTraversed(EdgeTraversalEvent<Fermata,DefaultWeightedEdge> evento) {
		Fermata c1 = graph.getEdgeSource( evento.getEdge()) ;
		Fermata c2 = graph.getEdgeTarget(evento.getEdge()) ;
		
		if(map.containsKey(c1) && map.containsKey(c2))
			return ;
		
		if( !map.containsKey(c1) ) {
			// c1 è quello nuovo
			map.put(c1,  c2) ;
		} else {
			// c2 è quello nuovo
			map.put(c2,  c1) ;
		}
		
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<Fermata> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<Fermata> arg0) {
		// TODO Auto-generated method stub
		
	}

	

}
