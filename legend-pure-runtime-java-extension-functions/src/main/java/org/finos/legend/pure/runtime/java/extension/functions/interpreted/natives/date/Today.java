// Copyright 2020 Goldman Sachs
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.finos.legend.pure.runtime.java.extension.functions.interpreted.natives.date;


import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.finos.legend.pure.m4.ModelRepository;
import org.finos.legend.pure.m4.coreinstance.primitive.date.PureDate;
import org.finos.legend.pure.m4.coreinstance.primitive.date.StrictDate;
import org.finos.legend.pure.runtime.java.interpreted.FunctionExecutionInterpreted;

public class Today extends NativeDateIndexicalFunction
{
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    public Today(FunctionExecutionInterpreted functionExecution, ModelRepository repository)
    {
        super(repository);
    }

    @Override
    protected PureDate getDate()
    {
        return StrictDate.fromCalendar(new GregorianCalendar(GMT));
    }
}
