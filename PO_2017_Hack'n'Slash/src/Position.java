
public class Position {
	private int positionX;
	private int positionY;
	
	public Position(int x, int y){
		positionX=x;
		positionY=y;
	}
	
	public int getX(){
		return positionX;
	}
	
	public int getY(){
		return positionY;
	}
	
	public boolean equals(Position p){
		return (this.positionX==p.positionX && this.positionY==p.positionY);
	}

	public void setY(int y) {
		// TODO Auto-generated method stub
		this.positionY = y;
	}

	public void setX(int x) {
		// TODO Auto-generated method stub
		this.positionX = x;
	}
	
	public String toString(){
		return null;
	}
}
