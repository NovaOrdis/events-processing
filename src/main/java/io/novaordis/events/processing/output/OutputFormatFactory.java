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

import java.util.Iterator;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class OutputFormatFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * The factory method consumes all relevant arguments and removes them from the list.
     *
     * No arguments means DefaultOutputFormat.
     *
     * @exception IllegalArgumentException if the argument list is null.
     *
     */
    public static OutputFormat fromArguments(List<String> mutableCommandLineArguments) {

        if (mutableCommandLineArguments == null) {

            throw new IllegalArgumentException("null argument list");
        }

        if (mutableCommandLineArguments.isEmpty()) {

            return new DefaultOutputFormat();
        }

        //
        // we interpret the unqualified arguments as property names
        //

        OutputFormatImpl outputFormat = new OutputFormatImpl();

        for(Iterator<String> i = mutableCommandLineArguments.iterator(); i.hasNext(); ) {

            String propertyName = i.next();
            i.remove();
            outputFormat.addPropertyName(propertyName);
        }

        return outputFormat;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
