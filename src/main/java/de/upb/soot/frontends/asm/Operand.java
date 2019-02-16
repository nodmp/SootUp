package de.upb.soot.frontends.asm;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 1997 - 2014 Raja Vallee-Rai and others
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import de.upb.soot.jimple.basic.Local;
import de.upb.soot.jimple.basic.Value;
import de.upb.soot.jimple.basic.ValueBox;
import org.objectweb.asm.tree.AbstractInsnNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stack operand.
 *
 * @author Aaloan Miftah
 */
final class Operand {

  final @Nonnull AbstractInsnNode insn;
  final @Nonnull Value value;
  @Nullable
  Local stack;
  private @Nullable Object boxes;

  /**
   * Constructs a new stack operand.
   *
   * @param insn
   *          the instruction that produced this operand.
   * @param value
   *          the generated value.
   */
  Operand(@Nonnull AbstractInsnNode insn, @Nonnull Value value) {
    this.insn = insn;
    this.value = value;
  }

  /**
   * Removes a value box from this operand.
   *
   * @param vb
   *          the value box.
   */
  @SuppressWarnings("unchecked")
  void removeBox(@Nullable ValueBox vb) {
    if (vb == null) {
      return;
    }
    if (boxes == vb) {
      boxes = null;
    } else if (boxes instanceof List) {
      List<ValueBox> list = (List<ValueBox>) boxes;
      list.remove(vb);
    }
  }

  /**
   * Adds a value box to this operand.
   *
   * @param vb
   *          the value box.
   */
  @SuppressWarnings("unchecked")
  void addBox(@Nonnull ValueBox vb) {
    if (boxes instanceof List) {
      List<ValueBox> list = (List<ValueBox>) boxes;
      list.add(vb);
    } else if (boxes instanceof ValueBox) {
      ValueBox ovb = (ValueBox) boxes;
      List<ValueBox> list = new ArrayList<>();
      list.add(ovb);
      list.add(vb);
      boxes = list;
    } else {
      boxes = vb;
    }
  }

  /** Updates all value boxes registered to this operand. */
  @SuppressWarnings("unchecked")
  void updateBoxes() {
    Value val = stackOrValue();
    if (boxes instanceof List) {
      for (ValueBox vb : (List<ValueBox>) boxes) {
        vb.setValue(val);
      }
    } else if (boxes instanceof ValueBox) {
      ((ValueBox) boxes).setValue(val);
    }
  }

  /**
   * @param <A>
   *          type of value to cast to.
   * @return the value.
   */
  @SuppressWarnings("unchecked")
  @Nonnull
  <A> A value() {
    return (A) value;
  }

  /** @return either the stack local allocated for this operand, or its value. */
  @Nonnull
  Value stackOrValue() {
    Local s = stack;
    return s == null ? value : s;
  }

  /**
   * Determines if this operand is equal to another operand.
   *
   * @param other
   *          the other operand.
   * @return {@code true} if this operand is equal to another operand, {@code false} otherwise.
   */
  boolean equivTo(@Nonnull Operand other) {
    if (other.value == null && value == null) {
      return true;
    }
    return stackOrValue().equivTo(other.stackOrValue());
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Operand && equivTo((Operand) other);
  }
}
