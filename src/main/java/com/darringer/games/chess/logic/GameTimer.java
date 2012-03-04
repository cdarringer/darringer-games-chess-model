package com.darringer.games.chess.logic;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * Utility functions for tracking time in a chess game - 
 * we need to know when a time limit is up and, in some cases,
 * it may be good to know when half of our time is up so 
 * that we can decide whether to change search strategies.
 * 
 * @author cdarringer
 *
 */
public class GameTimer {

	private static Logger log = Logger.getLogger(GameTimer.class);
	
	private Timer timer;
	private Timer halfTimer;
	private boolean isTimeUp;
	private boolean isHalfTimeUp;
	private int timeoutInSeconds;

	/**
	 * Default constructor.
	 * Create a timer with no timeouts - useful for 
	 * unit testing, for example.
	 */
	public GameTimer() {
		isTimeUp = false;
		isHalfTimeUp = false;
		timer = null;
		halfTimer = null;
	}
	
	
	/**
	 * Create a timer with the given timeout in seconds
	 * @param timeoutInSeconds
	 */
	public GameTimer(int timeoutInSeconds) {
		isTimeUp = false;
		isHalfTimeUp = false;
		this.timeoutInSeconds = timeoutInSeconds;
		timer = new Timer();
		halfTimer = new Timer();
	}

	public boolean isTimeUp() {
		return isTimeUp;
	}
	
	public boolean isHalfTimeUp() {
		return isHalfTimeUp;
	}

	public void start() {
		if (timer != null) {
			timer.schedule(new GameTimerTask(), timeoutInSeconds * 1000);
		}
		if (halfTimer != null) {
			halfTimer.schedule(new GameTimerHalfTask(), timeoutInSeconds * 500);
		}
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
		}
		if (halfTimer != null) {
			halfTimer.cancel();
		}
	}

	/**
	 * Implementation of {@link TimerTask} that lets us know when 
	 * the time limit is reached.
	 * 
	 * @author cdarringer
	 *
	 */
	class GameTimerTask extends TimerTask {
		public void run() {
			isTimeUp = true;
			timer.cancel();
			log.info("Time up!");
		}
	}

	/**
	 * Implementation of {@link TimerTask} that lets us know when
	 * we are half way to the time limit
	 * 
	 * @author cdarringer
	 *
	 */
	class GameTimerHalfTask extends TimerTask {
		public void run() {
			isHalfTimeUp = true;
			halfTimer.cancel(); 
			log.info("Half time up!");
		}
	}
}
