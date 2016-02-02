package zkcn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class App {

	private static int CONNECTION_TIMEOUT = 3000;

	static ZooKeeper zk = null;
	static List<String> tmpList = null;

	public static void main(String[] args) {

		tmpList = new ArrayList<String>();
		try {
			zk = new ZooKeeper(args[0],
					CONNECTION_TIMEOUT, new Watcher() {
						public void process(WatchedEvent event) {
							System.out.println("evnt " + event.getType() + "！");
						}
					});
			if (zk.exists(args[1], true) != null) {
				getChild(args[1]);
				for (int i = 0; i < tmpList.size(); i++) {
					System.out.println("Delete:" + tmpList.get(tmpList.size() - (i + 1)));
					zk.delete(tmpList.get(tmpList.size() - (i + 1)), -1);

				}
				zk.delete(args[1], -1);
				System.out.println(args[1] + ": Delete success..............");
			} else {
				System.out.println(args[1] + ":node does not exist.............");
			}
			zk.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (KeeperException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void getChild(String path) throws IOException,
			InterruptedException, KeeperException {
		List<String> child_list = zk.getChildren(path, false);
		for (String child : child_list) {
			System.out.println("child:" + child);
			if (path.equals("/")) {
				child = path + "" + child;
//				System.out.println("A:" + child);
			} else {
				child = path + "/" + child;
//				System.out.println("B:" + child);
				tmpList.add(child);
			}
			getChild(child);
		}
	}

}
