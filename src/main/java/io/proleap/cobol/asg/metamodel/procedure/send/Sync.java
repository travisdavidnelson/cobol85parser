/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.cobol.asg.metamodel.procedure.send;

import io.proleap.cobol.Cobol85Parser.SendAdvancingPhraseContext;
import io.proleap.cobol.Cobol85Parser.SendFromPhraseContext;
import io.proleap.cobol.Cobol85Parser.SendWithPhraseContext;
import io.proleap.cobol.asg.metamodel.CobolDivisionElement;
import io.proleap.cobol.asg.metamodel.valuestmt.ValueStmt;

public interface Sync extends CobolDivisionElement {

	Advancing addAdvancing(SendAdvancingPhraseContext ctx);

	From addFrom(SendFromPhraseContext ctx);

	With addWith(SendWithPhraseContext ctx);

	Advancing getAdvancing();

	From getFrom();

	ValueStmt getReceivingProgramValueStmt();

	With getWith();

	boolean isReplacing();

	void setReceivingProgramValueStmt(ValueStmt receivingProgramValueStmt);

	void setReplacing(boolean replacing);

}
