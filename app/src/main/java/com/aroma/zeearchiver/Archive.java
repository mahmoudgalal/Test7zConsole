package com.aroma.zeearchiver;

import java.util.Vector;

import android.util.Log;

public class Archive {
	static
	{
		System.loadLibrary("7z");
		System.loadLibrary("test7zconsole");
		init();
	}
	public native void print5zInfo();
	static native void init();
	static native long getRamSize();
	public native void loadAllCodecsAndFormats();
	public native int listArchive(String archPath);
	public native int extractArchive(String archPath,String extractionPath,ExtractCallback callback);
	public native int createArchive(String archFullName,String[] itemsPaths,int length,int level,
			int dictionary,int wordSize,boolean orderMode,boolean solidDefined,long SolidBlockSize,
			String method,String encryptionMethod,int formatIndex,boolean encryptHeaders
			,boolean encryptHeadersAllowed , String pass,boolean multiThread,UpdateCallback callback);
	
	public Archive()
	{
		
	}
	
	
	private void loadAllCodecsFormats()
	{
		Log.i("libTest7ZConsole", "Calling loadAllCodecsFormats");
		loadAllCodecsAndFormats();
	}	
	
	
	private void addSupportedFormat(int libIndex,String name,boolean UpdateEnabled,
			boolean KeepName,String StartSignature,String mainExt,String exts)
	{
		ArchiveFormat af = new ArchiveFormat(libIndex, name, UpdateEnabled, 
				KeepName,mainExt, exts, StartSignature);
		supportedFormats.add(af);
	}
	
	private void addSupportedCodec(int clibIndex,long codecId,
			boolean codecEncoderIsAssigned,String codecName)
	{
		Codec codec = new Codec(clibIndex, codecId, codecEncoderIsAssigned, codecName);
		supportedCodecs.add(codec);
	}
	public Vector<ArchiveFormat> getSupportedFormats()
	{
		if(supportedFormats.isEmpty())
		{
			loadAllCodecsFormats();
		}
		return supportedFormats;
	}
	public Vector<Codec> getSupportedCodecs()
	{
		if(supportedCodecs.isEmpty())
		{
			loadAllCodecsFormats();
		}
		return supportedCodecs;
	}
	public static class ArchiveFormat
	{
		int libIndex;
		String name;
		//FormatExtensions[] exts;
		String mainExtension;
		String exts;
		boolean UpdateEnabled;
		boolean KeepName;
		String StartSignature;
		public ArchiveFormat(int libIndex,String name,boolean UpdateEnabled,boolean KeepName
				, String mainExt,String exts,String StartSignature)
		{
			this.libIndex=libIndex;
			this.name=name;
			this.UpdateEnabled=UpdateEnabled;
			this.KeepName=KeepName;
			this.mainExtension=mainExt;
			this.exts=exts;
			this.StartSignature=StartSignature;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return name;
		}
		/*class FormatExtensions
		{
			String Ext;
			String addExt;
			public FormatExtensions() {
				// TODO Auto-generated constructor stub
			}
			public FormatExtensions(String ext) {
				Ext=ext;
			}
			public FormatExtensions(String ext,String addext) {
				Ext=ext;addExt=addext;
			}
		}*/
	}
	
	public static class Codec
	{
		int codecLibIndex;
		long codecId;
		boolean codecEncoderIsAssigned;
		String codecName;
		
		public Codec(int clibIndex,long codecId,boolean codecEncoderIsAssigned,String codecName )
		{
			this.codecLibIndex=clibIndex;
			this.codecId=codecId;
			this.codecEncoderIsAssigned=codecEncoderIsAssigned;
			this.codecName=codecName;
		}
	}
	private Vector<ArchiveFormat> supportedFormats=new Vector<Archive.ArchiveFormat>();
	private Vector<Codec> supportedCodecs=new Vector<Archive.Codec>();
	

}
