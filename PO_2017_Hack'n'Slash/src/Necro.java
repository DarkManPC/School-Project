import java.util.ArrayList;

public class Necro extends Hunter{
	
	private int nbEnfant;
	private long spendTime;
	private ArrayList<Hunter> child;

	public Necro(String nom, int x, int y, int pDV, int pAtk, int pDef, boolean k) {
		super(nom, x, y, pDV, pAtk, pDef, k);
		this.nbEnfant = 0;
		this.spendTime = System.nanoTime();
		this.child = new ArrayList<Hunter>();
	}
	/**
	 * Cree un enfant du type Hunter à intervalle de 15 secondes.
	 * Pour chaque enfant cree, le parent reçoit 5 points de vie
	 */
	
	public void enfant(){
		if (System.nanoTime() - this.spendTime > (long)(15*Math.pow(10, 9))){
			Hunter e = new Hunter(this.getNom()+String.valueOf(this.nbEnfant), this.getX(), this.getY(), this.pointsDeVie/2, this.pointsDAttaque/2, this.pointsDeDefense/2, false);
			GameWorld.addMonstre(e);
			child.add(e);
			
			this.nbEnfant++;
			this.spendTime = System.nanoTime();
			
			this.pointsDeVie += 5;
			
		}
	}
	
	@Override
	public int mourir(){
		super.mourir();
		for(Hunter h : child){
			h.mourir();
			GameWorld.remove(h);
		}
		return 30;
	}
	
	@Override
	public void dessine() {
		StdDraw.setPenColor(StdDraw.ORANGE);
		StdDraw.filledSquare((this.getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getY() + 0.5) / GameWorld.getTailleGrille(), 0.5 / GameWorld.getTailleGrille());
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text((this.getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getY() + 0.5) / GameWorld.getTailleGrille(), String.valueOf(this.pointsDeVie));
		
	}

}
