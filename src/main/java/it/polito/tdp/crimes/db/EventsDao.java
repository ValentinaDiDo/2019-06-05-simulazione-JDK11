package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.crimes.model.DistrettiCrimini;
import it.polito.tdp.crimes.model.District;
import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public List<Integer> getYears(){
		String sql = "select distinct YEAR(reported_date) as y "
				+ "from events";
		List<Integer> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("y"));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public List<District> getDistretti(int anno){
		String sql = "SELECT district_id as id, AVG(geo_lon) as lon, AVG(geo_lat) as lat "
				+ "FROM events "
				+ "WHERE YEAR(reported_date) = ? "
				+ "GROUP BY district_id";
		List<District> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				int id = res.getInt("id");
				double lat = res.getDouble("lat");
				double lon = res.getDouble("lon");
				LatLng coord = new LatLng(lat, lon);
				
				District d = new District (id, coord);
				result.add(d);
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public List<DistrettiCrimini> getDistrettoMenoCrimini(int anno){
		String sql = "SELECT district_id AS distretto, COUNT(incident_id) AS crimini "
				+ "FROM EVENTS "
				+ "WHERE YEAR(reported_date)=? "
				+ "GROUP BY district_id";
		
		List<DistrettiCrimini> result = new ArrayList<>();
				try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				int id = res.getInt("distretto");
				int crimini = res.getInt("crimini");
				DistrettiCrimini d = new DistrettiCrimini(id, crimini);
				result.add(d);
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Event> getEventiGiorno(int anno, int mese, int giorno){
		
		String sql = "SELECT * "
				+ "FROM EVENTS "
				+ "WHERE YEAR(reported_date)= ? AND MONTH(reported_date)= ? AND DAY(reported_date)= ? ";
		List<Event> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			ResultSet res = st.executeQuery() ;
		
			while(res.next()) {
			
				result.add(new Event(res.getLong("incident_id"),
						res.getInt("offense_code"),
						res.getInt("offense_code_extension"), 
						res.getString("offense_type_id"), 
						res.getString("offense_category_id"),
						res.getTimestamp("reported_date").toLocalDateTime(),
						res.getString("incident_address"),
						res.getDouble("geo_lon"),
						res.getDouble("geo_lat"),
						res.getInt("district_id"),
						res.getInt("precinct_id"), 
						res.getString("neighborhood_id"),
						res.getInt("is_crime"),
						res.getInt("is_traffic")));
			
			}
		
			conn.close();
			return result ;

		}catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
	}
}
