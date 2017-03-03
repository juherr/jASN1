/*
 * Copyright 2011-15 Fraunhofer ISE
 *
 * This file is part of jASN1.
 * For more information visit http://www.openmuc.org
 *
 * jASN1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jASN1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jASN1.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jasn1.compiler.model;

import java.util.ArrayList;

public class ConstraintElements {
    public AsnConstraint constraint;
    public ElementSetSpec elespec;
    public boolean isAlphabetConstraint;
    public boolean isElementSetSpec;
    public boolean isIncludeType;
    public boolean isLEndLess;
    public boolean isMaxKw;
    public boolean isMinKw;
    public boolean isPatternValue;
    public boolean isSizeConstraint;
    public boolean isTypeConstraint;
    public boolean isUEndLess;
    public boolean isValue;
    public boolean isValueRange;
    public boolean isWithComponent;
    public boolean isWithComponents;
    public AsnValue lEndValue, uEndValue;
    public Object type;
    public ArrayList typeConstraintList = new ArrayList();
    public AsnValue value;
}
