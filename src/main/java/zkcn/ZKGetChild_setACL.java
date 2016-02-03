package zkcn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

public class ZKGetChild_setACL {

	// 会话超时时间，设置为与系统默认时间一致11

	private static final int SESSION_TIMEOUT = 30000;

	// 创建 ZooKeeper 实例

	ZooKeeper zk;

	// 创建 Watcher 实例

	Watcher wh = new Watcher() {

		public void process(org.apache.zookeeper.WatchedEvent event)

		{

			System.out.println(event.toString());

		}

	};

	// 初始化 ZooKeeper 实例

	private void createZKInstance() throws IOException

	{

		zk = new ZooKeeper("192.168.188.128:2181",
				ZKGetChild_setACL.SESSION_TIMEOUT, this.wh);

	}

	private void getChild(String path) throws IOException,
			InterruptedException, KeeperException

	{

//		Id id = new Id("ip", "192.168.188.128/27");
//		ACL acl = new ACL(ZooDefs.Perms.ALL, id);
//		List<ACL> acls = new ArrayList<ACL>();
//		acls.add(acl);
//		zk.setACL(path, acls, -1);
//		System.out.println("set ACL succ:" + path);

		List<String> child_list = zk.getChildren(path, false);
		for (String child : child_list) {
			System.out.println("child:" + child);
			if (path.equals("/")) {
				child = path + "" + child;
				System.out.println("A:" + child);
			} else {
				child = path + "/" + child;
				System.out.println("B:" + child);
			}

			System.out.println(new String(zk.getData(child, false, null)));

			// child=path+""+child;
			Stat stat = zk.exists(child, false);
			List<ACL> child_acl = zk.getACL(child, stat);
			for (ACL a : child_acl) {
				// System.out.println(a.getPerms());
				System.out.println(a);

				System.out.println("**********");
			}

			getChild(child);
		}
	}

	private void confAcl(String path) throws KeeperException,
			InterruptedException

	{

		Id id = new Id("world", "anyone"); // 放开权限
		Id id2 = new Id("ip", "192.168.188.128"); // 限定ip

		ACL acl = new ACL(ZooDefs.Perms.ALL, id);
		ACL acl2 = new ACL(ZooDefs.Perms.ALL, id2);

		List<ACL> acls = new ArrayList<ACL>();
		acls.add(acl);
		acls.add(acl2);

		zk.setACL(path, acls, -1);
		System.out.println("set ACL succ:" + path);

	}

	private void ZKClose() throws InterruptedException {
		zk.close();
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, KeeperException {

		ZKGetChild_setACL dm = new ZKGetChild_setACL();

		dm.createZKInstance();

//		dm.confAcl("/zk");
		
		dm.getChild("/zk");

		dm.ZKClose();

	}

}
