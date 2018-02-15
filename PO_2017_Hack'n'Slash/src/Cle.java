
public class Cle extends Item {
	
	

	public Cle(String nom, int x, int y) {
		super(nom, x, y);
	}
	
	
	@Override
	public void dessine() {
		
		StdDraw.picture((0.007+(this.getX()/(double)GameWorld.getTailleGrille())),(0.008+(this.getY()/(double)GameWorld.getTailleGrille())), "key.png",(double)1/(double)GameWorld.getTailleGrille(),(double)1/(double)GameWorld.getTailleGrille());
			

	}
	
	
	

}
