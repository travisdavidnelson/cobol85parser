/*
 * Copyright (C) 2017, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.cobol.asg.metamodel.procedure.perform;

import io.proleap.cobol.Cobol85Parser.PerformTypeContext;
import io.proleap.cobol.asg.metamodel.Scope;

public interface PerformInlineStatement extends Scope {

	PerformType addPerformType(PerformTypeContext ctx);

	PerformType getPerformType();

}
