package com.dbasak_IRF16P2_RCV2.booleanSearch;

/**
 * This class builds the in-memory inverted-index using RCV2 index documents.
 * <Input> is the system path of folder containing RCV2 index
 * <Output> an ArrayList of type termList holding the inverted-index
 * 
 * @author dbasak
 * @version 1.0
 * @date 10-15-2016
 */

import java.nio.file.FileSystems;
import com.dbasak_IRF16P2_RCV2.Data.*;
import com.dbasak_IRF16P2_RCV2.Print.*;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.lucene.index.*;
import org.apache.lucene.store.*;

public class buildIndex {
	
	public final ArrayList<termData> termList = new ArrayList<termData>();
	
	private int build(String document) {
		TermsEnum sv;
		PostingsEnum pe;
		int termsCount=0;
		postingsData posting;
		LinkedList<postingsData> postingsList;
		
		try {
			//create IndexReader object
			Directory pwd = FSDirectory.open(FileSystems.getDefault().getPath(document));
			IndexReader indexLucene = DirectoryReader.open(pwd);
			
			printStats printTemplate = new printStats();
			//printTemplate.printToSysOut(indexLucene);
			
			//iterate over the language fields and extract terms for each language
			for (String field : printTemplate.getFieldList()) {
				//get Terms list for text_xx
				sv = MultiFields.getFields(indexLucene).terms(field).iterator();
				while (sv.next() != null) {
					//for each Term, create the Postings List
					postingsList = new LinkedList<postingsData>();
					pe = MultiFields.getTermDocsEnum(indexLucene, field, sv.term());
					while (pe.nextDoc() != PostingsEnum.NO_MORE_DOCS) {
						posting = new postingsData();
						posting.setDocID(pe.docID());
						posting.setSkipPointer(0);
						postingsList.addLast(posting);
					}
					
					//add the Term, the Document frequency and Postings list in termList object
					termList.add(new termData(sv.term().utf8ToString(), sv.docFreq(), postingsList));
					
					//iterate till all language fields have been processed
					termsCount++;
				}
			}			
			
			System.out.println("Total Terms added: "+termsCount);
			
			//close IndexReader object
			indexLucene.close();
		    return 0;
		} catch (Exception exception) {
			exception.printStackTrace();
			return -1;
		}
		
	}
	
	public int createInMemoryPostingsList(String document) {
		if (this.termList.isEmpty()) {
			build(document);
			return 0;
		}
		return -1;
	}
		
}
