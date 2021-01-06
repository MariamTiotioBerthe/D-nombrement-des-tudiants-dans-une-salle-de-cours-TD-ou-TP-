package testClass;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import test.Voice;

import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class FaceTracking extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_inscrit;
	private JTextField textFiel_present;
	private JTextField textField_abscent;
	JButton Start;
	JButton Stop;
	JPanel video;
	private Tracking myThread = null;
    int count = 0;
    private int nbr_present=0, nbr_inscrit=0,nbr_abscent=0;
    VideoCapture webSource = null;
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();
    CascadeClassifier faceDetector = new CascadeClassifier("C:\\Users\\Mariam Tiotio Berthe\\Desktop\\java\\workspace eclipse\\PFA\\haarcascade_frontalface_alt.xml");
    CascadeClassifier eyeDetector = new CascadeClassifier("C:\\Users\\Mariam Tiotio Berthe\\Desktop\\java\\workspace eclipse\\PFA\\haarcascade_eye.xml");
    MatOfRect faceDetections = new MatOfRect();
    MatOfRect eyeDetections = new MatOfRect();
    Mat face;
    Mat crop = null;
    Mat circles = new Mat();
    private JButton btnQuitter;
    private JLabel lblNewLabel_image_fond;
    

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
					FaceTracking frame = new FaceTracking();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FaceTracking() {
		System.out.println(FaceDetection.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1230, 679);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setLocationRelativeTo(null);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(176,196,222));
		panel.setBounds(0, 0, 1212, 632);
		contentPane.add(panel);
		
	
		Start = new JButton("  Start");
		Start.setBounds(1062, 13, 138, 39);
		Start.setIcon(new ImageIcon("C:\\Users\\Mariam Tiotio Berthe\\Desktop\\PFA\\image\\demarrer.jpg"));
		Start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				webSource = new VideoCapture(0); // video capture from default cam
		        myThread = new Tracking(); //create object of threat class
		        Thread t = new Thread(myThread);
		        t.setDaemon(true);
		        myThread.runnable = true;
		        t.start();                 //start thrad
		        System.out.println("start");
				
			}
		});
		panel.setLayout(null);
		Start.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(Start);
		
		Stop = new JButton("  Stop");
		Stop.setBounds(1055, 103, 145, 39);
		Stop.setIcon(new ImageIcon("C:\\Users\\Mariam Tiotio Berthe\\Desktop\\PFA\\image\\stop.png"));
		Stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myThread.runnable = false;            // stop thread
				webSource.release();  // stop caturing fron cam
				System.out.println("stop.....");
				
			}
		});
		Stop.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(Stop);
		
		JButton NbrInscrit = new JButton("Nbr d'inscrit");
		NbrInscrit.setBounds(1057, 196, 145, 39);
		NbrInscrit.setIcon(new ImageIcon("C:\\Users\\Mariam Tiotio Berthe\\Desktop\\PFA\\image\\inscription.png"));
		NbrInscrit.setFont(new Font("Tahoma", Font.BOLD, 14));
		/**pour afficher le n'ombre d'inscrit à partir d'une boite de dialogue  après le click sur le bouton*/
		NbrInscrit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChooseFile classChooseFile= new ChooseFile();
				File f= classChooseFile.fileToChoose();
				try {
					nbr_inscrit=classChooseFile.read_ligne(f);
					textField_inscrit.setText("Nbr : "+nbr_inscrit);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		panel.add(NbrInscrit);
		
		textField_inscrit = new JTextField();
		textField_inscrit.setBounds(1145, 248, 57, 31);
		textField_inscrit.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(textField_inscrit);
		textField_inscrit.setColumns(10);
		
		/**pour afficher  le nombre de visage detecter*/
		JButton NbrPresent = new JButton("Nbr de present");
		NbrPresent.setBounds(1037, 307, 175, 39);
		NbrPresent.setIcon(new ImageIcon("C:\\Users\\Mariam Tiotio Berthe\\Desktop\\PFA\\image\\pp.png"));
		NbrPresent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFiel_present.setText("Nbr : "+nbr_present);

			}
		});
		NbrPresent.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(NbrPresent);
		
		textFiel_present = new JTextField();
		textFiel_present.setBounds(1145, 374, 57, 31);
		textFiel_present.setFont(new Font("Tahoma", Font.BOLD, 15));
		textFiel_present.setColumns(10);
		panel.add(textFiel_present);
		/**le nombre d'abscent après avoir fait la difference*/
		JButton NbrAbscent = new JButton("Nbr d'abscent");
		NbrAbscent.setBounds(1037, 431, 165, 39);
		NbrAbscent.setIcon(new ImageIcon("C:\\Users\\Mariam Tiotio Berthe\\Desktop\\PFA\\image\\abst.png"));
		NbrAbscent.setFont(new Font("Tahoma", Font.BOLD, 14));
		NbrAbscent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    nbr_abscent = nbr_inscrit - nbr_present;
				
			    textField_abscent.setText("Nbr : "+nbr_abscent);
			    	/**pour la lecture en arabe du nombre d'abscent*/
					Voice v = new Voice();
					v.callNumber(nbr_abscent);
				
			}
		});
		panel.add(NbrAbscent);
		
		textField_abscent = new JTextField();
		textField_abscent.setBounds(1145, 498, 57, 31);
		textField_abscent.setFont(new Font("Tahoma", Font.BOLD, 15));
		textField_abscent.setColumns(10);
		panel.add(textField_abscent);
		
		video = new JPanel();
		video.setBounds(0, 0, 1007, 632);
		panel.add(video);
		video.setLayout(null);
		
		lblNewLabel_image_fond = new JLabel("");
		lblNewLabel_image_fond.setIcon(new ImageIcon("C:\\Users\\Mariam Tiotio Berthe\\Desktop\\PFA\\image\\faciale-mitchell.jpg"));
		lblNewLabel_image_fond.setBounds(0, 0, 1007, 632);
		video.add(lblNewLabel_image_fond);
		
		btnQuitter = new JButton("Quitter");
		btnQuitter.setBounds(1057, 560, 145, 39);
		btnQuitter.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(btnQuitter);
	}
	 class Tracking implements Runnable {

	        protected volatile boolean runnable = false;

	        int x,y;
	        public void run() {
	        	Point P1=new Point(0,0),P2=new Point(0,0);
	        	double nbrvisage=0;
	        	double nbrfois=0;
	        	double ttl=20;
	            synchronized (this) {
	                while (runnable) {
	                    if (webSource.grab()) {
	                        try {
	                            webSource.retrieve(frame);
	                            Graphics g = video.getGraphics();
	                            
	                           faceDetector.detectMultiScale(frame, faceDetections);
	                           
	                           nbrvisage=nbrvisage+faceDetections.total();
	                           nbrfois=nbrfois+1;
	                            for ( Rect rect : faceDetections.toArray()) {
	                            	P1.x=rect.x;                P1.y=rect.y;
	                            	P2.x=rect.x + rect.width;   P2.y=rect.y + rect.height; 
	                            	Imgproc.rectangle(frame, P1, P2,new Scalar(0, 255,0)); //pour que ça dessine le rectangle pour chaque point
	                            }
	                            
	                            
	                      //  }
	                        if(nbrfois==ttl)
	                        {
	                        	nbr_present=(int)(Math.round(nbrvisage/ttl));
	                        //TNBRPersonne.setText("Nbr : "+Integer.toString((int)(Math.round(nbrvisage/ttl))));
	                        	nbrvisage=0;
	                        	nbrfois=0;
	                        }
                          
	                            
	                            Imgcodecs.imencode(".bmp", frame, mem);
	                            Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
	                          
	                            BufferedImage buff = new BufferedImage(im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_ARGB);
	                            buff = (BufferedImage) im;
	                            if (g.drawImage(buff, 0, 0, video.getWidth(), video.getHeight(), 0, 0, buff.getWidth(), buff.getHeight(), null)) {
	                                if (runnable == false) {
	                                    System.out.println("Paused ..... ");
	                                    this.wait();
	                                   
	                                }
	                            }
	                        } catch (Exception ex) {
	                            System.out.println("Error!!");
	                            ex.printStackTrace();
	                        }
	                    }
	                }
	            }
	        }
	    }	
}
