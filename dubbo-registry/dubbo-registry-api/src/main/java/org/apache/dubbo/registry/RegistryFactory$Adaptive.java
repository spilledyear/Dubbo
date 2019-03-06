package org.apache.dubbo.registry;

import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * @author spilledyear
 * @date 2018/12/2 16:30
 */
public class RegistryFactory$Adaptive implements org.apache.dubbo.registry.RegistryFactory {
    private static final org.apache.dubbo.common.logger.Logger logger = org.apache.dubbo.common.logger.LoggerFactory.getLogger(ExtensionLoader.class);
    private java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger(0);

    public org.apache.dubbo.registry.Registry getRegistry(org.apache.dubbo.common.URL arg0) {
        if (arg0 == null) throw new IllegalArgumentException("url == null");
        org.apache.dubbo.common.URL url = arg0;
        String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(org.apache.dubbo.registry.RegistryFactory) name from url(" + url.toString() + ") use keys([protocol])");
        org.apache.dubbo.registry.RegistryFactory extension = null;
        try {
            extension = (org.apache.dubbo.registry.RegistryFactory) ExtensionLoader.getExtensionLoader(org.apache.dubbo.registry.RegistryFactory.class).getExtension(extName);
        } catch (Exception e) {
            if (count.incrementAndGet() == 1) {
                logger.warn("Failed to find extension named " + extName + " for type org.apache.dubbo.registry.RegistryFactory, will use default extension dubbo instead.", e);
            }
            extension = (org.apache.dubbo.registry.RegistryFactory) ExtensionLoader.getExtensionLoader(org.apache.dubbo.registry.RegistryFactory.class).getExtension("dubbo");
        }
        return extension.getRegistry(arg0);
    }
}
