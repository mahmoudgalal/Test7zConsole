package com.aroma.zeearchiver;

import java.util.List;

import com.aroma.zeearchiver.R;
import com.aroma.zeearchiver.Archive.ArchiveFormat;
import com.aroma.zeearchiver.Archive.Codec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FormatsCodecsAdapter extends ArrayAdapter<Archive.ArchiveFormat> {

	public FormatsCodecsAdapter(Context context,List<ArchiveFormat> objects) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		FormatListItem view =(FormatListItem)convertView;
		if(view==null)
		{
			view=new FormatListItem(getContext());
		}
		Archive.ArchiveFormat format=getItem(position);
		
		view.setName(format.name);
		view.setExtensions(format.exts);
		view.setSignature(format.StartSignature);
		view.setUpdatable(format.UpdateEnabled);
		view.setKeepName(format.KeepName);
		return view;//super.getView(position, convertView, parent);
	}

}

class FormatListItem extends LinearLayout
{
	private TextView name=null,extns=null,signature=null,updatable=null,keepName=null;
	
	public FormatListItem(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.formats_listitem, this);
		name=(TextView) findViewById(R.id.format_name);
		extns=(TextView) findViewById(R.id.format_ext);
		signature= (TextView) findViewById(R.id.format_sig);
		updatable = (TextView) findViewById(R.id.format_updatable);
		keepName =  (TextView) findViewById(R.id.format_keepname);
		// TODO Auto-generated constructor stub
	}
	
	public void setName(String fname)
	{
		name.setText(getContext().getString(R.string.format_name)+": "+fname);
	}
	public void setExtensions(String exts)
	{
		extns.setText(getContext().getString(R.string.format_extensions)+": "+exts);
	}
	public void setSignature(String sig)
	{
		String s="";
		for(char c:sig.toCharArray())
		{
			if(c > 0x20 && c < 0x80)
				{
				  s+=c;
				}
			else
			{
				s+=String.format("%x", (int)c);
			}
		}
		signature.setText(getContext().getString(R.string.format_signature)+": "+s);
	}
	public void setUpdatable(boolean updatable)
	{
		String formatUpdatable = getContext().getString(R.string.format_updatable);
		String value = updatable?getContext().getString(R.string.yes):
			getContext().getString(R.string.no);
		this.updatable.setText(formatUpdatable+": "+value);
	}
	public void setKeepName(boolean keepname)
	{
		String formatkeepname = getContext().getString(R.string.format_keepname);
		String value = keepname?getContext().getString(R.string.yes):
			getContext().getString(R.string.no);
		this.keepName.setText(formatkeepname+": "+value);
	}
	
}

class CodecListItem extends LinearLayout
{
	private TextView name=null,iD=null,encoderAssigned=null,libIndex=null;
	public CodecListItem(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.codecs_listitem, this);
		name=(TextView) findViewById(R.id.codec_name);
		iD=(TextView) findViewById(R.id.codec_id);
		encoderAssigned = (TextView) findViewById(R.id.codec_encoder_assigned); 
		libIndex = (TextView) findViewById(R.id.codec_libindex);
		// TODO Auto-generated constructor stub
	}
	
	public void setName(String name)
	{
		String coname=getContext().getString(R.string.codec_name);		
		this.name.setText(coname+": "+name);
		
	}	
	public void setID(String ID)
	{
		String coID=getContext().getString(R.string.codec_id);	
		this.iD.setText(coID+": "+ID);		
	}
	public void setEncoderAssigned(boolean assigned)
	{
		String encAssigned=getContext().getString(R.string.codec_encoder_assigned);
		String value=assigned?getContext().getString(R.string.yes):
			getContext().getString(R.string.no);
		this.encoderAssigned.setText(encAssigned+": "+value);		
	}
	public void setLibIndex(String libindex)
	{
		String coLibIndex=getContext().getString(R.string.codec_lib_index);
		this.libIndex.setText(coLibIndex+": "+libindex);		
	}
	
}

class CodecsAdapter extends ArrayAdapter<Archive.Codec>
{

	public CodecsAdapter(Context context, List<Codec> objects) {
		super(context, 0, objects);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CodecListItem view = (CodecListItem) convertView;
		if(view == null)
		{
			view =new CodecListItem(getContext());
		}
		Archive.Codec codec=getItem(position);
		view.setName(codec.codecName);
		view.setID(""+String.format("%x",codec.codecId));
		view.setEncoderAssigned(codec.codecEncoderIsAssigned);
		view.setLibIndex(codec.codecLibIndex+"");
		return view;//super.getView(position, convertView, parent);
	}
	
}





