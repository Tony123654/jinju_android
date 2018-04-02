package com.jinju.android.constant;

import com.jinju.android.R;

import java.util.ArrayList;
import java.util.List;

public final class TransType {

	public static final String IN = "in";
	public static final String OUT = "out";
	public static final String OTHER = "other";                                                                    

	public static final List<String> VALUES = new ArrayList<String>();
	public static final List<Integer> COLOR_RESOURCES = new ArrayList<Integer>();

	static {
		VALUES.add(IN);
		VALUES.add(OUT);
		VALUES.add(OTHER);

		COLOR_RESOURCES.add(R.color.main_red);
		COLOR_RESOURCES.add(R.color.word_black);
		COLOR_RESOURCES.add(R.color.word_black);
	}

	public static int getResColorByValue(String value) {
		for (int i = 0; i < VALUES.size(); i++)
			if (VALUES.get(i).equals(value))
				return COLOR_RESOURCES.get(i);

		return 0;
	}

}