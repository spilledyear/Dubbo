package Test;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

/**
 * @author spilledyear
 * @date 2019-01-09 13:31
 */
public class SubscribeChildChanges {
    static final String CONNECT_ADDR = "10.9.15.32:2181";
    static final int SESSION_TIMEOUT = 500000;

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient(new ZkConnection(CONNECT_ADDR, SESSION_TIMEOUT));

        // 监听的只是/super下面的直接子节点，并不会递归监控/super下面的所有节点
        zkClient.subscribeChildChanges("/super", (parentPath, currentChilds) -> {
            System.out.println("parentPath：" + parentPath);
            System.out.println("currentChilds：" + currentChilds);
        });

//        Thread.sleep(3000);
//        zkClient.createPersistent("/super");
//        Thread.sleep(1000);
//        zkClient.createPersistent("/super/c1", "内容一");
//        Thread.sleep(1000);
//        zkClient.createPersistent("/super/c2", "内容二");
//        Thread.sleep(1000);
//        zkClient.delete("/super/c2");
//        Thread.sleep(1000);
//        zkClient.deleteRecursive("/super");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
