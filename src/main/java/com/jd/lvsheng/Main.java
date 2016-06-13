package com.jd.lvsheng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		for (String s : list) {
			System.out.println(s);
		}

		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}

		String[] arr = new String[3];
		for (String s : arr) {
			System.out.println(s);
		}

		int len = arr.length;
		for (int i = 0; i < len; i++) {
			System.out.println(arr[i]);
		}
	}
}
