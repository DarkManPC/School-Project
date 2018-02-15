
public abstract class Entite {
	// le nom de notre entite
	protected String nom;
	
	// la position de l'entite
	protected Position position;
	
	public Entite(String nom, int x, int y) {
		this.nom = nom;
		position = new Position(x, y);
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public int getX() {
		return position.getX();
	}
	
	public int getY() {
		return this.position.getY();
	}
	
	public String toString() {
		return nom;
	}
	
	public void setPosition(Position p){
		this.position = p;
	}
	// dessine l'entite, aux bonnes coordonnees
	public abstract void dessine();
	

}
