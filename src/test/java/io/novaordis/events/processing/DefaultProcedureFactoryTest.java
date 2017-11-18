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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import io.novaordis.events.processing.help.Help;

import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class DefaultProcedureFactoryTest extends ProcedureFactoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void helpCommand_Help() throws Exception {

        String arg = "help";

        DefaultProcedureFactory d = getProcedureFactoryToTest();

        List<String> args = new ArrayList<>(Collections.singletonList(arg));

        Procedure procedure = d.find(arg, 1, args);

        assertTrue(procedure instanceof Help);
    }

    @Test
    public void helpCommand_DashHelp() throws Exception {

        String arg = "-help";

        DefaultProcedureFactory d = getProcedureFactoryToTest();

        List<String> args = new ArrayList<>(Collections.singletonList(arg));

        Procedure procedure = d.find(arg, 1, args);

        assertTrue(procedure instanceof Help);
    }

    @Test
    public void helpCommand_DashDashHelp() throws Exception {

        String arg = "--help";

        DefaultProcedureFactory d = getProcedureFactoryToTest();

        List<String> args = new ArrayList<>(Collections.singletonList(arg));

        Procedure procedure = d.find(arg, 1, args);

        assertTrue(procedure instanceof Help);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected DefaultProcedureFactory getProcedureFactoryToTest() throws Exception {

        return new DefaultProcedureFactory(null);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
