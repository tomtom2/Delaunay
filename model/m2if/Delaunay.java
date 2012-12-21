package m2if;

import java.util.ArrayList;

public class Delaunay implements Observable{

	private ArrayList<Point> P;
	private ArrayList<Triangle> T;
	
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	public Delaunay(){
		this.P = new ArrayList<Point>();
		this.T = new ArrayList<Triangle>();
	}
	
	public Delaunay(ArrayList<Point> liste){
		this.P = liste;
		this.T = new ArrayList<Triangle>();
	}
	
	
	public void addPoint(Point p){
		P.add(p);
		if(P.size()<3){
			return;
		}
		
		else if(P.size()==3){
			T.add(new Triangle(P.get(0), P.get(1), P.get(2)));
		}
		else{
			for(Triangle triangle : T){
				if(triangle.contains(p)){
					System.out.println("Add point in triangle!");
					addPointInTriangle(p, triangle);
					update();
					return;
				}
			}
			System.out.println("Add point outside!");
			addPointOutside(p);
		}
		update();
	}
	
	/**
	 * Method to add a point if it is contained in a triangle of
	 * the triangulation T of the current Delaunay object.
	 * @param point
	 * @param triangle
	 */
	public void addPointInTriangle(Point point, Triangle triangle){
		Triangle t1 = new Triangle(triangle.getP1(), triangle.getP2(), point);
		Triangle t2 = new Triangle(triangle.getP2(), triangle.getP3(), point);
		Triangle t3 = new Triangle(triangle.getP3(), triangle.getP1(), point);
		
		removeTriangle(triangle);
		addTriangle(t1);//T.add(t1);
		addTriangle(t2);//T.add(t2);
		addTriangle(t3);//T.add(t3);
		
		legalize(t1);
		legalize(t2);
		legalize(t3);
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
		ArrayList<Point[]> visibleSegmentList = this.getNeighboursOnVisibleLineFor(point);
		System.out.println("Segments to be linked:");
		for(Point[] seg : visibleSegmentList)
			System.out.println("\t"+seg[0]+"-"+seg[1]);
		for(Point[] segment : visibleSegmentList){
			Triangle futurTriangle = new Triangle(point, segment[0], segment[1]);
			System.out.println("new outside triangle -> "+futurTriangle);
			if(!containsTriangle(futurTriangle)){
				addTriangle(futurTriangle);//T.add(futurTriangle);
				legalize(futurTriangle);
			}
		}
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
			
			Point[] segment = {p21, p22};
			Rectangle cible = new Rectangle(segment);
			boolean secondCondition = (p12.getY()<y && p11.getY()>y) || (p11.getY()<y && p12.getY()>y);
			return (cible.containsCoord(x, y) && cible.containsCoord(x, y));
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
		Triangle illegalNeighbour = getIllegalNeighbour(t, getNeighbours(t));
		if(illegalNeighbour!=null){
			Triangle[] listOldAndNew = flip(t, illegalNeighbour);
			removeTriangle(listOldAndNew[0]);
			removeTriangle(listOldAndNew[1]);
			addTriangle(listOldAndNew[2]);//T.add(listOldAndNew[2]);
			addTriangle(listOldAndNew[3]);//T.add(listOldAndNew[3]);
			legalize(listOldAndNew[2]);
			legalize(listOldAndNew[3]);
		}
	}
	
	public ArrayList<Triangle> getNeighbours(Triangle t){
		ArrayList<Triangle> neigbourList = new ArrayList<Triangle>();
		for(Triangle triangle : T){
			if(triangle.hasSegment(t.getP1(), t.getP2()) || triangle.hasSegment(t.getP2(), t.getP3()) || triangle.hasSegment(t.getP3(), t.getP1())){
				neigbourList.add(triangle);
			}
		}
		neigbourList.remove(t);
		return neigbourList;
	}
	
	public ArrayList<Point> getNeighbours(Point p){
		ArrayList<Point> neigbourList = new ArrayList<Point>();
		for(Triangle triangle : T){
			Point[] pointList = triangle.getOtherPoints(p);
			if(pointList!=null){
				neigbourList.add(pointList[0]);
				neigbourList.add(pointList[1]);
			}
		}
		neigbourList.remove(p);
		return neigbourList;
	}
	
	public ArrayList<Point[]> getNeighboursOnVisibleLineFor(Point p){
		ArrayList<Point> visiblePoints = this.getListOfPointsVisibleBy(p);
		System.out.println("\tVisible points:");
		for(Point point : visiblePoints)
			System.out.println("\t\t"+point);
		ArrayList<Point[]> neigbourList = new ArrayList<Point[]>();
		for(Triangle triangle : T){
			if(visiblePoints.contains(triangle.getP1()) && visiblePoints.contains(triangle.getP2())){
				Triangle futureTriangle = new Triangle(triangle.getP1(), triangle.getP2(), p);
				boolean triangleIsValide = true;
				for(Point pointTest : P){
					if(futureTriangle.contains(pointTest) && !pointTest.equals(futureTriangle.getP1()) && !pointTest.equals(futureTriangle.getP2()) && !pointTest.equals(futureTriangle.getP3())){
						triangleIsValide = false;
						break;
					}
				}
				if(triangleIsValide){
					Point[] VisibleSegment = {triangle.getP1(), triangle.getP2()};
					neigbourList.add(VisibleSegment);
				}
			}
			if(visiblePoints.contains(triangle.getP2()) && visiblePoints.contains(triangle.getP3())){
				Triangle futureTriangle = new Triangle(triangle.getP2(), triangle.getP3(), p);
				boolean triangleIsValide = true;
				for(Point pointTest : P){
					if(futureTriangle.contains(pointTest) && !pointTest.equals(futureTriangle.getP1()) && !pointTest.equals(futureTriangle.getP2()) && !pointTest.equals(futureTriangle.getP3())){
						triangleIsValide = false;
						break;
					}
				}
				if(triangleIsValide){
					Point[] VisibleSegment = {triangle.getP2(), triangle.getP3()};
					neigbourList.add(VisibleSegment);
				}
			}
			if(visiblePoints.contains(triangle.getP3()) && visiblePoints.contains(triangle.getP1())){
				Triangle futureTriangle = new Triangle(triangle.getP3(), triangle.getP1(), p);
				boolean triangleIsValide = true;
				for(Point pointTest : P){
					if(futureTriangle.contains(pointTest) && !pointTest.equals(futureTriangle.getP1()) && !pointTest.equals(futureTriangle.getP2()) && !pointTest.equals(futureTriangle.getP3())){
						triangleIsValide = false;
						break;
					}
				}
				if(triangleIsValide){
					Point[] VisibleSegment = {triangle.getP3(), triangle.getP1()};
					neigbourList.add(VisibleSegment);
				}
			}
		}
		return neigbourList;
	}
	
	public Triangle[] flip(Triangle t1, Triangle t2){
		if(t1.equals(t2)){
			return null;
		}
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
		for(Triangle neighbour : neighbours){
			Triangle[] triangleSet = this.flip(t, neighbour);
			if(triangleSet!=null){
				Point[] organizedPointSet = t.getCommonSegment(neighbour);
				double currentAngleMin = Math.min(triangleSet[0].getAngleMin(), triangleSet[1].getAngleMin());
				double flipedAngleMin = Math.min(triangleSet[2].getAngleMin(), triangleSet[3].getAngleMin());
				if(currentAngleMin<flipedAngleMin && segmentCrossing(organizedPointSet[0], organizedPointSet[1], organizedPointSet[2], organizedPointSet[3])){
					return neighbour;
				}
			}
			
		}
		return null;
	}
	
	public void removeTriangle(Triangle triangle){
		int i=0;
		int size = T.size();
		for(int count=0; count<size; count++){
			if(T.get(i).equals(triangle)){
				T.remove(i);
			}
			else{
				i = i + 1;
			}
		}
	}
	
	public void addTriangle(Triangle triangle){
		if(!containsTriangle(triangle)){
			T.add(triangle);
		}
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

	@Override
	public void addObserver(Observer obs) {
		observers.add(obs);
	}

	@Override
	public void clear() {
		observers = new ArrayList<Observer>();
	}

	@Override
	public void update() {
		for(Observer obs : observers)
			obs.update(T);
	}
	
	
	
}
