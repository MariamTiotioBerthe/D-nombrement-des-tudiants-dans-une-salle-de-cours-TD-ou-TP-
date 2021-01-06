package testClass;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

public class ChooseFile {
	/** methode pour acceder au fichier */
	public File fileToChoose() {
		JFileChooser choix = new JFileChooser();
		int retour=choix.showOpenDialog(null);
		if(retour==JFileChooser.APPROVE_OPTION){
		   choix.getSelectedFile().getName();
		   choix.getSelectedFile().getAbsolutePath();
		   }
		return choix.getSelectedFile();
		}
	/**methode pour compter le nombre de ligne */
	public int read_ligne(File f) throws IOException {
		int count =0;
		try {
			BufferedReader read = new BufferedReader(new FileReader(f));
			while(read.readLine()!=null) count++;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			}
		return count;
	}

}
 