/*
 * Copyright 2011-17 Fraunhofer ISE
 *
 * This file is part of jASN1.
 * For more information visit http://www.openmuc.org
 *
 * jASN1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jASN1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jASN1.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jasn1.ber.types;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerTag;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;

public class BerGeneralizedTime extends BerVisibleString {

    private static final long serialVersionUID = 1L;

    public final static BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.PRIMITIVE, BerTag.GENERALIZED_TIME_TAG);

    public BerGeneralizedTime() {
    }

    public BerGeneralizedTime(byte[] value) {
        super(value);
    }

    public BerGeneralizedTime(String valueAsString) {
        super(valueAsString);
    }

    @Override
    public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {

        int codeLength = super.encode(os, false);

        if (withTag) {
            codeLength += tag.encode(os);
        }

        return codeLength;
    }

    @Override
    public int decode(InputStream is, boolean withTag) throws IOException {

        int codeLength = 0;

        if (withTag) {
            codeLength += tag.decodeAndCheck(is);
        }

        codeLength += super.decode(is, false);

        return codeLength;
    }

    /*
     * Generalized time is one of the following (ITU-T X.680 08/2015): YYYYMMDDHH[MM[SS]][.fff] LocalTime
     * YYYYMMDDHH[MM[SS]][.fff]Z UTC YYYYMMDDHH[MM[SS]][.fff]+-HH[MM] local time with time zone Note: the ITU-T X.680
     * (08/2015) standard is not fully supported. The standard specifies that .fff may appear after HH, after MM or
     * after SS.
     * 
     * Regexp: ^ (?<year>\\d{4}) YYYY (?<month>\\d{2}) MM (?<day>\\d{2}) DD (?<hour>\\d{2}) HH ( [MM[SS]]
     * (?<minute>\\d{2}) MM (?<second>\\d{2})? [SS] )? ([.,](?<frac>\\d+))? [.fff] (or [,fff]) (?<timezone> "" or "Z" or
     * "+-HH[MM]" Z | ( "+-HH[MM]" [+-] "+-" \\d{2}(?<tzmin>\\d{2})? HH[MM] ) )? $
     */
    private final static String GENERALIZED_TIME_PATTERN = "^(?<year>\\d{4})(?<month>\\d{2})(?<day>\\d{2})(?<hour>\\d{2})((?<minute>\\d{2})(?<second>\\d{2})?)?([.,](?<frac>\\d+))?(?<timezone>Z|([+-]\\d{2}(?<tzmin>\\d{2})?))?$";

    private final static Pattern generalizedTimePattern = Pattern.compile(GENERALIZED_TIME_PATTERN);

    @SuppressWarnings("WeakerAccess")
    Calendar asCalendar() throws ParseException {

        Matcher matcher = generalizedTimePattern.matcher(toString());

        if (!matcher.find()) {
            throw new ParseException("", 0);
        }

        String mg, mgf;
        int year = Integer.valueOf(matcher.group("year"));
        int month = Integer.valueOf(matcher.group("month"));
        month -= 1; // java.util.Calendar's month goes from 0 to 11
        int day = Integer.valueOf(matcher.group("day"));
        int hour = Integer.valueOf(matcher.group("hour"));

        mg = matcher.group("minute");
        mgf = matcher.group("frac");
        int minute = 0, second = 0, millisec = 0;
        double frac = mgf == null ? 0 : Double.valueOf("0." + mgf);
        if (mg == null) {
            // Missing minutes and seconds
            if (mgf != null) {
                // frac is a fraction of a hour
                minute = (int) (60 * frac);
                second = (int) (60 * 60 * frac) - (minute * 60);
                millisec = (int) (1000 * 60 * 60 * frac) - (minute * 60 * 1000) - (second * 1000);
            }
        }
        else {
            minute = Integer.valueOf(mg);
            mg = matcher.group("second");
            if (mg == null) {
                // Missing seconds
                if (mgf != null) {
                    // frac is a fraction of a minute
                    second = (int) (60 * frac);
                    millisec = (int) (1000 * 60 * frac) - (second * 1000);
                }
            }
            else {
                second = Integer.valueOf(mg);
                if (mgf != null) {
                    // frac is a fraction of a second
                    millisec = (int) (1000 * frac);
                }
            }
        }

        mg = matcher.group("timezone");
        String mgt = matcher.group("tzmin");
        String timeZoneStr = mg == null ? TimeZone.getDefault().getID()
                : (mg.equals("Z") ? "UTC" : (mgt == null ? "GMT" + mg + "00" : "GMT" + mg));
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneStr);

        Calendar calendar = Calendar.getInstance();
        // noinspection MagicConstant
        calendar.set(year, month, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, millisec);
        calendar.setTimeZone(timeZone);

        return calendar;
    }

    Date asDate() throws ParseException {
        return asCalendar().getTime();
    }

}
