package com.dbasak_IRF16P2_RCV2.booleanSearch;

/**
 * This is a test class for boolean search implementation.
 * <Inputs> 0 - RCV2 index folder directory path
 *          1 - output file (outputs the search result here)
 *          2 - input file (contains the query strings)
 *          
 * The main method creates the necessary objects required to perform 2 tasks:
 * 1. Create the in-memory inverted-index (buildIndex)
 * 2. Execute boolean search on the index and output results (searchIndex)
 * 
 * @author dbasak
 * @version 1.0
 * @date 10-15-2016
 */

public class testBooleanSearch {
	
	public static void main(String[] args) {
		try {
			System.out.println("Starting index build..");
			long startTime = System.currentTimeMillis();
			long endTime;
			
			//build inverted index
			buildIndex idx = new buildIndex();
			idx.createInMemoryPostingsList(args[0]);
			System.out.println("Index build successful");
			endTime = System.currentTimeMillis();
			System.out.println("\tTime taken (ms): "+(endTime-startTime));
			
			//search inverted index
			System.out.println("\nStarting search..");
			searchIndex srx = new searchIndex();
			srx.search(idx, args[1], args[2]);
			System.out.println("Search executed");
			System.out.println("\tTime taken (ms): "+(System.currentTimeMillis()-endTime));
			
			//print time statistics
			endTime = System.currentTimeMillis();
			System.out.println("______________________________");
			System.out.println("\tTotal time (ms): "+(endTime-startTime));
						
		} catch(Exception IOException) {
			System.out.println("There was an Error!\n");
			IOException.printStackTrace();
			System.out.println("\nExiting application.");
			System.exit(-1);
		}
	}

}
