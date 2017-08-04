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

package io.novaordis.events.processing.exclude;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.TextOutputProcedure;
import io.novaordis.events.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A procedure that looks at a stream of incoming events, and writes to the given OutputStream the raw represenation
 * of the events that DO NOT match the query.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class Exclude extends TextOutputProcedure {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(Exclude.class);

    public static final String COMMAND_LINE_LABEL = "exclude";
    public static final String ABBREVIATED_COMMAND_LINE_LABEL = "-x";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Query query;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Exclude() {

        this(null);
    }

    public Exclude(OutputStream os) {

        super(os);
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Arrays.asList(COMMAND_LINE_LABEL, ABBREVIATED_COMMAND_LINE_LABEL);
    }

    // ProcedureBase implementation ------------------------------------------------------------------------------------

    @Override
    public void process(AtomicLong invocationCount, Event in) throws EventProcessingException {

        try {

            if (query.selects(in)) {

                return;
            }
        }
        catch(NullPointerException e) {

            throw new IllegalStateException(this + " was not initialized: no query", e);
        }

        String s = in.getRawRepresentation();

        if (s == null) {

            s = in.toString();
        }

        try {

            println(s);
        }
        catch(IOException e) {

            log.warn("failed to print event", e);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setQuery(Query query) {

        if (query == null) {

            throw new IllegalArgumentException("null query");
        }

        this.query = query;
    }

    public Query getQuery() {

        return query;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
