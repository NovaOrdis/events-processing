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

package io.novaordis.events.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * A procedure that looks at a stream of events as they arrive and text at a configurable output stream.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/28/17
 */
public abstract class TextOutputProcedure extends ProcedureBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(TextOutputProcedure.class);

    private static final String NULL = "null";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private OutputStream os;
    private BufferedWriter bw;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Builds an uninitialized procedure.
     */
    protected TextOutputProcedure() {

        this(null);
    }

    /**
     * @param os may be null, in which case the instance is not initialized.
     */
    protected TextOutputProcedure(OutputStream os) {

        if (os != null) {

            setOutputStream(os);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * May return null if the instance was not initialized.
     */
    public OutputStream getOutputStream() {

        return os;
    }

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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * API for subclasses to use when they need to output text.
     *
     * Converts the given object to string (or to the "null" string, if the object is null) and sends the text to the
     * underlying output stream, flushing it after that.
     *
     * @exception IllegalStateException if an OutputStream was not installed.
     */
    protected void println(Object o) throws IOException {

        if (bw == null) {

            throw new IllegalStateException(this + " was not initialized: no output stream");
        }

        String s = o == null ? NULL : o.toString();

        bw.write(s);
        bw.newLine();
        bw.flush();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
