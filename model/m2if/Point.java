package m2if;

public class Point {

	private int x;
	private int y;
	
	public Point(int a, int b){
		x = a;
		y = b;
	}

	public double distance(Point p){
		return Math.sqrt((p.x-this.x)*(p.x-this.x) + (p.y-this.y)*(p.y-this.y));
	}
	
	public boolean isOnSegment(Point p1, Point p2){
		return (float)(this.distance(p1)+this.distance(p2)) == (float)p1.distance(p2);
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return distance entre le point p et le segment [p1; p2]
	 */
	public double distance(Point p1, Point p2){
		double normalLength = Math.sqrt((p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y));
	    return (Math.abs((this.x-p1.x)*(p2.y-p1.y)-(this.y-p1.y)*(p2.x-p1.x))/normalLength);
	}
	
	
	public String toString() {
		return "("+x+", "+y+")";
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
