/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.cobol.asg.metamodel.data.datadescription.impl;

import io.proleap.cobol.Cobol85Parser.DataSignClauseContext;
import io.proleap.cobol.asg.metamodel.ProgramUnit;
import io.proleap.cobol.asg.metamodel.data.datadescription.SignClause;
import io.proleap.cobol.asg.metamodel.impl.CobolDivisionElementImpl;

public class SignClauseImpl extends CobolDivisionElementImpl implements SignClause {

	protected DataSignClauseContext ctx;

	protected boolean separate;

	protected SignClauseType signClauseType;

	public SignClauseImpl(final ProgramUnit programUnit, final DataSignClauseContext ctx) {
		super(programUnit, ctx);

		this.ctx = ctx;
	}

	@Override
	public SignClauseType getSignClauseType() {
		return signClauseType;
	}

	@Override
	public boolean isSeparate() {
		return separate;
	}

	@Override
	public void setSeparate(final boolean separate) {
		this.separate = separate;
	}

	@Override
	public void setSignClauseType(final SignClauseType signClauseType) {
		this.signClauseType = signClauseType;
	}

}
