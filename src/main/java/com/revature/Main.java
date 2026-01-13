package com.revature;

import com.revature.utils.DatabaseUtil;
import com.revature.utils.JavalinUtil;

import java.util.Objects;
import java.util.Properties;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello world!");

		try {
			DatabaseUtil.initializeData();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}


		JavalinUtil.startServer();
	}
}