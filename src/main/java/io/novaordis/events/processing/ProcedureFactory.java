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

import io.novaordis.events.processing.describe.Describe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class ProcedureFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(ProcedureFactory.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Instantiates the procedure with the given command line label. The instance must come with default configuration
     * that should allow it to work correctly (albeit in the simplest possible case)
     *
     * @return null if no such procedure is found in classpath.
     */
    public static Procedure find(String commandLineLabel) {

        //
        // TODO: we currently only return the procedures we know of, but this is a hack, use an annotation and
        // annotation scanning instead.
        //

        if (Describe.COMMAND_LINE_LABEL.equals(commandLineLabel)) {

            Describe d = new Describe();

            //
            // unless configured otherwise, write to System.out
            //

            d.setOutputStream(System.out);
            return d;
        }
        else {

            log.debug("unknown command line label: \"" + commandLineLabel + "\"");
        }

        return null;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
