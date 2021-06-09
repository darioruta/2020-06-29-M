package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Elemento;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer,Actor> actors){
		String sql = "SELECT * FROM actors";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				actors.put(actor.getId(), actor);
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public void  listAllMovies(Map<Integer, Movie> movies){
		String sql = "SELECT * FROM movies";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				movies.put(movie.getId(), movie);
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	
	public void listAllDirectors(Map<Integer, Director> idMap){
		String sql = "SELECT * FROM directors";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				idMap.put(director.getId(),director);
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Director> getVertici(int anno) {
		
		String sql = "SELECT d.* "
				+ "FROM directors d , movies_directors md, movies m "
				+ "WHERE d.id=md.director_id AND md.movie_id=m.id "
				+ "AND m.year=? ";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();


		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	
	public List<Adiacenza> getArchi(int anno, Map<Integer, Director> idMap){
		
		//mi servono le coppie di registi che hanno diretto lo stesso attore almeno una volta
		
		String sql = "SELECT md1.director_id AS d1, md2.director_id AS d2,  COUNT(Distinct r1.actor_id) AS peso "
				+ "FROM movies_directors md1, movies m, movies_directors md2, movies m2, roles r1, roles r2 "
				+ "WHERE md1.movie_id=m.id "
				+ "AND md2.movie_id=m2.id "
				+ "AND m.year= ? "
				+ "AND m2.year= ? "
				+ "AND r1.actor_id=r2.actor_id "
				+ "AND r1.movie_id=md1.movie_id "
				+ "AND r2.movie_id=md2.movie_id "
				+ "AND md1.director_id > md2.director_id "
				+ "GROUP BY md1.director_id, md2.director_id ";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Director d1 = idMap.get(res.getInt("d1"));
				Director d2 = idMap.get(res.getInt("d2"));
				int peso = res.getInt("peso");
				Adiacenza aTemp = new Adiacenza(d1, d2, peso);
				result.add(aTemp);
				
			}
		
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
}
