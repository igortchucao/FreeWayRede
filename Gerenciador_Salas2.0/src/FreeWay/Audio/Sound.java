package FreeWay.Audio;

import java.applet.Applet;
import java.applet.AudioClip;

@SuppressWarnings("deprecation")
public class Sound {
	private AudioClip clip;
	
	public static final Sound buzina1 = new Sound("/buzina1.wav");
	public static final Sound buzina2 = new Sound("/buzina2.wav");
	public static final Sound buzina3 = new Sound("/buzina3.wav");
	

	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {
		}
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
