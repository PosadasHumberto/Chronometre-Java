//librairies necessaires
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class Chrono extends JFrame{
	//Variables necessaires
	private JPanel container = new JPanel();								//conteneur principal de notre fenetre qui organisera les differents composants
	String[] buttonNames = {"Start", "Lap", "Stop", "Resume", "Reset"};		//les labels de buttons
	JButton[] buttons = new JButton[buttonNames.length];					//Creation d'un element pour chaque label
	private JLabel[] screen = new JLabel[4];								//distribution du labels dans le container, 1 pour le chrono et 3 pour les 3 laps
	private Dimension dimButton = new Dimension(90,70);						//stocke la dimmension des buttons
	private int lap = 0;													//permet de savoir combien de fois le button lap a ete actionné
	long initVar, nowVar, pauseDepart = 0, pauseFin = 0;					//variables qui vont permettre de calculer le temps et simuler le chrono
	long hour, minute, second, mili;										//variables qui vont permettre de formatter le timestamp en hh:mm:ss.mili
	private SwingWorker<Void, Integer> worker;								//SwingWorker va permettre de lancer le chrono en Background pour ne pas bloquer l'application
	
}
