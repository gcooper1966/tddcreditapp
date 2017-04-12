package au.com.westpac.testing.utils;

import au.com.westpac.testing.assertions.CustomAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.apache.commons.io.FileUtils.getFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by M041451 on 12/04/2017.
 */
public class AutowireCheckReporterTest {

    private AutowireCheckReporter reporter;
    private ReportableItem item;

    @Before
    public void setup(){
        item = mock(ReportableItem.class);
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void can_add_items_to_report() throws AutowireCheckException {
        reporter = new AutowireCheckReporter();
        reporter.add(item);
        assertThat(reporter.size()).isEqualTo(1);
    }

    @Test
    public void can_output_report_to_specified_location() throws AutowireCheckException, IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        String testClassName = "au.com.westpac.testing.testclassname";
        String xPathExpressionForAttribute = "//*[@" + AutowireCheckReporter.FIRST_CHILD_ATTRIBUTE + "=\"" + testClassName + "\"]";
        when(item.getName()).thenReturn(testClassName);
        folder.create();
        reporter = new AutowireCheckReporter();
        reporter.add(item);
        reporter.setOutputLocation(folder.getRoot());
        reporter.report();
        assertThat(checkForFile(folder.getRoot(), AutowireCheckReporter.DEFAULT_OUTPUT_FILENAME)).isTrue();
        CustomAssertions.assertThat(getFile(Paths.get(folder.getRoot().toString(), AutowireCheckReporter.DEFAULT_OUTPUT_FILENAME).toFile())).containsChildNodesCalled("dependentClass");
        CustomAssertions.assertThat(getFile(Paths.get(folder.getRoot().toString(), AutowireCheckReporter.DEFAULT_OUTPUT_FILENAME).toFile()))
                .containsNodeWithAttribute(AutowireCheckReporter.FIRST_CHILD_ATTRIBUTE)
                .withAttributeSetTo(xPathExpressionForAttribute);
    }

    private boolean checkForFile(File root, String filename) {
        File f = Paths.get(root.toString(), filename).toFile();
        if(f.exists()){
            return true;
        }
        return false;
    }
}
