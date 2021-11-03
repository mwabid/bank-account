/*
 *
 * Copyright (c) 2017 Rabitka Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.sg.bank.account.domain;


import io.rabitka.validator.constraint.NonZeroConstraint;
import io.rabitka.validator.constraint.OnlyNegativeConstraint;
import io.rabitka.validator.constraint.OnlyPositiveConstraint;
import io.rabitka.validator.core.MetatypeConstraint;
import io.rabitka.validator.core.MetatypeValidator;
import io.rabitka.validator.core.TypeCheckerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wassim ABID wassim.abid@rabitka.io
 */
public class AmountMetatypeValidator extends MetatypeValidator<Amount, AmountMetaType> {

    public static final String FIELD_PATH_VALUE = "value";
    List<MetatypeConstraint<BigDecimal>> valueMetatypeConstraints = new ArrayList<>();

    public AmountMetatypeValidator(Class<?> parentType, String fieldName) {
         super(parentType, fieldName, Amount.class, AmountMetaType.class);

        valueMetatypeConstraints.add(new OnlyPositiveConstraint<>(super.getAnnotation().onlyPositive(), BigDecimal.class, FIELD_PATH_VALUE));
        valueMetatypeConstraints.add(new NonZeroConstraint<>(super.getAnnotation().nonZero(), BigDecimal.class, FIELD_PATH_VALUE));
        valueMetatypeConstraints.add(new OnlyNegativeConstraint<>(super.getAnnotation().onlyNegative(), BigDecimal.class, FIELD_PATH_VALUE));
    }

    @Override
    public List<MetatypeConstraint<?>> check(Amount object) {

        List<MetatypeConstraint<?>> violatedMetatypeConstraints = new ArrayList<>();
        if (object != null) {
            for (MetatypeConstraint<BigDecimal> valueMetatypeConstraint : this.valueMetatypeConstraints) {
                if (!valueMetatypeConstraint.isValid(object.getValue())) {
                    violatedMetatypeConstraints.add(valueMetatypeConstraint);
                }
            }
        }
        return violatedMetatypeConstraints;
    }


    public static class Factory implements TypeCheckerFactory<Amount, AmountMetaType> {
        @Override
        public AmountMetatypeValidator create(Class parentType, String fieldName) {
            return new AmountMetatypeValidator(parentType, fieldName);
        }
    }

}
