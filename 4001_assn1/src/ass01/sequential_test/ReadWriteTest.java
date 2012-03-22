package ass01.sequential_test;

import ass01.Vector;
import ass01.Polygon.Mutable;
import static ass01.Factory.makeReadWrite;

public class ReadWriteTest extends MutableTest {
	
	public ReadWriteTest(Vector[] vertexArray, double area, double perimeter,
			double angle) {
		super(vertexArray, area, perimeter, angle);
	}
	
	@Override
	protected Mutable makeMutableFromVertexList() {
		return makeReadWrite (vertexList());
	}
}
