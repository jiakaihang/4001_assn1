package ass01;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import ass01.Polygon.Mutable;

public class MutexPolygon extends UnsafePolygon {

	private final ReentrantLock l =  new ReentrantLock();
	private final Condition validMove = l.newCondition();
	private static long Wating_Time = 500;
	
	public MutexPolygon(List<Vector> vertices) {
		super(vertices);
	}
	
	@Override
	public void scale(double factor) throws IllegalArgumentException {
		l.lock();
		try{
			for(int i=0; i<vertices.size(); i++)
				vertices.set(i, vertices.get(i).scale(factor));
		}
		finally{
			l.unlock();
		}
	}

	@Override
	synchronized public void translate(Vector shift){
		l.lock();
		try{
		for(int i=0; i<vertices.size(); i++)
			vertices.set(i, vertices.get(i).add(shift));
		}
		finally{
			l.unlock();
		}
	}

	/**
	 * Make a copy of the original vertices and do setVertex() 
	 * on this.vertices
	 * if isValid() then return true
	 * if !isValid() then change the vertices back to the original vertices
	 * and return false.
	 * @throws InterruptedException 
	 */
	@Override
	public boolean setVertex(int index, Vector vertex)
			throws IllegalArgumentException {
		if(index<0 || index>=vertices.size())
			throw new IllegalArgumentException();

		l.lock();
		try{
			List<Vector> origin = new ArrayList<Vector>(vertices);
			vertices.set(index, vertex);

			long start = System.currentTimeMillis();
			while(!isValid(vertices) && Wating_Time > 0){
				validMove.await(Wating_Time, TimeUnit.MILLISECONDS);
				if((System.currentTimeMillis()-start) == Wating_Time)
					Wating_Time=0;
			}

			System.out.println("finishes after : "+(System.currentTimeMillis()-start)+"ms");
			if(!isValid(vertices)){
				System.out.println("Time is up, returning FALSE!!!");
				vertices = origin;
				return false;
			}

			validMove.signalAll();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{l.unlock();}
		System.out.println("RETURNING HERE!!!!!!!!!");
		return true;
	}
	
	@Override
	public boolean isValid() {
		l.lock();
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
		}finally{l.unlock();}
	}

	@Override
	public Mutable copy() {
		return new MutexPolygon(vertices);
	}

}
