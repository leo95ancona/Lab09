package it.polito.tdp.metrodeparis.model;

public class Linea {
	private int id ;
	private String nome;
	private double velocita;
	private double intervallo;
	private String colore;

public Linea(int id, String nome, double velocita, double intervallo, String colore) {
	super();
	this.id = id;
	this.nome = nome;
	this.velocita = velocita;
	this.intervallo = intervallo;
	this.colore = colore;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getNome() {
	return nome;
}
public void setNome(String nome) {
	this.nome = nome;
}
public double getVelocita() {
	return velocita;
}
public void setVelocita(double velocita) {
	this.velocita = velocita;
}
public double getIntervallo() {
	return intervallo;
}
public void setIntervallo(double intervallo) {
	this.intervallo = intervallo;
}
public String getColore() {
	return colore;
}
public void setColore(String colore) {
	this.colore = colore;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + id;
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Linea other = (Linea) obj;
	if (id != other.id)
		return false;
	return true;
}
}

