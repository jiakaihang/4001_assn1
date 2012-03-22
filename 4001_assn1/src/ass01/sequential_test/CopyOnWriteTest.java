package ass01.sequential_test;

import org.junit.Test;

import ass01.Vector;
import ass01.Polygon.Mutable;
import static ass01.Factory.makeCopyOnWrite;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CopyOnWriteTest extends MutableTest {
	
	public CopyOnWriteTest(Vector[] vertexArray, double area, double perimeter,
			double angle) {
		super(vertexArray, area, perimeter, angle);
	}
	
	@Override
	protected Mutable makeMutableFromVertexList() {
		return makeCopyOnWrite (vertexList());
	}
	
	@Test
	@Override
	public void testCopy() {
		Mutable copy = poly.copy();
		assertTrue(copy.isValid());
		assertEquals(area, copy.area(), DELTA);
		// copy may return the same for a COW implementation
	}
}
