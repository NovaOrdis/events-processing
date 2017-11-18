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

package io.novaordis.events.processing.help;

import java.util.Arrays;
import java.util.List;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.Procedure;

/**
 * Help display.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class Help implements Procedure {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COMMAND_LINE_LABEL = "help";
    public static final String COMMAND_LINE_LABEL_2 = "-help";
    public static final String COMMAND_LINE_LABEL_3 = "--help";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public Help() {
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Arrays.asList(
                COMMAND_LINE_LABEL,
                COMMAND_LINE_LABEL_2,
                COMMAND_LINE_LABEL_3);
    }

    @Override
    public void process(Event in) throws EventProcessingException {
        throw new RuntimeException("process() NOT YET IMPLEMENTED");
    }

    @Override
    public void process(List<Event> in) throws EventProcessingException {
        throw new RuntimeException("process() NOT YET IMPLEMENTED");
    }

    @Override
    public long getInvocationCount() {
        throw new RuntimeException("getInvocationCount() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isExitLoop() {
        throw new RuntimeException("isExitLoop() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
