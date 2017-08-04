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

package io.novaordis.events.processing.count;

import io.novaordis.events.api.event.EndOfStreamEvent;
import io.novaordis.events.api.event.Event;
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.TextOutputProcedure;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A procedure that looks at a stream of incoming events and counts events. The instance is thread safe, and the
 * getCount() invocation correctly returns the number of events counted up to the moment the method was invoked.
 *
 * The EndOfStream event is NOT included in the returned count.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class Count extends TextOutputProcedure {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COMMAND_LINE_LABEL = "count";
    public static final String ABBREVIATED_COMMAND_LINE_LABEL = "-c";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private final AtomicLong count;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Returns an instance that is not initialized with an OutputStream.
     */
    public Count() {

        this(null);
    }

    public Count(OutputStream os) {

        super(os);
        this.count = new AtomicLong(0);
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Arrays.asList(COMMAND_LINE_LABEL, ABBREVIATED_COMMAND_LINE_LABEL);
    }

    // ProcedureBase overrides -----------------------------------------------------------------------------------------

    @Override
    public void process(AtomicLong invocationCount, Event in) throws EventProcessingException {

        if (in instanceof EndOfStreamEvent) {

            //
            // report and exit
            //

            long countValue = count.get();

            try {

                println(countValue);
            }
            catch(IOException e) {

                throw new EventProcessingException(e);
            }
        }
        else {

            count.incrementAndGet();
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public long getCount() {

        return count.get();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
