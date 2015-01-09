Query Evaluation Engine using Indexes 
==============================

### Team members

Vivekanandh Vel Rathinam (vvelrath@buffalo.edu), Amitha Narasimha Murthy (amithana@buffalo.edu), 
Neeti Narayan (neetinar@buffalo.edu)

### Description

This project is an improved version of [Database Engine Query Evaluator for Big Data](https://github.com/vvelrath/Database-Query-Evalution-Engine-for-Big-Data) which uses indexes to improve performance

In this project, there is a 5 minute pre-computation phase with which we can improve the system's performance. Suggested uses for this additional time include building indexes as well as gathering table statistics. To help building the indexes, CREATE TABLE statements defining the schema are extended with PRIMARY KEY and UNIQUE entries.

### Indexes

An open-source library is included to make the task easier. In this case, rather than building an on-disk indexing system (a number of these are available), a simple-in-memory indexing system will be provided. For this project we will be linking against the JDBM2 open-source Key/Value store                              (https://code.google.com/p/jdbm2/)

This library provides both on-disk HashMap and TreeMap interfaces, supporting both clustered (Primary) and unclustered (Secondary) indexes, as well as extremely limited support for transactional access (one transaction at a time). A short example is available on the main page, and additional documentation is available through the project's javadoc. Particularly relevant classes include RecordManager, Primary{,Hash,Tree}Map, and Secondary{Hash,Tree}Map.

http://jdbm2.googlecode.com/svn/trunk/javadoc/jdbm/package-summary.html

### Program Execution

This checkpoint introduces two new command-line flags:

	• --index index_directory: Use index_directory for to store any persistent files you create. The same directory will be used for the duration of testing your submission. Unlike --swap, the directory provided will not be cleared in between queries.
	• --build: Queries are not processed. Instead it uses the time to build indexes, gather statistics, or do any other relevant pre-processing.
	
Your code will be evaluated in two stages.

1) First, before any queries are run, the code will be invoked with a file containing the database schema and the --index and --build flags. During this phase the system does not process any queries and ignores any SELECT statements in files that it receives. There is no output produced during this phase. 

	java -Xmx1024m -cp build:jsqlparser.jar:jdbm2.jar edu.buffalo.cse562.Main --build --data /tmp/data --index /tmp/index tpch-schema.sql
	
2) During the second stage, the --index flag is given, and it is expected to process queries as normal. As in project 2, the evaluation is done on a 100 MB TPC-H dataset, and Java will be configured with a 1GB heap limit.

	java -Xmx1024m -cp build:jsqlparser.jar:jdbm2.jar edu.buffalo.cse562.Main --data /tmp/data --index /tmp/index tpch-schema.sql sql-file1.sql sql-file2.sql ...

This example uses the following directories and files

/tmp/data: Table data stored in '|' separated files. As before, table names match the names provided in the matching CREATE TABLE with the .dat suffix.
tpch-schema.sql: A file containing the CREATE TABLE statements for all tables in the TPC-H schema.