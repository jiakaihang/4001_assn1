package ass01;

import static ass01.Vector.at;

import java.util.Collection;
import java.util.List;
import static java.util.Arrays.asList;

public class Test {
	
	Vector[] empty = {};
	
	static double tenRoot3 = 10 * Math.sqrt(3);
	static Vector[] triangle = {at(0,tenRoot3), at(-10,0), at(10,0)};
	static double triangleArea = 10 * tenRoot3;
	static double trianglePerimeter = 60;
	static double triangleAngle = Math.PI / 3;

	static Vector[] square1 = {at(0,0), at(10,0), at(10,10), at(0,10)};
	Vector[] square2 = {at(0,0), at(10,0), at(10,10), at(0,10)};
	Vector[] square3 = {at(5,5), at(15,5), at(15,15), at(5,15)};
	static double squareArea = 100;
	static double squarePerimeter = 40;
	static double squareAngle = Math.PI / 2;
	
	public static void display(Polygon poly, double area, double perimeter) {
		
		List<Vector> vs = poly.vertices();
		int n = vs.size();
		System.out.print(" Polygon: n = ");System.out.print(n);
		System.out.print(" area = "); System.out.print(poly.area());
		System.out.print(" perimeter = "); System.out.print(poly.perimeter());
		if (n < 10) {
			System.out.print(" vertices = ");
			System.out.print(vs);
		}
		System.out.println();

		System.out.print(" Expected:     ");
		System.out.print(" area = "); System.out.print(area);
		System.out.print(" perimeter = "); System.out.print(perimeter);
		System.out.println();
		System.out.println();
	}
	
	public static void unsafeTest(List<Vector> vertices){
		Polygon.Mutable poly = new UnsafePolygon(vertices);
		display(poly, squareArea, squarePerimeter);
//		System.out.println("interior angle = "+poly.interiorAngle(0));
//		System.out.println("Expected interior angle = " + triangleAngle);
		
		//test scale
		double factor = 1.00001d;
		
		System.out.println("The factor is: " + factor);
		
		double area = squareArea * factor * factor;
		double perimeter = squarePerimeter * factor;
		try{
			poly.scale(factor);
		}
		catch(IllegalArgumentException e){
			System.out.println("Illegal Argument Exception caught: " + e.getMessage());
		}
		display(poly, area, perimeter);
//		
		//test translate
		poly.translate(at(5,5));
		display(poly, area, perimeter);

//		//test setVertex
//		try{
//			poly.setVertex(0, at(9,9));
//		}catch(IllegalArgumentException e){
//			System.out.println("Illegal Argument Exception caught: " + e.getMessage());
//		}
//		display(poly, squareArea, squarePerimeter);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		unsafeTest(asList(square1));
	}

}
