/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.cobol.asg.metamodel.valuestmt.condition;

import io.proleap.cobol.Cobol85Parser.SimpleConditionContext;
import io.proleap.cobol.asg.metamodel.valuestmt.ValueStmt;

public interface CombinableCondition extends ValueStmt {

	SimpleCondition addSimpleCondition(SimpleConditionContext ctx);

	SimpleCondition getSimpleCondition();

	boolean isNot();

	void setNot(boolean not);

}
