package ass01;

import java.util.ArrayList;
import java.util.List;

public class CopyOnWritePolygon extends UnsafePolygon {

	public CopyOnWritePolygon(List<Vector> vertices) {
		super(vertices);
	}
	
	@Override
	public void scale(double factor) throws IllegalArgumentException {
		synchronized(this){
			List<Vector> temp = new ArrayList<Vector>(vertices);
			for(int i=0; i<temp.size(); i++)
				temp.set(i, temp.get(i).scale(factor));
			super.vertices = temp;
		}
	}

	@Override
	public void translate(Vector shift){
		synchronized(this){
			List<Vector> temp = new ArrayList<Vector>(vertices);
			for(int i=0; i<temp.size(); i++)
				temp.set(i, temp.get(i).add(shift));
			super.vertices = temp;
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
		if(index<0 || index>=super.vertices.size())
			throw new IllegalArgumentException();
		synchronized(this){
			List<Vector> temp = new ArrayList<Vector>(vertices);
			super.vertices.set(index, vertex);
			
			if(!isValid()){
				super.vertices = temp;
				return false;
			}			
			return true;
		}
	}

	
	@Override
	public boolean isValid() {
		synchronized(this){
			if(vertices == null){
				return false;
			}
			int N = vertices.size();
			//A polygon with 0, 1 or 2-vertex is always true;
			if(N == 0 || N == 1){
				if(area()!=0 && perimeter()!=0)
					return false;
			}
			else if(N==2){
				if(area()!=0)
					return false;
			}
			else{
				//vNext = v(i+1) vCurr = v(i) vPrev = v(i-1)
				for(int i=0; i<N; i++){
					Vector vPrev = vertices.get(((i-1)+N)%N);
					Vector vCurr = vertices.get(i);
					Vector vNext = vertices.get((i+1)%N);
					Vector v1 = vCurr.subtract(vPrev);
					Vector v2 = vNext.subtract(vCurr);
					double crossProduct = v1.vectorProduct(v2);
					
					if(crossProduct <= 0)
						return false;
				}
			}
			return true;
		}
	}
	

	@Override
	public Mutable copy() {
		return new SynchronizedPolygon(super.vertices);
	}

}
