package m2if;

import static org.junit.Assert.*;

import m2if.Point;
import m2if.Rectangle;

import org.junit.Before;
import org.junit.Test;

public class TestRectangle {

	private Rectangle rectangle;
	@Before
	public void setUp() throws Exception {
		Point p00 = new Point(0, 0);
		Point p11 = new Point(1, 1);
		
		Point[] segment = {p00, p11};
		rectangle = new Rectangle(segment);
	}

	@Test
	public void test() {
		assertTrue(rectangle.containsCoord(0.75, 0.25));
		assertFalse(rectangle.containsCoord(1.1, 0.25));
	}

}
