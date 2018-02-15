
public class Piege extends Monstre {
	
	private boolean attack;

	public Piege(String nom, int x, int y, int pDV, int pAtk, int pDef) {
		super(nom, x, y, pDV, pAtk, pDef, false);
		this.attack = false;
	}

	public boolean deplacer(Joueur J, long time) {
		// un pi�ge ne bouge pas
		return false;
	}
	
	//Prend en parametre un EtreVivant et lui enleve des point de vie 

	@Override
	public void attaquer(EtreVivant e) {
		
		if (this.joueurAProx((Joueur) e, 2) && !attack){
			e.prendreDegats(this.pointsDAttaque);
			this.attack = true;
		}
		
		if(this.attack){
			this.mourir();
		}

	}


	@Override
	public void dessine() {
		StdDraw.picture((double)this.getX()/(double)GameWorld.getTailleGrille(), (double)this.getY()/(double)GameWorld.getTailleGrille(), "mineActivee.png");
	}

	@Override
	public int mourir() {
		
		GameWorld.remove(this);
		
		return 0;
	}

	

}
