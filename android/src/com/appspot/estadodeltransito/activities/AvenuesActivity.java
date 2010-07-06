package com.appspot.estadodeltransito.activities;

import android.content.Intent;

import com.appspot.estadodeltransito.service.StatusService;

public class AvenuesActivity extends HighwaysActivity {

	@Override
	protected Intent getServerIntent() {
		Intent i;
		i = new Intent(this, StatusService.class);
		i.setAction(StatusService.NEW_AVENUES_STATUS);
		return i;
	}
}
