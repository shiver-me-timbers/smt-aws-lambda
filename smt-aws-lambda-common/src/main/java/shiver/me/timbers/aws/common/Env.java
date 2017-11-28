/*
 * Copyright 2017 Karl Bennett
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package shiver.me.timbers.aws.common;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class Env {

    public String get(String name) {
        return System.getenv(name);
    }

    public List<String> getAsList(String name) {
        final String variable = System.getenv(name);
        if (variable == null) {
            return emptyList();
        }
        return asList(variable.split(","));
    }
}
