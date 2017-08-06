package com.dplabs.utils.datetime;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

public class TimeUnitsBetweenTest {

	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	@Test
	public void testGetHoursElapsed() throws ParseException {

		Holidays holidays = new Holidays() {

			@Override
			public boolean isHoliday(LocalDate _date) {
				return _date.getDayOfWeek() == DayOfWeek.SATURDAY || _date.getDayOfWeek() == DayOfWeek.SUNDAY;
			}
		};

		String from = "2017-08-01 07:00:00";
		String to = "2017-08-07 21:00:00";
		TimeUnitsBetween timeUnits = new TimeUnitsBetween(from, to, DATE_PATTERN);
		timeUnits.setHolidays(holidays);
		Assert.assertEquals(new TimeUnits(5, 0, 0), timeUnits.getTimeUnitsElapsed());

		from = "2017-08-01 09:35:00";
		to = "2017-08-07 21:00:00";
		timeUnits = new TimeUnitsBetween(from, to, DATE_PATTERN);
		timeUnits.setHolidays(holidays);
		Assert.assertEquals(new TimeUnits(4, 8, 25), timeUnits.getTimeUnitsElapsed());

		from = "2017-08-01 09:35:00";
		to = "2017-08-07 16:12:00";
		timeUnits = new TimeUnitsBetween(from, to, DATE_PATTERN);
		timeUnits.setHolidays(holidays);
		Assert.assertEquals(new TimeUnits(4, 6, 37), timeUnits.getTimeUnitsElapsed());
	}

}
