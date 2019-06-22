package com.shaybox.straymod.timer;

import com.shaybox.straymod.Configuration;
import com.shaybox.straymod.CustomTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;

public class TimerHelper {
	private Timer timer = new Timer();
	private LocalDateTime timerDateTime = LocalDateTime.now();
	private LocalDateTime pauseDateTime = LocalDateTime.now();
	private State state = State.NOT_RUNNING;

	public void start() {
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new CustomTask(), 1000 * 60 * Configuration.timer.minutes, 1000 * 60 * Configuration.timer.minutes);
		this.timerDateTime = LocalDateTime.now().plusMinutes(Configuration.timer.minutes);
		this.state = State.RUNNING;
	}

	public void stop() {
		this.timer.cancel();
		this.state = State.NOT_RUNNING;
	}

	public void pause() {
		this.timer.cancel();
		this.pauseDateTime = LocalDateTime.now();
		this.state = State.PAUSED;
	}

	public void resume() {
		Duration pauseDuration = Duration.between(this.pauseDateTime, LocalDateTime.now());
		LocalDateTime localDateTime = this.timerDateTime.plusSeconds(pauseDuration.getSeconds());
		Duration delayDuration = Duration.between(LocalDateTime.now(), localDateTime);

		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new CustomTask(), 1000 * delayDuration.getSeconds(), 1000 * 60 * Configuration.timer.minutes);
		this.timerDateTime = localDateTime;
		this.state = State.RUNNING;
	}

	public void skip() {
		this.timer.cancel();
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new CustomTask(), 0, 1000 * 60 * Configuration.timer.minutes);
		this.state = State.RUNNING;
	}

	public void updateTime() {
		this.timerDateTime = LocalDateTime.now().plusMinutes(Configuration.timer.minutes);
	}

	public LocalDateTime getTimerDateTime() {
		return timerDateTime;
	}

	public State getState() {
		return state;
	}
}