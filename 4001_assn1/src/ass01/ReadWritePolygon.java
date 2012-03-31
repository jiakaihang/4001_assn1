package ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWritePolygon extends UnsafePolygon {

	private static final long Wating_Time = 500;
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock rl = rwl.readLock();
	private final Lock wl = rwl.writeLock();
	private Condition validMove = wl.newCondition();
	
	public ReadWritePolygon(List<Vector> vertices) {
		super(vertices);
	}
	
	@Override
	public List<Vector> vertices() {
		rl.lock();
		try{
			return new MyList<Vector>(vertices);
		}finally{rl.unlock();}
	}
	
	/**
	 * 
	 * Calculate the AREA of the polygon by the formula:
	 * A = 0.5 * vpSum 
	 * where vpSum is the sum of the vector product of all 
	 * adjacent vertices.
	 * For polygons with 0, 1 or 2-vertex return 0;
	 * @return area
	 */
	@Override
	public double area() {
		rl.lock();
		try{
			int N = vertices.size();
			if(N==0 || N==1 || N==2)
				return 0;
			double vpSum = 0;
			int i;
			for(i = 0; i<N; i++){
				Vector v1 = vertices.get(i);
				Vector v2 = vertices.get((i+1)%N);
				vpSum += v1.vectorProduct(v2);
			}
			return (0.5*vpSum);
		}finally{rl.unlock();}
	}

	/**
	 * Calculate the PERIMETER of the polygon by sum up 
	 * the length of each vector of the polygon.
	 * For polygons with 0 or 1-vertex return 0;
	 * For polygons with 2-vertex return twice the line length
	 * @return perimeter
	 */
	@Override
	public double perimeter() {
		rl.lock();
		try{
			int N = vertices.size();
			if(N==0 || N==1)
				return 0;
			double pSum = 0;
			int i;
			for(i=0; i<N; i++){
				Vector v1 = vertices.get(i);
				Vector v2 = vertices.get((i+1)%N);
				Vector vDiff = v2.subtract(v1);
				pSum += Math.sqrt(vDiff.scalarProduct(vDiff));
			}
			return pSum;	
		}finally{rl.unlock();}
	}

	@Override
	public double interiorAngle(int index) throws IllegalArgumentException {
		rl.lock();
		try{
			int N = vertices.size();
			if(index >= N || index < 0)
				throw new IllegalArgumentException();
			//The interior angle is 0 for polygons with 1 or 2-vertex.
			else if(N==1 || N==2)
				return 0;
			else{
				Vector vPrev = vertices.get(((index-1)+N)%N);
				Vector v	 = vertices.get(index);
				Vector vNext = vertices.get((index+1)%N);
				
				Vector v1 = vPrev.subtract(v);
				Vector v2 = vNext.subtract(v);
				
				double cosine = 
					v1.scalarProduct(v2)/(Math.sqrt(v1.scalarProduct(v1))*Math.sqrt(v2.scalarProduct(v2)));
				return Math.acos(cosine);
			}
		}finally{rl.unlock();}
	}
	
	@Override
	public Vector getVertex(int index) throws IllegalArgumentException {
		rl.lock();
		try{
			if (index >= vertices.size() || index < 0)
				throw new IllegalArgumentException();
			return vertices.get(index);
		}finally{rl.unlock();}
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
		wl.lock();
		if(index<0 || index>=vertices.size())
			throw new IllegalArgumentException();
		try{
			List<Vector> origin = new ArrayList<Vector>(vertices);
			vertices.set(index, vertex);
			
			boolean await = true;
			
			while(!isValid() && await==true){
				try {
//					System.out.println(Thread.currentThread().getName()+" is waiting! Condition is holding by "+(l.getWaitQueueLength(validMove)+1)+" threads!\n");
					await = validMove.await(Wating_Time, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
					//return false;
				}
			}
			
			if(!isValid()){
				vertices = origin;
				return false;
			}			
			validMove.signalAll();
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
