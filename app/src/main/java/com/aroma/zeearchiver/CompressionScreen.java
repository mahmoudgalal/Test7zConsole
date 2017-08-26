package com.aroma.zeearchiver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.aroma.zeearchiver.R;
import com.aroma.zeearchiver.Archive.ArchiveFormat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class CompressionScreen {

	static final String TAG = "libTest7ZConsole";
	static final int kNoSolidBlockSize = 0;
	static final int kSolidBlockSize = 64;
	private boolean isTaskDone =true;
	
	 View mainScreen;
	 //spinners
	 private Spinner archiveFormatSp,compressionLevelSp,compressionMethodSp,
	   dictionarySizeSp,wordSizeSp,solidBlockSizeSp,encryptionMethodSp;
	 
	 private Button browseArchive,compressOk,browsePath;
	 private TextView selectedFilesLabel,decompressionMem,compressionMem,selectedArchivePath;
	 
	 private EditText archiveName,password;
	 
	 private CheckBox encryptFileNames;
	 
	 Vector<Archive.ArchiveFormat> supportedFormats;
	 ArrayList<Integer> arcIndices;
	 ArrayList<String> selectedFiles;
	 String archivePath;
	 
	 boolean oneFile = true;
	 int formatIndex = 0;
	 CInfo info;
	 private String originalFileName;
	 //Adapters lists
	 ArrayList<FormatItem> formatsList;
	 ArrayList<SpinnerItem> levelList,methodList,encMethodList,dictList,wordSizeList
	 ,solidBlockSizeList;
	 // spinners Adapters
	 FormatsAdapter archiveFormatsAdapter;
	 SpinnerItemAdapter levelsAdapter,methodsAdapter,encryptionMethodsAdapter,dictAdapter
	 ,wordSizeAdapter,solidBlockAdapter;
	 
	 

	 String[] g_Levels = {
			 "Store",
			 "Fastest",
			 "",
			 "Fast",
			 "",
			 "Normal",
			 "",
			 "Maximum",
			 "",
			 "Ultra"
	 };
	 enum ELevel
	 {
	   kStore(0),
	   kFastest (1),
	   kFast(3),
	   kNormal(5),
	   kMaximum(7),
	   kUltra(9);
	   int value;
	   ELevel(int val)
	   {
		   value=val;
	   }	  
	 }
	 enum EEnum
	    {
	      kAdd,
	      kUpdate,
	      kFresh,
	      kSynchronize
	    }
	 enum EMethodID
	 {
	   kCopy,
	   kLZMA,
	   kLZMA2,
	   kPPMd,
	   kBZip2,
	   kDeflate,
	   kDeflate64,
	   kPPMdZip
	 }
	 
	 String kMethodsNames[] =
		 {
		   "Copy",
		   "LZMA",
		   "LZMA2",
		   "PPMd",
		   "BZip2",
		   "Deflate",
		   "Deflate64",
		   "PPMd"
		 };
	 EMethodID g_7zMethods[] =
		 {
			 EMethodID.kLZMA,
			 EMethodID.kLZMA2,
			 EMethodID.kPPMd,
			 EMethodID.kBZip2
		 };
	 EMethodID g_7zSfxMethods[] =
		 {
			 EMethodID.kCopy,
			 EMethodID.kLZMA,
			 EMethodID.kLZMA2,
			 EMethodID.kPPMd
		 };
	 EMethodID g_ZipMethods[] =
		 {
			 EMethodID.kDeflate,
			 EMethodID.kDeflate64,
			 EMethodID.kBZip2,
			 EMethodID.kLZMA,
			 EMethodID.kPPMdZip
		 };
	 EMethodID g_GZipMethods[] =
		 {
			 EMethodID.kDeflate
		 };
	 EMethodID g_BZip2Methods[] =
		 {
			 EMethodID.kBZip2
		 };
	 EMethodID g_XzMethods[] =
		 {
			 EMethodID.kLZMA2
		 };

	 CFormatInfo g_Formats[] =
		 {
			new CFormatInfo("", 
					 (1 << 0) | (1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9), 
					 null, 0,
					 false, false, false, false, false, false),
			new CFormatInfo("7z",
					(1 << 0) | (1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9),
					g_7zMethods, g_7zMethods.length,
					true, true, true, true, true, true),
			new CFormatInfo("Zip",
					(1 << 0) | (1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9),
					g_ZipMethods, g_ZipMethods.length,
					false, false, true, false, true, false),	
			new CFormatInfo("GZip",
					 (1 << 1) | (1 << 5) | (1 << 7) | (1 << 9),
					 g_GZipMethods, g_GZipMethods.length,
					 false, false, false, false, false, false),
			new CFormatInfo("BZip2",
					(1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9),
					g_BZip2Methods, g_BZip2Methods.length,
					false, false, true, false, false, false),
			new CFormatInfo("xz",
					(1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9),
					g_XzMethods, g_XzMethods.length,
					false, false, true, false, false, false),
			new CFormatInfo("Tar",
					(1 << 0),
					null, 0,
					false, false, false, false, false, false)	,
			new CFormatInfo("wim",
					(1 << 0),
					null, 0,
					false, false, false, false, false, false)		
		 };
	private int prevFormat;
	 
	public int Init(Context context)
	{
		LayoutInflater li=LayoutInflater.from(context);
		mainScreen=li.inflate(R.layout.activity_compress, null);
		if(mainScreen==null)
			return -1;
		g_Levels = mainScreen.getContext().getResources().getStringArray(R.array.compression_level_arr);
		//initializing adapters lists
		selectedFiles= new ArrayList<String>();
		formatsList= new ArrayList<FormatItem>();		
		arcIndices=new ArrayList<Integer>();
		levelList=new ArrayList<SpinnerItem>();
		methodList = new ArrayList<SpinnerItem>();
		encMethodList = new ArrayList<SpinnerItem>();
		dictList = new ArrayList<SpinnerItem>();
		wordSizeList = new ArrayList<SpinnerItem>();
		solidBlockSizeList = new ArrayList<SpinnerItem>();
		
		
		archiveFormatSp = (Spinner) mainScreen.findViewById(R.id.archive_format);
		compressionLevelSp = (Spinner) mainScreen.findViewById(R.id.compression_level);
		compressionMethodSp = (Spinner) mainScreen.findViewById(R.id.compression_method);
		dictionarySizeSp = (Spinner) mainScreen.findViewById(R.id.dict_size);
		wordSizeSp = (Spinner) mainScreen.findViewById(R.id.word_size);
		solidBlockSizeSp = (Spinner) mainScreen.findViewById(R.id.solid_block_size);
		encryptionMethodSp= (Spinner) mainScreen.findViewById(R.id.enc_method);
		
		selectedFilesLabel=(TextView) mainScreen.findViewById(R.id.archive_name);
		//selectedFilesLabel.setSingleLine(true);
		selectedFilesLabel.setEllipsize(TruncateAt.MARQUEE);
		selectedFilesLabel.setSelected(true);
		
		selectedArchivePath = (TextView) mainScreen.findViewById(R.id.archive_path);
		selectedArchivePath.setEllipsize(TruncateAt.MARQUEE);
		selectedArchivePath.setSelected(true);
		
		decompressionMem= (TextView) mainScreen.findViewById(R.id.m_use_decom);
		compressionMem= (TextView) mainScreen.findViewById(R.id.m_use_comp);
		
		encryptFileNames = (CheckBox) mainScreen.findViewById(R.id.enc_file_names);
		
		password = (EditText) mainScreen.findViewById(R.id.archive_password);
		archiveName = (EditText) mainScreen.findViewById(R.id.archive_label);
		originalFileName="archive";
		archiveName.setText(originalFileName);
		
		compressOk =  (Button)mainScreen.findViewById(R.id.compress_ok);
		compressOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onCompressOk();
			}
		});
		browseArchive= (Button)mainScreen.findViewById(R.id.browse_to);
		
		
		browseArchive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(mainScreen.getContext(), FileBrowserActivity.class);
				 intent.putExtra(FileBrowser.PICK_MODE_KEY, FileBrowser.BROWSE_MODE_SELECT);
				 ((CompressActivity)mainScreen.getContext()).
				 startActivityForResult(intent, CompressActivity.START_SELECT_REQUEST);
				
			}
		});
		
		browsePath = (Button)mainScreen.findViewById(R.id.browse_path);
		browsePath.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mainScreen.getContext(), FileBrowserActivity.class);
				 intent.putExtra(FileBrowser.PICK_MODE_KEY, FileBrowser.BROWSE_MODE_FOLDER);
				 ((CompressActivity)mainScreen.getContext()).
				 startActivityForResult(intent, CompressActivity.START_FOLDER_BROWSE);
			}
		});
		
		
		Archive arc=new Archive();
		supportedFormats=arc.getSupportedFormats();
		for(int i=0;i<supportedFormats.size();i++)
		{
			if(supportedFormats.get(i).name.equalsIgnoreCase("swfc")) 
				continue;
			if (supportedFormats.get(i).UpdateEnabled && 
					(oneFile || !supportedFormats.get(i).KeepName))
			{
		      arcIndices.add(i);
		      FormatItem fi=new FormatItem(supportedFormats.get(i).toString(),i);
		      formatsList.add(fi);
			}
		}
		if(arcIndices.size()==0)
			return -1;
		
		info = new CInfo();
		info.FormatIndex = arcIndices.get(0);
		
		archiveFormatsAdapter = new FormatsAdapter(mainScreen.getContext()
				, android.R.layout.simple_spinner_item, formatsList) ;
		archiveFormatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		archiveFormatsAdapter.setNotifyOnChange(true);
		archiveFormatSp.setAdapter(archiveFormatsAdapter);
		
		archiveFormatSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.i(TAG, "archiveFormatSp Selection Called");
				setLevel();
				setSolidBlockSize();
				checkControlsEnable();
				setArchiveName2(isSFX());
				setEncryptionMethod();
				setMemoryUsage();
				
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		archiveFormatSp.setSelection(0);
		
		
		
		levelsAdapter = new SpinnerItemAdapter(mainScreen.getContext(),
				android.R.layout.simple_spinner_item, levelList);
		levelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		levelsAdapter.setNotifyOnChange(true);
		compressionLevelSp.setAdapter(levelsAdapter);
		compressionLevelSp.setSelection(0);
		compressionLevelSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				setMethod();
				setSolidBlockSize();
				setMemoryUsage();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		methodsAdapter= new SpinnerItemAdapter(mainScreen.getContext(),
				android.R.layout.simple_spinner_item, methodList);
		
		methodsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		methodsAdapter.setNotifyOnChange(true);
		compressionMethodSp.setAdapter(methodsAdapter);
		compressionMethodSp.setSelection(0);
		compressionMethodSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				setDictionary();
				setOrder();
				setSolidBlockSize();
				setMemoryUsage();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		encryptionMethodsAdapter=new SpinnerItemAdapter(mainScreen.getContext(),
				android.R.layout.simple_spinner_item, encMethodList);
		encryptionMethodsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		encryptionMethodsAdapter.setNotifyOnChange(true);
		encryptionMethodSp.setAdapter(encryptionMethodsAdapter);
		
		
		dictAdapter = new SpinnerItemAdapter(mainScreen.getContext(),
				android.R.layout.simple_spinner_item, dictList);
		dictAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dictAdapter.setNotifyOnChange(true);
		dictionarySizeSp.setAdapter(dictAdapter);
		dictionarySizeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				setSolidBlockSize();
				setMemoryUsage();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		wordSizeAdapter = new SpinnerItemAdapter(mainScreen.getContext(),
				android.R.layout.simple_spinner_item, wordSizeList);
		wordSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		wordSizeAdapter.setNotifyOnChange(true);
		wordSizeSp.setAdapter(wordSizeAdapter);
		wordSizeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				setSolidBlockSize();
				setMemoryUsage();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		solidBlockAdapter = new SpinnerItemAdapter(mainScreen.getContext(),
				android.R.layout.simple_spinner_item, solidBlockSizeList);
		solidBlockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		solidBlockAdapter.setNotifyOnChange(true);
		solidBlockSizeSp.setAdapter(solidBlockAdapter);
		
		
		
		setLevel();
		setSolidBlockSize();
		
		setEncryptionMethod();
		setMemoryUsage();
		
		
		/**/
		return 0;
		
	}
	public boolean isMethodSupportedBySfx(EMethodID methodID)
	{
	  for (int i = 0; i < (g_7zSfxMethods.length); i++)
	    if (methodID == g_7zSfxMethods[i])
	      return true;
	  return false;
	}
	public int getFormatIndex()
	{
		return archiveFormatsAdapter.getItem(archiveFormatSp.getSelectedItemPosition())
				.formatIndex;
	}
	int getStaticFormatIndex()
	{
		int formatIndex = getFormatIndex();
		ArchiveFormat af=supportedFormats.get(formatIndex);
		for (int i = 0; i < g_Formats.length; i++)
		    if (af.name.equalsIgnoreCase(g_Formats[i].Name))
		      return i;
		return 0;
	}
	boolean IsZipFormat()
	{
		ArchiveFormat af=supportedFormats.get(getFormatIndex());
		return af.name.equalsIgnoreCase("zip");
	}
	
	void setNearestSelectComboBox(Spinner spinner, int value)
	{
		SpinnerItemAdapter adapter=(SpinnerItemAdapter) spinner.getAdapter();
	  for (int i = spinner.getCount() - 1; i >= 0; i--)
	    if (adapter.getItem(i).data <= value)
	    {
	      //comboBox.SetCurSel(i);
	      spinner.setSelection(i);
	      return;
	    }
	  if (spinner.getCount() > 0)
		  spinner.setSelection(0);
	    //comboBox.SetCurSel(0);
	}
	
	public void setLevel()
	{
		levelsAdapter.clear();
		levelList.clear();
		CFormatInfo fi = g_Formats[getStaticFormatIndex()];
		int level = ELevel.kNormal.value;
		int i;
		for (i = 0; i <= ELevel.kUltra.value; i++)
		  {
			if ((fi.LevelsMask & (1 << i)) != 0)
		    {
		      String levelInfo = g_Levels[i];		      
		      levelList.add(new SpinnerItem(levelInfo, i));
		    }
		  }
		levelsAdapter.notifyDataSetChanged();
		setNearestSelectComboBox(compressionLevelSp,level);
		setMethod();
	}
	
	int getMethodID()
	{
	  if (methodsAdapter.getCount() <= 0)
	    return -1;
	  return methodsAdapter.getItem(compressionMethodSp.getSelectedItemPosition()).data;
	  //.GetItemData(m_Method.GetCurSel());
	}
	
	String getMethodSpec()
	{
	  if (compressionMethodSp.getCount() <= 1)
	    return new String();
	  return kMethodsNames[getMethodID()];
	}
	
	void setMethod()
	{
		setMethod(-1);
	}
	
	public void setMethod(int keepMethodId)
	{
		methodsAdapter.clear();
		methodList.clear();
		int level = getLevel();
		if (level == 0)
		  {
		    setDictionary();
		    setOrder();
		    
		    return;
		  }
		CFormatInfo fi = g_Formats[getStaticFormatIndex()]	;
		String defaultMethod="";
		boolean isSfx = isSFX();
		boolean weUseSameMethod = false;
		for (int m = 0; m < fi.NumMethods; m++)
		  {
			EMethodID methodID = fi.MethodIDs[m];
			if (isSfx)
			      if (!isMethodSupportedBySfx(methodID))
			        continue;
			String method = kMethodsNames[methodID.ordinal()];
			methodList.add(new SpinnerItem(method, methodID.ordinal()));
			if (keepMethodId == methodID.ordinal())
		    {
				compressionMethodSp.setSelection(methodList.size()-1);
		         weUseSameMethod = true;
		      continue;
		    }
		    if ((defaultMethod.equalsIgnoreCase(method) || m == 0) && !weUseSameMethod)
		    	compressionMethodSp.setSelection(methodList.size()-1);
		  }
		methodsAdapter.notifyDataSetChanged();	
		if (!weUseSameMethod)
		  {
			Log.i(TAG, "weUseSameMethod is false");
		    setDictionary();
		    setOrder();
		  }
				
	}
	void setEncryptionMethod()	
	{
		encryptionMethodsAdapter.clear();
		encMethodList.clear();
		ArchiveFormat ai = supportedFormats.get(getFormatIndex());
		  if (ai.name.equalsIgnoreCase("7z"))
		  {
			  encMethodList.add(new SpinnerItem("AES-256", 0));
		    encryptionMethodSp.setSelection(0);
		  }
		  else if (ai.name.equalsIgnoreCase("zip"))
		  {
		    //int index = FindRegistryFormat(ai.Name);
		    String encryptionMethod="";
		    
		    encMethodList.add(new SpinnerItem("ZipCrypto",0));
		    encMethodList.add(new SpinnerItem("AES-256",1));
		    encryptionMethodSp.setSelection(encryptionMethod.contains("AES")? 1 : 0);
		  }
		
		encryptionMethodsAdapter.notifyDataSetChanged();
	}
	
	
	public boolean isSFX() {
		// TODO Auto-generated method stub
		return false;
	}
	boolean getOrderMode()
	{
	  switch (getMethodID())
	  {
	    case 3:// kPPMd:
	      return true;
	  }
	  return false;
	}
	int getOrderSpec() 
	{ 
		if (wordSizeList.size() <= 1)
		    return (int)-1;
		  return wordSizeAdapter.getItem(
				  wordSizeSp.getSelectedItemPosition()).data;
	}
	int getOrder() {
		 if (wordSizeSp.getCount() <= 0)
			    return (int)-1;
		 return wordSizeList.get(wordSizeSp.getSelectedItemPosition()).data;
		 // (UInt32)c.GetItemData(c.GetCurSel());		
	}
	int addOrder(int size)
	{	 
	   wordSizeList.add(new SpinnerItem(""+size, size));	
	   wordSizeAdapter.notifyDataSetChanged();
	  return wordSizeList.size()-1;
	}
	public void setOrder() {
		// TODO Auto-generated method stub
		wordSizeList.clear();
		wordSizeAdapter.clear();
		final ArchiveFormat ai = supportedFormats.get(getFormatIndex());
		 int defaultOrder = (int)-1;		 
		 int methodID = getMethodID();
		 int level = getLevel2();
		 if (methodID < 0)
		    return; 
		 
		 switch (methodID)
		  {
		    case 1://kLZMA:
		    case 2://kLZMA2:
		    {
		      if (defaultOrder == (int)-1)
		        defaultOrder = (level >= 7) ? 64 : 32;
		      for (int i = 3; i <= 8; i++)
		        for (int j = 0; j < 2; j++)
		        {
		          int order = (1 << i) + (j << (i - 1));
		          if (order <= 256)
		            addOrder(order);
		        }
		      addOrder(273);
		      setNearestSelectComboBox(wordSizeSp, defaultOrder);
		      break;
		    }
		    case 3://kPPMd:
		    {
		      if (defaultOrder == (int)-1)
		      {
		        if (level >= 9)
		          defaultOrder = 32;
		        else if (level >= 7)
		          defaultOrder = 16;
		        else if (level >= 5)
		          defaultOrder = 6;
		        else
		          defaultOrder = 4;
		      }
		      int i;
		      addOrder(2);
		      addOrder(3);
		      for (i = 2; i < 8; i++)
		        for (int j = 0; j < 4; j++)
		        {
		          int order = (1 << i) + (j << (i - 2));
		          if (order < 32)
		            addOrder(order);
		        }
		      addOrder(32);
		      setNearestSelectComboBox(wordSizeSp, defaultOrder);
		      break;
		    }
		    case 5://kDeflate:
		    case 6://kDeflate64:
		    {
		      if (defaultOrder == (int)-1)
		      {
		        if (level >= 9)
		          defaultOrder = 128;
		        else if (level >= 7)
		          defaultOrder = 64;
		        else
		          defaultOrder = 32;
		      }
		      int i;
		      for (i = 3; i <= 8; i++)
		        for (int j = 0; j < 2; j++)
		        {
		          int order = (1 << i) + (j << (i - 1));
		          if (order <= 256)
		            addOrder(order);
		        }
		      addOrder(methodID == EMethodID.kDeflate64.ordinal() ? 257 : 258);
		      setNearestSelectComboBox(wordSizeSp, defaultOrder);
		      break;
		    }
		    case 4://kBZip2:
		    {
		      break;
		    }
		    case 7://kPPMdZip:
		    {
		      if (defaultOrder == (int)-1)
		        defaultOrder = level + 3;
		      for (int i = 2; i <= 16; i++)
		        addOrder(i);
		      setNearestSelectComboBox(wordSizeSp, defaultOrder);
		      break;
		    }
		  } 
	}
	
	
	int getLevelSpec()  
	{ 
		if (levelList.size() <= 1)
		    return (int)-1;
		  return levelsAdapter.getItem(
				  compressionLevelSp.getSelectedItemPosition()).data;
	}
	
	public int getLevel()
	{
		if (compressionLevelSp.getCount() <= 0)
		    return (int)-1;
		  return levelsAdapter.getItem(compressionLevelSp.getSelectedItemPosition()).data;		
	}
	
	int getLevel2()
	{
	  int level = getLevel();
	  if (level == (int)-1)
	    level = 5;
	  return level;
	}
	
	
	class CFormatInfo
	{
		  String Name;
		  int LevelsMask;
		  EMethodID []MethodIDs;
		  int NumMethods;
		  boolean Filter;
		  boolean Solid;
		  boolean MultiThread;
		  boolean SFX;
		  boolean Encrypt;
		  boolean EncryptFileNames;
		  
		 public  CFormatInfo(String name,int levelsMask,EMethodID [] methodIDs,
				 int numMethods,boolean filter,boolean solid,boolean multiThread,boolean sFX,
				  boolean encrypt,boolean encryptFileNames)
		  {
			  Name=name;
			  LevelsMask=levelsMask;
			  MethodIDs=methodIDs;
			  NumMethods=numMethods;
			  Filter=filter;
			  Solid=solid;
			  MultiThread=multiThread;
			  SFX=sFX;
			  Encrypt=encrypt;
			  EncryptFileNames=encryptFileNames;
		  }
	}
	
	class CInfo
	  {
	    EEnum UpdateMode;
	    boolean SolidIsSpecified;
	    boolean MultiThreadIsAllowed;
	    long SolidBlockSize;
	    int NumThreads;

	    Vector<Long> VolumeSizes;

	    int Level;
	    String Method;
	    int Dictionary;
	    boolean OrderMode;
	    int Order;
	    String Options;

	    String EncryptionMethod;

	    boolean SFXMode;
	    boolean OpenShareForWrite;

	    
	    String ArchiveName; // in: Relative for ; out: abs
	    String CurrentDirPrefix;
	    boolean KeepName;

	    boolean GetFullPathName(String result) 
	    {
	    	return true;
	    }

	    int FormatIndex;

	    String Password;
	    boolean EncryptHeadersIsAllowed;
	    boolean EncryptHeaders;

	    void Init()
	    {
	      Level = Dictionary = Order =0;// UInt32(-1);
	      OrderMode = false;
	      Method="";
	      Options="";
	      EncryptionMethod="";
	    }
	    public CInfo()
	    {
	      Init();
	    }
	  }

	class FormatItem
	{
		String formatName;
		int formatIndex;
		public FormatItem(String fname,int findex)
		{
			formatName=fname;
			formatIndex=findex;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return formatName ;//+" "+this.formatIndex ;
		}
	}
	class FormatsAdapter extends ArrayAdapter<FormatItem>
	{

		public FormatsAdapter(Context context, int resource,
				List<FormatItem> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	
	class SpinnerItem
	{
		String name;
		int data;
		public SpinnerItem(String name,int data)
		{
			this.name=name;this.data=data;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.name ;//+" "+this.formatIndex ;
		}
	}
	class SpinnerItemAdapter extends ArrayAdapter<SpinnerItem>
	{

		public SpinnerItemAdapter(Context context, int resource,
				List<SpinnerItem> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}
		
	}
	private void updateFormatSpinner()
	{
		arcIndices.clear();
		//formatsList.clear();
		archiveFormatsAdapter.clear();
		for(int i=0;i<supportedFormats.size();i++)
		{
			if(supportedFormats.get(i).name.equalsIgnoreCase("swfc")) 
				continue;
			if (supportedFormats.get(i).UpdateEnabled && 
					(oneFile || !supportedFormats.get(i).KeepName))
			{
		      arcIndices.add(i);
		      FormatItem fi=new FormatItem(supportedFormats.get(i).toString(),i);
		      formatsList.add(fi);
			}
		}
		if(arcIndices.size()==0)
			return ;	
		
		
		//info = new CInfo();
		info.FormatIndex = arcIndices.get(0);
		archiveFormatsAdapter.notifyDataSetChanged();
		archiveFormatSp.setSelection(0);
		
		
		setLevel();
		setSolidBlockSize();
		checkControlsEnable();
		setArchiveName2(isSFX());
		setEncryptionMethod();
		setMemoryUsage();
		
		setMethod();
		
		setDictionary();
		setOrder();
		
		//compressionMethodSp.setSelection(0);
		
		Log.i("libTest7ZConsole", "info.FormatIndex:"+info.FormatIndex);
	}
	
	void setArchiveName2(boolean prevWasSFX)
	{
		String fileName = archiveName.getText().toString();
		ArchiveFormat prevArchiverInfo = supportedFormats.get(prevFormat);
		if (prevArchiverInfo.KeepName || info.KeepName)
		  {
			String prevExtension = prevArchiverInfo.mainExtension; 
			if (prevWasSFX)
			      prevExtension = "exe";
			else
			      prevExtension = new String(".") + prevExtension;
			int prevExtensionLen = prevExtension.length();
			if (fileName.length() >= prevExtensionLen)
			    if (fileName.endsWith(prevExtension))//.Right(prevExtensionLen).CompareNoCase(prevExtension) == 0)
			       fileName = fileName.substring(0, fileName.length() - prevExtensionLen);//.Left(fileName.length() - prevExtensionLen);
		  }
		
		setArchiveName(fileName);
	}
	
	void setArchiveName(String name)
	{
		String fileName = name;
		info.FormatIndex = getFormatIndex();
		ArchiveFormat af = supportedFormats.get(info.FormatIndex);
		prevFormat = info.FormatIndex;
		if(af.KeepName)
		{
			fileName = originalFileName;
		}
		else
		{
			if (!info.KeepName)
			{
				int dotPos = fileName.lastIndexOf('.');//.ReverseFind('.');
			    int slashPos =fileName.lastIndexOf('/');// MyMax(fileName.ReverseFind(WCHAR_PATH_SEPARATOR), fileName.ReverseFind('/'));
			      if (dotPos >= 0 && dotPos > slashPos + 1)
			        fileName = fileName.substring(0, dotPos);//.Left(dotPos);
			}
		}
		if (isSFX())
		    fileName += "exe";// kExeExt;
		  else
		  {
		    fileName += '.';
		    fileName += af.mainExtension;//GetMainExt();
		  }
		archiveName.setText(fileName);
	}
	
	public void setSelectedArchivePathText(String file)
	{
		archivePath=file;
		archivePath+=File.separator;
		selectedArchivePath.setText(archivePath);	
		updateFormatSpinner();
	}
	
	public void setSelectedFilesText(String[] files)
	{
		String text="";
		selectedFiles.clear();
		for(String st:files)
		  selectedFiles.add(st);
		if(files.length == 1)
		{
			File f=new File(files[0]);
			if(f != null && (!f.isDirectory()))	
			{
			    oneFile = true;	
			    originalFileName = f.getName();
			    archiveName.setText(f.getName());
			    
			}
			else
			{
				oneFile = false;
				originalFileName = f.getName();
				archiveName.setText(f.getName());
			}
		}
		else
		{
			File f=new File(files[0]).getParentFile();
			oneFile=false;
			originalFileName = f.getName();
			archiveName.setText(f.getName());
		}
		updateFormatSpinner();
		for(String st:files)
			text+=st+" ";
		selectedFilesLabel.setText(text);
	}
	static boolean IsAsciiString(String s)
	{
	  for (int i = 0; i < s.length(); i++)
	  {
	    char c = s.charAt(i);
	    if (c < 0x20 || c > 0x7F)
	      return false;
	  }
	  return true;
	}
	
	String GetEncryptionMethodSpec()
	{
	  if (encryptionMethodSp.getCount() <= 1)
	    return new String();
	  if (encryptionMethodSp.getSelectedItemPosition() <= 0)
	    return new String();
	  String result;
	  result=encryptionMethodSp.getSelectedItem().toString();//.GetText(result);
	  result=result.replace("-", "");
	  return result;
	}
	
	int getNumThreadsSpec()
	{
		return -1;
	}
	int getNumThreads2() 
	{ 
		int num = getNumThreadsSpec(); 
		if (num == (int)(-1)) 
			num = 1; 
		return num; 
	}
	
	long getMemoryUsage(int dictionary, DecompressMemory decompressMemory)
	{
		  decompressMemory.value = Long.MAX_VALUE;// UInt64(Int64(-1));
		  int level = getLevel2();
		  if (level == 0)
		  {
		    decompressMemory.value = (1 << 20);
		    return decompressMemory.value;
		  }
		  long size = 0;

		  final CFormatInfo fi = g_Formats[getStaticFormatIndex()];
		  if (fi.Filter && level >= 9)
		    size += (12 << 20) * 2 + (5 << 20);
		  int numThreads = getNumThreads2();
		  if (IsZipFormat())
		  {
		    int numSubThreads = 1;
		    if (getMethodID() == EMethodID.kLZMA.ordinal() && numThreads > 1 && level >= 5)
		      numSubThreads = 2;
		    int numMainThreads = numThreads / numSubThreads;
		    if (numMainThreads > 1)
		      size += (long)numMainThreads << 25;
		  }
		  int methidId = getMethodID();
		  switch (methidId)
		  {
		    case 1://kLZMA:
		    case 2://kLZMA2:
		    {
		      int hs = dictionary - 1;
		      hs |= (hs >> 1);
		      hs |= (hs >> 2);
		      hs |= (hs >> 4);
		      hs |= (hs >> 8);
		      hs >>= 1;
		      hs |= 0xFFFF;
		      if (hs > (1 << 24))
		        hs >>= 1;
		      hs++;
		      long size1 = (long)hs * 4;
		      size1 += (long)dictionary * 4;
		      if (level >= 5)
		        size1 += (long)dictionary * 4;
		      size1 += (2 << 20);

		      int numThreads1 = 1;
		      if (numThreads > 1 && level >= 5)
		      {
		        size1 += (2 << 20) + (4 << 20);
		        numThreads1 = 2;
		      }
		      int numBlockThreads = numThreads / numThreads1;
		      if (methidId == EMethodID.kLZMA.ordinal() || numBlockThreads == 1)
		        size1 += (long)dictionary * 3 / 2;
		      else
		      {
		        long chunkSize = (long)dictionary << 2;
		        chunkSize = Math.max(chunkSize, (long)(1 << 20));
		        chunkSize = Math.min(chunkSize, (long)(1 << 28));
		        chunkSize = Math.max(chunkSize, (long)dictionary);
		        size1 += chunkSize * 2;
		      }
		      size += size1 * numBlockThreads;

		      decompressMemory.value = dictionary + (2 << 20);
		      return size;
		    }
		    case 3://kPPMd:
		    {
		      decompressMemory.value = dictionary + (2 << 20);
		      return size + decompressMemory.value;
		    }
		    case 5://kDeflate:
		    case 6://kDeflate64:
		    {
		      int order = getOrder();
		      if (order == (int)-1)
		        order = 32;
		      if (level >= 7)
		        size += (1 << 20);
		      size += 3 << 20;
		      decompressMemory.value = (2 << 20);
		      return size;
		    }
		    case 4://kBZip2:
		    {
		      decompressMemory.value = (7 << 20);
		      long memForOneThread = (10 << 20);
		      return size + memForOneThread * numThreads;
		    }
		    case 7://kPPMdZip:
		    {
		      decompressMemory.value = dictionary + (2 << 20);
		      return size + (long)decompressMemory.value * numThreads;
		    }
		    
		  }
		  
		  return Long.MAX_VALUE;
	}
	
	
	
	int addDictionarySize(int size, boolean kilo, boolean maga)
	{
		String s="";
		 int sizePrint = size;
		  if (kilo)
		    sizePrint >>= 10;
		  else if (maga)
		    sizePrint >>= 20;
	     s+=sizePrint;
		 // TCHAR s[40];
		  //ConvertUInt32ToString(sizePrint, s);
		  if (kilo)
			  s+= " K";//lstrcat(s, TEXT(" K"));
		  else if (maga)
		    s+= " M";//lstrcat(s, TEXT(" M"));
		  else
		   s+= " "; //lstrcat(s, TEXT(" "));
		   s+= "B"; //lstrcat(s, TEXT("B"));
		   int index = dictList.size();
		   dictList.add(new SpinnerItem(s, size));
		   dictAdapter.notifyDataSetChanged();
		  //int index = (int)m_Dictionary.AddString(s);
		 // m_Dictionary.SetItemData(index, size);
		  return index;
	}
	
	int addDictionarySize(int size)
	{
	  if (size > 0)
	  {
	    if ((size & 0xFFFFF) == 0)
	      return addDictionarySize(size, false, true);
	    if ((size & 0x3FF) == 0)
	      return addDictionarySize(size, true, false);
	  }
	  return addDictionarySize(size, false, false);
	}
	
	public void setDictionary() {
		// TODO Auto-generated method stub
		Log.i("libTest7ZConsole", "Calling setDictionary() ");
		 dictAdapter.clear();
		 ArchiveFormat  ai = supportedFormats.get(getFormatIndex());		  
		 int defaultDictionary = (int)-1;
		 
		 int methodID = getMethodID();
		 int level = getLevel2();
		 Log.i("libTest7ZConsole", "methodID="+methodID);
		 if (methodID < 0)
		    return;
		  long maxRamSize = getMaxRamSizeForProgram();
		 /* EMethodID mID;
		  for(EMethodID methID:EMethodID.values())
			  if(methID.ordinal()==methodID)
			  {
				  mID = methID;
				  break;
			  }*/
			  
		  switch (methodID)
		  {
		    case 1://    kLZMA:
		    case 2://kLZMA2:
		    {
		    	 final int kMinDicSize = (1 << 16);
		         if (defaultDictionary == (int)-1)
		         {
		           if (level >= 9)      defaultDictionary = (1 << 26);
		           else if (level >= 7) defaultDictionary = (1 << 25);
		           else if (level >= 5) defaultDictionary = (1 << 24);
		           else if (level >= 3) defaultDictionary = (1 << 20);
		           else                 defaultDictionary = (kMinDicSize);
		         }
		         int i;
		         addDictionarySize(kMinDicSize);
		         dictionarySizeSp.setSelection(0);		        
		         for (i = 20; i <= 30; i++)
		           for (int j = 0; j < 2; j++)
		           {
		             if (i == 20 && j > 0)
		               continue;
		             int dictionary = (1 << i) + (j << (i - 1));
		             if (dictionary > (1 << 26))
		               continue;
		             addDictionarySize(dictionary);
		             DecompressMemory decomprSize=new DecompressMemory();
		             long requiredComprSize = getMemoryUsage(dictionary, decomprSize);
		             if (dictionary <= defaultDictionary && requiredComprSize <= maxRamSize)
		            	 dictionarySizeSp.setSelection(dictList.size() - 1);
		           }
		    }
		    	break;
		    case 3://kPPMd:
		    {
		      if (defaultDictionary == (int)-1)
		      {
		        if (level >= 9)      defaultDictionary = (192 << 20);
		        else if (level >= 7) defaultDictionary = ( 64 << 20);
		        else if (level >= 5) defaultDictionary = ( 16 << 20);
		        else                 defaultDictionary = (  4 << 20);
		      }
		      int i;
		      for (i = 20; i < 31; i++)
		        for (int j = 0; j < 2; j++)
		        {
		          if (i == 20 && j > 0)
		            continue;
		          int dictionary = (1 << i) + (j << (i - 1));
		          if (dictionary > (1 << 29))		            
		            continue;
		          addDictionarySize(dictionary);
		          DecompressMemory decomprSize=new DecompressMemory();
		          long requiredComprSize = getMemoryUsage(dictionary, decomprSize);
		          if (dictionary <= defaultDictionary && requiredComprSize <= maxRamSize || dictList.size() == 0)
		        	  dictionarySizeSp.setSelection(dictList.size() - 1);
		        }
		      setNearestSelectComboBox(dictionarySizeSp, defaultDictionary);
		      
		    }
		    break;
		  case 5://kDeflate:
		    {
		      addDictionarySize(32 << 10);
		      dictionarySizeSp.setSelection(0);
		      break;
		    }
		  case 6://kDeflate64:
		    {
		      addDictionarySize(64 << 10);
		      dictionarySizeSp.setSelection(0);
		      break;
		    }
		  case 4://kBZip2:
		    {
		      if (defaultDictionary == (int)-1)
		      {
		        if (level >= 5)
		          defaultDictionary = (900 << 10);
		        else if (level >= 3)
		          defaultDictionary = (500 << 10);
		        else
		          defaultDictionary = (100 << 10);
		      }
		      for (int i = 1; i <= 9; i++)
		      {
		        int dictionary = (i * 100) << 10;
		        addDictionarySize(dictionary);
		        if (dictionary <= defaultDictionary || dictionarySizeSp.getCount() == 0)
		        	dictionarySizeSp.setSelection(dictionarySizeSp.getCount() - 1);
		      }
		      break;
		    }
		  case 7://kPPMdZip:
		    {
		      if (defaultDictionary == (int)-1)
		        defaultDictionary = (1 << (19 + (level > 8 ? 8 : level)));
		      for (int i = 20; i <= 28; i++)
		      {
		        int dictionary = (1 << i);
		        addDictionarySize(dictionary);
		        DecompressMemory decomprSize =new DecompressMemory();
		        long requiredComprSize = getMemoryUsage(dictionary, decomprSize);
		        if (dictionary <= defaultDictionary && requiredComprSize <= maxRamSize || dictionarySizeSp.getCount() == 0)
		        	dictionarySizeSp.setSelection(dictionarySizeSp.getCount() - 1);
		      }
		      setNearestSelectComboBox(dictionarySizeSp, defaultDictionary);
		      break;
		    }
		    
		  }		
		  
		  dictAdapter.notifyDataSetChanged();
	}
	
	int getDictionarySpec() 
	{ 
		if (dictList.size() <= 1)
		    return (int)-1;
		  return dictAdapter.getItem(
				  dictionarySizeSp.getSelectedItemPosition()).data;
	}
	
	int getDictionary() 
	{ 
		 if (dictList.size() <= 0)
			    return (int)-1;
			  return dictAdapter.getItem(
					  dictionarySizeSp.getSelectedItemPosition()).data;
					  
	}
	
	int getBlockSizeSpec() 
	{
		if (solidBlockSizeList.size() <= 1)
		    return (int)-1;
		  return solidBlockAdapter.getItem(
				  solidBlockSizeSp.getSelectedItemPosition()).data;
	}
	
	void setSolidBlockSize()
	{
		solidBlockSizeList.clear();
		solidBlockAdapter.clear();
		final CFormatInfo fi = g_Formats[getStaticFormatIndex()];
		  if (!fi.Solid)
		    return;

		  int level = getLevel2();
		  if (level == 0)
		    return;

		  int dictionary = getDictionarySpec();
		  if (dictionary == (int)-1)
		    dictionary = 1;

		  int defaultBlockSize = (int)-1;
		  final ArchiveFormat ai = supportedFormats.get(getFormatIndex());
		  
		 // index = (int)m_Solid.AddString(LangString(IDS_COMPRESS_NON_SOLID, 0x02000D14));
		 // m_Solid.SetItemData(index, (UInt32)kNoSolidBlockSize);
		  solidBlockSizeList.add(new SpinnerItem("Non Solid", kNoSolidBlockSize));
		  solidBlockAdapter.notifyDataSetChanged();
		  solidBlockSizeSp.setSelection(0);
		  //m_Solid.SetCurSel(0);
		  boolean needSet = defaultBlockSize == -1;
		  for (int i = 20; i <= 36; i++)
		  {
		    if (needSet && dictionary >= (((long)1 << (i - 7))) && i <= 32)
		      defaultBlockSize = i;
		   // TCHAR s[40];
		    String s="";
		    s+= (1 << (i % 10));
		   // ConvertUInt32ToString(1 << (i % 10), s);
		    if (i < 30) 
		    	   s+= " M";//lstrcat(s, TEXT(" M"));
		    else        
		    	s+= " G";//lstrcat(s, TEXT(" G"));
		    s+=  "B";        //lstrcat(s, TEXT("B"));
		    solidBlockSizeList.add(new SpinnerItem(s, i));
		    solidBlockAdapter.notifyDataSetChanged();
		    //int index = (int)m_Solid.AddString(s);
		    //m_Solid.SetItemData(index, (UInt32)i);
		  }
		  //index = (int)m_Solid.AddString(LangString(IDS_COMPRESS_SOLID, 0x02000D15));
		  //m_Solid.SetItemData(index, kSolidBlockSize);
		  
		  solidBlockSizeList.add(new SpinnerItem("Solid", kSolidBlockSize));
		  solidBlockAdapter.notifyDataSetChanged();
		  if (defaultBlockSize == (int)-1)
		    defaultBlockSize = kSolidBlockSize;
		  if (defaultBlockSize != kNoSolidBlockSize)
		    setNearestSelectComboBox(solidBlockSizeSp, defaultBlockSize);
	}
	
	private long getMaxRamSizeForProgram() {
		// TODO Auto-generated method stub
		long physSize = Archive.getRamSize();
		Log.i("libTest7ZConsole", "RamSize="+physSize);
		if(physSize < 0)
		{
			physSize = Long.MAX_VALUE;
			Log.i(TAG, "Error,signed and unsigned error,resetting RamSize to max, RamSize="+physSize);
			
		}
		final long kMinSysSize = (1 << 24);
		  if (physSize <= kMinSysSize)
		      physSize = 0;
		  else
		    physSize -= kMinSysSize;
		  final long kMinUseSize = (1 << 24);
		  if (physSize < kMinUseSize)
		    physSize = kMinUseSize;
		  ActivityManager actm= (ActivityManager) mainScreen.getContext().
				  getSystemService(Context.ACTIVITY_SERVICE);
		  int memclass=actm.getMemoryClass();
		  Log.i(TAG, "Memory Class="+memclass+" ,physSize = "+physSize/1048576);
		  
		  return memclass*1024*1024;
		 // return physSize;
	}
	long getMemoryUsage(DecompressMemory decompressMemory)
	{
	  return getMemoryUsage(getDictionary(), decompressMemory);
	}
	void printMemUsage(TextView tv, long value)
	{
	  if (value == Long.MAX_VALUE)
	  {
	    tv.setText("?");
	    return;
	  }
	  value = (value + (1 << 20) - 1) >> 20;	  
	  //lstrcat(s, TEXT(" MB"));
	  tv.setText(value+" MB");
	}
	void setMemoryUsage()
	{
	  DecompressMemory decompressMem = new DecompressMemory();
	  long memUsage = getMemoryUsage(decompressMem);
	 
	  printMemUsage(decompressionMem,decompressMem.value);
	  printMemUsage(compressionMem,memUsage);
	 
	}
	void checkControlsEnable()
	{
		CFormatInfo fi = g_Formats[getStaticFormatIndex()];
		info.SolidIsSpecified = fi.Solid;
		boolean multiThreadEnable = fi.MultiThread;
		info.MultiThreadIsAllowed = multiThreadEnable;
		info.EncryptHeadersIsAllowed = fi.EncryptFileNames;
		solidBlockSizeSp.setEnabled(fi.Solid);
		
		encryptionMethodSp.setEnabled(fi.Encrypt);
		password.setEnabled(fi.Encrypt);
		encryptFileNames.setEnabled(fi.EncryptFileNames);
		
	}
	
	public void onCompressOk()
	{
		//showAlert("NOT IMPLEMENTED !!","Error");
		if(!isTaskDone)
			return;
		if(selectedFiles.isEmpty())
		{
			showAlert(mainScreen.getContext().getString(R.string.no_file_selected),null);
			return;
		}
		if(archivePath== null || archivePath.length()<2)
		{
			showAlert(mainScreen.getContext().getString(R.string.no_path_selected),null);
			return;
		}
		for(String selFile:selectedFiles)
		{
			File f= new File(selFile);
			if(!f.canRead())
			{
				showAlert(mainScreen.getContext().getString(R.string.file_not_accessible)+":\n"+selFile,
						mainScreen.getContext().getString(R.string.error));
				return;
			}
		}
		info.Password=password.getText().toString();
		if (IsZipFormat())
		  {
		    if (!IsAsciiString(info.Password))
		    {
		      //ShowErrorMessageHwndRes(*this, IDS_PASSWORD_USE_ASCII, 0x02000B11);
		    	showAlert(mainScreen.getContext().getString(R.string.pass_not_ascii),
		    			mainScreen.getContext().getString(R.string.error));
		      return;
		    }
		    String method = GetEncryptionMethodSpec();
		    method = method.toUpperCase(Locale.US);
		    if (method.contains("AES"))
		    {
		      if (info.Password.length() > 99)
		      {
		    	  showAlert(mainScreen.getContext().getString(R.string.pass_too_long),
		    			  mainScreen.getContext().getString(R.string.error));
		        //ShowErrorMessageHwndRes(*this, IDS_PASSWORD_IS_TOO_LONG, 0x02000B12);
		        return;
		      }
		    }
		  }
		
		String s=archiveName.getText().toString();
		s=s.trim();
		archivePath=archivePath.trim();		
		info.ArchiveName = archivePath+s;
		
		info.UpdateMode = EEnum.kAdd;//(m_UpdateMode.GetCurSel());
		
		info.Level = getLevelSpec();
		info.Dictionary = getDictionarySpec();
		info.Order = getOrderSpec();
		info.OrderMode = getOrderMode();
		info.NumThreads = getNumThreadsSpec();
		
		 int solidLogSize = getBlockSizeSpec();
		 info.SolidBlockSize = 0;
		 if (solidLogSize > 0 && solidLogSize != (int)-1)
		  info.SolidBlockSize = (solidLogSize >= 64) ? Long.MAX_VALUE : (1 << solidLogSize);
		 
		 info.Method = getMethodSpec();
		 info.EncryptionMethod = GetEncryptionMethodSpec();
		 info.FormatIndex = getFormatIndex();
		 info.SFXMode = isSFX();
		 info.OpenShareForWrite = false;// IsButtonCheckedBool(IDC_COMPRESS_SHARED);
		 
		 info.EncryptHeaders= encryptFileNames.isChecked();
		 	 
		 if(new File(info.ArchiveName).exists())
			{
			 
			 	showAlert(mainScreen.getContext().getString(R.string.overright_alarm)
			 			,mainScreen.getContext().getString(R.string.warning)
			 			,mainScreen.getContext().getString(R.string.yes),true
					 ,new DialogInterface.OnClickListener() {
			 		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					    if(which==DialogInterface.BUTTON_POSITIVE)
					    {
					    	new File(info.ArchiveName).delete();
					    	//info.ArchiveName = "\""+info.ArchiveName+"\"";
					    	UpdateTask updateTask = new UpdateTask(); 
							updateTask.execute(info);
					    }
					    else if(which==DialogInterface.BUTTON_NEGATIVE)
					    	return;					    
					}
			 	});
			 	
			}
		 else
		 {
			    // info.ArchiveName = "\""+info.ArchiveName+"\"";
			    UpdateTask updateTask = new UpdateTask(); 
				updateTask.execute(info); 
		 }
		 
	}
	
	void showAlert(String msg,String title)
	{
		 
		AlertDialog.Builder builder=new AlertDialog.Builder(mainScreen.getContext());
		builder.setMessage(msg).setPositiveButton(mainScreen.getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		if(title != null)
			builder.setTitle(title);
		builder.show();
	}
	void showAlert(String msg,String title,String okButTitle,boolean nobutton,DialogInterface.OnClickListener cl)
	{
		
		AlertDialog.Builder builder=new AlertDialog.Builder(mainScreen.getContext());
		builder.setMessage(msg).setPositiveButton(okButTitle, cl).create();
		if(title != null)
			builder.setTitle(title);
		if(nobutton)
			builder.setNegativeButton(mainScreen.getContext().getString(R.string.no), cl);
		 builder.show();
		// return dlistener.ret ;		 
	}
	
	
	
	class DecompressMemory
	{
		long value;
	}
	
	class UpdateProgressDialogView 
	{
		TextView currItem,percentage;
		View root;
		Context context;
		public UpdateProgressDialogView(Context context,int layout) {		
			
			this.context = context;
			root=LayoutInflater.from(context).inflate(layout, null);
			currItem=(TextView)root.findViewById(R.id.current_file);
			currItem.setEllipsize(TruncateAt.MARQUEE);
			currItem.setSelected(true);
			percentage = (TextView) root.findViewById(R.id.comp_ratio);
			
		}
		public View getRoot()
		{
			return root;
		}
		
		public void setCurrentItemText(String st)
		{
			currItem.setText(st);
		}
		public void setPercentage(String st)
		{
			percentage.setText(st);
		}
		public void setPercentage(long percent)
		{
			percentage.setText(context.getString(R.string.compression_ratio)+":"+percent+"%");
		}
		
	}
	
	class UpdateTask extends AsyncTask<CInfo, String, Void> implements UpdateCallback
	{
		AlertDialog pd;
		UpdateProgressDialogView view;
		long curBytes,totalBytes,totalFiles,curFiles,inSize,outSize;
		boolean bytesProgressMode=true;
		private PowerManager.WakeLock wl=null;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isTaskDone = false;
			//pd = new UpdateProgressDialog((CompressActivity)mainScreen.getContext());
			//pd.show();
			view =new UpdateProgressDialogView(mainScreen.getContext(), R.layout.progress_dialog);
			AlertDialog.Builder  builder=new Builder(mainScreen.getContext());
			pd = builder.setView(view.getRoot()).setTitle(mainScreen.getContext().getString(R.string.compressing))
					.setCancelable(false).create();
			pd.getWindow().setWindowAnimations(R.style.moving_dialog);
			//.getAttributes().windowAnimations = R.style.moving_dialog_theme;
			pd.show();
			//pd=ProgressDialog.show(mainScreen.getContext(), "Updating", "Creating Archive...",true,false);
			//pd.setView(view);
			PowerManager pm = (PowerManager) mainScreen.getContext().
					getSystemService(Context.POWER_SERVICE); 
			wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
			wl.acquire();
		}

		@Override
		protected Void doInBackground(CInfo... params) {
			// TODO Auto-generated method stub
			CInfo info = params[0];
			 Archive arc = new Archive();
			 int ret=arc.createArchive(info.ArchiveName, selectedFiles.toArray(new String[]{}), selectedFiles.size()
					 , info.Level, info.Dictionary, info.Order, info.OrderMode,info.SolidIsSpecified,
					 info.SolidBlockSize, info.Method, info.EncryptionMethod,
					 info.FormatIndex, info.EncryptHeaders,info.EncryptHeadersIsAllowed,info.Password,info.MultiThreadIsAllowed,this);
			if(ret != 0)
				publishProgress("-E","Error:code "+ret);
			 return null;
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if(values !=null)
			{
				if(values.length==1)
					view.setCurrentItemText(values[0]);
				else if(values.length==2)
				{
					if(values[0].equalsIgnoreCase("-R"))
					{
						long packSize = outSize,unpackSize= inSize;
						if(unpackSize != Long.MAX_VALUE && packSize!= Long.MAX_VALUE && unpackSize!= 0)
						{
							 long ratio = packSize * 100 / unpackSize;
							 // view.setPercentage("Compression Ratio:"+ratio+"%");
							  view.setPercentage(ratio);
						}
					}
					else if(values[0].equalsIgnoreCase("-P"))
					{
						long progressTotal = bytesProgressMode ? totalBytes : totalFiles;
						long progressCompleted = bytesProgressMode ? curBytes : curFiles;
						if (totalBytes == 0)
						      totalBytes = 1;
						int percentValue = (int)(curBytes * 100 / totalBytes);
						if(percentValue != Integer.MAX_VALUE)
						{
							pd.setTitle(mainScreen.getContext().getString(R.string.compressing)+
									" "+percentValue+"%");
							((Activity)(mainScreen.getContext())).setProgress(100*percentValue);
						}
					}
					else if(values[0].equalsIgnoreCase("-E"))
					{
						pd.setTitle(pd.getContext().getString(R.string.error));
						view.setCurrentItemText(values[1]);
						showAlert(values[1], mainScreen.getContext().getString(R.string.error));
					}
				}
			}
			//pd.set
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(wl!=null && wl.isHeld() )
			{
				Log.i(TAG,"Releasing WakeLock...");
				wl.release(); 
			}
			if(pd != null && pd.isShowing())
				pd.dismiss();
			((Activity)(mainScreen.getContext())).setProgress(Window.PROGRESS_END);
			
			isTaskDone = true;
				
		}

		@Override
		public void addErrorMessage(String message) {
			// TODO Auto-generated method stub
			publishProgress("-E",message);
		}

		@Override
		public long startArchive(String name, boolean updating) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long checkBreak() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long scanProgress(long numFolders, long numFiles, String path) {
			// TODO Auto-generated method stub
			totalFiles = numFiles;
			return 0;
		}

		@Override
		public long setNumFiles(long numFiles) {
			// TODO Auto-generated method stub
			totalFiles = numFiles;
			return 0;
		}

		@Override
		public long setTotal(long total) {
			// TODO Auto-generated method stub
			totalBytes = total;curBytes = 0;
			return 0;
		}

		@Override
		public long setCompleted(long completeValue) {
			// TODO Auto-generated method stub
			curBytes=completeValue;
			publishProgress("-P","");
			return 0;
		}

		@Override
		public long setRatioInfo(long inSize, long outSize) {
			// TODO Auto-generated method stub
			this.inSize = inSize;
			this.outSize =outSize;
			publishProgress("-R","");
			return 0;
		}

		@Override
		public long getStream(String name, boolean isAnti) {
			// TODO Auto-generated method stub
			Log.i(TAG, "Current File:"+name);
			publishProgress(name);
			return 0;
		}

		@Override
		public long setOperationResult(long operationResult) {
			// TODO Auto-generated method stub
			curFiles = operationResult;
			return 0;
		}

		@Override
		public long openCheckBreak() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long openSetCompleted(long numFiles, long numBytes) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
}
