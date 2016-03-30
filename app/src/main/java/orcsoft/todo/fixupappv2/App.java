package orcsoft.todo.fixupappv2;

import com.evernote.android.job.JobManager;

import orcsoft.todo.fixupappv2.BackgroundWork.BgJobCreator;

public class App extends android.support.multidex.MultiDexApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new BgJobCreator());
    }
}
