/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.cobol.asg.metamodel.impl;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.proleap.cobol.Cobol85Parser.AlphabetNameContext;
import io.proleap.cobol.Cobol85Parser.ArithmeticExpressionContext;
import io.proleap.cobol.Cobol85Parser.AssignmentNameContext;
import io.proleap.cobol.Cobol85Parser.BooleanLiteralContext;
import io.proleap.cobol.Cobol85Parser.CdNameContext;
import io.proleap.cobol.Cobol85Parser.ClassNameContext;
import io.proleap.cobol.Cobol85Parser.CobolWordContext;
import io.proleap.cobol.Cobol85Parser.ConditionContext;
import io.proleap.cobol.Cobol85Parser.DataDescNameContext;
import io.proleap.cobol.Cobol85Parser.DataNameContext;
import io.proleap.cobol.Cobol85Parser.EnvironmentNameContext;
import io.proleap.cobol.Cobol85Parser.FileNameContext;
import io.proleap.cobol.Cobol85Parser.IdentifierContext;
import io.proleap.cobol.Cobol85Parser.IndexNameContext;
import io.proleap.cobol.Cobol85Parser.IntegerLiteralContext;
import io.proleap.cobol.Cobol85Parser.LibraryNameContext;
import io.proleap.cobol.Cobol85Parser.LiteralContext;
import io.proleap.cobol.Cobol85Parser.LocalNameContext;
import io.proleap.cobol.Cobol85Parser.MnemonicNameContext;
import io.proleap.cobol.Cobol85Parser.NumericLiteralContext;
import io.proleap.cobol.Cobol85Parser.ProcedureNameContext;
import io.proleap.cobol.Cobol85Parser.ProgramNameContext;
import io.proleap.cobol.Cobol85Parser.QualifiedDataNameContext;
import io.proleap.cobol.Cobol85Parser.RecordNameContext;
import io.proleap.cobol.Cobol85Parser.ReportNameContext;
import io.proleap.cobol.Cobol85Parser.SystemNameContext;
import io.proleap.cobol.asg.metamodel.BooleanLiteral;
import io.proleap.cobol.asg.metamodel.IntegerLiteral;
import io.proleap.cobol.asg.metamodel.Literal;
import io.proleap.cobol.asg.metamodel.MnemonicName;
import io.proleap.cobol.asg.metamodel.ProgramUnit;
import io.proleap.cobol.asg.metamodel.ProgramUnitElement;
import io.proleap.cobol.asg.metamodel.call.Call;
import io.proleap.cobol.asg.metamodel.call.CommunicationDescriptionEntryCall;
import io.proleap.cobol.asg.metamodel.call.DataDescriptionEntryCall;
import io.proleap.cobol.asg.metamodel.call.ProcedureCall;
import io.proleap.cobol.asg.metamodel.call.ReportDescriptionEntryCall;
import io.proleap.cobol.asg.metamodel.call.impl.CommunicationDescriptionEntryCallImpl;
import io.proleap.cobol.asg.metamodel.call.impl.DataDescriptionEntryCallImpl;
import io.proleap.cobol.asg.metamodel.call.impl.ProcedureCallImpl;
import io.proleap.cobol.asg.metamodel.call.impl.ReportDescriptionEntryCallImpl;
import io.proleap.cobol.asg.metamodel.call.impl.UndefinedCallImpl;
import io.proleap.cobol.asg.metamodel.data.DataDivision;
import io.proleap.cobol.asg.metamodel.data.communication.CommunicationDescriptionEntry;
import io.proleap.cobol.asg.metamodel.data.communication.CommunicationSection;
import io.proleap.cobol.asg.metamodel.data.datadescription.DataDescriptionEntry;
import io.proleap.cobol.asg.metamodel.data.report.Report;
import io.proleap.cobol.asg.metamodel.data.report.ReportDescriptionEntry;
import io.proleap.cobol.asg.metamodel.data.report.ReportSection;
import io.proleap.cobol.asg.metamodel.data.workingstorage.WorkingStorageSection;
import io.proleap.cobol.asg.metamodel.procedure.Paragraph;
import io.proleap.cobol.asg.metamodel.procedure.ProcedureDivision;
import io.proleap.cobol.asg.metamodel.valuestmt.ArithmeticValueStmt;
import io.proleap.cobol.asg.metamodel.valuestmt.BooleanLiteralValueStmt;
import io.proleap.cobol.asg.metamodel.valuestmt.CallValueStmt;
import io.proleap.cobol.asg.metamodel.valuestmt.ConditionValueStmt;
import io.proleap.cobol.asg.metamodel.valuestmt.IntegerLiteralValueStmt;
import io.proleap.cobol.asg.metamodel.valuestmt.LiteralValueStmt;
import io.proleap.cobol.asg.metamodel.valuestmt.TerminalValueStmt;
import io.proleap.cobol.asg.metamodel.valuestmt.ValueStmt;
import io.proleap.cobol.asg.metamodel.valuestmt.impl.ArithmeticValueStmtImpl;
import io.proleap.cobol.asg.metamodel.valuestmt.impl.BooleanLiteralValueStmtImpl;
import io.proleap.cobol.asg.metamodel.valuestmt.impl.CallValueStmtImpl;
import io.proleap.cobol.asg.metamodel.valuestmt.impl.ConditionValueStmtImpl;
import io.proleap.cobol.asg.metamodel.valuestmt.impl.IntegerLiteralValueStmtImpl;
import io.proleap.cobol.asg.metamodel.valuestmt.impl.LiteralValueStmtImpl;
import io.proleap.cobol.asg.metamodel.valuestmt.impl.TerminalValueStmtImpl;
import io.proleap.cobol.asg.util.StringUtils;

public class ProgramUnitElementImpl extends CompilationUnitElementImpl implements ProgramUnitElement {

	private final static Logger LOG = LogManager.getLogger(ProgramUnitElementImpl.class);

	protected ProgramUnit programUnit;

	public ProgramUnitElementImpl(final ProgramUnit programUnit, final ParseTree ctx) {
		super(ctx);

		this.programUnit = programUnit;
	}

	protected ArithmeticValueStmt createArithmeticValueStmt(final ArithmeticExpressionContext ctx) {
		final ArithmeticValueStmt result = new ArithmeticValueStmtImpl(programUnit, ctx);
		return result;
	}

	protected BooleanLiteral createBooleanLiteral(final BooleanLiteralContext ctx) {
		BooleanLiteral result = (BooleanLiteral) getASGElement(ctx);

		if (result == null) {
			final Boolean value = StringUtils.parseBoolean(ctx.getText());
			result = new BooleanLiteralImpl(value, programUnit, ctx);

			registerASGElement(result);
		}

		return result;
	}

	protected BooleanLiteralValueStmt createBooleanLiteralValueStmt(final BooleanLiteralContext ctx) {
		final BooleanLiteral booleanLiteral = createBooleanLiteral(ctx);
		final BooleanLiteralValueStmt result = new BooleanLiteralValueStmtImpl(programUnit, ctx);
		result.setBooleanLiteral(booleanLiteral);
		return result;
	}

	protected Call createCall(final AlphabetNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final AssignmentNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final CdNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final CommunicationDescriptionEntry communicationDescriptionEntry = findCommunicationDescriptionEntry(name);

			if (communicationDescriptionEntry == null) {
				LOG.warn("call to unknown element {}", name);
				result = createUndefinedCall(ctx);
			} else {
				result = createCommunicationDescriptionEntryCall(name, communicationDescriptionEntry, ctx);
			}

			registerASGElement(result);
		}

		return result;
	}

	protected Call createCall(final ClassNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final CobolWordContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final DataDescNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final DataNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final DataDescriptionEntry dataDescriptionEntry = findDataDescriptionEntry(name);

			if (dataDescriptionEntry == null) {
				LOG.warn("call to unknown element {}", name);
				result = createUndefinedCall(ctx);
			} else {
				result = createDataDescriptionEntryCall(name, dataDescriptionEntry, ctx);
			}

			registerASGElement(result);
		}

		return result;
	}

	protected Call createCall(final EnvironmentNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final FileNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final IdentifierContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final DataDescriptionEntry dataDescriptionEntry = findDataDescriptionEntry(name);

			if (dataDescriptionEntry == null) {
				LOG.warn("call to unknown element {}", name);
				result = createUndefinedCall(ctx);
			} else {
				result = createDataDescriptionEntryCall(name, dataDescriptionEntry, ctx);
			}

			registerASGElement(result);
		}

		return result;
	}

	protected Call createCall(final IndexNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final IntegerLiteralContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final LibraryNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final LiteralContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final LocalNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final MnemonicNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final NumericLiteralContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final ParseTree... ctxs) {
		Call result = null;

		for (final ParseTree ctx : ctxs) {
			if (result != null) {
				break;
			}

			if (ctx == null) {
				continue;
			}

			if (ctx instanceof IdentifierContext) {
				result = createCall((IdentifierContext) ctx);
			} else if (ctx instanceof LiteralContext) {
				result = createCall((LiteralContext) ctx);
			} else if (ctx instanceof IntegerLiteralContext) {
				result = createCall((IntegerLiteralContext) ctx);
			} else if (ctx instanceof MnemonicNameContext) {
				result = createCall((MnemonicNameContext) ctx);
			} else if (ctx instanceof ProcedureNameContext) {
				result = createCall((ProcedureNameContext) ctx);
			} else if (ctx instanceof CdNameContext) {
				result = createCall((CdNameContext) ctx);
			} else if (ctx instanceof AlphabetNameContext) {
				result = createCall((AlphabetNameContext) ctx);
			} else if (ctx instanceof AssignmentNameContext) {
				result = createCall((AssignmentNameContext) ctx);
			} else if (ctx instanceof ClassNameContext) {
				result = createCall((ClassNameContext) ctx);
			} else if (ctx instanceof CobolWordContext) {
				result = createCall((CobolWordContext) ctx);
			} else if (ctx instanceof DataDescNameContext) {
				result = createCall((DataDescNameContext) ctx);
			} else if (ctx instanceof DataNameContext) {
				result = createCall((DataNameContext) ctx);
			} else if (ctx instanceof EnvironmentNameContext) {
				result = createCall((EnvironmentNameContext) ctx);
			} else if (ctx instanceof FileNameContext) {
				result = createCall((FileNameContext) ctx);
			} else if (ctx instanceof LibraryNameContext) {
				result = createCall((LibraryNameContext) ctx);
			} else if (ctx instanceof LocalNameContext) {
				result = createCall((LocalNameContext) ctx);
			} else if (ctx instanceof MnemonicNameContext) {
				result = createCall((MnemonicNameContext) ctx);
			} else if (ctx instanceof NumericLiteralContext) {
				result = createCall((NumericLiteralContext) ctx);
			} else if (ctx instanceof ProgramNameContext) {
				result = createCall((ProgramNameContext) ctx);
			} else if (ctx instanceof QualifiedDataNameContext) {
				result = createCall((QualifiedDataNameContext) ctx);
			} else if (ctx instanceof RecordNameContext) {
				result = createCall((RecordNameContext) ctx);
			} else if (ctx instanceof ReportNameContext) {
				result = createCall((ReportNameContext) ctx);
			} else if (ctx instanceof SystemNameContext) {
				result = createCall((SystemNameContext) ctx);
			} else {
				LOG.warn("unknown call at {}", ctx);
			}
		}

		return result;
	}

	protected Call createCall(final ProcedureNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final Paragraph paragraph = findProcedure(name);

			if (paragraph == null) {
				result = createUndefinedCall(ctx);
			} else {
				result = createProcedureCall(name, paragraph, ctx);
			}

			registerASGElement(result);
		}

		return result;
	}

	protected Call createCall(final ProgramNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final QualifiedDataNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final DataDescriptionEntry dataDescriptionEntry = findDataDescriptionEntry(name);

			if (dataDescriptionEntry == null) {
				LOG.warn("call to unknown element {}", name);
				result = createUndefinedCall(ctx);
			} else {
				result = createDataDescriptionEntryCall(name, dataDescriptionEntry, ctx);
			}

			registerASGElement(result);
		}

		return result;
	}

	protected Call createCall(final RecordNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected Call createCall(final ReportNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final ReportDescriptionEntry reportDescriptionEntry = findReportDescriptionEntry(name);

			if (reportDescriptionEntry == null) {
				result = createUndefinedCall(ctx);
			} else {
				result = createReportDescriptionEntryCall(name, reportDescriptionEntry, ctx);
			}

			registerASGElement(result);
		}

		return result;
	}

	protected Call createCall(final SystemNameContext ctx) {
		Call result = (Call) getASGElement(ctx);

		if (result == null) {
			result = createUndefinedCall(ctx);
		}

		return result;
	}

	protected CallValueStmt createCallValueStmt(final AlphabetNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final AssignmentNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final ClassNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final CobolWordContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final DataDescNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final DataNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final FileNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final IdentifierContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final IndexNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final LocalNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final ProgramNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final QualifiedDataNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final ReportNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CallValueStmt createCallValueStmt(final SystemNameContext ctx) {
		final Call delegatedCall = createCall(ctx);
		final CallValueStmt result = new CallValueStmtImpl(delegatedCall, programUnit, ctx);
		return result;
	}

	protected CommunicationDescriptionEntryCall createCommunicationDescriptionEntryCall(final String name,
			final CommunicationDescriptionEntry communicationDescriptionEntry, final CdNameContext ctx) {
		final CommunicationDescriptionEntryCall result = new CommunicationDescriptionEntryCallImpl(name,
				communicationDescriptionEntry, programUnit, ctx);
		linkCommunicationDescriptionEntryCallWithCommunicationDescriptionEntry(result, communicationDescriptionEntry);
		return result;
	}

	protected ConditionValueStmt createConditionValueStmt(final ConditionContext ctx) {
		final ConditionValueStmt result = new ConditionValueStmtImpl(programUnit, ctx);
		return result;
	}

	protected DataDescriptionEntryCall createDataDescriptionEntryCall(final String name,
			final DataDescriptionEntry dataDescriptionEntry, final ParseTree ctx) {
		final DataDescriptionEntryCall result = new DataDescriptionEntryCallImpl(name, dataDescriptionEntry,
				programUnit, ctx);
		linkDataDescriptionEntryCallWithDataDescriptionEntry(result, dataDescriptionEntry);
		return result;
	}

	protected IntegerLiteral createIntegerLiteral(final IntegerLiteralContext ctx) {
		IntegerLiteral result = (IntegerLiteral) getASGElement(ctx);

		if (result == null) {
			final Integer value = StringUtils.parseInteger(ctx.getText());
			result = new IntegerLiteralImpl(value, programUnit, ctx);

			registerASGElement(result);
		}

		return result;
	}

	protected IntegerLiteralValueStmt createIntegerLiteralValueStmt(final IntegerLiteralContext ctx) {
		final IntegerLiteral integerLiteral = createIntegerLiteral(ctx);
		final IntegerLiteralValueStmt result = new IntegerLiteralValueStmtImpl(programUnit, ctx);
		result.setIntegerLiteral(integerLiteral);
		return result;
	}

	protected Literal createLiteral(final LiteralContext ctx) {
		Literal result = (Literal) getASGElement(ctx);

		if (result == null) {
			final String value = ctx.getText();
			result = new LiteralImpl(value, programUnit, ctx);

			registerASGElement(result);
		}

		return result;
	}

	protected LiteralValueStmt createLiteralValueStmt(final LiteralContext ctx) {
		final Literal literal = createLiteral(ctx);
		final LiteralValueStmt result = new LiteralValueStmtImpl(programUnit, ctx);
		result.setLiteral(literal);
		return result;
	}

	protected MnemonicName createMnemonicName(final MnemonicNameContext ctx) {
		MnemonicName result = (MnemonicName) getASGElement(ctx);

		if (result == null) {
			final String value = ctx.getText();
			result = new MnemonicNameImpl(value, programUnit, ctx);

			registerASGElement(result);
		}

		return result;
	}

	protected ProcedureCall createProcedureCall(final String name, final Paragraph paragraph,
			final ProcedureNameContext ctx) {
		final ProcedureCall result = new ProcedureCallImpl(name, paragraph, programUnit, ctx);
		linkProcedureCallWithParagraph(result, paragraph);
		return result;
	}

	protected ReportDescriptionEntryCall createReportDescriptionEntryCall(final String name,
			final ReportDescriptionEntry reportDescriptionEntry, final ReportNameContext ctx) {
		final ReportDescriptionEntryCall call = new ReportDescriptionEntryCallImpl(name, reportDescriptionEntry,
				programUnit, ctx);
		linkReportDescriptionEntryCallWithReportDescriptionEntry(call, reportDescriptionEntry);
		return call;
	}

	protected TerminalValueStmt createTerminalValueStmt(final TerminalNode ctx) {
		final TerminalValueStmt result = new TerminalValueStmtImpl(programUnit, ctx);
		return result;
	}

	protected Call createUndefinedCall(final ParseTree ctx) {
		final String name = determineName(ctx);
		final Call result = new UndefinedCallImpl(name, programUnit, ctx);
		return result;
	}

	protected ValueStmt createValueStmt(final ParseTree... ctxs) {
		ValueStmt result = null;

		for (final ParseTree ctx : ctxs) {
			if (result != null) {
				break;
			}

			if (ctx == null) {
				continue;
			}

			if (ctx instanceof IdentifierContext) {
				result = createCallValueStmt((IdentifierContext) ctx);
			} else if (ctx instanceof AlphabetNameContext) {
				result = createCallValueStmt((AlphabetNameContext) ctx);
			} else if (ctx instanceof AssignmentNameContext) {
				result = createCallValueStmt((AssignmentNameContext) ctx);
			} else if (ctx instanceof ClassNameContext) {
				result = createCallValueStmt((ClassNameContext) ctx);
			} else if (ctx instanceof CobolWordContext) {
				result = createCallValueStmt((CobolWordContext) ctx);
			} else if (ctx instanceof DataDescNameContext) {
				result = createCallValueStmt((DataDescNameContext) ctx);
			} else if (ctx instanceof DataNameContext) {
				result = createCallValueStmt((DataNameContext) ctx);
			} else if (ctx instanceof FileNameContext) {
				result = createCallValueStmt((FileNameContext) ctx);
			} else if (ctx instanceof IndexNameContext) {
				result = createCallValueStmt((IndexNameContext) ctx);
			} else if (ctx instanceof LocalNameContext) {
				result = createCallValueStmt((LocalNameContext) ctx);
			} else if (ctx instanceof ProgramNameContext) {
				result = createCallValueStmt((ProgramNameContext) ctx);
			} else if (ctx instanceof QualifiedDataNameContext) {
				result = createCallValueStmt((QualifiedDataNameContext) ctx);
			} else if (ctx instanceof ReportNameContext) {
				result = createCallValueStmt((ReportNameContext) ctx);
			} else if (ctx instanceof SystemNameContext) {
				result = createCallValueStmt((SystemNameContext) ctx);
			} else if (ctx instanceof ConditionContext) {
				result = createConditionValueStmt((ConditionContext) ctx);
			} else if (ctx instanceof IntegerLiteralContext) {
				result = createIntegerLiteralValueStmt((IntegerLiteralContext) ctx);
			} else if (ctx instanceof LiteralContext) {
				result = createLiteralValueStmt((LiteralContext) ctx);
			} else if (ctx instanceof TerminalNode) {
				result = createTerminalValueStmt((TerminalNode) ctx);
			} else {
				LOG.warn("unknown value stmt at {}", ctx);
			}
		}

		return result;
	}

	protected CommunicationDescriptionEntry findCommunicationDescriptionEntry(final String name) {
		final DataDivision dataDivision = programUnit.getDataDivision();
		final CommunicationDescriptionEntry result;

		if (dataDivision == null) {
			result = null;
		} else {
			final CommunicationSection communicationSection = dataDivision.getCommunicationSection();

			if (communicationSection == null) {
				result = null;
			} else {
				result = communicationSection.getCommunicationDescriptionEntry(name);
			}
		}

		return result;
	}

	protected DataDescriptionEntry findDataDescriptionEntry(final String name) {
		final DataDivision dataDivision = programUnit.getDataDivision();
		final DataDescriptionEntry result;

		if (dataDivision == null) {
			result = null;
		} else {
			final WorkingStorageSection workingStorageSection = dataDivision.getWorkingStorageSection();

			if (workingStorageSection == null) {
				result = null;
			} else {
				result = workingStorageSection.findDataDescriptionEntry(name);
			}
		}

		return result;
	}

	protected Paragraph findProcedure(final String name) {
		final ProcedureDivision procedureDivision = programUnit.getProcedureDivision();
		final Paragraph result;

		if (procedureDivision == null) {
			result = null;
		} else {
			result = procedureDivision.getParagraph(name);
		}

		return result;
	}

	protected ReportDescriptionEntry findReportDescriptionEntry(final String name) {
		final DataDivision dataDivision = programUnit.getDataDivision();
		final ReportDescriptionEntry result;

		if (dataDivision == null) {
			result = null;
		} else {
			final ReportSection reportSection = dataDivision.getReportSection();

			if (reportSection == null) {
				result = null;
			} else {
				final Report report = reportSection.getReport(name);

				if (report == null) {
					result = null;
				} else {
					result = report.getReportDescriptionEntry();
				}
			}
		}

		return result;
	}

	@Override
	public ProgramUnit getProgramUnit() {
		return programUnit;
	}

	protected void linkCommunicationDescriptionEntryCallWithCommunicationDescriptionEntry(
			final CommunicationDescriptionEntryCall call,
			final CommunicationDescriptionEntry communicationDescriptionEntry) {
		communicationDescriptionEntry.addCall(call);
	}

	protected void linkDataDescriptionEntryCallWithDataDescriptionEntry(final DataDescriptionEntryCall call,
			final DataDescriptionEntry dataDescriptionEntry) {
		dataDescriptionEntry.addCall(call);
	}

	protected void linkProcedureCallWithParagraph(final ProcedureCall call, final Paragraph paragraph) {
		paragraph.addCall(call);
	}

	protected void linkReportDescriptionEntryCallWithReportDescriptionEntry(final ReportDescriptionEntryCall call,
			final ReportDescriptionEntry reportDescriptionEntry) {
		reportDescriptionEntry.addCall(call);
	}

}
