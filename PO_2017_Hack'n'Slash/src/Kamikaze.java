

public class Kamikaze extends Monstre{
	private int orientation;
	private int nbPiege;
	private long spendTime;
	
	
	/**
	 * La calsse Kamikaze est une sous classe de monstre qui invoque des nouveaux monstres qui drop des pieges toutes les 15s et qui explosent lorqu'il nous tape une fois.
	 * @param nom
	 * @param x
	 * @param y
	 * @param pDV
	 * @param pAtk
	 * @param pDef
	 */
	public Kamikaze(String nom, int x, int y, int pDV, int pAtk, int pDef) {
		super(nom, x, y, pDV, pAtk, pDef, false);
		orientation = (int)(Math.random()*8);
	}
	/**
	 * La fonction deplacer fait en sorte que dans un premier temps les monstres se deplacent de facon aleatoire sans rester bloquer a une certaine vitesse et lorque le joueur j est pr�sent dans le perimetre de celui-ci il se met a le poursuivre.
	 *@param Joueur J
	 *@param long Time
	 */
	@Override
	public boolean deplacer(Joueur J, long time) {
		if(time - super.tempTime > 100_000_000 ) {
		
		int sens = 1, random = (int) (Math.random()*3), tmpOrientation = 0;
		boolean inMap = true, trackJoueur = true;
			
		do{	 
			switch (random){
			case 0:
				this.tournerADroite();
			break;
			case 1:
				this.tournerAGauche();
				break;
			default:
				if(!inMap){
					this.tournerAGauche();	
				}
				if(!trackJoueur){
					this.tournerAGauche();	
				}
				break;
			}
			
			
			
			if(this.joueurAProx(J, 5)){
				
				trackJoueur = false;
				
				for(int i = 1; i < 5; i++){
					
					Position joueurTraque = new Position(this.getX()+((this.getCible().getX()-this.getX())*i),this.getY()+((this.getCible().getY()-this.getY())*i));
					
					if(joueurTraque.equals(J.position) || this.position.equals(J.position) || tmpOrientation > 7){
						
						trackJoueur = true;
						break;
					}
				}
			}
			
			tmpOrientation++;	
			
			
			inMap = (this.getCible().getX() >= 0 && this.getCible().getX() <= GameWorld.getTailleGrilleX()-1 
					&& this.getCible().getY() >= 0 && this.getCible().getY() <= GameWorld.getTailleGrilleY()-1);
			
			
		}while (!inMap || !trackJoueur);
		
		
		
		switch (orientation) {
		case 0:
			position.setY(getY() + sens);
			break;
		case 1:
			position.setX(getX() + sens);
			position.setY(getY() + sens);
			break;
		case 2:
			this.position.setX(getX() + sens);
			break;
		case 3:
			this.position.setX(getX() + sens);
			this.position.setY(getY() - sens);
			break;
		case 4:
			this.position.setY(getY() - sens);
			break;
		case 5:
			this.position.setX(getX() - sens);
			this.position.setY(getY() - sens);
			break;
		case 6:
			this.position.setX(getX() - sens);
			break;
		case 7:
			this.position.setX(getX() - sens);
			this.position.setY(getY() + sens);
			break;
		default:
			break;
		}
		super.tempTime = time;
		return true;
		}
		
		return false;	

	}

	public void tournerADroite() {
		this.orientation = (this.orientation + 1) % 8;
	}

	public void tournerAGauche() {
		this.orientation = (((this.orientation - 1) % 8) + 8) % 8;
	}
	
	public Position getCible() {
		
		Position p = new Position(0,0);
		
		switch (orientation) {
		case 0:
			p.setX(getX());
			p.setY(getY() + 1);
			break;
		case 1:
			p.setX(getX() + 1);
			p.setY(getY() + 1);
			break;
		case 2:
			p.setX(getX() + 1);
			p.setY(getY());
			break;
		case 3:
			p.setX(getX() + 1);
			p.setY(getY() - 1);
			break;
		case 4:
			p.setY(getY() - 1);
			p.setX(getX());
			break;
		case 5:
			p.setX(getX() - 1);
			p.setY(getY() - 1);
			break;
		case 6:
			p.setX(getX() - 1);
			p.setY(getY());
			break;
		case 7:
			p.setX(getX() - 1);
			p.setY(getY() + 1);
			break;
		default:
			break;
		}

		return p;
	}
/**
 * La fonction permet aux Kamikazes de poser un piege toutes les 15 secondes a l'endroit ou il se trouve (x,y)
 */
	public void piege(){
		if (System.nanoTime() - this.spendTime > (long)(15*Math.pow(10, 9))){
			Piege e = new Piege (this.getNom()+String.valueOf(this.nbPiege), this.getX(), this.getY(), 10, 10, 15);
			GameWorld.addMonstre(e);
			
			this.nbPiege++;
			
			this.spendTime = System.nanoTime();
			
			
			
		}
	}
	
	/**
	 * La fonction attaquer de cette classe fais en sorte que le kamikaze ne tape qu'une fois avant de mourir
	 */
	@Override
	public void attaquer(EtreVivant e) {
		
		if (e.position.equals(this.getCible())){
			if(this.pointsDAttaque - e.pointsDeDefense > 0){
				e.pointsDeVie -= (this.pointsDAttaque - e.pointsDeDefense);
			}
			this.mourir();
		}
		

		
	}

	@Override
	public void dessine() {
		StdDraw.setPenColor(StdDraw.MAGENTA);
		StdDraw.filledSquare((this.getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getY() + 0.5) / GameWorld.getTailleGrille(), 0.5 / GameWorld.getTailleGrille());
		StdDraw.filledSquare((this.getCible().getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getCible().getY() + 0.5) / GameWorld.getTailleGrille(), 0.3 / GameWorld.getTailleGrille());
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text((this.getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getY() + 0.5) / GameWorld.getTailleGrille(), String.valueOf(this.pointsDeVie));
		
	}
	
	/**
	 * 
	 */
	
	@Override
	public int mourir() {
		this.dropContent();
		GameWorld.remove(this);
		return 10;
	}
	
}


