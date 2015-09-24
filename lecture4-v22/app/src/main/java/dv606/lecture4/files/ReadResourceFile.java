/**
 * ReadResourceFile.java
 * 
 * Example stolen from (or inspired by) The Busy Coder�s Guide to Android Development.
 * Modified to also read from a text file.
*/

package dv606.lecture4.files;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import dv606.lecture4.R;

public class ReadResourceFile extends ListActivity {
	private TextView selection;
	private ArrayList<String> items = new ArrayList<String>();
	private InputStream inStream = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.read_resource);
		selection=(TextView)findViewById(R.id.selection);
		
		try {
			//readText();      // Read from file res/raw/swe_words_text.txt
			//readXMLRaw();     // Read from file res/raw/swe_words_xml.xml
			readXMLResource();     // Read from file res/xml/swe_words_xml.xml
		}
		catch (Exception t) {
			Log.e("Exception", t.toString());
			t.printStackTrace();
			Toast.makeText(this, "Exception: "+t.toString(), 2000).show();
		}
		
		setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items));
	}
	
	public void onListItemClick(ListView parent, View v, int position,long id) {
		selection.setText(items.get(position).toString());
	}
	
	private void readXMLRaw() throws Exception {
		inStream = getResources().openRawResource(R.raw.swe_words_xml);

		/* Read XML file using a DOM parser */
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(inStream, null);
		NodeList words = doc.getElementsByTagName("word");

		for (int i=0;i<words.getLength();i++) {
			items.add(((Element)words.item(i)).getAttribute("value"));
		}
	}
	
	private void readXMLResource() throws Exception {
		/* Read XML file using XmlResourceParser */
		XmlResourceParser xpp = getResources().getXml(R.xml.swe_words_xml);
		while (xpp.getEventType() != XmlResourceParser.END_DOCUMENT) {
			if (xpp.getEventType() == XmlResourceParser.START_TAG)
				if (xpp.getName().equals("word"))
					items.add(xpp.getAttributeValue(0));
			xpp.next();
		}
	}

	private void readText() throws Exception {
		inStream = getResources().openRawResource(R.raw.swe_words_text);

		/* Read text file using a reader */
		BufferedReader buf = new BufferedReader( new InputStreamReader(inStream) );
		String line;
		do {
			line = buf.readLine();
			if (line != null)
				items.add(line);
		} while(line != null);
		
		inStream.close();
	}
}