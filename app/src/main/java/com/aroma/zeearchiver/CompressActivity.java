package com.aroma.zeearchiver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.Window;

public class CompressActivity extends Activity {

	private CompressionScreen compSc;
	final static int START_SELECT_REQUEST=1011;
	final static int START_FOLDER_BROWSE=1013;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_compress);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		//getWindow().setFeatureInt(Window.PROGRESS_START, 0);
		//getWindow().setFeatureInt(Window.PROGRESS_END,100);
		compSc=new CompressionScreen();
		compSc.Init(this);
		setContentView(compSc.mainScreen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.compress, menu);
		return true;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			
			switch(requestCode)
			{
				case START_SELECT_REQUEST:
				{
					String[] paths=data.getStringArrayExtra(FileBrowser.SELECTED_FILES);
					if(paths!=null)
						compSc.setSelectedFilesText(paths);
				}
					break;
				case START_FOLDER_BROWSE:
				{
					String path=data.getStringExtra(FileBrowser.SELECTED_FILE);
					if(path!=null)
						compSc.setSelectedArchivePathText(path);
				}
				 break;
				
			}
		}
	}

}
