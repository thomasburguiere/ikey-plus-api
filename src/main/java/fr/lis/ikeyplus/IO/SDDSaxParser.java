package fr.lis.ikeyplus.IO;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fr.lis.ikeyplus.model.DataSet;
import fr.lis.ikeyplus.utils.Utils;


/**
 * This class starts the parsing of a SDD file
 * 
 * @author Florian Causse
 * @created 18-04-2011
 */
public class SDDSaxParser {

	// kwnoledge base (call dataset)
	private DataSet dataset = null;

	/**
	 * constructor which executes the parse method
	 */
	public SDDSaxParser(String uri, Utils utils) throws SAXException, IOException {
		XMLReader saxReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");

		SDDContentHandler handler = new SDDContentHandler(utils);
		saxReader.setContentHandler(handler);

		URL url = new URL(uri);
		InputSource is = null;
		is = new InputSource(url.openStream());

		saxReader.parse(is);
		this.setDataset(handler.getDataSet());
	}

	/**
	 * get the current dataset
	 * 
	 * @return DataSet, the current dataset
	 */
	public DataSet getDataset() {
		return dataset;
	}

	/**
	 * set the current dataset
	 * 
	 * @param DataSet
	 *            , the current dataset
	 */
	public void setDataset(DataSet dataset) {
		this.dataset = dataset;
	}

}