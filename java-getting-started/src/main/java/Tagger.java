import java.util.*;
import com.clarifai.api.*;
import java.io.*;

public class Tagger{
	//andy
	//client id: 637LVm8DH2FGiTgOxkp9-5K3StW399qGOCckBMT0
	// client secret: 1q3z-ltQwGKAdoA8ZxvMd4wiOCRRygryQkW6ksRg
	//andrew
	static final ClarifaiClient clarifai = new ClarifaiClient("5MJ-DS69ldGpnRN0XEht3I_bshR_lHe1fIn1MeXp", 
       "DO_M_zVOM63rft3hR9PMN5t8QlxUF75hBTHgFUfr");
	//shao
	//static final ClarifaiClient clarifai = new ClarifaiClient("BghyAs4zcj2rDBYvyCc5dYnKzcruf0jr87u3I6y3", 
    //    "-BVg3iP14k7TOYe-xa8DbvXvpcOKMFP0CB2aG3r2");
	public static Tag[][] tag(RecognitionRequest req) {
		List<RecognitionResult> results = clarifai.recognize(req);
		Tag[][] tags = new Tag[results.size()][];
		int i = 0;
		for (RecognitionResult res : results){
			tags[i] = new Tag[res.getTags().size()];
			int j = 0;
			for (Tag tag : res.getTags()) {
				tags[i][j] = tag;
				j++;
			}
			i++;
		}
		return tags;
	}
	public static void main(String[] args){
		String[] urls = {
			"https://scontent.cdninstagram.com/hphotos-xpa1/t50.2886-16/11685115_417439621778693_1857677926_n.mp4"
		};
		System.out.println(Arrays.deepToString(tag(new RecognitionRequest(urls))));
	}
}