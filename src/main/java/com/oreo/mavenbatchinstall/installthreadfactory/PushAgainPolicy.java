package com.oreo.mavenbatchinstall.installthreadfactory;

import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * CopyRight (C),YLINK-深圳雁联计算有限公司
 *
 * @author 李沛恒(QQ ： 单曲循环)
 * @date 2018/1/17 ~ 下午 9:52
 */
@Component
public final class PushAgainPolicy implements RejectedExecutionHandler {
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		try {
			executor.getQueue().put(r);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
