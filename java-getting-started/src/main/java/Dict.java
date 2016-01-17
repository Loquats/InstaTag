import java.util.*;
import java.io.*;

public class Dict{
	static HashMap<String, Integer> d = new HashMap<>();
	static{
		try{
			Scanner file = new Scanner(new File("20k.txt"));
			int i = 1;
			while(file.hasNextLine()){
				String s = file.nextLine().trim();
				if(!s.isEmpty()){
					d.put(s, i++);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	static int index(String s){
		Integer x = d.get(s);
		if(x == null){
			return -1;
		}
		return x;
	}
}