package client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.jar.Attributes;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CatsClient {

	private static final String URL = "http://localhost:9082/ws/rest/cats";

	private static final String GET = "GET";
	private static final String POST = "POST";

	private static final String POST_CAT_1 = "post_cat_1.xml";

	public static void main(String[] args) throws Exception {
		getCats();
		getBritishShorthair();

		postCat1();

		getCats();
		getBritishShorthair();
		getCat1();
	}

	private static void getCats() throws Exception {

		System.out.println("Getting all the cats");

		HttpURLConnection connection = getConnection(URL, "GET");
		try {
			connection.connect();
			print(connection, "GET");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getBritishShorthair() throws Exception {

		System.out.println("Getting the British Shorthair cat");

		HttpURLConnection connection = getConnection(URL + "?name=British%20Shorthair", "GET");
		try {
			connection.connect();
			print(connection, GET);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getCat1() throws Exception {

		System.out.println("Getting the TestCat cat");

		HttpURLConnection connection = getConnection(URL + "?name=TestCat", GET);
		try {
			connection.connect();
			print(connection, GET);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void postCat1() throws Exception {

		System.out.println("Posting the TestCat cat");

		HttpURLConnection connection = getConnection(URL, POST);
		try {
			connection.setRequestProperty("payload", readXmlFileToString(getPathToPostCat1XmlFile()));
			connection.connect();
			print(connection, POST);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static HttpURLConnection getConnection(String urlString, String httpVerb) {

		HttpURLConnection connection = null;

		URL url;
		try {
			url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(httpVerb);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return connection;
	}

	private static void print(HttpURLConnection connection, String httpVerb) throws Exception {

		BufferedReader bufferedReader = null;
		String xml = "";
		String next = null;

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((next = bufferedReader.readLine()) != null) {
				xml += next;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Response : " + xml);


		if ("GET".equals(httpVerb)) {
			System.out.println("GET - parsed xml : ");
			parseXml(xml);
		}

	}

	private static String readXmlFileToString(String path) throws FileNotFoundException, IOException {

		String line = null;
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {

			while ((line = br.readLine()) != null) {
				sb.append(line.trim() + System.getProperty("line.separator"));
			}
		}

		String xml = sb.toString();
		xml = xml.replace("\n", "").replace("\r", "");

		// System.out.println("output xml : " + xml);

		return xml;

	}

	private static String getPathToPostCat1XmlFile() {

		String currentWorkingDir = System.getProperty("user.dir");
		String seperator = System.getProperty("file.separator");
		String path = currentWorkingDir + seperator + "src" + seperator + "main" + seperator + "java" + seperator
				+ "client" + seperator + POST_CAT_1;
		// System.out.println(path);
		return path;
	}

	private static void parseXml(String xml) throws Exception {
		SAXParser parser;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new ByteArrayInputStream(xml.getBytes()), new SaxParserHandler());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			throw new Exception("Aborting");
		}
	}

	static class SaxParserHandler extends DefaultHandler {
		char[] buffer = new char[1024];
		int n = 0;

		public void startElement(String uri, String lname, String qname, Attributes attributes) {
			clear_buffer();
		}

		public void characters(char[] data, int start, int length) {
			System.arraycopy(data, start, buffer, 0, length);
			n += length;
		}

		public void endElement(String uri, String lname, String qname) {
			if (Character.isUpperCase(buffer[0]))
				System.out.println(new String(buffer));
			clear_buffer();
		}

		private void clear_buffer() {
			Arrays.fill(buffer, '\0');
			n = 0;
		}
	}

}
