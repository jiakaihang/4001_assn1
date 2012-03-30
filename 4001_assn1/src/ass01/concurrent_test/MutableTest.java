package ass01.concurrent_test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ass01.Polygon;
import ass01.Polygon.Mutable;
import ass01.Vector;


/**
 * General purpose class for defining unit tests with multiple threads.
 * Subclasses inherit these tests and must implement the make method to initialise
 * the type of polygon under test.
 * 
 * You may modify the initial value of N, controlling the length of each Runnable run.
 * If One_Thread tests fail, you may have increased N too far. Suggest max value of 2500.
 */
public abstract class MutableTest extends ConcurrentTestUtil
{
	static final VertexData V = new VertexData();
    static final int N = 2500;								// count for repeated updates in tests
	
	Mutable             poly;                              // test polygon
                                                           // initialised from make()
                                                           // with different types of
                                                           // vector using V.vertices()
	
///////////////////////////////////////////////////////////////////////////////// 
// Unit tests and setup    
/////////////////////////////////////////////////////////////////////////////////  
    
    @Before
    public void initialiseTest () {
        uncaught = null;	// static variable in ConcurrentTestUtil
        poly = make ();    // defer initialisation of v to subclasses
    }
    
    public abstract Mutable make ();
    
	@Test
    public void scale_One_Thread ()
    {
		double factor = 1.001;
        Polygon expected = V.scaledPolygon(Math.pow(factor, 2*N));
        
        Runnable r1 = scaleRunnable(factor);
        Runnable r2 = scaleRunnable(factor);
        r1.run();
        r2.run();
    	check (expected, poly);
    }
    
	@Test
    public void scale_Two_Thread ()
    {
		double factor = 1.001;
        Polygon expected = V.scaledPolygon(Math.pow(factor, 2*N));
        
        Runnable r1 = scaleRunnable(factor);
        Runnable r2 = scaleRunnable(factor);
        forkJoinWithThreadPerOp (r1, r2);
    	check (expected, poly);
    }

	@Test
    public void translate_One_Thread ()
    {
		Vector shift = Vector.at(1,1);
	    Polygon expected = V.translatedPolygon(shift.scale(2*N));
	    
        Runnable r1 = translateRunnable(shift);
        Runnable r2 = translateRunnable(shift);
        r1.run();
        r2.run();
        check (expected, poly);
    }
    
	@Test
    public void translate_Two_Thread ()
    {
		Vector shift = Vector.at(1,1);
        Polygon expected = V.translatedPolygon(shift.scale(2*N));

        Runnable r1 = translateRunnable(shift);
        Runnable r2 = translateRunnable(shift);
        forkJoinWithThreadPerOp (r1, r2);
        check (expected, poly);
    }
    
	/**
	 * Tests performance for concurrent read operations.
	 * Timings are displayed on standard output.
	 * CopyOnWrite should do better for this test than other thread-safe classes.
	 */
	@Test
	public void testPerformance_read_8 ()
	{
		// warm up ...
		read_8_seq();
		read_8_seq();
		
		// timed runs
		setTime();
		read_8_conc();
		display("concurrent", timeWithReset());
		read_8_seq();
		display("sequential", timeWithReset());
		System.out.println();
	}

	void read_8_seq() {
		final int n = 8;
		for (int i = 0; i < n; ++i)
			readRunnable().run();
	}

	void read_8_conc() {
		forkJoinWithThreadPerOp(readRunnable(), readRunnable(), readRunnable(), readRunnable(), readRunnable(), readRunnable(), readRunnable(), readRunnable());
	}

	protected void display(String execInfo, long duration) {
		System.out.print(getClass().getSimpleName());
		System.out.print(" -- ");
		System.out.print(execInfo);
		System.out.print(" -- duration for 8 read runnables = ");
		System.out.print(duration);
		System.out.print(" ms");
		System.out.println();
	}

 
///////////////////////////////////////////////////////////////////////////////// 
// Runnables used in tests   
/////////////////////////////////////////////////////////////////////////////////  

    Runnable scaleRunnable (final double factor)
    {
        return new Runnable ()
            {
                public void run ()
                {
                    for (int i = 0; i < N; ++i) {
                        poly.scale (factor);
//                        fail("failing Runnable");
                        assertTrue("valid polygon", poly.isValid());
                    }
                }
                
            };
    }
    
    Runnable translateRunnable (final Vector shift)
    {
        return new Runnable ()
            {
                public void run ()
                {
                    for (int i = 0; i < N; ++i) {
                        poly.translate (shift);
//                      fail("failing Runnable");
                        assertTrue("valid polygon", poly.isValid());
                    }
                }
                
            };
    }

    Runnable readRunnable ()
    {
        return new Runnable ()
            {
                public void run ()
                {
                    for (int i = 0; i < N; ++i) {
                        poly.area();
                        poly.perimeter();
                        assertTrue("valid polygon", poly.isValid());
                    }
                }
                
            };
    }
    
///////////////////////////////////////////////////////////////////////////////// 
 // Utilities for timing tests   
 /////////////////////////////////////////////////////////////////////////////////  

	/**
	 * Test start time. Initialise with System.currentTimeMillis()
	 */
	long startTime;

	/**
	 * Time in millisecs since last setTime.
	 */
	long time() {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Set time in millisecs.
	 * @return current time
	 */
	long setTime() {
		startTime = System.currentTimeMillis();
		return startTime;
	}

	/**
	 * Time in millisecs since last startTime. Resets startTime.
	 */
	long timeWithReset() {
		long oldStartTime = startTime;
		return setTime() - oldStartTime;
	}
}
