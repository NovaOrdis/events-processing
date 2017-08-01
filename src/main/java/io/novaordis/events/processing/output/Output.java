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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The default procedure to handle event streams: the procedure inspects the events and sends their string
 * representation to the configured output stream.
 *
 * More details: https://kb.novaordis.com/index.php/Event-processing_output
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class Output extends TextOutputProcedure {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COMMAND_LINE_LABEL = "output";

    public static final String OUTPUT_FORMAT_OPTION = "-o";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private OutputFormat format;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Uninitialized output.
     */
    public Output() {

        this(null);
    }

    public Output(OutputStream os) {

        this(os, Collections.emptyList());
    }

    public Output(OutputStream os, List<String> commandlineArguments) {

        if (os != null) {

            setOutputStream(os);
        }

        configureFromCommandLine(commandlineArguments);
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Collections.singletonList(COMMAND_LINE_LABEL);
    }

    @Override
    public void process(Event in) throws EventProcessingException {

        invocationCount ++;

        try {

            String s = format.format(in);
            println(s);
        }
        catch(Exception e) {

            throw new EventProcessingException(e);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Never returns null.
     */
    public OutputFormat getFormat() {

        return format;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Process the command line argument list and remove arguments if recognized as our own.
     */
    void configureFromCommandLine(List<String> mutableCommandLineArgumentsList) {

        List<String> outputFormatArgs = new ArrayList<>();

        //
        // scan the argument list until we find the output format command line option
        //

        boolean collect = false;

        for(Iterator<String> i = mutableCommandLineArgumentsList.iterator(); i.hasNext(); ) {

            String arg = i.next();

            if (collect) {

                outputFormatArgs.add(arg);
                i.remove();
                continue;
            }

            if (OUTPUT_FORMAT_OPTION.equals(arg)) {

                i.remove();
                collect = true;

            }
        }

        this.format = OutputFormatFactory.fromArguments(outputFormatArgs);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
