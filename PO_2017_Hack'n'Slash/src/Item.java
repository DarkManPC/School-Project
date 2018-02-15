
public class Item extends Entite {


	public Item(String nom, int x, int y) {
		super(nom, x, y);
	}

	public void dessine() {

		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.filledSquare((this.getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getY() + 0.5) / GameWorld.getTailleGrille(), 0.3 / GameWorld.getTailleGrille());
		StdDraw.setPenColor(StdDraw.BLACK);

	}
	
	public String toString(){
		return nom;
	}



}
