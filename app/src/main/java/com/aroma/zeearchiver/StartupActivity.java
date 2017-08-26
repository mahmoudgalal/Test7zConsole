package com.aroma.zeearchiver;

import java.util.Random;

import com.aroma.zeearchiver.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils.TruncateAt;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class StartupActivity extends ActionBarActivity {

	private TextView  copyRightLbl ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_startup);
		copyRightLbl =(TextView) findViewById(R.id.pass_lbl);
		//copyRightLbl.setText("");
		copyRightLbl.setEllipsize(TruncateAt.MARQUEE);			
		copyRightLbl.setSelected(true);
		// copyRightLbl.setText("Copyright  2014 Mahmoud Galal");
		//Random r=new Random();	
		//copyRightLbl.setBackgroundColor(Color.rgb(r.nextInt(200), 22, r.nextInt(255)));
		
		Button startDecompress = (Button) findViewById(R.id.decompress);
		startDecompress.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StartupActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		Button startCompressor=(Button) findViewById(R.id.create_archive);
		startCompressor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(StartupActivity.this, CompressActivity.class);
				startActivity(intent);
			}
		});
		
		Button techInfo=(Button) findViewById(R.id.tech_info);
		techInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(StartupActivity.this, InfoActivity.class);
				startActivity(intent);
			}
		});
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Random r=new Random();	
		copyRightLbl.setSelected(true);
		copyRightLbl.setBackgroundColor(Color.rgb(r.nextInt(200), 22, r.nextInt(255)));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.startup, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
			case R.id.action_about_item:
				showAboutDialog();
			break;
			case R.id.action_share_item:
				share();
			break;
		}
		return true;//super.onOptionsItemSelected(item);
	}
	
	private void showAboutDialog()
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.about).setCancelable(true).setMessage(R.string.about_msg)
		.setPositiveButton(R.string.ok, null).create();
		builder.show();
	}
	private void share()
	{
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Zee Archiver is awesome !!");
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent,getString(R.string.action_share) + "..."));
	}

}
