package ass01.concurrent_test;

import ass01.Polygon.Mutable;
import static ass01.Factory.*;

public class CopyOnWriteTest extends MutableTest {

	public Mutable make() {
		return makeCopyOnWrite(V.vertices());
	}

}
