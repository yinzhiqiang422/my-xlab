package com.mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.utils.DateUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.amqp.UserMessageLog;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class MockUsers {
	private static final Logger LOG = Logger.getLogger(MockUsers.class);

	public void mockUsers() {
		LOG.info("准备启动服务器.");
		LOG.info("准备启动JFinal框架...会连接MySql.");
		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://139.129.233.155:8066/TESTDB", "test", "test");
		// C3p0Plugin c3p0Plugin = new
		// C3p0Plugin("jdbc:mysql://139.129.233.155:3306/db1", "root", "root");
		// C3p0Plugin c3p0Plugin = new
		// C3p0Plugin("jdbc:mysql://114.215.120.29:3306/test", "root", "root");
		// C3p0Plugin c3p0Plugin = new
		// C3p0Plugin("jdbc:mysql://127.0.0.1:3306/test", "root", "root");
		c3p0Plugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		// arp.addMapping("claims", Claims.class);
		arp.start();

		// List<Record> rcdLst = Db.find("select * from employee");
		// for(Record record : rcdLst) {
		// System.out.println(JsonKit.toJson(record));
		// }

		// List<Record> recordLst = new ArrayList<Record>();
		//
		// System.out.println("------------------------");
		// for(int i=11;i<16;i++) {
		// Record record = new Record();
		// record.set("id", i)
		// .set("name", "lincon"+i)
		// .set("sharding_id", 10000);
		// Db.save("employee", record);
		// System.out.println("------Saved--"+JsonKit.toJson(record));
		// }
		//// Db.batchSave("t1", recordLst, 5);
		//
		// System.out.println("------------------------");
		// recordLst = Db.find("select * from employee");
		// for(Record record : rcdLst) {
		// System.out.println(JsonKit.toJson(record));
		// }
	}

	// start==>1479813078579
	// -- 50 more --
	// -- 50 more --
	// -- 50 more --
	// -- 50 more --
	// -- 50 more --
	// -- 50 more --
	// -- 50 more --
	// -- 50 more --
	// -- 50 more --
	// start==>1479813078579
	// e n d==>1479813126997
	// 500条/50s=10条/s
	public void saveTPS() {
		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://139.129.233.155:8066/TESTDB", "test", "test");
		c3p0Plugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.start();
		System.out.println("start==>" + System.currentTimeMillis());
		for (int i = 5500; i < 500; i++) {
			Record record = new Record();
			record.set("id", i).set("user", "lincon").set("traveldate", DateUtils.formatDate(new Date(), "yyyy-MM-dd"))
					.set("fee", Math.floor(Math.random() * 10000)).set("days", Math.round(Math.random() * 40));
			Db.save("travelrecord", record);
			if (i % 50 == 0) {
				System.out.println("-- 50 more --");
			}
		}
		System.out.println("e n d==>" + System.currentTimeMillis());
	}

	public void saveTPS0() {
		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://139.129.233.155:3306/contest", "root", "root");
		c3p0Plugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.start();
		long start = System.currentTimeMillis();
		for (int i = 5600; i < 5700; i++) {
			Record record = new Record();
			record.set("id", i).set("user", "lincon").set("traveldate", DateUtils.formatDate(new Date(), "yyyy-MM-dd"))
					.set("fee", Math.floor(Math.random() * 10000)).set("days", Math.round(Math.random() * 40));
			Db.save("travelrecord", record);
			if (i % 50 == 0) {
				System.out.println("-- 50 more --");
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(((end - start) / 1000) + "s");
	}

	public void saveWithNoticeMQ() {
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
		RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
		for (int i = 16; i < 120; i++) {
			Record record = new Record();
			record.set("id", i).set("name", "lincon" + i).set("sharding_id", 10000);
			UserMessageLog log = new UserMessageLog(UUID.randomUUID().toString(), i, "u.c", JsonKit.toJson(record),
					DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), null, System.currentTimeMillis(), 0);
			Db.save("employee", record);
			template.convertAndSend(JsonKit.toJson(log));
			System.out.println("------Saved--" + JsonKit.toJson(record));
		}
		ctx.destroy();
	}

	@Test
	public void mycatWithC3P0ForbiddenAutoCommit() {

		Connection conn = MDBManager.getConnection();
		long start = System.currentTimeMillis();
		try {
			//conn.createStatement().execute("set autocommit=0");
			String sql = "insert into travelrecord(id,user,traveldate,fee,days) values (?,?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for (int j = 0; j < 10; j++) {
				for (int i = 10000; i < 10500; i++) {
					pstmt.setInt(1, i);
					pstmt.setString(2, "lily");
					pstmt.setString(3, DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
					pstmt.setDouble(4, Math.floor(Math.random() * 10000));
					pstmt.setInt(5, 1);
					pstmt.addBatch(); // 添加一次预定义参数
				}
				// 批量执行预定义SQL
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MDBManager.closeConnection(conn);
		}
		long end = System.currentTimeMillis();
		System.out.println(((end - start) / 1000) + "s");
	}

	public void mycatForbiddenAutoCommit() {
		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://139.129.233.155:8066/TESTDB", "test", "test");
		c3p0Plugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.start();

		Db.update("set autocommit=0");
		long start = System.currentTimeMillis();
		Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				for (int i = 0; i < 100; i++) {
					Db.update("insert into travelrecord(id,user,traveldate,fee,days) values (?,?,?,?,?)", i, "lily",
							DateUtils.formatDate(new Date(), "yyyy-MM-dd"), Math.floor(Math.random() * 10000),
							Math.round(Math.random() * 40));
				}
				return true;
			}
		});
		long end = System.currentTimeMillis();
		System.out.println(((end - start) / 1000) + "s");
	}

	public void mycatWithC3P0() {

		long start = start = System.currentTimeMillis();
		System.out.println(start+ "ms");
		for (int i = 5000; i < 10000; i++) {
			Connection conn = MDBManager.getConnection();
			
			try {
				String sql = "insert into travelrecord(id,user,traveldate,fee,days) values (?,?,?,?,?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, i);
				pstmt.setString(2, "lily");
				pstmt.setString(3, DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
				pstmt.setDouble(4, Math.floor(Math.random() * 10000));
				pstmt.setInt(5, 1);
				pstmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				MDBManager.closeConnection(conn);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(end+ "ms");
		System.out.println(((end - start) / 1000) + "s");
	}

	public void mycatWithBatch() {
		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://139.129.233.155:8066/TESTDB", "test", "test");
		c3p0Plugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.start();

		// final Semaphore semp = new Semaphore(100);
		// ExecutorService exec = Executors.newCachedThreadPool();
		// long start = System.currentTimeMillis();
		// Db.update("delete from travelrecord");
		// int count = 0;
		// for (int batch = 0; batch < 2; batch++) {
		// List<Record> lst = new LinkedList<Record>();
		// for (int i = 0; i < 250; i++) {
		// count = count + 1;
		// Record record = new Record();
		// System.out.println(count);
		// record.set("id", count).set("user", "lincon")
		// .set("traveldate", DateUtils.formatDate(new Date(), "yyyy-MM-dd"))
		// .set("fee", Math.floor(Math.random() * 10000)).set("days",
		// Math.round(Math.random() * 40));
		// lst.add(record);
		// }
		// System.out.println(lst);
		// Db.batchSave("travelrecord", lst, lst.size());
		// }

		Db.update("delete from travelrecord");
		long start = System.currentTimeMillis();
		Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				for (int i = 0; i < 250; i++) {
					Db.update("insert into travelrecord(id,user,traveldate,fee,days) values (?,?,?,?,?)", i, "lily",
							DateUtils.formatDate(new Date(), "yyyy-MM-dd"), Math.floor(Math.random() * 10000),
							Math.round(Math.random() * 40));
				}
				return true;
			}
		});
		Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				for (int i = 250; i < 500; i++) {
					Db.update("insert into travelrecord(id,user,traveldate,fee,days) values (?,?,?,?,?)", i, "lily",
							DateUtils.formatDate(new Date(), "yyyy-MM-dd"), Math.floor(Math.random() * 10000),
							Math.round(Math.random() * 40));
				}
				return true;
			}
		});
		long end = System.currentTimeMillis();
		System.out.println(((end - start) / 1000) + "s");
	}

	public void mycatWithThreadPool() {
		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://139.129.233.155:8066/TESTDB", "test", "test");
		c3p0Plugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.start();

		final Semaphore semp = new Semaphore(100);
		ExecutorService exec = Executors.newCachedThreadPool();
		long start = System.currentTimeMillis();
		for (int index = 5400; index < 5500; index++) {
			final int NO = index;
			Runnable run = new Runnable() {
				public void run() {
					// 获取许可
					try {
						semp.acquire();
						Record record = new Record();
						record.set("id", NO).set("user", "lincon")
								.set("traveldate", DateUtils.formatDate(new Date(), "yyyy-MM-dd"))
								.set("fee", Math.floor(Math.random() * 10000))
								.set("days", Math.round(Math.random() * 40));
						Db.save("travelrecord", record);
						semp.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			exec.execute(run);
		}
		// 退出线程池
		exec.shutdown();
		try {
			boolean bool = exec.awaitTermination(50, TimeUnit.SECONDS);
			long end = System.currentTimeMillis();
			if (bool) {
				// 28s
				System.out.println(((end - start) / 1000) + "s");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
