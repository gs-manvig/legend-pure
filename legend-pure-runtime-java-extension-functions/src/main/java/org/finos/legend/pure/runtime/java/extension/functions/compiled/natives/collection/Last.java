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

package org.finos.legend.pure.runtime.java.extension.functions.compiled.natives.collection;

import org.eclipse.collections.api.list.ListIterable;
import org.finos.legend.pure.m3.navigation.Instance;
import org.finos.legend.pure.m3.navigation.M3Properties;
import org.finos.legend.pure.m3.navigation.ProcessorSupport;
import org.finos.legend.pure.m3.navigation.multiplicity.Multiplicity;
import org.finos.legend.pure.m4.coreinstance.CoreInstance;
import org.finos.legend.pure.runtime.java.compiled.generation.ProcessorContext;
import org.finos.legend.pure.runtime.java.compiled.generation.processors.natives.AbstractNative;
import org.finos.legend.pure.runtime.java.compiled.generation.processors.type.TypeProcessor;

public class Last extends AbstractNative
{
    public Last()
    {
        super("last_T_MANY__T_$0_1$_");
    }

    @Override
    public String build(CoreInstance topLevelElement, CoreInstance functionExpression, ListIterable<String> transformedParams, ProcessorContext processorContext)
    {
        ProcessorSupport processorSupport = processorContext.getSupport();
        ListIterable<? extends CoreInstance> parametersValues = Instance.getValueForMetaPropertyToManyResolved(functionExpression, M3Properties.parametersValues, processorSupport);
        CoreInstance expression = parametersValues.get(0);
        boolean castToIterable = !Multiplicity.isToOne(expression.getValueForMetaPropertyToOne(M3Properties.multiplicity));

        return "FunctionsGen.last(" + (castToIterable ? "(" + TypeProcessor.typeToJavaObjectWithMul(expression.getValueForMetaPropertyToOne(M3Properties.genericType), expression.getValueForMetaPropertyToOne(M3Properties.multiplicity), processorSupport) + ")" : "") + transformedParams.get(0) + ")";
    }

    @Override
    public String buildBody() {

        return "new DefendedPureFunction1<Object, Object>()\n" +
                "        {\n" +
                "            @Override\n" +
                "            public Object value(Object input, ExecutionSupport es)\n" +
                "            {\n" +
                "                return input instanceof RichIterable ? FunctionsGen.last((RichIterable<?>) input) : FunctionsGen.last(input);\n" +
                "            }\n" +
                "        }";
    }

}
