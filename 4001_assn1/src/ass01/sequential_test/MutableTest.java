package ass01.sequential_test;

import static ass01.Vector.at;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ass01.Polygon;
import ass01.Polygon.Mutable;
import ass01.Vector;

public abstract class MutableTest extends PolygonTest {

	Polygon.Mutable mutable;

	public MutableTest(Vector[] vertexArray, double area, double perimeter,
			double angle) {
		super(vertexArray, area, perimeter, angle);
	}

	@Before
	public void setUp() {
		poly = makeMutableFromVertexList();
		mutable = (Polygon.Mutable) poly;
	}

	/**
	 * Subclasses override this with different Factory methods.
	 */
	protected abstract Mutable makeMutableFromVertexList();

	@Test
	public void testScale() {
		double factor = 10d;
		mutable.scale(factor);
		area *= factor * factor;
		assertEquals (area, poly.area(), DELTA);
		assertEquals (perimeter, poly.perimeter(), DELTA);
	}

	@Test
	public void testScaleOnInput() {
		double factor = 10d;
		mutable.scale(factor);
		int index = lastIndex();
		if (index < 0) return; // ignore 0-polygons
		Vector inputAtIndex = vertexList().get(index);
		Vector scaledAtIndex = poly.getVertex(index);
		assertEquals (inputAtIndex.scale(factor), scaledAtIndex);
	}

	@Test
	public void testScaleDoesNotAffectInputData() {
		int index = lastIndex();
		if (index < 0) return; // ignore 0-polygons
		Vector inputAtIndexBeforeScale = vertexList().get(index);
		double factor = 10d;
		mutable.scale(factor);
		Vector inputAtIndexAfterScale = vertexList().get(index);
		assertEquals (inputAtIndexBeforeScale, inputAtIndexAfterScale);
	}

	@Test
	public void testTranslate() {
		Vector shift = at(5, 5);
		mutable.translate(shift);
		assertEquals (area, poly.area(), DELTA);
		assertEquals (perimeter, poly.perimeter(), DELTA);
		int index = lastIndex();
		if (index < 0) return;
		Vector inputAtIndex = vertexList().get(index);
		Vector translatedAtIndex = poly.getVertex(index);
		assertEquals (inputAtIndex.add(shift), translatedAtIndex);
	}

	@Test
	public void testTranslateDoesNotAffectInputData() {
		int index = lastIndex();
		if (index < 0) return;	// ignore 0-polygons
		Vector inputAtIndexBeforeTranslate = vertexList().get(index);
		Vector shift = at(5, 5);
		mutable.translate(shift);
		Vector inputAtIndexAfterTranslate = vertexList().get(index);
		assertEquals (inputAtIndexBeforeTranslate, inputAtIndexAfterTranslate);
	}

	@Test
	public void testSetVertex() {
		int index = lastIndex();
		if (index < 0) return;	// ignore 0-polygons
		Vector vertex = mutable.getVertex(index);
		vertex = vertex.scale(1.00001); // small shift preserves convexity
		boolean ok = mutable.setVertex(index, vertex);
		assertTrue("update succeeded", ok);
	}

	@Test
	public void testSetVertexBadUpdate() {
		int index = lastIndex();
		if (index < 2) return;	// ignore 0-, 1-, 2-polygons
		Vector vertex = at(-20,0);
		boolean ok = mutable.setVertex(index, vertex);
		assertTrue("update failed", !ok);
		assertEquals("no change when update fails", area, poly.area(), DELTA);
	}

	@Test(expected = Exception.class)
	public void testSetVertexBadIndex() {
		Vector vertex = at(10, 5);
		mutable.setVertex(lastIndex()+1, vertex);
	}

}