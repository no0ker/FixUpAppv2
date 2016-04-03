package orcsoft.todo.fixupappv2;

import android.app.Application;

import com.evernote.android.job.JobManager;

import orcsoft.todo.fixupappv2.BackgroundWork.BgJobCreator;

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new BgJobCreator());
    }
}
