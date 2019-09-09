package XMLParsing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class XMLSAXMovieParser extends DefaultHandler {

	HashMap<String, MovieWithGenre> myMovies;

    private String tempVal;
    //to maintain context
    private MovieWithGenre tempMovie;

    public XMLSAXMovieParser() {
        myMovies = new HashMap<String, MovieWithGenre>();
        parseDocument();
    }

    public void runExample() {
        parseDocument();
        printData();
    }
    
    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() {
    	System.out.println("No of Movies '" + myMovies.size() + "'.");
//
//        Iterator<GenreInMovie> it = myMovies.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next().toString());
//        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("film")) {
            //create a new instance of employee
            tempMovie = new MovieWithGenre();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("film")) {
            //add it to the list
            myMovies.put(tempMovie.getId(), tempMovie);

        } else if (qName.equalsIgnoreCase("t")) {
            tempMovie.setTitle(tempVal);
        } else if (qName.equalsIgnoreCase("fid")) {
        	tempMovie.setId(tempVal);
        } else if (qName.equalsIgnoreCase("year")) {
        	try {
        	tempMovie.setYear(Integer.parseInt(tempVal));
        	} catch (Exception e) {;}
        
        } else if (qName.equalsIgnoreCase("dirn")) {
        	tempMovie.setDirector(tempVal);
        } else if (qName.equalsIgnoreCase("cat")) {
        	tempMovie.setGenre(tempVal);
        } else if (qName.equalsIgnoreCase("year")) {
        	tempMovie.setYear(Integer.parseInt(tempVal));
        }
    }

    public static void main(String[] args) {
    	XMLSAXMovieParser spe = new XMLSAXMovieParser();
        spe.runExample();
    }

}
