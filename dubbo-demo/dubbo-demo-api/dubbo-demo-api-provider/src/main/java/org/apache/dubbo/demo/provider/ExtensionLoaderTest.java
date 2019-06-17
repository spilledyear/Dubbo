package org.apache.dubbo.demo.provider;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Protocol;
import org.junit.Test;

public class ExtensionLoaderTest {

    @Test
    public void test() {

        ExtensionLoader loader = ExtensionLoader.getExtensionLoader(Protocol.class);

        Protocol protocol = (Protocol) loader.getExtension("dubbo");
    }
}

/**
 * 1.  ExtensionLoader loder = ExtensionLoader.getExtensionLoader(Protocol.class); 返回 loader1
 *        1.1 创建一个ExtensionLoader实例(loader1)，此时 loader1 的 type=Protocol.class，在为 loader1 的 objectFactory 属性赋值的时候，触发 ExtensionFactory 对应的ExtensionLoader实例化；
 *
 *        1.2 创建一个ExtensionLoader实例(loader2)，此时 loader2 的 type=ExtensionFactory.class，objectFactory = null;
 *
 *        1.3 执行 loader2 的 getAdaptiveExtension() 方法， 生成一个 AdaptiveExtensionFactory 实例(extensionFactoryAdaptive)，赋值给 loader1 的 objectFactory 属性；
 *                  1.3.1 先从缓存中找到，中不到就执行 loadExtensionClasses() 方法，该方法会从配置文件中加载 ExtensionFactory 所有实现类的class对象，同事可以将 adaptive 和 wrapper 类型的是实现类缓存起来
 *                  1.3.2 找到 ExtensionFactory 对应的 adaptive实现类是 AdaptiveExtensionFactory，然后通过反射创建 AdaptiveExtensionFactory 实例(extensionFactoryAdaptive)；
 *                              1.3.2.1 在 AdaptiveExtensionFactory 构造函数中，通过 loader2.getSupportedExtensions() 方法找到 spi(1.2.1中缓存起来的), 然后调用 loader2.getExtension("spi");
 *                              1.3.2.2 在 loader2.getExtension("spi") 方法中通过反射生成一个 SpiExtensionFactory 实例(spiExtensionFactory);
 *                              1.3.2.3 将 1.2.2.2 中生成的 SpiExtensionFactory 实例(spiExtensionFactory)  保存到 extensionFactoryAdaptive的factories属性中；
 *
 *        1.4 将 1.3 中生成的 AdaptiveExtensionFactory 实例(extensionFactoryAdaptive) 赋值给loader2的 objectFactory 属性，即 loader.objectFactory = extensionFactoryAdaptive;
 *
 *
 * 2. Protocol protocol = (Protocol) loder.getExtension("dubbo"); 这里的 loader 对应上面的 loader1
 *        2.1  先从缓存中获取，如果缓存中没有，就获取dubbo对应实现类的class对象，然后通过通过反射创建实例并放入缓存；
 *
 *        2.2 执行 injectExtension 方法，这是依赖注入相关的方法，可以注入 spring里面的bean对象 和 SPI对象；
 *                  2.2.1 找到实例的 setter 方法；
 *                  2.2.2 执行 objectFactory.getExtension 方法，这里的 objectFactory 就是指 extensionFactoryAdaptive；
 *                  2.2.3 执行 extensionFactoryAdaptive.getExtension 方法，然后在 该方法内部调用 spiExtensionFactory.getExtension 方法；
 *                  2.2.4 在 spiExtensionFactory.getExtension 方法内部找到对应的adaptive实例；
 *
 *        2.3 执行 wrapper 操作，即给实例 一层一层的包装，类似于多层代理；
 *
 *        2.4 返回最终的实例；
 */
