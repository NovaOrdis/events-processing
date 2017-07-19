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

import io.novaordis.events.processing.MockEvent;
import io.novaordis.events.processing.ProcedureTest;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class DescribeTest extends ProcedureTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void commandLineLabel() throws Exception {

        Describe d = getProcedureToTest();

        String cll = d.getCommandLineLabel();

        assertEquals(Describe.COMMAND_LINE_LABEL, cll);
    }

    @Test
    public void initializedInstance() throws Exception {

        Describe d = getProcedureToTest();

        try {

            d.process(new MockEvent());
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("incorrectly initialized"));
            assertTrue(msg.contains("no output stream"));
        }
    }

    @Test
    public void happyPath() throws Exception {

        Describe d = getProcedureToTest();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        d.setOutputStream(baos);

        MockEvent me = new MockEvent();

        d.process(me);

        fail("return here, check expected output");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Describe getProcedureToTest() throws Exception {

        return new Describe();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
