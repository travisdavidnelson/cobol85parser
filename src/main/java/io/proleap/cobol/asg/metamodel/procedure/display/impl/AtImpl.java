/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.cobol.asg.metamodel.procedure.display.impl;

import io.proleap.cobol.Cobol85Parser.DisplayAtContext;
import io.proleap.cobol.asg.metamodel.ProgramUnit;
import io.proleap.cobol.asg.metamodel.impl.CobolDivisionElementImpl;
import io.proleap.cobol.asg.metamodel.procedure.display.At;
import io.proleap.cobol.asg.metamodel.valuestmt.ValueStmt;

public class AtImpl extends CobolDivisionElementImpl implements At {

	protected ValueStmt atValueStmt;

	protected DisplayAtContext ctx;

	public AtImpl(final ProgramUnit programUnit, final DisplayAtContext ctx) {
		super(programUnit, ctx);

		this.ctx = ctx;
	}

	@Override
	public ValueStmt getAtValueStmt() {
		return atValueStmt;
	}

	@Override
	public void setAtValueStmt(final ValueStmt atValueStmt) {
		this.atValueStmt = atValueStmt;
	}

}
