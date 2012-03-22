package ass01.sequential_test;

import ass01.Vector;
import ass01.Polygon.Mutable;
import static ass01.Factory.makeSynchronized;

public class SynchronizedTest extends MutableTest {
	
	public SynchronizedTest(Vector[] vertexArray, double area, double perimeter,
			double angle) {
		super(vertexArray, area, perimeter, angle);
	}
	
	@Override
	protected Mutable makeMutableFromVertexList() {
		return makeSynchronized (vertexList());
	}
}
