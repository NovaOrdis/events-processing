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

import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/7/17
 */
public interface ProcedureFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Instantiates the procedure with the given command line label. The instance must be delivered after it was
     * internally configured so it would work correctly without additional configuration (albeit in the simplest cases).
     *
     * @param commandLineLabel the command line label for the candidate procedure. Note that it can be the normal
     *                         label or the abbreviated label.
     *
     * @param from the index of the first argument  in the following argument list that can be interpreted as
     *              procedure argument. Note that the arguments that are recognized as procedure arguments are
     *              removed from the argument list.
     *
     * @param arguments the list - which must be mutable - of the possible arguments for the procedure. The arguments
     *                  that are recognized as procedure arguments are removed from the list.
     *
     * @return null if no such procedure is found in classpath.
     */
    Procedure find(String commandLineLabel, int from, List<String> arguments);


}
