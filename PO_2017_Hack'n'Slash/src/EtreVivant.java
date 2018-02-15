
public abstract class EtreVivant extends Entite {
	public EtreVivant(String nom, int x, int y, int pDV, int pAtk, int pDef) {
		super(nom, x, y);
		pointsDeVie = pDV;
		pointsDAttaque = pAtk;
		pointsDeDefense = pDef;
		// TODO Auto-generated constructor stub
	}

		// les points de vie du personnage
		protected int pointsDeVie;
		
		//Le nombre de degats infliges aux monstres
		protected int pointsDAttaque;
		
		//La reduction des degats subis par les monstres
		protected int pointsDeDefense;
		
		public abstract int mourir();
		
		public abstract void attaquer(EtreVivant e);
		
		//L'attaque d'agresseur est de atk
		public void prendreDegats(int atk) {

		}
}
