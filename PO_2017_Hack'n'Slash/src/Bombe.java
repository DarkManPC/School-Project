
public class Bombe extends Consommable {

	//Les dommages inflig�s par la bombe
	private int dommages;

	public Bombe(String nom, int x, int y) {
		super(nom, x, y);
		this.dommages = 30;
	}
	
	@Override
	public void dessine(){
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.filledSquare((this.getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getY() + 0.5) / GameWorld.getTailleGrille(), 0.3 / GameWorld.getTailleGrille());
		StdDraw.setPenColor(StdDraw.BLACK);
	}

	public int getDommages() {
		return this.dommages;
	}

}
