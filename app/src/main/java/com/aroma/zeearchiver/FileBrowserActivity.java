package com.aroma.zeearchiver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;

public class FileBrowserActivity extends Activity {

	private FileBrowser fB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int browseMode=getIntent().getIntExtra(FileBrowser.PICK_MODE_KEY, FileBrowser.BROWSE_MODE_FILE);
		fB=new FileBrowser(this);	
		fB.setBrowsingMode(browseMode);
		setContentView(fB);//R.layout.activity_file_browser);		
		fB.initializeList();	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.file_browser, menu);
		return true;
	}
	
	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub    		
		setResult(Activity.RESULT_CANCELED);		
    	super.onBackPressed();
    }

	public void setSelectedFile(String absolutePath) {
		// TODO Auto-generated method stub
		Intent i=new Intent();
		i.putExtra(FileBrowser.SELECTED_FILE, absolutePath);
		setResult(Activity.RESULT_OK,i);
		finish();
	}

	public void switchForms() {
		// TODO Auto-generated method stub
		
	}
	public void setSelectedFiles(String[] files)
	{
		Intent i=new Intent();
		i.putExtra(FileBrowser.SELECTED_FILES, files);
		setResult(Activity.RESULT_OK,i);
		finish();
	}

	public void setSelectedExtractionPath(String currentPath) {
		// TODO Auto-generated method stub
		Intent i=new Intent();
		i.putExtra(FileBrowser.SELECTED_FILE, currentPath);
		setResult(Activity.RESULT_OK,i);
		finish();
		//setResult(Activity.RESULT_OK);
	}

}
