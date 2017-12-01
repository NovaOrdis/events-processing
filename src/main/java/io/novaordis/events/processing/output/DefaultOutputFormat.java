/*
 * Copyright (c) 2017 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.events.processing.output;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.novaordis.events.api.event.Event;

/**
 * A very generic OutputFormat, that is a fall back if nothing more specific is installed. It displays the raw
 * representation of the event, if available, and if not, the timestamp if the event is a timed event, and the event
 * type, as reflected by its class. This is most likely useless in most of the cases, so Output users should replace
 * it with something more useful.
 *
 * For the description of the sequence, see https://kb.novaordis.com/index.php/Events-processing_output#Overview.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class DefaultOutputFormat implements OutputFormat {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final SimpleDateFormat DEFAULT_TIMESTAMP_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");

    public static final String DEFAULT_FIELD_SEPARATOR = ", ";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private DateFormat timestampFormat;

    // Constructors ----------------------------------------------------------------------------------------------------

    public DefaultOutputFormat() {

        setTimestampFormat(DEFAULT_TIMESTAMP_FORMAT);
    }

    // OutputFormat implementation -------------------------------------------------------------------------------------

    @Override
    public String formatHeader(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        //
        // start with preferred implementation
        //

        String s = e.getPreferredRepresentation(DEFAULT_FIELD_SEPARATOR);

        if (s != null) {

            return e.getPreferredRepresentationHeader(DEFAULT_FIELD_SEPARATOR);
        }

        s = e.getRawRepresentation();

        if (s != null) {

            //
            // if we are using the raw representation, there was no parsing and there are no field headers
            //
            return null;
        }

        //
        // do not display timestamp, Output does that by default
        //

        return "event type";
    }

    @Override
    public String format(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        //
        // start with preferred implementation
        //

        String s = e.getPreferredRepresentation(DEFAULT_FIELD_SEPARATOR);

        if (s != null) {

            //
            // the preferred representation is supposed to manage the new lines, so we do not interfere with it - this
            // way it may indicate that it wants nothing displayed, not even a new line
            //

            return s;
        }

        //
        // for raw representation we manage the new lines, the raw representation does NOT include a trailing new line
        //

        s = e.getRawRepresentation();

        if (s == null) {

            //
            // do not display timestamp, Output does that by default
            //

            return e.getClass().getSimpleName() + "\n";
        }

        return s + "\n";
    }

    @Override
    public String getSeparator() {

        return DEFAULT_FIELD_SEPARATOR;
    }

    @Override
    public DateFormat getTimestampFormat() {

        return timestampFormat;
    }

    @Override
    public void setTimestampFormat(DateFormat df) {

        this.timestampFormat = df;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
