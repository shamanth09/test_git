package com.mrk.myordershop.util;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * An extension of the CustomDateEditor that allows more than 1 format for
 * parsing dates.
 * 
 * @author bchild
 * 
 */
public class ExpandableCustomDateEditor extends PropertyEditorSupport {

	private DateFormat printFormatter;
	private List<DateFormat> parseFormatters;
	private boolean allowEmpty;

	/**
	 * @param printFormatter
	 *            formatter for printing to forms
	 * @param parseFormatters
	 *            formatters to try to parse a date string
	 * @param allowEmpty
	 *            if true, empty values will be parse as null
	 */
	public ExpandableCustomDateEditor(DateFormat printFormatter,
			List<DateFormat> parseFormatters, boolean allowEmpty) {
		this.printFormatter = printFormatter;
		this.parseFormatters = parseFormatters;
		this.allowEmpty = allowEmpty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && StringUtils.isEmpty(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else if (text != null) {

			boolean parseable = false;
			List<String> errors = new ArrayList<String>();
			for (DateFormat formatter : this.parseFormatters) {

				try {
					Date date = formatter.parse(text);
					setValue(date);
					parseable = true;
					break;
				} catch (ParseException e) {
					errors.add(e.getMessage());
				}

			}

			if (!parseable) {
				throw new IllegalArgumentException("Unparseable string: "
						+ errors.toString());
			}

		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? this.printFormatter.format(value) : "");
	}
}