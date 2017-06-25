package it.polito.tdp.metrodeparis.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println("TODO: write a Model class and test it!");
		
		model.creaGrafo();
		
		System.out.println(model.getRaggiungibili(new Fermata(10)));
		
		System.out.println(model.getPercorso(new Fermata (5)));
	}

}
