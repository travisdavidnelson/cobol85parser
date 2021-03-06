/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.cobol.asg.metamodel.procedure.declaratives.impl;

import io.proleap.cobol.Cobol85Parser.ProcedureSectionHeaderContext;
import io.proleap.cobol.asg.metamodel.ProgramUnit;
import io.proleap.cobol.asg.metamodel.impl.CobolDivisionElementImpl;
import io.proleap.cobol.asg.metamodel.procedure.declaratives.SectionHeader;

public class SectionHeaderImpl extends CobolDivisionElementImpl implements SectionHeader {

	protected final ProcedureSectionHeaderContext ctx;

	public SectionHeaderImpl(final ProgramUnit programUnit, final ProcedureSectionHeaderContext ctx) {
		super(programUnit, ctx);

		this.ctx = ctx;
	}

}
