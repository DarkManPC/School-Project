
public abstract class Outil extends Item {

	private int bonus;
	
	public Outil(String nom, int x, int y, int bonus) {
		super(nom, x, y);
		this.bonus = bonus;
	}
	
	public Outil(String nom, int x, int y, int bonus, boolean estPose){
		super(nom, x, y);
	}
	
	public int getBonus(){
		return this.bonus;
	}
	
	@Override
	public void dessine() {
	}

}
