package main;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	Clip clip;
	URL soundURL[] = new URL[30];
	FloatControl fc;
	float volume = 0;


	public Sound() {
		FloatControl fc;
		soundURL[0] = getClass().getResource("/sound/testsong.wav");
		soundURL[1] = getClass().getResource("/sound/zombie_burned.wav");
		soundURL[2] = getClass().getResource("/sound/projectile.wav");
		soundURL[3] = getClass().getResource("/sound/pickup.wav");
//		soundURL[4] = getClass().getResource("/sound/walk 1 fast.wav");
//		soundURL[5] = getClass().getResource("/sound/walk 3 slow.wav");
		soundURL[6] = getClass().getResource("/sound/impact5.wav");
		soundURL[7] = getClass().getResource("/sound/door.wav");
		soundURL[8] = getClass().getResource("/sound/machine.wav");
		soundURL[9] = getClass().getResource("/sound/Hit_Hurt1.wav");
		soundURL[10] = getClass().getResource("/sound/Impact5.wav");
		soundURL[11] = getClass().getResource("/sound/daynight.wav");
		soundURL[12] = getClass().getResource("/sound/okayish.wav");

	}
	public void setFile(int i){


		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);

			clip = AudioSystem.getClip();
			clip.open(ais);
			fc =  (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			fc.setValue(volume);
			
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void play() {
		clip.start();
		
	}
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop() {
		clip.stop();
	}
	public void setVolume(float v) {
		volume = v;
		
	}

}
