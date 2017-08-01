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

import io.novaordis.events.processing.ProcedureFactory;
import io.novaordis.events.processing.TextOutputProcedureTest;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class OutputTest extends TextOutputProcedureTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    // ProcedureFactory.find() -----------------------------------------------------------------------------------------

    @Test
    @Override
    public void procedureFactoryFind() throws Exception {

        Output p = (Output)ProcedureFactory.find(Output.COMMAND_LINE_LABEL, 0, Collections.emptyList());
        assertNotNull(p);

        //
        // the instance must come with default configuration that should allow it to work correctly (albeit in a
        // simplest possible case)
        //

        OutputStream os = p.getOutputStream();
        assertNotNull(os);
    }

    @Test
    @Override
    public void commandLineLabel() throws Exception {

        Output p = getTextOutputProcedureToTest(true);

        List<String> commandLineLabels = p.getCommandLineLabels();

        assertEquals(1, commandLineLabels.size());
        assertEquals(Output.COMMAND_LINE_LABEL, commandLineLabels.get(0));
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_UninitializedOutput() throws Exception {

        Output o = new Output();
        assertNull(o.getOutputStream());

    }

    @Test
    public void constructor_InitializedOutput() throws Exception {

        OutputStream os = new ByteArrayOutputStream();

        Output o = new Output(os);

        assertEquals(os, o.getOutputStream());
    }

    // output format ---------------------------------------------------------------------------------------------------

    @Test
    public void nonNullOutputFormat() throws Exception {

        Output o = new Output();
        assertNotNull(o.getFormat());
    }


    // getSignature() --------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Output getTextOutputProcedureToTest(boolean initialized) throws Exception {

        Output p = new Output();

        if (initialized) {

            p.setOutputStream(System.out);
        }

        return p;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
