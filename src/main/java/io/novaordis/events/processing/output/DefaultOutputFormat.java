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

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.TimedEvent;

import java.text.SimpleDateFormat;

/**
 * A very generic OutputFormat, that is a fall back if nothing more specific is installed. It displays the raw
 * representation of the event, if available, and if not, the timestamp if the event is a timed event, and the event
 * type, as reflected by its class. This is most likely useless in most of the cases, so Output users should replace
 * it with something more useful.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class DefaultOutputFormat implements OutputFormat {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final SimpleDateFormat DEFAULT_TIMESTAMP_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // OutputFormat implementation -------------------------------------------------------------------------------------

    @Override
    public String format(Event e) {

        if (e == null) {

            return "null";
        }

        String s = e.getRawRepresentation();

        if (s != null) {

            return s;
        }

        if (e instanceof TimedEvent) {

            long time = ((TimedEvent)e).getTime();
            s = DEFAULT_TIMESTAMP_FORMAT.format(time);
        }

        String type = e.getClass().getSimpleName();

        if (s == null) {

            return type;
        }
        else {

            return s + " " + type;
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
