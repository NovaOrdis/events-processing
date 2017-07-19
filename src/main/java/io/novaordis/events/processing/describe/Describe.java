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

package io.novaordis.events.processing.describe;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * A procedure that looks at a stream of incoming events, and writes to the given OutputStream a description of distinct
 * events, as they arrive.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class Describe implements Procedure {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(Describe.class);

    public static final String COMMAND_LINE_LABEL = "describe";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private BufferedWriter bw;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Describe() {
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public String getCommandLineLabel() {

        return COMMAND_LINE_LABEL;
    }

    @Override
    public void process(Event in) throws EventProcessingException {

        if (bw == null) {

            throw new IllegalStateException("incorrectly initialized Describe instance: no output stream");
        }

        throw new RuntimeException("process() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setOutputStream(OutputStream os) {

        if (os == null) {

            throw new IllegalArgumentException("null output stream");
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

        bw = new BufferedWriter(new OutputStreamWriter(os));

    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
