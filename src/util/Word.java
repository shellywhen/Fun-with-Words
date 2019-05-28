package util;
/**
 * @version 1
 * @author Liwenhan Xie (1600017744)
 * @since 2019-May-24
 */
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Word {
	public static String url = 
			"http://dict.youdao.com/dictvoice?audio=";
	public String word;
	public String phonetic;
	public String meaning;
	public Player pronunciation;
	public int level = 0;
	public String visit_date = "";
	public int mark =0;
	/**
	 * Create an instance of the word.
	 * @param w word spelling
	 * @param p word phonetic
	 * @param m word meaning
	 */
	public Word(String w, String p, String m) {
		this.word = w;
		this.phonetic = p;
		this.meaning = m;
	}
	/**
	 * Load Pronunciation from youdao.dict API and play the audio.
	 * @param word the spelling of the particular word
	 * @return the Player of the music
	 * @throws JavaLayerException failed to load music
	 */
	public static boolean loadPronunciation(String word) throws JavaLayerException{
		BufferedInputStream bis = null;
		if(word.contains(" ")) return false; // unable to handle phrase
		try {
			URL url = new URL(Word.url+word);
			URLConnection connection = null;
			connection = url.openConnection();
			InputStream s_ipStream =  connection.getInputStream();
		    bis = new BufferedInputStream(s_ipStream);
		    Player player = new Player(bis);
			player.play();
			return true;
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;	
	}
	/**
	 * Debug function, print details of word.
	 */
	public Word print() {
		System.out.println(word);
		System.out.println(phonetic);
		System.out.println(meaning);
		try {
          this.loadPronunciation(this.word);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
		return this;
	}
}
