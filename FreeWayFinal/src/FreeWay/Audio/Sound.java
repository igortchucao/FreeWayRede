package FreeWay.Audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	public Sound(String path) { // Método AudioAcerto para chamar na classe executavel.
		try {
			// URL do som que no caso esta no pendrive, mais ainda é uma fase de teste.
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
					new File(path)
							.getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
			//clip.loop(Clip.LOOP_CONTINUOUSLY); // Para repetir o som.
		} catch (Exception ex) {
			System.out.println("Erro ao executar SOM!");
			ex.printStackTrace();
		}
	}
}