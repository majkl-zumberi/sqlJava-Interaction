package it.jac.sqljava.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.jac.sqljava.entity.Citta;

public class CityDao {

	// SINGLETON
	private static CityDao INSTANCE;
	
	private CityDao() {
		
	}
	
	public static CityDao getInstance() {
		
		if (INSTANCE == null) {
			INSTANCE = new CityDao();
		}
		return INSTANCE;
	}
	
	public boolean test() throws ClassNotFoundException, SQLException {
		
		Connection connection = getConnection();
		
		System.out.println("Connessione: " + connection);
		
		return true;
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/sqljava?serverTimezone=UTC","root","Root2ubrat");
		return connection;
	}
	
	public void createCitta(Citta citta) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO CITY (NAME, POSTALCODE, CREATION_TIME, CREATION_USER)");
		sql.append(" VALUES (?, ?, ?, ?)");
		
		Connection connection = null;
		PreparedStatement pstm = null;
		
		try {
			try {
				connection = getConnection();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pstm = connection.prepareStatement(sql.toString());
			
			pstm.setString(1, citta.getNome());
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
	
	public List<Citta> findAll() {

		List<Citta> result = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ID, NAME, POSTALCODE, CREATION_USER, CREATION_TIME, UPDATE_USER, UPDATE_TIME");
		sql.append(" FROM CITY");
				
		Connection connection = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			pstm = connection.prepareStatement(sql.toString());
			
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				
				Citta citta = new Citta();
				
				citta.setId(rs.getInt("ID"));
				citta.setNome(rs.getString("NAME"));				
				citta.setCap(rs.getString("POSTALCODE"));
				citta.setCreationUser(rs.getString("CREATION_USER"));
				citta.setCreationTime(rs.getDate("CREATION_TIME"));
				citta.setUpdateTime(rs.getDate("UPDATE_TIME"));
				citta.setUpdateUser(rs.getString("UPDATE_USER"));
				
				result.add(citta);
			}
			
		} catch(SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// non faccio nulla
				}
			}			
			
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
		
		return result;
	}

	public void deleteCitta(int idCitta) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("DELETE FROM CITY");
		sql.append(" WHERE ID = ?");
		
		Connection connection = null;
		PreparedStatement pstm = null;
		
		try {
			connection = getConnection();
			
			pstm = connection.prepareStatement(sql.toString());
			
			pstm.setInt(1, idCitta);
			
			pstm.execute();
			
		} catch(SQLException | ClassNotFoundException e) {
			
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

}
