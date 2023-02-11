package com.roche.swam.labsimulator.lis.bl;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OrderIdGenerator {

	private Random random;

	public OrderIdGenerator() {
		this.random = new Random();
	}

	private String getRandomFormatedInt(int length) {
		double exp = length;
		int max = (int) Math.round(Math.pow(10, exp)) - 1;
		int number = this.random.nextInt(max);
		return String.format("%0" + length + "d", number);
	}

	public String getNext() {

		return this.getRandomFormatedInt(10);
	}
}
