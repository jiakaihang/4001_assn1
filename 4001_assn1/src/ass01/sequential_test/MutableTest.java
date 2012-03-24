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

	/**
	 * Test that area and perimeter scales correctly with polygon.
	 */
	@Test
	public void testScale() {
		double factor = 10d;
//		display();	// debug
		mutable.scale(factor);	// scale test polygon
		area *= factor * factor;
		perimeter *= factor;
//		display();	// debug
		assertEquals (area, poly.area(), DELTA);
		assertEquals (perimeter, poly.perimeter(), DELTA);
	}
	
	/**
	 * Test that last vertex scales correctly in polygon.
	 */
	@Test
	public void testScaleOnInput() {
		double factor = 10d;
		mutable.scale(factor);	// scale test polygon
		int index = lastIndex();
		if (index < 0) return; // ignore 0-polygons
		Vector inputAtIndex = vertexList().get(index);
		Vector scaledAtIndex = poly.getVertex(index);
		assertEquals (inputAtIndex.scale(factor), scaledAtIndex);	// scale last vertex of input data
	}

	/**
	 * Test that scaling polygon does not affect input list of vertices.
	 * Compares last vertex of input data with last vertex of scaled polygon.
	 */
	@Test
	public void testScaleDoesNotAffectInputData() {
		int index = lastIndex();
		if (index < 0) return; // ignore 0-polygons
		Vector inputAtIndexBeforeScale = vertexList().get(index);
		double factor = 10d;
		mutable.scale(factor);	// scale test polygon
		Vector inputAtIndexAfterScale = vertexList().get(index);
		assertEquals (inputAtIndexBeforeScale, inputAtIndexAfterScale);
	}

	/**
	 * Test that area and perimeter do not change with translate.
	 * Also test that last vertex translates correctly in polygon.
	 */
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

	/**
	 * Test that translating polygon does not affect input list of vertices.
	 * Compares last vertex of input data with last vertex of translated polygon.
	 */
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

	/**
	 * Test that last vertex can be reset to a new Vector.
	 * The new Vector is chosen for all test polygons so that they remain valid
	 * i.e. setVertex should return true.
	 */
	@Test
	public void testSetVertex() {
		int index = lastIndex();
		if (index < 0) return;	// ignore 0-polygons
		Vector vertex = mutable.getVertex(index);	// old vertex
		vertex = vertex.scale(1.00001); // new vertex ... small scale preserves convexity of test polygons
		boolean ok = mutable.setVertex(index, vertex);
		assertTrue("update succeeded", ok);
		assertEquals("polygon vertex updated at last index", vertex, poly.getVertex(index));
	}

	/**
	 * Test that last vertex can be reset to a new Vector.
	 * The new Vector is chosen for all test polygons so that they remain valid
	 * i.e. setVertex should return false, and there should be no change to the polygon.
	 */
	@Test
	public void testSetVertexBadUpdate() {
		int index = lastIndex();
		if (index < 2) return;	// ignore 0-, 1-, 2-polygons
		Vector vertex = at(-20,0);
		boolean ok = mutable.setVertex(index, vertex);	// attempt setVertex with index out of range
		assertTrue("update failed", !ok);
		assertEquals("no change when update fails", area, poly.area(), DELTA);
		assertEquals("no change when update fails", perimeter, poly.perimeter(), DELTA);
	}

	@Test(expected = Exception.class)
	public void testSetVertexBadIndex() {
		Vector vertex = at(10, 5);
		mutable.setVertex(lastIndex()+1, vertex);
	}
}