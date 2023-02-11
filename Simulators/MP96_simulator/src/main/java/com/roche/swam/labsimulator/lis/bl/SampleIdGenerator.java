package com.roche.swam.labsimulator.lis.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class SampleIdGenerator {

	static String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public String getNextTime() {
		long timeSeed = System.nanoTime();
		double randSeed = Math.random() * 1000;
		long midSeed = (long) (timeSeed * randSeed);
		String s = midSeed + "";
		String subStr = s.substring(0, 9);
		return subStr;
	}
	
	public static String getNext() {
		return UUID.randomUUID().toString().substring(0, 5)+UUID.randomUUID().toString().substring(0, 5);
	}

	public static List<String> getSamplePosition() {

		List<String> samplePosition = new ArrayList<>();

		for (int i = 1; i <= 12; i++) {

			for (char x = 'A'; x <= 'H'; x++) {
				samplePosition.add(x + String.valueOf(i));

			}
		}

		return samplePosition;

	}

	public static String getRandomValues() {
		StringBuilder sb = new StringBuilder(5);
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			int c = random.nextInt(string.length());
			sb.append(string.charAt(c));
		}

		return sb.toString();

	}
	
	public static List<String> getLpSeqSamplePosition(int number) {

		List<String> samplePosition = new ArrayList<>();

		
		for(int i=1;i<=number;i++) {
			
			if(i<=24) {
				samplePosition.add("A1");	
			}else if(i>24 && i<=48) {
				samplePosition.add("B1");	
			}else if(i>48 && i<=72) {
				samplePosition.add("C1");
			}else if(i>72 && i<=96) {
				samplePosition.add("D1");
			}
		}

		return samplePosition;

	}
	
	public static List<String> getLpPlateType(int number) {

		List<String> plateType = new ArrayList<>();

		
		for(int i=1;i<=number;i++) {
			
			if(i<=24) {
				plateType.add("A");	
			}else if(i>24 && i<=48) {
				plateType.add("B");	
			}else if(i>48 && i<=72) {
				plateType.add("C");
			}else if(i>72 && i<=96) {
				plateType.add("D");
			}
		}

		return plateType;

	}

	
	public static void main(String[] args) {
		System.out.println(getRandomValues());
	}
}
