package m2if.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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
	public void testLegalize() {
		Point point1 = new Point(1, 2);
		Point point2 = new Point(1, 2);
		
		assertTrue(point1.equals(point2));
		assertFalse(point1==point2);
	}

}
