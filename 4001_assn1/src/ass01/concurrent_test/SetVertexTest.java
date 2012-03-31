package ass01.concurrent_test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import ass01.*;
import static ass01.Factory.*;

public class SetVertexTest extends ConcurrentTestUtil {

	Polygon.Mutable poly;		// test polygon initialised in each test method
	boolean expectSetVertexOK;

	// following data initialised in setUp. Same for each test.
	int n = 100;				// number of shifts for each vertex
	List<Vector> inits;			// initial vertex list
	List<Vector> expecteds;		// expected final vertex list
	
	
	@Before
	public void setUp() {
		uncaught = null;		// reset thread exceptions
		
		inits = new VertexData().vertices();
		int size = inits.size();
		expecteds = new ArrayList<Vector>(size);
		for (int i = 0; i < size; ++i) {
			expecteds.add(nextVertex(i, n));
		}
	}

	@Test
	public void testUnsafe() {
		expectSetVertexOK = false;
		poly = makeUnsafe(inits);
		checkSetVertex();
	}

	@Test
	public void testSynchronized() {
		expectSetVertexOK = false;
		poly = makeSynchronized(inits);
		checkSetVertex();
	}

	@Test
	public void testMutex() {
		expectSetVertexOK = true;
		poly = makeMutex(inits);
		checkSetVertex();
	}

	@Test
	public void testReadWrite() {
		expectSetVertexOK = true;
		poly = makeReadWrite(inits);
		checkSetVertex();
	}

	void checkSetVertex() {
		forkJoinWithThreadPerOp(
				setVertexRunnable(0),
				setVertexRunnable(1),
				setVertexRunnable(2),
				setVertexRunnable(3)
		);
		List<Vector> actuals = poly.vertices();
		if (expectSetVertexOK)
			checkEqualVertices(expecteds, actuals);
	}

	/**
	 * Runnable shifting i'th vertex n times, using setVertex.
	 * @param i
	 * @return
	 */
	Runnable setVertexRunnable(final int i) {
		return new Runnable() {
			public void run() {
				for (int k = 1; k <= n; ++k ) {
					Vector vertex = nextVertex(i, k);
					//System.out.println("Vertices are: "+poly.vertices()+"\t setVertex("+i+", "+vertex+") ");
					boolean ok = poly.setVertex(i, vertex);
// DEBUG					
//					System.out.println("Vertices are: "+poly.vertices()+"\t setVertex("+i+", "+vertex+") "+ok);
					if (!ok) {
						// so we should be expecting a false return
						assertFalse("false for setVertex at "+i+" to "+vertex, expectSetVertexOK);
						// otherwise set has failed so terminate the runnable
						return;
					}
				}
			} 
			
		};
	}
	
	Vector nextVertex(int index, int k) {
		Vector init = inits.get(index);
		Vector vertex = Vector.at(init.x + k * 6, init.y + k * 3);
		return vertex;
	}
}
