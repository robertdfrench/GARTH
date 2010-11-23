package com.korovasoft.ga.distributed;


/**
 * 
 * @author robertdfrench
 * Contains all the MySQL specific strings for the common datasource operations
 */
public class KSMySQLStrings {
	public static String getStatisticsUpdateStatement(KSDistGAConfig conf, String statsTable, String geneTable, String workerTable) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO " + conf.databaseTablePrefix + statsTable);
		sql.append(" (sample_Time, min_fitness, max_fitness, avg_fitness, num_inserts) VALUES (");
		sql.append("NOW(),");
		sql.append("(SELECT MIN(fitness) as min_fitness from " + conf.databaseTablePrefix + geneTable + " where fitness > -1),");
		sql.append("(SELECT MAX(fitness) as max_fitness from " + conf.databaseTablePrefix + geneTable + "),");
		sql.append("(SELECT AVG(fitness) as avg_fitness from " + conf.databaseTablePrefix + geneTable + " where fitness > -1),");
		sql.append("(SELECT (MAX(id) - " + conf.populationSize + ") as num_inserts from " + conf.databaseTablePrefix + geneTable + "))");
		/*
		sql.append("CREATE TABLE " + conf.databaseTablePrefix + tableName + "(");
		sql.append("sample_time DATETIME NOT NULL,");
		sql.append("min_fitness DOUBLE,");
		sql.append("max_fitness DOUBLE,");
		sql.append("avg_fitness DOUBLE,");
		sql.append("num_inserts INT)");
		 */
		return sql.toString();
	}
	
	/**
	 * This is how we read our statistics, which are maintained by the bootloader process
	 * @param conf
	 * @param tableName
	 * @return MySQL string to get latest statistics from stats table
	 */
	public static String getLatestStatisticsStatement(KSDistGAConfig conf, String tableName) {
		return "SELECT * FROM " + conf.databaseTablePrefix + tableName + " ORDER BY sample_time DESC LIMIT 1";
	}
	
	/**
	 * This will generate the MySQL command to insert a KSOrganism into the database
	 * @param conf
	 * @param tableName
	 * @param organism
	 * @return MySQL Organism insert string
	 */
	public static String getInsertOrganismStatement(KSDistGAConfig conf, String tableName, KSOrganism organism) {
		StringBuilder insertSQL = new StringBuilder();
		insertSQL.append("INSERT INTO " + conf.databaseTablePrefix + tableName + "(");
		for (int i = 0; i < conf.genomeLength; i++) {
			insertSQL.append(String.format("gene%d,", i));
		}
		insertSQL.append("fitness) VALUES (");
		for (int i = 0; i < conf.genomeLength; i++) {
			insertSQL.append(organism.genome[i] + ",");
		}
		insertSQL.append(organism.fitnessScore + ")");
		return insertSQL.toString();
	}
	
	/**
	 * This reads the first two organisms from the database. In order to
	 * thoroughly "check them out", you need to call getDeletePairStatement
	 * as well. Well, you need to execute the string it returns, I mean.
	 * @param conf
	 * @param tableName
	 * @return MySQL string for selecting first two organisms from the gene_pool
	 */
	public static String getSelectArrayStatement(KSDistGAConfig conf, String tableName) {
		return "SELECT * FROM " + conf.databaseTablePrefix + tableName + " ORDER BY id ASC LIMIT " + conf.checkoutSize;
	}
	
	/**
	 * Since we're focused on pair operations at the moment, this is the command to delete
	 * the first two organisms from the database. To be used immediately after a call that
	 * selects these two from the database, effectively "checking them out"
	 * @param conf
	 * @param tableName
	 * @return MySQL string for deleting first two organisms in the gene_pool
	 */
	public static String getDeleteArrayStatement(KSDistGAConfig conf, String tableName) {
		return "DELETE FROM " + conf.databaseTablePrefix + tableName + " ORDER BY id ASC LIMIT " + conf.checkoutSize;
	}
	
	/**
	 * Constructs a table with one DOUBLE column per gene, an id column, and a fitness column
	 * Table will be named as follows "tablePrefix + GENE_POOL_TABLE_NAME". Capiche?
	 * @param conf
	 * @return
	 */
	public static String getGenePoolCreateStatement(KSDistGAConfig conf, String tableName) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE " + conf.databaseTablePrefix + tableName + "(");
		for (int i = 0; i < conf.genomeLength; i++) {
			sql.append(String.format("gene%d DOUBLE NOT NULL DEFAULT 0.0,", i));
		}
		sql.append("fitness DOUBLE,"); 
		// fitness must be IMMEDIATELY after genes for cooperation with locking optimization
		// in KSDistGADatasource
		sql.append("id INT AUTO_INCREMENT PRIMARY KEY)");
		return sql.toString();
	}
	
	/**
	 * Stores statistics which will be calculated intermittently during the experiment
	 * @param conf
	 * @return
	 */
	public static String getStatisticsCreateStatement(KSDistGAConfig conf, String tableName) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE " + conf.databaseTablePrefix + tableName + "(");
		sql.append("sample_time DATETIME NOT NULL,");
		sql.append("min_fitness DOUBLE,");
		sql.append("max_fitness DOUBLE,");
		sql.append("avg_fitness DOUBLE,");
		sql.append("num_inserts INT)");
		return sql.toString();
	}
	
	/**
	 * Stores statistics which will be calculated intermittently during the experiment
	 * @param conf
	 * @return
	 */
	public static String getWorkerStatusCreateStatement(KSDistGAConfig conf, String tableName) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE " + conf.databaseTablePrefix + tableName + "(");
		sql.append("start_time DATETIME NOT NULL,");
		sql.append("os_pid INT NOT NULL,");
		sql.append("last_access DATETIME NOT NULL)");
		return sql.toString();
	}
	
	/**
	 * Constructs an appropriate DROP statement for the given table
	 * @param tableName
	 * @return
	 */
	public static String getDropTableStatement(KSDistGAConfig conf, String tableName) {
		return "DROP TABLE IF EXISTS " + conf.databaseTablePrefix + tableName;
	}
	
	/**
	 * This statement will attempt to acquire a lock for a SINGLE table
	 * @param conf
	 * @param tableName
	 * @return MySQL statement to lock tableName
	 */
	public static String getLockTableStatement(KSDistGAConfig conf, String tableName) {
		return "LOCK TABLES " + conf.databaseTablePrefix + tableName + " WRITE";
	}
	
	/**
	 * This statement will release ALL tables locks.
	 * MySQL doesn't seem to have a specific "unlock just these ones"
	 * @return MySQL table unlock statement
	 */
	public static String getUnlockTableStatement() {
		return "UNLOCK TABLES";
	}
	
	/**
	 * Formats a MySQL Specific Connection String
	 * @param conf
	 * @return
	 */
	public static String getConnectionString(KSDistGAConfig conf) {
		// Ex: jdbc:mysql://localhost:3306/distributed_ga
		return String.format("jdbc:mysql://%s:%d/%s",conf.databaseHost,conf.databasePort,conf.databaseName);
	}
	
	/**
	 * @return class name for MySQL JDBC driver
	 */
	public static String getDriverClassName() {
		return "com.mysql.jdbc.Driver";
	}
}
