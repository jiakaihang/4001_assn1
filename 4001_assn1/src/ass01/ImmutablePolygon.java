package ass01;

import java.util.List;

public final class ImmutablePolygon extends AbstractPolygon {

	public ImmutablePolygon(List<Vector> vertices) {
		super(vertices);
	}

	@Override
	public Mutable copy() {
		return null;
	}

	@Override
	public Mutable makeMutable() {
		return new UnsafePolygon(this.vertices());
	}

	@Override
	public Polygon freeze() {
		return this;
	}

}
