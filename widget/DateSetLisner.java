package com.tudaidai.tuantrip.widget;

import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class DateSetLisner implements OnDateSetListener {

	private EditText dView;

	public DateSetLisner(EditText dView) {
		this.dView = dView;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {

		String month11 = new Integer(monthOfYear + 1).toString();
		if ((monthOfYear + 1) < 10)
			month11 = ("0" + month11);
		String day11 = new Integer(dayOfMonth).toString();
		if (dayOfMonth < 10)
			day11 = ("0" + day11);

		dView.setText(year + "-" + month11 + "-" + day11);

	}

}
