
public class Potion extends Consommable {
	
	private int bonus;

	public Potion(String nom, int x, int y) {
		super(nom, x, y);
		this.bonus = 10;
	}
	
	public int getBonus() {
		return this.bonus;
	}

	@Override
	public void dessine() {
		
		StdDraw.picture(0.007+((this.getX()/(double)GameWorld.getTailleGrille())),(0.007+(this.getY()/(double)GameWorld.getTailleGrille())), "Pot.png",(double)2.5/(double)GameWorld.getTailleGrille(),(double)2.5/(double)GameWorld.getTailleGrille());
		
		
	}

}
