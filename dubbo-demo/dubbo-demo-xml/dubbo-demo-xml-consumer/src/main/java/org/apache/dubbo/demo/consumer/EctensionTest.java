package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Protocol;

/**
 * @author spilledyear
 * @date 2018/11/25 18:10
 */

public class EctensionTest {
    public static void main(String[] args) {
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension("dubbo");

        protocol.destroy();
    }
}
