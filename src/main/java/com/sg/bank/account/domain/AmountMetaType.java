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

import io.rabitka.core.metatype.Metatype;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Wassim ABID wassim.abid@rabitka.io
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Metatype
public @interface AmountMetaType {
    boolean onlyPositive() default false;
    boolean nonZero() default false;
    boolean onlyNegative() default false;
}
