package ass01.sequential_test;

import org.junit.Before;

import ass01.Vector;

public class ImmutableTest extends PolygonTest {

	public ImmutableTest(Vector[] vertexArray, double area, double perimeter,
			double angle) {
		super(vertexArray, area, perimeter, angle);
	}

	@Before
	public void setUp() {
		poly = ass01.Factory.makeImmutable(vertexList());
	}
}
