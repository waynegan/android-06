package com.deitel.doodlz;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;



@SuppressWarnings("deprecation")
// class for the Select Color dialog  

public class BackgroundColorDialogFragment extends DialogFragment
{
	private static final String TAG = "Doodle BackgroundColorDialogFragment";
   private SeekBar alphaSeekBar;
   private SeekBar redSeekBar;
   private SeekBar greenSeekBar;
   private SeekBar blueSeekBar;
   private View colorView;
   private int color;
   private ArrayList<Drawable> images ;
      private Gallery gallery;
     private Integer[] imageIDs = {
  		   R.drawable.pict1,
  		   R.drawable.pict2,
   		   R.drawable.pict3,
   		   R.drawable.pict4
      };
   
   // create an AlertDialog and return it
     @SuppressWarnings("deprecation")
   @Override
   public Dialog onCreateDialog(Bundle bundle)
   {
      AlertDialog.Builder builder = 
         new AlertDialog.Builder(getActivity());
      View colorDialogView = 
         getActivity().getLayoutInflater().inflate(
            R.layout.fragment_background, null);
      builder.setView(colorDialogView); // add GUI to dialog
      
      // set the AlertDialog's message 
      builder.setTitle(R.string.title_background_color_dialog);
      builder.setCancelable(true);               
      
      // get the color SeekBars and set their onChange listeners
      alphaSeekBar = (SeekBar) colorDialogView.findViewById(
         R.id.alphaBgSeekBar);
      redSeekBar = (SeekBar) colorDialogView.findViewById(
         R.id.redBgSeekBar);
      greenSeekBar = (SeekBar) colorDialogView.findViewById(
         R.id.greenBgSeekBar);
      blueSeekBar = (SeekBar) colorDialogView.findViewById(
         R.id.blueBgSeekBar);
      colorView = colorDialogView.findViewById(R.id.colorBgView);
      gallery = (Gallery) colorDialogView.findViewById(R.id.gallery);
      // register SeekBar event listeners
      alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
      redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
      greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);
      blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);
     
      // use current drawing color to set SeekBar values
      final DoodleView doodleView = getDoodleFragment().getDoodleView();
      color = doodleView.getDrawingScreenColor();
      alphaSeekBar.setProgress(Color.alpha(color));
      redSeekBar.setProgress(Color.red(color));
      greenSeekBar.setProgress(Color.green(color));
      blueSeekBar.setProgress(Color.blue(color));  
      blueSeekBar.setProgress(Color.blue(color));  
          
           gallery.setAdapter(new GalleryImageAdapter(getActivity()));
           gallery.setOnItemClickListener(onItemClickListener);
      
      // add Set Color Button
      builder.setPositiveButton(R.string.button_set_background_color,
         new DialogInterface.OnClickListener() 
         {
            public void onClick(DialogInterface dialog, int id) 
            {
            	doodleView.clear();
            	doodleView.setDrawingScreenColor(color); 
            	System.out.println("setDrawingScreenColor:"+color);
            	doodleView.invalidate();
            } 
         } 
      ); // end call to setPositiveButton
      
      return builder.create(); // return dialog
   } // end method onCreateDialog   
   
   // gets a reference to the DoodleFragment
   private DoodleFragment getDoodleFragment()
   {
      return (DoodleFragment) getFragmentManager().findFragmentById(
         R.id.doodleFragment);
   }
   
   // tell DoodleFragment that dialog is now displayed
   @Override
   public void onAttach(Activity activity)
   {
      super.onAttach(activity);
      DoodleFragment fragment = getDoodleFragment();
      
      if (fragment != null)
         fragment.setDialogOnScreen(true);
   }

   // tell DoodleFragment that dialog is no longer displayed
   @Override
   public void onDetach()
   {
      super.onDetach();
      DoodleFragment fragment = getDoodleFragment();
      
      if (fragment != null)
         fragment.setDialogOnScreen(false);
   }
   
   // OnSeekBarChangeListener for the SeekBars in the color dialog
   private OnSeekBarChangeListener colorChangedListener = 
     new OnSeekBarChangeListener() 
   {
      // display the updated color
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
         boolean fromUser) 
      {      
         if (fromUser) // user, not program, changed SeekBar progress
            color = Color.argb(alphaSeekBar.getProgress(), 
               redSeekBar.getProgress(), greenSeekBar.getProgress(), 
               blueSeekBar.getProgress());
         colorView.setBackgroundColor(color);
      } 
      
      @Override
      public void onStartTrackingTouch(SeekBar seekBar) // required
      {
      } 
      
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) // required
      {
      }
   }; // end colorChanged
   private void dismissDialog(){
	   	   this.dismiss();
	       }
   private OnItemClickListener onItemClickListener= new OnItemClickListener() {
	   		@Override
	   		public void onItemClick(AdapterView<?> parent, View view, int position,
	   				long id) {
	   			// TODO Auto-generated method stub
	   			Toast.makeText(getActivity().getBaseContext(),"pict" +  (position + 1) + " selected",
	   					Toast.LENGTH_SHORT).show();
	   			// display the images selected
	   			final DoodleView doodleView = getDoodleFragment().getDoodleView();
	   			doodleView.clear();
	           	doodleView.setDrawingScreenImage(imageIDs[position]); 
	           	System.out.println("setDrawingScreenColor:"+color);
	           	doodleView.invalidate();
	           	dismissDialog();
	   		}
	   	};
	      
	      	public class GalleryImageAdapter extends BaseAdapter {
	      		private Context context;
	      		private int itemBackground;
	      		public GalleryImageAdapter(Context c)
	      		{
	      			context = c;
	      			// sets a grey background; wraps around the images
	      			TypedArray a =context.obtainStyledAttributes(R.styleable.Gallery);
	      			itemBackground = a.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
	      			a.recycle();
	      		}
	      		// returns the number of images
	      		public int getCount() {
	      			return imageIDs.length;
	      		}
	      		// returns the ID of an item
	      		@Override
	      		public Object getItem(int position) {
	      			return position;
	      		}
	      		// returns the ID of an item
	      		@Override
	      		public long getItemId(int position) {
	      			return position;
	      		}
	      		// returns an ImageView view
	      		@Override
	      		public View getView(int position, View convertView, ViewGroup parent) {
	      			ImageView imageView = new ImageView(context);
	      			imageView.setImageResource(imageIDs[position]);
	      			//imageView.setImageDrawable(images.get(position));
	      			//imageView.setLayoutParams(new Gallery.LayoutParams(300, R.dimen.color_view_height));
	      			imageView.setBackgroundResource(itemBackground);
	      			return imageView;
	      		
	      	}
}} // end class ColorDialogFragment