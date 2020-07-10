import kotlin.io.FilesKt;
import net.sf.cglib.core.*;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;

import java.io.File;

public class CglibSimple {

    public static void main(String[] args) {

        class CommonStrategy implements GeneratorStrategy, NamingPolicy {
            GeneratorStrategy generatorStrategy = new DefaultGeneratorStrategy();
            NamingPolicy namingPolicy = new DefaultNamingPolicy();
            String className = "";

            @Override
            public byte[] generate(ClassGenerator classGenerator) throws Exception {
                byte[] enhancerClass = generatorStrategy.generate(classGenerator);
                FilesKt.writeBytes(new File(className + ".class"), enhancerClass);
                return enhancerClass;
            }

            @Override
            public String getClassName(String s, String s1, Object o, Predicate predicate) {
                className = namingPolicy.getClassName(s, s1, o, predicate);
                return className;
            }
        }

        Enhancer enhancer = new Enhancer();
        CommonStrategy strategy = new CommonStrategy();
        enhancer.setStrategy(strategy);
        enhancer.setNamingPolicy(strategy);
        enhancer.setSuperclass(SimpleService.Person.class);
        enhancer.setInterfaces(new Class[]{SimpleService.GitHub.class, SimpleService.CcbBankDevolper.class});
        enhancer.setCallback(new FixedValue() {
            @Override
            public Object loadObject() throws Exception {
                return "Enhancer";
            }
        });
        Object enhancerClass = enhancer.create(new Class[]{String.class, int.class}, new Object[]{"boos", 1});
        System.out.println(enhancerClass.toString());
    }
}
