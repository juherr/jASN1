// $ANTLR 2.7.7 (20060906): "asn1.g" -> "ASNParser.java"$

package org.openmuc.jasn1.compiler.parser;

import java.math.BigInteger;
import java.util.ArrayList;

import org.openmuc.jasn1.compiler.model.AsnAny;
import org.openmuc.jasn1.compiler.model.AsnAnyNoDecode;
import org.openmuc.jasn1.compiler.model.AsnBitOrOctetStringValue;
import org.openmuc.jasn1.compiler.model.AsnBitString;
import org.openmuc.jasn1.compiler.model.AsnBoolean;
import org.openmuc.jasn1.compiler.model.AsnCharacterString;
import org.openmuc.jasn1.compiler.model.AsnCharacterStringValue;
import org.openmuc.jasn1.compiler.model.AsnChoice;
import org.openmuc.jasn1.compiler.model.AsnChoiceValue;
import org.openmuc.jasn1.compiler.model.AsnClassNumber;
import org.openmuc.jasn1.compiler.model.AsnConstraint;
import org.openmuc.jasn1.compiler.model.AsnDefinedType;
import org.openmuc.jasn1.compiler.model.AsnDefinedValue;
import org.openmuc.jasn1.compiler.model.AsnElementType;
import org.openmuc.jasn1.compiler.model.AsnElementTypeList;
import org.openmuc.jasn1.compiler.model.AsnEmbedded;
import org.openmuc.jasn1.compiler.model.AsnEnum;
import org.openmuc.jasn1.compiler.model.AsnExternal;
import org.openmuc.jasn1.compiler.model.AsnInteger;
import org.openmuc.jasn1.compiler.model.AsnModel;
import org.openmuc.jasn1.compiler.model.AsnModule;
import org.openmuc.jasn1.compiler.model.AsnModuleIdentifier;
import org.openmuc.jasn1.compiler.model.AsnNamedNumber;
import org.openmuc.jasn1.compiler.model.AsnNamedNumberList;
import org.openmuc.jasn1.compiler.model.AsnNamedValue;
import org.openmuc.jasn1.compiler.model.AsnNull;
import org.openmuc.jasn1.compiler.model.AsnObjectIdentifier;
import org.openmuc.jasn1.compiler.model.AsnOctetString;
import org.openmuc.jasn1.compiler.model.AsnOidComponent;
import org.openmuc.jasn1.compiler.model.AsnOidComponentList;
import org.openmuc.jasn1.compiler.model.AsnReal;
import org.openmuc.jasn1.compiler.model.AsnRelativeOid;
import org.openmuc.jasn1.compiler.model.AsnSelectionType;
import org.openmuc.jasn1.compiler.model.AsnSequenceOf;
import org.openmuc.jasn1.compiler.model.AsnSequenceOfValue;
import org.openmuc.jasn1.compiler.model.AsnSequenceSet;
import org.openmuc.jasn1.compiler.model.AsnSequenceValue;
import org.openmuc.jasn1.compiler.model.AsnSignedNumber;
import org.openmuc.jasn1.compiler.model.AsnTag;
import org.openmuc.jasn1.compiler.model.AsnTaggedType;
import org.openmuc.jasn1.compiler.model.AsnType;
import org.openmuc.jasn1.compiler.model.AsnValue;
import org.openmuc.jasn1.compiler.model.CharDef;
import org.openmuc.jasn1.compiler.model.ConstraintElements;
import org.openmuc.jasn1.compiler.model.ElementSetSpec;
import org.openmuc.jasn1.compiler.model.ErrorMacro;
import org.openmuc.jasn1.compiler.model.Intersection;
import org.openmuc.jasn1.compiler.model.NamedConstraint;
import org.openmuc.jasn1.compiler.model.ObjectType;
import org.openmuc.jasn1.compiler.model.OperationMacro;
import org.openmuc.jasn1.compiler.model.SymbolsFromModule;

import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;

public class ASNParser extends antlr.LLkParser implements ASNTokenTypes {

	protected ASNParser(TokenBuffer tokenBuf, int k) {
		super(tokenBuf, k);
		tokenNames = _tokenNames;
	}

	public ASNParser(TokenBuffer tokenBuf) {
		this(tokenBuf, 3);
	}

	protected ASNParser(TokenStream lexer, int k) {
		super(lexer, k);
		tokenNames = _tokenNames;
	}

	public ASNParser(TokenStream lexer) {
		this(lexer, 3);
	}

	public ASNParser(ParserSharedInputState state) {
		super(state, 3);
		tokenNames = _tokenNames;
	}

	public final void module_definitions(AsnModel model) throws RecognitionException, TokenStreamException {

		AsnModule module;

		try { // for error handling
			{
				int _cnt71 = 0;
				_loop71: do {
					if ((LA(1) == UPPER)) {
						module = module_definition();
						if (inputState.guessing == 0) {
							model.modulesByName.put(module.moduleIdentifier.name, module);
						}
					}
					else {
						if (_cnt71 >= 1) {
							break _loop71;
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
					}

					_cnt71++;
				} while (true);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_0);
			}
			else {
				throw ex;
			}
		}
	}

	public final AsnModule module_definition() throws RecognitionException, TokenStreamException {
		AsnModule module;

		Token e = null;
		Token i = null;
		Token a = null;

		module = new AsnModule();
		AsnModuleIdentifier mid;
		String s;

		try { // for error handling
			{
				mid = module_identifier();
				if (inputState.guessing == 0) {
					module.moduleIdentifier = mid;
				}
			}
			match(DEFINITIONS_KW);
			{
				switch (LA(1)) {
				case AUTOMATIC_KW:
				case EXPLICIT_KW:
				case IMPLICIT_KW: {
					{
						switch (LA(1)) {
						case EXPLICIT_KW: {
							e = LT(1);
							match(EXPLICIT_KW);
							if (inputState.guessing == 0) {
								module.tagDefault = e.getText();
							}
							break;
						}
						case IMPLICIT_KW: {
							i = LT(1);
							match(IMPLICIT_KW);
							if (inputState.guessing == 0) {
								module.tagDefault = i.getText();
							}
							break;
						}
						case AUTOMATIC_KW: {
							a = LT(1);
							match(AUTOMATIC_KW);
							if (inputState.guessing == 0) {
								module.tagDefault = a.getText();
							}
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
					match(TAGS_KW);
					if (inputState.guessing == 0) {
						module.tag = true;
					}
					break;
				}
				case EXTENSIBILITY_KW:
				case ASSIGN_OP: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case EXTENSIBILITY_KW: {
					match(EXTENSIBILITY_KW);
					match(IMPLIED_KW);
					if (inputState.guessing == 0) {
						module.extensible = true;
					}
					break;
				}
				case ASSIGN_OP: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(ASSIGN_OP);
			match(BEGIN_KW);
			module_body(module);
			match(END_KW);
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_1);
			}
			else {
				throw ex;
			}
		}
		return module;
	}

	public final AsnModuleIdentifier module_identifier() throws RecognitionException, TokenStreamException {
		AsnModuleIdentifier mid;

		Token md = null;
		mid = new AsnModuleIdentifier();
		AsnOidComponentList cmplst;

		try { // for error handling
			{
				{
					md = LT(1);
					match(UPPER);
					if (inputState.guessing == 0) {
						mid.name = md.getText();
					}
				}
				{
					switch (LA(1)) {
					case L_BRACE: {
						{
							cmplst = obj_id_comp_lst();
							if (inputState.guessing == 0) {
								mid.componentList = cmplst;
							}
						}
						break;
					}
					case DEFINITIONS_KW: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_2);
			}
			else {
				throw ex;
			}
		}
		return mid;
	}

	public final void module_body(AsnModule module) throws RecognitionException, TokenStreamException {

		try { // for error handling
			{
				switch (LA(1)) {
				case EXPORTS_KW: {
					exports(module);
					break;
				}
				case END_KW:
				case IMPORTS_KW:
				case UPPER:
				case LOWER: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case IMPORTS_KW: {
					imports(module);
					break;
				}
				case END_KW:
				case UPPER:
				case LOWER: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case UPPER:
				case LOWER: {
					{
						int _cnt108 = 0;
						_loop108: do {
							if ((LA(1) == UPPER || LA(1) == LOWER)) {
								assignment(module);
							}
							else {
								if (_cnt108 >= 1) {
									break _loop108;
								}
								else {
									throw new NoViableAltException(LT(1), getFilename());
								}
							}

							_cnt108++;
						} while (true);
					}
					break;
				}
				case END_KW: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_3);
			}
			else {
				throw ex;
			}
		}
	}

	public final AsnOidComponentList obj_id_comp_lst() throws RecognitionException, TokenStreamException {
		AsnOidComponentList oidcmplst;

		oidcmplst = new AsnOidComponentList();
		AsnOidComponent oidcmp;
		AsnDefinedValue defval;

		try { // for error handling
			match(L_BRACE);
			{
				if ((LA(1) == UPPER || LA(1) == LOWER) && (_tokenSet_4.member(LA(2))) && (_tokenSet_5.member(LA(3)))) {
					defval = defined_value();
					if (inputState.guessing == 0) {
						oidcmplst.isDefinitive = true;
						oidcmplst.defval = defval;
					}
				}
				else if (((LA(1) >= NUMBER && LA(1) <= LOWER)) && (_tokenSet_5.member(LA(2)))
						&& (_tokenSet_6.member(LA(3)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
			{
				int _cnt85 = 0;
				_loop85: do {
					if (((LA(1) >= NUMBER && LA(1) <= LOWER))) {
						oidcmp = obj_id_component();
						if (inputState.guessing == 0) {
							oidcmplst.components.add(oidcmp);
						}
					}
					else {
						if (_cnt85 >= 1) {
							break _loop85;
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
					}

					_cnt85++;
				} while (true);
			}
			match(R_BRACE);
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_7);
			}
			else {
				throw ex;
			}
		}
		return oidcmplst;
	}

	protected final AsnDefinedValue defined_value() throws RecognitionException, TokenStreamException {
		AsnDefinedValue defval;

		Token up = null;
		Token lid = null;
		defval = new AsnDefinedValue();

		try { // for error handling
			{
				{
					switch (LA(1)) {
					case UPPER: {
						up = LT(1);
						match(UPPER);
						if (inputState.guessing == 0) {
							defval.moduleIdentifier = up.getText();
						}
						match(DOT);
						if (inputState.guessing == 0) {
							defval.isDotPresent = true;
						}
						break;
					}
					case LOWER: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				lid = LT(1);
				match(LOWER);
				if (inputState.guessing == 0) {
					defval.name = lid.getText();
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_8);
			}
			else {
				throw ex;
			}
		}
		return defval;
	}

	public final AsnOidComponent obj_id_component() throws RecognitionException, TokenStreamException {
		AsnOidComponent oidcmp;

		Token num = null;
		Token lid = null;
		Token num1 = null;
		oidcmp = new AsnOidComponent();
		AsnDefinedValue defval;
		String s, n = "";

		try { // for error handling
			{
				if ((LA(1) == NUMBER)) {
					{
						num = LT(1);
						match(NUMBER);
						if (inputState.guessing == 0) {
							s = num.getText();
							oidcmp.num = new Integer(s);
							oidcmp.numberForm = true;
						}
					}
				}
				else {
					boolean synPredMatched91 = false;
					if (((LA(1) == LOWER) && (_tokenSet_9.member(LA(2))) && (_tokenSet_6.member(LA(3))))) {
						int _m91 = mark();
						synPredMatched91 = true;
						inputState.guessing++;
						try {
							{
								match(LOWER);
								{
									if ((LA(1) == L_PAREN)) {
										match(L_PAREN);
										match(NUMBER);
										match(R_PAREN);
									}
									else {
									}

								}
							}
						} catch (RecognitionException pe) {
							synPredMatched91 = false;
						}
						rewind(_m91);
						inputState.guessing--;
					}
					if (synPredMatched91) {
						{
							{
								lid = LT(1);
								match(LOWER);
								if (inputState.guessing == 0) {
									oidcmp.name = lid.getText();
									oidcmp.nameForm = true;
								}
							}
							{
								switch (LA(1)) {
								case L_PAREN: {
									match(L_PAREN);
									{
										num1 = LT(1);
										match(NUMBER);
										if (inputState.guessing == 0) {
											n = num1.getText();
											oidcmp.num = new Integer(n);
											oidcmp.nameAndNumberForm = true;
										}
									}
									match(R_PAREN);
									break;
								}
								case R_BRACE:
								case NUMBER:
								case UPPER:
								case LOWER: {
									break;
								}
								default: {
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
							}
						}
					}
					else {
						boolean synPredMatched97 = false;
						if (((LA(1) == UPPER || LA(1) == LOWER) && (_tokenSet_10.member(LA(2)))
								&& (_tokenSet_6.member(LA(3))))) {
							int _m97 = mark();
							synPredMatched97 = true;
							inputState.guessing++;
							try {
								{
									defined_value();
								}
							} catch (RecognitionException pe) {
								synPredMatched97 = false;
							}
							rewind(_m97);
							inputState.guessing--;
						}
						if (synPredMatched97) {
							{
								defval = defined_value();
								if (inputState.guessing == 0) {
									oidcmp.isDefinedValue = true;
									oidcmp.defval = defval;
								}
							}
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
					}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_11);
			}
			else {
				throw ex;
			}
		}
		return oidcmp;
	}

	public final String tag_default() throws RecognitionException, TokenStreamException {
		String s;

		Token tg = null;
		Token tg1 = null;
		Token tg2 = null;
		s = "";

		try { // for error handling
			switch (LA(1)) {
			case EXPLICIT_KW: {
				{
					tg = LT(1);
					match(EXPLICIT_KW);
					if (inputState.guessing == 0) {
						s = tg.getText();
					}
				}
				break;
			}
			case IMPLICIT_KW: {
				{
					tg1 = LT(1);
					match(IMPLICIT_KW);
					if (inputState.guessing == 0) {
						s = tg1.getText();
					}
				}
				break;
			}
			case AUTOMATIC_KW: {
				{
					tg2 = LT(1);
					match(AUTOMATIC_KW);
					if (inputState.guessing == 0) {
						s = tg2.getText();
					}
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_12);
			}
			else {
				throw ex;
			}
		}
		return s;
	}

	public final void exports(AsnModule module) throws RecognitionException, TokenStreamException {

		String s;
		ArrayList syml = new ArrayList();

		try { // for error handling
			match(EXPORTS_KW);
			if (inputState.guessing == 0) {
				module.exported = true;
			}
			{
				switch (LA(1)) {
				case ERROR_KW:
				case OBJECT_KW:
				case OPERATION_KW:
				case SEMI:
				case UPPER:
				case LOWER:
				case LITERAL_BIND:
				case LITERAL_UNBIND:
				case 128:
				case 129:
				case LITERAL_EXTENSION:
				case LITERAL_EXTENSIONS:
				case 132:
				case LITERAL_TOKEN:
				case 134:
				case 135:
				case LITERAL_PORT:
				case LITERAL_REFINE:
				case 138:
				case 139:
				case 140:
				case 141:
				case LITERAL_ALGORITHM:
				case LITERAL_ENCRYPTED:
				case LITERAL_SIGNED:
				case LITERAL_SIGNATURE:
				case LITERAL_PROTECTED:
				case 147: {
					{
						switch (LA(1)) {
						case ERROR_KW:
						case OBJECT_KW:
						case OPERATION_KW:
						case UPPER:
						case LOWER:
						case LITERAL_BIND:
						case LITERAL_UNBIND:
						case 128:
						case 129:
						case LITERAL_EXTENSION:
						case LITERAL_EXTENSIONS:
						case 132:
						case LITERAL_TOKEN:
						case 134:
						case 135:
						case LITERAL_PORT:
						case LITERAL_REFINE:
						case 138:
						case 139:
						case 140:
						case 141:
						case LITERAL_ALGORITHM:
						case LITERAL_ENCRYPTED:
						case LITERAL_SIGNED:
						case LITERAL_SIGNATURE:
						case LITERAL_PROTECTED:
						case 147: {
							syml = symbol_list();
							if (inputState.guessing == 0) {
								module.exportSymbolList = syml;
							}
							break;
						}
						case SEMI: {
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
					break;
				}
				case ALL_KW: {
					match(ALL_KW);
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(SEMI);
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_13);
			}
			else {
				throw ex;
			}
		}
	}

	public final void imports(AsnModule module) throws RecognitionException, TokenStreamException {

		try { // for error handling
			{
				match(IMPORTS_KW);
				{
					switch (LA(1)) {
					case ERROR_KW:
					case OBJECT_KW:
					case OPERATION_KW:
					case UPPER:
					case LOWER:
					case LITERAL_BIND:
					case LITERAL_UNBIND:
					case 128:
					case 129:
					case LITERAL_EXTENSION:
					case LITERAL_EXTENSIONS:
					case 132:
					case LITERAL_TOKEN:
					case 134:
					case 135:
					case LITERAL_PORT:
					case LITERAL_REFINE:
					case 138:
					case 139:
					case 140:
					case 141:
					case LITERAL_ALGORITHM:
					case LITERAL_ENCRYPTED:
					case LITERAL_SIGNED:
					case LITERAL_SIGNATURE:
					case LITERAL_PROTECTED:
					case 147: {
						{
							{
								int _cnt117 = 0;
								_loop117: do {
									if ((_tokenSet_14.member(LA(1)))) {
										symbols_from_module(module);
									}
									else {
										if (_cnt117 >= 1) {
											break _loop117;
										}
										else {
											throw new NoViableAltException(LT(1), getFilename());
										}
									}

									_cnt117++;
								} while (true);
							}
						}
						break;
					}
					case SEMI: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				match(SEMI);
			}
			if (inputState.guessing == 0) {
				module.imported = true;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_15);
			}
			else {
				throw ex;
			}
		}
	}

	public final void assignment(AsnModule module) throws RecognitionException, TokenStreamException {

		Token up = null;
		Token lid = null;
		Object obj;
		Object objv;
		AsnValue val;

		try { // for error handling
			if ((LA(1) == UPPER) && (LA(2) == ASSIGN_OP)) {
				{
					up = LT(1);
					match(UPPER);
					match(ASSIGN_OP);
					{
						obj = type();
					}
					if (inputState.guessing == 0) {

						if ((AsnAny.class).isInstance(obj)) {
							((AsnAny) obj).name = up.getText();
							module.asnTypes.anys.add((AsnAny) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnAnyNoDecode.class).isInstance(obj)) {
							((AsnAnyNoDecode) obj).name = up.getText();
							module.asnTypes.any_nodecodes.add((AsnAnyNoDecode) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnBitString.class).isInstance(obj)) {
							((AsnBitString) obj).name = up.getText();
							module.asnTypes.bitStrings.add((AsnBitString) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnBoolean.class).isInstance(obj)) {
							((AsnBoolean) obj).name = up.getText();
							module.asnTypes.booleans.add((AsnBoolean) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnCharacterString.class).isInstance(obj)) {
							((AsnCharacterString) obj).name = up.getText();
							module.asnTypes.characterStrings.add((AsnCharacterString) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnChoice.class).isInstance(obj)) {
							((AsnChoice) obj).name = up.getText();
							module.asnTypes.choices.add((AsnChoice) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnEnum.class).isInstance(obj)) {
							((AsnEnum) obj).name = up.getText();
							module.asnTypes.enums.add((AsnEnum) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnInteger.class).isInstance(obj)) {
							((AsnInteger) obj).name = up.getText();
							module.asnTypes.integers.add((AsnInteger) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnNull.class).isInstance(obj)) {
							((AsnNull) obj).name = up.getText();
							module.asnTypes.nulls.add((AsnNull) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnObjectIdentifier.class).isInstance(obj)) {
							((AsnObjectIdentifier) obj).name = up.getText();
							module.asnTypes.objectIdentifiers.add((AsnObjectIdentifier) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnOctetString.class).isInstance(obj)) {
							((AsnOctetString) obj).name = up.getText();
							module.asnTypes.octetStrings.add((AsnOctetString) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnReal.class).isInstance(obj)) {
							((AsnReal) obj).name = up.getText();
							module.asnTypes.reals.add((AsnReal) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnSequenceSet.class).isInstance(obj)) {
							((AsnSequenceSet) obj).name = up.getText();
							module.asnTypes.sequenceSets.add((AsnSequenceSet) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnSequenceOf.class).isInstance(obj)) {
							((AsnSequenceOf) obj).name = up.getText();
							module.asnTypes.sequenceSetsOf.add((AsnSequenceOf) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnExternal.class).isInstance(obj)) {
							((AsnExternal) obj).name = up.getText();
							module.asnTypes.externals.add((AsnExternal) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnSelectionType.class).isInstance(obj)) {
							((AsnSelectionType) obj).name = up.getText();
							module.asnTypes.selections.add((AsnSelectionType) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnRelativeOid.class).isInstance(obj)) {
							((AsnRelativeOid) obj).name = up.getText();
							module.asnTypes.relativeOids.add((AsnRelativeOid) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnEmbedded.class).isInstance(obj)) {
							((AsnEmbedded) obj).name = up.getText();
							module.asnTypes.embeddeds.add((AsnEmbedded) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnTaggedType.class).isInstance(obj)) {
							((AsnTaggedType) obj).name = up.getText();
							module.asnTypes.taggeds.add((AsnTaggedType) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((AsnDefinedType.class).isInstance(obj)) {
							((AsnDefinedType) obj).name = up.getText();
							module.asnTypes.defineds.add((AsnDefinedType) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((OperationMacro.class).isInstance(obj)) {
							((OperationMacro) obj).name = up.getText();
							module.asnTypes.macroOperations.add((OperationMacro) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else if ((ErrorMacro.class).isInstance(obj)) {
							((ErrorMacro) obj).name = up.getText();
							module.asnTypes.macroErrors.add((ErrorMacro) obj);
							module.asnTypes.typesByName.put(((AsnType) obj).name, (AsnType) obj);
						}
						else {
							System.out.println("Unknown Type" + (obj.getClass().getName()));
						}

					}
				}
			}
			else if ((LA(1) == LOWER)) {
				{
					lid = LT(1);
					match(LOWER);
					{
						objv = type();
					}
					match(ASSIGN_OP);
					{
						val = value();
					}
					if (inputState.guessing == 0) {

						if ((AsnBoolean.class).isInstance(objv)) {
							val.name = lid.getText();
							val.typeName = ((AsnBoolean) objv).BUILTINTYPE;
							module.asnValues.add(val);
						}
						else if ((AsnCharacterString.class).isInstance(objv)) {
							val.name = lid.getText();
							val.typeName = ((AsnCharacterString) objv).BUILTINTYPE; // Correct it
							module.asnValues.add(val);
						}
						else if ((AsnInteger.class).isInstance(objv)) {
							val.name = lid.getText();
							val.typeName = ((AsnInteger) objv).BUILTINTYPE;
							module.asnValues.add(val);
						}
						else if ((AsnOctetString.class).isInstance(objv)) {
							val.name = lid.getText();
							val.typeName = ((AsnOctetString) objv).BUILTINTYPE;
							module.asnValues.add(val);
						}
						else if ((AsnReal.class).isInstance(objv)) {
							val.name = lid.getText();
							val.typeName = ((AsnReal) objv).BUILTINTYPE;
							module.asnValues.add(val);
						}
						else if ((AsnDefinedType.class).isInstance(objv)) {
							val.name = lid.getText();
							val.typeName = ((AsnDefinedType) objv).typeName;
							module.asnValues.add(val);
						}
						else if ((ObjectType.class).isInstance(objv)) {
							val.name = lid.getText();
							val.typeName = ((ObjectType) objv).BUILTINTYPE;
							module.asnValues.add(val);
						}
						else {
							// System.out.println("Unsupported value definition: " + (objv.getClass().getName()));
						}

					}
				}
			}
			else {
				boolean synPredMatched144 = false;
				if (((LA(1) == UPPER) && (LA(2) == LITERAL_MACRO))) {
					int _m144 = mark();
					synPredMatched144 = true;
					inputState.guessing++;
					try {
						{
							match(UPPER);
							match(LITERAL_MACRO);
							match(ASSIGN_OP);
							match(BEGIN_KW);
							{
								_loop143: do {
									if ((_tokenSet_16.member(LA(1)))) {
										{
											match(_tokenSet_16);
										}
									}
									else {
										break _loop143;
									}

								} while (true);
							}
							match(END_KW);
						}
					} catch (RecognitionException pe) {
						synPredMatched144 = false;
					}
					rewind(_m144);
					inputState.guessing--;
				}
				if (synPredMatched144) {
					match(UPPER);
					match(LITERAL_MACRO);
					match(ASSIGN_OP);
					match(BEGIN_KW);
					{
						_loop147: do {
							if ((_tokenSet_16.member(LA(1)))) {
								{
									match(_tokenSet_16);
								}
							}
							else {
								break _loop147;
							}

						} while (true);
					}
					match(END_KW);
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_15);
			}
			else {
				throw ex;
			}
		}
	}

	public final ArrayList symbol_list() throws RecognitionException, TokenStreamException {
		ArrayList symlist;

		symlist = new ArrayList();
		String s = "";

		try { // for error handling
			{
				{
					s = symbol();
					if (inputState.guessing == 0) {
						symlist.add(s);
					}
				}
				{
					_loop131: do {
						if ((LA(1) == COMMA)) {
							match(COMMA);
							{
								s = symbol();
								if (inputState.guessing == 0) {
									symlist.add(s);
								}
							}
						}
						else {
							break _loop131;
						}

					} while (true);
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_17);
			}
			else {
				throw ex;
			}
		}
		return symlist;
	}

	public final void symbols_from_module(AsnModule module) throws RecognitionException, TokenStreamException {

		Token up = null;
		SymbolsFromModule sym = new SymbolsFromModule();
		String s = "";
		AsnModuleIdentifier mid;
		AsnDefinedValue defval;
		ArrayList arl;
		AsnOidComponentList cmplist;

		try { // for error handling
			{
				{
					arl = symbol_list();
					if (inputState.guessing == 0) {
						sym.symbolList = arl;
					}
				}
				match(FROM_KW);
				{
					up = LT(1);
					match(UPPER);
					if (inputState.guessing == 0) {
						sym.modref = up.getText();
					}
					{
						if ((LA(1) == L_BRACE)) {
							cmplist = obj_id_comp_lst();
							if (inputState.guessing == 0) {
								sym.isOidValue = true;
								sym.cmplist = cmplist;
							}
						}
						else {
							boolean synPredMatched124 = false;
							if (((LA(1) == UPPER || LA(1) == LOWER) && (_tokenSet_18.member(LA(2)))
									&& (_tokenSet_19.member(LA(3))))) {
								int _m124 = mark();
								synPredMatched124 = true;
								inputState.guessing++;
								try {
									{
										defined_value();
									}
								} catch (RecognitionException pe) {
									synPredMatched124 = false;
								}
								rewind(_m124);
								inputState.guessing--;
							}
							if (synPredMatched124) {
								{
									defval = defined_value();
									if (inputState.guessing == 0) {
										sym.isDefinedValue = true;
										sym.defval = defval;
									}
								}
							}
							else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_19.member(LA(2)))
									&& (_tokenSet_21.member(LA(3)))) {
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}
						}
					}
				}
			}
			if (inputState.guessing == 0) {
				module.importSymbolFromModuleList.add(sym);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_20);
			}
			else {
				throw ex;
			}
		}
	}

	public final String symbol() throws RecognitionException, TokenStreamException {
		String s;

		Token up = null;
		Token lid = null;
		s = "";

		try { // for error handling
			switch (LA(1)) {
			case UPPER: {
				up = LT(1);
				match(UPPER);
				if (inputState.guessing == 0) {
					s = up.getText();
				}
				break;
			}
			case LOWER: {
				lid = LT(1);
				match(LOWER);
				if (inputState.guessing == 0) {
					s = lid.getText();
				}
				break;
			}
			case ERROR_KW:
			case OBJECT_KW:
			case OPERATION_KW:
			case LITERAL_BIND:
			case LITERAL_UNBIND:
			case 128:
			case 129:
			case LITERAL_EXTENSION:
			case LITERAL_EXTENSIONS:
			case 132:
			case LITERAL_TOKEN:
			case 134:
			case 135:
			case LITERAL_PORT:
			case LITERAL_REFINE:
			case 138:
			case 139:
			case 140:
			case 141:
			case LITERAL_ALGORITHM:
			case LITERAL_ENCRYPTED:
			case LITERAL_SIGNED:
			case LITERAL_SIGNATURE:
			case LITERAL_PROTECTED:
			case 147: {
				s = macroName();
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_22);
			}
			else {
				throw ex;
			}
		}
		return s;
	}

	public final String macroName() throws RecognitionException, TokenStreamException {
		String s;

		s = "";

		try { // for error handling
			switch (LA(1)) {
			case OPERATION_KW: {
				match(OPERATION_KW);
				if (inputState.guessing == 0) {
					s = "OPERATION";
				}
				break;
			}
			case ERROR_KW: {
				match(ERROR_KW);
				if (inputState.guessing == 0) {
					s = "ERROR";
				}
				break;
			}
			case LITERAL_BIND: {
				match(LITERAL_BIND);
				if (inputState.guessing == 0) {
					s = "BIND";
				}
				break;
			}
			case LITERAL_UNBIND: {
				match(LITERAL_UNBIND);
				if (inputState.guessing == 0) {
					s = "UNBIND";
				}
				break;
			}
			case 128: {
				match(128);
				if (inputState.guessing == 0) {
					s = "APPLICATION-SERVICE-ELEMENT";
				}
				break;
			}
			case 129: {
				match(129);
				if (inputState.guessing == 0) {
					s = "APPLICATION-CONTEXT";
				}
				break;
			}
			case LITERAL_EXTENSION: {
				match(LITERAL_EXTENSION);
				if (inputState.guessing == 0) {
					s = "EXTENSION";
				}
				break;
			}
			case LITERAL_EXTENSIONS: {
				match(LITERAL_EXTENSIONS);
				if (inputState.guessing == 0) {
					s = "EXTENSIONS";
				}
				break;
			}
			case 132: {
				match(132);
				if (inputState.guessing == 0) {
					s = "EXTENSION-ATTRIBUTE";
				}
				break;
			}
			case LITERAL_TOKEN: {
				match(LITERAL_TOKEN);
				if (inputState.guessing == 0) {
					s = "TOKEN";
				}
				break;
			}
			case 134: {
				match(134);
				if (inputState.guessing == 0) {
					s = "TOKEN-DATA";
				}
				break;
			}
			case 135: {
				match(135);
				if (inputState.guessing == 0) {
					s = "SECURITY-CATEGORY";
				}
				break;
			}
			case OBJECT_KW: {
				match(OBJECT_KW);
				if (inputState.guessing == 0) {
					s = "OBJECT";
				}
				break;
			}
			case LITERAL_PORT: {
				match(LITERAL_PORT);
				if (inputState.guessing == 0) {
					s = "PORT";
				}
				break;
			}
			case LITERAL_REFINE: {
				match(LITERAL_REFINE);
				if (inputState.guessing == 0) {
					s = "REFINE";
				}
				break;
			}
			case 138: {
				match(138);
				if (inputState.guessing == 0) {
					s = "ABSTRACT-BIND";
				}
				break;
			}
			case 139: {
				match(139);
				if (inputState.guessing == 0) {
					s = "ABSTRACT-UNBIND";
				}
				break;
			}
			case 140: {
				match(140);
				if (inputState.guessing == 0) {
					s = "ABSTRACT-OPERATION";
				}
				break;
			}
			case 141: {
				match(141);
				if (inputState.guessing == 0) {
					s = "ABSTRACT-ERROR";
				}
				break;
			}
			case LITERAL_ALGORITHM: {
				match(LITERAL_ALGORITHM);
				if (inputState.guessing == 0) {
					s = "ALGORITHM";
				}
				break;
			}
			case LITERAL_ENCRYPTED: {
				match(LITERAL_ENCRYPTED);
				if (inputState.guessing == 0) {
					s = "ENCRYPTED";
				}
				break;
			}
			case LITERAL_SIGNED: {
				match(LITERAL_SIGNED);
				if (inputState.guessing == 0) {
					s = "SIGNED";
				}
				break;
			}
			case LITERAL_SIGNATURE: {
				match(LITERAL_SIGNATURE);
				if (inputState.guessing == 0) {
					s = "SIGNATURE";
				}
				break;
			}
			case LITERAL_PROTECTED: {
				match(LITERAL_PROTECTED);
				if (inputState.guessing == 0) {
					s = "PROTECTED";
				}
				break;
			}
			case 147: {
				match(147);
				if (inputState.guessing == 0) {
					s = "OBJECT-TYPE";
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_22);
			}
			else {
				throw ex;
			}
		}
		return s;
	}

	public final Object type() throws RecognitionException, TokenStreamException {
		Object obj;

		obj = null;

		try { // for error handling
			switch (LA(1)) {
			case ANY_KW:
			case ANY_NODECODE_KW:
			case BIT_KW:
			case BMP_STR_KW:
			case BOOLEAN_KW:
			case CHARACTER_KW:
			case CHOICE_KW:
			case EMBEDDED_KW:
			case ENUMERATED_KW:
			case EXTERNAL_KW:
			case GENERALIZED_TIME_KW:
			case GENERAL_STR_KW:
			case GRAPHIC_STR_KW:
			case IA5_STRING_KW:
			case INTEGER_KW:
			case ISO646_STR_KW:
			case NULL_KW:
			case NUMERIC_STR_KW:
			case OBJECT_KW:
			case OCTET_KW:
			case PRINTABLE_STR_KW:
			case REAL_KW:
			case RELATIVE_KW:
			case SEQUENCE_KW:
			case SET_KW:
			case TELETEX_STR_KW:
			case T61_STR_KW:
			case UNIVERSAL_STR_KW:
			case UTC_TIME_KW:
			case UTF8_STR_KW:
			case VIDEOTEX_STR_KW:
			case VISIBLE_STR_KW:
			case L_BRACKET: {
				{
					obj = built_in_type();
				}
				break;
			}
			case UPPER: {
				{
					obj = defined_type();
				}
				break;
			}
			case LOWER: {
				{
					obj = selection_type();
				}
				break;
			}
			case ERROR_KW:
			case OPERATION_KW:
			case 147: {
				{
					obj = macros_type();
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final AsnValue value() throws RecognitionException, TokenStreamException {
		AsnValue value;

		Token c = null;
		value = new AsnValue();
		AsnSequenceValue seqval;
		AsnDefinedValue defval;
		String aStr;
		AsnSignedNumber num;
		AsnOidComponentList cmplst;

		try { // for error handling
			switch (LA(1)) {
			case TRUE_KW: {
				{
					match(TRUE_KW);
					if (inputState.guessing == 0) {
						value.isTrueKW = true;
					}
				}
				break;
			}
			case FALSE_KW: {
				{
					match(FALSE_KW);
					if (inputState.guessing == 0) {
						value.isFalseKW = true;
					}
				}
				break;
			}
			case NULL_KW: {
				{
					match(NULL_KW);
					if (inputState.guessing == 0) {
						value.isNullKW = true;
					}
				}
				break;
			}
			case C_STRING: {
				{
					c = LT(1);
					match(C_STRING);
					if (inputState.guessing == 0) {
						value.isCString = true;
						value.cStr = c.getText();
					}
				}
				break;
			}
			case MINUS:
			case NUMBER: {
				{
					num = signed_number();
					if (inputState.guessing == 0) {
						value.isSignedNumber = true;
						value.signedNumber = num;
					}
				}
				break;
			}
			case PLUS_INFINITY_KW: {
				{
					match(PLUS_INFINITY_KW);
					if (inputState.guessing == 0) {
						value.isPlusInfinity = true;
					}
				}
				break;
			}
			case MINUS_INFINITY_KW: {
				{
					match(MINUS_INFINITY_KW);
					if (inputState.guessing == 0) {
						value.isMinusInfinity = true;
					}
				}
				break;
			}
			default:
				boolean synPredMatched422 = false;
				if (((LA(1) == UPPER || LA(1) == LOWER) && (_tokenSet_24.member(LA(2)))
						&& (_tokenSet_25.member(LA(3))))) {
					int _m422 = mark();
					synPredMatched422 = true;
					inputState.guessing++;
					try {
						{
							defined_value();
						}
					} catch (RecognitionException pe) {
						synPredMatched422 = false;
					}
					rewind(_m422);
					inputState.guessing--;
				}
				if (synPredMatched422) {
					{
						defval = defined_value();
						if (inputState.guessing == 0) {
							value.isDefinedValue = true;
							value.definedValue = defval;
						}
					}
				}
				else {
					boolean synPredMatched428 = false;
					if (((LA(1) == LOWER) && (_tokenSet_26.member(LA(2))) && (_tokenSet_24.member(LA(3))))) {
						int _m428 = mark();
						synPredMatched428 = true;
						inputState.guessing++;
						try {
							{
								choice_value(value);
							}
						} catch (RecognitionException pe) {
							synPredMatched428 = false;
						}
						rewind(_m428);
						inputState.guessing--;
					}
					if (synPredMatched428) {
						{
							choice_value(value);
							if (inputState.guessing == 0) {
								value.isChoiceValue = true;
							}
						}
					}
					else {
						boolean synPredMatched431 = false;
						if (((LA(1) == L_BRACE) && (LA(2) == COMMA || LA(2) == R_BRACE || LA(2) == LOWER)
								&& (_tokenSet_27.member(LA(3))))) {
							int _m431 = mark();
							synPredMatched431 = true;
							inputState.guessing++;
							try {
								{
									sequence_value();
								}
							} catch (RecognitionException pe) {
								synPredMatched431 = false;
							}
							rewind(_m431);
							inputState.guessing--;
						}
						if (synPredMatched431) {
							{
								seqval = sequence_value();
								if (inputState.guessing == 0) {
									value.isSequenceValue = true;
									value.seqval = seqval;
								}
							}
						}
						else {
							boolean synPredMatched434 = false;
							if (((LA(1) == L_BRACE) && (_tokenSet_28.member(LA(2))) && (_tokenSet_24.member(LA(3))))) {
								int _m434 = mark();
								synPredMatched434 = true;
								inputState.guessing++;
								try {
									{
										sequenceof_value(value);
									}
								} catch (RecognitionException pe) {
									synPredMatched434 = false;
								}
								rewind(_m434);
								inputState.guessing--;
							}
							if (synPredMatched434) {
								{
									sequenceof_value(value);
									if (inputState.guessing == 0) {
										value.isSequenceOfValue = true;
									}
								}
							}
							else {
								boolean synPredMatched437 = false;
								if (((LA(1) == L_BRACE || LA(1) == B_STRING || LA(1) == H_STRING)
										&& (_tokenSet_27.member(LA(2))) && (_tokenSet_25.member(LA(3))))) {
									int _m437 = mark();
									synPredMatched437 = true;
									inputState.guessing++;
									try {
										{
											cstr_value(value);
										}
									} catch (RecognitionException pe) {
										synPredMatched437 = false;
									}
									rewind(_m437);
									inputState.guessing--;
								}
								if (synPredMatched437) {
									{
										cstr_value(value);
										if (inputState.guessing == 0) {
											value.isCStrValue = true;
										}
									}
								}
								else {
									boolean synPredMatched440 = false;
									if (((LA(1) == L_BRACE) && ((LA(2) >= NUMBER && LA(2) <= LOWER))
											&& (_tokenSet_5.member(LA(3))))) {
										int _m440 = mark();
										synPredMatched440 = true;
										inputState.guessing++;
										try {
											{
												obj_id_comp_lst();
											}
										} catch (RecognitionException pe) {
											synPredMatched440 = false;
										}
										rewind(_m440);
										inputState.guessing--;
									}
									if (synPredMatched440) {
										{
											cmplst = obj_id_comp_lst();
											if (inputState.guessing == 0) {
												value.isAsnOIDValue = true;
												value.oidval = cmplst;
											}
										}
									}
									else {
										throw new NoViableAltException(LT(1), getFilename());
									}
								}
							}
						}
					}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_27);
			}
			else {
				throw ex;
			}
		}
		return value;
	}

	public final Object built_in_type() throws RecognitionException, TokenStreamException {
		Object obj;

		obj = null;

		try { // for error handling
			switch (LA(1)) {
			case ANY_KW: {
				{
					obj = any_type();
				}
				break;
			}
			case ANY_NODECODE_KW: {
				{
					obj = anyNoDecode_type();
				}
				break;
			}
			case BIT_KW: {
				{
					obj = bit_string_type();
				}
				break;
			}
			case BOOLEAN_KW: {
				{
					obj = boolean_type();
				}
				break;
			}
			case BMP_STR_KW:
			case CHARACTER_KW:
			case GENERALIZED_TIME_KW:
			case GENERAL_STR_KW:
			case GRAPHIC_STR_KW:
			case IA5_STRING_KW:
			case ISO646_STR_KW:
			case NUMERIC_STR_KW:
			case PRINTABLE_STR_KW:
			case TELETEX_STR_KW:
			case T61_STR_KW:
			case UNIVERSAL_STR_KW:
			case UTC_TIME_KW:
			case UTF8_STR_KW:
			case VIDEOTEX_STR_KW:
			case VISIBLE_STR_KW: {
				{
					obj = character_str_type();
				}
				break;
			}
			case CHOICE_KW: {
				{
					obj = choice_type();
				}
				break;
			}
			case EMBEDDED_KW: {
				{
					obj = embedded_type();
				}
				match(EMBEDDED_KW);
				match(PDV_KW);
				break;
			}
			case ENUMERATED_KW: {
				{
					obj = enum_type();
				}
				break;
			}
			case EXTERNAL_KW: {
				{
					obj = external_type();
				}
				break;
			}
			case INTEGER_KW: {
				{
					obj = integer_type();
				}
				break;
			}
			case NULL_KW: {
				{
					obj = null_type();
				}
				break;
			}
			case OBJECT_KW: {
				{
					obj = object_identifier_type();
				}
				break;
			}
			case OCTET_KW: {
				{
					obj = octetString_type();
				}
				break;
			}
			case REAL_KW: {
				{
					obj = real_type();
				}
				break;
			}
			case RELATIVE_KW: {
				{
					obj = relativeOid_type();
				}
				break;
			}
			case L_BRACKET: {
				{
					obj = tagged_type();
				}
				break;
			}
			default:
				if ((LA(1) == SEQUENCE_KW) && (LA(2) == L_BRACE)
						&& (LA(3) == COMPONENTS_KW || LA(3) == R_BRACE || LA(3) == LOWER)) {
					{
						obj = sequence_type();
					}
				}
				else if ((LA(1) == SEQUENCE_KW) && (_tokenSet_29.member(LA(2))) && (_tokenSet_30.member(LA(3)))) {
					{
						obj = sequenceof_type();
					}
				}
				else if ((LA(1) == SET_KW) && (LA(2) == L_BRACE)
						&& (LA(3) == COMPONENTS_KW || LA(3) == R_BRACE || LA(3) == LOWER)) {
					{
						obj = set_type();
					}
				}
				else if ((LA(1) == SET_KW) && (_tokenSet_29.member(LA(2))) && (_tokenSet_30.member(LA(3)))) {
					{
						obj = setof_type();
					}
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object defined_type() throws RecognitionException, TokenStreamException {
		Object obj;

		Token up = null;
		Token up1 = null;
		AsnDefinedType deftype = new AsnDefinedType();
		AsnConstraint cnstrnt;
		obj = null;

		try { // for error handling
			{
				{
					if ((LA(1) == UPPER) && (LA(2) == DOT)) {
						up = LT(1);
						match(UPPER);
						if (inputState.guessing == 0) {
							deftype.isModuleReference = true;
							deftype.moduleReference = up.getText();
						}
						match(DOT);
					}
					else if ((LA(1) == UPPER) && (_tokenSet_31.member(LA(2)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				{
					up1 = LT(1);
					match(UPPER);
					if (inputState.guessing == 0) {
						deftype.typeName = up1.getText();
					}
				}
				{
					if ((_tokenSet_31.member(LA(1))) && (_tokenSet_32.member(LA(2))) && (_tokenSet_33.member(LA(3)))) {
						cnstrnt = constraint();
						if (inputState.guessing == 0) {
							deftype.constraint = cnstrnt;
						}
					}
					else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
			}
			if (inputState.guessing == 0) {
				obj = deftype;
				deftype = null;
				cnstrnt = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object selection_type() throws RecognitionException, TokenStreamException {
		Object obj;

		Token lid = null;
		AsnSelectionType seltype = new AsnSelectionType();
		obj = null;
		Object obj1;

		try { // for error handling
			{
				{
					lid = LT(1);
					match(LOWER);
					if (inputState.guessing == 0) {
						seltype.selectionID = lid.getText();
					}
				}
				match(LESS);
				{
					obj1 = type();
					if (inputState.guessing == 0) {
						seltype.type = obj1;
					}
				}
			}
			if (inputState.guessing == 0) {
				obj = seltype;
				seltype = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object macros_type() throws RecognitionException, TokenStreamException {
		Object obj;

		obj = null;

		try { // for error handling
			switch (LA(1)) {
			case OPERATION_KW: {
				{
					obj = operation_macro();
				}
				break;
			}
			case ERROR_KW: {
				{
					obj = error_macro();
				}
				break;
			}
			case 147: {
				{
					obj = objecttype_macro();
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final AsnAny any_type() throws RecognitionException, TokenStreamException {
		AsnAny an;

		Token lid = null;
		an = new AsnAny();

		try { // for error handling
			{
				match(ANY_KW);
				{
					switch (LA(1)) {
					case DEFINED_KW: {
						match(DEFINED_KW);
						match(BY_KW);
						if (inputState.guessing == 0) {
							an.isDefinedBy = true;
						}
						lid = LT(1);
						match(LOWER);
						if (inputState.guessing == 0) {
							an.definedByType = lid.getText();
						}
						break;
					}
					case ANY_KW:
					case ANY_NODECODE_KW:
					case BIT_KW:
					case BMP_STR_KW:
					case BOOLEAN_KW:
					case CHARACTER_KW:
					case CHOICE_KW:
					case DEFAULT_KW:
					case EMBEDDED_KW:
					case END_KW:
					case ENUMERATED_KW:
					case ERROR_KW:
					case ERRORS_KW:
					case EXTERNAL_KW:
					case FALSE_KW:
					case GENERALIZED_TIME_KW:
					case GENERAL_STR_KW:
					case GRAPHIC_STR_KW:
					case IA5_STRING_KW:
					case INTEGER_KW:
					case INTERSECTION_KW:
					case ISO646_STR_KW:
					case LINKED_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case NUMERIC_STR_KW:
					case OBJECT_KW:
					case OCTET_KW:
					case OPERATION_KW:
					case OPTIONAL_KW:
					case PLUS_INFINITY_KW:
					case PRINTABLE_STR_KW:
					case REAL_KW:
					case RELATIVE_KW:
					case RESULT_KW:
					case SEQUENCE_KW:
					case SET_KW:
					case TELETEX_STR_KW:
					case TRUE_KW:
					case T61_STR_KW:
					case UNION_KW:
					case UNIVERSAL_STR_KW:
					case UTC_TIME_KW:
					case UTF8_STR_KW:
					case VIDEOTEX_STR_KW:
					case VISIBLE_STR_KW:
					case ASSIGN_OP:
					case BAR:
					case COLON:
					case COMMA:
					case EXCLAMATION:
					case INTERSECTION:
					case L_BRACE:
					case L_BRACKET:
					case MINUS:
					case R_BRACE:
					case R_PAREN:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING:
					case 147:
					case LITERAL_ACCESS:
					case EXCEPT: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return an;
	}

	public final Object anyNoDecode_type() throws RecognitionException, TokenStreamException {
		Object obj;

		Token lid = null;
		obj = null;
		AsnAnyNoDecode an = new AsnAnyNoDecode();

		try { // for error handling
			{
				match(ANY_NODECODE_KW);
				{
					switch (LA(1)) {
					case DEFINED_KW: {
						match(DEFINED_KW);
						match(BY_KW);
						if (inputState.guessing == 0) {
							an.isDefinedBy = true;
						}
						lid = LT(1);
						match(LOWER);
						if (inputState.guessing == 0) {
							an.definedByType = lid.getText();
						}
						break;
					}
					case ANY_KW:
					case ANY_NODECODE_KW:
					case BIT_KW:
					case BMP_STR_KW:
					case BOOLEAN_KW:
					case CHARACTER_KW:
					case CHOICE_KW:
					case DEFAULT_KW:
					case EMBEDDED_KW:
					case END_KW:
					case ENUMERATED_KW:
					case ERROR_KW:
					case ERRORS_KW:
					case EXTERNAL_KW:
					case FALSE_KW:
					case GENERALIZED_TIME_KW:
					case GENERAL_STR_KW:
					case GRAPHIC_STR_KW:
					case IA5_STRING_KW:
					case INTEGER_KW:
					case INTERSECTION_KW:
					case ISO646_STR_KW:
					case LINKED_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case NUMERIC_STR_KW:
					case OBJECT_KW:
					case OCTET_KW:
					case OPERATION_KW:
					case OPTIONAL_KW:
					case PLUS_INFINITY_KW:
					case PRINTABLE_STR_KW:
					case REAL_KW:
					case RELATIVE_KW:
					case RESULT_KW:
					case SEQUENCE_KW:
					case SET_KW:
					case TELETEX_STR_KW:
					case TRUE_KW:
					case T61_STR_KW:
					case UNION_KW:
					case UNIVERSAL_STR_KW:
					case UTC_TIME_KW:
					case UTF8_STR_KW:
					case VIDEOTEX_STR_KW:
					case VISIBLE_STR_KW:
					case ASSIGN_OP:
					case BAR:
					case COLON:
					case COMMA:
					case EXCLAMATION:
					case INTERSECTION:
					case L_BRACE:
					case L_BRACKET:
					case MINUS:
					case R_BRACE:
					case R_PAREN:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING:
					case 147:
					case LITERAL_ACCESS:
					case EXCEPT: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
			}
			if (inputState.guessing == 0) {
				obj = an;
				an = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object bit_string_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnBitString bstr = new AsnBitString();
		AsnNamedNumberList nnlst;
		AsnConstraint cnstrnt;
		obj = null;

		try { // for error handling
			{
				match(BIT_KW);
				match(STRING_KW);
				{
					if ((LA(1) == L_BRACE) && (LA(2) == LOWER) && (LA(3) == L_PAREN)) {
						nnlst = namedNumber_list();
						if (inputState.guessing == 0) {
							bstr.namedNumberList = nnlst;
						}
					}
					else if ((_tokenSet_31.member(LA(1))) && (_tokenSet_32.member(LA(2)))
							&& (_tokenSet_33.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				{
					if ((_tokenSet_31.member(LA(1))) && (_tokenSet_32.member(LA(2))) && (_tokenSet_33.member(LA(3)))) {
						cnstrnt = constraint();
						if (inputState.guessing == 0) {
							bstr.constraint = cnstrnt;
						}
					}
					else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
			}
			if (inputState.guessing == 0) {
				obj = bstr;
				nnlst = null;
				cnstrnt = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object boolean_type() throws RecognitionException, TokenStreamException {
		Object obj;

		obj = null;

		try { // for error handling
			match(BOOLEAN_KW);
			if (inputState.guessing == 0) {
				obj = new AsnBoolean();
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object character_str_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnCharacterString cstr = new AsnCharacterString();
		String s;
		AsnConstraint cnstrnt;
		obj = null;

		try { // for error handling
			{
				switch (LA(1)) {
				case CHARACTER_KW: {
					{
						match(CHARACTER_KW);
						match(STRING_KW);
						if (inputState.guessing == 0) {
							cstr.isUCSType = true;
						}
					}
					break;
				}
				case BMP_STR_KW:
				case GENERALIZED_TIME_KW:
				case GENERAL_STR_KW:
				case GRAPHIC_STR_KW:
				case IA5_STRING_KW:
				case ISO646_STR_KW:
				case NUMERIC_STR_KW:
				case PRINTABLE_STR_KW:
				case TELETEX_STR_KW:
				case T61_STR_KW:
				case UNIVERSAL_STR_KW:
				case UTC_TIME_KW:
				case UTF8_STR_KW:
				case VIDEOTEX_STR_KW:
				case VISIBLE_STR_KW: {
					{
						s = character_set();
						if (inputState.guessing == 0) {
							cstr.stringtype = s;
						}
						{
							if ((_tokenSet_31.member(LA(1))) && (_tokenSet_32.member(LA(2)))
									&& (_tokenSet_33.member(LA(3)))) {
								cnstrnt = constraint();
								if (inputState.guessing == 0) {
									cstr.constraint = cnstrnt;
								}
							}
							else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_34.member(LA(2)))
									&& (_tokenSet_35.member(LA(3)))) {
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}

						}
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			if (inputState.guessing == 0) {
				obj = cstr;
				cnstrnt = null;
				cstr = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object choice_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnChoice ch = new AsnChoice();
		AsnElementTypeList eltplst;
		obj = null;

		try { // for error handling
			{
				match(CHOICE_KW);
				match(L_BRACE);
				{
					eltplst = elementType_list();
					if (inputState.guessing == 0) {
						ch.elementTypeList = eltplst;
					}
				}
				match(R_BRACE);
			}
			if (inputState.guessing == 0) {
				obj = ch;
				eltplst = null;
				ch = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object embedded_type() throws RecognitionException, TokenStreamException {
		Object obj;

		obj = null;

		try { // for error handling
			{
				match(EMBEDDED_KW);
				match(PDV_KW);
			}
			if (inputState.guessing == 0) {
				obj = new AsnEmbedded();
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_36);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object enum_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnEnum enumtyp = new AsnEnum();
		AsnNamedNumberList nnlst;
		obj = null;

		try { // for error handling
			{
				match(ENUMERATED_KW);
				{
					nnlst = namedNumber_list();
					if (inputState.guessing == 0) {
						enumtyp.namedNumberList = nnlst;
					}
				}
			}
			if (inputState.guessing == 0) {
				obj = enumtyp;
				enumtyp = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object external_type() throws RecognitionException, TokenStreamException {
		Object obj;

		obj = null;

		try { // for error handling
			match(EXTERNAL_KW);
			if (inputState.guessing == 0) {
				obj = new AsnExternal();
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object integer_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnInteger intgr = new AsnInteger();
		AsnNamedNumberList numlst;
		AsnConstraint cnstrnt;
		obj = null;

		try { // for error handling
			{
				match(INTEGER_KW);
				{
					if ((LA(1) == L_BRACE) && (LA(2) == LOWER) && (LA(3) == L_PAREN)) {
						numlst = namedNumber_list();
						if (inputState.guessing == 0) {
							intgr.namedNumberList = numlst;
						}
					}
					else if ((_tokenSet_31.member(LA(1))) && (_tokenSet_32.member(LA(2)))
							&& (_tokenSet_33.member(LA(3)))) {
						cnstrnt = constraint();
						if (inputState.guessing == 0) {
							intgr.constraint = cnstrnt;
						}
					}
					else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
			}
			if (inputState.guessing == 0) {
				obj = intgr;
				numlst = null;
				cnstrnt = null;
				intgr = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object null_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnNull nll = new AsnNull();
		obj = null;

		try { // for error handling
			match(NULL_KW);
			if (inputState.guessing == 0) {
				obj = nll;
				nll = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object object_identifier_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnObjectIdentifier objident = new AsnObjectIdentifier();
		obj = null;

		try { // for error handling
			match(OBJECT_KW);
			match(IDENTIFIER_KW);
			if (inputState.guessing == 0) {
				obj = objident;
				objident = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object octetString_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnOctetString oct = new AsnOctetString();
		AsnConstraint cnstrnt;
		obj = null;

		try { // for error handling
			{
				match(OCTET_KW);
				match(STRING_KW);
				{
					if ((_tokenSet_31.member(LA(1))) && (_tokenSet_32.member(LA(2))) && (_tokenSet_33.member(LA(3)))) {
						cnstrnt = constraint();
						if (inputState.guessing == 0) {
							oct.constraint = cnstrnt;
						}
					}
					else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
			}
			if (inputState.guessing == 0) {
				obj = oct;
				cnstrnt = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object real_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnReal rl = new AsnReal();
		obj = null;

		try { // for error handling
			match(REAL_KW);
			if (inputState.guessing == 0) {
				obj = rl;
				rl = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object relativeOid_type() throws RecognitionException, TokenStreamException {
		Object obj;

		obj = null;

		try { // for error handling
			match(RELATIVE_KW);
			match(MINUS);
			match(OID_KW);
			if (inputState.guessing == 0) {
				obj = new AsnRelativeOid();
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object sequence_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnSequenceSet seq = new AsnSequenceSet();
		AsnElementTypeList eltplist;
		AsnConstraint cnstrnt;
		obj = null;

		try { // for error handling
			{
				match(SEQUENCE_KW);
				if (inputState.guessing == 0) {
					seq.isSequence = true;
				}
				match(L_BRACE);
				{
					switch (LA(1)) {
					case COMPONENTS_KW:
					case LOWER: {
						eltplist = elementType_list();
						if (inputState.guessing == 0) {
							seq.elementTypeList = eltplist;
						}
						break;
					}
					case R_BRACE: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				match(R_BRACE);
			}
			if (inputState.guessing == 0) {
				obj = seq;
				eltplist = null;
				seq = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final AsnSequenceOf sequenceof_type() throws RecognitionException, TokenStreamException {
		AsnSequenceOf obj;

		AsnSequenceOf seqof = new AsnSequenceOf();
		AsnConstraint cnstrnt;
		obj = null;
		Object referencedAsnType;
		String s;

		try { // for error handling
			{
				match(SEQUENCE_KW);
				if (inputState.guessing == 0) {
					seqof.isSequenceOf = true;
				}
				{
					if ((_tokenSet_29.member(LA(1))) && (_tokenSet_30.member(LA(2))) && (_tokenSet_37.member(LA(3)))) {
						cnstrnt = constraint();
						if (inputState.guessing == 0) {
							seqof.constraint = cnstrnt;
						}
					}
					else if ((LA(1) == OF_KW) && (_tokenSet_12.member(LA(2))) && (_tokenSet_38.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				match(OF_KW);
				{
					referencedAsnType = type();
					if (inputState.guessing == 0) {
						if ((AsnDefinedType.class).isInstance(referencedAsnType)) {
							seqof.isDefinedType = true;
							seqof.typeName = ((AsnDefinedType) referencedAsnType).typeName;
						}
						else {
							seqof.typeReference = referencedAsnType;
						}

					}
				}
			}
			if (inputState.guessing == 0) {
				obj = seqof;
				cnstrnt = null;
				seqof = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object set_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnSequenceSet set = new AsnSequenceSet();
		AsnElementTypeList eltplist;
		obj = null;

		try { // for error handling
			{
				match(SET_KW);
				match(L_BRACE);
				{
					switch (LA(1)) {
					case COMPONENTS_KW:
					case LOWER: {
						eltplist = elementType_list();
						if (inputState.guessing == 0) {
							set.elementTypeList = eltplist;
						}
						break;
					}
					case R_BRACE: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				match(R_BRACE);
			}
			if (inputState.guessing == 0) {
				obj = set;
				eltplist = null;
				set = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object setof_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnSequenceOf setof = new AsnSequenceOf();
		AsnConstraint cns;
		obj = null;
		Object obj1;
		String s;

		try { // for error handling
			{
				match(SET_KW);
				{
					if ((_tokenSet_29.member(LA(1))) && (_tokenSet_30.member(LA(2))) && (_tokenSet_37.member(LA(3)))) {
						cns = constraint();
						if (inputState.guessing == 0) {
							setof.constraint = cns;
						}
					}
					else if ((LA(1) == OF_KW) && (_tokenSet_12.member(LA(2))) && (_tokenSet_38.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				match(OF_KW);
				{
					obj1 = type();
					if (inputState.guessing == 0) {
						if ((AsnDefinedType.class).isInstance(obj1)) {
							setof.isDefinedType = true;
							setof.typeName = ((AsnDefinedType) obj1).typeName;
						}
						else {
							setof.typeReference = obj1;
						}

					}
				}
			}
			if (inputState.guessing == 0) {
				obj = setof;
				cns = null;
				obj1 = null;
				setof = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object tagged_type() throws RecognitionException, TokenStreamException {
		Object obj;

		AsnTaggedType tgtyp = new AsnTaggedType();
		AsnTag tg;
		Object obj1 = null;
		String s;
		obj = null;

		try { // for error handling
			{
				{
					tg = tag();
					if (inputState.guessing == 0) {
						tgtyp.tag = tg;
					}
				}
				{
					switch (LA(1)) {
					case AUTOMATIC_KW:
					case EXPLICIT_KW:
					case IMPLICIT_KW: {
						s = tag_default();
						if (inputState.guessing == 0) {
							tgtyp.tagType = s;
						}
						break;
					}
					case ANY_KW:
					case ANY_NODECODE_KW:
					case BIT_KW:
					case BMP_STR_KW:
					case BOOLEAN_KW:
					case CHARACTER_KW:
					case CHOICE_KW:
					case EMBEDDED_KW:
					case ENUMERATED_KW:
					case ERROR_KW:
					case EXTERNAL_KW:
					case GENERALIZED_TIME_KW:
					case GENERAL_STR_KW:
					case GRAPHIC_STR_KW:
					case IA5_STRING_KW:
					case INTEGER_KW:
					case ISO646_STR_KW:
					case NULL_KW:
					case NUMERIC_STR_KW:
					case OBJECT_KW:
					case OCTET_KW:
					case OPERATION_KW:
					case PRINTABLE_STR_KW:
					case REAL_KW:
					case RELATIVE_KW:
					case SEQUENCE_KW:
					case SET_KW:
					case TELETEX_STR_KW:
					case T61_STR_KW:
					case UNIVERSAL_STR_KW:
					case UTC_TIME_KW:
					case UTF8_STR_KW:
					case VIDEOTEX_STR_KW:
					case VISIBLE_STR_KW:
					case L_BRACKET:
					case UPPER:
					case LOWER:
					case 147: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					obj1 = type();
					if (inputState.guessing == 0) {
						if ((AsnDefinedType.class).isInstance(obj1)) {
							tgtyp.isDefinedType = true;
							tgtyp.typeName = ((AsnDefinedType) obj1).typeName;
						}
						else {
							tgtyp.typeReference = obj1;
						}

					}
				}
			}
			if (inputState.guessing == 0) {
				obj = tgtyp;
				tg = null;
				obj1 = null;
				tgtyp = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final AsnNamedNumberList namedNumber_list() throws RecognitionException, TokenStreamException {
		AsnNamedNumberList nnlist;

		nnlist = new AsnNamedNumberList();
		AsnNamedNumber nnum;

		try { // for error handling
			{
				match(L_BRACE);
				{
					nnum = namedNumber();
					if (inputState.guessing == 0) {
						nnlist.namedNumbers.add(nnum);
					}
				}
				{
					_loop344: do {
						if ((LA(1) == COMMA)) {
							match(COMMA);
							{
								nnum = namedNumber();
								if (inputState.guessing == 0) {
									nnlist.namedNumbers.add(nnum);
								}
							}
						}
						else {
							break _loop344;
						}

					} while (true);
				}
				match(R_BRACE);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_31);
			}
			else {
				throw ex;
			}
		}
		return nnlist;
	}

	public final AsnConstraint constraint() throws RecognitionException, TokenStreamException {
		AsnConstraint cnstrnt;

		cnstrnt = new AsnConstraint();

		try { // for error handling
			if ((_tokenSet_39.member(LA(1))) && (_tokenSet_34.member(LA(2))) && (_tokenSet_35.member(LA(3)))) {
				{
					_loop353: do {
						if ((LA(1) == L_PAREN)) {
							match(L_PAREN);
							{
								switch (LA(1)) {
								case ALL_KW:
								case ANY_KW:
								case ANY_NODECODE_KW:
								case BIT_KW:
								case BMP_STR_KW:
								case BOOLEAN_KW:
								case CHARACTER_KW:
								case CHOICE_KW:
								case EMBEDDED_KW:
								case ENUMERATED_KW:
								case ERROR_KW:
								case EXTERNAL_KW:
								case FALSE_KW:
								case FROM_KW:
								case GENERALIZED_TIME_KW:
								case GENERAL_STR_KW:
								case GRAPHIC_STR_KW:
								case IA5_STRING_KW:
								case INTEGER_KW:
								case ISO646_STR_KW:
								case MINUS_INFINITY_KW:
								case MIN_KW:
								case NULL_KW:
								case NUMERIC_STR_KW:
								case OBJECT_KW:
								case OCTET_KW:
								case OPERATION_KW:
								case PLUS_INFINITY_KW:
								case PRINTABLE_STR_KW:
								case REAL_KW:
								case RELATIVE_KW:
								case SEQUENCE_KW:
								case SET_KW:
								case SIZE_KW:
								case TELETEX_STR_KW:
								case TRUE_KW:
								case T61_STR_KW:
								case UNIVERSAL_STR_KW:
								case UTC_TIME_KW:
								case UTF8_STR_KW:
								case VIDEOTEX_STR_KW:
								case VISIBLE_STR_KW:
								case WITH_KW:
								case L_BRACE:
								case L_BRACKET:
								case L_PAREN:
								case MINUS:
								case NUMBER:
								case UPPER:
								case LOWER:
								case B_STRING:
								case H_STRING:
								case C_STRING:
								case 147:
								case INCLUDES:
								case PATTERN_KW: {
									element_set_specs(cnstrnt);
									if (inputState.guessing == 0) {
										cnstrnt.isElementSetSpecs = true;
									}
									break;
								}
								case EXCLAMATION:
								case R_PAREN: {
									break;
								}
								default: {
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
							}
							{
								switch (LA(1)) {
								case EXCLAMATION: {
									exception_spec(cnstrnt);
									break;
								}
								case R_PAREN: {
									break;
								}
								default: {
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
							}
							match(R_PAREN);
						}
						else {
							break _loop353;
						}

					} while (true);
				}
			}
			else if ((_tokenSet_40.member(LA(1))) && (_tokenSet_32.member(LA(2))) && (_tokenSet_33.member(LA(3)))) {
				{
					if ((_tokenSet_41.member(LA(1))) && (_tokenSet_42.member(LA(2))) && (_tokenSet_43.member(LA(3)))) {
						element_set_specs(cnstrnt);
						if (inputState.guessing == 0) {
							cnstrnt.isElementSetSpecs = true;
						}
					}
					else if ((_tokenSet_44.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				{
					if ((LA(1) == EXCLAMATION) && (_tokenSet_45.member(LA(2))) && (_tokenSet_46.member(LA(3)))) {
						exception_spec(cnstrnt);
					}
					else if ((_tokenSet_44.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}

		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_44);
			}
			else {
				throw ex;
			}
		}
		return cnstrnt;
	}

	public final String character_set() throws RecognitionException, TokenStreamException {
		String s;

		Token s1 = null;
		Token s2 = null;
		Token s3 = null;
		Token s4 = null;
		Token s5 = null;
		Token s6 = null;
		Token s7 = null;
		Token s8 = null;
		Token s9 = null;
		Token s10 = null;
		Token s11 = null;
		Token s12 = null;
		Token s13 = null;
		Token s14 = null;
		Token s15 = null;
		s = "";

		try { // for error handling
			switch (LA(1)) {
			case BMP_STR_KW: {
				{
					s1 = LT(1);
					match(BMP_STR_KW);
					if (inputState.guessing == 0) {
						s = s1.getText();
					}
				}
				break;
			}
			case GENERALIZED_TIME_KW: {
				{
					s2 = LT(1);
					match(GENERALIZED_TIME_KW);
					if (inputState.guessing == 0) {
						s = s2.getText();
					}
				}
				break;
			}
			case GENERAL_STR_KW: {
				{
					s3 = LT(1);
					match(GENERAL_STR_KW);
					if (inputState.guessing == 0) {
						s = s3.getText();
					}
				}
				break;
			}
			case GRAPHIC_STR_KW: {
				{
					s4 = LT(1);
					match(GRAPHIC_STR_KW);
					if (inputState.guessing == 0) {
						s = s4.getText();
					}
				}
				break;
			}
			case IA5_STRING_KW: {
				{
					s5 = LT(1);
					match(IA5_STRING_KW);
					if (inputState.guessing == 0) {
						s = s5.getText();
					}
				}
				break;
			}
			case ISO646_STR_KW: {
				{
					s6 = LT(1);
					match(ISO646_STR_KW);
					if (inputState.guessing == 0) {
						s = s6.getText();
					}
				}
				break;
			}
			case NUMERIC_STR_KW: {
				{
					s7 = LT(1);
					match(NUMERIC_STR_KW);
					if (inputState.guessing == 0) {
						s = s7.getText();
					}
				}
				break;
			}
			case PRINTABLE_STR_KW: {
				{
					s8 = LT(1);
					match(PRINTABLE_STR_KW);
					if (inputState.guessing == 0) {
						s = s8.getText();
					}
				}
				break;
			}
			case TELETEX_STR_KW: {
				{
					s9 = LT(1);
					match(TELETEX_STR_KW);
					if (inputState.guessing == 0) {
						s = s9.getText();
					}
				}
				break;
			}
			case T61_STR_KW: {
				{
					s10 = LT(1);
					match(T61_STR_KW);
					if (inputState.guessing == 0) {
						s = s10.getText();
					}
				}
				break;
			}
			case UNIVERSAL_STR_KW: {
				{
					s11 = LT(1);
					match(UNIVERSAL_STR_KW);
					if (inputState.guessing == 0) {
						s = s11.getText();
					}
				}
				break;
			}
			case UTF8_STR_KW: {
				{
					s12 = LT(1);
					match(UTF8_STR_KW);
					if (inputState.guessing == 0) {
						s = s12.getText();
					}
				}
				break;
			}
			case UTC_TIME_KW: {
				{
					s13 = LT(1);
					match(UTC_TIME_KW);
					if (inputState.guessing == 0) {
						s = s13.getText();
					}
				}
				break;
			}
			case VIDEOTEX_STR_KW: {
				{
					s14 = LT(1);
					match(VIDEOTEX_STR_KW);
					if (inputState.guessing == 0) {
						s = s14.getText();
					}
				}
				break;
			}
			case VISIBLE_STR_KW: {
				{
					s15 = LT(1);
					match(VISIBLE_STR_KW);
					if (inputState.guessing == 0) {
						s = s15.getText();
					}
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_31);
			}
			else {
				throw ex;
			}
		}
		return s;
	}

	public final AsnElementTypeList elementType_list() throws RecognitionException, TokenStreamException {
		AsnElementTypeList elelist;

		elelist = new AsnElementTypeList();
		AsnElementType eletyp;

		try { // for error handling
			{
				eletyp = elementType();
				if (inputState.guessing == 0) {
					elelist.elements.add(eletyp);
				}
				{
					_loop328: do {
						if ((LA(1) == COMMA)) {
							match(COMMA);
							{
								eletyp = elementType();
								if (inputState.guessing == 0) {
									elelist.elements.add(eletyp);
								}
							}
						}
						else {
							break _loop328;
						}

					} while (true);
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_47);
			}
			else {
				throw ex;
			}
		}
		return elelist;
	}

	public final AsnTag tag() throws RecognitionException, TokenStreamException {
		AsnTag tg;

		tg = new AsnTag();
		String s;
		AsnClassNumber cnum;

		try { // for error handling
			{
				match(L_BRACKET);
				{
					switch (LA(1)) {
					case APPLICATION_KW:
					case PRIVATE_KW:
					case UNIVERSAL_KW: {
						s = clazz();
						if (inputState.guessing == 0) {
							tg.clazz = s;
						}
						break;
					}
					case NUMBER:
					case LOWER: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					cnum = class_NUMBER();
					if (inputState.guessing == 0) {
						tg.classNumber = cnum;
					}
				}
				match(R_BRACKET);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_48);
			}
			else {
				throw ex;
			}
		}
		return tg;
	}

	public final String clazz() throws RecognitionException, TokenStreamException {
		String s;

		Token c1 = null;
		Token c2 = null;
		Token c3 = null;
		s = "";

		try { // for error handling
			switch (LA(1)) {
			case UNIVERSAL_KW: {
				{
					c1 = LT(1);
					match(UNIVERSAL_KW);
					if (inputState.guessing == 0) {
						s = c1.getText();
					}
				}
				break;
			}
			case APPLICATION_KW: {
				{
					c2 = LT(1);
					match(APPLICATION_KW);
					if (inputState.guessing == 0) {
						s = c2.getText();
					}
				}
				break;
			}
			case PRIVATE_KW: {
				{
					c3 = LT(1);
					match(PRIVATE_KW);
					if (inputState.guessing == 0) {
						s = c3.getText();
					}
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_49);
			}
			else {
				throw ex;
			}
		}
		return s;
	}

	public final AsnClassNumber class_NUMBER() throws RecognitionException, TokenStreamException {
		AsnClassNumber cnum;

		Token num = null;
		Token lid = null;
		cnum = new AsnClassNumber();
		String s;

		try { // for error handling
			{
				switch (LA(1)) {
				case NUMBER: {
					{
						num = LT(1);
						match(NUMBER);
						if (inputState.guessing == 0) {
							s = num.getText();
							cnum.num = new Integer(s);
						}
					}
					break;
				}
				case LOWER: {
					{
						lid = LT(1);
						match(LOWER);
						if (inputState.guessing == 0) {
							s = lid.getText();
							cnum.name = s;
						}
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_50);
			}
			else {
				throw ex;
			}
		}
		return cnum;
	}

	public final Object operation_macro() throws RecognitionException, TokenStreamException {
		Object obj;

		Token ld1 = null;
		Token ld2 = null;
		OperationMacro op = new OperationMacro();
		String s;
		obj = null;
		Object obj1;
		Object obj2;

		try { // for error handling
			{
				match(OPERATION_KW);
				{
					switch (LA(1)) {
					case ARGUMENT_KW: {
						match(ARGUMENT_KW);
						{
							if ((LA(1) == LOWER) && (_tokenSet_12.member(LA(2))) && (_tokenSet_38.member(LA(3)))) {
								ld1 = LT(1);
								match(LOWER);
								if (inputState.guessing == 0) {
									op.argumentTypeIdentifier = ld1.getText();
								}
							}
							else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_38.member(LA(2)))
									&& (_tokenSet_51.member(LA(3)))) {
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}

						}
						{
							{
								obj2 = type();
							}
							if (inputState.guessing == 0) {
								op.argumentType = obj2;
								op.isArgumentName = true;
								if ((AsnDefinedType.class).isInstance(obj2)) {
									op.argumentName = ((AsnDefinedType) obj2).typeName;
								}
								else {
									op.argumentName = op.argumentTypeIdentifier;
								}

							}
						}
						break;
					}
					case ANY_KW:
					case ANY_NODECODE_KW:
					case BIT_KW:
					case BMP_STR_KW:
					case BOOLEAN_KW:
					case CHARACTER_KW:
					case CHOICE_KW:
					case DEFAULT_KW:
					case EMBEDDED_KW:
					case END_KW:
					case ENUMERATED_KW:
					case ERROR_KW:
					case ERRORS_KW:
					case EXTERNAL_KW:
					case FALSE_KW:
					case GENERALIZED_TIME_KW:
					case GENERAL_STR_KW:
					case GRAPHIC_STR_KW:
					case IA5_STRING_KW:
					case INTEGER_KW:
					case INTERSECTION_KW:
					case ISO646_STR_KW:
					case LINKED_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case NUMERIC_STR_KW:
					case OBJECT_KW:
					case OCTET_KW:
					case OPERATION_KW:
					case OPTIONAL_KW:
					case PLUS_INFINITY_KW:
					case PRINTABLE_STR_KW:
					case REAL_KW:
					case RELATIVE_KW:
					case RESULT_KW:
					case SEQUENCE_KW:
					case SET_KW:
					case TELETEX_STR_KW:
					case TRUE_KW:
					case T61_STR_KW:
					case UNION_KW:
					case UNIVERSAL_STR_KW:
					case UTC_TIME_KW:
					case UTF8_STR_KW:
					case VIDEOTEX_STR_KW:
					case VISIBLE_STR_KW:
					case ASSIGN_OP:
					case BAR:
					case COLON:
					case COMMA:
					case EXCLAMATION:
					case INTERSECTION:
					case L_BRACE:
					case L_BRACKET:
					case MINUS:
					case R_BRACE:
					case R_PAREN:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING:
					case 147:
					case LITERAL_ACCESS:
					case EXCEPT: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					if ((LA(1) == RESULT_KW) && (_tokenSet_52.member(LA(2))) && (_tokenSet_34.member(LA(3)))) {
						match(RESULT_KW);
						if (inputState.guessing == 0) {
							op.isResult = true;
						}
						{
							boolean synPredMatched278 = false;
							if (((LA(1) == SEMI))) {
								int _m278 = mark();
								synPredMatched278 = true;
								inputState.guessing++;
								try {
									{
										match(SEMI);
									}
								} catch (RecognitionException pe) {
									synPredMatched278 = false;
								}
								rewind(_m278);
								inputState.guessing--;
							}
							if (synPredMatched278) {
								match(SEMI);
							}
							else {
								boolean synPredMatched281 = false;
								if (((_tokenSet_12.member(LA(1))) && (_tokenSet_38.member(LA(2)))
										&& (_tokenSet_51.member(LA(3))))) {
									int _m281 = mark();
									synPredMatched281 = true;
									inputState.guessing++;
									try {
										{
											{
												if ((LA(1) == LOWER) && (_tokenSet_12.member(LA(2))) && (true)) {
													match(LOWER);
												}
												else if ((_tokenSet_12.member(LA(1))) && (true) && (true)) {
												}
												else {
													throw new NoViableAltException(LT(1), getFilename());
												}

											}
											type();
										}
									} catch (RecognitionException pe) {
										synPredMatched281 = false;
									}
									rewind(_m281);
									inputState.guessing--;
								}
								if (synPredMatched281) {
									{
										if ((LA(1) == LOWER) && (_tokenSet_12.member(LA(2)))
												&& (_tokenSet_38.member(LA(3)))) {
											ld2 = LT(1);
											match(LOWER);
											if (inputState.guessing == 0) {
												op.resultTypeIdentifier = ld2.getText();
											}
										}
										else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_38.member(LA(2)))
												&& (_tokenSet_51.member(LA(3)))) {
										}
										else {
											throw new NoViableAltException(LT(1), getFilename());
										}

									}
									{
										obj1 = type();
										if (inputState.guessing == 0) {
											op.resultType = obj1;
											op.isResultName = true;
											if ((AsnDefinedType.class).isInstance(obj1)) {
												op.resultName = ((AsnDefinedType) obj1).typeName;
											}
											else {
												op.resultName = op.resultTypeIdentifier;
											}

										}
									}
								}
								else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_34.member(LA(2)))
										&& (_tokenSet_35.member(LA(3)))) {
								}
								else {
									throw new NoViableAltException(LT(1), getFilename());
								}
							}
						}
					}
					else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				{
					if ((LA(1) == ERRORS_KW) && (LA(2) == L_BRACE) && (_tokenSet_53.member(LA(3)))) {
						match(ERRORS_KW);
						match(L_BRACE);
						{
							switch (LA(1)) {
							case ANY_KW:
							case ANY_NODECODE_KW:
							case BIT_KW:
							case BMP_STR_KW:
							case BOOLEAN_KW:
							case CHARACTER_KW:
							case CHOICE_KW:
							case EMBEDDED_KW:
							case ENUMERATED_KW:
							case ERROR_KW:
							case EXTERNAL_KW:
							case FALSE_KW:
							case GENERALIZED_TIME_KW:
							case GENERAL_STR_KW:
							case GRAPHIC_STR_KW:
							case IA5_STRING_KW:
							case INTEGER_KW:
							case ISO646_STR_KW:
							case MINUS_INFINITY_KW:
							case NULL_KW:
							case NUMERIC_STR_KW:
							case OBJECT_KW:
							case OCTET_KW:
							case OPERATION_KW:
							case PLUS_INFINITY_KW:
							case PRINTABLE_STR_KW:
							case REAL_KW:
							case RELATIVE_KW:
							case SEQUENCE_KW:
							case SET_KW:
							case TELETEX_STR_KW:
							case TRUE_KW:
							case T61_STR_KW:
							case UNIVERSAL_STR_KW:
							case UTC_TIME_KW:
							case UTF8_STR_KW:
							case VIDEOTEX_STR_KW:
							case VISIBLE_STR_KW:
							case L_BRACE:
							case L_BRACKET:
							case MINUS:
							case NUMBER:
							case UPPER:
							case LOWER:
							case B_STRING:
							case H_STRING:
							case C_STRING:
							case 147: {
								operation_errorlist(op);
								if (inputState.guessing == 0) {
									op.isErrors = true;
								}
								break;
							}
							case R_BRACE: {
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						match(R_BRACE);
					}
					else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				{
					if ((LA(1) == LINKED_KW) && (LA(2) == L_BRACE) && (_tokenSet_53.member(LA(3)))) {
						match(LINKED_KW);
						match(L_BRACE);
						{
							switch (LA(1)) {
							case ANY_KW:
							case ANY_NODECODE_KW:
							case BIT_KW:
							case BMP_STR_KW:
							case BOOLEAN_KW:
							case CHARACTER_KW:
							case CHOICE_KW:
							case EMBEDDED_KW:
							case ENUMERATED_KW:
							case ERROR_KW:
							case EXTERNAL_KW:
							case FALSE_KW:
							case GENERALIZED_TIME_KW:
							case GENERAL_STR_KW:
							case GRAPHIC_STR_KW:
							case IA5_STRING_KW:
							case INTEGER_KW:
							case ISO646_STR_KW:
							case MINUS_INFINITY_KW:
							case NULL_KW:
							case NUMERIC_STR_KW:
							case OBJECT_KW:
							case OCTET_KW:
							case OPERATION_KW:
							case PLUS_INFINITY_KW:
							case PRINTABLE_STR_KW:
							case REAL_KW:
							case RELATIVE_KW:
							case SEQUENCE_KW:
							case SET_KW:
							case TELETEX_STR_KW:
							case TRUE_KW:
							case T61_STR_KW:
							case UNIVERSAL_STR_KW:
							case UTC_TIME_KW:
							case UTF8_STR_KW:
							case VIDEOTEX_STR_KW:
							case VISIBLE_STR_KW:
							case L_BRACE:
							case L_BRACKET:
							case MINUS:
							case NUMBER:
							case UPPER:
							case LOWER:
							case B_STRING:
							case H_STRING:
							case C_STRING:
							case 147: {
								linkedOp_list(op);
								if (inputState.guessing == 0) {
									op.isLinkedOperation = true;
								}
								break;
							}
							case R_BRACE: {
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						match(R_BRACE);
					}
					else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
			}
			if (inputState.guessing == 0) {
				obj = op;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object error_macro() throws RecognitionException, TokenStreamException {
		Object obj;

		Token lw = null;
		ErrorMacro merr = new ErrorMacro();
		obj = null;
		Object obj1;

		try { // for error handling
			{
				match(ERROR_KW);
				{
					switch (LA(1)) {
					case PARAMETER_KW: {
						match(PARAMETER_KW);
						if (inputState.guessing == 0) {
							merr.isParameter = true;
						}
						{
							{
								if ((LA(1) == LOWER) && (_tokenSet_12.member(LA(2))) && (_tokenSet_38.member(LA(3)))) {
									lw = LT(1);
									match(LOWER);
									if (inputState.guessing == 0) {
										merr.parameterName = lw.getText();
									}
								}
								else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_38.member(LA(2)))
										&& (_tokenSet_51.member(LA(3)))) {
								}
								else {
									throw new NoViableAltException(LT(1), getFilename());
								}

							}
							{
								obj1 = type();
								if (inputState.guessing == 0) {
									if ((AsnDefinedType.class).isInstance(obj1)) {
										merr.isDefinedType = true;
										merr.typeName = ((AsnDefinedType) obj1).typeName;
									}
									else {
										merr.typeReference = obj1;
									}

								}
							}
						}
						break;
					}
					case ANY_KW:
					case ANY_NODECODE_KW:
					case BIT_KW:
					case BMP_STR_KW:
					case BOOLEAN_KW:
					case CHARACTER_KW:
					case CHOICE_KW:
					case DEFAULT_KW:
					case EMBEDDED_KW:
					case END_KW:
					case ENUMERATED_KW:
					case ERROR_KW:
					case ERRORS_KW:
					case EXTERNAL_KW:
					case FALSE_KW:
					case GENERALIZED_TIME_KW:
					case GENERAL_STR_KW:
					case GRAPHIC_STR_KW:
					case IA5_STRING_KW:
					case INTEGER_KW:
					case INTERSECTION_KW:
					case ISO646_STR_KW:
					case LINKED_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case NUMERIC_STR_KW:
					case OBJECT_KW:
					case OCTET_KW:
					case OPERATION_KW:
					case OPTIONAL_KW:
					case PLUS_INFINITY_KW:
					case PRINTABLE_STR_KW:
					case REAL_KW:
					case RELATIVE_KW:
					case RESULT_KW:
					case SEQUENCE_KW:
					case SET_KW:
					case TELETEX_STR_KW:
					case TRUE_KW:
					case T61_STR_KW:
					case UNION_KW:
					case UNIVERSAL_STR_KW:
					case UTC_TIME_KW:
					case UTF8_STR_KW:
					case VIDEOTEX_STR_KW:
					case VISIBLE_STR_KW:
					case ASSIGN_OP:
					case BAR:
					case COLON:
					case COMMA:
					case EXCLAMATION:
					case INTERSECTION:
					case L_BRACE:
					case L_BRACKET:
					case MINUS:
					case R_BRACE:
					case R_PAREN:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING:
					case 147:
					case LITERAL_ACCESS:
					case EXCEPT: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
			}
			if (inputState.guessing == 0) {
				obj = merr;
				merr = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final Object objecttype_macro() throws RecognitionException, TokenStreamException {
		Object obj;

		Token lid = null;
		Token lid1 = null;
		ObjectType objtype = new ObjectType();
		AsnValue val;
		obj = null;
		String s;
		Object typ;

		try { // for error handling
			{
				match(147);
				match(LITERAL_SYNTAX);
				{
					typ = type();
					if (inputState.guessing == 0) {
						objtype.type = typ;
					}
				}
				{
					match(LITERAL_ACCESS);
					lid = LT(1);
					match(LOWER);
					if (inputState.guessing == 0) {
						objtype.accessPart = lid.getText();
					}
				}
				{
					match(LITERAL_STATUS);
					lid1 = LT(1);
					match(LOWER);
					if (inputState.guessing == 0) {
						objtype.statusPart = lid1.getText();
					}
				}
				{
					switch (LA(1)) {
					case LITERAL_DESCRIPTION: {
						match(LITERAL_DESCRIPTION);
						match(CHARACTER_KW);
						match(STRING_KW);
						break;
					}
					case ANY_KW:
					case ANY_NODECODE_KW:
					case BIT_KW:
					case BMP_STR_KW:
					case BOOLEAN_KW:
					case CHARACTER_KW:
					case CHOICE_KW:
					case DEFAULT_KW:
					case EMBEDDED_KW:
					case END_KW:
					case ENUMERATED_KW:
					case ERROR_KW:
					case ERRORS_KW:
					case EXTERNAL_KW:
					case FALSE_KW:
					case GENERALIZED_TIME_KW:
					case GENERAL_STR_KW:
					case GRAPHIC_STR_KW:
					case IA5_STRING_KW:
					case INTEGER_KW:
					case INTERSECTION_KW:
					case ISO646_STR_KW:
					case LINKED_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case NUMERIC_STR_KW:
					case OBJECT_KW:
					case OCTET_KW:
					case OPERATION_KW:
					case OPTIONAL_KW:
					case PLUS_INFINITY_KW:
					case PRINTABLE_STR_KW:
					case REAL_KW:
					case RELATIVE_KW:
					case RESULT_KW:
					case SEQUENCE_KW:
					case SET_KW:
					case TELETEX_STR_KW:
					case TRUE_KW:
					case T61_STR_KW:
					case UNION_KW:
					case UNIVERSAL_STR_KW:
					case UTC_TIME_KW:
					case UTF8_STR_KW:
					case VIDEOTEX_STR_KW:
					case VISIBLE_STR_KW:
					case ASSIGN_OP:
					case BAR:
					case COLON:
					case COMMA:
					case EXCLAMATION:
					case INTERSECTION:
					case L_BRACE:
					case L_BRACKET:
					case MINUS:
					case R_BRACE:
					case R_PAREN:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING:
					case 147:
					case LITERAL_ACCESS:
					case LITERAL_REFERENCE:
					case LITERAL_INDEX:
					case LITERAL_DEFVAL:
					case EXCEPT: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					switch (LA(1)) {
					case LITERAL_REFERENCE: {
						match(LITERAL_REFERENCE);
						match(CHARACTER_KW);
						match(STRING_KW);
						break;
					}
					case ANY_KW:
					case ANY_NODECODE_KW:
					case BIT_KW:
					case BMP_STR_KW:
					case BOOLEAN_KW:
					case CHARACTER_KW:
					case CHOICE_KW:
					case DEFAULT_KW:
					case EMBEDDED_KW:
					case END_KW:
					case ENUMERATED_KW:
					case ERROR_KW:
					case ERRORS_KW:
					case EXTERNAL_KW:
					case FALSE_KW:
					case GENERALIZED_TIME_KW:
					case GENERAL_STR_KW:
					case GRAPHIC_STR_KW:
					case IA5_STRING_KW:
					case INTEGER_KW:
					case INTERSECTION_KW:
					case ISO646_STR_KW:
					case LINKED_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case NUMERIC_STR_KW:
					case OBJECT_KW:
					case OCTET_KW:
					case OPERATION_KW:
					case OPTIONAL_KW:
					case PLUS_INFINITY_KW:
					case PRINTABLE_STR_KW:
					case REAL_KW:
					case RELATIVE_KW:
					case RESULT_KW:
					case SEQUENCE_KW:
					case SET_KW:
					case TELETEX_STR_KW:
					case TRUE_KW:
					case T61_STR_KW:
					case UNION_KW:
					case UNIVERSAL_STR_KW:
					case UTC_TIME_KW:
					case UTF8_STR_KW:
					case VIDEOTEX_STR_KW:
					case VISIBLE_STR_KW:
					case ASSIGN_OP:
					case BAR:
					case COLON:
					case COMMA:
					case EXCLAMATION:
					case INTERSECTION:
					case L_BRACE:
					case L_BRACKET:
					case MINUS:
					case R_BRACE:
					case R_PAREN:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING:
					case 147:
					case LITERAL_ACCESS:
					case LITERAL_INDEX:
					case LITERAL_DEFVAL:
					case EXCEPT: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					switch (LA(1)) {
					case LITERAL_INDEX: {
						match(LITERAL_INDEX);
						match(L_BRACE);
						{
							typeorvaluelist(objtype);
						}
						match(R_BRACE);
						break;
					}
					case ANY_KW:
					case ANY_NODECODE_KW:
					case BIT_KW:
					case BMP_STR_KW:
					case BOOLEAN_KW:
					case CHARACTER_KW:
					case CHOICE_KW:
					case DEFAULT_KW:
					case EMBEDDED_KW:
					case END_KW:
					case ENUMERATED_KW:
					case ERROR_KW:
					case ERRORS_KW:
					case EXTERNAL_KW:
					case FALSE_KW:
					case GENERALIZED_TIME_KW:
					case GENERAL_STR_KW:
					case GRAPHIC_STR_KW:
					case IA5_STRING_KW:
					case INTEGER_KW:
					case INTERSECTION_KW:
					case ISO646_STR_KW:
					case LINKED_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case NUMERIC_STR_KW:
					case OBJECT_KW:
					case OCTET_KW:
					case OPERATION_KW:
					case OPTIONAL_KW:
					case PLUS_INFINITY_KW:
					case PRINTABLE_STR_KW:
					case REAL_KW:
					case RELATIVE_KW:
					case RESULT_KW:
					case SEQUENCE_KW:
					case SET_KW:
					case TELETEX_STR_KW:
					case TRUE_KW:
					case T61_STR_KW:
					case UNION_KW:
					case UNIVERSAL_STR_KW:
					case UTC_TIME_KW:
					case UTF8_STR_KW:
					case VIDEOTEX_STR_KW:
					case VISIBLE_STR_KW:
					case ASSIGN_OP:
					case BAR:
					case COLON:
					case COMMA:
					case EXCLAMATION:
					case INTERSECTION:
					case L_BRACE:
					case L_BRACKET:
					case MINUS:
					case R_BRACE:
					case R_PAREN:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING:
					case 147:
					case LITERAL_ACCESS:
					case LITERAL_DEFVAL:
					case EXCEPT: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					switch (LA(1)) {
					case LITERAL_DEFVAL: {
						match(LITERAL_DEFVAL);
						match(L_BRACE);
						{
							val = value();
							if (inputState.guessing == 0) {
								objtype.value = val;
							}
						}
						match(R_BRACE);
						break;
					}
					case ANY_KW:
					case ANY_NODECODE_KW:
					case BIT_KW:
					case BMP_STR_KW:
					case BOOLEAN_KW:
					case CHARACTER_KW:
					case CHOICE_KW:
					case DEFAULT_KW:
					case EMBEDDED_KW:
					case END_KW:
					case ENUMERATED_KW:
					case ERROR_KW:
					case ERRORS_KW:
					case EXTERNAL_KW:
					case FALSE_KW:
					case GENERALIZED_TIME_KW:
					case GENERAL_STR_KW:
					case GRAPHIC_STR_KW:
					case IA5_STRING_KW:
					case INTEGER_KW:
					case INTERSECTION_KW:
					case ISO646_STR_KW:
					case LINKED_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case NUMERIC_STR_KW:
					case OBJECT_KW:
					case OCTET_KW:
					case OPERATION_KW:
					case OPTIONAL_KW:
					case PLUS_INFINITY_KW:
					case PRINTABLE_STR_KW:
					case REAL_KW:
					case RELATIVE_KW:
					case RESULT_KW:
					case SEQUENCE_KW:
					case SET_KW:
					case TELETEX_STR_KW:
					case TRUE_KW:
					case T61_STR_KW:
					case UNION_KW:
					case UNIVERSAL_STR_KW:
					case UTC_TIME_KW:
					case UTF8_STR_KW:
					case VIDEOTEX_STR_KW:
					case VISIBLE_STR_KW:
					case ASSIGN_OP:
					case BAR:
					case COLON:
					case COMMA:
					case EXCLAMATION:
					case INTERSECTION:
					case L_BRACE:
					case L_BRACKET:
					case MINUS:
					case R_BRACE:
					case R_PAREN:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING:
					case 147:
					case LITERAL_ACCESS:
					case EXCEPT: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
			}
			if (inputState.guessing == 0) {
				obj = objtype;
				objtype = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_23);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final void operation_errorlist(OperationMacro oper) throws RecognitionException, TokenStreamException {

		Object obj;

		try { // for error handling
			obj = typeorvalue();
			if (inputState.guessing == 0) {
				oper.errorList.add(obj);
			}
			{
				_loop291: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						{
							obj = typeorvalue();
							if (inputState.guessing == 0) {
								oper.errorList.add(obj);
							}
						}
					}
					else {
						break _loop291;
					}

				} while (true);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_47);
			}
			else {
				throw ex;
			}
		}
	}

	public final void linkedOp_list(OperationMacro oper) throws RecognitionException, TokenStreamException {

		Object obj;

		try { // for error handling
			obj = typeorvalue();
			if (inputState.guessing == 0) {
				oper.linkedOpList.add(obj);
			}
			{
				_loop295: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						{
							obj = typeorvalue();
							if (inputState.guessing == 0) {
								oper.linkedOpList.add(obj);
							}
						}
					}
					else {
						break _loop295;
					}

				} while (true);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_47);
			}
			else {
				throw ex;
			}
		}
	}

	public final Object typeorvalue() throws RecognitionException, TokenStreamException {
		Object obj;

		Object obj1;
		obj = null;

		try { // for error handling
			{
				boolean synPredMatched322 = false;
				if (((_tokenSet_12.member(LA(1))) && (_tokenSet_54.member(LA(2))) && (_tokenSet_55.member(LA(3))))) {
					int _m322 = mark();
					synPredMatched322 = true;
					inputState.guessing++;
					try {
						{
							type();
						}
					} catch (RecognitionException pe) {
						synPredMatched322 = false;
					}
					rewind(_m322);
					inputState.guessing--;
				}
				if (synPredMatched322) {
					{
						obj1 = type();
					}
				}
				else if ((_tokenSet_56.member(LA(1))) && (_tokenSet_57.member(LA(2))) && (_tokenSet_58.member(LA(3)))) {
					obj1 = value();
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
			if (inputState.guessing == 0) {
				obj = obj1;
				obj1 = null;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_59);
			}
			else {
				throw ex;
			}
		}
		return obj;
	}

	public final void typeorvaluelist(ObjectType objtype) throws RecognitionException, TokenStreamException {

		Object obj;

		try { // for error handling
			{
				{
					obj = typeorvalue();
					if (inputState.guessing == 0) {
						objtype.elements.add(obj);
					}
				}
				{
					match(COMMA);
					{
						_loop318: do {
							if ((_tokenSet_60.member(LA(1)))) {
								obj = typeorvalue();
								if (inputState.guessing == 0) {
									objtype.elements.add(obj);
								}
							}
							else {
								break _loop318;
							}

						} while (true);
					}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_47);
			}
			else {
				throw ex;
			}
		}
	}

	public final AsnElementType elementType() throws RecognitionException, TokenStreamException {
		AsnElementType eletyp;

		Token lid = null;
		eletyp = new AsnElementType();
		AsnValue val;
		Object obj;
		AsnTag tg;
		String s;

		try { // for error handling
			{
				switch (LA(1)) {
				case LOWER: {
					{
						lid = LT(1);
						match(LOWER);
						if (inputState.guessing == 0) {
							eletyp.name = lid.getText();
						}
						{
							if ((LA(1) == L_BRACKET) && (_tokenSet_61.member(LA(2)))
									&& (LA(3) == R_BRACKET || LA(3) == NUMBER || LA(3) == LOWER)) {
								tg = tag();
								if (inputState.guessing == 0) {
									eletyp.tag = tg;
								}
							}
							else if ((_tokenSet_48.member(LA(1))) && (_tokenSet_62.member(LA(2)))
									&& (_tokenSet_63.member(LA(3)))) {
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}

						}
						{
							switch (LA(1)) {
							case AUTOMATIC_KW:
							case EXPLICIT_KW:
							case IMPLICIT_KW: {
								s = tag_default();
								if (inputState.guessing == 0) {
									eletyp.tagType = s;
								}
								break;
							}
							case ANY_KW:
							case ANY_NODECODE_KW:
							case BIT_KW:
							case BMP_STR_KW:
							case BOOLEAN_KW:
							case CHARACTER_KW:
							case CHOICE_KW:
							case EMBEDDED_KW:
							case ENUMERATED_KW:
							case ERROR_KW:
							case EXTERNAL_KW:
							case GENERALIZED_TIME_KW:
							case GENERAL_STR_KW:
							case GRAPHIC_STR_KW:
							case IA5_STRING_KW:
							case INTEGER_KW:
							case ISO646_STR_KW:
							case NULL_KW:
							case NUMERIC_STR_KW:
							case OBJECT_KW:
							case OCTET_KW:
							case OPERATION_KW:
							case PRINTABLE_STR_KW:
							case REAL_KW:
							case RELATIVE_KW:
							case SEQUENCE_KW:
							case SET_KW:
							case TELETEX_STR_KW:
							case T61_STR_KW:
							case UNIVERSAL_STR_KW:
							case UTC_TIME_KW:
							case UTF8_STR_KW:
							case VIDEOTEX_STR_KW:
							case VISIBLE_STR_KW:
							case L_BRACKET:
							case UPPER:
							case LOWER:
							case 147: {
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						{
							obj = type();
						}
						{
							switch (LA(1)) {
							case OPTIONAL_KW: {
								{
									match(OPTIONAL_KW);
									if (inputState.guessing == 0) {
										eletyp.isOptional = true;
									}
								}
								break;
							}
							case DEFAULT_KW: {
								{
									match(DEFAULT_KW);
									if (inputState.guessing == 0) {
										eletyp.isDefault = true;
									}
									val = value();
									if (inputState.guessing == 0) {
										eletyp.value = val;
									}
								}
								break;
							}
							case COMMA:
							case R_BRACE: {
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
					}
					break;
				}
				case COMPONENTS_KW: {
					match(COMPONENTS_KW);
					match(OF_KW);
					if (inputState.guessing == 0) {
						eletyp.isComponentsOf = true;
					}
					{
						obj = type();
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			if (inputState.guessing == 0) {

				if ((AsnDefinedType.class).isInstance(obj)) {
					eletyp.isDefinedType = true;
					eletyp.typeName = ((AsnDefinedType) obj).typeName;
				}
				else {
					eletyp.typeReference = obj;
				}

			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_64);
			}
			else {
				throw ex;
			}
		}
		return eletyp;
	}

	public final AsnNamedNumber namedNumber() throws RecognitionException, TokenStreamException {
		AsnNamedNumber nnum;

		Token lid = null;
		nnum = new AsnNamedNumber();
		AsnSignedNumber i;
		AsnDefinedValue s;

		try { // for error handling
			{
				lid = LT(1);
				match(LOWER);
				if (inputState.guessing == 0) {
					nnum.name = lid.getText();
				}
				match(L_PAREN);
				{
					switch (LA(1)) {
					case MINUS:
					case NUMBER: {
						i = signed_number();
						if (inputState.guessing == 0) {
							nnum.signedNumber = i;
							nnum.isSignedNumber = true;
						}
						break;
					}
					case UPPER:
					case LOWER: {
						{
							s = defined_value();
							if (inputState.guessing == 0) {
								nnum.definedValue = s;
							}
						}
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				match(R_PAREN);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_64);
			}
			else {
				throw ex;
			}
		}
		return nnum;
	}

	public final AsnSignedNumber signed_number() throws RecognitionException, TokenStreamException {
		AsnSignedNumber i;

		Token n = null;
		i = new AsnSignedNumber();
		String s;

		try { // for error handling
			{
				{
					switch (LA(1)) {
					case MINUS: {
						match(MINUS);
						if (inputState.guessing == 0) {
							i.positive = false;
						}
						break;
					}
					case NUMBER: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					n = LT(1);
					match(NUMBER);
					if (inputState.guessing == 0) {
						s = n.getText();
						i.num = new BigInteger(s);
					}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_27);
			}
			else {
				throw ex;
			}
		}
		return i;
	}

	public final void element_set_specs(AsnConstraint cnstrnt) throws RecognitionException, TokenStreamException {

		ElementSetSpec elemspec;

		try { // for error handling
			{
				elemspec = element_set_spec();
				if (inputState.guessing == 0) {

					cnstrnt.elemSetSpec = elemspec; // TODO - need list.add() func

				}
				{
					if ((LA(1) == COMMA) && (LA(2) == ELLIPSIS) && (_tokenSet_44.member(LA(3)))) {
						match(COMMA);
						match(ELLIPSIS);
						if (inputState.guessing == 0) {
							cnstrnt.isCommaDotDot = true;
						}
					}
					else if ((_tokenSet_44.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				{
					if ((LA(1) == COMMA) && (_tokenSet_41.member(LA(2))) && (_tokenSet_42.member(LA(3)))) {
						match(COMMA);
						elemspec = element_set_spec();
						if (inputState.guessing == 0) {
							cnstrnt.addElemSetSpec = elemspec;
							cnstrnt.isAdditionalElementSpec = true;
						}
					}
					else if ((_tokenSet_44.member(LA(1))) && (_tokenSet_34.member(LA(2)))
							&& (_tokenSet_35.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_44);
			}
			else {
				throw ex;
			}
		}
	}

	public final void exception_spec(AsnConstraint cnstrnt) throws RecognitionException, TokenStreamException {

		AsnSignedNumber signum;
		AsnDefinedValue defval;
		Object typ;
		AsnValue val;

		try { // for error handling
			{
				match(EXCLAMATION);
				{
					boolean synPredMatched360 = false;
					if (((LA(1) == MINUS || LA(1) == NUMBER))) {
						int _m360 = mark();
						synPredMatched360 = true;
						inputState.guessing++;
						try {
							{
								signed_number();
							}
						} catch (RecognitionException pe) {
							synPredMatched360 = false;
						}
						rewind(_m360);
						inputState.guessing--;
					}
					if (synPredMatched360) {
						{
							signum = signed_number();
							if (inputState.guessing == 0) {
								cnstrnt.isSignedNumber = true;
								cnstrnt.signedNumber = signum;
							}
						}
					}
					else {
						boolean synPredMatched363 = false;
						if (((LA(1) == UPPER || LA(1) == LOWER) && (_tokenSet_65.member(LA(2)))
								&& (_tokenSet_34.member(LA(3))))) {
							int _m363 = mark();
							synPredMatched363 = true;
							inputState.guessing++;
							try {
								{
									defined_value();
								}
							} catch (RecognitionException pe) {
								synPredMatched363 = false;
							}
							rewind(_m363);
							inputState.guessing--;
						}
						if (synPredMatched363) {
							{
								defval = defined_value();
								if (inputState.guessing == 0) {
									cnstrnt.isDefinedValue = true;
									cnstrnt.definedValue = defval;
								}
							}
						}
						else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_66.member(LA(2)))
								&& (_tokenSet_67.member(LA(3)))) {
							typ = type();
							match(COLON);
							val = value();
							if (inputState.guessing == 0) {
								cnstrnt.isColonValue = true;
								cnstrnt.type = typ;
								cnstrnt.value = val;
							}
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
					}
				}
			}
			if (inputState.guessing == 0) {
				cnstrnt.isExceptionSpec = true;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_44);
			}
			else {
				throw ex;
			}
		}
	}

	public final ElementSetSpec element_set_spec() throws RecognitionException, TokenStreamException {
		ElementSetSpec elemspec;

		elemspec = new ElementSetSpec();
		Intersection intersect;
		ConstraintElements cnselem;

		try { // for error handling
			switch (LA(1)) {
			case ANY_KW:
			case ANY_NODECODE_KW:
			case BIT_KW:
			case BMP_STR_KW:
			case BOOLEAN_KW:
			case CHARACTER_KW:
			case CHOICE_KW:
			case EMBEDDED_KW:
			case ENUMERATED_KW:
			case ERROR_KW:
			case EXTERNAL_KW:
			case FALSE_KW:
			case FROM_KW:
			case GENERALIZED_TIME_KW:
			case GENERAL_STR_KW:
			case GRAPHIC_STR_KW:
			case IA5_STRING_KW:
			case INTEGER_KW:
			case ISO646_STR_KW:
			case MINUS_INFINITY_KW:
			case MIN_KW:
			case NULL_KW:
			case NUMERIC_STR_KW:
			case OBJECT_KW:
			case OCTET_KW:
			case OPERATION_KW:
			case PLUS_INFINITY_KW:
			case PRINTABLE_STR_KW:
			case REAL_KW:
			case RELATIVE_KW:
			case SEQUENCE_KW:
			case SET_KW:
			case SIZE_KW:
			case TELETEX_STR_KW:
			case TRUE_KW:
			case T61_STR_KW:
			case UNIVERSAL_STR_KW:
			case UTC_TIME_KW:
			case UTF8_STR_KW:
			case VIDEOTEX_STR_KW:
			case VISIBLE_STR_KW:
			case WITH_KW:
			case L_BRACE:
			case L_BRACKET:
			case L_PAREN:
			case MINUS:
			case NUMBER:
			case UPPER:
			case LOWER:
			case B_STRING:
			case H_STRING:
			case C_STRING:
			case 147:
			case INCLUDES:
			case PATTERN_KW: {
				intersect = intersections();
				if (inputState.guessing == 0) {
					elemspec.intersectionList.add(intersect);
				}
				{
					_loop372: do {
						if ((LA(1) == UNION_KW || LA(1) == BAR) && (_tokenSet_68.member(LA(2)))
								&& (_tokenSet_69.member(LA(3)))) {
							{
								switch (LA(1)) {
								case BAR: {
									match(BAR);
									break;
								}
								case UNION_KW: {
									match(UNION_KW);
									break;
								}
								default: {
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
							}
							intersect = intersections();
							if (inputState.guessing == 0) {
								elemspec.intersectionList.add(intersect);
							}
						}
						else {
							break _loop372;
						}

					} while (true);
				}
				break;
			}
			case ALL_KW: {
				match(ALL_KW);
				match(EXCEPT_KW);
				cnselem = constraint_elements();
				if (inputState.guessing == 0) {
					elemspec.allExceptCnselem = cnselem;
					elemspec.isAllExcept = true;
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_44);
			}
			else {
				throw ex;
			}
		}
		return elemspec;
	}

	public final Intersection intersections() throws RecognitionException, TokenStreamException {
		Intersection intersect;

		intersect = new Intersection();
		ConstraintElements cnselem;

		try { // for error handling
			cnselem = constraint_elements();
			if (inputState.guessing == 0) {
				intersect.cnsElemList.add(cnselem);
			}
			{
				if ((LA(1) == EXCEPT) && (_tokenSet_68.member(LA(2))) && (_tokenSet_69.member(LA(3)))) {
					match(EXCEPT);
					if (inputState.guessing == 0) {
						intersect.isExcept = true;
					}
					cnselem = constraint_elements();
					if (inputState.guessing == 0) {
						intersect.exceptCnsElem.add(cnselem);
					}
				}
				else if ((_tokenSet_44.member(LA(1))) && (_tokenSet_34.member(LA(2))) && (_tokenSet_35.member(LA(3)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
			{
				_loop378: do {
					if ((LA(1) == INTERSECTION_KW || LA(1) == INTERSECTION) && (_tokenSet_68.member(LA(2)))
							&& (_tokenSet_69.member(LA(3)))) {
						{
							switch (LA(1)) {
							case INTERSECTION: {
								match(INTERSECTION);
								break;
							}
							case INTERSECTION_KW: {
								match(INTERSECTION_KW);
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						if (inputState.guessing == 0) {
							intersect.isInterSection = true;
						}
						cnselem = constraint_elements();
						if (inputState.guessing == 0) {
							intersect.cnsElemList.add(cnselem);
						}
						{
							if ((LA(1) == EXCEPT) && (_tokenSet_68.member(LA(2))) && (_tokenSet_69.member(LA(3)))) {
								match(EXCEPT);
								cnselem = constraint_elements();
								if (inputState.guessing == 0) {
									intersect.exceptCnsElem.add(cnselem);
								}
							}
							else if ((_tokenSet_44.member(LA(1))) && (_tokenSet_34.member(LA(2)))
									&& (_tokenSet_35.member(LA(3)))) {
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}

						}
					}
					else {
						break _loop378;
					}

				} while (true);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_44);
			}
			else {
				throw ex;
			}
		}
		return intersect;
	}

	public final ConstraintElements constraint_elements() throws RecognitionException, TokenStreamException {
		ConstraintElements cnsElem;

		cnsElem = new ConstraintElements();
		AsnValue val;
		AsnConstraint cns;
		ElementSetSpec elespec;
		Object typ;

		try { // for error handling
			switch (LA(1)) {
			case SIZE_KW: {
				{
					match(SIZE_KW);
					cns = constraint();
					if (inputState.guessing == 0) {
						cnsElem.isSizeConstraint = true;
						cnsElem.constraint = cns;
					}
				}
				break;
			}
			case FROM_KW: {
				{
					match(FROM_KW);
					cns = constraint();
					if (inputState.guessing == 0) {
						cnsElem.isAlphabetConstraint = true;
						cnsElem.constraint = cns;
					}
				}
				break;
			}
			case L_PAREN: {
				{
					match(L_PAREN);
					elespec = element_set_spec();
					if (inputState.guessing == 0) {
						cnsElem.isElementSetSpec = true;
						cnsElem.elespec = elespec;
					}
					match(R_PAREN);
				}
				break;
			}
			case PATTERN_KW: {
				{
					match(PATTERN_KW);
					val = value();
					if (inputState.guessing == 0) {
						cnsElem.isPatternValue = true;
						cnsElem.value = val;
					}
				}
				break;
			}
			case WITH_KW: {
				{
					match(WITH_KW);
					{
						switch (LA(1)) {
						case COMPONENT_KW: {
							{
								match(COMPONENT_KW);
								cns = constraint();
								if (inputState.guessing == 0) {
									cnsElem.isWithComponent = true;
									cnsElem.constraint = cns;
								}
							}
							break;
						}
						case COMPONENTS_KW: {
							{
								match(COMPONENTS_KW);
								if (inputState.guessing == 0) {
									cnsElem.isWithComponents = true;
								}
								match(L_BRACE);
								{
									switch (LA(1)) {
									case ELLIPSIS: {
										match(ELLIPSIS);
										match(COMMA);
										break;
									}
									case LOWER: {
										break;
									}
									default: {
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
								}
								type_constraint_list(cnsElem);
								match(R_BRACE);
							}
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
				}
				break;
			}
			default:
				boolean synPredMatched381 = false;
				if (((_tokenSet_56.member(LA(1))) && (_tokenSet_65.member(LA(2))) && (_tokenSet_34.member(LA(3))))) {
					int _m381 = mark();
					synPredMatched381 = true;
					inputState.guessing++;
					try {
						{
							value();
						}
					} catch (RecognitionException pe) {
						synPredMatched381 = false;
					}
					rewind(_m381);
					inputState.guessing--;
				}
				if (synPredMatched381) {
					{
						val = value();
						if (inputState.guessing == 0) {
							cnsElem.isValue = true;
							cnsElem.value = val;
						}
					}
				}
				else {
					boolean synPredMatched384 = false;
					if (((_tokenSet_70.member(LA(1))) && (_tokenSet_71.member(LA(2)))
							&& (_tokenSet_72.member(LA(3))))) {
						int _m384 = mark();
						synPredMatched384 = true;
						inputState.guessing++;
						try {
							{
								value_range(cnsElem);
							}
						} catch (RecognitionException pe) {
							synPredMatched384 = false;
						}
						rewind(_m384);
						inputState.guessing--;
					}
					if (synPredMatched384) {
						{
							value_range(cnsElem);
							if (inputState.guessing == 0) {
								cnsElem.isValueRange = true;
							}
						}
					}
					else if ((_tokenSet_73.member(LA(1))) && (_tokenSet_46.member(LA(2)))
							&& (_tokenSet_51.member(LA(3)))) {
						{
							{
								switch (LA(1)) {
								case INCLUDES: {
									match(INCLUDES);
									if (inputState.guessing == 0) {
										cnsElem.isIncludeType = true;
									}
									break;
								}
								case ANY_KW:
								case ANY_NODECODE_KW:
								case BIT_KW:
								case BMP_STR_KW:
								case BOOLEAN_KW:
								case CHARACTER_KW:
								case CHOICE_KW:
								case EMBEDDED_KW:
								case ENUMERATED_KW:
								case ERROR_KW:
								case EXTERNAL_KW:
								case GENERALIZED_TIME_KW:
								case GENERAL_STR_KW:
								case GRAPHIC_STR_KW:
								case IA5_STRING_KW:
								case INTEGER_KW:
								case ISO646_STR_KW:
								case NULL_KW:
								case NUMERIC_STR_KW:
								case OBJECT_KW:
								case OCTET_KW:
								case OPERATION_KW:
								case PRINTABLE_STR_KW:
								case REAL_KW:
								case RELATIVE_KW:
								case SEQUENCE_KW:
								case SET_KW:
								case TELETEX_STR_KW:
								case T61_STR_KW:
								case UNIVERSAL_STR_KW:
								case UTC_TIME_KW:
								case UTF8_STR_KW:
								case VIDEOTEX_STR_KW:
								case VISIBLE_STR_KW:
								case L_BRACKET:
								case UPPER:
								case LOWER:
								case 147: {
									break;
								}
								default: {
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
							}
							typ = type();
							if (inputState.guessing == 0) {
								cnsElem.isTypeConstraint = true;
								cnsElem.type = typ;
							}
						}
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_44);
			}
			else {
				throw ex;
			}
		}
		return cnsElem;
	}

	public final void value_range(ConstraintElements cnsElem) throws RecognitionException, TokenStreamException {

		AsnValue val;

		try { // for error handling
			{
				switch (LA(1)) {
				case FALSE_KW:
				case MINUS_INFINITY_KW:
				case NULL_KW:
				case PLUS_INFINITY_KW:
				case TRUE_KW:
				case L_BRACE:
				case MINUS:
				case NUMBER:
				case UPPER:
				case LOWER:
				case B_STRING:
				case H_STRING:
				case C_STRING: {
					val = value();
					if (inputState.guessing == 0) {
						cnsElem.lEndValue = val;
					}
					break;
				}
				case MIN_KW: {
					match(MIN_KW);
					if (inputState.guessing == 0) {
						cnsElem.isMinKw = true;
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case LESS: {
					match(LESS);
					if (inputState.guessing == 0) {
						cnsElem.isLEndLess = true;
					}
					break;
				}
				case DOTDOT: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(DOTDOT);
			{
				switch (LA(1)) {
				case LESS: {
					match(LESS);
					if (inputState.guessing == 0) {
						cnsElem.isUEndLess = true;
					}
					break;
				}
				case FALSE_KW:
				case MAX_KW:
				case MINUS_INFINITY_KW:
				case NULL_KW:
				case PLUS_INFINITY_KW:
				case TRUE_KW:
				case L_BRACE:
				case MINUS:
				case NUMBER:
				case UPPER:
				case LOWER:
				case B_STRING:
				case H_STRING:
				case C_STRING: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case FALSE_KW:
				case MINUS_INFINITY_KW:
				case NULL_KW:
				case PLUS_INFINITY_KW:
				case TRUE_KW:
				case L_BRACE:
				case MINUS:
				case NUMBER:
				case UPPER:
				case LOWER:
				case B_STRING:
				case H_STRING:
				case C_STRING: {
					val = value();
					if (inputState.guessing == 0) {
						cnsElem.uEndValue = val;
					}
					break;
				}
				case MAX_KW: {
					match(MAX_KW);
					if (inputState.guessing == 0) {
						cnsElem.isMaxKw = true;
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_44);
			}
			else {
				throw ex;
			}
		}
	}

	public final void type_constraint_list(ConstraintElements cnsElem)
			throws RecognitionException, TokenStreamException {

		NamedConstraint namecns;

		try { // for error handling
			namecns = named_constraint();
			if (inputState.guessing == 0) {
				cnsElem.typeConstraintList.add(namecns);
			}
			{
				_loop404: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						namecns = named_constraint();
						if (inputState.guessing == 0) {
							cnsElem.typeConstraintList.add(namecns);
						}
					}
					else {
						break _loop404;
					}

				} while (true);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_47);
			}
			else {
				throw ex;
			}
		}
	}

	public final NamedConstraint named_constraint() throws RecognitionException, TokenStreamException {
		NamedConstraint namecns;

		Token lid = null;
		namecns = new NamedConstraint();
		AsnConstraint cns;

		try { // for error handling
			lid = LT(1);
			match(LOWER);
			if (inputState.guessing == 0) {
				namecns.name = lid.getText();
			}
			{
				if ((_tokenSet_74.member(LA(1))) && (_tokenSet_42.member(LA(2))) && (_tokenSet_43.member(LA(3)))) {
					cns = constraint();
					if (inputState.guessing == 0) {
						namecns.isConstraint = true;
						namecns.constraint = cns;
					}
				}
				else if ((_tokenSet_75.member(LA(1))) && (_tokenSet_44.member(LA(2))) && (_tokenSet_34.member(LA(3)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
			{
				switch (LA(1)) {
				case PRESENT_KW: {
					match(PRESENT_KW);
					if (inputState.guessing == 0) {
						namecns.isPresentKw = true;
					}
					break;
				}
				case ABSENT_KW: {
					match(ABSENT_KW);
					if (inputState.guessing == 0) {
						namecns.isAbsentKw = true;
					}
					break;
				}
				case OPTIONAL_KW: {
					match(OPTIONAL_KW);
					if (inputState.guessing == 0) {
						namecns.isOptionalKw = true;
					}
					break;
				}
				case COMMA:
				case R_BRACE: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_64);
			}
			else {
				throw ex;
			}
		}
		return namecns;
	}

	public final void choice_value(AsnValue value) throws RecognitionException, TokenStreamException {

		Token lid = null;
		AsnChoiceValue chval = new AsnChoiceValue();
		AsnValue val;

		try { // for error handling
			{
				{
					lid = LT(1);
					match(LOWER);
					if (inputState.guessing == 0) {
						chval.name = lid.getText();
					}
				}
				{
					switch (LA(1)) {
					case COLON: {
						match(COLON);
						break;
					}
					case FALSE_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case PLUS_INFINITY_KW:
					case TRUE_KW:
					case L_BRACE:
					case MINUS:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					val = value();
					if (inputState.guessing == 0) {
						chval.value = val;
					}
				}
			}
			if (inputState.guessing == 0) {
				value.chval = chval;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_27);
			}
			else {
				throw ex;
			}
		}
	}

	public final AsnSequenceValue sequence_value() throws RecognitionException, TokenStreamException {
		AsnSequenceValue seqval;

		AsnNamedValue nameval = new AsnNamedValue();
		seqval = new AsnSequenceValue();

		try { // for error handling
			match(L_BRACE);
			{
				{
					switch (LA(1)) {
					case LOWER: {
						nameval = named_value();
						if (inputState.guessing == 0) {
							seqval.isValPresent = true;
							seqval.namedValueList.add(nameval);
						}
						break;
					}
					case COMMA:
					case R_BRACE: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					_loop500: do {
						if ((LA(1) == COMMA)) {
							match(COMMA);
							nameval = named_value();
							if (inputState.guessing == 0) {
								seqval.namedValueList.add(nameval);
							}
						}
						else {
							break _loop500;
						}

					} while (true);
				}
			}
			match(R_BRACE);
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_27);
			}
			else {
				throw ex;
			}
		}
		return seqval;
	}

	public final void sequenceof_value(AsnValue value) throws RecognitionException, TokenStreamException {

		AsnValue val;
		value.seqOfVal = new AsnSequenceOfValue();

		try { // for error handling
			match(L_BRACE);
			{
				{
					switch (LA(1)) {
					case FALSE_KW:
					case MINUS_INFINITY_KW:
					case NULL_KW:
					case PLUS_INFINITY_KW:
					case TRUE_KW:
					case L_BRACE:
					case MINUS:
					case NUMBER:
					case UPPER:
					case LOWER:
					case B_STRING:
					case H_STRING:
					case C_STRING: {
						val = value();
						if (inputState.guessing == 0) {
							value.seqOfVal.value.add(val);
						}
						break;
					}
					case COMMA:
					case R_BRACE: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					_loop505: do {
						if ((LA(1) == COMMA)) {
							match(COMMA);
							val = value();
							if (inputState.guessing == 0) {
								value.seqOfVal.value.add(val);
							}
						}
						else {
							break _loop505;
						}

					} while (true);
				}
			}
			match(R_BRACE);
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_27);
			}
			else {
				throw ex;
			}
		}
	}

	public final void cstr_value(AsnValue value) throws RecognitionException, TokenStreamException {

		Token h = null;
		Token b = null;
		AsnBitOrOctetStringValue bstrval = new AsnBitOrOctetStringValue();
		AsnCharacterStringValue cstrval = new AsnCharacterStringValue();
		AsnSequenceValue seqval;

		try { // for error handling
			{
				switch (LA(1)) {
				case H_STRING: {
					{
						h = LT(1);
						match(H_STRING);
						if (inputState.guessing == 0) {
							bstrval.isHString = true;
							bstrval.bhStr = h.getText();
						}
					}
					break;
				}
				case B_STRING: {
					{
						b = LT(1);
						match(B_STRING);
						if (inputState.guessing == 0) {
							bstrval.isBString = true;
							bstrval.bhStr = b.getText();
						}
					}
					break;
				}
				case L_BRACE: {
					{
						match(L_BRACE);
						{
							boolean synPredMatched459 = false;
							if (((LA(1) == LOWER) && (LA(2) == COMMA || LA(2) == R_BRACE)
									&& (_tokenSet_27.member(LA(3))))) {
								int _m459 = mark();
								synPredMatched459 = true;
								inputState.guessing++;
								try {
									{
										id_list(bstrval);
									}
								} catch (RecognitionException pe) {
									synPredMatched459 = false;
								}
								rewind(_m459);
								inputState.guessing--;
							}
							if (synPredMatched459) {
								{
									id_list(bstrval);
								}
							}
							else {
								boolean synPredMatched462 = false;
								if (((_tokenSet_76.member(LA(1))) && (_tokenSet_77.member(LA(2)))
										&& (_tokenSet_27.member(LA(3))))) {
									int _m462 = mark();
									synPredMatched462 = true;
									inputState.guessing++;
									try {
										{
											char_defs_list(cstrval);
										}
									} catch (RecognitionException pe) {
										synPredMatched462 = false;
									}
									rewind(_m462);
									inputState.guessing--;
								}
								if (synPredMatched462) {
									{
										char_defs_list(cstrval);
									}
								}
								else if ((LA(1) == MINUS || LA(1) == NUMBER)) {
									tuple_or_quad(cstrval);
								}
								else {
									throw new NoViableAltException(LT(1), getFilename());
								}
							}
						}
						match(R_BRACE);
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			if (inputState.guessing == 0) {
				value.cStrValue = cstrval;
				value.bStrValue = bstrval;
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_27);
			}
			else {
				throw ex;
			}
		}
	}

	public final void id_list(AsnBitOrOctetStringValue bstrval) throws RecognitionException, TokenStreamException {

		Token ld = null;
		Token ld1 = null;
		String s = "";

		try { // for error handling
			{
				ld = LT(1);
				match(LOWER);
				if (inputState.guessing == 0) {
					s = ld.getText();
					bstrval.idlist.add(s);
				}
			}
			{
				_loop467: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						ld1 = LT(1);
						match(LOWER);
						if (inputState.guessing == 0) {
							s = ld1.getText();
							bstrval.idlist.add(s);
						}
					}
					else {
						break _loop467;
					}

				} while (true);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_47);
			}
			else {
				throw ex;
			}
		}
	}

	public final void char_defs_list(AsnCharacterStringValue cstrval)
			throws RecognitionException, TokenStreamException {

		CharDef a;

		try { // for error handling
			a = char_defs();
			if (inputState.guessing == 0) {
				cstrval.isCharDefList = true;
				cstrval.charDefsList.add(a);
			}
			{
				_loop471: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						{
							a = char_defs();
							if (inputState.guessing == 0) {
								cstrval.charDefsList.add(a);
							}
						}
					}
					else {
						break _loop471;
					}

				} while (true);
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_47);
			}
			else {
				throw ex;
			}
		}
	}

	public final void tuple_or_quad(AsnCharacterStringValue cstrval) throws RecognitionException, TokenStreamException {

		AsnSignedNumber n;

		try { // for error handling
			{
				n = signed_number();
				if (inputState.guessing == 0) {
					cstrval.tupleQuad.add(n);
				}
			}
			match(COMMA);
			{
				n = signed_number();
				if (inputState.guessing == 0) {
					cstrval.tupleQuad.add(n);
				}
			}
			{
				switch (LA(1)) {
				case R_BRACE: {
					{
						match(R_BRACE);
						if (inputState.guessing == 0) {
							cstrval.isTuple = true;
						}
					}
					break;
				}
				case COMMA: {
					{
						match(COMMA);
						{
							n = signed_number();
							if (inputState.guessing == 0) {
								cstrval.tupleQuad.add(n);
							}
						}
						match(COMMA);
						{
							n = signed_number();
							if (inputState.guessing == 0) {
								cstrval.tupleQuad.add(n);
							}
						}
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_47);
			}
			else {
				throw ex;
			}
		}
	}

	public final CharDef char_defs() throws RecognitionException, TokenStreamException {
		CharDef chardef;

		Token c = null;
		chardef = new CharDef();
		AsnSignedNumber n;
		AsnDefinedValue defval;

		try { // for error handling
			switch (LA(1)) {
			case C_STRING: {
				{
					c = LT(1);
					match(C_STRING);
					if (inputState.guessing == 0) {
						chardef.isCString = true;
						chardef.cStr = c.getText();
					}
				}
				break;
			}
			case L_BRACE: {
				{
					match(L_BRACE);
					{
						n = signed_number();
						if (inputState.guessing == 0) {
							chardef.tupleQuad.add(n);
						}
					}
					match(COMMA);
					{
						n = signed_number();
						if (inputState.guessing == 0) {
							chardef.tupleQuad.add(n);
						}
					}
					{
						switch (LA(1)) {
						case R_BRACE: {
							{
								match(R_BRACE);
								if (inputState.guessing == 0) {
									chardef.isTuple = true;
								}
							}
							break;
						}
						case COMMA: {
							{
								match(COMMA);
								{
									n = signed_number();
									if (inputState.guessing == 0) {
										chardef.tupleQuad.add(n);
									}
								}
								match(COMMA);
								{
									n = signed_number();
									if (inputState.guessing == 0) {
										chardef.tupleQuad.add(n);
									}
								}
								match(R_BRACE);
								if (inputState.guessing == 0) {
									chardef.isQuadruple = true;
								}
							}
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
				}
				break;
			}
			case UPPER:
			case LOWER: {
				{
					defval = defined_value();
					if (inputState.guessing == 0) {
						chardef.defval = defval;
					}
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_64);
			}
			else {
				throw ex;
			}
		}
		return chardef;
	}

	public final AsnNamedValue named_value() throws RecognitionException, TokenStreamException {
		AsnNamedValue nameval;

		Token lid = null;
		nameval = new AsnNamedValue();
		AsnValue val;

		try { // for error handling
			{
				lid = LT(1);
				match(LOWER);
				if (inputState.guessing == 0) {
					nameval.name = lid.getText();
				}
				val = value();
				if (inputState.guessing == 0) {
					nameval.value = val;
				}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_64);
			}
			else {
				throw ex;
			}
		}
		return nameval;
	}

	public static final String[] _tokenNames = { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "\"ABSENT\"",
			"\"ABSTRACT-SYNTAX\"", "\"ALL\"", "\"ANY\"", "\"ANY_NODECODE\"", "\"ARGUMENT\"", "\"APPLICATION\"",
			"\"AUTOMATIC\"", "\"BASEDNUM\"", "\"BEGIN\"", "\"BIT\"", "\"BMPString\"", "\"BOOLEAN\"", "\"BY\"",
			"\"CHARACTER\"", "\"CHOICE\"", "\"CLASS\"", "\"COMPONENTS\"", "\"COMPONENT\"", "\"CONSTRAINED\"",
			"\"DEFAULT\"", "\"DEFINED\"", "\"DEFINITIONS\"", "\"EMBEDDED\"", "\"END\"", "\"ENUMERATED\"", "\"ERROR\"",
			"\"ERRORS\"", "\"EXCEPT\"", "\"EXPLICIT\"", "\"EXPORTS\"", "\"EXTENSIBILITY\"", "\"EXTERNAL\"", "\"FALSE\"",
			"\"FROM\"", "\"GeneralizedTime\"", "\"GeneralString\"", "\"GraphicString\"", "\"IA5String\"",
			"\"IDENTIFIER\"", "\"IMPLICIT\"", "\"IMPLIED\"", "\"IMPORTS\"", "\"INCLUDES\"", "\"INSTANCE\"",
			"\"INTEGER\"", "\"INTERSECTION\"", "\"ISO646String\"", "\"LINKED\"", "\"MAX\"", "\"MINUSINFINITY\"",
			"\"MIN\"", "\"NULL\"", "\"NumericString\"", "\"ObjectDescriptor\"", "\"OBJECT\"", "\"OCTET\"",
			"\"OPERATION\"", "\"OF\"", "\"OID\"", "\"OPTIONAL\"", "\"PARAMETER\"", "\"PDV\"", "\"PLUSINFINITY\"",
			"\"PRESENT\"", "\"PrintableString\"", "\"PRIVATE\"", "\"REAL\"", "\"RELATIVE\"", "\"RESULT\"",
			"\"SEQUENCE\"", "\"SET\"", "\"SIZE\"", "\"STRING\"", "\"TAGS\"", "\"TeletexString\"", "\"TRUE\"",
			"\"TYPE-IDENTIFIER\"", "\"T61String\"", "\"UNION\"", "\"UNIQUE\"", "\"UNIVERSAL\"", "\"UniversalString\"",
			"\"UTCTime\"", "\"UTF8String\"", "\"VideotexString\"", "\"VisibleString\"", "\"WITH\"", "ASSIGN_OP", "BAR",
			"COLON", "COMMA", "COMMENT", "DOT", "DOTDOT", "ELLIPSIS", "EXCLAMATION", "INTERSECTION", "LESS", "L_BRACE",
			"L_BRACKET", "L_PAREN", "MINUS", "PLUS", "R_BRACE", "R_BRACKET", "R_PAREN", "SEMI", "SINGLE_QUOTE", "CHARB",
			"CHARH", "WS", "SL_COMMENT", "NUMBER", "UPPER", "LOWER", "BDIG", "HDIG", "B_OR_H_STRING", "B_STRING",
			"H_STRING", "C_STRING", "\"BIND\"", "\"UNBIND\"", "\"APPLICATION-SERVICE-ELEMENT\"",
			"\"APPLICATION-CONTEXT\"", "\"EXTENSION\"", "\"EXTENSIONS\"", "\"EXTENSION-ATTRIBUTE\"", "\"TOKEN\"",
			"\"TOKEN-DATA\"", "\"SECURITY-CATEGORY\"", "\"PORT\"", "\"REFINE\"", "\"ABSTRACT-BIND\"",
			"\"ABSTRACT-UNBIND\"", "\"ABSTRACT-OPERATION\"", "\"ABSTRACT-ERROR\"", "\"ALGORITHM\"", "\"ENCRYPTED\"",
			"\"SIGNED\"", "\"SIGNATURE\"", "\"PROTECTED\"", "\"OBJECT-TYPE\"", "\"MACRO\"", "\"SYNTAX\"", "\"ACCESS\"",
			"\"STATUS\"", "\"DESCRIPTION\"", "\"REFERENCE\"", "\"INDEX\"", "\"DEFVAL\"", "EXCEPT", "INCLUDES",
			"PATTERN_KW" };

	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

	private static final long[] mk_tokenSet_1() {
		long[] data = { 2L, 18014398509481984L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

	private static final long[] mk_tokenSet_2() {
		long[] data = { 67108864L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

	private static final long[] mk_tokenSet_3() {
		long[] data = { 268435456L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

	private static final long[] mk_tokenSet_4() {
		long[] data = { 0L, 63050403373121536L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());

	private static final long[] mk_tokenSet_5() {
		long[] data = { 0L, 63070194582421504L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());

	private static final long[] mk_tokenSet_6() {
		long[] data = { 8889551171217113488L, -513172901800865863L, 273678335L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());

	private static final long[] mk_tokenSet_7() {
		long[] data = { 8889551171217113488L, -513175109414056007L, 273678335L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());

	private static final long[] mk_tokenSet_8() {
		long[] data = { 8889551171150004624L, -513175109414056007L, 273678335L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());

	private static final long[] mk_tokenSet_9() {
		long[] data = { 0L, 63070185992486912L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());

	private static final long[] mk_tokenSet_10() {
		long[] data = { 0L, 63067995559165952L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());

	private static final long[] mk_tokenSet_11() {
		long[] data = { 0L, 63067986969231360L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());

	private static final long[] mk_tokenSet_12() {
		long[] data = { 4254221114807271808L, 54044295170395552L, 524288L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());

	private static final long[] mk_tokenSet_13() {
		long[] data = { 70369012613120L, 54043195528445952L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());

	private static final long[] mk_tokenSet_14() {
		long[] data = { 2882303762590859264L, -4557642822898941952L, 1048575L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());

	private static final long[] mk_tokenSet_15() {
		long[] data = { 268435456L, 54043195528445952L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());

	private static final long[] mk_tokenSet_16() {
		long[] data = { -268435472L, -1L, 2147483647L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());

	private static final long[] mk_tokenSet_17() {
		long[] data = { 274877906944L, 140737488355328L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());

	private static final long[] mk_tokenSet_18() {
		long[] data = { 2882303762590859264L, -4557502076820652032L, 1048575L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());

	private static final long[] mk_tokenSet_19() {
		long[] data = { 275146342400L, 54043197675929600L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());

	private static final long[] mk_tokenSet_20() {
		long[] data = { 2882303762590859264L, -4557502085410586624L, 1048575L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());

	private static final long[] mk_tokenSet_21() {
		long[] data = { 4254221114807271810L, -4557641722988556896L, 2097151L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());

	private static final long[] mk_tokenSet_22() {
		long[] data = { 274877906944L, 140739635838976L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());

	private static final long[] mk_tokenSet_23() {
		long[] data = { 4277865152722616704L, 4098369879467200425L, 273154048L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());

	private static final long[] mk_tokenSet_24() {
		long[] data = { 8889551171150004624L, 4098370180114911161L, 273154048L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());

	private static final long[] mk_tokenSet_25() {
		long[] data = { 8934596238430291922L, 4098513151122587647L, 2021130240L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());

	private static final long[] mk_tokenSet_26() {
		long[] data = { 90072129986363392L, 4098280609783283720L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());

	private static final long[] mk_tokenSet_27() {
		long[] data = { 8889551171150004624L, 4098370171524976569L, 273154048L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());

	private static final long[] mk_tokenSet_28() {
		long[] data = { 90072129986363392L, 4098298203043069960L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());

	private static final long[] mk_tokenSet_29() {
		long[] data = { 8919950741079966144L, 4098283976228445608L, 1611137024L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());

	private static final long[] mk_tokenSet_30() {
		long[] data = { 8925589043189499840L, 4098372379006058478L, 1881669632L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());

	private static final long[] mk_tokenSet_31() {
		long[] data = { 4313894224619487680L, 4098372078624677801L, 1883766784L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());

	private static final long[] mk_tokenSet_32() {
		long[] data = { 8925589043474712530L, 4098513151122587647L, 2021130240L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());

	private static final long[] mk_tokenSet_33() {
		long[] data = { -288758193282101294L, 4098548335494676479L, 2029518848L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());

	private static final long[] mk_tokenSet_34() {
		long[] data = { 8925589039175550930L, 4098513133942718463L, 2021130240L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());

	private static final long[] mk_tokenSet_35() {
		long[] data = { -297765392536842286L, 4098548335494676479L, 2029518848L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());

	private static final long[] mk_tokenSet_36() {
		long[] data = { 134217728L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());

	private static final long[] mk_tokenSet_37() {
		long[] data = { -288775794125191232L, 4098548335494676463L, 1885863936L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());

	private static final long[] mk_tokenSet_38() {
		long[] data = { 8925589039173453760L, 4098372362094624751L, 1885863936L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());

	private static final long[] mk_tokenSet_39() {
		long[] data = { 8889551171150004624L, 4098372078490455993L, 273154048L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());

	private static final long[] mk_tokenSet_40() {
		long[] data = { 8925580243046875600L, 4098372078624677817L, 1883766784L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());

	private static final long[] mk_tokenSet_41() {
		long[] data = { 4308264722652578240L, 4098283907508968872L, 1611137024L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());

	private static final long[] mk_tokenSet_42() {
		long[] data = { 8925589043474712528L, 4098372379274493951L, 1885863936L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());

	private static final long[] mk_tokenSet_43() {
		long[] data = { -288775794125191214L, 4098548335494676479L, 2021130240L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());

	private static final long[] mk_tokenSet_44() {
		long[] data = { 8889551171150004624L, 4098369879467200441L, 273154048L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());

	private static final long[] mk_tokenSet_45() {
		long[] data = { 4254221114807271808L, 63055892471647648L, 524288L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());

	private static final long[] mk_tokenSet_46() {
		long[] data = { 8925589039173453776L, 4098372362094624767L, 1885863936L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());

	private static final long[] mk_tokenSet_47() {
		long[] data = { 0L, 17592186044416L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());

	private static final long[] mk_tokenSet_48() {
		long[] data = { 4254238715583252864L, 54044295170395552L, 524288L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());

	private static final long[] mk_tokenSet_49() {
		long[] data = { 0L, 45035996273704960L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_49 = new BitSet(mk_tokenSet_49());

	private static final long[] mk_tokenSet_50() {
		long[] data = { 0L, 35184372088832L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_50 = new BitSet(mk_tokenSet_50());

	private static final long[] mk_tokenSet_51() {
		long[] data = { -297782993379932206L, 4098548335494676479L, 2021130240L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_51 = new BitSet(mk_tokenSet_51());

	private static final long[] mk_tokenSet_52() {
		long[] data = { 4277865152722616704L, 4098510616955555753L, 273154048L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_52 = new BitSet(mk_tokenSet_52());

	private static final long[] mk_tokenSet_53() {
		long[] data = { 4272235650755707264L, 4098299300537535912L, 524288L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_53 = new BitSet(mk_tokenSet_53());

	private static final long[] mk_tokenSet_54() {
		long[] data = { 8924463138981398464L, 4098301854031921134L, 1613234176L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_54 = new BitSet(mk_tokenSet_54());

	private static final long[] mk_tokenSet_55() {
		long[] data = { -297782993379932224L, 4098548301134938095L, 2020081664L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_55 = new BitSet(mk_tokenSet_55());

	private static final long[] mk_tokenSet_56() {
		long[] data = { 90072129986363392L, 4098280608709541896L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_56 = new BitSet(mk_tokenSet_56());

	private static final long[] mk_tokenSet_57() {
		long[] data = { 4272235650755707264L, 4098299312348695976L, 524288L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_57 = new BitSet(mk_tokenSet_57());

	private static final long[] mk_tokenSet_58() {
		long[] data = { 8925589039173453760L, 4098372362094624751L, 2020081664L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_58 = new BitSet(mk_tokenSet_58());

	private static final long[] mk_tokenSet_59() {
		long[] data = { 4272235650755707264L, 4098299302685019560L, 524288L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_59 = new BitSet(mk_tokenSet_59());

	private static final long[] mk_tokenSet_60() {
		long[] data = { 4272235650755707264L, 4098281708351491496L, 524288L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_60 = new BitSet(mk_tokenSet_60());

	private static final long[] mk_tokenSet_61() {
		long[] data = { 1024L, 45035996275802176L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_61 = new BitSet(mk_tokenSet_61());

	private static final long[] mk_tokenSet_62() {
		long[] data = { 8924463138998175680L, 4098301854031921135L, 1613234176L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_62 = new BitSet(mk_tokenSet_62());

	private static final long[] mk_tokenSet_63() {
		long[] data = { -297782993379932224L, 4098548301134938095L, 1885863936L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_63 = new BitSet(mk_tokenSet_63());

	private static final long[] mk_tokenSet_64() {
		long[] data = { 0L, 17594333528064L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_64 = new BitSet(mk_tokenSet_64());

	private static final long[] mk_tokenSet_65() {
		long[] data = { 8889551171150004624L, 4098369888057135033L, 273154048L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_65 = new BitSet(mk_tokenSet_65());

	private static final long[] mk_tokenSet_66() {
		long[] data = { 8924463138981398464L, 4098284260772134894L, 1613234176L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_66 = new BitSet(mk_tokenSet_66());

	private static final long[] mk_tokenSet_67() {
		long[] data = { -297782993665144896L, 4098548300866502638L, 1881669632L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_67 = new BitSet(mk_tokenSet_67());

	private static final long[] mk_tokenSet_68() {
		long[] data = { 4308264722652578176L, 4098283907508968872L, 1611137024L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_68 = new BitSet(mk_tokenSet_68());

	private static final long[] mk_tokenSet_69() {
		long[] data = { 8925589039179745232L, 4098372379274493951L, 1885863936L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_69 = new BitSet(mk_tokenSet_69());

	private static final long[] mk_tokenSet_70() {
		long[] data = { 126100927005327360L, 4098280608709541896L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_70 = new BitSet(mk_tokenSet_70());

	private static final long[] mk_tokenSet_71() {
		long[] data = { 90072129986363392L, 4098298504764522504L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_71 = new BitSet(mk_tokenSet_71());

	private static final long[] mk_tokenSet_72() {
		long[] data = { 99079329241104384L, 4098300703787778056L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_72 = new BitSet(mk_tokenSet_72());

	private static final long[] mk_tokenSet_73() {
		long[] data = { 4254221114807271808L, 54044295170395552L, 537395200L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_73 = new BitSet(mk_tokenSet_73());

	private static final long[] mk_tokenSet_74() {
		long[] data = { 4308264722652578256L, 4098301570561973689L, 1611137024L, 0L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_74 = new BitSet(mk_tokenSet_74());

	private static final long[] mk_tokenSet_75() {
		long[] data = { 16L, 17594333528081L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_75 = new BitSet(mk_tokenSet_75());

	private static final long[] mk_tokenSet_76() {
		long[] data = { 0L, 2359886754497953792L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_76 = new BitSet(mk_tokenSet_76());

	private static final long[] mk_tokenSet_77() {
		long[] data = { 0L, 9029200224714752L, 0L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_77 = new BitSet(mk_tokenSet_77());

}
