package com.dbasak_IRF16P2_RCV2.Print;

/**
 * This is to implement file writing capability to host system directory.
 * 
 * @author dbasak
 * @version 1.0
 * @date 10-15-2016
 */

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class writerM {

	private BufferedWriter bw;
	
	public writerM(String output) {
		try {
			this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));
		} catch(Exception IOException) {
			IOException.printStackTrace();
		}
	}
	
	public int setlnn(String result) {
		try {
			bw.write(result);
		} catch(Exception IOException) {
			IOException.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	public int clean() {
		try {
			bw.flush();
			bw.close();
			return 0;
		} catch(Exception IOException) {
			IOException.printStackTrace();
			return -1;
		}
	}
}
