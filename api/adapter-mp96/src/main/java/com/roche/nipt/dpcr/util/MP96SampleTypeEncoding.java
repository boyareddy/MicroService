package com.roche.nipt.dpcr.util;

public enum MP96SampleTypeEncoding {

	STANDARD("STANDARD"), EXPRESS("EXPRESS"), DUO("DUO"), FLEX("FLEX"), ADVANCED("ADVANCED");

	private MP96SampleTypeEncoding(String value) {
		this.value = value;
	}

	private String value;

	public static MP96SampleTypeEncoding findByValue(String value) {
		MP96SampleTypeEncoding[] values = MP96SampleTypeEncoding.values();
		for (MP96SampleTypeEncoding encoding : values) {
			if (encoding.value.equalsIgnoreCase(value))
				return encoding;
		}
		return null;
	}

	public static MP96SampleTypeEncoding fromValue(String value) {
		MP96SampleTypeEncoding[] values = MP96SampleTypeEncoding.values();
		for (MP96SampleTypeEncoding encoding : values) {
			if (encoding.value.equalsIgnoreCase(value))
				return encoding;
		}
		return null;
	}

	public static String getEncodingValue(String value) {
		MP96SampleTypeEncoding[] values = MP96SampleTypeEncoding.values();
		for (MP96SampleTypeEncoding encoding : values) {
			if (encoding.value.equalsIgnoreCase(value))
				return encoding.value;
		}
		return "UnKnown";
	}

	public static String findByKey(MP96SampleTypeEncoding key) {
		MP96SampleTypeEncoding[] values = MP96SampleTypeEncoding.values();
		for (MP96SampleTypeEncoding encoding : values) {
			if (encoding == key)
				return encoding.value;
		}
		return "UnKnown";
	}

}
