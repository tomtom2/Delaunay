package m2if.model;

import java.util.ArrayList;

public class Delaunay {

	private ArrayList<Point> P;
	private ArrayList<Triangle> T;
	
	public Delaunay(){
		this.P = new ArrayList<Point>();
		this.T = new ArrayList<Triangle>();
	}
	
	public Delaunay(ArrayList<Point> liste){
		this.P = liste;
		this.T = new ArrayList<Triangle>();
	}
	
	/**
	 * Method to add a point if it is contained in a triangle of
	 * the triangulation T of the current Delaunay object.
	 * @param point
	 * @param triangle
	 */
	public void addPointInTriangle(Point point, Triangle triangle){
		if(triangle.contains(point)){
			Triangle t1 = new Triangle(triangle.getP1(), triangle.getP2(), point);
			Triangle t2 = new Triangle(triangle.getP2(), triangle.getP3(), point);
			Triangle t3 = new Triangle(triangle.getP3(), triangle.getP1(), point);
			T.remove(triangle);
			T.add(t1);
			T.add(t2);
			T.add(t3);
			legalize(t1);
			legalize(t2);
			legalize(t3);
		}
	}
	
	public void addPointOnSegment(){
		//-TODO
	}
	
	/**
	 * add a point outside the current triangulation.
	 * Link that point with the maximum of other points avoiding line crossing.
	 * @param point
	 */
	public void addPointOutside(Point point){
		//-TODO
		X
	}
	
	public ArrayList<Point> getListOfPointsVisibleBy(Point point){
		ArrayList<Point> visiblePoints = new ArrayList<Point>();
		for(Point pointOfGraph : P){
			boolean noCrossing = true;
			for(Triangle triangle : T){
				if(segmentCrossing(point, pointOfGraph, triangle.getP1(), triangle.getP2())){
					noCrossing = false;
				}
				if(segmentCrossing(point, pointOfGraph, triangle.getP2(), triangle.getP3())){
					noCrossing = false;
				}
				if(segmentCrossing(point, pointOfGraph, triangle.getP3(), triangle.getP1())){
					noCrossing = false;
				}
			}
			if(noCrossing){
				visiblePoints.add(pointOfGraph);
			}
		}
		visiblePoints.remove(point);
		return visiblePoints;
	}
	
	public boolean segmentCrossing(Point p11, Point p12, Point p21, Point p22){
		//if vertical parallel
		if(p12.getX()==p11.getX() && p22.getX()==p21.getX()){
			return false;
		}
		//if first segment is vertical
		else if(p12.getX()==p11.getX()){
			//coef of second segment
			double c = 1.0*(p22.getY()-p21.getY())/(p22.getX()-p21.getX());
			double d = 1.0*p21.getY()-c*p21.getX();
			
			double x = p12.getX();
			double y = c*x + d;
			
			Point[] segment1 = {p11, p12};
			Rectangle cible1 = new Rectangle(segment1);
			Point[] segment2 = {p21, p22};
			Rectangle cible2 = new Rectangle(segment2);
			
			return (cible1.containsCoord(x, y) && cible2.containsCoord(x, y));
		}
		//if second segment is vertical
		else if(p22.getX()==p21.getX()){
			//coef for first segment
			double a = 1.0*(p12.getY()-p11.getY())/(p12.getX()-p11.getX());
			double b = 1.0*p11.getY()-a*p11.getX();
			
			double x = p22.getX();
			double y = a*x + b;
			
			Point[] segment1 = {p11, p12};
			Rectangle cible1 = new Rectangle(segment1);
			Point[] segment2 = {p21, p22};
			Rectangle cible2 = new Rectangle(segment2);
			
			return (cible1.containsCoord(x, y) && cible2.containsCoord(x, y));
		}
		
		//solve the equation a.x+b = c.x+d
		double a = 1.0*(p12.getY()-p11.getY())/(p12.getX()-p11.getX());
		double b = 1.0*p11.getY()-a*p11.getX();
		double c = 1.0*(p22.getY()-p21.getY())/(p22.getX()-p21.getX());
		double d = 1.0*p21.getY()-c*p21.getX();
		
		if(a==c){
			return false;
		}
		//coord of crossing point x and y.
		//x an y don't have to be integers !
		
		double x = (d-b)/(a-c);
		double y = a*x + b;
		
		Point[] segment1 = {p11, p12};
		Rectangle cible1 = new Rectangle(segment1);
		Point[] segment2 = {p21, p22};
		Rectangle cible2 = new Rectangle(segment2);
		
		return (cible1.containsCoord(x, y) && cible2.containsCoord(x, y));
	}
	
	public void legalize(Triangle t){
		Triangle illegalNeighbour = getIllegalNeighbour(t, getNeigbours(t));
		if(illegalNeighbour!=null){
			Triangle[] listOldAndNew = flip(t, illegalNeighbour);
			T.remove(listOldAndNew[0]);
			T.remove(listOldAndNew[1]);
			T.add(listOldAndNew[2]);
			T.add(listOldAndNew[3]);
		}
	}
	
	public ArrayList<Triangle> getNeigbours(Triangle t){
		ArrayList<Triangle> neigbourList = new ArrayList<Triangle>();
		for(Triangle triangle : T){
			if(triangle.hasSegment(t.getP1(), t.getP2()) || triangle.hasSegment(t.getP2(), t.getP3()) || triangle.hasSegment(t.getP3(), t.getP1())){
				neigbourList.add(triangle);
			}
		}
		neigbourList.remove(t);
		return neigbourList;
	}
	
	public Triangle[] flip(Triangle t1, Triangle t2){
		Point [] segmentToFlip = new Point[2];
		Point[] set1 = {t1.getP1(), t1.getP2(), t1.getP3()};
		Point[] set2 = {t2.getP1(), t2.getP2(), t2.getP3()};
		int count = 0;
		for(Point p1 : set1){
			for(Point p2 : set2){
				if(p1.equals(p2)){
					segmentToFlip[count] = p1;
					count++;
				}
			}
		}
		
		Point t1_point = null;
		for(Point p : set1){
			if(!p.equals(segmentToFlip[0]) && !p.equals(segmentToFlip[1])){
				t1_point = p;
				break;
			}
		}
		Point t2_point = null;
		for(Point p : set2){
			if(!p.equals(segmentToFlip[0]) && !p.equals(segmentToFlip[1])){
				t2_point = p;
				break;
			}
		}
		
		Triangle t1bis = new Triangle(segmentToFlip[0], t1_point, t2_point);
		Triangle t2bis = new Triangle(segmentToFlip[1], t1_point, t2_point);
		Triangle[] list_old_new = {t1, t2, t1bis, t2bis};
		return list_old_new;
		
	}
	
	public Triangle getIllegalNeighbour(Triangle t, ArrayList<Triangle> neighbours){
		Triangle illegalTriangle = null;
		for(Triangle neighbour : neighbours){
			Point[] organizedPointSet = t.getCommonSegment(neighbour);
			double currentSegmentLenght = organizedPointSet[0].distance(organizedPointSet[1]);
			double flipedSegmentLenght = organizedPointSet[2].distance(organizedPointSet[3]);
			if(currentSegmentLenght>flipedSegmentLenght){
				illegalTriangle = neighbour;
				break;
			}
		}
		return illegalTriangle;
	}

	public ArrayList<Point> getP() {
		return P;
	}

	public void setP(ArrayList<Point> p) {
		P = p;
	}

	public ArrayList<Triangle> getT() {
		return T;
	}

	public void setT(ArrayList<Triangle> t) {
		T = t;
	}

	public boolean containsTriangle(Triangle triangle) {
		for(Triangle t : T){
			if(t.equals(triangle)){
				return true;
			}
		}
		return false;
	}
	
	
	
}
