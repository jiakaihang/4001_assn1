package ass01;

import java.util.ArrayList;
import java.util.List;

public class UnsafePolygon implements Polygon.Mutable{

	private List<Vector> vertices;
	
	public UnsafePolygon(List<Vector> vertices) {
		//super(vertices);
		this.vertices = vertices;
	}
	
	@Override
	public List<Vector> vertices() {
		return vertices;
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
		int N = vertices.size();
		if(N==0 || N==1 || N==2)
			return 0;
		double vpSum = 0;
		int i;
		int j = N-1;
		for(i = 0; i<N; i++){
			Vector v1 = vertices.get(j);
			Vector v2 = vertices.get(i);
			vpSum += v1.vectorProduct(v2);
			j=i;
		}
		return (0.5*vpSum);
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
		int N = vertices.size();
		if(N==0 || N==1)
			return 0;
		double pSum = 0;
		int i;
		int j = N-1;
		for(i=0; i<N; i++){
			Vector v1 = vertices.get(j);
			Vector v2 = vertices.get(i);
			Vector vDiff = v2.subtract(v1);
			pSum += Math.sqrt(vDiff.scalarProduct(vDiff));
			j = i;
		}
		return pSum;	
	}

	@Override
	public Vector getVertex(int index) throws IllegalArgumentException {
		if (index >= vertices.size() || index < 0)
			throw new IllegalArgumentException();
		return vertices.get(index);
	}

	@Override
	public double interiorAngle(int index) throws IllegalArgumentException {
		int N = vertices.size();
		if(index >= N || index < 0)
			throw new IllegalArgumentException();
		//The interior angle is 0 for polygons with 1 or 2-vertex.
		else if(N==1 || N==2)
			return 0;
		else{
			Vector vPrev = vertices.get(index-1);
			Vector v	 = vertices.get(index);
			Vector vNext = vertices.get(index+1);
			
			Vector v1 = vPrev.subtract(v);
			Vector v2 = vNext.subtract(v);
			
			double cosine = 
				v1.scalarProduct(v2)/(Math.sqrt(v1.scalarProduct(v1))*Math.sqrt(v2.scalarProduct(v2)));
			return Math.acos(cosine);
		}
	}

	@Override
	public boolean isValid() {
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
			int i;
			int j = N-1;
			int k = N-2;
			//vNext = v(i+1) vCurr = v(i) vPrev = v(i-1)
			for(i=0; i<N; i++){
				Vector vNext = vertices.get(i);
				Vector vCurr = vertices.get(j);
				Vector vPrev = vertices.get(k);
				Vector v1 = vCurr.subtract(vPrev);
				Vector v2 = vNext.subtract(vCurr);
				double crossProduct = v1.vectorProduct(v2);
				
				if(crossProduct <= 0)
					return false;
				
				k=j;
				j=i;
			}
		}
		return true;
	}

	@Override
	public Polygon intersect(Polygon that) {
		return null;
	}

	@Override
	public boolean isEquivalent(Polygon that) {
		int N1 = this.vertices().size();
		int N2 = that.vertices().size();
		
		if(N1 != N2)
			return false;				//not equivalent if the sizes are different
		else if(N1 == 0 && N2 == 0)		//0-vertex polygon
			return true;
		
		else{
			int i = 0;
			int j = 0;
			// find the first equivalent vertex
			while(j<N2){
				if(this.vertices().get(i).equals(that.vertices().get(j)))
					break;
				else
					j++;
			}
			if(j == N2)			//went through the list, no match vertex found.
				return false;
			else{
				i++;
				j = (j+1)%N2;
				while(i<N1){
					if(!this.vertices().get(i).equals(that.vertices().get(j)))
						return false;
					i++;
					j = (j+1)%N2;
				}
			}
			return true;
		}
	}

	@Override
	public Mutable copy() {
		return new UnsafePolygon(this.vertices());
	}

	@Override
	public Mutable makeMutable() {
		return this;
	}

	@Override
	public Polygon freeze() {
		return new ImmutablePolygon(this.vertices());
	}

	@Override
	public void scale(double factor) throws IllegalArgumentException {
		for(Vector v: this.vertices())
			v.scale(factor);
	}

	@Override
	public void translate(Vector shift) {
		for(Vector v: this.vertices())
			v.add(shift);
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
		List<Vector> origin = new ArrayList<Vector>(this.vertices());
		this.vertices.set(index, vertex);
		
		if(!isValid()){
			this.vertices = origin;
			return false;
		}			
		return true;
	}
	
}