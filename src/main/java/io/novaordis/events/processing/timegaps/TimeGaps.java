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
import io.novaordis.events.processing.ProcedureBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class TimeGaps extends ProcedureBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(TimeGaps.class);

    public static final String COMMAND_LINE_LABEL = "time-gaps";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private OutputStream os;
    private BufferedWriter bw;

    private TimedEvent previous;
    private long maxTimeGap;
    private TimedEvent maxTimeGapFirst;
    private TimedEvent maxTimeGapSecond;


    // Constructors ----------------------------------------------------------------------------------------------------

    public TimeGaps() {
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Collections.singletonList(COMMAND_LINE_LABEL);
    }

    @Override
    public void process(Event in) throws EventProcessingException {

        invocationCount ++;

        if (bw == null) {

            throw new IllegalStateException("incorrectly initialized instance: no output stream");
        }

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

                    System.out.println(maxTimeGap + " ms, lines " + maxTimeGapFirst.getLineNumber() + ", " + maxTimeGapSecond.getLineNumber());
                    System.out.println("line " + maxTimeGapFirst.getLineNumber() + ":");
                    System.out.println(maxTimeGapFirst);
                    System.out.println("line " + maxTimeGapSecond.getLineNumber() + ":");
                    System.out.println(maxTimeGapSecond);
                }
            }

            previous = te;
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setOutputStream(OutputStream os) {

        if (os == null) {

            throw new IllegalArgumentException("null output stream");
        }

        if (this.os != null) {

            try {

                this.os.close();
            }
            catch(IOException e) {

                String msg = "failed to close the current writer";
                log.warn(msg);
                log.debug(msg, e);
            }
        }

        if (bw != null) {

            try {

                bw.close();
            }
            catch(IOException e) {

                String msg = "failed to close the current writer";
                log.warn(msg);
                log.debug(msg, e);
            }
        }

        this.os = os;
        this.bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    /**
     * May return null.
     */
    public OutputStream getOutputStream() {

        return os;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
