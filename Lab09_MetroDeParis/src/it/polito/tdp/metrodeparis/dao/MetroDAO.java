package it.polito.tdp.metrodeparis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.Linea;

public class MetroDAO {

	public List<Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy, id_linea FROM fermata, connessione WHERE id_fermata=id_stazP ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"), new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")), rs.getInt("id_linea"));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}
	
	
	public boolean isCollegato(Fermata f1, Fermata f2){
		
		final String sql = "SELECT * FROM connessione WHERE id_stazP=? AND id_stazA=?";
		
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			
			st.setInt(1, f1.getIdFermata());
			st.setInt(2, f2.getIdFermata());
			
			ResultSet res = st.executeQuery() ;
			
			boolean result = false;
			
			
			while (res.next()){
				result = true;
			}
			
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	
	public List<Fermata> listAdiacenti(Fermata f) {
		final String sql = "SELECT * FROM connessione, fermata WHERE id_stazA = id_fermata AND id_stazP=?";
				

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, f.getIdFermata());
			
			ResultSet res = st.executeQuery() ;
			
			List<Fermata> list = new ArrayList<>() ;
			
			while(res.next()) {
				
				
				
				Fermata fermata = new Fermata(res.getInt("id_fermata"), res.getString("nome"), new LatLng(res.getDouble("coordX"), res.getDouble("coordY")), res.getInt("id_linea"));
				list.add(fermata);
			}
			
			res.close();
			conn.close();
			
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public Map<Integer, Linea> getListaLinee() {
		final String sql = "SELECT  * "+
                "FROM linea";
		Map<Integer,Linea>listaLinee=new HashMap<Integer,Linea>();            
		try {
				Connection conn = DBConnect.getInstance().getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
	
				ResultSet rs = st.executeQuery();
				
				while(rs.next()) {
					listaLinee.put(rs.getInt("id_linea"), new Linea(rs.getInt("id_linea"),rs.getString("nome"),rs.getDouble("velocita"),rs.getDouble("intervallo"),rs.getString("colore")));
				}

				st.close();
				conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return listaLinee;
	}
}
