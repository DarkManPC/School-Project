/* Le but du jeu est de trouver les 4 clees afin douvir la porte magique, on gagne de l'xp en tuant different monstre qui drop des items pour vous aider dans votre quete
 * , des fois il faudra ddes armes en plus pour mettre fin au monstre qui sont trop forts au debut.
 */
import java.util.*;

public class GameWorld {

	//la taille du monde
	private static int tailleGrilleX;
	private static int tailleGrilleY;

	// on cree notre personnage
	private static Joueur personnage;

	// les monstres sur la carte
	private static ArrayList<Monstre> monstres;

	// l'ensemble des entites, pour gerer (notamment) l'affichage
	private static HashMap<String, Entite> entites;

	// pour savoir si le heros est toujours en vie, pour savoir si on continue �
	// jouer ou pas
	private static boolean alive;
	
	//Pour savoir si la partie est gagnee ou pas
	private static boolean gameWon;
	
	public static boolean pause;

	// constructeur, il faut initialiser notre monde virtuel
	/**
	 * La classe GameWorld permet de gerer l'ensemble de la map en creant les monstres ainsi que les objectifs et affiche l'HUD
	 * @param tailleGrille
	 */
	public GameWorld(int tailleGrille) {
		// la taille de la grille, et la grille
		GameWorld.tailleGrilleX = tailleGrille;
		// On differencie X et Y pour limiter la taille de la map pour placer le HUD en haut
		GameWorld.tailleGrilleY = tailleGrille - 3;

		gameWon=false;
		
		// on cree collections
		entites = new HashMap<String, Entite>();
		monstres = new ArrayList<Monstre>();

		// on cree notre personnage
		
		personnage = new Joueur("Perso", 1, 0, 200, 25, 5);
		entites.put(personnage.getNom(), personnage);
		alive = true;
		pause = false;

		// it's a trap !!
		for (int i = 0; i < 8+(int)(Math.random()*5); i++){
			Piege piege = new Piege("Trap"+i, (int)(Math.random()*tailleGrilleX), (int)(Math.random()*tailleGrilleY)-1, 10, 100, 15);
			entites.put(piege.getNom(), piege);
			monstres.add(piege);
			
		}
		/**
		 * La boucle suivante permet de creer des Hunter (minimum 4) dont 4 qui sont porteurs de clees
		 */
		for (int i = 0; i < 4+(int)(Math.random()*3); i++){
			
			Hunter monstre;
			
			if(i < 4){
				monstre = new Hunter("Hunter"+i,(int)(Math.random()*tailleGrilleX), (int)(Math.random()*tailleGrilleY)-1, 50, 15, 20, true );
			}
			else{
				monstre = new Hunter("Hunter"+i,(int)(Math.random()*tailleGrilleX), (int)(Math.random()*tailleGrilleY)-1, 30, 15, 15, false );
			}
			entites.put(monstre.getNom(), monstre);
			monstres.add(monstre);
		}
		
		/**
		 * On cree et on place notre porte magique
		 * */
		 
		PorteMagique pm = new PorteMagique("Porte Noire", (int)(Math.random()*tailleGrilleX), (int)(Math.random()*tailleGrilleY)-1);
		entites.put(pm.getNom(), pm);
		
		for (int i = 0; i < 2; i++){
			Potion p = new Potion("Potion"+i, 0, 0);
			entites.put(p.nom, p);
			personnage.ramasser(p);
		}
		
		Bombe b = new Bombe("Bombe", 15, 7);
		entites.put(b.nom, b);
		
		Necro n = new Necro("Necro", (int)(Math.random()*tailleGrilleX), (int)(Math.random()*tailleGrilleY),70, 20, 15, false);
		entites.put(n.getNom(), n);
		monstres.add(n);
		
       for (int i = 0; i < 1+(int)(Math.random()*3); i++){
			
			Zombie z = new Zombie("Zombie"+i,(int)(Math.random()*tailleGrilleX), (int)(Math.random()*tailleGrilleY)-1, 150, 50, 50);
			entites.put(z.getNom(), z);
			monstres.add(z);
       }
	
       
	for (int i = 0; i < 1+(int)(Math.random()*3); i++){
		
		Kamikaze k = new Kamikaze("Kamikaze"+i,(int)(Math.random()*tailleGrilleX), (int)(Math.random()*tailleGrilleY)-1, 60, 70, 30);
		entites.put(k.getNom(), k);
		monstres.add(k);
   }}



	/**
	 * En fonction d'une touche pressee par l'utilisateur, fait les mises a jour
	 * necessaires dans le monde du jeu.
	 * 
	 * @param key
	 *            Touche pressee par l'utilisateur
	 */
	public void processUserInput(char key) {
		switch (key) {
		case 'z':
			if((!(personnage.getCible().getX() == -1) && !(personnage.getCible().getX() == GameWorld.getTailleGrilleX())) 
					&& (!(personnage.getCible().getY() == -1) && !(personnage.getCible().getY() == GameWorld.getTailleGrilleY()))){
				personnage.seDeplacer(1);
			}
			
			break;
		case 's':
			Position backTarget = new Position(personnage.getX()+((personnage.getCible().getX()-personnage.getX())*-1),personnage.getY()+((personnage.getCible().getY()-personnage.getY())*-1));
			
			if((!(backTarget.getX() == -1) && !(backTarget.getX() == GameWorld.getTailleGrilleX())) 
					&& (!(backTarget.getY() == -1) && !(backTarget.getY() == GameWorld.getTailleGrilleY()))){
				personnage.seDeplacer(-1);
			}
			
			break;
		case 'q':
			personnage.tournerAGauche();
			break;
		case 'd':
			personnage.tournerADroite();
			break;
		case 'i':
			personnage.afficheInventaire();
			break;
		case 'e':
			personnage.afficheEquipement();
			break;
		case 'k':
			Monstre monster = null;
			
			for (Monstre m: monstres){
				if (m.position.equals(personnage.getCible())){
					monster = m;
				}
			}
			
			if(monster != null){
				personnage.attaquer(monster);
			}
			
			break;
		case 'r':
			for (Entite e: entites.values()){
				if((e.getClass().getSuperclass().getName().equals(Item.class.getName())
						|| e.getClass().getSuperclass().getSuperclass().getName().equals(Item.class.getName())) 
						&& personnage.getCible().equals(e.position)){
					personnage.ramasser((Item)e);
				}
			}
			break;
		case 'l':
			ArrayList<Monstre> mList = new ArrayList<Monstre>();
			
			for(int i = 1; i < 5; i++){
				
				Position longTarget = new Position(personnage.getX()+((personnage.getCible().getX()-personnage.getX())*i),personnage.getY()+((personnage.getCible().getY()-personnage.getY())*i));
				for (Monstre m: monstres){
					if (m.position.equals(longTarget)){
						mList.add(m);
					}
				}
			}
			
			
			personnage.attaqueIndirect(mList);
			
			break;
		case 'm':
			personnage.deposer((PorteMagique) this.entites.get("Porte Noire"));
			break;
		case 'p':
			personnage.usePotion();
			break;
		case 'c':
			personnage.selectTools(-1);
			break;
		case 'v':
			personnage.selectTools(1);
			break;
		case '\n':
			personnage.setEquipTool(true);
			break;
		default:
			System.out.println("Touche non prise en charge");
			break;
		}

	}


	public void step() {
		
		ArrayList<Monstre> mTab = new ArrayList<Monstre>();
		
		long timeToMove = System.nanoTime();
		
		for (Monstre monstre : monstres){
				mTab.add(monstre);	
		}
		
		for(Monstre m : mTab){
			if(m.getClass().getName().equals(Necro.class.getName())){
				((Necro) m).enfant();
			}
			if(m.getClass().getName().equals(Kamikaze.class.getName())){
				((Kamikaze) m).piege();
			}
			if(m.deplacer(personnage, timeToMove) || m.getClass().getName().equals(Piege.class.getName())){
			m.attaquer(personnage);
			}
		}
		
		this.updateLevel();
		
	}

	
	public void dessine() {

		this.drawHUD();
		
		for (Entite entite : entites.values())
			
			if(!personnage.gotStuff(entite)){
				entite.dessine();
			}
	}
	
	private void drawHUD(){
		StdDraw.text((3 + 0.5) / GameWorld.getTailleGrille(),
				(GameWorld.getTailleGrille()-2 + 0.5) / GameWorld.getTailleGrille(), "Atk : " + personnage.pointsDAttaque);
		StdDraw.text((9 + 0.5) / GameWorld.getTailleGrille(),
				(GameWorld.getTailleGrille()-2 + 0.5) / GameWorld.getTailleGrille(), "Def : " + personnage.pointsDeDefense);
		StdDraw.text((15 + 0.5) / GameWorld.getTailleGrille(),
				(GameWorld.getTailleGrille()-2 + 0.5) / GameWorld.getTailleGrille(), "Exp : " + personnage.getExp());
		StdDraw.text((GameWorld.getTailleGrille() - 16 + 0.5) / GameWorld.getTailleGrille(),
				(GameWorld.getTailleGrille()-2 + 0.5) / GameWorld.getTailleGrille(), personnage.level);
		double[] xLife = {(double)20 / GameWorld.getTailleGrille(), (20 +((double)personnage.pointsDeVie/(double)personnage.maxLife)*20) / GameWorld.getTailleGrille(),
				(20 + ((double)personnage.pointsDeVie/(double)personnage.maxLife)*20) / GameWorld.getTailleGrille(), (double)20 / GameWorld.getTailleGrille()};
		double[] yLife = {((double)(GameWorld.getTailleGrille()-2) / GameWorld.getTailleGrille()), ((double)(GameWorld.getTailleGrille()-2) / GameWorld.getTailleGrille()),
				((double)(GameWorld.getTailleGrille()-1) / GameWorld.getTailleGrille()), ((double)(GameWorld.getTailleGrille()-1) / GameWorld.getTailleGrille())};
		if(((double)personnage.pointsDeVie/(double)personnage.maxLife) < 0.25){
			StdDraw.setPenColor(StdDraw.RED);
		}
		else if(((double)personnage.pointsDeVie/(double)personnage.maxLife) > 0.75){
			StdDraw.setPenColor(StdDraw.GREEN);
		}
		else{
			StdDraw.setPenColor(StdDraw.ORANGE);
		}
		StdDraw.filledPolygon(xLife, yLife);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.polygon(xLife, yLife);
		StdDraw.text((30 + 0.5) / GameWorld.getTailleGrille(),
				(GameWorld.getTailleGrille()-2 + 0.5) / GameWorld.getTailleGrille(), String.valueOf(personnage.pointsDeVie));
		
	}

	
	public static boolean isCharacterAlive() {
		
		if (personnage.pointsDeVie <= 0){
			alive = false;
		}
		return alive;
	}

	public static int getTailleGrilleX() {
		return tailleGrilleX;
	}
	
	public static int getTailleGrilleY() {
		return tailleGrilleY;
	}
	
	public static int getTailleGrille() {
		return tailleGrilleX;
	}

	//Pose un Item sur la carte
	public static void dropItem(Item i) {
		
	}

	/**
	 * Indique si la partie est gagnee ou non
	 * @return 
	 * Vrai si la partie est gagnee, faux sinon
	 */
	public static boolean gameWon(){
		if(((PorteMagique) entites.get("Porte Noire")).isOpen()){
			gameWon = true;
		}
		return gameWon;
	}
	
	public static void addItem(Item i){
		if (entites == null){
			return;
		}
		else{
			entites.put(i.nom, i);
		}
	}
	
	public static void addMonstre(Monstre m){
		if (entites == null || monstres == null){
			return;
		}
		else{
			entites.put(m.nom, m);
			monstres.add(m);
		}
	}
	
	public static void remove(Entite e){
		if (entites == null){
		}
		else{
			entites.remove(e.nom);
		}

		if (e.getClass().getSuperclass().getName().equals(Monstre.class.getName())
				|| e.getClass().getSuperclass().getSuperclass().getName().equals(Monstre.class.getName())){
			monstres.remove(e);
		}
	}
	/**
	 * UpdateLevel1 permet de choisir une classe de guerrier
	 */
	public void updateLevel(){
		if(personnage.exp >= 0 && personnage.exp < 50){
			
		}
		else if(personnage.exp >= 50 && personnage.exp < 100){
			if(!(personnage.level.equals("Soldat"))){
				this.soldat();
			}
		}
		else if(personnage.exp >= 100 && personnage.exp < 200 && personnage.choix == 1){
			if(!(personnage.level.equals("Guerrier"))){
				this.guerrier();
			}
		}
		else if(personnage.exp >= 100 && personnage.exp < 200 && personnage.choix == 2){
			if(!(personnage.level.equals("Defenseur"))){
				this.defenseur();
			}
		}
		else if(personnage.exp >= 200 && personnage.exp < 300 && personnage.choix == 3){
			if(!(personnage.level.equals("Guerisseur"))){
				this.guerisseur();
			}
		}
		else if(personnage.exp >= 200 && personnage.exp < 300 && personnage.choix == 4){
			if(!(personnage.level.equals("Forgeron"))){
				this.forgeron();
			}
		}
		else if(personnage.exp >= 200 && personnage.exp < 300 && personnage.choix == 5){
			if(!(personnage.level.equals("Vagabon"))){
				this.vagabon();
			}
		}
		else if(personnage.exp >= 200 && personnage.exp < 300 && personnage.choix == 6){
			if(!(personnage.level.equals("Escrimeur"))){
				this.escrimeur();
			}
		}
		else{
			personnage.level = "Choisir niveau dans la console";
			this.choix();
		}
	}
	/**
	 * La fonction qui va dans la console et qui permet de choisir sa nouvelle classe 
	 */
	private void choix(){
		Scanner sc = new Scanner(System.in);
		String s = "";
		if(personnage.choix == 0){
			System.out.println("1 -> Guerrier : +20 atk");
			System.out.println("2 -> Defenseur : +20 def");
			do{
				System.out.println("Votre choix ?");
				s = sc.nextLine();
				personnage.choix = Integer.valueOf(s);
			}while(Integer.valueOf(s).equals(1) || Integer.valueOf(s).equals(2));
		}
		if(personnage.choix == 2){
			System.out.println("3 -> Guerisseur : +30 pDV Potion");
			System.out.println("4 -> Forgeron : +30 def Armure");
			do{
				System.out.println("Votre choix ?");
				s = sc.nextLine();
				personnage.choix = Integer.valueOf(s);
			}while(Integer.valueOf(s).equals(3) || Integer.valueOf(s).equals(4));
		}
		if(personnage.choix == 1){
			System.out.println("5 -> Vagabon : +30 atk Bombe");
			System.out.println("6 -> Escrimeur : +30 atk Arme");
			do{
				System.out.println("Votre choix ?");
				s = sc.nextLine();
				personnage.choix = Integer.valueOf(s);
			}while(Integer.valueOf(s).equals(5) || Integer.valueOf(s).equals(6));
		}
	}
	private void soldat(){
		personnage.maxLife += 50;
		personnage.pointsDeVie = personnage.maxLife;
		personnage.level = "Soldat";
		this.updateMonstre(5);
		this.addNecro();
	}
	
	private void guerrier(){
		personnage.pointsDAttaque += 20;
		personnage.level = "Guerrier";
		this.updateMonstre(20);
		this.addNecro();
	}
	
	private void defenseur(){
		personnage.pointsDeDefense += 20;
		personnage.level = "Defenseur";
		this.updateMonstre(20);
		this.addNecro();
	}
	
	private void guerisseur(){
		personnage.bonusPotion = 30;
		personnage.level = "Guerisseur";
		this.updateMonstre(50);
		this.addNecro();
	}
	
	private void forgeron(){
		personnage.bonusArmure = 30;
		personnage.level = "Forgeron";
		this.updateMonstre(50);
		this.addNecro();
	}
	
	private void vagabon(){
		personnage.bonusBombe = 30;
		personnage.level = "Vagabon";
		this.updateMonstre(50);
		this.addNecro();
	}
	
	private void escrimeur(){
		personnage.bonusArme = 30;
		personnage.level = "Escrimeur";
		this.updateMonstre(50);
		this.addNecro();
	}
	
	private void updateMonstre(int i){
		for(Monstre m : GameWorld.monstres){
			m.pointsDeVie += i;
			m.pointsDAttaque += i;
			m.pointsDeDefense += i/2;
		}
	}
	
	private void addNecro(){
		Necro n = new Necro("Necro"+(Math.random()*Math.random()), (int)(Math.random()*tailleGrilleX), (int)(Math.random()*tailleGrilleY),100, 20, 15, false);
		entites.put(n.getNom(), n);
		monstres.add(n);
	}
}
