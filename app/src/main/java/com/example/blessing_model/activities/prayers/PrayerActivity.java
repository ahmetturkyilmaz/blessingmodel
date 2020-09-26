package com.example.blessing_model.activities.prayers;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blessing_model.pojo.Prayer;
import com.example.blessing_model.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;

public class PrayerActivity extends AppCompatActivity {

    private HashMap<Integer, String> countZiqir;
    private Button counterButton;
    private TextView sureItself;
    private Button zeroButton;
    private TextView number;
    private Prayer prayer;
    private InterstitialAd mInterstitialAd;
    private Button reduceTextSizeButton;
    private Button increaseTextSizeButton;
    private Toolbar toolbar;
    private Button changeAlphabetPNGs;
    private Button changeAlphabetLatin;
    private Button changeAlphabetMeaning;
    private ImageView imageView;
    LinearLayout childLinearLayout;
    LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //ca-app-pub-4701964854424760/2932349785
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        define();
        loadData();
        try {
            setImagesToView();
        } catch (IOException e) {
            e.printStackTrace();

        }

        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                parentLinearLayout.setBackgroundColor("");
            case Configuration.UI_MODE_NIGHT_NO:
                parentLinearLayout.setBackgroundColor(000000);

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
        changeAlphabetPNGs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sureItself.setText("");

                    setImagesToView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        changeAlphabetMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < childLinearLayout.getChildCount(); i++) {

                    View subView = childLinearLayout.getChildAt(i);
                    if (subView instanceof ImageView) {
                        ImageView existingImageView = (ImageView) subView;
                        existingImageView.setImageBitmap(null);
                    }
                }
                sureItself.setText(prayer.getLatinAlphabet());
            }
        });
        changeAlphabetLatin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < childLinearLayout.getChildCount(); i++) {

                    View subView = childLinearLayout.getChildAt(i);
                    if (subView instanceof ImageView) {
                        ImageView existingImageView = (ImageView) subView;
                        existingImageView.setImageBitmap(null);
                    }
                }
                sureItself.setText(prayer.getBlessing());
            }
        });
        number.setText(countZiqir.get(Integer.parseInt(prayer.getSureId())));

        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter = Integer.parseInt(countZiqir.get(Integer.parseInt(prayer.getSureId())));
                counter++;
                countZiqir.put(Integer.parseInt(prayer.getSureId()), String.valueOf(counter));
                saveData();
                number.setText(String.valueOf(counter));
            }
        });
//        if (prayer.getLatinAlphabet() != null) {
//            sureItself.setText(prayer.getLatinAlphabet());
//
//        } else {
//            sureItself.setText(prayer.getBlessing());
//        }

        zeroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countZiqir.put(Integer.parseInt(prayer.getSureId()), "0");
                saveData();
                number.setText("0");
            }
        });

        textSize();
    }

    private void define() {
        counterButton = findViewById(R.id.counterButton);
        sureItself = findViewById(R.id.sureItself);
        zeroButton = findViewById(R.id.blessingZeroButton);
        number = findViewById(R.id.number);
        changeAlphabetLatin = findViewById(R.id.changeAlphabetToLatin);
        changeAlphabetMeaning = findViewById(R.id.changeAlphabetToMeanings);
        changeAlphabetPNGs = findViewById(R.id.changeAlphabetToPNGs);
        prayer = (Prayer) getIntent().getSerializableExtra("id");
        reduceTextSizeButton = findViewById(R.id.text_size_minus_button);
        increaseTextSizeButton = findViewById(R.id.text_size_plus_button);
        toolbar = findViewById(R.id.blessingPrayerToolbar);
        childLinearLayout = findViewById(R.id.prayerImages);
        parentLinearLayout = findViewById(R.id.parenLinearLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setImagesToView() throws IOException {
        ConstraintLayout constraintLayout = findViewById(R.id.prayerActivityParentLayout);
        childLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        if (childLinearLayout.getChildCount() != 0) {
            for (int i = 0; i < childLinearLayout.getChildCount(); i++) {

                View subView = childLinearLayout.getChildAt(i);
                if (subView instanceof ImageView) {
                    ImageView existingImageView = (ImageView) subView;
                    String imageNumb = prayer.getImageNumbs().get(i);
                    InputStream ims = getAssets().open("pngs/" + imageNumb);
                    Drawable d = Drawable.createFromStream(ims, null);
                    existingImageView.setImageDrawable(d);
                    ims.close();
                }
            }
        } else {
            for (String imageNumb : prayer.getImageNumbs()) {
                try {
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 280));
                    // get input stream
                    InputStream ims = getAssets().open("pngs/" + imageNumb);
                    // load image as Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                    imageView.setImageDrawable(d);
                    ims.close();
                    childLinearLayout.addView(imageView);

                    setContentView(constraintLayout);
                } catch (IOException ex) {
                    return;
                }

            }
        }
    }

    private void textSize() {
        reduceTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureItself.getTextSize() <= 300 && sureItself.getTextSize() >= 80) {
                    sureItself.setTextSize(TypedValue.COMPLEX_UNIT_PX, sureItself.getTextSize() - 8);
                }
            }
        });
        increaseTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureItself.getTextSize() <= 300 && sureItself.getTextSize() >= 80) {
                    sureItself.setTextSize(TypedValue.COMPLEX_UNIT_PX, sureItself.getTextSize() + 8);
                }
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("prayerPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(countZiqir);
        editor.putString("prayers", json);
        editor.apply();

    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("prayerPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("prayers", null);

        Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        countZiqir = gson.fromJson(json, type);
        if (countZiqir == null) {
            countZiqir = new HashMap<>();
            for (int i = 1; i < 115; i++) {
                countZiqir.put(i, "0");
            }
        }
    }
}