package edu.buffalo.cse562;

public class booleanDatum extends IDatum{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9146678469225039606L;
	
	Boolean bool;
	public booleanDatum(String s) {
		    this.bool = Boolean.valueOf(s); 
	}
	@Override
	public boolean equals(IDatum d) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int compareTo(IDatum rightexprDatum) {
		return 0;
	}
	@Override
	public Object getValue() {
		return this.bool;
	}
	@Override
	public IDatum plus(IDatum rightValue) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IDatum Difference(IDatum leftValue) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IDatum Multiply(IDatum rightValue) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Boolean.toString(bool);
	}

}
