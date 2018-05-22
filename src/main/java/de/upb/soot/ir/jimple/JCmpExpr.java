/* Soot - a J*va Optimization Framework
 * Copyright (C) 1999 Patrick Lam
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the Sable Research Group and others 1997-1999.  
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */






package de.upb.soot.ir.jimple;

import soot.*;
import soot.jimple.*;
import soot.baf.*;
import soot.util.*;

public class JCmpExpr extends AbstractJimpleIntBinopExpr implements CmpExpr
{
    public JCmpExpr(Value op1, Value op2) { super(op1, op2); }
    public final String getSymbol() { return " " + Jimple.CMP + " " ; }
    public void apply(Switch sw) { ((ExprSwitch) sw).caseCmpExpr(this); }
    Object makeBafInst(Type opType) { return Baf.v().newCmpInst(this.getOp1().getType()); }
    
    public Object clone() 
    {
        return new JCmpExpr(Jimple.cloneIfNecessary(getOp1()), Jimple.cloneIfNecessary(getOp2()));
    }

}