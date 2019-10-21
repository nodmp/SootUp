package de.upb.swt.soot.core.jimple.common.constant;

import de.upb.swt.soot.core.jimple.visitor.ConstantVisitor;
import de.upb.swt.soot.core.jimple.visitor.Visitor;
import de.upb.swt.soot.core.types.PrimitiveType;
import de.upb.swt.soot.core.types.Type;
import javax.annotation.Nonnull;

/**
 * BooleanConstant didn't exist in old soot, because in Java byte code boolean values are
 * represented as integer values 1 or 0. However, from the source code we have the information if a
 * constant is boolean or not, adding this class is helpful for setting type of boolean variables.
 *
 * @author Linghui Luo
 */
public class BooleanConstant
    implements LogicalConstant<BooleanConstant>, ComparableConstant<BooleanConstant> {

  private static final BooleanConstant FALSE = new BooleanConstant(0);
  private static final BooleanConstant TRUE = new BooleanConstant(1);

  private final int value;

  private BooleanConstant(int value) {
    this.value = value;
  }

  public static BooleanConstant getInstance(boolean value) {
    return value ? TRUE : FALSE;
  }

  public static BooleanConstant getTrue() {
    return TRUE;
  }

  public static BooleanConstant getFalse() {
    return FALSE;
  }

  public static BooleanConstant getInstance(int value) {
    if (value != 1 && value != 0) {
      throw new RuntimeException("The value of boolean constant can only be 1 or 0");
    }
    return value == 1 ? TRUE : FALSE;
  }

  @Override
  public Type getType() {
    return PrimitiveType.getBoolean();
  }

  @Override
  public void accept(Visitor sw) {
    ((ConstantVisitor) sw).caseBooleanConstant(this);
  }

  @Nonnull
  @Override
  public BooleanConstant equalEqual(@Nonnull BooleanConstant c) {
    return BooleanConstant.getInstance(value == c.value);
  }

  @Nonnull
  @Override
  public BooleanConstant notEqual(@Nonnull BooleanConstant c) {
    return BooleanConstant.getInstance(value != c.value);
  }

  @Nonnull
  public BooleanConstant and(@Nonnull BooleanConstant c) {
    return BooleanConstant.getInstance(value & c.value);
  }

  @Nonnull
  @Override
  public BooleanConstant or(@Nonnull BooleanConstant c) {
    return BooleanConstant.getInstance(value | c.value);
  }

  @Nonnull
  @Override
  public BooleanConstant xor(@Nonnull BooleanConstant c) {
    return BooleanConstant.getInstance(value ^ c.value);
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }
}
