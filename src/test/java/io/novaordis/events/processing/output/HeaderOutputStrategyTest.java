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

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/30/17
 */
public abstract class HeaderOutputStrategyTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldDisplayHeader_NullEvent() throws Exception {

        HeaderOutputStrategy s = getHeaderOutputStrategyToTest();

        try {

            s.shouldDisplayHeader(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null event"));
        }
    }

    @Test
    public void headerDisplayed_NullEvent() throws Exception {

        HeaderOutputStrategy s = getHeaderOutputStrategyToTest();

        try {

            s.headerDisplayed(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null event"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract HeaderOutputStrategy getHeaderOutputStrategyToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
