package m2if.model;

import static org.junit.Assert.assertEquals;
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
	@Before
	public void setUp() throws Exception {
		p1 = new Point(1, 2);
		p2 = new Point(2, 3);
		p3 = new Point(4, 1);
		p4 = new Point(2, 2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPoint() {
		Point point1 = new Point(1, 2);
		Point point2 = new Point(1, 2);
		
		assertTrue(point1.equals(point2));
		assertFalse(point1==point2);
	}
	
	@Test
	public void testTriangle() {
		Triangle t1 = new Triangle(p1, p2, p3);
		Triangle t2 = new Triangle(p1, p3, p2);
		Triangle t3 = new Triangle(p1, p2, p4);
		
		assertTrue(t1.equals(t2));
		assertFalse(t1.equals(t3));
	}

}
