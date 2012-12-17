package m2if;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import m2if.Delaunay;
import m2if.Point;
import m2if.Triangle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestDelaunay {

	private Point p1;
	private Point p2;
	private Point p3;
	private Point p4;
	private ArrayList<Point> P = new ArrayList<Point>();
	private Delaunay d;
	@Before
	public void setUp() throws Exception {
		p1 = new Point(1, 2);
		p2 = new Point(2, 3);
		p3 = new Point(4, 1);
		p4 = new Point(2, 2);
		P.add(p1);
		P.add(p2);
		P.add(p3);
		P.add(p4);
		d = new Delaunay(P);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFlip() {
		Triangle t1 = new Triangle(p1, p2, p3);
		Triangle t2 = new Triangle(p2, p3, p4);
		Triangle[] flipList = d.flip(t1, t2);
		Triangle t3 = new Triangle(p1, p2, p4);
		Triangle t4 = new Triangle(p1, p4, p3);
		
		assertFalse(t3.equals(flipList[0]));
		assertFalse(t4.equals(flipList[1]));
		assertTrue(t3.equals(flipList[2]));
		assertTrue(t4.equals(flipList[3]));
	}
	
	@Test
	public void testGetNeigboursOfPoint() {
		Triangle t1 = new Triangle(p1, p2, p3);
		Triangle t2 = new Triangle(p2, p3, p4);
		Point p5 = new Point(5, 5);
		Triangle t3 = new Triangle(p1, p5, p4);
		ArrayList<Triangle> triangulation = new ArrayList<Triangle>();
		triangulation.add(t1);
		triangulation.add(t2);
		triangulation.add(t3);
		d.setT(triangulation);
		
		assertTrue(d.getNeighbours(p1).size()==4);
		assertTrue(d.getNeighbours(p1).contains(p2));
		assertTrue(d.getNeighbours(p1).contains(p3));
		assertTrue(d.getNeighbours(p1).contains(p4));
		assertTrue(d.getNeighbours(p1).contains(p5));
	}
	
	@Test
	public void testGetNeigbours() {
		Triangle t1 = new Triangle(p1, p2, p3);
		Triangle t2 = new Triangle(p2, p3, p4);
		Point p5 = new Point(5, 5);
		Triangle t3 = new Triangle(p1, p5, p4);
		ArrayList<Triangle> triangulation = new ArrayList<Triangle>();
		triangulation.add(t1);
		triangulation.add(t2);
		triangulation.add(t3);
		d.setT(triangulation);
		
		assertTrue(d.getNeighbours(t1).size()==1);
		assertTrue(d.getNeighbours(t1).get(0).equals(t2));
	}
	
	@Test
	public void testGetIllegalNeighbour() {
		Point p11 = new Point(1, 1);
		Point p21 = new Point(2, 1);
		Point p34 = new Point(3, 4);
		Point p06 = new Point(0, 6);
		Point p12 = new Point(1, 2);
		//t1 et t2 forment une triangulation illegale
		Triangle t1 = new Triangle(p12, p11, p34);
		Triangle t2Legal = new Triangle(p06, p11, p34);
		Triangle t2 = new Triangle(p21, p11, p34);
		
		ArrayList<Triangle> triangulation = new ArrayList<Triangle>();
		triangulation.add(t1);
		triangulation.add(t2Legal);
		triangulation.add(t2);
		d.setT(triangulation);
		
		assertTrue(d.getNeighbours(t1).size()==2);
		assertTrue(d.getNeighbours(t1).get(0).equals(t2Legal));
		assertTrue(d.getNeighbours(t1).get(1).equals(t2));
		assertTrue(d.getIllegalNeighbour(t1, d.getNeighbours(t1)).equals(t2));
	}
	
	@Test
	public void testLegalizeIllegal() {
		Point p11 = new Point(1, 1);
		Point p34 = new Point(3, 4);
		Point p03 = new Point(0, 3);
		Point p12 = new Point(1, 2);
		//[p11, P34] est un segment long commun a t1 et t2
		//il doit subire un flip car les points p03 et p12
		//appartiennent à des cercles circonscris
		
		Triangle t1 = new Triangle(p12, p11, p34);
		Triangle t2 = new Triangle(p03, p11, p34);
		
		ArrayList<Triangle> triangulation = new ArrayList<Triangle>();
		triangulation.add(t1);
		triangulation.add(t2);
		
		d.setT(triangulation);
		d.legalize(t1);
		assertTrue(d.getT().size()==2);
		assertTrue(d.getT().get(0).equals(d.flip(t1, t2)[2]));
		assertFalse(d.getT().contains(t1));
	}
	
	@Test
	public void testLegalizeLegal() {
		Point p11 = new Point(1, 1);
		Point p34 = new Point(3, 4);
		Point p03 = new Point(0, 3);
		Point p12 = new Point(1, 2);
		//[p03, P12] est un segment correct commun a t1 et t2
		//il ne doit pas subire un flip
		
		Triangle t1 = new Triangle(p11, p03, p12);
		Triangle t2 = new Triangle(p34, p03, p12);
		
		ArrayList<Triangle> triangulation = new ArrayList<Triangle>();
		triangulation.add(t1);
		triangulation.add(t2);
		
		d.setT(triangulation);
		d.legalize(t1);
		assertTrue(d.getT().size()==2);
		assertTrue(d.getT().get(0).equals(t1));
		assertFalse(d.getT().contains(d.flip(t1, t2)[2]));
	}
	@Test
	public void testContainsTriangle(){
		Point p00 = new Point(0, 0);
		Point p04 = new Point(0, 4);
		Point p24 = new Point(2, 4);
		
		Triangle triangle1 = new Triangle(p00, p04, p24);
		Triangle triangle2 = new Triangle(p00, p04, p24);
		
		d.getT().add(triangle1);
		assertFalse(triangle1==triangle2);
		assertTrue(d.containsTriangle(triangle2));
	}
	
	@Test
	public void testAddPointInTriangle(){
		Point p00 = new Point(0, 0);
		Point p40 = new Point(4, 0);
		Point p24 = new Point(2, 4);
		Point p22 = new Point(2, 2);
		
		Triangle triangle = new Triangle(p00, p40, p24);
		
		//p22 is contained by triangle
		//adding p22 must lead to built 3 new triangles.
		
		Triangle newTriangle1 = new Triangle(p00, p22, p24);
		Triangle newTriangle2 = new Triangle(p40, p22, p24);
		Triangle newTriangle3 = new Triangle(p00, p22, p40);
		
		d.getT().add(triangle);
		assertTrue(triangle.contains(p22));
		
		d.addPointInTriangle(p22, triangle);
		
		assertTrue(d.getT().size()==3);
		assertTrue(d.containsTriangle(newTriangle1));
		assertTrue(d.containsTriangle(newTriangle2));
		assertTrue(d.containsTriangle(newTriangle3));
		assertFalse(d.containsTriangle(triangle));
	}
	
	@Test
	public void testSegmentCrossing(){
		Point p00 = new Point(0, 0);
		Point p10 = new Point(1, 0);
		Point p01 = new Point(0, 1);
		Point p11 = new Point(1, 1);
		Point p12 = new Point(1, 2);
		
		assertTrue(d.segmentCrossing(p00, p11, p01, p10));
		assertFalse(d.segmentCrossing(p00, p10, p01, p12));
	}
	
	@Test
	public void testSegmentCrossingParallelIssue(){
		Point p00 = new Point(0, 0);
		Point p10 = new Point(1, 0);
		Point p01 = new Point(0, 1);
		Point p11 = new Point(1, 1);
		
		Point p22 = new Point(2, 2);
		Point p20 = new Point(2, 0);
		
		assertFalse(d.segmentCrossing(p00, p10, p01, p11));
		assertTrue(d.segmentCrossing(p00, p11, p01, p20));
		assertFalse(d.segmentCrossing(p22, p01, p01, p11));
		assertFalse(d.segmentCrossing(p22, p01, p11, p20));
	}
	
	@Test
	public void testGetListOfPointsVisibleBy(){
		ArrayList<Point> pointList = new ArrayList<Point>();
		Point p00 = new Point(0, 0); pointList.add(p00);
		Point p20 = new Point(2, 0); pointList.add(p20);
		Point p01 = new Point(0, 1); pointList.add(p01);
		Point p11 = new Point(1, 1); pointList.add(p11);
		Point p22 = new Point(2, 2); pointList.add(p22);
		d.setP(pointList);
		Triangle triangle = new Triangle(p01, p20, p11);
		d.getT().add(triangle);
		
		ArrayList<Point> originSet = d.getListOfPointsVisibleBy(p00);
		//the origin should only see p01 and p10
		
		ArrayList<Point> p22Set = d.getListOfPointsVisibleBy(p22);
		//p22 should see the whole triangle p01, p10, p11
		
		assertTrue(originSet.size()==2);
		assertTrue(originSet.contains(p01));
		assertTrue(originSet.contains(p20));
		
		assertTrue(p22Set.size()==3);
		assertTrue(p22Set.contains(p01));
		assertTrue(p22Set.contains(p20));
		assertTrue(p22Set.contains(p11));
	}
	
	@Test
	public void testGetNeighboursOnVisibleLineFor(){
		ArrayList<Point> pointList = new ArrayList<Point>();
		Point p00 = new Point(0, 0); pointList.add(p00);
		Point p20 = new Point(2, 0); pointList.add(p20);
		Point p01 = new Point(0, 1); pointList.add(p01);
		Point p11 = new Point(1, 1); pointList.add(p11);
		Point p22 = new Point(2, 2); pointList.add(p22);
		d.setP(pointList);
		Triangle triangle = new Triangle(p01, p20, p11);
		d.getT().add(triangle);
		
		ArrayList<Point[]> visibleSegmentListOrigin = d.getNeighboursOnVisibleLineFor(p00);
		assertTrue(visibleSegmentListOrigin.size()==1);
		assertTrue(visibleSegmentListOrigin.get(0)[0].equals(p01));
		assertTrue(visibleSegmentListOrigin.get(0)[1].equals(p20));
		
		ArrayList<Point[]> visibleSegmentListForp22 = d.getNeighboursOnVisibleLineFor(p22);
		assertTrue(visibleSegmentListForp22.size()==2);
		
		assertTrue(visibleSegmentListForp22.get(0)[0].equals(p20));
		assertTrue(visibleSegmentListForp22.get(0)[1].equals(p11));
		assertTrue(visibleSegmentListForp22.get(1)[0].equals(p11));
		assertTrue(visibleSegmentListForp22.get(1)[1].equals(p01));
	}
	
	/*
	@Test
	public void testAddPointOnSegmentExtern(){
		Point p00 = new Point(0, 0);
		Point p40 = new Point(4, 0);
		Point p24 = new Point(2, 4);
		
		Point p32 = new Point(3, 2);//sur le segment [p24, p40]
		
		Triangle triangle = new Triangle(p00, p40, p24);
		
		fail("not implemented yet");
	}
	
	@Test
	public void testAddPointOnSegmentIntern(){
		Point p00 = new Point(0, 0);
		Point p40 = new Point(4, 0);
		Point p24 = new Point(2, 4);
		Point p54 = new Point(5, 4);
		
		Point p32 = new Point(3, 2);//sur le segment commun aux deux triangles [p24, p40]
		
		Triangle triangle = new Triangle(p00, p40, p24);
		Triangle triangleVoisin = new Triangle(p54, p40, p24);
		
		d.getT().add(triangle);
		d.getT().add(triangleVoisin);
		
		d.addPointOnSegment();
		
		fail("not implemented yet");
	}
	*/
	@Test
	public void testAddPointOutside(){
		ArrayList<Point> pointList = new ArrayList<Point>();
		Point p00 = new Point(0, 0); pointList.add(p00);
		Point p20 = new Point(2, 0); pointList.add(p20);
		Point p01 = new Point(0, 1); pointList.add(p01);
		Point p11 = new Point(1, 1); pointList.add(p11);
		Point p22 = new Point(2, 2); pointList.add(p22);
		d.setP(pointList);
		Triangle triangle = new Triangle(p01, p20, p11);
		d.getT().add(triangle);
		
		d.addPointOutside(p22);
		assertTrue(d.getT().size()==3);
		assertTrue(d.containsTriangle(new Triangle(p22, p11, p20)));
		assertTrue(d.containsTriangle(new Triangle(p22, p11, p01)));
		
		d.addPointOutside(p00);
		assertTrue(d.getT().size()==4);
		assertTrue(d.containsTriangle(new Triangle(p00, p01, p20)));
	}
	
	@Test
	public void testAddPointOutsideAvoidTriangleContained(){
		ArrayList<Point> pointList = new ArrayList<Point>();
		Point p00 = new Point(0, 0); pointList.add(p00);
		Point p20 = new Point(2, 0); pointList.add(p20);
		Point p01 = new Point(0, 1); pointList.add(p01);
		Point p11 = new Point(1, 1); pointList.add(p11);
		Point p22 = new Point(2, 2); pointList.add(p22);
		d.setP(pointList);
		Triangle triangle = new Triangle(p01, p20, p11);
		d.getT().add(triangle);
		
		d.addPointOutside(p00);
		assertTrue(d.getT().size()==2);
		assertTrue(d.containsTriangle(new Triangle(p00, p01, p20)));
		
		Triangle testContains = new Triangle(p01, p22, p20);
		d.addPointOutside(p22);
		assertTrue(d.getT().size()==4);
		assertTrue(d.containsTriangle(new Triangle(p22, p11, p20)));
		assertTrue(d.containsTriangle(new Triangle(p22, p11, p01)));
	}
	
	@Test
	public void testAddPoint(){
		Point p00 = new Point(0, 0);
		Point p20 = new Point(2, 0);
		Point p01 = new Point(0, 1);
		Point p11 = new Point(1, 1);
		Point p22 = new Point(2, 2);
		
		Delaunay delauney = new Delaunay();
		
		delauney.addPoint(p01);
		delauney.addPoint(p22);
		delauney.addPoint(p20);
		//the first triangle should be created
		assertTrue(delauney.getT().size()==1);
		
		delauney.addPoint(p11);
		//the point is added inside the triangle
		assertTrue(delauney.getT().size()==3);
		
		delauney.addPoint(p00);
		//p00 is added as an outside point
		assertTrue(delauney.getT().size()==4);
		
		//P contains exactly 5 points
		assertTrue(delauney.getP().size()==5);
	}

}
