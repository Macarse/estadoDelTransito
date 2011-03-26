package com.appspot.estadodeltransito.activities;

import android.content.Intent;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.service.StatusService;
import com.appspot.estadodeltransito.service.asyncTasks.AvenuesAsyncTask;

public class AvenuesActivity extends HighwaysActivity {

	@Override
	protected Intent getServerIntent() {
		Intent i;
		i = new Intent(this, StatusService.class);
		i.setAction(AvenuesAsyncTask.NEW_AVENUES_STATUS);
		return i;
	}

	@Override
	protected int getTitleId() {
	    return R.string.menu_avenues;
	}
}
