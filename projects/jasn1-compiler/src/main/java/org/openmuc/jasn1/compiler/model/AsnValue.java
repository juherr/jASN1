/*
 * Copyright 2011-17 Fraunhofer ISE
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

import java.util.List;

public class AsnValue {
    String bStr;
    public AsnBitOrOctetStringValue bStrValue;
    public String cStr;
    public AsnCharacterStringValue cStrValue;
    public AsnChoiceValue chval;
    public AsnDefinedValue definedValue;
    String hStr, enumIntVal;
    public boolean isAsnOIDValue;
    boolean isBStrOrOstrValue;
    public boolean isCStrValue;
    public boolean isCString;
    public boolean isChoiceValue;
    public boolean isDefinedValue;
    boolean isEnumIntValue;
    public boolean isFalseKW;
    public boolean isMinusInfinity;
    public boolean isNullKW;
    public boolean isPlusInfinity;
    public boolean isSequenceOfValue;
    public boolean isSequenceValue;
    public boolean isSignedNumber;
    public boolean isTrueKW;
    public String name;
    public AsnOidComponentList oidval;
    public AsnSequenceOfValue seqOfVal;
    public AsnSequenceValue seqval;
    public AsnSignedNumber signedNumber;
    public boolean isValueInBraces = false;
    public List<String> valueInBracesTokens;

}
