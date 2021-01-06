package testClass;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Voice {
	public void Play(String f) {

	    try {
	      File file = new File("audio/"+f+".wav");
	      AudioInputStream stream = AudioSystem.getAudioInputStream(file);
	      Clip clip = AudioSystem.getClip();
	      clip.open(stream);
	      clip.start();
	 
	      // sleep to allow enough time for the clip to play
	      Thread.sleep(1000);
	 
	      stream.close();
	 
	    } catch (Exception ex) {
	      System.out.println(ex.getMessage());
	    }
	  }
	public void callNumber(int a) { 
		int tab1[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,30,40,50,60,80,90,100}; 
		String tab2 [] = {"21","22","23","24","25","27","28","29","31","32","33","34","35","36","37","38","39","41","42","43","44","45","46","47","48","49","51","52","53","54","55","56","57","58","59","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99"};
		for (int i = 0;i<tab1.length;i++) {
			if (a==tab1[i]) {
				this.Play(""+tab1[i]);
			} 
		}
		String n = String.valueOf(a);
		for (int i = 0;i< tab2.length;i++) {
			if (n.equals(tab2[i])) {
				String[] tab = tab2[i].split("");
				this.Play(""+tab[1]);
				this.Play("ET");
				this.Play(""+tab[0]+"0");
			}
			} 
	}
		


	public static void main(String[] args) {
		Voice e = new Voice();
		e.callNumber(1);
	}	

}
