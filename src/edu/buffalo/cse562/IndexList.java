package edu.buffalo.cse562;

import java.util.HashMap;
import java.util.HashSet;

public class IndexList {
	
	public static HashMap<String,HashSet<Integer>> primaryIndexList = new HashMap<String,HashSet<Integer>>();;
	public static HashMap<String,HashSet<Integer>> secondaryIndexList = new HashMap<String,HashSet<Integer>>();;
	HashSet<Integer> primIndexPos = null;
	HashSet<Integer> secIndexPos = null;
	
	public IndexList()
	{		
		//LINEITEM
		primIndexPos = new HashSet<Integer>();
		primIndexPos.add(0);
		primIndexPos.add(3);
		
		secIndexPos = new HashSet<Integer>();
		//secIndexPos.add(0);
		secIndexPos.add(10);
		
		primaryIndexList.put("LINEITEM", primIndexPos);
		secondaryIndexList.put("LINEITEM", secIndexPos);
		
		
		//ORDERS
		primIndexPos = new HashSet<Integer>();
		primIndexPos.add(0);
		
		secIndexPos = new HashSet<Integer>();
		secIndexPos.add(4);
		
		primaryIndexList.put("ORDERS", primIndexPos);
		secondaryIndexList.put("ORDERS", secIndexPos);

		
		/*//PART
		primIndexPos = new HashSet<Integer>();
		primIndexPos.add(0);
		
		secIndexPos = new HashSet<Integer>();
		secIndexPos.add(3);
		
		primaryIndexList.put("PART", primIndexPos);
		secondaryIndexList.put("PART", secIndexPos);
		
	
		//CUSTOMER
		primIndexPos = new HashSet<Integer>();
		primIndexPos.add(0);
		
		secIndexPos = new HashSet<Integer>();
		
		primaryIndexList.put("CUSTOMER", primIndexPos);
		secondaryIndexList.put("CUSTOMER", secIndexPos);

	
		//SUPPLIER
		primIndexPos = new HashSet<Integer>();
		primIndexPos.add(0);
		
		secIndexPos = new HashSet<Integer>();
		
		primaryIndexList.put("SUPPLIER", primIndexPos);
		secondaryIndexList.put("SUPPLIER", secIndexPos);


		//PARTSUPP
		primIndexPos = new HashSet<Integer>();
		primIndexPos.add(0);
		primIndexPos.add(1);
		
		secIndexPos = new HashSet<Integer>();
		secIndexPos.add(0);
		
		primaryIndexList.put("PARTSUPP", primIndexPos);
		secondaryIndexList.put("PARTSUPP", secIndexPos);


		//NATION
		primIndexPos = new HashSet<Integer>();
		primIndexPos.add(0);
		
		secIndexPos = new HashSet<Integer>();
		
		primaryIndexList.put("NATION", primIndexPos);
		secondaryIndexList.put("NATION", secIndexPos);
		
		
		//REGION
		primIndexPos = new HashSet<Integer>();
		primIndexPos.add(0);
		
		secIndexPos = new HashSet<Integer>();
		
		primaryIndexList.put("REGION", primIndexPos);
		secondaryIndexList.put("REGION", secIndexPos);
*/	}

}
