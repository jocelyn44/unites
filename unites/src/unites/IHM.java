package unites;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.CaretListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IHM extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTabbedPane PanOnglets = new JTabbedPane();
	JTextField resultat = new JTextField (1);
	JTextField entree = new JTextField(1);
	JComboBox<elemListe> choix2 = new JComboBox<elemListe>();
	
	public IHM(){
		this.setVisible(true);
		this.setTitle("JoCaf converter");
		GridLayout grille = new GridLayout(3,1);
	    this.setLayout(grille);
	    //on rend le champ resultat non editable 
	    resultat.setEditable(false);
	    
	    Element racineElement;
	    Document documentXML = null;
	    
	    // on parse le fichier de conf
	    try {
	    File file = new File("./conf.xml");
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docBuilderFactory.newDocumentBuilder();
		documentXML = docBuilder.parse(file);
	    } 
		catch (SAXException e) {
			e.printStackTrace();
			System.exit(0);
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(0);
		}
	    
	    racineElement = documentXML.getDocumentElement(); 
	    NodeList panList = racineElement.getElementsByTagName("pan");
	    
	    int nbPans=panList.getLength();
	    // pour chaque panneau
	    int i=0;
	    try {
		    for(i=0; i<nbPans; i++){
		    	Element pan = (Element)panList.item(i);
		    	NodeList unitList = pan.getElementsByTagName("unit");
		    	JPanel onglet = new JPanel();
		    	JComboBox<elemListe> choixOrigine = new JComboBox<elemListe>();
		    	JComboBox<elemListe> choixDestination = new JComboBox<elemListe>();
		    	int nbunits = unitList.getLength();
		    	// pour chaque unite du panneau
		    	for(int j=0;j<nbunits;j++){
		    		//on ajoute toutes les unit en tant qu'item
		    		Element unit =(Element)unitList.item(j);
		    		String valtxt=unit.getTextContent();
		    		float val=Float.parseFloat(valtxt);
		    		String name = unit.getAttribute("nom");
					choixOrigine.insertItemAt(new elemListe(val, name), j);
					choixDestination.insertItemAt(new elemListe(val, name), j);
					if(choixOrigine.getItemCount()>0){
				    	choixOrigine.setSelectedIndex(0);
				    	choixDestination.setSelectedIndex(0);
			    	}
					//listener permettant de faire la conversion automaitque quand on change d'unité  
					ItemListener actionChange = new ItemListener() {
				        public void itemStateChanged(ItemEvent arg0) {
				        	if(entree.getText()!=""){
				                float val= Float.parseFloat(entree.getText());
				                resultat.setText(String.valueOf(calcul(val)));
			                }
				        }
				    };
				    choixDestination.addItemListener(actionChange);
				    choixOrigine.addItemListener(actionChange);
				}
		    	
		    	onglet.setLayout(new GridLayout(2,2));
		    	onglet.add(new JLabel("Origine : "));
		    	onglet.add(choixOrigine);
		    	onglet.add(new JLabel("Destination : "));
		    	onglet.add(choixDestination);
		    	//on ajoute chaque panneau au tab pannel
		    	PanOnglets.addTab(pan.getAttribute("nom"), onglet);
		    }
		    //listener qui fait la conversion automatique au changement de valeur dans le champ d'entree 
	        CaretListener caretupdate = new CaretListener() {
	            public void caretUpdate(javax.swing.event.CaretEvent e) {
	                JTextField text = (JTextField)e.getSource();
	                if(text.getText()!=""){
		                float val= Float.parseFloat(text.getText());
		                resultat.setText(String.valueOf(calcul(val)));
	                }
	            }
	        };
		    
		    PanOnglets.setVisible(true);
		    PanOnglets.setSize(100, 100);
		    entree.addCaretListener(caretupdate);
		    	    
		    entree.setText("1");
		    resultat.setText("1");
		    
		    Container entreLab = new Container();
		    entreLab.setLayout(new GridLayout(2,1));
		    entreLab.add(new JLabel("Bienvenu dans cette application de conversion"));
		    entreLab.add(entree);
		    this.add(entreLab);	    
		    this.add(PanOnglets);
		    
		    Container resFooter= new Container();
		    resFooter.setLayout(new GridLayout(2,1));
		    resFooter.add(resultat);
		    resFooter.add(new JLabel("Application réalisée par Jocelyn MERLAUD et Charles Antoine FONVIELLE"));
		    this.add(resFooter);
		    this.setSize(70*i, 300);
	    }catch (Exception e){
	    	i=5;
	    	this.add(new JLabel("Fichier de configuration mal construit :"));
	    	this.add(new JLabel(e.toString()));
	    	this.setSize(500, 100);
	    }
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public float calcul(float entree){
		float res=0;
		JPanel panCourant = (JPanel)PanOnglets.getSelectedComponent();// getTabComponentAt(PanOnglets.getSelectedIndex());
		JComboBox<elemListe> origine = (JComboBox<elemListe>)panCourant.getComponent(1);
		JComboBox<elemListe> destination = (JComboBox<elemListe>)panCourant.getComponent(3);
		if(origine.getItemCount()>0)
			res = entree*(origine.getItemAt(origine.getSelectedIndex()).val / origine.getItemAt(destination.getSelectedIndex()).val);
		
		return res;
	}
	
}
