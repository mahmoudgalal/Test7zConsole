package com.aroma.zeearchiver;

import java.util.Vector;

import com.aroma.zeearchiver.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.Menu;
import android.widget.ListView;

public class InfoActivity extends Activity {

	private ListView supportedFormatsList=null,supportedCodecsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
		supportedFormatsList=(ListView) findViewById(R.id.formats_list);
		supportedCodecsList= (ListView) findViewById(R.id.codecs_list);
		FormatsCodecsLoader task=new FormatsCodecsLoader();
		task.execute();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}
	class FormatsCodecsLoader extends AsyncTask<Void, Void, Void>
	{
		FormatsCodecsAdapter fadapter=null;
		CodecsAdapter cadapter=null;
		Vector<Archive.ArchiveFormat> formats=null;
		Vector<Archive.Codec> codecs=null;
       @Override
	    protected void onPostExecute(Void result) {
	    	// TODO Auto-generated method stub
	    	super.onPostExecute(result);
	    	fadapter=new FormatsCodecsAdapter(InfoActivity.this, formats);
	    	cadapter=new CodecsAdapter(InfoActivity.this, codecs);
	    	supportedFormatsList.setAdapter(fadapter);
	    	supportedCodecsList.setAdapter(cadapter);
	    	
	    }
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Archive arc=new Archive();
			formats=arc.getSupportedFormats();
			codecs=arc.getSupportedCodecs();
			return null;
		}
		
	}

}
