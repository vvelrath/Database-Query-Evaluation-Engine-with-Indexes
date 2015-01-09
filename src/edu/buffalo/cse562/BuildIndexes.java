package edu.buffalo.cse562;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jdbm.*;
import net.sf.jsqlparser.schema.*;

public class BuildIndexes {
	/**
	 * @param args
	 */
	File indexDir = null;
	public static int rowno = 0;
	public BuildIndexes(File indexDir)
	{
		this.indexDir = indexDir;
		new IndexList();
	}
	
	public void create(){
		
		try 
		{
		RecordManager index = RecordManagerFactory.createRecordManager(indexDir+"//myindex");
		
		Set<String> tableNames = IndexList.primaryIndexList.keySet();
		Iterator<String> tableName_iter = tableNames.iterator();
		
		HashMap<String,HashSet<Integer>> primaryIndexList = IndexList.primaryIndexList;
		HashMap<String,HashSet<Integer>> secondaryIndexList = IndexList.secondaryIndexList;
		HashMap<String,IOperator> tabToOperator = TableDetailsVisitor.tabToOperator;
		
		while(tableName_iter.hasNext())
		{
			String tableName = tableName_iter.next();
			String primaryKeyName = tableName.concat("_PRIMARY");
			IOperator operator = tabToOperator.get(tableName);
			
			//Schema
			Column[] full_schema = operator.getSchema();
			
			//Primary Key Schema
			HashSet<Integer> primaryPos = primaryIndexList.get(tableName);
			Column[] primarySchema = new Column[primaryPos.size()];
			Iterator<Integer> primaryPos_iter = primaryPos.iterator();
			for(int i=0;i<primaryPos.size();i++)
			{
				primarySchema[i] = full_schema[primaryPos_iter.next()];
			}	
			
			//Primary Index
			PrimaryTreeMap<Integer,String> primaryIndex = index
					.treeMap(primaryKeyName);
			
			
			//Secondary Index
			HashSet<Integer> secondaryPos = secondaryIndexList.get(tableName);
			Iterator<Integer> secondaryPos_iter = secondaryPos.iterator();
			
			for(int i=0;i<secondaryPos.size();i++)
			{
				Column[] secondarySchema = new Column[1];
				final int SecKeyPosition = secondaryPos_iter.next();
				secondarySchema[0] = full_schema[SecKeyPosition];
				String secondaryKeyName = secondarySchema[0].getColumnName().concat("_SECONDARY");
				
				SecondaryTreeMap<String, Integer, String> secondaryIndex = 
						primaryIndex.secondaryTreeMap(secondaryKeyName, 
	                                    new SecondaryKeyExtractor<String, Integer, String>() {
	                                            public String extractSecondaryKey(Integer key,String value) {
	                                                    return value.split("\\|")[SecKeyPosition];
	                                            }
	                                    });
				
			}
			
			int i = 0;
			String value = null;
			FileReadOperator fileOp = (FileReadOperator) operator;
			while((value = fileOp.readString())!=null) {
				rowno++;
				Integer rowint = rowno;
				primaryIndex.put(rowint,value);
				i++;
				if(i>1000) { 
					i = 0;
					index.commit();
					index.clearCache();
				} 
			}
			index.commit();
		}
		index.commit();
		index.close();

		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
		}


	}
/*
	private IDatum[] getPrimaryKey(IDatum[] value, HashSet<Integer> primaryPos) {
		// TODO Auto-generated method stub
		IDatum[] primaryKey = new IDatum[primaryPos.size()];
		Iterator<Integer> primarykey_pos = primaryPos.iterator();
		int i=0;
		while(primarykey_pos.hasNext())
		{
			primaryKey[i++] = value[primarykey_pos.next()];
		}	
		return primaryKey;
	}*/ 
}
