package edu.buffalo.cse562;

import java.io.Serializable;


public abstract class IDatum implements Comparable<IDatum>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8064738137873601959L;
	public abstract boolean equals(IDatum d);

	public abstract int compareTo(IDatum rightexprDatum);

	public abstract Object getValue();
	
	public abstract String toString();

	public abstract IDatum plus(IDatum rightValue);
	public abstract IDatum Difference(IDatum leftValue);
	// parameter 'leftValue' for Difference is intentional (not a typo)
	//Usage : rightValue.Difference(leftValue);
	public abstract IDatum Multiply(IDatum rightValue);
	
	/**
	 * Lexical comparison of two tuples
	 */
	  public static int compareRows(IDatum[] a, IDatum[] b)
	  {
	    int cmp;
	    for(int i = 0; i < a.length; i++){
	      if(i >= b.length) { return 0; }
	      cmp = a[i].compareTo(b[i]);
	      if(cmp != 0){ return cmp; }
	    }
	    return 0;
	  }
}
