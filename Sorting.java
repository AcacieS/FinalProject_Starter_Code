//package finalproject;
/*
Name: Acacie Song
ID: 261182381
*/
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map.Entry; // You may (or may not) need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {
    	
     	ArrayList<Entry<K,V>> keyValueList = new ArrayList<Entry<K,V>>(results.entrySet());
     	//sorting values
    	ArrayList<Entry<K,V>> sortedKeyValueList = mergeSort(keyValueList);
    	
    	//return the key
    	ArrayList<K> sortedKeysList = new ArrayList<>();
    	for(Entry<K,V> sortedKey: sortedKeyValueList) {
    		sortedKeysList.add(sortedKey.getKey());
    	}
    	return sortedKeysList;
    }
    
    private static <K, V extends Comparable<V>> ArrayList<Entry<K,V>> mergeSort(ArrayList<Entry<K,V>> mergeKV) {
    	int size = mergeKV.size();
    	if(size<=1) { //if just size 1, don't need mergeSort
    		return mergeKV;
    	}else {
    		//split to two
	    	int mid = (size)/2;
			ArrayList<Entry<K,V>> left = new ArrayList<>(mergeKV.subList(0, mid));
	    	ArrayList<Entry<K,V>> right = new ArrayList<>(mergeKV.subList(mid, size));
    		//sort each one
	    	left = mergeSort(left);
    		right = mergeSort(right);
    		//merge together
    		return merge(left, right);
    	}
    }

    private static <K, V extends Comparable<V>> ArrayList<Entry<K,V>> merge(ArrayList<Entry<K,V>> left, ArrayList<Entry<K,V>> right) {
    	ArrayList<Entry<K,V>> mergedLR = new ArrayList<>();
    	//our current index
    	int leftIndex = 0, rightIndex = 0;
    	
    	//until one is finished
    	while(leftIndex < left.size() && rightIndex < right.size()) {
    		Entry<K,V> leftEntry = left.get(leftIndex);
    		Entry<K,V> rightEntry = right.get(rightIndex);
    		
    		if(leftEntry.getValue().compareTo(rightEntry.getValue())>=0) { //if left is greater than right add it
    			mergedLR.add(leftEntry);
    			leftIndex++;
    		}else {//if right is greater than left add it
    			mergedLR.add(rightEntry);
    			rightIndex++;
    		}
    	}
    	//for the rest inside Index
    	while(leftIndex < left.size()) {
    		mergedLR.add(left.get(leftIndex));
			leftIndex++;
    	}
    	while(rightIndex < right.size()) {
    		mergedLR.add(right.get(rightIndex));
			rightIndex++;
    	}
    	
    	return mergedLR;
    }
}
