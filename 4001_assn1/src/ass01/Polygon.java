package ass01;

import java.util.List;

/**
 * A convex polygon.
 * A polygon is a 2D shape with straight edges.
 * It has a fixed number of vertices and edges.
 * 
 * Vertices are represented as 2D vectors.
 * Vertices must be indexed in anti-clockwise order around the polygon.
 * 
 * Trivial polygons with 0, 1 or 2 vertices are allowed.
 * A 0-vertex polygon is empty, with area 0 and perimeter 0.
 * A 1-vertex polygon is a point, with area 0 and perimeter 0.
 * A 2-vertex polygon is a line, with area 0 and perimeter twice the line length.
 * 
 * @author potter
 */
public interface Polygon
{
	// read operations on one polygon
	
	/**
	 * List of vertices forming the polygon.
	 * Provides a snapshot view of this polygon.
	 * The list that is returned should not be modifiable.
	 * Any attempt to apply update operations on the returned list must throw
	 * an UnsupportedOperationException.
	 * Using the standard Java libraries this is as close as we get to immutable collections!
	 */
	List<Vector> vertices ();
	
	/**
	 * Area of this polygon.
	 */
	double area ();
	
	/**
	 * Perimeter of this polygon.
	 * The perimeter is the sum of the length of the edges.
	 */
	double perimeter ();
	
	
	/**
	 * Get a vertex of this polygon.
	 * @param index of vertex in range vertices list.
	 * @throws IllegalArgument exception if index is out of range
	 */
	Vector getVertex (int index) throws IllegalArgumentException;
	
	/**
	 * Get interior angle at a vertex of this polygon.
	 * Vertices of 1-vertex and 2-vertex polygons have interior angle 0, by definition.
	 * @param index of vertex in range from 0 to vertexCount-1.
	 * @return angle in range range [0, Math.Pi).
	 * @throws IllegalArgument exception if index is out of range
	 */
	double interiorAngle (int index) throws IllegalArgumentException;
	
	/**
	 * Invariant for convex polygon.
	 * Checks that the list of vertices forms a valid convex polygon,
	 * and that vertices are indexed in anti-clockwise order.
	 * Interior angles at vertices must be in range from 0 inclusive to Math.Pi exclusive.
	 * This method should always return true when the polygon is in a stable state.
	 * It is intended for invariant checks within implementations.
	 */
	boolean isValid ();
	
	/**
	 * The intersection of this polygon with that.
	 * The result may be empty: a 0-vertex polygon.
	 * @return an immutable polygon
	 */
	Polygon intersect (Polygon that);
	
	/**
	 * Equivalent polygons have the same vertices,
	 * but their vertex listings need not start in the same position.
	 * @return this is equivalent to that
	 */
	boolean isEquivalent (Polygon that);
	
	/**
	 * Make a mutable copy of this polygon.
	 * @return a fresh mutable polygon
	 */
	Mutable copy ();
	
	/**
	 * Make a mutable copy of this polygon if this is immutable.
	 * Otherwise return this
	 * @return this or a mutable copy of this
	 */
	Mutable makeMutable ();
	
	/**
	 * Make an immutable copy of this polygon if this is mutable.
	 * Otherwise return this.
	 * @return an immutable copy of this
	 */
	Polygon freeze ();
   
	
	/**
	 * Extension of Polygon interface with write operations.
	 */
	interface Mutable extends Polygon
	{
		// write operations
		
		/**
		 * Scale the polygon by a given factor.
		 * @param factor 
		 */
		void scale (double factor) throws IllegalArgumentException;
		
		/**
		 * Translate this polygon by a given shift.
		 * @param shift vector to add to each vertex.
		 */
		void translate (Vector shift);
		
	    /**
	     * Set a specified vertex of the polygon to a new position.
	     * The set operation returns false if the new position would result
	     * in an invalid (non-convex) polygon; in that case, no change is made.
	     * If the operation is not valid, implementations may choose to suspend
	     * for a given time-out, during which other updates may alter the validity
	     * of the operation.
	     * @param index must be in range [0, vertexCount-1)
	     * @param vertex new position for given index
	     * @return success of set operation, preserving convexity
	     * @throws IllegalArgument exception if index is out of range
	     */
	    boolean setVertex (int index, Vector vertex) throws IllegalArgumentException;
	}
}
