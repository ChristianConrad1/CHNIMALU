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
    private File promoteSound;
    
    private boolean play_pressed = false; 
    private boolean schlagloop_started = false;
    private boolean ziehloop_started = false;
    
    private Clip ziehClip;
    private DataLine.Info ziehInfo;
    private AudioInputStream ziehAis;
    
    private Clip schlagClip;
    private DataLine.Info schlagInfo;
    private AudioInputStream schlagAis;
    
    private Clip promoteClip;
    private DataLine.Info promoteInfo;
    private AudioInputStream promoteAis;
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
    	promoteSound = new File ("res/audio/promoteSound.wav");
    	promoteAis = AudioSystem.getAudioInputStream(promoteSound);
    	promoteInfo = new DataLine.Info(Clip.class, promoteAis.getFormat());
    	promoteClip = (Clip) AudioSystem.getLine (promoteInfo);
    	promoteClip.open(promoteAis); 
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
    public void promoteSound(){
    	promoteClip.setMicrosecondPosition(0);
    	promoteClip.start();
    }

}
