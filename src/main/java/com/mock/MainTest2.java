package com.mock;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.utils.DateUtils;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class MainTest2 {

	public static void main(String[] args) {
		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://192.168.1.113:3306/contest", "root", "root");
		c3p0Plugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.start();

		final Semaphore semp = new Semaphore(50);
		ExecutorService exec = Executors.newCachedThreadPool();
		long start = System.currentTimeMillis();
		for (int index = 5000; index < 10000; index++) {
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
			boolean bool = exec.awaitTermination(30, TimeUnit.SECONDS);
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
