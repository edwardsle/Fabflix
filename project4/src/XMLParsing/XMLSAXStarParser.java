package XMLParsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import Models.Star;

public class XMLSAXStarParser extends DefaultHandler {

	List<Star> stars;

	private String tempVal;
	// to maintain context
	private Star tempStar;

	public XMLSAXStarParser() {
		stars = new ArrayList<Star>();
		parseDocument();
	}

	public void runExample() {
		parseDocument();
		printData();
	}

	private void parseDocument() {

		// get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			// get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			// parse the file and also register this class for call backs
			sp.parse("actors63.xml", this);

		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * Iterate through the list and print the contents
	 */
	private void printData() {
		System.out.println("No of Movies '" + stars.size() + "'.");

		Iterator<Star> it = stars.iterator();
		while (it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}

	// Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// reset
		tempVal = "";
		if (qName.equalsIgnoreCase("actor")) {
			// create a new instance of star
			tempStar = new Star();
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (qName.equalsIgnoreCase("actor")) {
			// add it to the list
			stars.add(tempStar);

		} else if (qName.equalsIgnoreCase("stagename")) {
			tempStar.setName(tempVal);
		} else if (qName.equalsIgnoreCase("dob")) {
			tempStar.setBirthYear(tempVal);
		}
	}

	public List<Star> getStars() {
		return stars;
	}

	public void setStars(List<Star> stars) {
		this.stars = stars;
	}
	
	public static void main(String[] args) {
		XMLSAXStarParser spe = new XMLSAXStarParser();
		spe.runExample();
	}

}
