package com.dplabs.utils.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Stream;

public class TimeUnitsBetween {
	
	private Date start;
	private Date end;
	private Holidays holidays;	
	private BusinessHours bHours;

	public TimeUnitsBetween(String _from, String _to, String _pattern) throws ParseException {
		this(new SimpleDateFormat(_pattern).parse(_from), new SimpleDateFormat(_pattern).parse(_to));
	}
		
	public TimeUnitsBetween(Date _start, Date _end) {
		start = _start;
		end = _end;
		if (start == null || end == null || end.before(start)) {
			throw new IllegalArgumentException("Invalid Range of dates");
		}
		holidays = new Holidays() {

			@Override
			public boolean isHoliday(LocalDate _date) {
				return false;
			}
		};
		bHours = new BusinessHours() {

			@Override
			public LocalTime getStartHour() {
				return LocalTime.of(9, 0);
			}

			@Override
			public LocalTime getEndHour() {
				return LocalTime.of(18, 0);
			}

		};
	}

	public  void setHolidays(Holidays _holidays) {
		holidays = _holidays;
	}

	public void setBusinessHours(BusinessHours _bHours) {
		bHours = _bHours;
	}

	public TimeUnits getTimeUnitsElapsed() {

		LocalDateTime from = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault());
		LocalDateTime to = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
		from.truncatedTo(ChronoUnit.MINUTES);
		to.truncatedTo(ChronoUnit.MINUTES);
		
		if (bHours != null) {
			if (from.getHour() < bHours.getStartHour().getHour()) {
				from = from.withHour(bHours.getStartHour().getHour()).withMinute(bHours.getStartHour().getMinute());
			}
			if (to.getHour() > bHours.getEndHour().getHour()) {
				to = to.withHour(bHours.getEndHour().getHour()).withMinute(bHours.getEndHour().getMinute());
			}
		}
		if (holidays != null) {
			while (holidays.isHoliday(from.toLocalDate())) {
				from = from.plusDays(1);
			}
			while (holidays.isHoliday(to.toLocalDate())) {
				to = to.minusDays(1);
			}
		}

		if (from.isAfter(to)) {
			return new TimeUnits(0, 0, 0);
		}
		
		long minutes = 0;
		if (bHours != null) {
			minutes += from.toLocalTime().until(bHours.getEndHour(), ChronoUnit.MINUTES);
			minutes += bHours.getStartHour().until(to.toLocalTime(), ChronoUnit.MINUTES);
		}
		
		LocalDate fromLocal = from.toLocalDate().plusDays(1);
		LocalDate toLocal = to.toLocalDate();
		long days = Stream.iterate(fromLocal, d -> d.plusDays(1))
				.limit(fromLocal.until(toLocal, ChronoUnit.DAYS)).filter(d -> !holidays.isHoliday(d)).count();

		long minutesPerDay = bHours.getStartHour().until(bHours.getEndHour(), ChronoUnit.MINUTES);

		days += minutes / minutesPerDay;
		minutes = minutes % minutesPerDay;
		long hours = minutes / 60;
		minutes = minutes % 60;

		return new TimeUnits(days, hours, minutes);
	}

}
