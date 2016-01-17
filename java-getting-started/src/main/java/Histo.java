import java.util.*;

public class Histo{
	private HashMap<String, Double> m = new HashMap<>();
	public void put(String s, double d){
		if(s.isEmpty() || !s.matches("\\w+") || s.contains("follow") || s.contains("like")){
			return;
		}
		s = s.toLowerCase();
		Double x = m.get(s);
		if( x == null ){
			x = 0.0;
		}
		m.put(s, x + d);
	}
	public double get(String s){
		s = s.toLowerCase();
		return m.get(s);
	}
	public void remove(String s){
		m.remove(s);
	}
	public void merge(Histo h){
		for(Map.Entry<String,Double> e : h.entrySet()){
			put(e.getKey(), e.getValue());
		}
	}
	public Set<Map.Entry<String,Double> > entrySet(){
		return m.entrySet();
	}
	public ArrayList<Map.Entry<String,Double> > shortedSet(){
		ArrayList<Map.Entry<String,Double> > list = new ArrayList<>(m.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String,Double>>(){
			public int compare(Map.Entry<String,Double> a, Map.Entry<String,Double> b){
				return b.getValue().compareTo(a.getValue());
			}
		});
		return list;
	}
}