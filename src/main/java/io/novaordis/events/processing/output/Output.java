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
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.TextOutputProcedure;

import java.util.Collections;
import java.util.List;

/**
 * The default procedure to handle event streams: the procedure inspects the events and sends their string
 * representation to the configured output stream.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class Output extends TextOutputProcedure {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COMMAND_LINE_LABEL = "output";

    // public static final SimpleDateFormat TIMESTAMP_OUTPUT_FORMAT = new SimpleDateFormat("MM/dd/YY HH:mm:ss,SSS");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Collections.singletonList(COMMAND_LINE_LABEL);
    }

    @Override
    public void process(Event in) throws EventProcessingException {

        invocationCount ++;

        try {

            println(in);
        }
        catch(Exception e) {

            throw new EventProcessingException(e);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}