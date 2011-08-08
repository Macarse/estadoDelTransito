package com.appspot.estadodeltransito.service.asyncTasks;

import java.util.LinkedList;

import android.app.Service;

import com.appspot.estadodeltransito.domain.highway.Highway;
import com.appspot.estadodeltransito.parsers.AvenuesParser;

public class AvenuesAsyncTask extends HighwaysAsyncTask {

    public static final String NEW_AVENUES_STATUS = "New_Avenues_Status";

    public AvenuesAsyncTask(Service service) {
        super(service, NEW_AVENUES_STATUS);
    }

    @Override
    protected String getFromServer(String gaeUrl) {
        return AvenuesParser.getUrlContent();
    }

    @Override
    protected LinkedList<Highway> getInstancesFromJson(String json) {
        return AvenuesParser.getHighways(json);
    }

}