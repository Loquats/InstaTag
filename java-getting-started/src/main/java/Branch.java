import java.util.*;
import com.clarifai.api.*;
import java.io.*;

public class Branch{
	static final int TARGET = 7;
	static final int SECTION = 10;
	public static ArrayList<ArrayList<String> > branchTags(Tag[] origTags){
		Histo h = new Histo();
		int tagcount = 0;
		for(Tag tag : origTags){
			if(tagcount == TARGET){
				break;
			}
			String name = tag.getName();
			List<InstagramEntry> hashs = null;
			try{
				hashs = Instascrape.getHashtags(name);
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
			//System.out.println(hashs);
			//new Scanner(System.in).nextLine();
			ArrayList<InstagramEntry> entries = new ArrayList<InstagramEntry>();
			ArrayList<String> urls = new ArrayList<String>();
			for(InstagramEntry e : hashs){
				if(!e.isVideo() && e.getHashtags().size() >= 5 && urls.size() < TARGET){
					urls.add(e.getUrl());
					entries.add(e);
				}
			}
			Tag[][] tags = Tagger.tag(new RecognitionRequest(urls.toArray(new String[0])));
			//System.out.println(Arrays.deepToString(tags));
			int i = 0;
			for(Tag[] tr : tags){
				double prob = 0.0;
				for(Tag t : tr){
					for(Tag tt : origTags){
						if(t.getName().equals(tt.getName())){
							prob += t.getProbability() * tt.getProbability();
						}
					}
				}
				for(String hashtag : entries.get(i).getHashtags()){
					h.put(hashtag, prob);
				}
				i++;
			}
			System.out.println(urls.size());
			//System.out.println(h.shortedSet());
			tagcount++;
			//new Scanner(System.in).nextLine();
		}
		ArrayList<Pair> best = new ArrayList<Pair>();
		for(Map.Entry<String, Double> e : h.shortedSet()){
			int index = Dict.index(e.getKey());
			if(index == -1){
				index = 20000;
			}
			double scale = Math.log(index) / Math.log(20000);
			best.add(new Pair(e.getValue() * scale, e.getKey()));
		}
		Collections.sort(best);
		ArrayList<String> popular = new ArrayList<>();
		for(int i = 0; i<SECTION; i++){
			popular.add(best.get(i).word);
			h.remove(best.get(i).word);
		}
		ArrayList<String> common = new ArrayList<String>();
		ArrayList<String> unique = new ArrayList<String>();
		ArrayList<Map.Entry<String, Double> > sorted = h.shortedSet();
		for(int i = 0; i < sorted.size(); i++){
			String word = sorted.get(i).getKey();
			if(Dict.index(word) != -1){
				if(common.size() < SECTION){
					common.add(word);
				}
			}else{
				if(unique.size() < SECTION){
					unique.add(word);
				}
			}
		}
		ArrayList<ArrayList<String> > res = new ArrayList<>();
		res.add(popular);
		res.add(unique);
		res.add(common);
		return res;
	}
	static class Pair implements Comparable<Pair>{
		double value;
		String word;
		Pair(double v, String w){
			value = v;
			word = w;
		}
		public int compareTo(Pair b){
			return new Double(b.value).compareTo(value);
		}
	}
	public static void main(String[] args){
		Tag[] tags = Tagger.tag(new RecognitionRequest("https://scontent.cdninstagram.com/hphotos-xap1/t51.2885-15/e35/12424685_844239065687314_1956852925_n.jpg"))[0];
		System.out.println(branchTags(tags));
	}
}