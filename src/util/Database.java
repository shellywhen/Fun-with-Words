package util;

/**
 * @version 1
 * @author Liwenhan Xie (1600017744)
 * @since 2019-May-24
 */
import java.sql.*;
import java.util.ArrayList;

public class Database {
	private int index = 0; // index of arrayList
	public final static int MAX_LENGTH = 100;
	private int end = 0; // top of the next word
	private String user = "default"; // user name of current client
	private Connection conn; // connection to database
	private ArrayList<Word> wordList = new ArrayList<Word>(); // current word list
	private Statement statement;

	/**
	 * Default user log in.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Database() throws ClassNotFoundException, SQLException {
		connect();
		retrieveWord();
	}

	/**
	 * Log in with user name and password.
	 * 
	 * @param user
	 * @param password
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws LoginException         nonexistent user name or wrong password
	 */
	public Database(String user, String password) throws ClassNotFoundException, SQLException, LoginException {
		connect();
		retrieveWord();
		this.statement = this.conn.createStatement();
		String query = "SELECT password FROM user WHERE name=\"" + user + "\";";
		ResultSet pw = this.statement.executeQuery(query);
		if (pw.next()) {
			if (pw.getString("password").equals(Util.md5(password))) {
				this.user = user;
			} else {
				throw new LoginException("Wrong Password");
			}
		} else
			throw new LoginException("Nonexistent Username");
		pw.close();
		this.statement.close();
	}

	/**
	 * Establish connection to the database.
	 * @return Connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Connection connect() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:data/dict.db");
		conn.setAutoCommit(true); // commit transaction automatically
		return conn;
	}

	/**
	 * Fill the wordList with pages.
	 */
	public void retrieveWord() {
		int count = 0;
		try {
			this.statement = conn.createStatement();
			String query = "SELECT * FROM word ORDER BY word LIMIT " + Database.MAX_LENGTH + " OFFSET " + this.end + ";";
			ResultSet results = statement.executeQuery(query);
			while (results.next() && count < Database.MAX_LENGTH) {
				String spell = results.getString("word");
				String phone = "";
				try{
					phone = results.getString("prounce_uk");
				}catch(SQLException e) {
					e.printStackTrace();
				}
				String meaning = results.getString("trans");
				wordList.add(new Word(spell, phone, meaning));
				count++;
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.end += count;
		}
	}

	/**
	 * Insert a new user profile into the database.
	 * 
	 * @param name     user name
	 * @param password typed-in password
	 * @return flag, true for success
	 * @throws LoginException
	 */
	public boolean createUser(String name, String password) throws LoginException {
		ResultSet result;
		try {
			this.statement = conn.createStatement();
			String query = "SELECT count(*) FROM user WHERE name=\"" + name + "\";";
			result = this.statement.executeQuery(query);
			if (result.getInt(1) > 0) {
				// validate the user name
				throw new LoginException("Already Exist Username ");
			} else {
				// insert a new user into sheet "user"
				String insert = "INSERT INTO user (name, password, register_date)" + "values(?,?,?);";
				PreparedStatement pstmt = conn.prepareStatement(insert);
				pstmt.setString(1, name);
				pstmt.setString(2, Util.md5(password));
				pstmt.setString(3, Util.getTodayString());
				int flag = pstmt.executeUpdate();
				result.close();
				this.statement.close();
				if (flag > 0)
					return true;
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Get the level of user to a certain word.
	 * 
	 * @param word string of the particular word
	 * @return integer level index
	 */
	public int queryLevel(String word) {
		ResultSet result;
		int level = 0;
		try {
			statement = conn.createStatement();
			String query = "SELECT * FROM log WHERE word=\"" + word + "\"" + "AND user = \"" + this.user + "\";";
			result = statement.executeQuery(query);
			if (result.next()) {
				level = result.getInt("level");
			}
			result.close();
			statement.close();
			return level;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Query the mark of this word.
	 * 
	 * @param word word to be updated
	 * @return the mark whether this is a new word
	 */
	public int queryMark(String word) {
		ResultSet result;
		try {
			statement = conn.createStatement();
			String query = "SELECT * FROM log WHERE word=\"" + word + "\"" + "AND user = \"" + this.user + "\";";
			result = statement.executeQuery(query);
			int mark = 0;
			if (result.next()) {
				mark = result.getInt("mark");
			}
			result.close();
			statement.close();
			return mark;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Update the log for all users.
	 * 
	 * @param word  string of the word
	 * @param level expertise of the user to the word
	 * @return success flag
	 */
	public boolean updateLog(String word, int level, int mark) {
		String date = Util.getTodayString();
		PreparedStatement pstmt;
		try {
			this.statement = conn.createStatement();
			String query = "SELECT * FROM log WHERE user=" + "\"user\"" + "AND word=\"" + word + "\"";
			ResultSet result = this.statement.executeQuery(query);
			if (result.next()) {
				// update log
				this.statement.close();
				String update = "UPDATE log SET date=?, level=?, mark = ? WHERE user=? AND word = ?;";
				pstmt = conn.prepareStatement(update);
				pstmt.setString(1, date);
				pstmt.setInt(2, level);
				pstmt.setInt(3, mark);
				pstmt.setString(4, this.user);
				pstmt.setString(5, word);
			} else {
				// insert log
				this.statement.close();
				String insert = "INSERT INTO log" + " VALUES(?, ?, ?, ?, ?);";
				pstmt = conn.prepareStatement(insert);
				pstmt.setString(1, word);
				pstmt.setString(2, this.user);
				pstmt.setString(3, date);
				pstmt.setInt(4, level);
				pstmt.setInt(5, mark);
			}
			// execute
			int flag = pstmt.executeUpdate();
			pstmt.close();
			if (flag > 0)
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Word next() {
		this.index++;
		if (this.index == Database.MAX_LENGTH) {
			this.wordList.clear();
			this.retrieveWord();
			this.index = 0;
		}
		return wordList.get(this.index);
	}

	/**
	 * Search the word with its spelling.
	 * 
	 * @param word query word
	 * @return Word object matched or "?"
	 */
	public Word queryWord(String word) {
		try {
			statement = conn.createStatement();
			String query = "SELECT * FROM word WHERE word = \"" + word + "\"";
			ResultSet results = this.statement.executeQuery(query);
			if (results.next()) {
				String phone = results.getString("prounce_uk");
				String mean = results.getString("trans");
				results.close();
				statement.close();
				return new Word(word, phone, mean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Word(word, "?", "?");
	}

	/**
	 * Close the database connection. Important!
	 */
	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}