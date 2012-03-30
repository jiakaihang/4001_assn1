package ass01.concurrent_test;

import static org.junit.Assert.assertEquals;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import ass01.Polygon;
import ass01.Vector;

public abstract class ConcurrentTestUtil {

    public static final double DELTA = Vector.DELTA;	// precision for double equality tests
	
    /** Check expected coordinates against actual vector. */
    public static void check (Polygon expected, Polygon actual)
    {
        assertEquals ("area", expected.area(), actual.area(), DELTA);
        assertEquals ("perimeter", expected.perimeter(), actual.perimeter(), DELTA);
    	checkEqualVertices(expected.vertices(), actual.vertices());
    }
    
	public static void checkEqualVertices(List<Vector> es, List<Vector> as) {
		int n = es.size();
		assertEquals("number of vertices", n, as.size());
		for (int i = 0; i < n; ++i) {
			assertEquals("equal vertex", es.get(i), as.get(i));
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Utility fields and methods for managing sub-threads
	// ///////////////////////////////////////////////////////////////////////////////

	protected volatile Throwable uncaught; // assigned by exception handler for
								 // sub-threads

	protected UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		public void uncaughtException(Thread t, Throwable e) {
			uncaught = e;
		}
	};

	public Thread makeThreadWithHandler(Runnable r) {
		Thread t = new Thread(r);
		t.setUncaughtExceptionHandler(handler);
		return t;
	}

	public void forkJoinWithThreadPerOp(Runnable... op) {
		assert uncaught == null;
		int nThreads = op.length;
		Thread[] thread = new Thread[nThreads];

		// create all subthreads with handler for uncaught exceptions
		for (int i = 0; i < nThreads; ++i) {
			thread[i] = makeThreadWithHandler(op[i]);
		}
		// start all threads
		for (Thread t : thread) {
			t.start();
		}
		// join all threads
		try {
			for (Thread t : thread) {
				t.join();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e); // convert to unchecked
		}

		handleLastUncaughtException();
	}

	public void handleLastUncaughtException() {
		if (uncaught instanceof AssertionError) {
			throw (AssertionError) uncaught;
		} else if (uncaught instanceof RuntimeException) {
			throw (RuntimeException) uncaught;
		} else if (uncaught != null) { // convert to unchecked exception
			throw new RuntimeException(uncaught);
		}
	}

}
