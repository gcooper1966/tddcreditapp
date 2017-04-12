package au.com.westpac.testing.utils;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by M041451 on 3/04/2017.
 */
public class AutowireCheckConfiguration {
    private static Logger log = LogManager.getLogger();

    private Map<String, String> properties = new HashMap<>();
    public static String GENERATE_REPORT = "au.com.westpac.autowirecheck.report";
    public static String OUTPUT_DIR = "au.com.westpac.autowirecheck.output";
    public static String OUTPUT_FILENAME = "missingdependencies.xml";
    public AutowireCheckConfiguration() {
        locateConfiguration();
    }

    public boolean generateReport(){
        if(properties.containsKey(GENERATE_REPORT)){
            return Boolean.parseBoolean(properties.get(GENERATE_REPORT));
        }
        return false;
    }

    public Path getOutputDir(){
        if(properties.containsKey(OUTPUT_DIR)){
            return FileSystems.getDefault().getPath(properties.get(OUTPUT_DIR));
        }
        return null;
    }


    private void locateConfiguration() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] propertyResources = null;
        Resource[] xmlResources = null;
        try {
            propertyResources = resolver.getResources("classpath*:**/autowirecheck.properties");
        } catch (IOException e) {
            log.warn("Unable to find autowirecheck.properties", e);
        }
        try{
            xmlResources = resolver.getResources("classpath*:**/autowirecheck.xml");
        }catch (IOException e){
            log.warn("Unable to find autowirecheck.xml", e);
        }
        Map<String, String> props = new HashMap<>();
        for (Resource resource : propertyResources) {
            if(resource.exists()){
                try {
                    List<String> propertyLines = IOUtils.readLines(new FileInputStream(resource.getFile()), Charset.defaultCharset());
                    for (String propertyLine :
                            propertyLines) {
                        String[] line = propertyLine.split("=");
                        props.put(line[0], line[1]);
                    }
                } catch (IOException e) {
                    log.error("Unable to load autowirecheck.properties", e);
                }
            }
        }
        for (Resource resource :
                xmlResources) {
            if (resource.exists()){
                try {
                    props.putAll(convertXmlToPropertyMap(resource));
                } catch (ParserConfigurationException e) {
                    log.error("Unable to parse xml configuration file", e);
                } catch (IOException e) {
                    log.error("Unable to load xml configuration file from classpath", e);
                } catch (SAXException e) {
                    log.error("Unable to load xml from the configuration file", e);
                } catch (XPathExpressionException e) {
                    log.error("XPath expression to locate configuration value is invalid", e);
                }
            }
        }

        properties.putAll(props);
    }

    private Map<? extends String, ? extends String> convertXmlToPropertyMap(Resource resource) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        Map<String, String> map = new HashMap<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder  builder = factory.newDocumentBuilder();
        XPath xPath = XPathFactory.newInstance().newXPath();

        Document doc = builder.parse(resource.getFile());
        map.put(GENERATE_REPORT, getNodeValue(doc, xPath, "//report/generate"));
        map.put(OUTPUT_DIR, getNodeValue(doc, xPath, "//report/outputDir"));

        return map;
    }

    private String getNodeValue(Document doc, XPath xPath, String pathExpression) throws XPathExpressionException {
        return ((NodeList) xPath.compile(pathExpression).evaluate(doc, XPathConstants.NODESET)).item(0).getTextContent();
    }
}
