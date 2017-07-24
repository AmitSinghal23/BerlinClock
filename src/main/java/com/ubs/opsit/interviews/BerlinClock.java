package com.ubs.opsit.interviews;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BerlinClock implements TimeConverter {
	
	@Override
	public String convertTime(String aTime) {
		validateTime(aTime);
		String timeArray[] = this.convertBerlinTimeArray(aTime);

		String joined = Stream.of(timeArray).collect(Collectors.joining("\r\n"));

		return joined;
	}
	
	private void validateTime(String aTime){
		TimeValidator validator = new TimeValidator();
		validator.parse(aTime);
	}

	private String[] convertBerlinTimeArray(String timeToConvert) {
		int[] parts = Stream.of(timeToConvert.split(":")).mapToInt(Integer::parseInt).toArray();

		return new String[] { getSeconds(parts[2]), getFirstHours(parts[0]), getSecondHours(parts[0]),
				getFirstMinutes(parts[1]), getSecondMinutes(parts[1]) };
	}

	private String getSeconds(int number) {
		if (number % 2 == 0) {
			return "Y";
		} else {
			return "O";
		}
	}

	private String getFirstHours(int number) {
		return getSetUnsetFlag(4, getFirstSets(number));
	}

	private String getSecondHours(int number) {
		return getSetUnsetFlag(4, number % 5);
	}

	private String getFirstMinutes(int number) {
		String firstMinutes = getOnOff(11, getFirstSets(number), "Y");
		return firstMinutes.replaceAll("YYY", "YYR");
	}

	private String getSecondMinutes(int number) {
		return getOnOff(4, number % 5, "Y");
	}

	private String getSetUnsetFlag(int lamps, int onSigns) {
		return getOnOff(lamps, onSigns, "R");
	}

	private String getOnOff(int lamps, int onSigns, String onVal) {
		String value = "";
		for (int i = 0; i < onSigns; i++) {
			value += onVal;
		}
		for (int i = 0; i < (lamps - onSigns); i++) {
			value += "O";
		}
		return value;
	}

	private int getFirstSets(int number) {
		return (number - (number % 5)) / 5;
	}

}

class TimeValidator {
    private final String[] patterns = {"HH:mm:ss"};

    public TimeValidator(){
    }

    public LocalTime parse(String text){
        for(int i = 0; i < patterns.length; i++){
            try{
                return LocalTime.parse(text, DateTimeFormatter.ofPattern(patterns[i]));
            }catch(DateTimeParseException excep){}
        }
        throw new IllegalArgumentException("Time should be in HH:mm:ss format");
    }
}
