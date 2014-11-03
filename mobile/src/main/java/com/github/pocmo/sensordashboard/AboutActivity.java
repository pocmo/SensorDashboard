package com.github.pocmo.sensordashboard;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class AboutActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new AboutFragment())
                    .commit();
        }

    }


    public static class AboutFragment extends Fragment
    {

        public AboutFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_about, container, false);

            ((TextView) rootView.findViewById(R.id.oss_content)).setText(Html.fromHtml(getResources().getString(R.string.about_open_source_content)));
            ((TextView) rootView.findViewById(R.id.oss_content)).setMovementMethod(LinkMovementMethod.getInstance());

            ((TextView) rootView.findViewById(R.id.about_github_content)).setText(Html.fromHtml(getResources().getString(R.string.about_github_content)));
            ((TextView) rootView.findViewById(R.id.about_github_content)).setMovementMethod(LinkMovementMethod.getInstance());


            ((TextView) rootView.findViewById(R.id.dev_content)).setText(Html.fromHtml(getResources().getString(R.string.about_development_content)));
            ((TextView) rootView.findViewById(R.id.dev_content)).setMovementMethod(LinkMovementMethod.getInstance());



            try
            {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                ((TextView) rootView.findViewById(R.id.header)).setText(getResources().getString(R.string.app_name) + " v." + pInfo.versionName);
            }
            catch (NameNotFoundException e)
            {
                e.printStackTrace();
            }


            return rootView;
        }
    }
}
