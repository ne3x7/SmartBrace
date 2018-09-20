package ru.skoltech.smartbrace;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button update;
    private ProgressBar update_progress;
    private UpdateTask mTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        update = (Button) findViewById(R.id.update_button);
        update_progress = (ProgressBar) findViewById(R.id.update_progress);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptUpdate();
            }
        });
    }

    private void attemptUpdate() {
        if (mTask != null) {
            return;
        }

        showProgress(true);
        mTask = new UpdateTask(this);
        mTask.execute((Void) null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            update.setVisibility(show ? View.GONE : View.VISIBLE);
            update.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    update.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            update_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            update_progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    update_progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            update_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            update.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UpdateTask extends AsyncTask<Void, Void, Boolean> {

        private Activity mActivity;

        UpdateTask(Activity a) {
            mActivity = a;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            ImageView img = (ImageView) mActivity.findViewById(R.id.teeth_image);
            img.setImageDrawable(getDrawable(R.drawable.teeth_color));

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }
}
