package com.googlecode.edisontsui;


public class IdGenerator {
	static private int m_count = 0;
	
	static public int getNewId() {
		return m_count++;
	}
}
