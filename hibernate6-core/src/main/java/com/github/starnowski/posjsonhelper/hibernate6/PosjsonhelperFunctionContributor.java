/**
 *     Posjsonhelper library is an open-source project that adds support of
 *     Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 *
 *     Copyright (C) 2023  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posjsonhelper.hibernate6;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

/**
 * Implementation of {@link FunctionContributor}.
 * It uses component of type {@link SqmFunctionRegistryEnricher} to register the Posjsonhelper library functions.
 */
public class PosjsonhelperFunctionContributor implements FunctionContributor {

    private final SqmFunctionRegistryEnricher sqmFunctionRegistryEnricher;

    public PosjsonhelperFunctionContributor() {
        this(new SqmFunctionRegistryEnricher());
    }

    PosjsonhelperFunctionContributor(SqmFunctionRegistryEnricher sqmFunctionRegistryEnricher) {
        this.sqmFunctionRegistryEnricher = sqmFunctionRegistryEnricher;
    }


    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        sqmFunctionRegistryEnricher.enrich(functionContributions.getFunctionRegistry());
    }
}
