//librairies necessaires
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class Chrono extends JFrame{
	//Variables necessaires
	private JPanel container = new JPanel();								//conteneur principal de notre fenetre qui organisera les differents composants
	String[] tab_string = {"Start", "Lap", "Stop", "Resume", "Reset"};		//les labels de buttons
	JButton[] tab_button = new JButton[tab_string.length];					//Creation d'un element pour chaque label
	private JLabel[] screen = new JLabel[4];								//distribution du labels dans le container, 1 pour le chrono et 3 pour les 3 laps
	private Dimension dimButton = new Dimension(90,70);						//stocke la dimmension des buttons
	private int Lap = 1;													//permet de savoir combien de fois le button lap a ete actionné
	long initVar, nowVar, pauseDepart = 0, pauseFin = 0;					//variables qui vont permettre de calculer le temps et simuler le chrono
	long hour, minute, second, mili;										//variables qui vont permettre de formatter le timestamp en hh:mm:ss.mili
	private SwingWorker<Void, Integer> worker;								//SwingWorker va permettre de lancer le chrono en Background pour ne pas bloquer l'application
	
	//Constructor
	public Chrono() {
		this.setSize(350, 435);
		this.setTitle("Chronometre");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		//initialiser le conteneur avec tous les composants
		initComposant();
		
		//Rajout du composant a la fenetre
		this.setContentPane(container);
		this.setVisible(true);
	}
	
	private void initComposant() {
		//definir les 2 sous conteneurs
		JPanel buttons = new JPanel();											//premier sous-conteneur
		buttons.setPreferredSize(new Dimension(320, 225));
		JPanel panScreen = new JPanel();										//deuxieme sous-conteneur
		panScreen.setPreferredSize(new Dimension(320, 230));
		panScreen.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		panScreen.setBackground(Color.white);
		
		//definir la police 
		Font police = new Font("Arial", Font.BOLD, 50);
		Font police2 = new Font("Arial", Font.BOLD, 25);
		
		//initialiser les labels avec le texte de depart, police et dimension et on les ajoute au sous conteneur correspondant
		for(int i = 0; i < 4; i++) {
			if(i == 0) {
				screen[i] = new JLabel("00:00:00.00");
				screen[i].setFont(police);
			} else {
				screen[i] = new JLabel("00:00:00.00");
				screen[i].setFont(police2);
			}
			
			//centrer les informations dans le JLabel
			screen[i].setHorizontalAlignment(JLabel.CENTER);
			screen[i].setPreferredSize(new Dimension(300, 50));
			panScreen.add(screen[i]);
		}
		
		//parcourir le tableau initialisé afin de créer nos boutons avec le texte, dimension et on les ajoute au sous conteneur
		for(int i = 0; i < tab_string.length; i++) {
			tab_button[i] = new JButton(tab_string[i]);
			tab_button[i].setPreferredSize(dimButton);
			buttons.add(tab_button[i]);
			if (i != 0)
				tab_button[i].setEnabled(false);
		}
		
		//definir pour chaque button l'Action Listener correspondant et son couleur
		tab_button[0].addActionListener(new StartListener());
		tab_button[0].setForeground(Color.RED);

		tab_button[1].addActionListener(new LapListener());
		tab_button[1].setForeground(Color.MAGENTA);
		
		tab_button[2].addActionListener(new StopListener());
		tab_button[2].setForeground(Color.BLUE);
		
		tab_button[3].addActionListener(new ResumeListener());
		tab_button[3].setForeground(Color.DARK_GRAY);
		
		tab_button[4].addActionListener(new ResetListener());
		tab_button[4].setForeground(Color.DARK_GRAY);
		
		//rajouter les deux sous conteneurs au conteneur principale container
		container.add(panScreen, BorderLayout.NORTH);
		container.add(buttons, BorderLayout.CENTER);
	}
	
	private void initializeWorker() {
		worker = new SwingWorker<Void, Integer>() {
			protected Void doInBackground() throws Exception {
				
				initVar = initVar + (pauseFin - pauseDepart);
				pauseDepart = pauseFin = 0;
				
				while (!isCancelled()) {
					nowVar = System.currentTimeMillis() - initVar;
					nowVar /= 10;
					mili = nowVar % 100;
					second = nowVar / 100;
					minute = second / 60;
					second = second % 60;
					hour = minute / 60;
					minute = minute % 60;
					screen[0].setText(String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second) + "." + String.format("%02d", mili));
					screen[0].paintImmediately(screen[0].getVisibleRect());
				}
				return null;
			}
		};		
	}
	
	//Listener affecté au boutton Start
	class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			initVar = System.currentTimeMillis();
			initializeWorker();
			worker.execute();
			tab_button[0].setEnabled(false);
			tab_button[1].setEnabled(true);
			tab_button[2].setEnabled(true);
			tab_button[4].setEnabled(true);
		}
	}
	
	//Listener affecté au button Lap
	class LapListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(Lap == 3) {
				tab_button[1].setEnabled(false);
				screen[Lap].setText(Lap + " : " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second) + "." + String.format("%02d", mili));
				screen[Lap].paintImmediately(screen[Lap].getVisibleRect());
				Lap++;				
			}
		}		
	}
	
	//Listener affecté au button Stop
	class StopListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			worker.cancel(true);
			pauseDepart = System.currentTimeMillis();
			tab_button[2].setEnabled(false);
			tab_button[3].setEnabled(true);
		}
	}
	
	//Listener affecté au button Resume
	class ResumeListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			pauseFin = System.currentTimeMillis();
			initializeWorker();
			worker.execute();
			tab_button[2].setEnabled(true);
			tab_button[3].setEnabled(false);
		}
	}
	
	//Listener affecté au button Reset
	class ResetListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			worker.cancel(true);
			hour = minute = second = mili = pauseDepart = pauseFin = 0;
			Lap = 1;
			for(int i = 0; i < 4; i++) {
				if(i == 0) {
					screen[i].setText("00:00:00.00");
				} else {
					screen[i].setText(i + ".- 00:00:00.00");
				}
				tab_button[i].setEnabled(false);
			}
			tab_button[0].setEnabled(true);
			tab_button[4].setEnabled(false);
		}
	}
}
























