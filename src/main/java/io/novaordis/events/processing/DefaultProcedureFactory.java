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

import io.novaordis.events.processing.count.Count;
import io.novaordis.events.processing.describe.Describe;
import io.novaordis.events.processing.exclude.Exclude;
import io.novaordis.events.processing.output.Output;
import io.novaordis.events.processing.timegaps.TimeGaps;
import io.novaordis.utilities.appspec.ApplicationSpecificBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The procedure factory that builds procedures shipped with this package. The factory give the standard procedures
 * a chance to configure themselves with application-specific behavior, if that is possible for a specific procedure.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class DefaultProcedureFactory implements ProcedureFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(DefaultProcedureFactory.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private ApplicationSpecificBehavior applicationSpecificBehavior;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param asb may be null, it won't break anything, but the procedures built by this instance won't be able
     *            to pull application-specific behavior.
     */
    public DefaultProcedureFactory(ApplicationSpecificBehavior asb) {

        this.applicationSpecificBehavior = asb;
    }

    // ProcedureFactory implementation ---------------------------------------------------------------------------------

    @Override
    public Procedure find(String commandLineLabel, int from, List<String> arguments) {

        //
        // TODO: we currently only return the procedures we know of, but this is a hack, use an annotation and
        // annotation scanning instead.
        //

        if (Describe.COMMAND_LINE_LABEL.equals(commandLineLabel)) {

            //
            // unless configured otherwise, write to System.out
            //

            return new Describe(System.out);
        }
        else if (TimeGaps.COMMAND_LINE_LABEL.equals(commandLineLabel)) {

            //
            // unless configured otherwise, write to System.out
            //

            return new TimeGaps(System.out);
        }
        else if (Count.ABBREVIATED_COMMAND_LINE_LABEL.equals(commandLineLabel) ||
                Count.COMMAND_LINE_LABEL.equals(commandLineLabel)) {

            //
            // unless configured otherwise, write to System.out
            //

            return new Count(System.out);
        }
        else if (Exclude.ABBREVIATED_COMMAND_LINE_LABEL.equals(commandLineLabel) ||
                Exclude.COMMAND_LINE_LABEL.equals(commandLineLabel)) {

            //
            // unless configured otherwise, write to System.out
            //

            return new Exclude(System.out);
        }
        else if (Output.COMMAND_LINE_LABEL.equals(commandLineLabel)) {

            //
            // unless configured otherwise, write to System.out
            //

            return new Output(System.out, applicationSpecificBehavior, from, arguments);
        }
        else {

            log.debug("unknown command line label: \"" + commandLineLabel + "\"");
        }

        return null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
