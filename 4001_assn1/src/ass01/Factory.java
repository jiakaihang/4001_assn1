package ass01;

import java.util.List;

public class Factory
{
	public static Polygon makeImmutable (List<Vector> vertices)
	{
		return new ImmutablePolygon(vertices);
	}

	public static Polygon.Mutable makeUnsafe (List<Vector> vertices)
	{
		return new UnsafePolygon(vertices);
	}

	public static Polygon.Mutable makeSynchronized (List<Vector> vertices)
	{
		return null;
	}

	public static Polygon.Mutable makeMutex (List<Vector> vertices)
	{
		// TODO
		return null;
	}

	public static Polygon.Mutable makeReadWrite (List<Vector> vertices)
	{
		// TODO
		return null;
	}

	public static Polygon.Mutable makeCopyOnWrite (List<Vector> vertices)
	{
		// TODO
		return null;
	}
}
