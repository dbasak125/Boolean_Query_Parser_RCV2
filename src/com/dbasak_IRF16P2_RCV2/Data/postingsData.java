package com.dbasak_IRF16P2_RCV2.Data;

/**
 * This is a data structure for a posting.
 * 
 * @author dbasak
 * @version 1.0
 * @date 10-15-2016
 */

public class postingsData {
	private int docID;
	private int skipPointer;
	
	public int getDocID() {
		return docID;
	}
	public void setDocID(int docID) {
		this.docID = docID;
	}
	public int getSkipPointer() {
		return skipPointer;
	}
	public void setSkipPointer(int skipPointer) {
		this.skipPointer = skipPointer;
	}
}
