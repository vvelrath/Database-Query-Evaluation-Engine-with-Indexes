package edu.buffalo.cse562;
import java.io.*;
import net.sf.jsqlparser.schema.Column;
import jdbm.*;

public class IndexRowSerializer implements Serializer<IndexRow>{
	Column[] schema;
	String[] type;
	
	public IndexRowSerializer(Column[] schema) {
		this.schema = schema;
		type = new String[schema.length];
		
		for(int i=0; i<schema.length; i++) {
			if(Main.buildFlag==true)
				type[i] = TableDetailsVisitor.colDetails.get(schema[i].getColumnName());
			else
				type[i] = FromScanner.colDetails.get(schema[i].getColumnName());
		}
	}

	public IndexRow deserialize(SerializerInput in) throws java.io.IOException {
		IDatum[] row = new IDatum[schema.length];
		
		for(int i=0; i<type.length; i++) {
			switch(type[i]) {
			case "int": row[i] = new integerDatum(in.readUTF()); break;
			case "double": row[i] = new doubleDatum(in.readUTF()); break;
			case "boolean": row[i] = new booleanDatum(in.readUTF()); break;
			case "string": row[i] = new stringDatum(in.readUTF()); break;
			case "date": row[i] = new dateDatum(in.readUTF()); break;
			default: throw new IOException("Unhandled type: "+ type[i]);
			}
		}
		return new IndexRow(row);
	}
	
	public void serialize(SerializerOutput out, IndexRow row) throws java.io.IOException {
		for(int i=0; i<type.length; i++) {
			try {
				switch(type[i]) {
				case "int": out.writeUTF(row.data[i].toString()); break;
				case "double": out.writeUTF(row.data[i].toString()); break;
				case "boolean": out.writeUTF(row.data[i].toString()); break;
				case "string": out.writeUTF(row.data[i].toString()); break;
				case "date": out.writeUTF(row.data[i].toString());  break;
				default: throw new IOException("Unhandled type: "+ type[i]);
				}
			}
			catch(Exception e) {
				throw new IOException("Cast error while serializing",e);
			}
		}
	}
	
}
