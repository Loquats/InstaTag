import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Contains the image/video URL and other metadata for an Instagram post.
 * @author Andy Zhang, Joseph Lee
 */
public class InstagramEntry {
	private int numLikes;
	private int numComments;
	private List<String> hashtags;
	private boolean isVideo;
	private String url;

	public InstagramEntry(JSONObject data) throws IOException {
		numLikes = data.getJSONObject("likes").getInt("count");
		numComments = data.getJSONObject("comments").getInt("count");
		hashtags = parseHashtags(data.getString("caption"));

		if (data.getBoolean("is_video")) {
			// look for the video
			isVideo = true;
			String code = data.getString("code");
			Document videoDoc = Jsoup.connect(String.format("https://www.instagram.com/p/%s/", code)).get();
			Elements video = videoDoc.select("meta[property=og:video:secure_url]");
			url = video.attr("content");
		} else {
			isVideo = false;
			url = data.getString("thumbnail_src");
		}
	}

	public int getNumLikes() {
		return numLikes;
	}

	public int getNumComments() {
		return numComments;
	}

	public List<String> getHashtags() {
		return hashtags;
	}

	public boolean isVideo() {
		return isVideo;
	}

	public String getUrl() {
		return url;
	}

	/** Parses the hashtags in a caption.
	 * 
	 * @param rawCaption caption of image
	 * @return a list of hashtags
	 */
	private static List<String> parseHashtags(String rawCaption) {
		String[] temp = rawCaption.split(" ");
		for (int i = 0; i < temp.length; i++) {
			if (!temp[i].matches("#.+")) {
				temp[i] = "";
			} else {
				temp[i] = temp[i].substring(1);
			}
		}
		List<String> hashList = new ArrayList<String>();
		for (String s : temp) {
			if (!s.equals("")) {
				if (!s.contains("#")) {
					hashList.add(s);
				} else {
					String[] temp2 = s.split("#");
					for (String s2 : temp2) {
						hashList.add(s2);
					}
				}
			}
		}
		return hashList;
	}
	
	@Override
	public String toString() {
		return "InstagramEntry[numLikes: " + numLikes + ", numComments: " + numComments + 
				", isVideo: " + isVideo + " , hashtags:" + hashtags.toString() + 
				", url: " + url + "]";
	}
}
