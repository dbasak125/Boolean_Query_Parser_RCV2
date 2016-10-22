package com.dbasak_IRF16P2_RCV2.Data;

/**
 * This is a data structure for dictionary.
 * Note: the postings-list is defined as a linkedList of postingsData objects.
 * 
 * @author dbasak
 * @version 1.0
 * @date 10-15-2016
 */

import java.util.LinkedList;

public class termData {
	
	private String token;
	private int docPointer;
	private LinkedList<postingsData> nodeHead;
	
	public termData(String token, int docPointer, LinkedList<postingsData> nodeHead) {
		this.token = token;
		this.docPointer = docPointer;
		this.nodeHead = nodeHead;
	}
	
	public LinkedList<postingsData> getNodeHead() {
		return nodeHead;
	}
	public void setNodeHead(LinkedList<postingsData> nodeHead) {
		this.nodeHead = nodeHead;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getDocPointer() {
		return docPointer;
	}
	public void setDocPointer(int docPointer) {
		this.docPointer = docPointer;
	}
}
