package utilities;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.xmlbeans.XmlException;

import eu.datex2.schema.x10.x10.D2LogicalModelDocument;
import eu.datex2.schema.x10.x10.D2LogicalModelDocument.Factory;

public class DataDownloader {
	private String savePath;
	private String url;
	private D2LogicalModelDocument document;

	public DataDownloader(String savePath, String url, D2LogicalModelDocument document) {
		this.savePath = savePath;
		this.url = url;
		this.document = document;
	}

	/**
	 * DataDownloader constructor
	 * @param savePath the path where the file is saved
	 * @param url the url where the DATEX2 document is fetched
	 */
	public DataDownloader (String savePath, String url) {
		this.savePath = savePath;
		this.savePath = "D:\\Documentos\\Mis docs\\Estudios\\Grado Uv\\Universidad 4\\tfg\\eclipse-workspace\\EtrafficMobile\\src\\main\\java\\content.xml";
		this.url = url;
	}
	
	/**
	 * DataDownloader constructor with default values
	 */
	public DataDownloader () {
		this.savePath = "D:\\Documentos\\Mis docs\\Estudios\\Grado Uv\\Universidad 4\\tfg\\eclipse-workspace\\EtrafficMobile\\src\\main\\java\\content.xml";
		this.url = "http://infocar.dgt.es/datex2/dgt/SituationPublication/all/content.xml";
	}
	
	/**
	 * @return the savePath
	 */
	public String getSavePath() {
		return savePath;
	}
	
	/**
	 * @param savePath the savePath to set
	 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return the document
	 */
	public D2LogicalModelDocument getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(D2LogicalModelDocument document) {
		this.document = document;
	}
	
	/**
	 * Function that downloads the DATEX2 file
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void downloadFile() throws MalformedURLException, IOException {
		ReadableByteChannel rc = Channels.newChannel(new URL(this.url).openStream());
		try (FileOutputStream fos = new FileOutputStream(this.savePath)) {
			FileChannel writeChannel = fos.getChannel();
			writeChannel.transferFrom(rc, 0, Long.MAX_VALUE);
		}
	}
	
	/**
	 * Function that parses the XML DATEX2 file in to a D2LogicalModelDocument object
	 * @throws IOException
	 */
	public void loadFile() throws IOException{
		String content = new String(Files.readAllBytes(Paths.get(this.savePath)));
		try {
			document = Factory.parse(content);
		} catch (XmlException e) {
			System.out.println("Could not parse the Datex2 document");
			e.printStackTrace();
		}
	}
	
	public static void main(final String[] args) throws MalformedURLException, IOException {
		DataDownloader dd = new DataDownloader("src/main/java/content.xml","http://infocar.dgt.es/datex2/dgt/SituationPublication/all/content.xml");
		dd.downloadFile();
	}
}


