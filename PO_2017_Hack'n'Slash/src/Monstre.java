import java.util.ArrayList;

public abstract class Monstre extends EtreVivant {
    protected long tempTime;
	protected ArrayList<Item> tresor;
	protected boolean gotKey;

	// constructeur
	public Monstre(String nom, int x, int y, int pDV, int pAtk, int pDef, boolean k) {
		super(nom, x, y, pDV, pAtk, pDef);
		tresor = new ArrayList<Item>();
		this.gotKey = k;
		this.tempTime = System.nanoTime();
	}

	public int getPointsDeVie() {
		return this.pointsDeVie;
	}

	// deplace le monstre
	public abstract boolean deplacer(Joueur J,long time);
	/**
	 * La fonction joueurAProx permet de detecter si le joueur j est dans le perimetre autour de lui
	 * @param J
	 * @param radius
	 * @return
	 */
	protected boolean joueurAProx(Joueur J, int radius){
		for (int i =  this.getX()-radius; i < this.getX()+radius+1; i++){
			for (int j = this.getY()-radius; j < this.getY()+radius+1; j++){
	
				if(J.position.equals(new Position(i,j))){
					return true;
				}
			}
		}
		
		return false;
	}

	
	public abstract int mourir();

	public void dropContent() {
		if(gotKey){
			GameWorld.addItem(new Cle("key"+this.nom, this.getX(), this.getY()));
		}
		if(Math.random()<=0.7){
			GameWorld.addItem(new Potion("potion"+this.nom, this.getX(), this.getY()+1));
		}
		if(Math.random()<=0.5){
			GameWorld.addItem(new Arme("arme"+this.nom, this.getX()+1, this.getY()+1, 10+(int)(Math.random()*5)));
		}
		if(Math.random()<=0.5){
			GameWorld.addItem(new Armure("armure"+this.nom, this.getX()-1, this.getY()+1, 20+(int)(Math.random()*5)));
		}
	}

}
