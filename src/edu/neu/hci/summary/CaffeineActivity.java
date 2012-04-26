package edu.neu.hci.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.hci.R;

public class CaffeineActivity extends Activity {
	private Button caffeineDoneBtn;
	private TextView tv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle class with XML layout
		setContentView(R.layout.summary_caffeine);
		tv = (TextView) findViewById(R.id.caffeineTV);
		Bundle bundle = this.getIntent().getExtras();
		int i = bundle.getInt("compare");
		if (i == 0)
			tv.setText("Reduce consumption of all caffeinated products. Yesterday you drank fewer caffeinated drinks than your average.");
		else
			tv.setText("Reduce consumption of all caffeinated products. Yesterday you drank more caffeinated drinks than your average.");
		
		// Bundle button in code with button in XML layout
		caffeineDoneBtn = (Button) findViewById(R.id.caffeineDoneBtn);
		caffeineDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// Set navigation, first parameter is source, second is target.
				i.setClass(CaffeineActivity.this, SleepSummaryMain.class);
				startActivity(i);

			}
		});
	}
}
