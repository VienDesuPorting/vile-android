package ru.visualandroid.vile;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;


public class MainActivity extends AppCompatActivity 
{	
	private SharedPreferences mPrefs = null;
    public static String SETTINGS_FOLDER_DEFAULT_KEY = null;
    private static final String LAST_DIRECTORY = "last_directory_key";
    private static File DEFAULT_LOCATION;
    public String  currentPath;
    private static RunAdapter ra;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		SETTINGS_FOLDER_DEFAULT_KEY = getString(R.string.settings_folder_default_key);
        DEFAULT_LOCATION = Environment2.getExternalSDCardDirectory() != null ?
                Environment2.getExternalSDCardDirectory() : Environment.getExternalStorageDirectory();
        File directory = getStartingDirectory();
         if (directory != null) 
                currentPath = directory.getAbsolutePath();
              
	    setContentView(R.layout.main);
	    mSaveDirBrowse = new FolderBrowserDialogWrapper(this, true, true);
	    createDirectoryBrowserDialog();      
	    final ActionBar actionBar = getSupportActionBar();
	    //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setTitle(R.string.app_name);
	    actionBar.setSubtitle("visual_android");
	    actionBar.setDisplayShowHomeEnabled(true);
 
	    final RecyclerView recylerView = (RecyclerView)findViewById(R.id.my_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        
        recylerView.setLayoutManager(layoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //final RunAdapter adapter = new RunAdapter(getDataSet());
                ra= new RunAdapter(getDataSet());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	recylerView.setAdapter(ra);
                    }
                });
            }
        }).start();
        
	}
	  private ArrayList<RunItem> getDataSet() {
	    	//File mCurrentDirectory = new File(Environment.getExternalStorageDirectory() + "/ons");
	    	File mCurrentDirectory = getStartingDirectory();
	    	File []   mDirectoryFiles = mCurrentDirectory.listFiles(new FileFilter() {
	              public boolean accept(File file) {
	                  return (!file.isHidden() && file.isDirectory());
	              }
	          });
	    	
	  //    Arrays.sort(mDirectoryFiles, new FileSort());
	    	
	      ArrayList<RunItem> mDataSet = new ArrayList();
	      int j=0 ;
	      for (int i=0 ; i<mDirectoryFiles.length ; i++){   
		        
	    	  File [] mDirectoryFiles2 = mDirectoryFiles[i].listFiles(new FileFilter() {
	                public boolean accept(File file) {
	                    return (file.isFile() && 
	                            (file.getName().equals("vilevn.pck")));
	                }
	            });
	    	  
	    	  if (mDirectoryFiles2.length != 0){
		    	    RunItem m = new RunItem();
		        	m.setTitle(mDirectoryFiles[i].getName());
		        	m.setPath(currentPath);
		        	mDataSet.add(j, m);
		        	j++;
	          }
	      }

	      return mDataSet;
	    }
    
    private File getStartingDirectory() {
        // Detect folder location if none is provided
        String path = mPrefs.getString(SETTINGS_FOLDER_DEFAULT_KEY, null);


        if (path == null) {
            // Check to see if there is a <internal storage>/ons
            File onsDefaultDir = new File(Environment.getExternalStorageDirectory() + "/ons");
            if (onsDefaultDir.exists()) {
                path = onsDefaultDir.getPath();
                mPrefs.edit().putString(SETTINGS_FOLDER_DEFAULT_KEY, path).apply();
            } else if (Environment2.hasExternalSDCard()) {
                // Check to see if there is a <extSdCard storage>/ons
                onsDefaultDir = new File(Environment2.getExternalSDCardDirectory() + "/ons");
                if (onsDefaultDir.exists()) {
                    path = onsDefaultDir.getPath();
                    mPrefs.edit().putString(SETTINGS_FOLDER_DEFAULT_KEY, path).apply();
                }
            }
        }
        // Set path unless it still can't find it, then set default folder
        File directory = null;
        if ( path != null && new File(path).exists()) {
            directory = new File(path);
        } else {
            directory = DEFAULT_LOCATION;
            mPrefs.edit().putString(SETTINGS_FOLDER_DEFAULT_KEY, DEFAULT_LOCATION.getPath()).apply();
        }
        if (!directory.exists()){
           // showError(getString(R.string.message_cannot_find_internal_storage));
            return null;
        }
        return directory;
    }
	
    // Launcher contributed by katane-san
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_version) {
            Toast.makeText(this,  R.string.version, Toast.LENGTH_SHORT).show();

			return true;
		}		
		
		if (id == R.id.dir_change) {
			         
			mSaveDirBrowse.show(currentPath);
			

			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}	
	
	private void createDirectoryBrowserDialog() {
		   AlertDialog.Builder builder = new Builder(this);
	        builder.setView(mSaveDirBrowse.getDialogLayout());
	        builder.setTitle("test");
	        builder.setPositiveButton(R.string.dialog_select_button_text, new OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	  Editor editor = mPrefs.edit();
	                  String path = mSaveDirBrowse.getResultDirectory().getPath();
	                  editor.putString(SETTINGS_FOLDER_DEFAULT_KEY, path);
	                  editor.apply();
	                  setPath(path);
	    			  ra.swapArray(getDataSet());
	    			  ra.notifyDataSetChanged();
	    			
	               // ExtSDCardFix.setSaveFolder(mSaveDirBrowse.getResultDirectory().getAbsolutePath());
	            }
	        });
	        builder.setNegativeButton(android.R.string.cancel, null);
	        mSaveDirBrowse.setDialog(builder.create());
    }
	
    protected void setPath(String path) {
          currentPath = path;
    }
    
    
    private FolderBrowserDialogWrapper mSaveDirBrowse = null;

}