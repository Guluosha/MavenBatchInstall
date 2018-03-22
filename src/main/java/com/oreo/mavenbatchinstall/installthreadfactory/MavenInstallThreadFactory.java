package com.oreo.mavenbatchinstall.installthreadfactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CopyRight (C),YLINK-深圳雁联计算有限公司
 *
 * @author 李沛恒(QQ ： 单曲循环)
 * @date 2018/1/12 ~ 下午 10:56
 */
@Component
public class MavenInstallThreadFactory implements ThreadFactory {

	@Value(value = "${threadName}")
	private String threadName;
	@Autowired
	private AtomicInteger threadId;

	@Override
	public Thread newThread(Runnable runnable) {
		return new Thread(runnable, threadName + "--" + threadId.getAndIncrement());
	}
}