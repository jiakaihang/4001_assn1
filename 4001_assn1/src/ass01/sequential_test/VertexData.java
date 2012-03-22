package ass01.sequential_test;

import ass01.Vector;
import static ass01.Vector.at;

public class VertexData {
	
	Vector[] empty = {};
	double emptyArea = 0;
	double emptyPerimeter = 0;
	
	Vector[] vector1 = {at(0,0)};
	Vector[] vector2 = {at(10,0)};
	
	Vector[] edge1 = {at(0,0), at(10,0)};
	Vector[] edge2 = {at(10,10), at(0,10)};
	double edgeArea = 0;
	double edgePerimeter = 20;
	
	double tenRoot3 = 10 * Math.sqrt(3);
	Vector[] triangle = {at(0,tenRoot3), at(-10,0), at(10,0)};
	double triangleArea = 10 * tenRoot3;
	double trianglePerimeter = 60;
	double triangleAngle = Math.PI / 3;
	
	Vector[] square1 = {at(0,0), at(10,0), at(10,10), at(0,10)};
	Vector[] square2 = {at(0,0), at(10,0), at(10,10), at(0,10)};
	Vector[] square3 = {at(5,5), at(15,5), at(15,15), at(5,15)};
	double squareArea = 100;
	double squarePerimeter = 40;
	double squareAngle = Math.PI / 2;
	
	int n = 1000;
	double nExterior = 0.002 * Math.PI;
	Vector[] nPolygon = new Vector[n];
	double nArea = 1000 * Math.sin(nExterior) / 2;
	double nPerimeter = 1000 * Math.sin(nExterior/2) * 2;
	double nAngle = Math.PI - nExterior;

	VertexData() {
		for (int i = 0; i < n; ++i) {
			double angle = i*nExterior;
			nPolygon[i] = at(Math.cos(angle), Math.sin(angle));
		}
	}

	Vector[] rotate(Vector[] vs, int positions) {
		assert vs != null;
		final int n = vs.length;
		Vector[] result = new Vector[n];
		if (n > 0) {
			final int shift = Math.abs (positions % n);
			System.arraycopy(vs, 0, result, n - shift, shift);
			System.arraycopy(vs, shift, result, 0, n - shift);
		}
		return result;
	}
	
	Vector[] add(Vector[] vs, Vector v) {
		assert vs != null && v != null;
		final int n = vs.length;
		Vector[] result = new Vector[n];
		for (int i = 0; i < n; ++i) {
			result[i] = vs[i].add(v);
		}
		return result;
	}
}
