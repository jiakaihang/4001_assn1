package ass01.concurrent_test;

import org.junit.Test;

import ass01.Polygon.Mutable;
import static ass01.Factory.*;

public class UnsafeTest extends MutableTest {

	public Mutable make() {
		return makeUnsafe(V.vertices());
	}

	@Override
	@Test(expected = AssertionError.class)
	public void scale_Two_Thread() {
		super.scale_Two_Thread();
	}

	@Override
	@Test(expected = AssertionError.class)
	public void translate_Two_Thread() {
		super.translate_Two_Thread();
	}
	
	

}
