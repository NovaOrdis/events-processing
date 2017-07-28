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

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/28/17
 */
public abstract class TextOutputProcedureTest extends ProcedureTest {


    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // println() -------------------------------------------------------------------------------------------------------

    @Test
    public void println() throws Exception {

        TextOutputProcedure p = getTextOutputProcedureToTest();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        p.setOutputStream(baos);

        p.println("something");

        //
        // this should also flush
        //

        assertEquals("something\n", new String(baos.toByteArray()));

        p.println("else");

        assertEquals("something\nelse\n", new String(baos.toByteArray()));

    }

    @Test
    public void println_Null() throws Exception {

        TextOutputProcedure p = getTextOutputProcedureToTest();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        p.setOutputStream(baos);

        p.println(null);

        //
        // this should also flush
        //

        assertEquals("null\n", new String(baos.toByteArray()));
    }

    @Test
    public void println_InstanceNotInitialized() throws Exception {

        TextOutputProcedure p = getTextOutputProcedureToTest(false);

        assertNull(p.getOutputStream());

        try {

            p.println("something");
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("was not initialized: no output stream"));

        }
    }

    // setOutputStream() -----------------------------------------------------------------------------------------------

    @Test
    public void setOutputStream_Null() throws Exception {

        TextOutputProcedure p = getTextOutputProcedureToTest();

        try {

            p.setOutputStream(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null output stream"));
        }
    }

    @Test
    public void setOutputStream() throws Exception {

        TextOutputProcedure p = getTextOutputProcedureToTest();

        OutputStream os = p.getOutputStream();

        assertNotNull(os);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        p.setOutputStream(baos);

        assertTrue(baos.equals(p.getOutputStream()));

        p.println("red");

        //
        // replace the underlying stream
        //

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

        p.setOutputStream(baos2);

        assertTrue(baos2.equals(p.getOutputStream()));

        assertEquals("red\n", new String(baos.toByteArray()));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Procedure getProcedureToTest() throws Exception {

        return getTextOutputProcedureToTest();
    }

    /**
     * @return an initialized (with an OutputStream) instance
     */
    protected TextOutputProcedure getTextOutputProcedureToTest() throws Exception {

        return getTextOutputProcedureToTest(true);
    }

    /**
     * @param initialized whether the instance should be initialized with an OutputStream or not.
     */
    protected abstract TextOutputProcedure getTextOutputProcedureToTest(boolean initialized) throws Exception;


    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
