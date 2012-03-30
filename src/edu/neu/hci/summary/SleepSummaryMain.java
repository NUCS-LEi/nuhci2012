package edu.neu.hci.summary;

import edu.neu.hci.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class SleepSummaryMain extends TabActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary_tab_layout);
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabSpec spec;
		Intent intent; // Reusable Intent for each tab
		// TAB one
		intent = new Intent(this, SleepSummaryDetail.class);
		spec = tabHost.newTabSpec("tab1").setIndicator("Detail").setContent(intent);
		tabHost.addTab(spec);

		// TAB two
		intent = new Intent(this, SleepSummaryGraph.class);
		spec = tabHost.newTabSpec("tab2").setIndicator("Graph").setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}

}
