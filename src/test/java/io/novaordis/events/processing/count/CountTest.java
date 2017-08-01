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

import io.novaordis.events.processing.MockEvent;
import io.novaordis.events.processing.ProcedureFactory;
import io.novaordis.events.processing.ProcedureTest;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class CountTest extends ProcedureTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void procedureFactoryFind() throws Exception {

        Count p = (Count)ProcedureFactory.find(Count.COMMAND_LINE_LABEL, 0, Collections.emptyList());
        assertNotNull(p);

        Count p2 = (Count)ProcedureFactory.find(Count.ABBREVIATED_COMMAND_LINE_LABEL, 0, Collections.emptyList());
        assertNotNull(p2);
    }

    @Test
    @Override
    public void commandLineLabel() throws Exception {

        Count d = getProcedureToTest();

        List<String> commandLineLabels = d.getCommandLineLabels();
        assertEquals(2, commandLineLabels.size());
        assertTrue(commandLineLabels.contains(Count.COMMAND_LINE_LABEL));
        assertTrue(commandLineLabels.contains(Count.ABBREVIATED_COMMAND_LINE_LABEL));
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void count() throws Exception {

        Count d = getProcedureToTest();

        assertEquals(0L, d.getCount());

        MockEvent me = new MockEvent();

        d.process(me);

        assertEquals(1L, d.getCount());

        MockEvent me2 = new MockEvent();

        d.process(me2);

        assertEquals(2L, d.getCount());
    }
    
    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Count getProcedureToTest() throws Exception {

        return new Count();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
