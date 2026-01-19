package com.revature.utils;

public class ServiceUtil {

	public static void validateId(String id) {
		try {
			int a = Integer.parseInt(id);
			if (a < 1) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			throw new NumberFormatException(id);
		}
	}
}
