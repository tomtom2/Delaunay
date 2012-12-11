package m2if.model;

import java.util.ArrayList;

public class Delaunay {

	private ArrayList<Point> P;
	private ArrayList<Triangle> triangulation;
	
	public Delaunay(ArrayList<Point> liste){
		this.P = liste;
		this.triangulation = new ArrayList<Triangle>();
	}
	
	public void legalize(Triangle t){
		
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
}
