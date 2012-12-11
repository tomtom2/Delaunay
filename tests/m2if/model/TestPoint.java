package m2if.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestPoint {


	@Test
	public void testEquals() {
		Point point1 = new Point(1, 2);
		Point point2 = new Point(1, 2);
		Point point3 = new Point(2, 2);
		
		assertTrue(point1.equals(point2));
		assertFalse(point1==point2);
		assertFalse(point1.equals(point3));
	}
	
	@Test
	public void testDistancePoint() {
		Point point1 = new Point(1, 2);
		Point point2 = new Point(4, 6);
		
		assertTrue(point1.distance(point2)==5);
	}
	
	@Test
	public void testIsOnSegment() {
		Point point1 = new Point(1, 2);
		Point point2 = new Point(4, -1);
		Point pointOn = new Point(2, 1);
		Point pointOut = new Point(2, 2);
		
		assertTrue(pointOn.isOnSegment(point1, point2));
		assertFalse(pointOut.isOnSegment(point1, point2));
	}
	
	@Test
	public void testDistanceSegment() {
		Point point1 = new Point(1, 2);
		Point point2 = new Point(4, 6);
		Point point3 = new Point(4, 0);
		
		assertTrue(point1.distance(point2, point3)==3);
	}
	
}
