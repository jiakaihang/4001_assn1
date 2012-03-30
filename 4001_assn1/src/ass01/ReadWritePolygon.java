package ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWritePolygon extends UnsafePolygon {

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock rl = rwl.readLock();
	private final Lock wl = rwl.writeLock();
	
	public ReadWritePolygon(List<Vector> vertices) {
		super(vertices);
	}
	
	@Override
	public Vector getVertex(int index) throws IllegalArgumentException {
		if (index >= vertices.size() || index < 0)
			throw new IllegalArgumentException();
		rl.lock();
		try{return vertices.get(index);}
		finally{rl.unlock();}
	}

	@Override
	public void scale(double factor) throws IllegalArgumentException {
		wl.lock();
		try{
			for(int i=0; i<vertices.size(); i++)
				super.vertices.set(i, vertices.get(i).scale(factor));
		}finally{
			wl.unlock();
		}
		
	}

	@Override
	public void translate(Vector shift){
		wl.lock();
		try{
			for(int i=0; i<vertices.size(); i++)
				vertices.set(i, vertices.get(i).add(shift));
		}finally{
			wl.unlock();
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
		if(index<0 || index>=vertices.size())
			throw new IllegalArgumentException();
		wl.lock();
		try{
			List<Vector> origin = new ArrayList<Vector>(vertices);
			vertices.set(index, vertex);
			
			if(!isValid()){
				vertices = origin;
				return false;
			}			
			return true;
		}finally{
			wl.unlock();
		}
	}
	
	@Override
	public boolean isValid() {
		wl.lock();
		try{
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
		}finally{wl.unlock();}
	}

	@Override
	public Mutable copy() {
		return new ReadWritePolygon(vertices);
	}
}
