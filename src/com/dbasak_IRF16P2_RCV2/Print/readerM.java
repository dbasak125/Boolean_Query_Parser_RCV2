package com.dbasak_IRF16P2_RCV2.Print;

/**
 * This is to implement file reading capability from host system directory.
 * 
 * @author dbasak
 * @version 1.0
 * @date 10-15-2016
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class readerM {
	
	private BufferedReader br;
	private List<String> queryTokens;
	
	public readerM(String input) {
		try {
			this.br = new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF-8"));
		} catch(Exception IOException) {
			IOException.printStackTrace();
		}
	}
	
	public List<String> getlnn() {
		try {
			this.queryTokens = Arrays.asList(this.br.readLine().split("\\s"));
			return queryTokens;
		} catch(Exception IOException) {
			return null;
		}
	}
	
	public int clean() {
		try {
			br.close();
			return 0;
		} catch(Exception IOException) {
			IOException.printStackTrace();
			return -1;
		}
	}
}
