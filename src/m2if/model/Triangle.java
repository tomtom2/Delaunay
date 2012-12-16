package m2if.model;

import java.util.ArrayList;

public class Triangle {

	private Point p1;
	private Point p2;
	private Point p3;
	
	public Triangle(Point p1, Point p2, Point p3){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	
	public boolean contains(Point p){
		double areaTotal = this.getArea();
		Triangle t = new Triangle(p1,p2, p);
		double area1 = t.getArea();
		t = new Triangle(p3,p2, p);
		double area2 = t.getArea();
		t = new Triangle(p3,p1, p);
		double area3 = t.getArea();
		return (float)(areaTotal + 0.000001) >= (float)(area1 + area2 + area3);
	}
	
	public boolean hasSegment(Point p1, Point p2){
		ArrayList<Point> set1 = new ArrayList<Point>();
		set1.add(this.p1);
		set1.add(this.p2);
		set1.add(this.p3);
		ArrayList<Point> set2 = new ArrayList<Point>();
		set2.add(p1);
		set2.add(p2);
		for(int i = 0; i<2; i++){
			for(Point p : set1){
				if(p.equals(set2.get(i))){
					set1.remove(p);
					break;
				}
			}
		}
		return set1.size()==1;
	}
	
	public Point[] getOtherPoints(Point p){
		if(p.equals(p1)){
			Point[] neighbourList = {p2, p3};
			return neighbourList;
		}
		else if(p.equals(p2)){
			Point[] neighbourList = {p3, p1};
			return neighbourList;
		}
		else if(p.equals(p3)){
			Point[] neighbourList = {p1, p2};
			return neighbourList;
		}
		return null;
	}
	
	public Point[] getCommonSegment(Triangle t){
		ArrayList<Point> set_this = new ArrayList<Point>();
		set_this.add(this.p1);
		set_this.add(this.p2);
		set_this.add(this.p3);
		
		ArrayList<Point> set_t = new ArrayList<Point>();
		set_t.add(t.getP1());
		set_t.add(t.getP2());
		set_t.add(t.getP3());
		
		Point[] return_set = new Point[4];
		
		int count=0;
		for(Point p_this : set_this){
			boolean match = false;
			for(Point p_t : set_t){
				if(p_this.equals(p_t)){
					return_set[count] = p_t;
					count++;
					match = true;
				}
			}
			if(!match){
				return_set[2] = p_this;
			}
		}
		set_t.remove(return_set[0]);
		set_t.remove(return_set[1]);
		return_set[3] = set_t.get(0);
		if(set_t.size()!=1){
			return null;
		}
		return return_set;
	}
	
	public boolean equals(Triangle t) {
		ArrayList<Point> set1 = new ArrayList<Point>();
		set1.add(this.p1);
		set1.add(this.p2);
		set1.add(this.p3);
		ArrayList<Point> set2 = new ArrayList<Point>();
		set2.add(t.getP1());
		set2.add(t.getP2());
		set2.add(t.getP3());
		for(int i = 0; i<3; i++){
			for(Point p : set2){
				if(p.equals(set1.get(0))){
					set2.remove(p);
					set1.remove(0);
					break;
				}
			}
		}
		return set1.size()==0;
	}


	public double getArea(){
		double hauteur = this.p1.distance(p2, p3);
		double base = p2.distance(p3);
		return base*hauteur/2.0;
	}

	/*
	 * GETTERS AND SETTERS
	 */
	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public Point getP3() {
		return p3;
	}

	public void setP3(Point p3) {
		this.p3 = p3;
	}


	public String toString() {
		return "["+p1+"; "+p2+", "+p3+"]";
	}
	
}
