package ass01;

/**
 * Immutable 2D vectors, with double coordinates.
 * 
 * @author potter
 *
 */
public final class Vector
{
	/** 
	 * Factory method for a 2D Vector.
	 */
	public static Vector at (double x, double y)

	{
		return new Vector(x, y);
	}
    public final double x;
    public final double y;

    public Vector(double x, double y)
    {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Add coordinates of that Vector to this.
	 */
	public Vector add (Vector that)
	{
		return at(x + that.x, y + that.y);
	}

	/**
	 * Subtract coordinates of that Vector from this.
	 */
	public Vector subtract (Vector that)
	{
		return at(x - that.x, y - that.y);
	}
	
	/**
	 * Scale coordinates of this Vector by multiplier.
	 */
	public Vector scale (double multiplier)
	{
		return at(multiplier * x, multiplier * y);
	}
	
	/**
	 * Scalar product of this Vector with that.
	 * Useful for finding projections of one vector on another.
	 */
	public double scalarProduct (Vector that)
	{
		return x * that.x + y * that.y;
	}

	/**
	 * Vector product of this Vector with that.
	 * Useful for finding areas of parallelograms formed by the vectors.
	 */
	public double vectorProduct (Vector that)
	{
		return x * that.y - y * that.x;
	}

	@Override
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Vector) {
			Vector that = (Vector) o;
			return equals(this.x, that.x) && equals (this.y, that.y);
		} else {
			return false;
		}
		
	}
	
	/**
	 * Absolute precision when comparing components of vectors.
	 * Used in override of equals(Object) method.
	 */
	public static double DELTA = 1e-9d;
	
	private boolean equals (double d1, double d2) {
		double diff = d1 - d2;
		return Math.abs(diff) <= DELTA;
	}
}
