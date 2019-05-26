/** Tests and Usage Scenarios for package util.
 * @version 1
 * @author Liwenhan Xie (1600017744)
 * @since 2019-May-24
 */
import util.*;
import java.sql.SQLException;
import javazoom.jl.player.Player;

public class Test {
	
	public Test(){
		//debug();
		TestUtil();
		TestPronunce("beautiful");
		TestDb("test");
	}
	
//	static public void main(String[] args) {
//		new Test();
//	}
//	
	protected void TestPronunce(String word) {
		// PRONUNCE A GIVEN WORD
		try {
			Word.loadPronunciation(word);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void TestDb(String word) {
		Database db;
		try {
			// LOG IN AS A GUEST AND CREATE AN ACCOUNT
			// db = new Database();
			// db.createUser("UserName", "Password");
			
			// LOG IN TO THE DATABASE
	        db = new Database("Nancy", "123456");
	        
	        // SEARCH FOR A GIVEN WORD
			Word result = db.queryWord(word);
			result.print();
			
			// UPDATE A WORD STATE (automatically update or insert)
			int familiarity = db.queryLevel(word); // get familiarity
			int starred  = db.queryMark(word);  // get the starred state
			boolean flag = db.updateLog("sing", familiarity+1, starred);
        	System.out.println("Update: "+new Boolean(flag).toString());

			// System.out.println(db.wordList.size());
	        db.next().print();
	        
	        // REMEMBER TO CLOSE CONNECTION£¡£¡£¡£¡£¡£¡£¡
			db.close();
		} catch (ClassNotFoundException | SQLException | LoginException e) {
			e.printStackTrace();
		}
	}
	
	protected void TestUtil() {
		// ENCRYPT THE PASSWORD
		String password = "123456";
		System.out.println(password+" -> "+Util.md5(password));
		
		// GET TODAY
		System.out.println(Util.getTodayLongString());
		System.out.println(Util.getTodayString());
	}
	
	protected void debug() {
		Database db;
		try {
			db = new Database("Nancy", "123456");
			boolean flag = db.updateLog("laugh", 5, 2);
			System.out.println("Update: "+new Boolean(flag).toString());
		
		} catch (ClassNotFoundException | SQLException | LoginException e) {
			e.printStackTrace();
		}
	}
}
