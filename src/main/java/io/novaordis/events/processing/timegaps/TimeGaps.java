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

package io.novaordis.events.processing.timegaps;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.TextOutputProcedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class TimeGaps extends TextOutputProcedure {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(TimeGaps.class);

    public static final String COMMAND_LINE_LABEL = "time-gaps";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private TimedEvent previous;
    private long maxTimeGap;
    private TimedEvent maxTimeGapFirst;
    private TimedEvent maxTimeGapSecond;

    // Constructors ----------------------------------------------------------------------------------------------------

    public TimeGaps() {

        this(null);
    }

    public TimeGaps(OutputStream os) {

        super(os);
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Collections.singletonList(COMMAND_LINE_LABEL);
    }

    // ProcedureBase implementation ------------------------------------------------------------------------------------

    @Override
    public void process(AtomicLong invocationCount, Event in) throws EventProcessingException {

        if (!(in instanceof TimedEvent)) {

            return;
        }

        TimedEvent te = (TimedEvent)in;

        Long t = te.getTime();

        if (t == null) {

            log.warn("null timestamp time event: " + te);
        }
        else {

            if (previous != null) {

                Long pt = previous.getTime();

                long delta = Math.abs(t - pt);

                if (delta > 3600 * 1000L) {

                    //
                    // ignore
                    //
                    log.warn("ignored delta " + delta + " ms");
                }
                else if (delta > maxTimeGap) {

                    maxTimeGap = delta;
                    maxTimeGapFirst = previous;
                    maxTimeGapSecond = te;

                    try {

                        println(maxTimeGap + " ms, lines " + maxTimeGapFirst.getLineNumber() + ", " + maxTimeGapSecond.getLineNumber());
                        println("line " + maxTimeGapFirst.getLineNumber() + ":");
                        println(maxTimeGapFirst);
                        println("line " + maxTimeGapSecond.getLineNumber() + ":");
                        println(maxTimeGapSecond);

                    }
                    catch(Exception e) {

                        log.warn("failed to write output", e);
                    }
                }
            }

            previous = te;
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
