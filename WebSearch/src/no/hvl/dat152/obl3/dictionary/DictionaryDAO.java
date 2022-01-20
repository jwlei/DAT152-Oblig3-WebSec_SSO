package no.hvl.dat152.obl3.dictionary;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import no.hvl.dat152.obl3.util.ServerConfig;


public class DictionaryDAO {

	public static String DEFAULT_DICT_URL = ServerConfig.DEFAULT_DICT_URL;
	private String opted_root;
	
	public DictionaryDAO(String dicturl) {
		opted_root = dicturl;
	}

	public List<String> findEntries(String ord) throws Exception {

		String ordfil = opted_root + dictFile(ord.toLowerCase().charAt(0));
		String page = null;
		try {
			page = FileReaderUtil.getWebFile(ordfil);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		DictionaryParser parser = new DictionaryParser(page);
		List<String> oppforinger = parser.findMatchingEntries(ord);
		
		return oppforinger;
	}

	private String dictFile(char firstLetter) {
		return "wb1913_" + firstLetter + ".html";
	}

}
