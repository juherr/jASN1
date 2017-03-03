header {
package org.openmuc.jasn1.compiler.parser;

import org.openmuc.jasn1.compiler.model.*;
import java.math.*;
import java.util.*;
}

//	Creation of ASN.1 grammar for ANTLR	V2.7.7
// ===================================================
//		  TOKENS FOR ASN.1 LEXER DEFINITIONS
// ===================================================

class ASNLexer extends Lexer;
options	{
	k =	11;
	exportVocab=ASN;
	charVocabulary = '\3'..'\377';
	caseSensitive=true;
	testLiterals = true;
	codeGenMakeSwitchThreshold = 2;  // Some optimizations
	codeGenBitsetTestThreshold = 3;
}

//	ASN1 Tokens 

tokens {
	
ABSENT_KW				=	"ABSENT"			;
ABSTRACT_SYNTAX_KW		=	"ABSTRACT-SYNTAX"	;
ALL_KW					=	"ALL"				;
ANY_KW					=	"ANY"				;
ANY_NODECODE_KW			=	"ANY_NODECODE"		;
ARGUMENT_KW				=	"ARGUMENT"			;
APPLICATION_KW			=	"APPLICATION"		;
AUTOMATIC_KW			=	"AUTOMATIC"			;
BASED_NUM_KW			=	"BASEDNUM"			;
BEGIN_KW				=	"BEGIN"				;
BIT_KW					=	"BIT"				;
BMP_STR_KW			=	"BMPString"			;
BOOLEAN_KW				=	"BOOLEAN"			;
BY_KW					=	"BY"				;
CHARACTER_KW			=	"CHARACTER"			;
CHOICE_KW				=	"CHOICE"			;
CLASS_KW				=	"CLASS"				;
COMPONENTS_KW			=	"COMPONENTS"		;
COMPONENT_KW			=	"COMPONENT"			;
CONSTRAINED_KW			=	"CONSTRAINED"		;
DEFAULT_KW				=	"DEFAULT"			;
DEFINED_KW				=	"DEFINED"			;
DEFINITIONS_KW			=	"DEFINITIONS"		;
EMBEDDED_KW				=	"EMBEDDED"			;
END_KW					=	"END"				;
ENUMERATED_KW			=	"ENUMERATED"		;
ERROR_KW				=	"ERROR"				;
ERRORS_KW				=	"ERRORS"			;
EXCEPT_KW				=	"EXCEPT"			;
EXPLICIT_KW				=	"EXPLICIT"			;
EXPORTS_KW				=	"EXPORTS"			;
EXTENSIBILITY_KW		=	"EXTENSIBILITY"		;
EXTERNAL_KW				=	"EXTERNAL"			;
FALSE_KW				=	"FALSE"				;
FROM_KW					=	"FROM"				;
GENERALIZED_TIME_KW		=	"GeneralizedTime"	;
GENERAL_STR_KW			=	"GeneralString"		;
GRAPHIC_STR_KW			=	"GraphicString"		;
IA5_STRING_KW			=	"IA5String"			;
IDENTIFIER_KW			=	"IDENTIFIER"		;
IMPLICIT_KW				=	"IMPLICIT"			;
IMPLIED_KW				=	"IMPLIED"			;
IMPORTS_KW				=	"IMPORTS"			;
INCLUDES_KW				=	"INCLUDES"			;
INSTANCE_KW				=	"INSTANCE"			;
INTEGER_KW				=	"INTEGER"			;
INTERSECTION_KW			=	"INTERSECTION"		;
ISO646_STR_KW			=	"ISO646String"		;
LINKED_KW				=	"LINKED"			;
MAX_KW					=	"MAX"				;
MINUS_INFINITY_KW		=	"MINUSINFINITY"		;
MIN_KW					=	"MIN"				;
NULL_KW					=	"NULL"				;
NUMERIC_STR_KW			=	"NumericString"		;
OBJECT_DESCRIPTOR_KW 	=	"ObjectDescriptor"	;
OBJECT_KW				=	"OBJECT"			;
OCTET_KW				=	"OCTET"				;
OPERATION_KW			=	"OPERATION"			;
OF_KW					=	"OF"				;
OID_KW					=	"OID"				;
OPTIONAL_KW				=	"OPTIONAL"			;
PARAMETER_KW			=	"PARAMETER"			;
PDV_KW					=	"PDV"				;
PLUS_INFINITY_KW		=	"PLUSINFINITY"		;
PRESENT_KW				=	"PRESENT"			;
PRINTABLE_STR_KW		=	"PrintableString"	;
PRIVATE_KW				=	"PRIVATE"			;
REAL_KW					=	"REAL"				;
RELATIVE_KW				=	"RELATIVE"			;
RESULT_KW				=	"RESULT"			;
SEQUENCE_KW				=	"SEQUENCE"			;
SET_KW					=	"SET"				;
SIZE_KW					=	"SIZE"				;
STRING_KW				=	"STRING"			;
TAGS_KW					=	"TAGS"				;
TELETEX_STR_KW			=	"TeletexString"		;
TRUE_KW					=	"TRUE"				;
TYPE_IDENTIFIER_KW		=	"TYPE-IDENTIFIER"	;
T61_STR_KW              =   "T61String"         ;
UNION_KW				=	"UNION"				;
UNIQUE_KW				=	"UNIQUE"			;
UNIVERSAL_KW			=	"UNIVERSAL"			;
UNIVERSAL_STR_KW		=	"UniversalString"	;
UTC_TIME_KW				=	"UTCTime"			;
UTF8_STR_KW             =	"UTF8String"		;
VIDEOTEX_STR_KW			=	"VideotexString"	;
VISIBLE_STR_KW			=	"VisibleString"		;
WITH_KW					=	"WITH"				;
}

// Operators

ASSIGN_OP			:	"::="	;
BAR					:	'|'		;
COLON				:	':'		;
COMMA				:	','		;
COMMENT				:	"--"	;
DOT					:	'.'		;
DOTDOT				:	".."	;
ELLIPSIS			:	"..."	;
EXCLAMATION			:	'!'		;
INTERSECTION		:	'^'		;
LESS				:	'<'		;
L_BRACE				:	'{'		;
L_BRACKET			:	'['		;
L_PAREN				:	'('		;
MINUS				:	'-'		;
PLUS				:	'+'		;
R_BRACE				:	'}'		;
R_BRACKET			:	']'		;
R_PAREN				:	')'		;
SEMI				:	';'		;
SINGLE_QUOTE		:	"'"		;
CHARB				:	"'B"	;
CHARH				:	"'H"	;

// Whitespace -- ignored

WS			
	:	(	' ' | '\t' | '\f'	|	(	options {generateAmbigWarnings=false;}
	:	"\r\n"		{ newline(); }// DOS
	|	'\r'   		{ newline(); }// Macintosh
	|	'\n'		{ newline(); }// Unix 
	))+
	{$setType(Token.SKIP); }
	;

// Single-line comments
SL_COMMENT
	: (options {warnWhenFollowAmbig=false;} 
	: COMMENT (  { LA(2)!='-' }? '-' 	|	~('-'|'\n'|'\r'))*	( (('\r')? '\n') { newline(); }| COMMENT) )
		{$setType(Token.SKIP);  }
	;

// multi-line comments
ML_COMMENT
	: "/*" (('*' ~'/') | (('\r')? '\n') { newline(); } | ~'*')* "*/"
		{$setType(Token.SKIP);  }
	;

NUMBER	:	('0'..'9')+ ;

UPPER	
options {testLiterals = false;}
	:   ('A'..'Z') 
		(options {warnWhenFollowAmbig = false;}
	:	( 'a'..'z' | 'A'..'Z' |'-' | '0'..'9'|'_' ))* 	;

LOWER
options {testLiterals = false;}
	:	('a'..'z') 
		(options {warnWhenFollowAmbig = false;}
	:	( 'a'..'z' | 'A'..'Z' |'-' | '0'..'9'|'_' ))* 	;


protected
BDIG		: ('0'|'1') ;
protected
HDIG		:	(options {warnWhenFollowAmbig = false;} :('0'..'9') )
			|	('A'..'F')
			|	('a'..'f')
			;

// Unable to resolve a string like 010101 followed by 'H
//B_STRING 	: 	SINGLE_QUOTE ({LA(3)!='B'}? BDIG)+  BDIG SINGLE_QUOTE 'B' 	;
//H_STRING 	: 	SINGLE_QUOTE ({LA(3)!='H'}? HDIG)+  HDIG SINGLE_QUOTE 'H'  ;

B_OR_H_STRING
	:	(options {warnWhenFollowAmbig = false;} 
		:(B_STRING)=>B_STRING {$setType(B_STRING);}
		| H_STRING {$setType(H_STRING);})
	;

protected
B_STRING 	: 	SINGLE_QUOTE (BDIG)+ SINGLE_QUOTE 'B' 	;
protected
H_STRING 	: 	SINGLE_QUOTE (HDIG)+ SINGLE_QUOTE 'H'  ;

			
//C_STRING 	: 	'"'	(UPPER | LOWER )*  '"' ;

C_STRING	:	'"' (~'"')* '"'	;



//*************************************************************************
//**********		PARSER DEFINITIONS
//*************************************************************************


class ASNParser	extends	Parser;
options	{
	exportVocab=ASN;
	k=3;
}

// Grammar Definitions


module_definitions[AsnModel model] 
{
    AsnModule module;
}
	:	(module = module_definition { model.modulesByName.put(module.moduleIdentifier.name, module); })+ 
	;



module_definition returns [AsnModule module] 	
{
    module = new AsnModule();
	AsnModuleIdentifier mid;
	String s ;	
}
	:	(mid = module_identifier
		{ module.moduleIdentifier = mid; 	})
		DEFINITIONS_KW 
		(( e:EXPLICIT_KW {module.tagDefault = e.getText();}
		  |i:IMPLICIT_KW {module.tagDefault = i.getText();}
		  |a:AUTOMATIC_KW {module.tagDefault = a.getText();}
		 ) TAGS_KW {module.tag = true;} |) 
		(EXTENSIBILITY_KW IMPLIED_KW {module.extensible=true;} | )
		ASSIGN_OP 
		BEGIN_KW 
		module_body[module] 
		END_KW
	; 

module_identifier  returns [ AsnModuleIdentifier mid ]
{mid = new AsnModuleIdentifier();
AsnOidComponentList cmplst; }
	:	(( md:UPPER { mid.name = md.getText();}) 
		 ((cmplst = obj_id_comp_lst { mid.componentList = cmplst; })|) 
		)
	;

obj_id_comp_lst	returns [AsnOidComponentList oidcmplst]
{oidcmplst = new AsnOidComponentList();
AsnOidComponent oidcmp; AsnDefinedValue defval; }
	:	L_BRACE 
		(defval = defined_value {oidcmplst.isDefinitive=true;oidcmplst.defval=defval;})?
		(oidcmp = obj_id_component {oidcmplst.components.add(oidcmp);})+  
		R_BRACE 
	;

obj_id_component returns [AsnOidComponent oidcmp ]
{oidcmp = new AsnOidComponent(); AsnDefinedValue defval;
String s,n =""; }
	: 	((num:NUMBER {s=num.getText();oidcmp.num = new Integer(s); oidcmp.numberForm=true;})
	|	(LOWER (L_PAREN NUMBER R_PAREN)?)=>((lid:LOWER {oidcmp.name = lid.getText();oidcmp.nameForm=true;}) 
		( L_PAREN 
		 (num1:NUMBER {n=num1.getText(); oidcmp.num = new Integer(n);oidcmp.nameAndNumberForm=true;})
		R_PAREN ) ? )
	|	(defined_value)=>(defval = defined_value {oidcmp.isDefinedValue=true;oidcmp.defval=defval;}))
	;
		
tag_default returns [String s]
{ s = ""; }
	:	(tg:EXPLICIT_KW   {s = tg.getText();})
	|	(tg1:IMPLICIT_KW  {s = tg1.getText();})
	|	(tg2:AUTOMATIC_KW {s = tg2.getText();})
	;
		
module_body[AsnModule module] 
//	:	(exports[module])? (imports[module])? (assignment[module])+
	:	(exports[module]|) (imports[module]|) ((assignment[module])+ |)
	;

exports[AsnModule module]		
{String s; ArrayList syml = new ArrayList();}
//	:	(EXPORTS_KW {module.exported=true;})   (s = symbol { module.exportList.add(s) ; })
//		(COMMA (s = symbol {module.exportList.add(s) ;} ) )*  SEMI
//	;
	:	EXPORTS_KW {module.exported=true;} 
		((syml = symbol_list {module.exportSymbolList = syml;} |)
		|ALL_KW) 
		SEMI
	;	
		
imports[AsnModule module]
	:	(IMPORTS_KW (((symbols_from_module[module])+)|)  SEMI)
		{module.imported=true;}
	;

symbols_from_module[AsnModule module]
{SymbolsFromModule sym = new SymbolsFromModule();
String s = "" ; AsnModuleIdentifier mid; AsnDefinedValue defval;
ArrayList arl; AsnOidComponentList cmplist;}

	:	((arl= symbol_list {sym.symbolList = arl;}) FROM_KW	
	    (up:UPPER {sym.modref = up.getText();}
	     (cmplist = obj_id_comp_lst {sym.isOidValue=true;sym.cmplist = cmplist;}
	     |(defined_value)=>(defval = defined_value {sym.isDefinedValue=true;sym.defval=defval;})|)))
	    {module.importSymbolFromModuleList.add(sym);}
	;
	
symbol_list returns[ArrayList symlist]
{symlist = new ArrayList(); String s=""; }
	:	((s = symbol {symlist.add(s); })
		(COMMA (s = symbol {symlist.add(s); }))*) 
	; 

symbol returns [String s]
{s="";}			
	:	up:UPPER  { s = up.getText();}
	|	lid:LOWER { s = lid.getText();}
	|	s=macroName   	//To solve the matching of Macro Name with Keyword
	;

macroName returns [String s]
{s="";}
	:	OPERATION_KW 						{ s = "OPERATION";}
	|	ERROR_KW							{ s = "ERROR";}
 	|	"BIND"								{ s = "BIND";}
 	|	"UNBIND"							{ s = "UNBIND";}
 	|	"APPLICATION-SERVICE-ELEMENT"		{ s = "APPLICATION-SERVICE-ELEMENT";}
 	|	"APPLICATION-CONTEXT"				{ s = "APPLICATION-CONTEXT";}
 	|	"EXTENSION"							{ s = "EXTENSION";}
 	|	"EXTENSIONS"						{ s = "EXTENSIONS";}
 	|	"EXTENSION-ATTRIBUTE"				{ s = "EXTENSION-ATTRIBUTE";}
 	|	"TOKEN"								{ s = "TOKEN";}
 	|	"TOKEN-DATA"						{ s = "TOKEN-DATA";}
 	|	"SECURITY-CATEGORY"					{ s = "SECURITY-CATEGORY";}
 	|	"OBJECT"							{ s = "OBJECT";}
 	|	"PORT"								{ s = "PORT";}
 	|	"REFINE"							{ s = "REFINE";}
 	|	"ABSTRACT-BIND"						{ s = "ABSTRACT-BIND";}
 	|	"ABSTRACT-UNBIND"					{ s = "ABSTRACT-UNBIND";}
 	|	"ABSTRACT-OPERATION"				{ s = "ABSTRACT-OPERATION";}
 	|	"ABSTRACT-ERROR"					{ s = "ABSTRACT-ERROR";}
 	|	"ALGORITHM"							{ s = "ALGORITHM";}
 	|	"ENCRYPTED"							{ s = "ENCRYPTED";}
 	|	"SIGNED"							{ s = "SIGNED";}
 	|	"SIGNATURE"							{ s = "SIGNATURE";}
 	|	"PROTECTED"							{ s = "PROTECTED";}
 	|	"OBJECT-TYPE"						{ s = "OBJECT-TYPE";}
 	;

assignment[AsnModule module]	
{Object obj ; Object objv; AsnValue val;	}

// Type Assignment Definition
	:	(up:UPPER ASSIGN_OP	(obj=type) 
{
			((AsnType)obj).name = up.getText();
            module.typesByName.put(((AsnType)obj).name,(AsnType)obj);
		})

// Value Assignment definition	
	|	(lid:LOWER (objv = type ) ASSIGN_OP (val = value) 
		{
   			val.name=lid.getText();
			module.asnValues.add(val);
		})
// Definition of Macro type. Consume the definitions . No Actions

	|(UPPER "MACRO" ASSIGN_OP BEGIN_KW (~(END_KW) )* END_KW)=>  UPPER "MACRO" ASSIGN_OP BEGIN_KW (~(END_KW))* END_KW
// ***************************************************
// Define the following
// ***************************************************
//	|XMLValueAssignment 
//	|ValueSetTypeAssignment 
//	|ObjectClassAssignment 
//	|ObjectAssignment 
//	|ObjectSetAssignment 
//	|ParameterizedAssignment
	;

/*TYPES===============================================================*/

type returns [Object obj]		
{obj = null;}
	:	(obj = built_in_type)
	|	(obj = defined_type)		// Referenced Type
	|	(obj = selection_type ) 		// Referenced Type
	|	(obj = macros_type)
	;

built_in_type returns [Object obj]
{obj = null;}
	:	(obj = any_type)
	|	(obj = anyNoDecode_type)
	|	(obj = bit_string_type)
	|	(obj = boolean_type )
	|	(obj = character_str_type)
	|	(obj = choice_type)
	|	(obj = embedded_type) EMBEDDED_KW  PDV_KW		
	|	(obj = enum_type) 
	|	(obj = external_type) 
	|	(obj = integer_type )
	|	(obj = null_type )
//	|	ObjectClassFieldType OBJECT_DESCRIPTOR_KW
	|	(obj = object_identifier_type)
	|	(obj = octetString_type)
	|	(obj = real_type )
	|	(obj = relativeOid_type)
	|	(obj = sequence_type)
	|	(obj = sequenceof_type)
	|	(obj = set_type)
	|	(obj = setof_type)
	|	(obj = tagged_type)
	;
			
any_type returns [AsnAny an]
{an = new AsnAny();}
	: (	ANY_KW  ( DEFINED_KW BY_KW {an.isDefinedBy = true ;} lid:LOWER { an.definedByType = lid.getText();})? )
	;

anyNoDecode_type returns [Object obj]
{obj = null;AsnAnyNoDecode an = new AsnAnyNoDecode();}
	: (	ANY_NODECODE_KW  ( DEFINED_KW BY_KW {an.isDefinedBy = true ;} lid:LOWER { an.definedByType = lid.getText();})? )
		{obj = an ;  an = null;}
	;
	
bit_string_type	returns [Object obj]
{AsnBitString bstr = new AsnBitString(); 
AsnNamedNumberList nnlst ; AsnConstraint cnstrnt;obj = null;}
	:	(BIT_KW STRING_KW (nnlst =namedNumber_list { bstr.namedNumberList = nnlst;})? 
		(cnstrnt = constraint { bstr.constraint = cnstrnt;} )? )
		{obj=bstr; nnlst = null ; cnstrnt = null;}
	;

// Includes Useful types as well
character_str_type returns [Object obj]
{AsnCharacterString cstr = new AsnCharacterString();
String s ; AsnConstraint cnstrnt; obj = null;}
	:	((CHARACTER_KW STRING_KW {cstr.isUCSType = true;})
	|	(s = character_set {cstr.stringtype = s;} 
		(cnstrnt = constraint{cstr.constraint = cnstrnt;})? ))
		{obj = cstr; cnstrnt = null; cstr = null;}
	;
		
character_set returns [String s]
{s = "";}
	:	(s1:BMP_STR_KW 			{s = s1.getText();})
	|	(s2:GENERALIZED_TIME_KW	{s = s2.getText();})
	|	(s3:GENERAL_STR_KW		{s = s3.getText();})
	|	(s4:GRAPHIC_STR_KW		{s = s4.getText();})
	|	(s5:IA5_STRING_KW		{s = s5.getText();})
	|	(s6:ISO646_STR_KW		{s = s6.getText();})
	|	(s7:NUMERIC_STR_KW		{s = s7.getText();})
	|	(s8:PRINTABLE_STR_KW	{s = s8.getText();})
	|	(s9:TELETEX_STR_KW		{s = s9.getText();})
	|	(s10:T61_STR_KW			{s = s10.getText();})
	|	(s11:UNIVERSAL_STR_KW	{s = s11.getText();})
	|	(s12:UTF8_STR_KW		{s = s12.getText();})
	|	(s13:UTC_TIME_KW		{s = "UtcTime";})
	|	(s14:VIDEOTEX_STR_KW	{s = s14.getText();})
	|	(s15:VISIBLE_STR_KW		{s = s15.getText();})
	;		
boolean_type returns [Object obj]
{obj = null;}
	: BOOLEAN_KW 
	  {obj = new AsnBoolean();}
	;
				
choice_type	returns [Object obj]
{AsnChoice ch = new AsnChoice(); List<AsnComponentType> eltplst; 
obj = null;}
	: (	CHOICE_KW L_BRACE (eltplst = elementType_list {ch.componentTypes = eltplst ;}) R_BRACE ) 
		{obj = ch; eltplst = null; ch = null;}
	;

embedded_type returns [Object obj]
{obj = null;}
	:	(EMBEDDED_KW  PDV_KW)
		{obj = new AsnEmbedded();}
	;
enum_type returns [Object obj]
{AsnEnum enumtyp = new AsnEnum() ;
AsnNamedNumberList nnlst; obj = null;}
	: ( ENUMERATED_KW (nnlst = namedNumber_list { enumtyp.namedNumberList = nnlst;}) )
	  {obj = enumtyp ; enumtyp=null;}	
	;
		
external_type returns [Object obj]
{obj = null; }
	: EXTERNAL_KW {obj = new AsnExternal();}
	;

integer_type returns [Object obj]	
{AsnInteger intgr = new AsnInteger();
AsnNamedNumberList numlst; AsnConstraint cnstrnt; obj=null;}
	: (	INTEGER_KW (numlst = namedNumber_list {intgr.namedNumberList = numlst;}
		| cnstrnt = constraint {intgr.constraint = cnstrnt;})? )
		{obj = intgr ; numlst = null ; cnstrnt = null; intgr = null; }
	;
		
null_type returns [Object obj]
{AsnNull nll = new AsnNull(); obj = null;}
	: NULL_KW
	  {obj = nll; nll = null ; }
	;

object_identifier_type returns [Object obj]
{AsnObjectIdentifier objident = new AsnObjectIdentifier(); obj = null;}
	: OBJECT_KW IDENTIFIER_KW 
	  {obj = objident; objident = null;}	
	; 
	
octetString_type returns [Object obj]
{AsnOctetString oct = new AsnOctetString();
AsnConstraint cnstrnt ; obj = null;}
	: (	OCTET_KW STRING_KW (cnstrnt = constraint{oct.constraint = cnstrnt;})? )
		{obj = oct ; cnstrnt = null;}
	;

real_type returns [Object obj]
{AsnReal rl = new AsnReal();obj = null;}
	: REAL_KW  {obj = rl ; rl = null;}		
	;

relativeOid_type returns [Object obj]
{obj = null; }
	: RELATIVE_KW MINUS OID_KW {obj = new AsnRelativeOid();}
	;
		
sequence_type returns [Object obj]
{AsnSequenceSet seq = new AsnSequenceSet();
List<AsnComponentType> eltplist ; AsnConstraint cnstrnt ; obj = null;}
	:  ( SEQUENCE_KW {seq.isSequence = true;} 
	    L_BRACE 
	   (eltplist = elementType_list {seq.componentTypes = eltplist;})? 
	    R_BRACE )
		{obj = seq ; eltplist = null; seq =null; }
	;
	
		
	
sequenceof_type returns [AsnSequenceOf obj]
{AsnSequenceOf seqof = new AsnSequenceOf();
AsnConstraint cnstrnt; obj = null; Object referencedAsnType ; String s ;}
	:  ( SEQUENCE_KW {seqof.isSequenceOf = true;}
	         (cnstrnt = constraint{seqof.constraint = cnstrnt;})? OF_KW 
		( referencedAsnType = type 
		{	if((AsnDefinedType.class).isInstance(referencedAsnType)){
		  		seqof.isDefinedType=true;
				seqof.typeName = ((AsnDefinedType)referencedAsnType).typeName ; 
			}
			else{	
				seqof.typeReference = (AsnType) referencedAsnType ; 
			}
		}) )
		{obj = seqof;  cnstrnt = null; seqof=null;}		
	;
	
set_type returns [Object obj]
{AsnSequenceSet set = new AsnSequenceSet();
List<AsnComponentType> eltplist ;obj = null;}
	:  ( SET_KW L_BRACE (eltplist =  elementType_list {set.componentTypes = eltplist ;})? R_BRACE )
		{obj = set ; eltplist = null; set = null;}
	;
		
setof_type	returns [Object obj]
{AsnSequenceOf setof = new AsnSequenceOf();
AsnConstraint cns; obj = null;
Object obj1 ; String s;}
	:	(SET_KW	
	(cns = constraint {setof.constraint = cns ;})? OF_KW 
		(obj1 = type 
		{	if((AsnDefinedType.class).isInstance(obj1)){
		  		setof.isDefinedType=true;
				setof.typeName = ((AsnDefinedType)obj1).typeName ; 
			}
			else{
				setof.typeReference = (AsnType) obj1;
			} 
		}) )
		{obj = setof; cns = null; obj1=null; setof=null;} 		
	;

tagged_type returns [Object obj]
{AsnNamedType tgtyp = new AsnNamedType();
AsnTag tg; Object obj1 = null; String s; obj = null;}
	:	((tg = tag {tgtyp.tag = tg ;}) 
		(s = tag_default { tgtyp.tagType = s ;})? 
		(obj1 = type 
		{	if((AsnDefinedType.class).isInstance(obj1)){
		  		tgtyp.isDefinedType=true;
				tgtyp.typeName = ((AsnDefinedType)obj1).typeName ; 
			}
			else{	
				tgtyp.typeReference = obj1; 
			} 
		}))
		{obj = tgtyp ; tg = null; obj1= null ;tgtyp = null; }
	;


tag	returns [AsnTag tg]	
{tg = new AsnTag(); String s; AsnClassNumber cnum;} 
	:	(L_BRACKET (s = clazz {tg.clazz = s ;})? (cnum = class_NUMBER { tg.classNumber = cnum ;}) R_BRACKET )
	;
	
clazz returns [String s]
{s = ""; }			
	:	(c1:UNIVERSAL_KW 	{s= c1.getText();})
	|	(c2:APPLICATION_KW	{s= c2.getText();})
	|	(c3:PRIVATE_KW		{s= c3.getText();})
	;

class_NUMBER returns [AsnClassNumber cnum]		
{cnum = new AsnClassNumber() ; String s; }
	:	((num:NUMBER {s=num.getText(); cnum.num = new Integer(s);})
	|	(lid:LOWER  {s=lid.getText(); cnum.name = s ;}) )		
	;

defined_type returns [Object obj]	
{AsnDefinedType deftype = new AsnDefinedType();
AsnConstraint cnstrnt; obj = null;}
	:	((up:UPPER {deftype.isModuleReference = true ;deftype.moduleReference = up.getText();} 
			DOT )? 
		(up1:UPPER {deftype.typeName = up1.getText();})
		(cnstrnt = constraint{deftype.constraint = cnstrnt;})? )
		{obj = deftype; deftype=null ; cnstrnt = null;}
	;

selection_type returns [Object obj]
{AsnSelectionType seltype = new AsnSelectionType();
obj = null;Object obj1;}
	:	((lid:LOWER { seltype.selectionID = lid.getText();})
	 	LESS
	 	(obj1=type {seltype.type = obj1;}))
	 	{obj=seltype; seltype=null;}
	;


macros_type	returns [Object obj]
{obj = null;}
		:	(obj = operation_macro)
		|	(obj = error_macro)
		|	(obj = objecttype_macro)
		;

operation_macro	returns [Object obj]
{OperationMacro op = new OperationMacro();
String s ;obj = null; Object obj1; Object obj2;}
	: (	"OPERATION"
		(ARGUMENT_KW (ld1:LOWER {op.argumentTypeIdentifier = ld1.getText();})?
		((obj2 = type)
			{op.argumentType = obj2; op.isArgumentName=true;
				if((AsnDefinedType.class).isInstance(obj2))
					op.argumentName = ((AsnDefinedType)obj2).typeName;
				else
					op.argumentName = op.argumentTypeIdentifier;
			}
		))?
		(RESULT_KW  {op.isResult=true;} ((SEMI)=>SEMI|((LOWER)? type)=>(ld2:LOWER {op.resultTypeIdentifier = ld2.getText();})?
		(obj1=type 
			{op.resultType=obj1;op.isResultName=true;
				if((AsnDefinedType.class).isInstance(obj1))
					op.resultName = ((AsnDefinedType)obj1).typeName;
				else
					op.resultName = op.resultTypeIdentifier;
			}
		)|))?
		(ERRORS_KW L_BRACE (operation_errorlist[op]
				{op.isErrors=true;}|) R_BRACE )?
		(LINKED_KW L_BRACE (linkedOp_list[op]
			{op.isLinkedOperation = true ;})?	 R_BRACE )? )
		{obj = op;}
	;

operation_errorlist[OperationMacro oper]
{Object obj;}
	:	obj = typeorvalue {oper.errorList.add(obj);}
		(COMMA (obj = typeorvalue {oper.errorList.add(obj);}))*
	;
	
linkedOp_list[OperationMacro oper]
{Object obj;}
	:	obj = typeorvalue {oper.linkedOpList.add(obj);}
		(COMMA (obj = typeorvalue {oper.linkedOpList.add(obj);}))*
	;
	
error_macro	returns [Object obj]
{ErrorMacro merr = new ErrorMacro();obj = null;
Object obj1;}
	:  ( ERROR_KW  (PARAMETER_KW {merr.isParameter = true; }
		(( lw:LOWER { merr.parameterName = lw.getText(); })? 
		(obj1 = type 
		{	if((AsnDefinedType.class).isInstance(obj1)){
				merr.isDefinedType=true;
				merr.typeName = ((AsnDefinedType)obj1).typeName ; 
			}
			else{
				merr.typeReference = obj1 ;
			}
		}) ) )? )
		{obj = merr ; merr = null;}
	;
	
objecttype_macro returns [Object obj]
{ObjectType objtype = new ObjectType();
AsnValue val; obj = null; String s; Object typ;} 
	: ("OBJECT-TYPE" "SYNTAX" (typ = type {objtype.type=typ;} )
		("ACCESS" lid:LOWER {objtype.accessPart = lid.getText();})
	  ("STATUS" lid1:LOWER {objtype.statusPart = lid1.getText();}) 
	  ("DESCRIPTION" CHARACTER_KW STRING_KW)?
	  ("REFERENCE" CHARACTER_KW STRING_KW)? 
	  ("INDEX" L_BRACE (typeorvaluelist[objtype]) R_BRACE)? 
	  ("DEFVAL" L_BRACE ( val = value {objtype.value = val;}) R_BRACE )? )
	  {obj= objtype; objtype = null;}	
	;

typeorvaluelist[ObjectType objtype]
{Object obj; }
	: ((obj = typeorvalue {objtype.elements.add(obj);})
	   (COMMA (obj=typeorvalue {objtype.elements.add(obj);})* ))
	;

typeorvalue returns [Object obj]
{Object obj1; obj=null;}
	: ((type)=>(obj1 = type) | obj1 = value)
	  {obj = obj1; obj1=null;}
	;

elementType_list returns [List<AsnComponentType> elelist]
{elelist = new ArrayList<>(); AsnComponentType eletyp; int i=1; }
	:	(ELLIPSIS | eletyp = elementType {if (eletyp.name.isEmpty()) {eletyp.name = "element" + i;};elelist.add(eletyp);i++; }
	    (COMMA (ELLIPSIS | (eletyp = elementType {if (eletyp.name.isEmpty()) {eletyp.name = "element" + i;};elelist.add(eletyp);i++; })))*)
	;

elementType	returns [AsnComponentType eletyp]
{eletyp = new AsnComponentType();AsnValue val; 
Object obj; AsnTag tg; String s;}
	: (	((lid:LOWER {eletyp.name = lid.getText();})? 
		(tg = tag { eletyp.tag = tg ;})? 
		(s = tag_default {eletyp.tagType = s ;})? 
		(obj = type) ( (OPTIONAL_KW {eletyp.isOptional=true;}) 
		| (DEFAULT_KW { eletyp.isDefault = true;} 
		 val = value {eletyp.value = val;} ))? )
	|	COMPONENTS_KW OF_KW {eletyp.isComponentsOf = true;}(obj = type ))
		{
			if((AsnDefinedType.class).isInstance(obj)){
				eletyp.isDefinedType=true;
				eletyp.typeName = ((AsnDefinedType)obj).typeName ; 
			} else{		
				eletyp.typeReference = obj;
			}
		}
	;
		
namedNumber_list returns [AsnNamedNumberList nnlist]
{nnlist = new AsnNamedNumberList();AsnNamedNumber nnum ; }	
	: (	L_BRACE (ELLIPSIS | nnum= namedNumber {nnlist.namedNumbers.add(nnum); })
	   (COMMA ( ELLIPSIS | (nnum = namedNumber  {nnlist.namedNumbers.add(nnum); }) ))*  R_BRACE )
	;


namedNumber	returns [AsnNamedNumber nnum]
{nnum = new AsnNamedNumber() ;AsnSignedNumber i; 
AsnDefinedValue s;	}
	:	(lid:LOWER {nnum.name = lid.getText();} L_PAREN 
		(i = signed_number {nnum.signedNumber = i;nnum.isSignedNumber=true;}
		| (s = defined_value {nnum.definedValue=s;})) R_PAREN	)
	;
	
constraint returns [AsnConstraint cnstrnt]
{cnstrnt=new AsnConstraint();}
	:(L_PAREN 
		(element_set_specs[cnstrnt]{cnstrnt.isElementSetSpecs=true;})? 
		(exception_spec[cnstrnt])? 
	  R_PAREN)*
    |   (element_set_specs[cnstrnt]{cnstrnt.isElementSetSpecs=true;})? 
		(exception_spec[cnstrnt])? 
	;

exception_spec[AsnConstraint cnstrnt]
{AsnSignedNumber signum; AsnDefinedValue defval;
Object typ;AsnValue val;}
	: (EXCLAMATION 
	  ( (signed_number)=>(signum=signed_number {cnstrnt.isSignedNumber=true;cnstrnt.signedNumber=signum;})
	   |(defined_value)=>(defval=defined_value {cnstrnt.isDefinedValue=true;cnstrnt.definedValue=defval;})
	   |typ=type COLON val=value {cnstrnt.isColonValue=true;cnstrnt.type=typ;cnstrnt.value=val;}))
	   {cnstrnt.isExceptionSpec=true;}
	;

element_set_specs[AsnConstraint cnstrnt]
{ElementSetSpec elemspec;}
	:	(elemspec=element_set_spec { 
				cnstrnt.elemSetSpec=elemspec; // TODO - need list.add() func
		}
		(COMMA ELLIPSIS {cnstrnt.isCommaDotDot=true;})? 
		(COMMA elemspec=element_set_spec {cnstrnt.addElemSetSpec=elemspec;cnstrnt.isAdditionalElementSpec=true;})?)
	;

element_set_spec returns [ElementSetSpec elemspec]	
{elemspec = new ElementSetSpec(); Intersection intersect;ConstraintElements cnselem;}
	:	intersect=intersections {elemspec.intersectionList.add(intersect);}
		((BAR | UNION_KW ) intersect=intersections {elemspec.intersectionList.add(intersect);})*
	| ALL_KW EXCEPT_KW cnselem=constraint_elements  {elemspec.allExceptCnselem=cnselem;elemspec.isAllExcept=true;}
	;

// Coding is not proper for EXCEPT constraint elements. 
// One EXCEPT constraint elements should be tied to one Constraint elements
//(an object of constraint and except list)
// and not in one single list
intersections returns [Intersection intersect]
{intersect = new Intersection();ConstraintElements cnselem;}

	:	cnselem=constraint_elements {intersect.cnsElemList.add(cnselem);}
	   (EXCEPT {intersect.isExcept=true;} cnselem=constraint_elements {intersect.exceptCnsElem.add(cnselem);})? 
	   ((INTERSECTION | INTERSECTION_KW) {intersect.isInterSection=true;}
	   cnselem=constraint_elements {intersect.cnsElemList.add(cnselem);}
	   (EXCEPT cnselem=constraint_elements {intersect.exceptCnsElem.add(cnselem);})?)*
	;
				
constraint_elements	returns [ConstraintElements cnsElem]
{ cnsElem = new ConstraintElements(); AsnValue val;
AsnConstraint cns; ElementSetSpec elespec;Object typ; }
	:	(value)=>(val = value {cnsElem.isValue=true;cnsElem.value=val;})
	|	(value_range[cnsElem])=>(value_range[cnsElem]	{cnsElem.isValueRange=true;})
	|	(SIZE_KW cns=constraint {cnsElem.isSizeConstraint=true;cnsElem.constraint=cns;})
	|	(FROM_KW cns=constraint {cnsElem.isAlphabetConstraint=true;cnsElem.constraint=cns;})
	|	(L_PAREN elespec=element_set_spec {cnsElem.isElementSetSpec=true;cnsElem.elespec=elespec;} R_PAREN)
	|	((INCLUDES {cnsElem.isIncludeType=true;})? typ=type {cnsElem.isTypeConstraint=true;cnsElem.type=typ;})
	|	(PATTERN_KW val=value {cnsElem.isPatternValue=true;cnsElem.value=val;})
	|	(WITH_KW 
		((COMPONENT_KW cns=constraint {cnsElem.isWithComponent=true;cnsElem.constraint=cns;})
		|	
		(COMPONENTS_KW {cnsElem.isWithComponents=true;}
		L_BRACE (ELLIPSIS COMMA)? type_constraint_list[cnsElem] R_BRACE )))
	;

value_range[ConstraintElements cnsElem]
{AsnValue val;}
	: (val=value {cnsElem.lEndValue=val;} | MIN_KW {cnsElem.isMinKw=true;}) (LESS {cnsElem.isLEndLess=true;})?  // lower end
	   DOTDOT
	  (LESS {cnsElem.isUEndLess=true;})? (val=value{cnsElem.uEndValue=val;} | MAX_KW {cnsElem.isMaxKw=true;}) // upper end
	;
	
type_constraint_list[ConstraintElements cnsElem]
{NamedConstraint namecns;}
	: namecns=named_constraint {cnsElem.typeConstraintList.add(namecns);}
	 (COMMA namecns=named_constraint {cnsElem.typeConstraintList.add(namecns);})*
	;

named_constraint returns [NamedConstraint namecns]
{namecns = new NamedConstraint(); AsnConstraint cns;}
	:	lid:LOWER {namecns.name=lid.getText();}
	    (cns=constraint {namecns.isConstraint=true;namecns.constraint=cns;})? 
	    (PRESENT_KW {namecns.isPresentKw=true;}
	     |ABSENT_KW {namecns.isAbsentKw=true;}
	     | OPTIONAL_KW {namecns.isOptionalKw=true;})?
	;
				
/*-----------VALUES ---------------------------------------*/

value returns [AsnValue value]
{value = new AsnValue(); AsnSequenceValue seqval;
AsnDefinedValue defval;String aStr;AsnSignedNumber num; 
AsnOidComponentList cmplst;}		

	: 	(TRUE_KW)=>(TRUE_KW 				{value.isTrueKW = true; })
	|	(FALSE_KW)=>(FALSE_KW				{value.isFalseKW = true;})
	|	(NULL_KW)=>(NULL_KW				{value.isNullKW = true;})
	|	(C_STRING)=>(c:C_STRING				{value.isCString=true; value.cStr = c.getText();})
	|	(defined_value)=>(defval = defined_value {value.isDefinedValue = true; value.definedValue = defval;})
	|	(signed_number)=>(num = signed_number	{value.isSignedNumber=true ; value.signedNumber = num;}) 
	|	(choice_value[value])=>(choice_value[value]	{value.isChoiceValue = true;})
	|	(sequence_value)=>(seqval=sequence_value	{value.isSequenceValue=true;value.seqval=seqval;})
	|	(sequenceof_value[value])=>(sequenceof_value[value] {value.isSequenceOfValue=true;})
	|	(cstr_value[value])=>(cstr_value[value]		{value.isCStrValue = true;})
	|	(obj_id_comp_lst)=>(cmplst=obj_id_comp_lst	{value.isAsnOIDValue=true;value.oidval=cmplst;})
	|	(PLUS_INFINITY_KW)=>(PLUS_INFINITY_KW		{value.isPlusInfinity = true;})
	|	(MINUS_INFINITY_KW)=>(MINUS_INFINITY_KW		{value.isMinusInfinity = true;})
	;

cstr_value[AsnValue value]
{AsnBitOrOctetStringValue bstrval = new AsnBitOrOctetStringValue();
AsnCharacterStringValue cstrval = new AsnCharacterStringValue();
AsnSequenceValue seqval;}
	:  ((H_STRING)=>(h:H_STRING 	{bstrval.isHString=true; bstrval.bhStr = h.getText();})
	|	(B_STRING)=>(b:B_STRING		{bstrval.isBString=true; bstrval.bhStr = b.getText();})
	|	(L_BRACE	((id_list[bstrval])=>(id_list[bstrval])
					|(char_defs_list[cstrval])=>(char_defs_list[cstrval])
					| tuple_or_quad[cstrval])    R_BRACE))
		{value.cStrValue=cstrval;value.bStrValue=bstrval;}
	;

id_list[AsnBitOrOctetStringValue bstrval]
{String s="";}
	: (ld:LOWER {s = ld.getText(); bstrval.idlist.add(s);}) 
	  (COMMA ld1:LOWER {s = ld1.getText();bstrval.idlist.add(s);})*
	;
	
char_defs_list[AsnCharacterStringValue cstrval]
{CharDef a ;}
	:a = char_defs {cstrval.isCharDefList = true;cstrval.charDefsList.add(a);} 
	(COMMA (a = char_defs {cstrval.charDefsList.add(a);}))* 
	;

tuple_or_quad[AsnCharacterStringValue cstrval]
{AsnSignedNumber n;}
	: (n = signed_number {cstrval.tupleQuad.add(n);}) 
	  COMMA 
	  (n = signed_number {cstrval.tupleQuad.add(n);}) 
	  ((R_BRACE {cstrval.isTuple=true;})  |  (COMMA 
	  (n = signed_number {cstrval.tupleQuad.add(n);}) 
	  COMMA (n = signed_number {cstrval.tupleQuad.add(n);})))
	;

char_defs  returns [CharDef chardef]
{chardef = new CharDef(); 
AsnSignedNumber n ; AsnDefinedValue defval;}
	:	(c:C_STRING {chardef.isCString = true;chardef.cStr=c.getText();})
	|	(L_BRACE (n = signed_number {chardef.tupleQuad.add(n);}) COMMA (n = signed_number {chardef.tupleQuad.add(n);}) 
		((R_BRACE {chardef.isTuple=true;})
		|(COMMA (n = signed_number {chardef.tupleQuad.add(n);}) 
		COMMA (n = signed_number {chardef.tupleQuad.add(n);}) R_BRACE{chardef.isQuadruple=true;})))
	|	(defval = defined_value {chardef.defval=defval;})
	;

choice_value[AsnValue value]
{AsnChoiceValue chval = new AsnChoiceValue(); AsnValue val;}
	: ((lid:LOWER {chval.name = lid.getText();})
	   (COLON)?  (val=value {chval.value = val;}))
	  {value.chval = chval;}
	;

sequence_value returns [AsnSequenceValue seqval]
{AsnNamedValue nameval = new AsnNamedValue();
seqval = new AsnSequenceValue();}
	:	L_BRACE  ((nameval=named_value {seqval.isValPresent=true;seqval.namedValueList.add(nameval);})?
		(COMMA nameval=named_value {seqval.namedValueList.add(nameval);})*)   R_BRACE
	;

sequenceof_value[AsnValue value]
{AsnValue val;value.seqOfVal = new AsnSequenceOfValue();}
	: L_BRACE ((val=value {value.seqOfVal.value.add(val);})?
       (COMMA val=value {value.seqOfVal.value.add(val);})*) 
	  R_BRACE
	;

protected
defined_value returns [AsnDefinedValue defval]
{defval = new AsnDefinedValue(); }
	:	((up:UPPER {defval.moduleIdentifier = up.getText(); } 
			DOT {defval.isDotPresent=true;})? 
		lid:LOWER { defval.name = lid.getText();})
	;
		
signed_number returns [AsnSignedNumber i]
{i = new AsnSignedNumber() ; String s ; }
	:	((MINUS {i.positive=false;})? 
		(n:NUMBER  {s = n.getText(); i.num= new BigInteger(s);}) )
	;
	
named_value returns [AsnNamedValue nameval]
{nameval = new AsnNamedValue(); AsnValue val;}	
	:	(lid:LOWER	{nameval.name = lid.getText(); } 
		val=value	{nameval.value = val;})
	;	

