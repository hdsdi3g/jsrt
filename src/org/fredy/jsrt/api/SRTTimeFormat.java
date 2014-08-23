/* 
 * Copyright 2012 Fredy Wijaya
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.fredy.jsrt.api;

/**
 * This class contains utility methods for SRT time format related stuff.
 * @author fredy
 */
public class SRTTimeFormat {
	public static final String TIME_DELIMITER = " --> ";
	
	private SRTTimeFormat() {
	}
	
	/**
	 * Formats the date into SRT time format.
	 */
	public static String format(float value) {
		StringBuffer sb = new StringBuffer();
		
		float hrs = (float) Math.floor((float) value / 3600f);
		if (hrs < 10) {
			sb.append(0);
		}
		sb.append((int) Math.floor(hrs));
		sb.append(":");
		
		float _diff_hours = (float) value / 3600f; // en heures,minutes
		int diff_hours = (int) Math.floor(_diff_hours); // en heures
		float min = ((float) _diff_hours - (float) diff_hours) * 60f;
		if (min < 10) {
			sb.append(0);
		}
		sb.append((int) Math.floor(min));
		sb.append(":");
		
		int secresult = (int) Math.floor((min - Math.floor(min)) * (float) 60);
		float frmresult = (value - (float) Math.floor(value)) * 1000;
		
		if (secresult < 10) {
			sb.append(0);
		}
		if (Math.round(frmresult) == 1000) {
			secresult++;
		}
		sb.append(secresult);
		sb.append(",");
		
		if (Math.floor(frmresult) < 100) {
			sb.append(0);
		}
		if (Math.floor(frmresult) < 10) {
			sb.append(0);
		}
		if (Math.round(frmresult) == 1000) {
			sb.append("000");
		} else {
			sb.append(Math.round(frmresult));
		}
		
		return sb.toString();
	}
	
	/**
	 * Parses the SRT time format into date.
	 */
	public static float parse(String srtTime) throws NumberFormatException {
		srtTime = srtTime.trim();
		String[] blockL = srtTime.split(":");
		String[] blockR = blockL[2].split(",");
		
		float result = 0f;
		result = 3600 * Integer.parseInt(blockL[0]);
		result = result + 60 * Integer.parseInt(blockL[1]);
		result = result + Integer.parseInt(blockR[0]);
		result = result + ((float) Integer.parseInt(blockR[1])) / 1000f;
		
		return result;
	}
}
