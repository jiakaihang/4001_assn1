/**
 * 
 */
package ass01;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kaihang
 *
 */
public abstract class AbstractPolygon implements Polygon {
	
	private List<Vector> vertices;
	/**
	 * 
	 */
	public AbstractPolygon (List<Vector> vertices) {
		this.vertices = new ArrayList<Vector>(vertices);
		//check();	// check if the arguments are valid;
	}

	/**
	 * 
	 * 
	 */
//	private void check() throws IllegalArgumentException{
//		if(!isValid() || this.area() < 0 || this.perimeter() < 0)
//			throw new IllegalArgumentException();
//	}
	
	@Override
	public List<Vector> vertices() {
		return this.vertices;
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
		for(i = 0; i<N; i++){
			Vector v1 = vertices.get(i);
			Vector v2 = vertices.get((i+1)%N);
			vpSum += v1.vectorProduct(v2);
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
		for(i=0; i<N; i++){
			Vector v1 = vertices.get(i);
			Vector v2 = vertices.get((i+1)%N);
			Vector vDiff = v2.subtract(v1);
			pSum += Math.sqrt(vDiff.scalarProduct(vDiff));
		}
		return pSum;	
	}


	/* (non-Javadoc)
	 * @see ass01.Polygon#getVertex(int)
	 */
	@Override
	public Vector getVertex(int index) throws IllegalArgumentException {
		if (index >= vertices.size() || index < 0)
			throw new IllegalArgumentException();
		return this.vertices().get(index);
	}

	/* (non-Javadoc)
	 * @see ass01.Polygon#interiorAngle(int)
	 * 
	 */
	@Override
	public double interiorAngle(int index) throws IllegalArgumentException {
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
	}
	

	/**
	 * Validate the polygon by utilize the fact that a convex anti-clockwise
	 * ordered polygon has a POSITIVE value of cross product of all 
	 * adjacent vectors 
	 * 
	 */
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


	/* (non-Javadoc)
	 * @see ass01.Polygon#intersect(ass01.Polygon)
	 */
	@Override
	public Polygon intersect(Polygon that) {
		return null;
	}

}
