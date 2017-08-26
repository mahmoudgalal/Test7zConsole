package com.aroma.zeearchiver;

public interface ExtractCallback {

	//Android GUI passward handlers;
	public void guiSetPassword(String pass);
	public String guiGetPassword();
	public boolean guiIsPasswordSet();
	//public ExtractCallback() ;
	public void beforeOpen(String name);
	public void extractResult(long result);
	public void openResult(String name, long result, boolean encrypted);
	public long thereAreNoFiles();
	long setPassword(String password);
	
	// IFolderOperationsExtractCallback
	public long askWrite(
		                 String srcPath,
		                 int srcIsFolder,
		                 long  srcTime,
		                 long srcSize,
		                 String destPathRequest,
		                 String destPathResult,
		                 int writeAnswer);
	public long setCurrentFilePath(String filePath,long numFilesCur);
	public long showMessage(String message);
	public long setNumFiles(long numFiles);
	//ICompressProgressInfo
	public long setRatioInfo(long inSize, long outSize);
	
	//IFolderArchiveExtractCallback
	public long askOverwrite(
		      String existName, long existTime, long existSize,
		      String newName, long newTime, long newSize,
		      int answer);
	public long prepareOperation(String name, boolean isFolder, int askExtractMode
			, long position);
	public long messageError(String message);
	public long setOperationResult(int operationResult,long numFilesCur, boolean encrypted);

	// ICryptoGetTextPassword	 
	public String cryptoGetTextPassword(String password);
	//IProgress
	public long setTotal(long total);
	public long setCompleted(long value);
	
	//IOpenCallbackUI
	public long open_CheckBreak();
	public long open_SetTotal(long numFiles /* numFiles */, long numBytes/* numBytes */);
	public long open_SetCompleted(long numFiles /* numFiles */, long numBytes/* numBytes */);
	public long open_CryptoGetTextPassword(String password);
	public long open_GetPasswordIfAny(String password);
	public boolean open_WasPasswordAsked();
	public void open_ClearPasswordWasAskedFlag();
	
	
	public void addErrorMessage(String message); 
	

}
