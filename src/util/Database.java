package util;
/**
 * @version 1
 * @author Liwenhan Xie (1600017744)
 * @since 2019-May-24
 */
import java.sql.*;

public class Database {
	private int index;
	private Connection conn;
	public Database() {
		connect();
	}
	/**
	 * Establish connection to database.
	 * @return Connection
	 */
	public Connection connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:data/dict.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * Query the word with its spelling.
	 * @param word query word
	 * @return Word object matched
	 * @throws SQLException 
	 */
	public Word queryWord(String word) throws SQLException {
		Statement statement = conn.createStatement();
		String query = "SELECT * FROM word WHERE word = '"+word+"'";
		ResultSet results = statement.executeQuery(query);
		while(results.next()) {
			String phone = results.getString("prounce_uk");
			String mean = results.getString("trans");
			return new Word(word, phone, mean);
		}
		return new Word(word, "", "");
	}
}
