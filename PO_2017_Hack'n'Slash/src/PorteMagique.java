import java.util.ArrayList;

public class PorteMagique extends Entite {

	private boolean porteOuverte;
	private ArrayList<Cle> cle;

	public PorteMagique(String nom, int x, int y) {
		super(nom, x, y);
		this.porteOuverte = false;
		this.cle = new ArrayList<Cle>();
	}

	@Override
	public void dessine() {
		
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.filledSquare((this.getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getY() + 0.5) / GameWorld.getTailleGrille(), 0.5 / GameWorld.getTailleGrille());
		StdDraw.setPenColor(StdDraw.BLACK);
	}

	public void placer(Cle c) {
		this.cle.add(c);
		if(this.cle.size() >= 4){
			this.porteOuverte = true;
		}
	}

	public boolean isOpen() {
		return this.porteOuverte;
	}
}
