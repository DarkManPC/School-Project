
public class Zombie extends Monstre{
	
	private int orientation;
/**
 * La classe Zombie est une sous classe de monstre qui invoque des monstres plus lent mais qui tape plus fort

 */
	public Zombie(String nom, int x, int y, int pDV, int pAtk, int pDef) {
	
		super(nom, x, y, pDV, pAtk, pDef, false);
		orientation = (int)(Math.random()*8);
	}
	/**
	 * La fonction deplacer fait en sorte que dans un premier temps les monstres se deplacent de facon aleatoire sans rester bloquer a une certaine vitesse et lorque le joueur j est present dans le perimetre de celui-ci il se met a le poursuivre.
	 *@param Joueur J
	 *@param long Time
	 */
	@Override
	
	public boolean deplacer(Joueur J, long time) {
		if(time-super.tempTime > 1_300_000_000 ) {
		
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

	@Override
	public void attaquer(EtreVivant e) {
		
		if (e.position.equals(this.getCible())){
			if(this.pointsDAttaque - e.pointsDeDefense > 0){
				e.pointsDeVie -= (this.pointsDAttaque - e.pointsDeDefense);
				}
		}

		
	}

	@Override
	public void dessine() {
		StdDraw.setPenColor(StdDraw.CYAN);
		StdDraw.filledSquare((this.getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getY() + 0.5) / GameWorld.getTailleGrille(), 0.5 / GameWorld.getTailleGrille());
		StdDraw.filledSquare((this.getCible().getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getCible().getY() + 0.5) / GameWorld.getTailleGrille(), 0.3 / GameWorld.getTailleGrille());
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text((this.getX() + 0.5) / GameWorld.getTailleGrille(),
				(this.getY() + 0.5) / GameWorld.getTailleGrille(), String.valueOf(this.pointsDeVie));
		
	}
	
	
	
	@Override
	public int mourir() {
		this.dropContent();
		GameWorld.remove(this);
		return 10;
	}
	
	@Override
	public void dropContent() {
		if(Math.random()<=0.5){
			GameWorld.addItem(new Potion("potion"+this.nom, this.getX(), this.getY()+1));
		}
		if(Math.random()<=0.5){
			GameWorld.addItem(new Potion("potion"+this.nom, this.getX()+1, this.getY()+1));
		}
		if(Math.random()<=0.5){
			GameWorld.addItem(new Potion("potion"+this.nom, this.getX()-1, this.getY()+1));
		}
	}
}
