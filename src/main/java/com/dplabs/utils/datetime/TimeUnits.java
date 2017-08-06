package com.dplabs.utils.datetime;

public class TimeUnits {
	long days;
	long hours;
	long minutes;

	public TimeUnits(long _days, long _hours, long _minutes) {
		days = _days;
		hours = _hours;
		minutes = _minutes;
	}

	public long getDays() {
		return days;
	}

	public long getMinutes() {
		return minutes;
	}

	public long getHours() {
		return hours;
	}

	public TimeUnits withHours(long _hours) {
		hours += _hours;
		return this;
	}

	public TimeUnits withMinutes(long _minutes) {
		minutes += _minutes;
		return this;
	}

	public TimeUnits withDays(long _days) {
		days += _days;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TimeUnits) {
			TimeUnits other = (TimeUnits) obj;
			return other.getDays() == getDays() && other.getHours() == getHours() && other.getMinutes() == getMinutes();
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "days: " + days + "; Hours: " + hours + "; Minutes: " + minutes + ";";
	}
}
