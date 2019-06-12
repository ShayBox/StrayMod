package com.shaybox.chancecubetimer;

class Utilities {
	// I'm a filthy javascript developer, suck it
	static void setTimeout(Runnable runnable, int delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (Exception e) {
				System.err.println(e);
			}
		}).start();
	}
}
