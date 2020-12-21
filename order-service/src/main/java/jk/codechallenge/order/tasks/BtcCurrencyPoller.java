package jk.codechallenge.order.tasks;

import java.util.concurrent.Future;

public abstract class BtcCurrencyPoller implements Runnable {
	private Future<?> future;

	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}
}
