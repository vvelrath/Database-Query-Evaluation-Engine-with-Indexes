package edu.buffalo.cse562;


import java.io.File;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.statement.create.table.*;
import net.sf.jsqlparser.schema.*;

public class TableDetailsVisitor extends AbstractExpressionAndStatementVisitor{

	public static HashMap<String,String> colDetails = new HashMap<String,String>();
	public static Column[] schema=null;
	public IOperator source=null;
	public static HashMap<String,IOperator> tabToOperator = new HashMap<String,IOperator>();
	public File basePath;
	
	public TableDetailsVisitor(File basePath)
	{
		this.basePath = basePath;
	}
	
	@Override
	public void visit(CreateTable tableName) {
		
		List<?> cols=tableName.getColumnDefinitions();
		
		schema=new Column[cols.size()];
	
		for(int i=0;i<cols.size();i++)
		{
			ColumnDefinition colDef=(ColumnDefinition)cols.get(i);
			
			String type = colDef.getColDataType().toString();
			
			if(type.toLowerCase().startsWith("decimal"))
			{
				ColDataType dataType = new ColDataType();
				dataType.setDataType("double");
				colDef.setColDataType(dataType);
			}
					
			if(type.toLowerCase().startsWith("varchar")||type.toLowerCase().startsWith("char"))
			{
				ColDataType dataType = new ColDataType();
				dataType.setDataType("string");
				colDef.setColDataType(dataType);
			}
			
			colDetails.put(colDef.getColumnName(), colDef.getColDataType().toString().toLowerCase());
			
			// populate the schema array to contain column names 
			schema[i]=new Column(tableName.getTable(),colDef.getColumnName());
		}
		
		//create a class instead of the one below
		source=new FileReadOperator(new File(basePath,tableName.getTable().getName()+".dat"),colDetails, schema, null, schema.length);
		tabToOperator.put(tableName.getTable().getName(), source);
	}
}
