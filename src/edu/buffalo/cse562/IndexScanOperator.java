package edu.buffalo.cse562;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.io.File;
import java.io.IOException;

import jdbm.PrimaryStoreMap;
import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.SecondaryKeyExtractor;
import jdbm.SecondaryTreeMap;
import jdbm.helper.PrimaryStoreMapImpl;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

public class IndexScanOperator implements IOperator {
	IOperator input;
	Expression condition;
	Column[] schema;
	IDatum[] colValues = null;	
	File indexDir = null;
	RecordManager index = null;
	ArrayList<String> evaluatedRows = null;
	Iterator<String> eval_iterator = null;
	
	public IndexScanOperator(IOperator input,Column[] schema,Expression condition,File indexDir) {
		this.input = input;
		this.condition = condition;
		this.schema=schema;		
		
		this.indexDir = indexDir;
		
		try {
			String cond_str = condition.toString();
			
			index = RecordManagerFactory.createRecordManager(indexDir+"//myindex");
			new IndexList();
			String tableName = schema[0].getTable().getName().toUpperCase();
			String primaryKeyName = tableName.concat("_PRIMARY");
			
			//Primary Index
			FileReadOperator fileOp = (FileReadOperator) input;
			this.schema = fileOp.schema_original;

			Column[] priSchema = null;
			Column[] secSchema = new Column[1];
			String secKeyName = null;
			final int secKeyPos;
			String fromDate = null;
			String toDate = null;
	
			if(tableName.equals("LINEITEM"))
			{
				priSchema = new Column[2];
				priSchema[0]=fileOp.schema_original[0];
				priSchema[1]=fileOp.schema_original[3];
				secSchema[0]=fileOp.schema_original[10];
				secKeyName = "shipdate_SECONDARY";
				secKeyPos = 10;
				fromDate = cond_str.substring(27, 37);
				toDate = cond_str.substring(71, 81);
			}
			else
			{
				priSchema = new Column[1];
				priSchema[0]=fileOp.schema_original[0];
				secSchema[0]=fileOp.schema_original[4];
				secKeyName = "orderdate_SECONDARY";
				secKeyPos = 4;
				fromDate = cond_str.substring(26, 36);
				toDate = cond_str.substring(68, 78);
			}	
			
			PrimaryTreeMap<Integer, String> primaryIndex = index
					.treeMap(primaryKeyName);
		
			
			SecondaryTreeMap<String, Integer, String> secondaryIndex = 
					primaryIndex.secondaryTreeMap(secKeyName, 
                                    new SecondaryKeyExtractor<String, Integer, String>() {
                                            public String extractSecondaryKey(Integer key,String value) {
                                                    return value.split("\\|")[secKeyPos];
                                            }
                                    });
			
			
			evaluatedRows = new ArrayList<String>();

			Iterator<Iterable<Integer>> rows = secondaryIndex.subMap(fromDate, toDate).values().iterator();
			
			
			while(rows.hasNext())
			{		
				Iterator<Integer> ind = rows.next().iterator();
				while(ind.hasNext())
				{
					evaluatedRows.add(primaryIndex.get(ind.next()));
				}
			}
			
			if(tableName.equals("LINEITEM"))
			{
				Iterator<Integer> toDaterows = secondaryIndex.get(toDate).iterator();
				while(toDaterows.hasNext())
				{
					evaluatedRows.add(primaryIndex.get(toDaterows.next()));
				}
			}
			
			eval_iterator = evaluatedRows.iterator(); 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void resetStream() {
		input.resetStream();
	}
	
	@Override
	public IDatum[] readOneTuple() {
		
		colValues = null;		
		String colString = null;
		if (eval_iterator.hasNext()) {
			colString = eval_iterator.next(); 
		}
		
		if(colString!=null)
		{	
			String[] colArray = colString.split("\\|");
			
			colValues = new IDatum[colArray.length];
			
			for (int i = 0; i < colArray.length; i++) {
	
					String type = FromScanner.colDetails.get(schema[i].getColumnName());
					switch (type) {
					case "int":
						colValues[i] = new integerDatum(colArray[i]);
						break;
					case "boolean":
						colValues[i] = new booleanDatum(colArray[i]);
						break;
					case "date":
						colValues[i] = new dateDatum(colArray[i]);
						break;
					case "double":
						colValues[i] = new doubleDatum(colArray[i]);
						break;
					case "string":
						colValues[i] = new stringDatum(colArray[i]);
						break;
					}
				
			}
		}
		return colValues;
	}
	
	@Override
	public Column[] getSchema() {
		// TODO Auto-generated method stub
		return this.schema;
	}

	@Override
	public void setSchema(Column[] col) {
		// TODO Auto-generated method stub
		this.schema=col;
	}
	
}
