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

import io.novaordis.events.api.event.EndOfStreamEvent;
import io.novaordis.events.api.event.Event;
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.TextOutputProcedure;
import io.novaordis.utilities.appspec.ApplicationSpecificBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The procedure inspects the events and sends their string representation, and optionally corresponding headers, to
 * the configured output stream. The formatting and header generation is delegated to the embedded OutputFormat
 * instance. The OutputFormat instance drives header logic generation, the Output procedure will displays whatever
 * the OutputFormat decides.
 *
 * More details: https://kb.novaordis.com/index.php/Events-processing_output#Overview
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class Output extends TextOutputProcedure {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COMMAND_LINE_LABEL = "output";

    public static final String OUTPUT_FORMAT_OPTION = "-o";

    private static final Logger log = LoggerFactory.getLogger(Output.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private HeaderOutputStrategy headerOutputStrategy;

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

        this(os, 0, commandlineArguments, null);
    }

    /**
     * @param asb may be null, it won't break anything, but this procedure won't be able to pull application-specific
     *            behavior.
     */
    public Output(OutputStream os, int from, List<String> commandlineArguments, ApplicationSpecificBehavior asb) {

        if (os != null) {

            setOutputStream(os);
        }

        configureFromCommandLine(from, commandlineArguments);

        this.headerOutputStrategy = new DefaultHeaderOutputStrategy();
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Collections.singletonList(COMMAND_LINE_LABEL);
    }

    // ProcedureBase implementation ------------------------------------------------------------------------------------

    /**
     * https://kb.novaordis.com/index.php/Events-processing_output#Overview
     */
    @Override
    public void process(AtomicLong invocationCount, Event in) throws EventProcessingException {

        if (log.isDebugEnabled()) { log.debug(this + " got " + in); }

        if (in instanceof EndOfStreamEvent) {

            //
            // we don't do anything special on EndOfStream
            //

            if (log.isDebugEnabled()) { log.debug(this + " got EndOfStreamEvent"); }
            return;
        }

        try {

            if (headerOutputStrategy.shouldDisplayHeader(in)) {

                String header = format.formatHeader(in);

                if (header != null) {

                    println(header);

                    headerOutputStrategy.headerDisplayed(in);
                }
            }

            String s = format.format(in);

            if (s == null) {

                if (log.isDebugEnabled()) {

                    log.debug(this + "'s output format did not match the event, ignoring ...");
                }

                return;
            }

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

    public HeaderOutputStrategy getHeaderOutputStrategy() {

        return headerOutputStrategy;
    }

    public void setHeaderOutputStrategy(HeaderOutputStrategy s) {

        if (s == null) {

            throw new IllegalArgumentException("null header output strategy");
        }

        this.headerOutputStrategy = s;
    }

    // Package protected static ----------------------------------------------------------------------------------------

    static String cleanCommas(String argument) {

        if (argument == null) {

            return null;
        }

        argument = argument.trim();

        int i;

        for(i = 0; i < argument.length(); i ++) {

            if (argument.charAt(i) != ',') {

                break;
            }
        }

        if (i >= argument.length()) {

            return null;
        }

        int j;

        for(j = argument.length() - 1; j >= 0; j --) {

            if (argument.charAt(j) != ',') {

                break;
            }
        }

        return argument.substring(i, j + 1);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Process the command line argument list and remove arguments if recognized as our own.
     */
    void configureFromCommandLine(int from, List<String> mutableCommandLineArgumentsList) {

        //
        // tokenized output format arguments, with separators removed
        //
        List<String> outputFormatArgsWithSeparatorsRemoved = new ArrayList<>();

        //
        // scan the argument list until we find the output format command line option
        //

        boolean collect = false;
        int i = 0;

        for(Iterator<String> si = mutableCommandLineArgumentsList.iterator(); si.hasNext(); i ++) {

            String arg = si.next();

            if (i < from) {

                //
                // skip
                //

                continue;
            }

            if (collect) {

                si.remove();

                arg = cleanCommas(arg);

                if (arg == null) {

                    continue;
                }

                outputFormatArgsWithSeparatorsRemoved.add(arg);
                continue;
            }

            if (OUTPUT_FORMAT_OPTION.equals(arg)) {

                si.remove();
                collect = true;
            }
        }

        OutputFormat f = OutputFormatFactory.fromArguments(outputFormatArgsWithSeparatorsRemoved);
        setOutputFormat(f);
    }

    void setOutputFormat(OutputFormat format) {

        this.format = format;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
