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

public class AsnConstraint {
    public ElementSetSpec addElemSetSpec;
    public AsnDefinedValue definedValue;
    public ElementSetSpec elemSetSpec;
    public boolean isAdditionalElementSpec;
    public boolean isColonValue;
    public boolean isCommaDotDot;
    public boolean isDefinedValue;
    public boolean isElementSetSpecs;
    public boolean isExceptionSpec;
    public boolean isSignedNumber;
    public AsnSignedNumber signedNumber;
    public Object type;
    public AsnValue value;

    // Return AllExcept additional Constraint Elements
    public ConstraintElements addElemSS_allExceptConstraintElem() {
        return addElemSetSpec.allExceptCnselem;
    }

    // Return if All Except additional Constraint Elements exists
    public boolean addElemSS_isAllExcept() {
        if (addElemSetSpec.isAllExcept) {
            return true;
        }
        else {
            return false;
        }
    }

    // Return AllExcept Constraint Elements
    public ConstraintElements elemSS_allExceptConstraintElem() {
        return elemSetSpec.allExceptCnselem;
    }

    // Return if All Except Constraint Elements exists
    public boolean elemSS_isAllExcept() {
        if (elemSetSpec.isAllExcept) {
            return true;
        }
        else {
            return false;
        }
    }

    // Return the required additional intersection element
    public Intersection get_addElemSS_IntsectElem(int i) {
        return (Intersection) addElemSetSpec.intersectionList.get(i);
    }

    // Returns first additional constraint Element in the first Intersection
    // List
    public ConstraintElements get_addElemSS_firstConstraintElem() {
        Intersection intersect = get_addElemSS_firstIntsectElem();

        return (ConstraintElements) intersect.cnsElemList.get(0);
    }

    // Return the first additional intersection element
    public Intersection get_addElemSS_firstIntsectElem() {
        return (Intersection) addElemSetSpec.intersectionList.get(0);
    }

    public ConstraintElements get_addElemSS_intersectionConstraintElems(int intersectElem, int constraintElem) {
        Intersection intersect = get_addElemSS_IntsectElem(intersectElem);

        return (ConstraintElements) intersect.cnsElemList.get(constraintElem);
    }

    // Return the required intersection element
    public Intersection get_elemSS_IntsectElem(int i) {
        return (Intersection) elemSetSpec.intersectionList.get(i);
    }

    // Returns first constraint Element in the first Intersection
    // List
    public ConstraintElements get_elemSS_firstConstraintElem() {
        Intersection intersect = get_elemSS_firstIntsectElem();

        return (ConstraintElements) intersect.cnsElemList.get(0);
    }

    // Return the first intersection element
    public Intersection get_elemSS_firstIntsectElem() {
        return (Intersection) elemSetSpec.intersectionList.get(0);
    }

    public ConstraintElements get_elemSS_intersectionConstraintElems(int intersectElem, int constraintElem) {
        Intersection intersect = get_elemSS_IntsectElem(intersectElem);

        return (ConstraintElements) intersect.cnsElemList.get(constraintElem);
    }

    // ---------For additionalElementSetSpecs
    // Return the total intersection elements in the add element
    // Spec list
    public int sz_addElemSS_IntsectList() {
        return addElemSetSpec.intersectionList.size();
    }

    // Returns Number of additional Constraint Elements in the
    // specified IntersectionList
    public int sz_addElemSS_intersectionConstraintElems(int i) {
        Intersection intersect = get_addElemSS_IntsectElem(i);

        if (intersect != null) {
            return intersect.cnsElemList.size();
        }
        else {
            return -1;
        }
    }

    // Returns specified additional Constraint Elements in the specified IntersectionList

    // Return the total intersection elements in the element
    // Spec list
    public int sz_elemSS_IntsectList() {
        return elemSetSpec.intersectionList.size();
    }

    // Returns Number of Constraint Elements in the
    // specified IntersectionList
    public int sz_elemSS_intersectionConstraintElems(int i) {
        Intersection intersect = get_elemSS_IntsectElem(i);

        if (intersect != null) {
            return intersect.cnsElemList.size();
        }
        else {
            return -1;
        }
    }

}
