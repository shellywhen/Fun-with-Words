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
		try {
			this.pronunciation = loadPronunciation(w);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Load Pronunciation from youdao.dict API
	 * @param word the spelling of the particular word
	 * @return the Player of the music
	 * @throws Exception
	 */
	public static Player loadPronunciation(String word) throws Exception {
		URL url = new URL(Word.url+word);
		URLConnection connection = null;
		try {
			connection = url.openConnection();
		}catch(IOException e){
			e.printStackTrace();
		}
		BufferedInputStream bis = null;
		try {
			InputStream s_ipStream =  connection.getInputStream();
		    bis = new BufferedInputStream(s_ipStream);
		}catch (IOException e){
			e.printStackTrace();
		}
		return new Player(bis);
	}
	public void print() {
		System.out.println(word);
		System.out.println(phonetic);
		System.out.println(meaning);
		try {
			pronunciation.play();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
