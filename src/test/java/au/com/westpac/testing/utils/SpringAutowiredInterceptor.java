package au.com.westpac.testing.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * SpringAutowiredInterceptor
 * Created by M041451 on 20/03/2017.
 *
 * Responsible for locating missing Autowired dependencies and mocking them so that unit tests can potentially continue
 * since they cannot have been run with a real collaboration tested against the missing bean. This class also outputs a
 * report into the test output location that includes all classes that have missing dependencies.
 *
 * DO NOT use in production Spring configuration.
 */
@Component
public class SpringAutowiredInterceptor implements BeanFactoryPostProcessor {

    private static Logger log = LogManager.getLogger();
    private final Map<Class, Class<?>[]> autowiredTypes;
    private boolean generateReport;
    private Path reportOutputDirectory = null;

    public SpringAutowiredInterceptor() {
        updateConfiguration();
        autowiredTypes = autowiredTypes(locateClassesWithAutowiredAnnotations());
    }

    private void updateConfiguration() {
        AutowireCheckConfiguration config = new AutowireCheckConfiguration();
        generateReport = config.generateReport();
        if(generateReport) {
            reportOutputDirectory = Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceFirst("^/(.:/)", "$1"));
            if(config.getOutputDir() != null){
                reportOutputDirectory = config.getOutputDir();
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Set<Class> missingBeanDefinitions = null;
        try {
            missingBeanDefinitions = detectMissingBeanDefinitions(configurableListableBeanFactory);
        } catch (AutowireCheckException e) {
            log.error(e.getMessage(), e);
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry)configurableListableBeanFactory;
        for (Class clazz : missingBeanDefinitions) {
            BeanDefinition bd = createMockBeandefinition(clazz);
            registry.registerBeanDefinition(clazz.getName(), bd);
        }
    }

    protected void report(List<Class> classes) throws AutowireCheckException {
        AutowireCheckReporter reporter = new AutowireCheckReporter();
        reporter.setOutputLocation(this.reportOutputDirectory.toFile());
        for (final Class clazz :
                classes) {
            reporter.add(new ReportableItem() {
                @Override
                public String getName() {
                    return clazz.getName();
                }
            });
        }
        reporter.report();
    }

    private String findParentPathFromLoader(Class clazz) {
        ClassLoader loader = clazz.getClassLoader();
        String className = clazz.getName().replace('.', '/');
        URL url = loader.getResource(className + ".class");
        Assert.notNull(url);
        String path = url.getPath();
        return path.substring(0, path.indexOf(className + ".class") - 1);
    }

    private BeanDefinition createMockBeandefinition(Class clazz) {
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Mockito.class);
        definitionBuilder.setFactoryMethod("mock");
        definitionBuilder.addConstructorArgValue(clazz.getName());
        return definitionBuilder.getBeanDefinition();

    }

    protected Set<Class> detectMissingBeanDefinitions(ConfigurableListableBeanFactory configurableListableBeanFactory) throws AutowireCheckException {
        Set<Class> missingBeanDefinitions = new HashSet<>();
        List<Class> unmetDependencies = new ArrayList<>();
        Set<Class> classes = autowiredTypes.keySet();
        for (Class aClass : classes) {
            for (Class<?> bean : autowiredTypes.get(aClass)) {
                try {
                    configurableListableBeanFactory.getBean(bean);
                }catch(NoSuchBeanDefinitionException nsbde){
                    unmetDependencies.add(aClass);
                    missingBeanDefinitions.add(bean);
                }
            }
        }
        report(unmetDependencies);
        return missingBeanDefinitions;
    }

    private Map<Class, Class<?>[]> autowiredTypes(Set<BeanDefinition> potentiallyUnconfiguredComponentDefinitions) {
        Map<Class, Class<?>[]> typesAndTheirAutowireTypes = new HashMap<>();
        for (BeanDefinition componentDefinition : potentiallyUnconfiguredComponentDefinitions) {
            try {
                Class<?> clazz = Class.forName(componentDefinition.getBeanClassName());
                log.debug("loaded a class definition for {}", clazz.getName());
                Constructor<?>[] constructors = clazz.getConstructors();
                for (Constructor<?> constructor : constructors) {
                    if(constructor.getAnnotation(Autowired.class) != null){
                        log.debug("got the constructor with parameters {}", mapArrayToString(constructor.getParameterTypes()));
                        //constructor is autowired so find the parameter types and add them to the map
                        typesAndTheirAutowireTypes.put(clazz, constructor.getParameterTypes());
                    }
                }
            } catch (ClassNotFoundException e) {
                log.error(e);
            }
        }
        return typesAndTheirAutowireTypes;
    }

    private String mapArrayToString(Class<?>[] parameterTypes) {
        final String prefix = "Class: ";
        final String suffix = ", ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            sb.append(prefix).append(parameterTypes[i].getName());
            if(i == parameterTypes.length -1){
                continue;
            }
            sb.append(suffix);
        }
        return sb.toString();
    }

    private Set<BeanDefinition> locateClassesWithAutowiredAnnotations() {
        ClassPathScanningCandidateComponentProvider  scanner = new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(WebAppConfiguration.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Autowired.class));
        return scanner.findCandidateComponents("au.com.westpac");
    }
}