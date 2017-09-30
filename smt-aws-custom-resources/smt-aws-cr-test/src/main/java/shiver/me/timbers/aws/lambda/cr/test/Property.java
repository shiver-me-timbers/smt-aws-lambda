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

package shiver.me.timbers.aws.lambda.cr.test;

/**
 * @author Karl Bennett
 */
public class Property {

    private final String name;
    private final Object value;
    private boolean last;

    Property(String name, Object value) {
        this.name = name;
        this.value = value;
        this.last = false;
    }

    String getName() {
        return name;
    }

    Object getValue() {
        return value;
    }

    boolean isLast() {
        return last;
    }

    Property withLast(boolean last) {
        this.last = last;
        return this;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;

        Property property = (Property) o;

        if (last != property.last) return false;
        if (name != null ? !name.equals(property.name) : property.name != null) return false;
        return value != null ? value.equals(property.value) : property.value == null;
    }

    @Override
    public final int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (last ? 1 : 0);
        return result;
    }
}
