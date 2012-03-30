package ass01;

import java.util.ArrayList;
import java.util.Collection;

public class MyList<Vector> extends ArrayList<Vector> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyList(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public MyList(Collection<? extends Vector> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector set(int index, Vector v){
		throw new UnsupportedOperationException();
	}
}
