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
import java.util.HashMap;
import java.util.List;

public class AsnTypes {

	public final HashMap<String, AsnType> typesByName = new HashMap<>();

	public final List<AsnAny> anys = new ArrayList<>();
	public final List<AsnAnyNoDecode> any_nodecodes = new ArrayList<>();
	public final List<AsnBitString> bitStrings = new ArrayList<>();
	public final List<AsnBoolean> booleans = new ArrayList<>();
	public final List<AsnCharacterString> characterStrings = new ArrayList<>();
	public final List<AsnChoice> choices = new ArrayList<>();
	public final List<AsnDefinedType> defineds = new ArrayList<>();
	public final List<AsnEmbedded> embeddeds = new ArrayList<>();
	public final List<AsnEnum> enums = new ArrayList<>();
	public final List<AsnExternal> externals = new ArrayList<>();
	public final List<AsnInteger> integers = new ArrayList<>();
	public final List<ErrorMacro> macroErrors = new ArrayList<>();
	public final List<OperationMacro> macroOperations = new ArrayList<>();
	public final List<AsnNull> nulls = new ArrayList<>();
	public final List<AsnObjectIdentifier> objectIdentifiers = new ArrayList<>();
	public final List<AsnOctetString> octetStrings = new ArrayList<>();
	public final List<AsnReal> reals = new ArrayList<>();
	public final List<AsnRelativeOid> relativeOids = new ArrayList<>();
	public final List<AsnSelectionType> selections = new ArrayList<>();
	public final List<AsnSequenceSet> sequenceSets = new ArrayList<>();
	public final List<AsnSequenceOf> sequenceSetsOf = new ArrayList<>();
	public final List<AsnTaggedType> taggeds = new ArrayList<>();

}
