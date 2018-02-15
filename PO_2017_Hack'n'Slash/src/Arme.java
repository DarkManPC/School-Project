
public class Arme extends Outil {

	public Arme(String nom, int x, int y, int bonus) {
		super(nom, x, y, bonus);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void dessine() {
		
		StdDraw.picture(0.004+(this.getX()/(double)GameWorld.getTailleGrille()),(0.005+(this.getY()/(double)GameWorld.getTailleGrille())), "sword.png",(double)1/(double)GameWorld.getTailleGrille(),(double)1/(double)GameWorld.getTailleGrille());
		
		
		
	}
	
	

}
