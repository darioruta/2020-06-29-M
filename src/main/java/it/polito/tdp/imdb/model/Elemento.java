package it.polito.tdp.imdb.model;

public class Elemento {

	int actorId;
	int DirectorId;
	
	public Elemento(int actorId, int directorId) {
		super();
		this.actorId = actorId;
		DirectorId = directorId;
	}

	public int getActorId() {
		return actorId;
	}

	public void setActorId(int actorId) {
		this.actorId = actorId;
	}

	public int getDirectorId() {
		return DirectorId;
	}

	public void setDirectorId(int directorId) {
		DirectorId = directorId;
	}
	
	
	
}
