package ass01.sequential_test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import static java.util.Arrays.asList;

import ass01.*;
import ass01.Polygon.Mutable;
import static ass01.Vector.at;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith (Parameterized.class)
public abstract class PolygonTest {

	final static double DELTA = 1e-6;
	final static double HALF_PI = Math.PI/2;

	Vector[] vertexArray;
	double area;
	double perimeter;
	double angle;
	
	
	public PolygonTest(Vector[] vertexArray, double area, double perimeter,
			double angle) {
		this.vertexArray = vertexArray;
		this.area = area;
		this.perimeter = perimeter;
		this.angle = angle;
	}
	
	final static VertexData V = new VertexData();
	@Parameters
	public static Collection<Object []> data () {
		return asList(new Object [] [] {
				{ V.empty, V.emptyArea, V.emptyPerimeter, Double.NaN },
				{ V.vector1, V.emptyArea, V.emptyPerimeter, 0 },
				{ V.edge2, V.edgeArea, V.edgePerimeter, 0 },
				{ V.triangle, V.triangleArea, V.trianglePerimeter, V.triangleAngle },
				{ V.square1, V.squareArea, V.squarePerimeter, V.squareAngle},
				{ V.nPolygon, V.nArea, V.nPerimeter, V.nAngle}
		});
		
	}

	List<Vector> vertexList() { return asList(vertexArray); }
	int lastIndex() { return vertexArray.length - 1; }

	Polygon poly;

	@Test
	public void testIsValid() {
		assertTrue ("Polygon is valid", poly.isValid());
	}

	@Test
	public void testInputCopy() {
		if (vertexArray.length == 0) return;
		Vector original = vertexArray[0];
		Vector pv = poly.getVertex(0);
		assertTrue ("vertex 0 is same before input array update", pv.equals(vertexArray[0]));
		Vector update = at(-1,-1);
		vertexArray[0] = update;
		pv = poly.getVertex(0);	// poly should not see the update
		vertexArray[0] = original;	// restore data for later tests!!
		assertFalse ("vertex 0 is not same after input array update", pv.equals(update));
	}

	@Test
	public void testArea() {
		assertEquals (area, poly.area(), DELTA);
	}

	@Test
	public void testPerimeter() {
		assertEquals (perimeter, poly.perimeter(), DELTA);
	}

	@Test
	public void testGetVertex() {
		for (int i = 0; i < vertexArray.length; ++i) {
			assertEquals (vertexArray[i], poly.getVertex(i));
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetVertexBad() {
		poly.getVertex(poly.vertices().size());
	}
	
	@Test(expected = Exception.class)	// non-specific exception
	public void testVerticesForMutability() {
		poly.vertices().set(0, at(0,0));
	}
	
	@Test
	public void testInteriorAngle() {
		for (int i = 0; i < vertexArray.length; ++i) {
			assertEquals (angle, poly.interiorAngle(i), DELTA);
		}
	}

	@Test
	public void testCopy() {
		Mutable copy = poly.copy();
		assertTrue(copy.isValid());
		assertEquals(area, copy.area(), DELTA);
		assertNotSame(poly, copy);		
	}
	
	@Test
	public void testMakeMutable() {
		Mutable mutable = poly.makeMutable();
		assertTrue(mutable.isValid());
		assertEquals(area, mutable.area(), DELTA);
		if (poly instanceof Mutable)
			assertSame(poly, mutable);
		else
			assertNotSame(poly, mutable);
	}

	@Test
	public void testFreeze() {
		Polygon frozen = poly.freeze();
		
		if (!(poly instanceof Mutable))
			assertSame(poly, frozen);
		else {
			assertEquals(area, frozen.area(), DELTA);
			assertFalse("freeze result should not be Mutable", frozen instanceof Mutable);
		}
	}

	@Test
	public void testNotEquivalent() {
		Vector[] thatVertices = V.add(vertexArray, at(5,5));		
		Polygon that = Factory.makeImmutable(asList(thatVertices));
		assertTrue(poly.isEquivalent(that) == poly.vertices().isEmpty());
	}

	@Test
	public void testIsEquivalent() {
		Vector[] thatVertices = V.rotate(vertexArray, 5);		
		Polygon that = Factory.makeImmutable(asList(thatVertices));
		assertTrue(poly.isEquivalent(that));
	}

	@Test
	public void testIsEquivalentEmpty() {
		Polygon that = Factory.makeImmutable(asList(V.empty));
		assertTrue(poly.isEquivalent(that) == poly.vertices().isEmpty());
	}

	@Test
	public void testIsEquivalentCopy() {
		assertTrue(poly.isEquivalent(poly.copy()));
	}

	@Test
	public void testIsEquivalentFreeze() {
		assertTrue(poly.isEquivalent(poly.freeze()));
	}
	
	@Test
	public void testIsEquivalentMutable() {
		assertTrue(poly.isEquivalent(poly.freeze()));
	}

}