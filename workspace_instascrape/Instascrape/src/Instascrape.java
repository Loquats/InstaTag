import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** Parses hashtags and other metadata from Instagram searches.
 * 
 * @author Andy Zhang, Joseph Lee, Xinhe Ren
 */
public class Instascrape {
	/** Returns InstagramEntry objects associated with searched images
	 * 
	 * @param tag a word describing the desired images
	 * @return returns a list of InstragramEntry(s).
	 * @throws IOException 
	 */
	public static List<InstagramEntry> getHashtags(String tag) throws IOException {
		Document doc = Jsoup.connect(String.format("https://www.instagram.com/explore/tags/%s/", tag)).get();
        Elements img = doc.select("script");
        String imgJSONStr = null;
        for(Element i : img) {
        	if (i.html().matches("window\\._sharedData.+")) {
        		imgJSONStr = i.html();
        		break;
        	}
        }
        imgJSONStr = imgJSONStr.replaceAll("window._sharedData = ", "");
        imgJSONStr = imgJSONStr.substring(0, imgJSONStr.length() - 1);
        JSONObject imgJSON = new JSONObject(imgJSONStr);
        
        JSONArray topKeks = imgJSON.getJSONObject("entry_data").getJSONArray("TagPage").getJSONObject(0).getJSONObject("tag").getJSONObject("top_posts").getJSONArray("nodes");
        JSONArray newMemes = imgJSON.getJSONObject("entry_data").getJSONArray("TagPage").getJSONObject(0).getJSONObject("tag").getJSONObject("media").getJSONArray("nodes");
        
        // List of InstagramEntry(s)
        List<InstagramEntry> instagramEntries = new ArrayList<InstagramEntry>();
        
        for (int i = 0; i < topKeks.length(); i++) {
        	JSONObject kek = topKeks.getJSONObject(i);
        	instagramEntries.add(new InstagramEntry(kek));
        }
        
        for (int i = 0; i < newMemes.length(); i++) {
        	JSONObject meme = newMemes.getJSONObject(i);
        	instagramEntries.add(new InstagramEntry(meme));
        }
        return instagramEntries;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(getHashtags("neko"));
	}
}
