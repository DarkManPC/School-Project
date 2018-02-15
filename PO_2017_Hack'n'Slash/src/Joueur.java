import java.util.*;

public class Joueur extends EtreVivant {
		
	private class Triangle {
		Point p1, p2, p3;

		public Triangle(Point p1, Point p2, Point p3) {
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
		}
	}

	private class Point {
		double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	//l'orientation du joueur
	//				7	0	1
	//				6		2
	//				5	4	3
	private int orientation = 0;
	private Triangle[] triangles;
	
	private ArrayList<Item> stuff;
	private HashMap<String, Integer> stuffList;
	private boolean drawStuff;
	private boolean drawTools;
	public int maxLife;
	private Arme armeEquipe;
	private Armure armureEquipe;
	private int cursor;
	private boolean equipTool;
	public int exp;
	public String level;
	public int choix;
	public int bonusPotion;
	public int bonusArmure;
	public int bonusBombe;
	public int bonusArme;

	/**
	 * La classe Joueur gere le personnage principale, ses déplacements, son inventaire et ses outils
	 * @param nom
	 * @param x
	 * @param y
	 * @param pDV
	 * @param pAtk
	 * @param pDef
	 */

	public Joueur(String nom, int x, int y, int pDV, int pAtk, int pDef) {
		super(nom, x, y, pDV, pAtk, pDef);
		initTriangles();
		this.stuff = new ArrayList<Item>();
		this.stuffList = new HashMap<String, Integer>();
		
		this.armureEquipe = new Armure("null", 0,0,0);
		this.armeEquipe = new Arme("null", 0,0,0);
		this.cursor = 0;
		this.equipTool = false;
		
		this.drawStuff = false;
		this.drawTools = false;
		this.maxLife = pDV;
		this.level = "Recrue";
		this.choix = 0;
		this.bonusPotion = 0;
		this.bonusArmure = 0;
		this.bonusBombe = 0;
		this.bonusArme = 0;
	}

	private void initTriangles() {
		triangles = new Triangle[8];
		triangles[0] = new Triangle(new Point(1, 0.5), new Point(0.5, 1), new Point(0, 0.5));
		triangles[1] = new Triangle(new Point(0.85, 0.15), new Point(0.85, 0.85), new Point(0.15, 0.85));
		triangles[2] = new Triangle(new Point(0.5, 0), new Point(1, 0.5), new Point(0.5, 1));
		triangles[3] = new Triangle(new Point(0.15, 0.15), new Point(0.85, 0.15), new Point(0.85, 0.85));
		triangles[4] = new Triangle(new Point(0, 0.5), new Point(0.5, 0), new Point(1, 0.5));
		triangles[5] = new Triangle(new Point(0.15, 0.85), new Point(0.85, 0.15), new Point(0.15, 0.15));
		triangles[6] = new Triangle(new Point(0, 0.5), new Point(0.5, 1), new Point(0.5, 0));
		triangles[7] = new Triangle(new Point(0.85, 0.85), new Point(0.15, 0.85), new Point(0.15, 0.15));
	}

	public int getPointsDeVie() {
		return this.pointsDeVie;
	}

	public void mouvementHaut() {
		this.position.setY(this.position.getY() + 1);
	}

	public void mouvementBas() {
		this.position.setY(this.position.getY() - 1);
	}

	public void mouvementGauche() {
		this.position.setX(this.position.getX() - 1);
	}

	public void mouvementDroite() {
		this.position.setX(this.position.getX() + 1);
	}

	/**
	 * Autorise l'affichage de l'inventaire si les outils ne sont pas deja affiches
	 */
	public void afficheInventaire() {
		if(!drawTools){
			drawStuff = !drawStuff;
		}
	}

	/**
	 * Autorise l'affichage des outils si l'inventaire n'est pas deja affiche
	 */
	public void afficheEquipement() {
		if(!drawStuff){
			drawTools = !drawTools;
		}
	}
	/**
	 * Gere l'affiche du joueur ainsi que l'affichage de l'inventaire et des outils
	 */
	public void dessine() {
		//Dessine le triangle selon l'orientation
		double[] x = { ((double) this.position.getX() + triangles[orientation].p1.x) / GameWorld.getTailleGrille(),
				((double) this.position.getX() + triangles[orientation].p2.x) / GameWorld.getTailleGrille(),
				((double) this.position.getX() + triangles[orientation].p3.x) / GameWorld.getTailleGrille() };
		double[] y = { ((double) this.position.getY() + triangles[orientation].p1.y) / GameWorld.getTailleGrille(),
				((double) this.position.getY() + triangles[orientation].p2.y) / GameWorld.getTailleGrille(),
				((double) this.position.getY() + triangles[orientation].p3.y) / GameWorld.getTailleGrille() };
		StdDraw.filledPolygon(x, y);
		
		if (this.drawStuff){
			this.drawStuff = this.drawStuff();
		}
		if (this.drawTools){
			this.drawTools = this.drawTool();
		}
		
	}
	/**
	 * Dessine les differents elements de l'inventaire ainsi que leur nombre les uns en dessous des autres
	 * @return
	 *  Vrai si il y a un inventaire a afficher
	 *  Faux si l'on veut afficher un inventaire vide
	 */
	private boolean drawStuff(){
		String stringStuff = "";
		double X = (this.getX() + 1.5), Y = (this.getY() + 0.5);
		
		for(String k : this.stuffList.keySet()){
			stringStuff = k + " : " + this.stuffList.get(k);
			StdDraw.textLeft(X/GameWorld.getTailleGrille(), Y/GameWorld.getTailleGrille(), stringStuff);
			Y--;
		}
		
		if(this.stuffList.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}
	/**
	 * Dessine les differents elements des outils ainsi que leur bonus les uns en dessous des autres.
	 * Elle gere aussi la selection de l'armure et de l'arme que l'on veut equiper
	 * @return
	 *  Vrai si il y a des outils a afficher
	 *  Faux si l'on veut afficher un inventaire d'outils vide
	 */
	private boolean drawTool(){
		int index = 0;
		String stringTool = "";
		double X = (this.getX() + 1.5), Y = (this.getY() + 0.5);
		
		for(Item i : stuff){
			if (i.getClass().getSuperclass().getName().equals(Outil.class.getName())){
				if(i.getClass().getName().equals(Arme.class.getName())){
					stringTool = i.getClass().getName() + " : +" + ((Outil) i).getBonus() + " atk";
				}
				else{
					stringTool = i.getClass().getName() + " : +" + ((Outil) i).getBonus() + " def";
				}
				if(i.equals(armeEquipe) || i.equals(armureEquipe)){
					stringTool += " Equip";
				}
				if(this.cursor == index){
					stringTool = "O " + stringTool;
					
					if(this.equipTool){
						if(i.getClass().getName().equals(Arme.class.getName())){
							super.pointsDAttaque -= this.armeEquipe.getBonus() + this.bonusArme;
							this.armeEquipe = (Arme) i;
							super.pointsDAttaque += this.armeEquipe.getBonus() + this.bonusArme;
						}
						else{
							super.pointsDeDefense -= this.armureEquipe.getBonus() + this.bonusArmure;
							this.armureEquipe = (Armure) i;
							super.pointsDeDefense += this.armureEquipe.getBonus() + this.bonusArmure;
						}
						this.equipTool = false;
					}
				}
				StdDraw.textLeft(X/GameWorld.getTailleGrille(), Y/GameWorld.getTailleGrille(), stringTool);
				Y--;
				index++;
			}
		}
		if(index != 0){
			if (this.cursor >= 0){
				this.cursor = this.cursor % index;
			}
			else{
				this.cursor += index;
			}
		}
		
		if(index == 0){
			return false;
		}
		else{
			return true;
		}
	}
	

	@Override
	public int mourir() {
		
		this.pointsDeVie = 0;
		
		return 0;

	}

	/**
	 * Prend en parametre un EtreVivant, lui enleve des points de vie
	 */
	
	@Override
	public void attaquer(EtreVivant e) {
		
		if(this.pointsDAttaque - e.pointsDeDefense > 0){
			e.pointsDeVie -= (this.pointsDAttaque - e.pointsDeDefense);
		}
		if (e.pointsDeVie <= 0){
			this.addExp(e.mourir());
		}

	}
	/**
	 * Attaque tous les monstres devant soi.
	 * @param m les monstres a attauqer
	 */
	public void attaqueIndirect(ArrayList<Monstre> m) {
	
		Bombe b = null;
		
		for(Item i : stuff){
			if(i.getClass().getName().equals(Bombe.class.getName())){
				b = (Bombe) i;
			}
		}
		
		if(b != null){
			
			for(Monstre e: m){
				e.pointsDeVie -= (b.getDommages() + this.bonusBombe - e.pointsDeDefense);
				if (e.pointsDeVie <= 0){
					this.addExp(e.mourir());
				}
			}
			stuff.remove(b);
			GameWorld.remove(b);
		
		}
		
		this.updateInventory();

	}

	//avancer (sens = 1) ou reculer (sens = -1)
	public void seDeplacer(int sens) {
		// TODO Auto-generated method stub
		switch (orientation) {
		case 0:
			position.setY(getY() + sens);
			break;
		case 1:
			position.setX(getX() + sens);
			position.setY(getY() + sens);
			break;
		case 2:
			position.setX(getX() + sens);
			break;
		case 3:
			position.setX(getX() + sens);
			position.setY(getY() - sens);
			break;
		case 4:
			position.setY(getY() - sens);
			break;
		case 5:
			position.setX(getX() - sens);
			position.setY(getY() - sens);
			break;
		case 6:
			position.setX(getX() - sens);
			break;
		case 7:
			position.setX(getX() - sens);
			position.setY(getY() + sens);
			break;
		default:
			break;
		}

	}

	public void tournerADroite() {
		orientation = (orientation + 1) % 8;
	}

	public void tournerAGauche() {
		orientation = (((orientation - 1) % 8) + 8) % 8;
	}


	//retourne la position correspondant � la case en face du joueur
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
	
	// Prend en parametre un int atk et enleve atk point de vie au jouer

	@Override
	public void prendreDegats(int atk) {

		this.pointsDeVie -= (atk - this.pointsDeDefense);
		
	}

	public void ramasser(Item i) {
		this.stuff.add(i);
		this.updateInventory();
	}
	
	private void updateInventory(){
		this.stuffList.clear();
		for(Item i : stuff){
			if(i.getClass().getSuperclass().getName().equals(Consommable.class.getName())
					|| i.getClass().getName().equals(Cle.class.getName())){
				if(this.stuffList.containsKey(i.getClass().getName())){
					this.stuffList.put(i.getClass().getName(), this.stuffList.get(i.getClass().getName()) + 1);
				}
				else{
					this.stuffList.put(i.getClass().getName(), 1);
				}
			}
		}
	}

	//d�pose les cl�s que le joueur poss�de dans la porte
	public void deposer(PorteMagique pm) {
		if(this.getCible().equals(pm.position)){
			for(Item i : stuff){
				if(i.getClass().getName().equals(Cle.class.getName())){
					pm.placer((Cle)i);
					stuff.remove(i);
					GameWorld.remove(i);
					break;
				}
			}
			this.updateInventory();
		}
	}
	
	public boolean gotStuff(Entite e){
		return stuff.contains(e);
	}
	
	public void usePotion(){
		for(Item i : stuff){
			if(i.getClass().getName().equals(Potion.class.getName())){
				if(this.pointsDeVie < this.maxLife){
					if(this.pointsDeVie + ((Potion) i).getBonus() + this.bonusPotion < this.maxLife){
						this.pointsDeVie += ((Potion) i).getBonus() + this.bonusPotion;
					}
					else{
						this.pointsDeVie = this.maxLife;
					}
					GameWorld.remove(i);
					stuff.remove(i);
					break;
				}
			}
		}
		this.updateInventory();
	}
	
	public int getExp(){
		return this.exp;
	}
	
	public void addExp(int e){
		this.exp += e;
	}
	
	public void selectTools(int i){
		this.cursor += i;
	}
	
	
	public void setEquipTool(boolean b){
		this.equipTool = b;
	}
}
