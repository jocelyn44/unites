package unites;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class unites {
	JFrame fenetre = new JFrame();
	JButton bouton = new JButton("convertir");
	JLabel resultat = new JLabel();
	JTextField nombreField = new JTextField(0);
	public unites (){       
	    
	    fenetre.setVisible(true);
	    fenetre.setSize(300, 500);
	    fenetre.setTitle("JoCaf converter");
	    
	    bouton.setText("convertir");
	    bouton.setSize(30, 80);
	    
	    fenetre.add(nombreField, BorderLayout.NORTH);
	    fenetre.add(bouton, BorderLayout.CENTER);
	    fenetre.add(resultat, BorderLayout.SOUTH);
	    
	    bouton.addActionListener(new boutonListener());
	  }
	
	class boutonListener implements ActionListener {

		public void actionPerformed(ActionEvent e){
			resultat.setText(String.valueOf((Integer.parseInt(nombreField.toString())*10)));
		}
		
	}
	
	public static void appBouton(){
		
	}
}
