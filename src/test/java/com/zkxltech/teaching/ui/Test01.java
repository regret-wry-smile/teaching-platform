package com.zkxltech.teaching.ui;

import java.util.ArrayList;
import java.util.List;

import com.zkxltech.domain.Record;

import net.sf.json.JSONArray;

public class Test01 {
	private static Record record;
	
	public static void main(String[] args) {
		List<Record> records = new ArrayList<Record>();

		
		for (int i = 0; i < 10; i++) {
			record = new Record();
			record.setAnswer("A");
			record.setId(Integer.parseInt(String.valueOf(i)));
			records.add(record);
			System.out.println(JSONArray.fromObject(record));
		}

		System.out.println(JSONArray.fromObject(records));
	}
}
