package com.java.mag;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

public class DateTemporalAdjust {


	public static void main(String[] args) {
		TemporalAdjuster friday13thAdjuster = temporal -> {
			// adjust if we have already crossed 13 of current month.
			if (temporal.get(ChronoField.DAY_OF_MONTH) > 13) {
				temporal = temporal.plus(1, ChronoUnit.MONTHS);
			}

			temporal = temporal.with(ChronoField.DAY_OF_MONTH, 13);
			while (temporal.get(ChronoField.DAY_OF_WEEK) != DayOfWeek.FRIDAY.getValue()) {
				temporal = temporal.plus(1, ChronoUnit.MONTHS);
			}

			return temporal;
		};

		LocalDate today = LocalDate.of(2016, 5, 14);
		LocalDate nextfriday13th = (LocalDate) friday13thAdjuster.adjustInto(today);
		System.out.println(nextfriday13th);
	}
}
