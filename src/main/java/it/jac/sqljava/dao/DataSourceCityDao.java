package it.jac.sqljava.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import it.jac.sqljava.entity.Citta;

public class DataSourceCityDao {

	private static DataSourceCityDao instance;
	
	private ComboPooledDataSource dataSource;
	
	private DataSourceCityDao() {
		
	}
	
	public static DataSourceCityDao getInstance() {
		
		if (instance == null) {
			instance = new DataSourceCityDao();
			instance.initDataSource();
		}
		return instance;
	}
	
	private void initDataSource() {
		
		dataSource = new ComboPooledDataSource();
		dataSource.setJdbcUrl("jdbc:mysql://localhost/sqljava?serverTimezone=UTC");
		dataSource.setUser("root");
		dataSource.setPassword("mysql");
		
		// Optional Settings
		dataSource.setInitialPoolSize(5);
		dataSource.setMinPoolSize(5);
		dataSource.setAcquireIncrement(5);
		dataSource.setMaxPoolSize(20);
		dataSource.setMaxStatements(100);
		try {
			Connection connection = dataSource.getConnection();
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	public void createCitta(Citta citta) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO CITY (NAME, POSTALCODE, CREATION_TIME, CREATION_USER)");
		sql.append(" VALUES (?, ?, ?, ?)");
		
		Connection connection = null;
		PreparedStatement pstm = null;
		
		try {
			connection = dataSource.getConnection();
			
			pstm = connection.prepareStatement(sql.toString());
			
			pstm.setString(1, StringUtils.abbreviate(citta.getNome(), 45));
			pstm.setString(2, citta.getCap());
			pstm.setTimestamp(3, new java.sql.Timestamp(citta.getCreationTime().getTime()));
			pstm.setString(4, citta.getCreationUser());
			
			pstm.execute();
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e) {
					// non faccio nulla
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// non faccio nulla
				}
			}
		}
	}

	public void createCitta(List<Citta> list) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO CITY (NAME, POSTALCODE, CREATION_TIME, CREATION_USER)");
		sql.append(" VALUES (?, ?, ?, ?)");
		
		Connection connection = null;
		PreparedStatement pstm = null;
		boolean commit = false;
		
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			
			for(Citta item : list) {
				
				try {
					pstm = connection.prepareStatement(sql.toString());
					pstm.setString(1, StringUtils.abbreviate(item.getNome(), 45));
					pstm.setString(2, item.getCap());
					pstm.setTimestamp(3, new java.sql.Timestamp(item.getCreationTime().getTime()));
					pstm.setString(4, item.getCreationUser());
					
					pstm.execute();
					
				} finally {					
					if (pstm != null) {
						try {
							pstm.close();
						} catch (SQLException e) {
							// non faccio nulla
						}
					}
				}
			}
			
			commit = true;
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (commit) {
				try {
					connection.commit();
				} catch (SQLException e) {
					// non faccio nulla
				}
			} else {
				try {
					connection.rollback();
				} catch (SQLException e) {
					// non faccio nulla
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// non faccio nulla
				}
			}
		}
	}

}
