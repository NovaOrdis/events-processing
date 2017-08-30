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

import io.novaordis.events.api.event.GenericEvent;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/30/17
 */
public class NoHeaderOutputStrategyTest extends HeaderOutputStrategyTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void lifecycle() throws Exception {

        NoHeaderOutputStrategy s = getHeaderOutputStrategyToTest();

        GenericEvent e = new GenericEvent();

        assertFalse(s.shouldDisplayHeader(e));

        GenericEvent e2 = new GenericEvent();

        assertFalse(s.shouldDisplayHeader(e2));

        s.headerDisplayed(e2);

        GenericEvent e3 = new GenericEvent();

        assertFalse(s.shouldDisplayHeader(e3));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected NoHeaderOutputStrategy getHeaderOutputStrategyToTest() throws Exception {

        return new NoHeaderOutputStrategy();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
