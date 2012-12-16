package m2if.model;

public class Rectangle {

	private double Xmin;
	private double Xmax;
	private double Ymin;
	private double Ymax;
	
	/**
	 * construit le plus petit rectangle contenant le segment donné en argument.
	 * @param points
	 */
	public Rectangle(Point[] points){
		Xmin = points[0].getX();
		Xmax = points[0].getX();
		Ymin = points[0].getY();
		Ymax = points[0].getY();
		for(Point p : points){
			if(p.getX()>=Xmax){
				Xmax = p.getX();
			}
			if(p.getX()<=Xmin){
				Xmin = p.getX();
			}
			if(p.getY()>=Ymax){
				Ymax = p.getY();
			}
			if(p.getY()<=Ymin){
				Ymin = p.getY();
			}
		}
	}
	
	public boolean containsCoord(double x, double y){
		return x<Xmax && x>Xmin && y<Ymax && y>Ymin;
	}
}
