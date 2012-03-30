package ass01.concurrent_test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ass01.Polygon;
import ass01.Vector;
import static ass01.Vector.at;

public class VertexData {
	final Vector[] data =
		new Vector[] {at(-1,-1), at(1,-1), at(1,1), at(-1,1)};
	final double area = 4;
	final double perimeter = 8;

	public List<Vector> vertices() {
		return new ArrayList<Vector>(Arrays.asList(data));
	}
	
	
	public Polygon scaledPolygon (final double factor) {
		return new Given() {
			public double area() {
				return area * factor * factor;
			}

			public double perimeter() {
				return perimeter * factor;
			}
			
			public List<Vector> vertices() {
				List<Vector> result = VertexData.this.vertices();
				for (int i = 0; i < result.size(); ++i) {
					Vector v = result.get(i);
					v = v.scale(factor);
					result.set(i, v );
				}
				return result ;
			}

		};
	}
	
	public Polygon translatedPolygon (final Vector shift) {
		return new Given() {
			public double area() {
				return area;
			}

			public double perimeter() {
				return perimeter;
			}
			
			public List<Vector> vertices() {
				List<Vector> result = VertexData.this.vertices();
				for (int i = 0; i < result.size(); ++i) {
					Vector v = result.get(i);
					v = v.add(shift);
					result.set(i, v );
				}
				return result ;
			}

		};
	}
	
	/**
	 * Stub implementation of most Polygon methods.
	 */
	abstract class Given implements Polygon {

		public Vector getVertex(int index) throws IllegalArgumentException {
			return null;
		}

		public double interiorAngle(int index) throws IllegalArgumentException {
			return 0;
		}

		public boolean isValid() {
			return false;
		}

		public Polygon intersect(Polygon that) {
			return null;
		}

		public boolean isEquivalent(Polygon that) {
			return false;
		}

		public Mutable copy() {
			return null;
		}

		public Polygon freeze() {
			return null;
		}
		
		public Mutable makeMutable() {
			return null;
		}	
	}
}