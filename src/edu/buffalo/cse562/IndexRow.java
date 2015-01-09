package edu.buffalo.cse562;

import java.io.*;

public class IndexRow implements Serializable, Comparable<IndexRow>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8412697394495869322L;
	public final IDatum[] data;

	public IndexRow(IDatum[] data) {
		this.data = data;
	}

	public IDatum[] getValue() {
		return this.data;
	}

	@Override
	public int compareTo(IndexRow other) {
		return IDatum.compareRows(data, other.data);

	}
}
