package orcsoft.todo.fixupappv2.BackgroundWork;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class BgJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        if(BgJob.TAG.equals(tag)){
            return new BgJob();
        }
        return null;
    }
}
