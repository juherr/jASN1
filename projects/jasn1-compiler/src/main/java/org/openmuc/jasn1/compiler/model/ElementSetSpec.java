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

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class ElementSetSpec {
    public ConstraintElements allExceptCnselem;
    public ArrayList intersectionList;
    public boolean isAllExcept;

    // ~--- constructors -------------------------------------------------------

    // Default Constructor
    public ElementSetSpec() {
        intersectionList = new ArrayList();
    }

    // ~--- methods ------------------------------------------------------------

    // toString Method
    @Override
    public String toString() {
        String ts = "";
        Iterator e = intersectionList.iterator();

        if (e != null) {
            while (e.hasNext()) {
                ts += e.next();
                ts += "|";
            }
        }

        if (isAllExcept) {
            ts += "ALL EXCEPT  " + allExceptCnselem;
        }

        return ts;
    }
}
