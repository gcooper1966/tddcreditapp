package au.com.westpac.testing.assertions;

import org.assertj.core.api.AbstractAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by M041451 on 12/04/2017.
 */
public class XmlFileAssert extends AbstractAssert<XmlFileAssert, File> {
    public XmlFileAssert(File actual) {
        super(actual, XmlFileAssert.class);
    }

    public static XmlFileAssert assertThat(File actual){
        return new XmlFileAssert(actual);
    }

    public XmlFileAssert containsChildNodesCalled(String nodeName) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        isNotNull();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(actual);
        XPath  xPath = XPathFactory.newInstance().newXPath();
        NodeList  nodes = (NodeList)xPath.compile("//" + nodeName).evaluate(document, XPathConstants.NODESET);
        if(nodes.getLength() == 0){
            failWithMessage("Expected the XML file to contain at least 1 child node called <%s> but none were found", nodeName);
        }
        return this;
    }

    public XmlFileAssert containsNodeWithAttribute(String firstChildAttribute) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        isNotNull();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(actual);
        XPath  xPath = XPathFactory.newInstance().newXPath();
        NodeList  nodes = (NodeList)xPath.compile("//*[@" + firstChildAttribute + "]").evaluate(document, XPathConstants.NODESET);
        if(nodes.getLength() == 0){
            failWithMessage("Expected the XML file to contain at least 1 child node with an attribute called <%s> but none were found", firstChildAttribute);
        }
        return this;
    }

    public XmlFileAssert withAttributeSetTo(String expression) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        isNotNull();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(actual);
        XPath  xPath = XPathFactory.newInstance().newXPath();
        NodeList  nodes = (NodeList)xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        if(nodes.getLength() == 0){
            failWithMessage("Expected the XML file to contain at least 1 child node matching the xpath expression but none were found");
        }
        return null;
    }
}
