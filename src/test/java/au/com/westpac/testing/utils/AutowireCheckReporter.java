package au.com.westpac.testing.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by M041451 on 12/04/2017.
 */
public class AutowireCheckReporter extends ArrayList<ReportableItem> {

    private File outputLocation;
    private Document document;
    private static Logger log = LogManager.getLogger();

    public static String ROOT = "missedDependencies";
    public static String FIRST_CHILD = "dependentClass";
    public static String FIRST_CHILD_ATTRIBUTE = "className";
    public static String DEFAULT_OUTPUT_FILENAME = "missingdependencies.xml";

    public AutowireCheckReporter() throws AutowireCheckException {
        super();
        document = createDocument();
        outputLocation = Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceFirst("^/(.:/)", "$1"), DEFAULT_OUTPUT_FILENAME).toFile();
    }

    public AutowireCheckReporter(int initialCapacity) throws AutowireCheckException {
        super(initialCapacity);
        document = createDocument();
    }

    public AutowireCheckReporter(Collection<? extends ReportableItem> c) throws AutowireCheckException {
        super(c);
        document = createDocument();
    }


    public void setOutputLocation(File outputLocation) {
        if(outputLocation.isDirectory()){
            outputLocation = Paths.get(outputLocation.getAbsolutePath(), DEFAULT_OUTPUT_FILENAME).toFile();
        }
        this.outputLocation = outputLocation;
    }

    public void report() throws AutowireCheckException {
        Node root = createNode(null,ROOT, document);
        for (ReportableItem item :
                this) {
            createNode(item, FIRST_CHILD, root);
        }
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer  = null;
        try {
            transformer = factory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileWriter(outputLocation));
            transformer.transform(source,result);
        } catch (TransformerException e) {
            log.error("Unable to transform document tree to xml", e);
            throw new AutowireCheckException("Unable to transform document tree to xml");
        } catch (IOException e) {
            log.error("Unable to write xml report file. Check permissions to output location", e);
            throw new AutowireCheckException("Unable to write xml report file. Check permissions to output location");
        }
    }

    private Document createDocument() throws AutowireCheckException {
        Document doc;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        }catch (ParserConfigurationException e) {
            log.error("Unable to create document", e);
            throw new AutowireCheckException("Unable to create document");
        }
        return doc;
    }

    private Node createNode(ReportableItem item, String elementName, Node parentNode) {
        Node child = document.createElement(elementName);
        if(item != null){
            Attr attr = document.createAttribute(FIRST_CHILD_ATTRIBUTE);
            attr.setValue(item.getName());
            child.getAttributes().setNamedItem(attr);
        }
        parentNode.appendChild(child);
        return child;
    }
}
