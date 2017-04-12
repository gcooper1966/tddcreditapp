package au.com.westpac.testing.utils;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by M041451 on 3/04/2017.
 */
public class AutowireCheckConfigurationTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder(new File(this.getClass().getClassLoader().getResource("").getFile()));

    @Test
    public void can_locate_properties_config_file() throws IOException {
        //arrange
        String filename = "autowirecheck.properties";
        File file = prepareConfigFile(folder, filename);
        IOUtils.write(createProperties(), new FileOutputStream(file), Charset.defaultCharset());
        AutowireCheckConfiguration config = new AutowireCheckConfiguration();

        //act
        ReflectionTestUtils.invokeMethod(config, "locateConfiguration");

        //assert
        final Map<String, Object> properties = (Map<String, Object>)ReflectionTestUtils.getField(config, "properties");
        assertThat(properties.size()).isEqualTo(2);
    }

    @Test
    public void can_locate_xml_config_file() throws IOException, TransformerException {
        String filename = "autowirecheck.xml";
        File file = prepareConfigFile(folder, filename);
        IOUtils.write(createXmlProperties(), new FileOutputStream(file), Charset.defaultCharset());
        AutowireCheckConfiguration config = new AutowireCheckConfiguration();

        //act
        ReflectionTestUtils.invokeMethod(config,
                "locateConfiguration");

        //assert
        final Map<String, Object> properties =
                (Map<String, Object>)ReflectionTestUtils.getField(config, "properties");
        assertThat(properties.size()).isEqualTo(2);
    }

    private CharSequence createXmlProperties() throws TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("configuration");
        doc.appendChild(root);
        Element report = doc.createElement("report");
        root.appendChild(report);
        Element generate = doc.createElement("generate");
        generate.appendChild(doc.createTextNode("true"));
        report.appendChild(generate);
        Element outputDir = doc.createElement("outputDir");
        outputDir.appendChild(doc.createTextNode(folder.getRoot().getAbsolutePath()));
        report.appendChild(outputDir);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        DOMSource source = new DOMSource(doc);
        transformer = factory.newTransformer();
        transformer.transform(source,result);

        return writer.toString();
    }

    private CharSequence createProperties() {
        StringBuilder sb = new StringBuilder();
        sb.append(AutowireCheckConfiguration.GENERATE_REPORT).append("=").append(true).append("\n");
        sb.append(AutowireCheckConfiguration.OUTPUT_DIR).append("=").append(folder.getRoot().getAbsolutePath());
        return sb.toString();
    }

    private File prepareConfigFile(TemporaryFolder folder, String filename) throws IOException {
        final File file = folder.newFile(filename);
        return file;
    }
}
