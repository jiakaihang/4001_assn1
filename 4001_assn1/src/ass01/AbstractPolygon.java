/**
 * 
 */
package ass01;

import java.util.List;

/**
 * @author Kaihang
 *
 */
public abstract class AbstractPolygon implements Polygon {

	final private List<Vector> vertices;
	final private double area;
	final private double perimeter;
	/**
	 * 
	 */
	public AbstractPolygon (List<Vector> vertices) {
		this.vertices = vertices;
		this.area = getArea();
		this.perimeter = getPerimeter();
		
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
	private double getArea(){
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
	private double getPerimeter(){
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


	/* (non-Javadoc)
	 * @see ass01.Polygon#vertices()
	 */
	@Override
	public List<Vector> vertices() {
		return vertices;
	}
	
	/* (non-Javadoc)
	 * @see ass01.Polygon#area()
	 */
	@Override
	public double area() {
		return area;
	}

	/* (non-Javadoc)
	 * @see ass01.Polygon#perimeter()
	 */
	@Override
	public double perimeter() {
		return perimeter;
	}

	/* (non-Javadoc)
	 * @see ass01.Polygon#getVertex(int)
	 */
	@Override
	public Vector getVertex(int index) throws IllegalArgumentException {
		if (index >= vertices.size() || index < 0)
			throw new IllegalArgumentException();
		return vertices.get(index);
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
			if(area!=0 && perimeter!=0)
				return false;
		}
		else if(N==2){
			if(area!=0 || perimeter!=getPerimeter())
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
	public boolean isEquivalent(Polygon that) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ass01.Polygon#freeze()
	 */
	@Override
	public Polygon freeze() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ass01.Polygon#intersect(ass01.Polygon)
	 */
	@Override
	public Polygon intersect(Polygon that) {
		return null;
	}




}
