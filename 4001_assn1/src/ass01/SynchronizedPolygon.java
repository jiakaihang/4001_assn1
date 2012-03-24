/**
 * 
 */
package ass01;

import java.util.ArrayList;
import java.util.List;

import ass01.Polygon.Mutable;

/**
 * @author Kaihang
 *
 */
public class SynchronizedPolygon extends UnsafePolygon {

	/**
	 * @param vertices
	 */
	public SynchronizedPolygon(List<Vector> vertices) {
		super(vertices);
	}
	
	@Override
	public synchronized Vector getVertex(int index) throws IllegalArgumentException {
		if (index >= this.vertices().size() || index < 0)
			throw new IllegalArgumentException();
		return this.vertices().get(index);
	}
	
	@Override
	public void scale(double factor) throws IllegalArgumentException {
		synchronized(this){
			for(int i=0; i<this.vertices().size(); i++)
				this.vertices().set(i, this.vertices().get(i).scale(factor));
		}
	}

	@Override
	public void translate(Vector shift) {
		synchronized(this){
			for(int i=0; i<vertices().size(); i++)
				this.vertices().set(i, this.vertices().get(i).add(shift));
		}
	}

	/**
	 * Make a copy of the original vertices and do setVertex() 
	 * on this.vertices
	 * if isValid() then return true
	 * if !isValid() then change the vertices back to the original vertices
	 * and return false.
	 */
	@Override
	public boolean setVertex(int index, Vector vertex)
			throws IllegalArgumentException {
		if(index<0 || index>=this.vertices().size())
			throw new IllegalArgumentException();
		synchronized(this){
			List<Vector> origin = new ArrayList<Vector>(this.vertices());
			this.vertices().set(index, vertex);
			
			if(!isValid()){
				this.vertices = origin;
				return false;
			}			
			return true;
		}
	}

	@Override
	public Mutable copy() {
		return new SynchronizedPolygon(this.vertices());
	}
}
