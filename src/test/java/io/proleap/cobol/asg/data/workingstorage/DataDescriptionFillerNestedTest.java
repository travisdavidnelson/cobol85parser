package io.proleap.cobol.asg.data.workingstorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import io.proleap.cobol.CobolTestBase;
import io.proleap.cobol.asg.metamodel.CompilationUnit;
import io.proleap.cobol.asg.metamodel.Program;
import io.proleap.cobol.asg.metamodel.ProgramUnit;
import io.proleap.cobol.asg.metamodel.data.DataDivision;
import io.proleap.cobol.asg.metamodel.data.datadescription.DataDescriptionEntry;
import io.proleap.cobol.asg.metamodel.data.datadescription.DataDescriptionEntryGroup;
import io.proleap.cobol.asg.metamodel.data.workingstorage.WorkingStorageSection;
import io.proleap.cobol.asg.runner.impl.CobolParserRunnerImpl;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class DataDescriptionFillerNestedTest extends CobolTestBase {

	@Test
	public void test() throws Exception {
		final File inputFile = new File(
				"src/test/resources/io/proleap/cobol/asg/data/workingstorage/DataDescriptionFillerNested.cbl");
		final Program program = new CobolParserRunnerImpl().analyzeFile(inputFile, CobolSourceFormatEnum.TANDEM);

		final CompilationUnit compilationUnit = program.getCompilationUnit("DataDescriptionFillerNested");
		final ProgramUnit programUnit = compilationUnit.getProgramUnit();
		final DataDivision dataDivision = programUnit.getDataDivision();
		final WorkingStorageSection workingStorageSection = dataDivision.getWorkingStorageSection();

		assertEquals(3, workingStorageSection.getDataDescriptionEntries().size());
		assertEquals(1, workingStorageSection.getRootDataDescriptionEntries().size());

		{
			final DataDescriptionEntry dataDescriptionEntry = workingStorageSection.getDataDescriptionEntries().get(0);
			assertNotNull(dataDescriptionEntry);
			assertNull(dataDescriptionEntry.getName());
			assertEquals(DataDescriptionEntry.DataDescriptionEntryType.GROUP,
					dataDescriptionEntry.getDataDescriptionEntryType());
			assertEquals(new Integer(1), dataDescriptionEntry.getLevelNumber());

			{
				final DataDescriptionEntryGroup dataDescriptionEntryGroup = (DataDescriptionEntryGroup) dataDescriptionEntry;
				assertTrue(dataDescriptionEntryGroup.getFiller());
				assertEquals(new Integer(0), dataDescriptionEntryGroup.getFillerNumber());

				{
					final DataDescriptionEntry dataDescriptionEntryFiller = dataDescriptionEntryGroup
							.getDataDescriptionEntries().get(0);
					assertNotNull(dataDescriptionEntryFiller);
					assertNull(dataDescriptionEntryFiller.getName());
					assertEquals(DataDescriptionEntry.DataDescriptionEntryType.GROUP,
							dataDescriptionEntryFiller.getDataDescriptionEntryType());
					assertEquals(new Integer(5), dataDescriptionEntryFiller.getLevelNumber());

					{
						final DataDescriptionEntryGroup dataDescriptionEntryFillerGroup = (DataDescriptionEntryGroup) dataDescriptionEntryFiller;
						assertTrue(dataDescriptionEntryFillerGroup.getFiller());
						assertEquals(new Integer(1), dataDescriptionEntryFillerGroup.getFillerNumber());
					}
				}

				{
					final DataDescriptionEntry dataDescriptionEntrySomeId = dataDescriptionEntryGroup
							.getDataDescriptionEntries().get(1);
					assertNotNull(dataDescriptionEntrySomeId);
					assertEquals("SOMEID", dataDescriptionEntrySomeId.getName());
					assertEquals(DataDescriptionEntry.DataDescriptionEntryType.GROUP,
							dataDescriptionEntrySomeId.getDataDescriptionEntryType());
					assertEquals(new Integer(5), dataDescriptionEntrySomeId.getLevelNumber());

					{
						final DataDescriptionEntryGroup dataDescriptionEntryFillerGroup = (DataDescriptionEntryGroup) dataDescriptionEntrySomeId;
						assertNull(dataDescriptionEntryFillerGroup.getFiller());
					}
				}
			}
		}
	}
}
