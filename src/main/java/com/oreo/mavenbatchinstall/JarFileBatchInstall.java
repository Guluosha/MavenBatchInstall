package com.oreo.mavenbatchinstall;

import com.oreo.mavenbatchinstall.installthread.MavenInstallThread;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * CopyRight (C),YLINK-深圳雁联计算有限公司
 *
 * @author 李沛恒(QQ ： 单曲循环)
 * @date 2018/1/12 ~ 下午 11:45
 */
public class JarFileBatchInstall {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/context.xml");
		File jarFileDirectory = (File) context.getBean("file");
		ThreadPoolExecutor threadPoolExecutor = context.getBean("threadPoolExecutor", ThreadPoolExecutor.class);
		File[] jarFiles = jarFileDirectory.listFiles();
		if (jarFiles != null) {
			for (File jarFile : jarFiles) {
				threadPoolExecutor.execute(new MavenInstallThread(jarFile));
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		threadPoolExecutor.shutdown();
		context.close();
	}
}