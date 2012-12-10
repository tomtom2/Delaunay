package m2if.model;

public class Point {

	private int x;
	private int y;
	
	public Point(int a, int b){
		x = a;
		y = b;
	}

	public boolean equals(Point p){
		return p.x==this.x && p.y==this.y;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	public int getX() {
		return x;
	}

	public void setX(int abscise) {
		this.x = abscise;
	}

	public int getY() {
		return y;
	}

	public void setY(int ordonnee) {
		this.y = ordonnee;
	}
	
	
}
