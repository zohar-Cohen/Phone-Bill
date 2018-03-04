/*  Program that reads input string that contains call durations and phone number.
 *  Creates a sum of call duraitons for each phone number, as we can have multiple lines for each number.
 *  Sort the map and removes the number that has the max duration, and calculates the bill.
 * 
 * 
 * 
 * 
 */



package com.phone.bill;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PhoneBill {

	public int calculateBill(String rawLogData) {

		DateFormat df = new SimpleDateFormat("HH:mm:ss");

		System.out.println("Input Data: " + rawLogData);
		String[] phoneRecords = rawLogData.split("\n");
		Map<Integer, Integer> phoneDurationMap = new HashMap<>();

		// Populate the map with the phone records
		for (String phoneRecord : phoneRecords) {
			String[] s = phoneRecord.split(",");
			Integer phoneNum = Integer.parseInt(s[1].replaceAll("-", ""));
			if (s.length > 1) {
				if (phoneDurationMap.containsKey(phoneNum)) {
					Integer time = phoneDurationMap.get(phoneNum)
							+ convertToSecond(s[0]);
					phoneDurationMap.put(phoneNum, time);

				} else {
					phoneDurationMap.put(phoneNum, convertToSecond(s[0]));

				}
			}
		}
		phoneDurationMap.forEach((k, v) -> System.out.println("Phone number : " + k + " Duration : " + v));
		System.out.println("-----------------------------------------------------------------");
		
		
		//Sort the map according to the longest call duration.
		Map<Integer, Integer> sortedNewMap = phoneDurationMap
				.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.collect(
						Collectors.toMap(Map.Entry::getKey,
								Map.Entry::getValue, (e1, e2) -> e1,
								LinkedHashMap::new));
		
		sortedNewMap.forEach((k, v) -> System.out.println("Phone number : " + k+ " Duration : " + v));
		Integer longestDurationValue = 0;
		if (!sortedNewMap.values().isEmpty()) {
			longestDurationValue = (Integer) sortedNewMap.values().toArray()[0];
			System.out.println(longestDurationValue);
		}
		Map<Integer, Integer> sortedMapByKey = sortedNewMap
				.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(
						Collectors.toMap(Map.Entry::getKey,
								Map.Entry::getValue, (e1, e2) -> e1,
								LinkedHashMap::new));
		sortedMapByKey.forEach((k, v) -> System.out.println("Phone number : " + k
				+ " Duration : " + v));
		sortedMapByKey.values().remove(longestDurationValue);
		System.out
				.println("-----------------------------------------------------------------");
		sortedMapByKey.forEach((k, v) -> System.out.println("Phone number  : " + k
				+ " Duration : " + v));

		Integer total = sortedMapByKey.values().stream()
				.map(v -> costofCall(v)).mapToInt(Integer::intValue).sum();
		System.out.println(total);
		return 0;
	}

	private static Integer costofCall(Integer v) {
		// 3p a minute if under 300s
		if (v < 300) {
			return v * 3;
		} else {
			// else charge per minute for extra time used at 150p for every
			// started minute
			// 360s is 6 minutes
			// 361s is 7 minutes
			Integer minutes = v / 60;
			if ((v % 60) != 0) {
				minutes += 1;
			}
			return 150 * minutes;
		}
	}

	public static void main(String[] args) {

		String input = "01:30:10,400-234-090\n" + "04:21:46,701-080-080\n"
				+ "02:30:05,400-234-090\n" + "02:45:20,301-240-567\n"
				+ "09:01:30,123-456-789\n";
		PhoneBill bill = new PhoneBill();
		bill.calculateBill(input);

	}

	public int convertToSecond(String date) {
		System.out.println("Input time " + date);
		LocalTime localTime = LocalTime.parse(date,
				DateTimeFormatter.ofPattern("HH:mm:ss"));
		int hour = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
		int minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
		int second = localTime.get(ChronoField.SECOND_OF_MINUTE);
		int res = (second + (60 * minute) + (3600 * hour));
		System.out.println("Output time " + res);
		return res;
	}
}
