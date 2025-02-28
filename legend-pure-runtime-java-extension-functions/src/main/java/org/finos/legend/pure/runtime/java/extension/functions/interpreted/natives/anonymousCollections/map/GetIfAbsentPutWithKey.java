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

package org.finos.legend.pure.runtime.java.extension.functions.interpreted.natives.anonymousCollections.map;


import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.finos.legend.pure.m3.navigation.M3Properties;
import org.finos.legend.pure.m3.exception.PureExecutionException;
import org.finos.legend.pure.m3.compiler.Context;
import org.finos.legend.pure.m3.navigation.ValueSpecificationBootstrap;
import org.finos.legend.pure.m3.navigation.valuespecification.ValueSpecification;
import org.finos.legend.pure.m3.navigation.ProcessorSupport;
import org.finos.legend.pure.m4.ModelRepository;
import org.finos.legend.pure.m4.coreinstance.CoreInstance;
import org.finos.legend.pure.runtime.java.interpreted.ExecutionSupport;
import org.finos.legend.pure.runtime.java.interpreted.FunctionExecutionInterpreted;
import org.finos.legend.pure.runtime.java.interpreted.VariableContext;
import org.finos.legend.pure.runtime.java.interpreted.natives.InstantiationContext;
import org.finos.legend.pure.runtime.java.interpreted.natives.MapCoreInstance;
import org.finos.legend.pure.runtime.java.interpreted.natives.NativeFunction;
import org.finos.legend.pure.runtime.java.interpreted.profiler.Profiler;

import java.util.Stack;

public class GetIfAbsentPutWithKey extends NativeFunction
{
    private final FunctionExecutionInterpreted functionExecution;

    public GetIfAbsentPutWithKey(FunctionExecutionInterpreted functionExecution, ModelRepository repository)
    {
        this.functionExecution = functionExecution;
    }

    @Override
    public CoreInstance execute(ListIterable<? extends CoreInstance> params, Stack<MutableMap<String, CoreInstance>> resolvedTypeParameters, Stack<MutableMap<String, CoreInstance>> resolvedMultiplicityParameters, VariableContext variableContext, CoreInstance functionExpressionToUseInStack, Profiler profiler, InstantiationContext instantiationContext, ExecutionSupport executionSupport, Context context, ProcessorSupport processorSupport) throws PureExecutionException
    {
        MapCoreInstance mapCi = (MapCoreInstance) params.get(0).getValueForMetaPropertyToOne(M3Properties.values);
        MutableMap<CoreInstance, CoreInstance> map = mapCi.getMap();

        CoreInstance key = params.get(1).getValueForMetaPropertyToOne(M3Properties.values);
        boolean isExecutable = ValueSpecification.isExecutable(params.get(0), processorSupport);
        CoreInstance function = params.get(2).getValueForMetaPropertyToOne(M3Properties.values);

        Function<CoreInstance, CoreInstance> valueFunction = getValueFunction(mapCi, resolvedTypeParameters, resolvedMultiplicityParameters, variableContext, functionExpressionToUseInStack, profiler, instantiationContext, executionSupport, processorSupport, key, isExecutable, function);

        CoreInstance res = map.getIfAbsentPutWithKey(key, valueFunction);
        return ValueSpecificationBootstrap.wrapValueSpecification(res, ValueSpecification.isExecutable(params.get(0), processorSupport), processorSupport);
    }

    private Function<CoreInstance, CoreInstance> getValueFunction(final MapCoreInstance mapCi, final Stack<MutableMap<String, CoreInstance>> resolvedTypeParameters, final Stack<MutableMap<String, CoreInstance>> resolvedMultiplicityParameters, final VariableContext variableContext, final CoreInstance functionExpressionToUseInStack, final Profiler profiler, final InstantiationContext instantiationContext, final ExecutionSupport executionSupport, final ProcessorSupport processorSupport, final CoreInstance key, final boolean isExecutable, final CoreInstance function)
    {
        return new Function<CoreInstance, CoreInstance>(){

            @Override
            public CoreInstance valueOf(CoreInstance key)
            {
                mapCi.getStats().incrementGetIfAbsentCounter();
                CoreInstance value =  functionExecution.executeLambdaFromNative(function, Lists.mutable.with(ValueSpecificationBootstrap.wrapValueSpecification(key, isExecutable, processorSupport)), resolvedTypeParameters, resolvedMultiplicityParameters, variableContext, functionExpressionToUseInStack, profiler, instantiationContext, executionSupport);
                return value.getValueForMetaPropertyToOne(M3Properties.values);
            }
        };
    }


}