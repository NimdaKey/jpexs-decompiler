/*
 *  Copyright (C) 2010-2013 JPEXS
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.action.treemodel.operations;

import com.jpexs.decompiler.flash.ecma.*;
import com.jpexs.decompiler.flash.graph.BinaryOpItem;
import com.jpexs.decompiler.flash.graph.GraphSourceItem;
import com.jpexs.decompiler.flash.graph.GraphTargetItem;
import com.jpexs.decompiler.flash.graph.LogicalOpItem;

public class StrictNeqTreeItem extends BinaryOpItem implements LogicalOpItem {

    public StrictNeqTreeItem(GraphSourceItem instruction, GraphTargetItem leftSide, GraphTargetItem rightSide) {
        super(instruction, PRECEDENCE_EQUALITY, leftSide, rightSide, "!==");
    }

    @Override
    public Object getResult() {
        Object x = leftSide.getResult();
        Object y = rightSide.getResult();
        return EcmaScript.type(x) != EcmaScript.type(y)
                || (!EcmaScript.equals(x, y));
    }

    @Override
    public GraphTargetItem invert() {
        return new StrictEqTreeItem(src, leftSide, rightSide);
    }
}
