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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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
    private PrintWriter pw;

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

        if (pw != null) {

            pw.close();
        }

        this.os = os;
        this.pw = new PrintWriter(new OutputStreamWriter(os));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * API for subclasses to use when they need to output text.
     *
     * Converts the given object to string (or to the "null" string, if the object is null) and sends the text to the
     * underlying output stream, followed by a new line, flushing it after that.
     *
     * @exception IllegalStateException if an OutputStream was not installed.
     */
    protected void println(Object o) throws IOException {

        print(o);
        pw.println();
        pw.flush();
    }

    /**
     * API for subclasses to use when they need to output text.
     *
     * Converts the given object to string (or to the "null" string, if the object is null) and sends the text to the
     * underlying output stream, flushing it after that.
     *
     * @exception IllegalStateException if an OutputStream was not installed.
     */
    protected void print(Object o) throws IOException {

        insureInitialized();

        String s = o == null ? NULL : o.toString();

        pw.print(s);
        pw.flush();
    }

    /**
     * API for subclasses to use when they need to output text. Convenience method for rendering new lines.
     *
     * @exception IllegalStateException if an OutputStream was not installed.
     */
    protected void println() throws IOException {

        insureInitialized();
        pw.println();
        pw.flush();
    }

    /**
     * API for subclasses to use when they need to output text. Offers printf() semantics for sending ouptut to
     * the underlying output stream.
     *
     * https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#syntax
     *
     * Sends the given argument to the underlying stream via printf() and flushes the stream after that.
     *
     * @exception IllegalStateException if an OutputStream was not installed.
     */
    protected void printf(String format, Object o) throws IOException {

        insureInitialized();

        pw.printf(format, o);
        pw.flush();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    private void insureInitialized() {

        if (pw == null) {

            throw new IllegalStateException(this + " was not initialized: no output stream");
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
