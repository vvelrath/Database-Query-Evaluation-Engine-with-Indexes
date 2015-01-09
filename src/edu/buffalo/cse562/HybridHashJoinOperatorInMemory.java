package edu.buffalo.cse562;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

public class HybridHashJoinOperatorInMemory implements IOperator {
	IOperator left;
	IOperator right;
	IDatum[] leftValues = null;
	IDatum[] rightValues = null;
	Expression onExpression=null;
	Column[] schema;
	
	ArrayList<IDatum[]> leftList = null;
	ArrayList<IDatum[]> rightList = null;
	Iterator<IDatum[]> leftIterator = null;
	Iterator<IDatum[]> rightIterator = null;
	Iterator<String> keys_iterator = null;
	HashMap<String, ArrayList<IDatum[]>> leftHashMap = null;
	HashMap<String, ArrayList<IDatum[]>> rightHashMap = null;
	
	public static int i=0;
	
	public HybridHashJoinOperatorInMemory(IOperator left, IOperator right,Expression onExpression, Column[] right_schema) {
		this.left = left;
		this.right = right;
		this.onExpression=onExpression;
		schema =null;
			
		ArrayList<Column> schlst = new ArrayList<Column>();
		Column[] left_schema = left.getSchema();
		for(int i=0;i<left_schema.length;i++)
		{
			schlst.add(left_schema[i]);
		}
		for(int i=0;i<right_schema.length;i++)
		{
			schlst.add(right_schema[i]);
		}
		Column[] schema_loc = new Column[schlst.size()];
		schema_loc = schlst.toArray(schema_loc);
		this.setSchema(schema_loc);
		
		i=i+1;//For naming the files
		
		leftValues = left.readOneTuple();
		
		BinaryExpression bex = (BinaryExpression)onExpression;
		Column leftColumn = (Column) bex.getLeftExpression();
		Column rightColumn = (Column) bex.getRightExpression();
		StatementEvaluator evaluator=new StatementEvaluator(left_schema,leftValues);
		rightColumn.accept(evaluator);
		
		if(evaluator.getColValue()!=null)
		{
			leftColumn = (Column) bex.getRightExpression();
			rightColumn = (Column) bex.getLeftExpression();
		}
		
		leftHashMap = new HashMap<String, ArrayList<IDatum[]>>();
		
		do {
			
			evaluator=new StatementEvaluator(left_schema,leftValues);
			
			leftColumn.accept(evaluator);
			String leftKey = evaluator.getColValue().getValue().toString();
			
			ArrayList<IDatum[]> leftList = null;
			if(leftHashMap.containsKey(leftKey))
			{
				leftList = leftHashMap.get(leftKey);
			}
			else
			{
				leftList = new ArrayList<IDatum[]>();
			}
			
			leftList.add(leftValues);
			leftHashMap.put(leftKey, leftList);
					
		} while ((leftValues = left.readOneTuple())!=null);
		
		
/*		
		if(i>=2)
		{
			int j= i-1;
			File[] hash_files = Main.swapDir.listFiles();
			for(File used_file:hash_files)
			{
				if(used_file.getName().startsWith("HybridHash_right_"+j)||used_file.getName().startsWith("HybridHash_left_"+j))
				{
					used_file.delete();
				}	
			}	
		}*/	
		
			
		rightValues = right.readOneTuple();

		rightHashMap = new HashMap<String, ArrayList<IDatum[]>>();
		
		do {
			
			evaluator=new StatementEvaluator(right_schema,rightValues);
			rightColumn.accept(evaluator);
			
			String rightKey = evaluator.getColValue().getValue().toString();

			if(leftHashMap.containsKey(rightKey))
			{ 
				ArrayList<IDatum[]> rightList = null;
				if(rightHashMap.containsKey(rightKey))
				{
					rightList = rightHashMap.get(rightKey);
				}
				else
				{
					rightList = new ArrayList<IDatum[]>();
				}
				
				rightList.add(rightValues);
				rightHashMap.put(rightKey, rightList);
			}
			
			
		} while ((rightValues = right.readOneTuple())!=null);
	
		
		
		Set<String> keys = rightHashMap.keySet();
		keys_iterator = keys.iterator();
		
		
		if(keys_iterator.hasNext())
		{	
			String key = keys_iterator.next();
			leftList = leftHashMap.get(key);
			rightList = rightHashMap.get(key);
			leftIterator = leftList.iterator();
			rightIterator = rightList.iterator();
			
			if(leftIterator.hasNext())
				leftValues = leftIterator.next();
		}
	}

	@Override
	public void resetStream() {
	}

	@Override
	public IDatum[] readOneTuple() {
		IDatum[] colValues=null;
	do{
		if(leftList==null || rightList == null)
		{
			return null;
		} 
		else if(rightIterator.hasNext())
		{
			rightValues = rightIterator.next();
		}
		else if(leftIterator.hasNext())
		{
			leftValues = leftIterator.next();
			rightIterator = rightList.iterator();
			rightValues = rightIterator.next();
		}
		else
		{	
			if(keys_iterator.hasNext())
			{
				String key = keys_iterator.next();
				leftList = leftHashMap.get(key);
				rightList = rightHashMap.get(key);
				leftIterator = leftList.iterator();
				rightIterator = rightList.iterator();
				
				if(leftIterator.hasNext())
					leftValues = leftIterator.next();
			
				if(rightIterator.hasNext())
					rightValues = rightIterator.next();

			}
			else
			{
				leftList = null;
				rightList = null;
				return null;
			}
			
		}
		
		
		ArrayList<IDatum> commonList = new ArrayList<IDatum>();
		
		for(int i=0;i<leftValues.length;i++)
		{
			commonList.add(leftValues[i]);	
		}
		
		for(int i=0;i<rightValues.length;i++)
		{
			commonList.add(rightValues[i]);
		}
		
		colValues = new IDatum[commonList.size()];
		colValues = commonList.toArray(colValues);		
		
	} while(colValues==null);
		
		return colValues;
	}

	@Override
	public Column[] getSchema() {
		// TODO Auto-generated method stub
		//return left.getSchema();
		return this.schema;
	}

	@Override
	public void setSchema(Column[] col) {
		// TODO Auto-generated method stub
		//left.setSchema(col);
		this.schema = col;
	}
	

}
