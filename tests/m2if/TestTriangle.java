package m2if;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import m2if.Point;
import m2if.Triangle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTriangle {

	private Point p1;
	private Point p2;
	private Point p3;
	private Point p4;
	
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
	public void testEquals() {
		Triangle t1 = new Triangle(p1, p2, p3);
		Triangle t2 = new Triangle(p1, p3, p2);
		Triangle t3 = new Triangle(p1, p2, p4);
		
		assertTrue(t1.equals(t2));
		assertFalse(t1.equals(t3));
	}
	
	@Test
	public void testContainsPoint() {
		Triangle t1 = new Triangle(p1, p2, p3);
		Point ext = new Point(2, 1);
		
		assertTrue(t1.contains(p4));
		assertFalse(t1.contains(ext));
	}
	
	@Test
	public void testHasSegment() {
		Triangle t1 = new Triangle(p1, p2, p3);
		Point ext = new Point(2, 1);
		
		assertTrue(t1.hasSegment(p1, p3));
		assertFalse(t1.hasSegment(p1, ext));
	}
	
	@Test
	public void testGetArea() {
		Triangle t1 = new Triangle(p1, p2, p4);
		
		assertTrue(t1.getArea()==1.0/2);
	}
	
	@Test
	public void testGetCommonSegment() {
		Triangle t1 = new Triangle(p1, p2, p3);
		Triangle t2 = new Triangle(p1, p2, p4);
		Point ext = new Point(2, 1);
		Triangle t_ext = new Triangle(p1, ext, p4);
		
		Point[] commonSeg = new Point[4];
		commonSeg = t1.getCommonSegment(t2);
		assertTrue(commonSeg[0].equals(p1));
		assertTrue(commonSeg[1].equals(p2));
		assertTrue(commonSeg[2].equals(p3));
		assertTrue(commonSeg[3].equals(p4));
		
		assertTrue(t1.getCommonSegment(t_ext)==null);
	}

}
