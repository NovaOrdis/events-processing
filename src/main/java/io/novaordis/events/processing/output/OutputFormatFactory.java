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

import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/30/17
 */
public interface OutputFormatFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The factory method consumes all relevant arguments and removes them from the list.
     *
     * No arguments means DefaultOutputFormat.
     *
     * @param mutableCommandLineArguments the output format arguments as extracted from command line, but with
     *                                    separators removed. At this level, we assume that the separators have been
     *                                    cleaned by the upper layer. The arguments pertaining to the output format
     *                                    may be property names, property indices, etc.
     *
     * @exception IllegalArgumentException if the argument list is null.
     */
    OutputFormat fromArguments(List<String> mutableCommandLineArguments);


}
