import java.awt.Font;

public class Main {

	public static void main(String[] args) {
		
		// la carte sera une grille de taille tailleGrille * tailleGrille
		int tailleGrille = 70;
		int canvasSize = 800;
		
		StdDraw.setCanvasSize(canvasSize, canvasSize);
		
		GameWorld world = new GameWorld(tailleGrille);
		
		// permet le double buffering, pour permettre l'animation
		StdDraw.enableDoubleBuffering();
		
		
		// la boucle principale de notre jeu
		while (GameWorld.isCharacterAlive() && !GameWorld.gameWon()) {
			// Efface la fen�tre graphique
			StdDraw.clear();
			
			
			// Capture et traite les �ventuelles interactions clavier du joueur
			if (StdDraw.hasNextKeyTyped()) {
				char key = StdDraw.nextKeyTyped();
				world.processUserInput(key);
			}
			if(!GameWorld.pause){
				world.step();
			}
			
			// dessine la carte
			Font font = new Font("Arial", Font.PLAIN, canvasSize/GameWorld.getTailleGrille());
			StdDraw.setFont(font);
			world.dessine();
			
			
			// Montre la fen�tre graphique mise � jour et attends 20 millisecondes
			StdDraw.show();
			StdDraw.pause(20);
			
			
		}
		
		if (GameWorld.gameWon()) System.out.println("Game won !");

	}

}
