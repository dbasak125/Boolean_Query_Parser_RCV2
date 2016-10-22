package com.dbasak_IRF16P2_RCV2.Print;

/**
 * This is a printer class.
 * Defines the filed list of RCV2 index.
 * Handles the sysout printing of various debugging commands.
 * 
 * @author dbasak
 * @version 1.0
 * @date 10-15-2016
 */

import org.apache.lucene.index.IndexReader;

public class printStats {
	private String[] fieldList = {	
			"text_nl",
			"text_fr",
			"text_de",
			"text_ja",
			"text_ru",
			"text_pt",
			"text_es",
			"text_it",
			"text_da",
			"text_no",
			"text_sv"};
	
	public String[] getFieldList() {
		return fieldList;
	}

	public void printToSysOut(IndexReader indexLucene) {
		
		try {
			System.out.println("No. of documents in RCV2 corpus: "+indexLucene.numDocs());
			for (String field : fieldList) {
				System.out.println("docCount "+field+": "+indexLucene.getDocCount(field));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}
}
