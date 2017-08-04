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
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.TextOutputProcedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The default procedure to handle event streams: the procedure inspects the events and sends their string
 * representation to the configured output stream.
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

    private OutputFormat format;

    private DateFormat timestampFormat;

    private boolean outputHeader;

    private String headerMarker;

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

        this(os, 0, commandlineArguments);
    }

    public Output(OutputStream os, int from, List<String> commandlineArguments) {

        if (os != null) {

            setOutputStream(os);
        }

        setTimestampFormat(DefaultOutputFormat.DEFAULT_TIMESTAMP_FORMAT);
        configureFromCommandLine(from, commandlineArguments);

        //
        // we do output headers, by default
        //

        this.outputHeader = true;
        this.headerMarker = "# ";

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

            String header = null;

            String s = format.format(in);

            if (s == null) {

                if (log.isDebugEnabled()) {

                    log.debug(this + "'s output format did not match the event, ignoring ...");
                }

                return;
            }


            if (outputHeader) {

                header = format.getHeader(in);

                //
                // turn off header request, so the next events will be displayed without header lines
                //
                outputHeader = false;
            }

            //
            // unless the format already added a timestamp at the beginning of the line, start the line with a timestamp
            //

            if (!format.isLeadingTimestamp() && in instanceof TimedEvent) {

                Long timestamp = ((TimedEvent)in).getTime();

                if (header != null) {

                    header = headerMarker + TimedEvent.TIMESTAMP_PROPERTY_NAME + format.getSeparator() + header;
                }

                String sTimestamp = " ";

                if (timestamp != null) {

                    sTimestamp = timestampFormat.format(timestamp);
                }

                s = sTimestamp + format.getSeparator() + s;
            }

            if (header != null) {

                println(header);
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

    /**
     * @return true if the next line to be sent to the output stream is a header line, false otherwise.
     */
    public boolean isOutputHeader() {

        return outputHeader;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Process the command line argument list and remove arguments if recognized as our own.
     */
    void configureFromCommandLine(int from, List<String> mutableCommandLineArgumentsList) {

        List<String> outputFormatArgs = new ArrayList<>();

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

                outputFormatArgs.add(arg);
                si.remove();
                continue;
            }

            if (OUTPUT_FORMAT_OPTION.equals(arg)) {

                si.remove();
                collect = true;

            }
        }

        OutputFormat f = OutputFormatFactory.fromArguments(outputFormatArgs);
        setOutputFormat(f);
    }

    void setOutputFormat(OutputFormat format) {

        this.format = format;
    }

    void setTimestampFormat(DateFormat df) {

        if (df == null) {

            throw new IllegalArgumentException("null timestamp format");
        }

        this.timestampFormat = df;
    }

    /**
     * If set to true, the instance will output a header on first event that matches the format, then reset the flag.
     * The flag can then be set again, to get a new header.
     */
    void setOutputHeader(boolean b) {

        this.outputHeader = b;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
