//package finalproject;
/*
Name: Acacie Song
ID: 261182381
*/
import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, ArrayList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does an exploration of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		if(!internet.getVisited(url)) {
			internet.addVertex(url);
			internet.setVisited(url, true);
			
			//put worldIndex
			ArrayList<String> pageContent = parser.getContent(url); 
			
			for(String word: pageContent) {
				String notCaseSensitive = word.toLowerCase();
				if(!wordIndex.containsKey(notCaseSensitive)) { //not two times same word as key
					wordIndex.put(notCaseSensitive, new ArrayList<String>());	
				}
				if(!wordIndex.get(notCaseSensitive).contains(url)) { //no two times same url
					wordIndex.get(notCaseSensitive).add(url); 
				}
			}
			//visit neighbour
			ArrayList<String> listLinkNeighbors = parser.getLinks(url);
			
			for(String neighbor: listLinkNeighbors) {
				if(!internet.getVisited(neighbor)) {
					crawlAndIndex(neighbor);
				}
				internet.addEdge(url, neighbor);
			}
		}
	}
	
	

//Please note that your word index should not be case sensitive, that is words such as "Hello","hello",or"hElLo" should all be considered the same. How to change my code so that works?*/
	
	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		ArrayList<String> listVertices = this.internet.getVertices();
		ArrayList<Double> prevListRank = new ArrayList<Double>();
		int lengthLV = listVertices.size();
		
		for(String eachVertice: listVertices) {
			this.internet.setPageRank(eachVertice, 1.0);
			prevListRank.add(1.0);
		}
		if(epsilon >=1) { //if bigger than 1 you just return
			return;
		}
		boolean allSmallerThanEpsilon = false;
		
		while(!allSmallerThanEpsilon) {
			
			ArrayList<Double> newListRank = computeRanks(listVertices);
			//update page
			for(int i=0; i<lengthLV; i++) {
				this.internet.setPageRank(listVertices.get(i), newListRank.get(i));
			}
			//Convergence
			//if the new one difference is enough smaller with the prev one
			allSmallerThanEpsilon = true;
			for(int i=0; i<lengthLV; i++) {
				if(Math.abs(prevListRank.get(i)-newListRank.get(i))>=epsilon) {
					allSmallerThanEpsilon = false;
					break;
				}
			}
			prevListRank = new ArrayList<Double>(newListRank);
		}
		
	}

	
	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 * 
	 * This method will probably fit in about 20 lines.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		double d = 0.5;
		double d1 = 1-d;
		ArrayList<Double> pageRankList = new ArrayList<Double>();
		
		for(String eachVertice: vertices) {
			//for neighbor that get into this vertex
			ArrayList<String> listNeighborsGetIn = this.internet.getEdgesInto(eachVertice);
			double neighborsResult = 0;
			
			for(String neighborGetIn: listNeighborsGetIn) { 
				if(this.internet.getOutDegree(neighborGetIn)!=0) { // just in case: but impossible usually
					double prDivideOut = this.internet.getPageRank(neighborGetIn)/this.internet.getOutDegree(neighborGetIn);
					neighborsResult+=prDivideOut;
				}
			}
			
			//the formula
			double resultPageRank = d1 + d*neighborsResult;
			pageRankList.add(resultPageRank);
		}
		return pageRankList;
	}

	
	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method will probably fit in about 10-15 lines.
	 */
	public ArrayList<String> getResults(String query) { 
		ArrayList<String> result = new ArrayList<String>();
		String lowerCase = query.toLowerCase();
		
		if(wordIndex.containsKey(lowerCase)) { //if contain the world
			ArrayList<String> listUrl = wordIndex.get(lowerCase); //return list URL link to it
			HashMap<String, Double> mapListUrl = new HashMap<String, Double>();
			
			for(String url: listUrl) { //rank of each;
				mapListUrl.put(url, this.internet.getPageRank(url));
			}
			//sort
			result = Sorting.fastSort(mapListUrl);
		}
		return result; 
	}
}
