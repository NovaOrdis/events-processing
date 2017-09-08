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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class DefaultOutputFormatFactory implements OutputFormatFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(DefaultOutputFormatFactory.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // OutputFormatFactory implementation ------------------------------------------------------------------------------

    @Override
    public OutputFormat fromArguments(List<String> mutableCommandLineArguments) {

        if (mutableCommandLineArguments == null) {

            throw new IllegalArgumentException("null argument list");
        }

        if (log.isDebugEnabled()) {

            String debug = this + " building output format from: ";
            for(int i = 0; i < mutableCommandLineArguments.size(); i ++) {

                debug += "\"" + mutableCommandLineArguments.get(i) + "\"";

                if (i < mutableCommandLineArguments.size() - 1) {

                    debug += ", ";
                }
            }

            log.debug(debug);
        }

        OutputFormat result;

        if (mutableCommandLineArguments.isEmpty()) {

            result = new DefaultOutputFormat();
        }
        else {

            //
            // we interpret the unqualified arguments as property names
            //

            OutputFormatImpl outputFormat = new OutputFormatImpl();

            for (Iterator<String> i = mutableCommandLineArguments.iterator(); i.hasNext(); ) {

                String propertyIdentifier = i.next(); // may be name, index, etc.

                i.remove();

                //
                // if it can be converted to an int, it is an index
                //

                try {

                    int propertyIndex = Integer.parseInt(propertyIdentifier);
                    outputFormat.addPropertyIndex(propertyIndex);
                    continue; // conversion to index worked, process next ...
                }
                catch (Exception e) {

                    //
                    // that is fine, conversion to integer did not work, handle it as a property name
                    //
                }

                outputFormat.addPropertyName(propertyIdentifier);
            }

            result = outputFormat;
        }

        log.debug(this + " built " + result);

        return result;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return "DefaultOutputFormatFactory[" + Integer.toHexString(System.identityHashCode(this)) + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
