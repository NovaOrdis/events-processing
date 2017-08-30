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

import java.text.DateFormat;

/**
 * Encapsulates the output format specification of the "output" procedure and it formats handed-over events according to
 * the contained specification, by producing Strings that are further handled by the "output" procedure, presumably
 * by sending them to the output stream.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public interface OutputFormat {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return a header line that corresponds to the given event, with its distinct elements separated by getSeparator()
     * sequence of characters. The header line starts with a header marker. The event is needed as an argument because
     * different outputs are possible for TimedEvents vs. non-TimedEvents.
     *
     * The implementation must never return null, at minimum it must return the header separator.
     */
    String formatHeader(Event e);

    /**
     * Format the event according to the specified format. If none of the format criteria match, the result is null,
     * which is an indication to the calling layer that the event does not match the format. It is up to the calling
     * layer to decide what to do - print noting, print empty line or whatever else it wants.
     *
     * The result may or may not start with a timestamp, depending on the underlying implementation and/or format.
     * The instance must indicate whether the representation starts or not with a timestamp via method.
     *
     * @exception IllegalArgumentException if the event is null.
     */
    String format(Event e);

    /**
     * The field separator.
     */
    String getSeparator();

    /**
     * @return the DateFormat used to format timestamps.
     */
    DateFormat getTimestampFormat();

    /**
     * Set the DateFormat used to format timestamps.
     */
    void setTimestampFormat(DateFormat df);

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
