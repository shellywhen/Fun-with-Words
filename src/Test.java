import util.*;

import java.sql.SQLException;

import javazoom.jl.player.Player;
/** Tests and Usage Scenarios for package util.
 * @version 1
 * @author Liwenhan Xie (1600017744)
 * @since 2019-May-24
 */
public class Test {
	public Test(){
		TestPronunce("beautiful");
		TestDb("test");
	}
	static public void main(String[] args) {
		new Test();
	}
	protected void TestPronunce(String word) {
		try {
			Player player = Word.loadPronunciation(word);
			player.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void TestDb(String word) {
		Database db = new Database();
		db.connect();
		try {
			Word result = db.queryWord(word);
			result.print();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
