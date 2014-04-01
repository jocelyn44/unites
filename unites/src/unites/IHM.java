package unites;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.ElementIterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IHM extends JFrame{
	JTabbedPane PanOnglets = new JTabbedPane();
	JTextField resultat = new JTextField (1);
	JTextField entree = new JTextField(1);
	JComboBox<elemListe> choix2 = new JComboBox<elemListe>();
	
	public IHM(){
		this.setVisible(true);
		this.setSize(300, 500);
		this.setTitle("JoCaf converter");
	    this.setLayout(new GridLayout(4,1));
	    
	    Element racineElement;
	    Document documentXML = null;
	    
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
	    for(int i=0; i<nbPans; i++){
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
			}
	    	onglet.setLayout(new GridLayout(2,2));
	    	onglet.add(new JLabel("Origine : "));
	    	onglet.add(choixOrigine);
	    	onglet.add(new JLabel("Destination : "));
	    	onglet.add(choixDestination);
	    	//on ajoute chaque panneau au tab pannel
	    	PanOnglets.addTab(pan.getAttribute("nom"), onglet);
	    	
	    	
	    }
	    
	    PanOnglets.setVisible(true);
	    PanOnglets.setSize(100, 100);
	    
	    this.add(new JLabel("Bienvenu dans cette application de conversion"));
	    this.add(entree);
	    this.add(PanOnglets);
	    this.add(resultat);
	    this.revalidate();
	    	    
	    //this.add(resultat);
	    
	    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
}
