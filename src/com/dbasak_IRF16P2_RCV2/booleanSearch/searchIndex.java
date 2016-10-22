package com.dbasak_IRF16P2_RCV2.booleanSearch;

/**
 * This class implements the search on in-memory inverted-index.
 * <Input> index Object, output file, input file
 * <Output> int error code
 * 
 * @author dbasak
 * @version 1.0
 * @date 10-15-2016
 */

import java.util.ArrayList;
import com.dbasak_IRF16P2_RCV2.Data.*;
import com.dbasak_IRF16P2_RCV2.Print.*;
import java.util.LinkedList;
import java.util.List;

public class searchIndex {
	
	//initialize objects
	private readerM ip;
	private writerM op;
	private ArrayList<termData> pList;
	private ArrayList<termData> pList2;
	private int nComp, docCount, docCount2;
	
	@SuppressWarnings("unchecked")
	public int search(buildIndex idx, String output, String input) {
		
		try {
			List<String> qTerms;
			String qLine = "";
			//create new reader/writer objects
			ip = new readerM(input);
			op = new writerM(output);
			
			//for each line in input.txt...
			while ((qTerms = ip.getlnn()) != null) {
				qLine = "";
				pList = new ArrayList<termData>();
				for(String s : qTerms) { //compute get postings
					qLine = qLine+s+" ";
					//... and copy postings list of this Term to an intermediate data structure - pList2
					getPostings(s, idx);
				}
				
				if (pList.size() > 0) {
					pList2 = (ArrayList<termData>)pList.clone(); nComp=0;
					taatAnd(qLine.trim(), qTerms, idx); //compute taat-and on the intermediate-List
					pList2 = (ArrayList<termData>)pList.clone(); nComp=0;
					taatOr(qLine.trim(), qTerms, idx);  //compute taat-or on the intermediate-List
					pList2 = (ArrayList<termData>)pList.clone(); nComp=0; docCount=0; docCount2=0;
					daatAndOr(qLine.trim(), qTerms, idx); //compute daat-and and daat-or on the intermediate-List
				}
			}
			
			//clean input/output file handlers and exit
			ip.clean();
			op.clean();
		} catch(Exception IOException) {
			IOException.printStackTrace();
		}
		return 0;
	}
	
	private int getPostings(String term, buildIndex idx) {
		String result = "GetPostings\n"+term+"\nPostings list:";
		
		//search the Term and print its postings list
		for (termData td : idx.termList) {
			if (td.getToken().equals(term)) {
				pList.add(td); //copy postings list of that Term to an intermediate data structure
				for (postingsData post : td.getNodeHead()) {
					result+=" "+post.getDocID();
				}
			}
		}
		result+="\n";
		op.setlnn(result);
		return 0;
	}
	
	private int taatAnd(String qLine, List<String> qTerms, buildIndex idx) {
		//execute taat and
		String result = "TaatAnd\n"+qLine+"\nResults:";
		termData list;
		int i=0;
		
		list = pList2.get(0);
		if (pList2.size() > 1) {
			for (termData curPList : pList2) {
				//iterate postings-list of terms. intersect with next. iterate.
				if (i == 0) {//if first iteration, then skip, as it is comparing curPlist with itself (since list = curPlist, for first loop iteration)
					i=1;
					continue;
				}
				list = intersect(curPList, list);
			}
		}
		
		for (postingsData s : list.getNodeHead()) {
			result+=" "+s.getDocID();
		}
		if (list.getNodeHead().size() == 0) {
			result+=" "+"empty";
		}
		
		result+="\n"+"Number of documents in results: "+list.getNodeHead().size();
		result+="\n"+"Number of comparisons: "+nComp+"\n";
		op.setlnn(result);
		return 0;
	}
	
	private int taatOr(String qLine, List<String> qTerms, buildIndex idx) {
		//execute taat or
		String result = "TaatOr\n"+qLine+"\nResults:";
		termData list;
		int i=0;
		
		list = pList2.get(0);
		if (pList2.size() > 1) {
			for (termData curPList : pList2) {
				//iterate postings-list of terms. union with next. iterate.
				if (i == 0) {//if first iteration, then skip, as it is comparing curPlist with itself (since list = curPlist, for first loop iteration)
					i=1;
					continue;
				}
				list = union(curPList, list);
			}
		}
		
		for (postingsData s : list.getNodeHead()) {
			result+=" "+s.getDocID();
		}
		if (list.getNodeHead().size() == 0) {
			result+=" "+"empty";
		}
		
		result+="\n"+"Number of documents in results: "+list.getNodeHead().size();
		result+="\n"+"Number of comparisons: "+nComp+"\n";
		op.setlnn(result);
		return 0;
	}
	
	private boolean checkPointersExhausted() {
		//a method to check if any of the pointers pointing to documents in postings list has been exhausted
		for (termData t : pList2) {
			if (t.getDocPointer() < t.getNodeHead().size()) {
				return false;
			}
		}
		return true;
	}
	
	private int daatAndOr(String qLine, List<String> qTerms, buildIndex idx) {
		//execute daat and and daat or
		String result = "DaatAnd\n"+qLine+"\nResults:";
		String result2 = "DaatOr\n"+qLine+"\nResults:";
		int[] resultList = new int[idx.termList.size()];
		int minDocId = 0;
		int j;
		
		//reset every document pointer to 0
		for (termData t : pList2) {
			t.setDocPointer(0);
		}		
		if (pList2.size() > 1) {
			while (!checkPointersExhausted()) { //while pointers are not exhausted for all terms..
				//find minimum document id in the first column in postings list
				minDocId = idx.termList.size();
				for (termData t : pList2) {
					if (t.getDocPointer() < t.getNodeHead().size()) {
						nComp++;
						if (t.getNodeHead().get(t.getDocPointer()).getDocID() < minDocId) {
							minDocId = t.getNodeHead().get(t.getDocPointer()).getDocID();
						}
					}
				}
				//for the smallest document id, count how many of the search terms are contained in it. 
				//increment that count for that document id in result array.
				for (termData t : pList2) {
					if (t.getDocPointer() < t.getNodeHead().size()) {
						nComp++;
						if (t.getNodeHead().get(t.getDocPointer()).getDocID() == minDocId) {
							resultList[minDocId]+=1;
							//for the postings lists containing the smallest document id,.. 
							//increment the document pointer to next one in linked list.
							t.setDocPointer(t.getDocPointer()+1);
						}
					}
				}
				
				//iterate this process till all pointers are exhausted,..
				//i.e., all the weights of document ids have been calculated iteratively and stored in results.
			}
			for (j=0; j<idx.termList.size(); j++) {
				//print result for daat-and only when document weight is equal to count of search terms
				if (resultList[j] == pList2.size()) {
					result+=" "+j;
					docCount++;
				}
				//print result for daat-or for all documents whose weights are positive i.e., > 0
				if (resultList[j] > 0) {
					result2+=" "+j;
					docCount2++;
				}
			}
		} else {
			//if number of search term is 1, print the postings list as result for both daat-and and daat-or
			for (postingsData p : pList2.get(0).getNodeHead()) {
				result+=" "+p.getDocID();
				result2+=" "+p.getDocID();
				docCount++;
				docCount2++;
			}
		}
		
		if (docCount == 0) {
			result+=" "+"empty";
		}
		if (docCount2 == 0) {
			result2+=" "+"empty";
		}
		
		result+="\n"+"Number of documents in results: "+docCount;
		result+="\n"+"Number of comparisons: "+nComp+"\n";
		result2+="\n"+"Number of documents in results: "+docCount2;
		result2+="\n"+"Number of comparisons: "+nComp+"\n";
		op.setlnn(result);
		op.setlnn(result2);
		return 0;
	}
	
	private termData intersect(termData a, termData b) {
		//implement intersection of two postings list
		//since postings lists are non-decreasing ordered, we take elements from one list,...
		//and binary-search that id in second list. if find a match, build the intersect list using that id.
		termData stagingList = new termData("x", 0, new LinkedList<postingsData>());
		for (postingsData p : a.getNodeHead()) {
			//calls binary-search method
			if (b.getNodeHead().size() > 0 && binarySearch(p, b.getNodeHead(), 0, (b.getNodeHead().size() - 1)))
				stagingList.getNodeHead().add(p);
		}
		return stagingList;
	}
	
	private termData union(termData a, termData b) {
		//implements union of two ordered list
		termData stagingList = new termData("x", 0, new LinkedList<postingsData>());
		int i=0;
		int j=0;
		//a variation of merge sort, works well here.
		//if both list contains duplicate ids, which is possible in our case,...
		//record the id, and increment pointer on both lists. repeat.
		while (i<a.getNodeHead().size() || j<b.getNodeHead().size()) {
			nComp++;
			if (i>=a.getNodeHead().size()) {
				stagingList.getNodeHead().add(b.getNodeHead().get(j));
				j++;
			} else if (j>=b.getNodeHead().size()) {
				stagingList.getNodeHead().add(a.getNodeHead().get(i));
				i++;
			} else if (a.getNodeHead().get(i).getDocID() < b.getNodeHead().get(j).getDocID()) {
				stagingList.getNodeHead().add(a.getNodeHead().get(i));
				i++;
			} else if (a.getNodeHead().get(i).getDocID() > b.getNodeHead().get(j).getDocID()) {
				stagingList.getNodeHead().add(b.getNodeHead().get(j));
				j++;
			} else {
				stagingList.getNodeHead().add(b.getNodeHead().get(j));
				i++;
				j++;
			}
		}
		return stagingList;
	}
	
	private boolean binarySearch(postingsData p, LinkedList<postingsData> b, int start, int end) {
		//implements binary-search
		//recursively checks for a match in log-n time.
		//self-explanatory.
		
		//the number of comparisons are maintained in a private class variable, because of recursion.
		int i=(end-start)/2;
		
		nComp++;
		if (i==0) {
			if (p.getDocID() == b.get(start).getDocID() || p.getDocID() == b.get(end).getDocID())
				return true;
			else
				return false;
		} else {
			if (p.getDocID() < b.get(start+i).getDocID()) {
				return binarySearch(p, b, start, (start+i-1));
			} else if (p.getDocID() > b.get(start+i).getDocID()) {
				return binarySearch(p, b, (start+i+1), end);
			} else if (p.getDocID() == b.get(start+i).getDocID()) {
				return true;
			} else
				return false;
		}
	}
}
