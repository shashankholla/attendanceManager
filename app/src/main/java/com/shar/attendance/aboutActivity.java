package com.shar.attendance;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shar.attendance.adapters.DrawerItemCustomAdapter;
import com.shar.attendance.models.DataModel;

public class aboutActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView otherApps = (TextView) findViewById(R.id.otherApps);
        String linkText = "<a href=\"https://play.google.com/store/apps/developer?id=SHAR\" >Other Apps</a>";
        otherApps.setText(Html.fromHtml(linkText));
        otherApps.setMovementMethod(LinkMovementMethod.getInstance());

        TextView sourceCode = (TextView) findViewById(R.id.sourceCode );
        String linkText2 = "<a href=\"https://github.com/shashankholla/attendanceManager\" >Source Code</a>";
        sourceCode.setText(Html.fromHtml(linkText2));
        sourceCode.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public int getStatusBarHeight() {
        Rect rect = new Rect();
        Window window = this.getWindow();
        if (window != null) {
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            android.view.View v = window.findViewById(Window.ID_ANDROID_CONTENT);

            android.view.Display display = ((android.view.WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            //return result title bar height
            return display.getHeight() - v.getBottom() + rect.top;
        }
        return 0;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("About Us");
        toolbar.setPadding(0, 70,0,0);

        ImageButton b = findViewById(R.id.info);
        b.setVisibility(View.GONE);

    }
}
