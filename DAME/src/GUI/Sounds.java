package GUI;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;


/**
 *
 * @author k0wnz
 */
public class Sounds{
   
    private File ziehSound;
    private File schlagSound;

    private boolean play_pressed = false; 
    private boolean schlagloop_started = false;
    private boolean ziehloop_started = false;
    
    private Clip ziehClip;
    private DataLine.Info ziehInfo;
    private AudioInputStream ziehAis;
    
    private Clip schlagClip;
    private DataLine.Info schlagInfo;
    private AudioInputStream schlagAis;
    public Sounds (){

      //////////////////////////////////////////////////////////////
        try {
    	ziehSound = new File ("res/audio/ziehSound.wav");

		ziehAis = AudioSystem.getAudioInputStream(ziehSound);

      ziehInfo = new DataLine.Info(Clip.class, ziehAis.getFormat());
      ziehClip = (Clip) AudioSystem.getLine (ziehInfo);
      ziehClip.open(ziehAis); 
      //////////////////////////////////////////////////////////////
      schlagSound = new File ("res/audio/schlagSound.wav");
      schlagAis = AudioSystem.getAudioInputStream(schlagSound);
      schlagInfo = new DataLine.Info(Clip.class, schlagAis.getFormat());
      schlagClip = (Clip) AudioSystem.getLine (schlagInfo);
      schlagClip.open(schlagAis); 
      //////////////////////////////////////////////////////////////
  	} catch (Exception e) {
		
		e.printStackTrace();
	}

      }
    public void schlagSound(){
    	schlagClip.setMicrosecondPosition(0);
    	schlagClip.start();
    }
    public void ziehSound(){
    	ziehClip.setMicrosecondPosition(0);
    	ziehClip.start();
    }
      

}
