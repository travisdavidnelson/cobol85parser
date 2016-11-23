/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.cobol.parser.metamodel.procedure;

import java.util.List;

import io.proleap.cobol.Cobol85Parser.AcceptStatementContext;
import io.proleap.cobol.Cobol85Parser.AddStatementContext;
import io.proleap.cobol.Cobol85Parser.AlterStatementContext;
import io.proleap.cobol.Cobol85Parser.CallStatementContext;
import io.proleap.cobol.Cobol85Parser.CancelStatementContext;
import io.proleap.cobol.Cobol85Parser.CloseStatementContext;
import io.proleap.cobol.Cobol85Parser.DisplayStatementContext;
import io.proleap.cobol.Cobol85Parser.MoveToStatementContext;
import io.proleap.cobol.Cobol85Parser.ParagraphContext;
import io.proleap.cobol.Cobol85Parser.ParagraphNameContext;
import io.proleap.cobol.Cobol85Parser.PerformStatementContext;
import io.proleap.cobol.Cobol85Parser.StopStatementContext;
import io.proleap.cobol.parser.metamodel.CobolDivision;
import io.proleap.cobol.parser.metamodel.procedure.accept.AcceptStatement;
import io.proleap.cobol.parser.metamodel.procedure.add.AddStatement;
import io.proleap.cobol.parser.metamodel.procedure.alter.AlterStatement;
import io.proleap.cobol.parser.metamodel.procedure.call.CallStatement;
import io.proleap.cobol.parser.metamodel.procedure.cancel.CancelStatement;
import io.proleap.cobol.parser.metamodel.procedure.close.CloseStatement;
import io.proleap.cobol.parser.metamodel.procedure.display.DisplayStatement;
import io.proleap.cobol.parser.metamodel.procedure.move.MoveToStatement;
import io.proleap.cobol.parser.metamodel.procedure.perform.PerformStatement;
import io.proleap.cobol.parser.metamodel.procedure.stop.StopStatement;

/**
 * Contains procedures to manipulate the data from data division.
 */
public interface ProcedureDivision extends CobolDivision {

	AcceptStatement addAcceptStatement(AcceptStatementContext ctx);

	AddStatement addAddStatement(AddStatementContext ctx);

	AlterStatement addAlterStatement(AlterStatementContext ctx);

	CallStatement addCallStatement(CallStatementContext ctx);

	CancelStatement addCancelStatement(CancelStatementContext ctx);

	CloseStatement addCloseStatement(CloseStatementContext ctx);

	DisplayStatement addDisplayStatement(DisplayStatementContext ctx);

	MoveToStatement addMoveToStatement(MoveToStatementContext ctx);

	Paragraph addParagraph(ParagraphContext ctx);

	ParagraphName addParagraphName(ParagraphNameContext ctx);

	PerformStatement addPerformStatement(PerformStatementContext ctx);

	StopStatement addStopStatement(StopStatementContext ctx);

	Paragraph getParagraph(String name);

	List<Paragraph> getParagraphs();

	List<Statement> getStatements();

}
