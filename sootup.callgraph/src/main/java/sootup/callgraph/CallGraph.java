package sootup.callgraph;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 2019-2020 Linghui Luo, Christian Brüggemann, Ben Hermann, Markus Schmidt
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

import java.util.Set;
import javax.annotation.Nonnull;
import sootup.core.signatures.MethodSignature;

public interface CallGraph {

  @Nonnull
  Set<MethodSignature> getMethodSignatures();

  @Nonnull
  Set<MethodSignature> callsFrom(@Nonnull MethodSignature sourceMethod);

  @Nonnull
  Set<MethodSignature> callsTo(@Nonnull MethodSignature targetMethod);

  boolean containsMethod(@Nonnull MethodSignature method);

  boolean containsCall(
      @Nonnull MethodSignature sourceMethod, @Nonnull MethodSignature targetMethod);

  int callCount();

  @Nonnull
  MutableCallGraph copy();

  String toStringSorted();
}