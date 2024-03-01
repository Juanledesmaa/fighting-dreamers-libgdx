package com.mygdx.game.sound;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public enum GeneralSounds {

	BLUE_PROJECTILE("Sounds/Projectile/CD_00150.wav"),
	SINGLE_HIT_1("Sounds/Hit/single_1.wav"),
	SINGLE_HIT_2("Sounds/Hit/single_2.wav"),
	SINGLE_HIT_3("Sounds/Hit/single_3.wav");


	final static float VOLUME = 0.2f;

	private final Sound sound;

	// Define a constructor to initialize each enum constant with its corresponding sound file
	GeneralSounds(String path) {
		this.sound = Gdx.audio.newSound(Gdx.files.internal(path));
	}

	// Method to play the sound
	public void play() {
		long id = sound.play(VOLUME);
		sound.setLooping(id, false);
	}

	// Method to dispose of the sound when it's no longer needed
	public void dispose() {
		sound.dispose();
	}

	// Example method to stop the sound
	public void stop() {
		sound.stop();
	}
}
