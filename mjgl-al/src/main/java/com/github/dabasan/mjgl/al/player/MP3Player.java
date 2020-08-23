package com.github.dabasan.mjgl.al.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * MP3 player
 * 
 * @author Daba
 *
 */
public class MP3Player {
	private Logger logger = LoggerFactory.getLogger(MP3Player.class);

	private AdvancedPlayer player;
	private PlayerThread playerThread;

	public MP3Player(InputStream is) throws JavaLayerException {
		this.constructorBase(is);
	}
	public MP3Player(File file) throws IOException, JavaLayerException {
		try (var fis = new FileInputStream(file)) {
			this.constructorBase(fis);
		}
	}
	public MP3Player(String filepath) throws IOException, JavaLayerException {
		try (var fis = new FileInputStream(filepath)) {
			this.constructorBase(fis);
		}
	}
	private void constructorBase(InputStream is) throws JavaLayerException {
		player = new AdvancedPlayer(is);
	}

	public void close() {
		player.close();
	}

	public void play() {
		playerThread = new PlayerThread(player);
		playerThread.start();
	}
	public void stop() {
		if (playerThread == null) {
			return;
		}

		playerThread.shutdown();
	}

	private class PlayerThread extends Thread {
		private AdvancedPlayer player;
		private volatile boolean isActive;

		private PlayerThread(AdvancedPlayer player) {
			this.player = player;
			isActive = true;
		}

		public void shutdown() {
			player.stop();
			isActive = false;
		}

		@Override
		public void run() {
			try {
				player.play();
			} catch (JavaLayerException e) {
				logger.error("Error", e);
			}

			while (this.isActive) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					logger.error("Error", e);
				}
			}
		}
	}
}
