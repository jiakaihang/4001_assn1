package ass01.sequential_test;

import ass01.Vector;
import ass01.Polygon.Mutable;
import static ass01.Factory.makeMutex;

public class MutexTest extends MutableTest {
	
	public MutexTest(Vector[] vertexArray, double area, double perimeter,
			double angle) {
		super(vertexArray, area, perimeter, angle);
	}
	
	@Override
	protected Mutable makeMutableFromVertexList() {
		return makeMutex (vertexList());
	}
}
