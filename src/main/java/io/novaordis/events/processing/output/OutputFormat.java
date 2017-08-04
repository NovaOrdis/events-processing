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

/**
 * Encapsulates the output format specification and formats events according to the specification.
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
     * @return a header line, with its distinct elements separated by the sequence of character given by getSeparator()
     *
     * The implementation has the choice of not providing a header, in which case this method will return null. The
     * calling layer must take this into account and not display anything as header, if a header is not provided.
     */
    String getHeader();

    /**
     * Format the event according to the specified format. If none of the format criteria match, the result is null,
     * which is an indication to the calling layer that the event does not match the format. It is up to the calling
     * layer to decide what to do - print noting, print empty line or whatever else it wants.
     *
     * The result may or may not start with a timestamp, depending on the underlying implementation and/or format.
     * The instance must indicate whether the representation starts or not with a timestamp via method.
     *
     * @exception IllegalArgumentException if the event is null.
     *
     * @see OutputFormat#isLeadingTimestamp()
     */
    String format(Event e);

    /**
     * @return true if the representation returned by format contains a leading timestamp.
     */
    boolean isLeadingTimestamp();

    /**
     * The field separator.
     */
    String getSeparator();

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
