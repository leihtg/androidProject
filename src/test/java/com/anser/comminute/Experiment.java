package com.anser.comminute;

import java.util.List;

public class Experiment {
	private static <T extends List> void reflect(T t) {
		System.out.println(t);
	}

	public static void main(String[] args) {
		reflect(null);
	}
}
