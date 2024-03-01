package com.mygdx.game.sound;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public enum GeneralSounds {
	BACKGROUND_MUSIC("Music/Calling.mp3"),
	BLUE_PROJECTILE("Sounds/Projectile/CD_00150.wav"),
	SINGLE_HIT_1("Sounds/Hit/single_1.wav"),
	SINGLE_HIT_2("Sounds/Hit/single_2.wav"),
	SINGLE_HIT_3("Sounds/Hit/single_3.wav");

	private static final float VOLUME = 0.2f;
	private static final float VOLUME_MUSIC = 0.1f;
	private static final ObjectMap<GeneralSounds, Sound> soundMap = new ObjectMap<>();

	private final String path;

	// Constructor
	GeneralSounds(String path) {
		this.path = path;
	}

	public static void initializeSounds() {
		for (GeneralSounds sound : GeneralSounds.values()) {
			String path = sound.getPath();
			Sound loadedSound = Gdx.audio.newSound(Gdx.files.internal(path));
			soundMap.put(sound, loadedSound);
		}
	}

	public void play(boolean loop) {
		Sound sound = soundMap.get(this);
		if (sound != null) {
			long id = sound.play(VOLUME);
			sound.setLooping(id, loop);
		}
	}

	public void playMusic(boolean loop) {
		Sound sound = soundMap.get(this);
		if (sound != null) {
			long id = sound.play(VOLUME_MUSIC);
			sound.setLooping(id, loop);
		}
	}

	public void dispose() {
		Sound sound = soundMap.get(this);
		if (sound != null) {
			sound.dispose();
		}
	}

	public String getPath() {
		return path;
	}
}
