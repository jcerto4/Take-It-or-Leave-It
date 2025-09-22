package gameoutput;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import player.Player;

public class GameData {
	
	protected Connection connection;
	protected Statement statement;
	protected ResultSet results;
	
	public GameData() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver is loaded!");
		}catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/tioli", 
					"root", 
					"root"
					);
			System.out.println("Connection Established!");
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		try {
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY
					);
			System.out.println("Statement Created!");
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	public void close() {
		try {
			connection.close();
			System.out.println("Database Closed!");
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	public void updateBank(Player player) {
		 String updateStatement = "UPDATE player SET bank = " + player.getBank() +
                 " WHERE player_id = '" + player.getId() + "'";
		try {
			statement.executeUpdate(updateStatement);
			System.out.println("Bank Updated!");
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	public void insertResults(Player player, int amountWon) {
		String insertStatement = "INSERT INTO game_results (game_id, player_id, hand_descr, amount_won, player_bank) " +
                "VALUES (DEFAULT, '" + player.getId() + "', '" +
                player.getHand().getHandDescr() + "', " + amountWon + ", " + player.getBank() + ")";
			try {
				statement.executeUpdate(insertStatement);
				System.out.println("Insertion Complete!");
			} catch (SQLException ex) {
				ex.printStackTrace();
}
	}
	public ResultSet getReportData(Player player) {
		String reportStatement = "SELECT * FROM game_results WHERE player_id "
				+ "= '" + player.getId() + "'";
		try {
			results = statement.executeQuery(reportStatement);
			System.out.println("Query Executed!");
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return results;
	}
}
