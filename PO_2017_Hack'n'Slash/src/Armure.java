
public class Armure extends Outil {

	public Armure(String nom, int x, int y, int bonus) {
		super(nom, x, y, bonus);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void dessine() {
		
		StdDraw.picture((0.006+this.getX()/(double)GameWorld.getTailleGrille()),((0.006+this.getY()/(double)GameWorld.getTailleGrille())), "Armor.jpg",(double)1/(double)GameWorld.getTailleGrille(),(double)1/(double)GameWorld.getTailleGrille());
		
	}

}
