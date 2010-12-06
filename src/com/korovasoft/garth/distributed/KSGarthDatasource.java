/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.garth.distributed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.korovasoft.garth.KSOrganism;

//TODO: IF a SQL Exception is thrown after lockTable() is called,
// 		then we need to call unlockTable() in the handler before
//		returning control.
/**
 * @author robertdfrench
 * Acts as a wrapper around our underlying data store
 * TODO: Make a KSDistGADatasourceInterface so that we can
 * more easily swap out other RDBMS's or even something
 * more resilient like CouchDB, or maybe something quicker
 * like a shared data structure.
 */
public class KSGarthDatasource {
	private Connection dbh;
	private Statement lock_handle;
	private Statement work_handle;
	private KSGarthConfig conf;
	private final static String GENE_POOL_TABLE = "gene_pool";
	private final static String STATISTICS_TABLE = "stats";
	private final static String WORKER_STATUS_TABLE = "workers";

	/**
	 * Stores the config object and creates a new database handle
	 * based on the database settings in config
	 * @param conf
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public KSGarthDatasource(KSGarthConfig conf) throws SQLException {
		this.conf = conf;
		try {
			Class.forName(KSMySQLStrings.getDriverClassName());
		} catch (ClassNotFoundException e) {
			System.err.println("Could not load MySQL driver. Are you sure that mysql-connector-java-5.0.5-bin.jar or greater is installed?");
		}
		this.dbh = DriverManager.getConnection(KSMySQLStrings.getConnectionString(conf),conf.databaseUser, conf.databasePassword);
		this.lock_handle = dbh.createStatement();
		this.work_handle = dbh.createStatement();
	}
	
	/**
	 * Gets the poorest fitness score from the current population
	 * @return
	 * @throws SQLException
	 */
	public double getPoorestFitness() throws SQLException {
		double poorestScore = Double.POSITIVE_INFINITY;
		
		work_handle.execute(KSMySQLStrings.getLatestStatisticsStatement(conf, STATISTICS_TABLE));
		ResultSet rs = work_handle.getResultSet();
		if (rs.next()) {
			poorestScore = rs.getDouble("max_fitness");
		}
		
		return poorestScore;
	}
	
	/**
	 * updates the statistics table
	 * @throws SQLException 
	 */
	public void calculateAndLogStatistics() throws SQLException {
		work_handle.executeUpdate(KSMySQLStrings.getStatisticsUpdateStatement(conf, STATISTICS_TABLE, GENE_POOL_TABLE, WORKER_STATUS_TABLE));
	}
	
	
	/**
	 * Creates gene_pool table, statistics table, and worker registration table
	 * @throws SQLException
	 */
	public void setupTables() throws SQLException {
		ArrayList<String> workload = new ArrayList<String>();
		workload.add(KSMySQLStrings.getDropTableStatement(conf, GENE_POOL_TABLE));
		workload.add(KSMySQLStrings.getDropTableStatement(conf, STATISTICS_TABLE));
		workload.add(KSMySQLStrings.getDropTableStatement(conf, WORKER_STATUS_TABLE));
		workload.add(KSMySQLStrings.getGenePoolCreateStatement(conf, GENE_POOL_TABLE));
		workload.add(KSMySQLStrings.getStatisticsCreateStatement(conf, STATISTICS_TABLE));
		workload.add(KSMySQLStrings.getWorkerStatusCreateStatement(conf, WORKER_STATUS_TABLE));
		
		Statement sth = dbh.createStatement();
		for (String sql : workload) { // Nice collection iteration!
			sth.executeUpdate(sql);
		}
	}
	
	
	/**
	 * Checks a pair of organisms out of the datasource
	 * @return
	 * @throws SQLException
	 */
	public KSOrganism[] takeArray() throws SQLException {
		// Do all the allocation & initialization up front
		// so that we minimize the time spent with the gene
		// pool locked.
		KSOrganism[] checkoutArray = new KSOrganism[conf.checkoutSize];
		for (int i = 0; i < conf.checkoutSize; i++) {
			checkoutArray[i] = new KSOrganism(conf.genomeLength);
		}
		Statement sth = dbh.createStatement();
		String selectStatement = KSMySQLStrings.getSelectArrayStatement(conf, GENE_POOL_TABLE);
		String deleteStatement = KSMySQLStrings.getDeleteArrayStatement(conf, GENE_POOL_TABLE);
		ResultSet rs = null;
		int i = 0,j = 0;
		
		/*** TABLE IS LOCKED ***/
		lockTable(GENE_POOL_TABLE);
		sth.execute(selectStatement);
		rs = sth.getResultSet();
		// This is a for loop which has been optimized to reduce the amount of time
		// spent with the tables locked. This may be Knuthian evil.
		while(rs.next()) { 
			while( i < conf.genomeLength ) {
				checkoutArray[j].genome[i] = rs.getDouble(i + 1); // (i + 1) -> gene_i
				i++;
			}
			checkoutArray[j].fitnessScore = rs.getDouble(i + 1);
			j++; i = 0;
		}
		sth.executeUpdate(deleteStatement);
		unlockAllTables();
		/*** TABLES AIN'T LOCKED ***/
		
		
		return checkoutArray;
	}
	
	private void lockTable(String tableName) throws SQLException {
		lock_handle.executeUpdate(KSMySQLStrings.getLockTableStatement(conf, tableName));
	}
	
	private void unlockAllTables() throws SQLException {
		lock_handle.executeUpdate(KSMySQLStrings.getUnlockTableStatement());
	}
	
	public void putArray(KSOrganism[] organismArray) throws SQLException {
		String insertStatements[] = new String[organismArray.length];
		for (int i = 0; i < organismArray.length; i++) {
			insertStatements[i] = KSMySQLStrings.getInsertOrganismStatement(conf, GENE_POOL_TABLE, organismArray[i]);
		}
		lockTable(GENE_POOL_TABLE);
		for (int i = 0; i < organismArray.length; i++) {
			work_handle.executeUpdate(insertStatements[i].toString());
		}
		unlockAllTables();
	}
}
