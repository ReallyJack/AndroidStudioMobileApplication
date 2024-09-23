package com.example.ttf;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Keep;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    AudioManager audioManager;

    @Keep
    public FirstFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        SeekBar seekBarTextSize = view.findViewById(R.id.seekBarTextSize);
        SeekBar seekBarBrightness = view.findViewById(R.id.seekBarBrightness);
        Switch switchSoundEffects = view.findViewById(R.id.switchSound);

        TextView titleText = view.findViewById(R.id.titleText);
        TextView sizeText = view.findViewById(R.id.textSizeText);
        TextView brightText = view.findViewById(R.id.brightText);
        TextView soundText = view.findViewById(R.id.soundText);

        SharedPreferences prefs = getActivity().getSharedPreferences("AppSettings", MODE_PRIVATE);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        int textSize = prefs.getInt("textSize", 16);
        int brightness = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 50);
        int brightnessPercentage = (brightness * 100) / 255;
        boolean soundEffects = prefs.getBoolean("soundEffects", true);

        seekBarTextSize.setProgress(textSize);
        seekBarBrightness.setProgress(brightnessPercentage);
        switchSoundEffects.setChecked(soundEffects);
        titleText.setTextSize(textSize);
        sizeText.setTextSize(textSize);
        brightText.setTextSize(textSize);
        soundText.setTextSize(textSize);

        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                titleText.setTextSize(progress);
                sizeText.setTextSize(progress);
                brightText.setTextSize(progress);
                soundText.setTextSize(progress);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("textSize", seekBarTextSize.getProgress());
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("brightness", seekBarBrightness.getProgress());
                editor.apply();

                if (Settings.System.canWrite(getActivity())) {

                    Settings.System.putInt(getActivity().getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
                    );

                    Settings.System.putInt(getActivity().getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS, progress
                    );

                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    startActivity(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        switchSoundEffects.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("soundEffects", switchSoundEffects.isChecked());
            editor.apply();

            if (isChecked) {
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
            }
        });

        return view;
    }
}