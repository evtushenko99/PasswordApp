package com.example.passwordlayout;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private final static int MIN_LENGTH = 8;
    private final static String PASSWORD_LENGTH = "Длина: " + MIN_LENGTH;

    private TextView mResultTextView;
    private TextView mQualityText;
    private TextView mSeekBarHelperTextView;
    private TextView mGeneratedPasswordText;

    private EditText mSourceTextView;
    private PasswordsHelper mHelper;
    private View mCopyButton;
    private View mCopyGeneratedPasswordButton;

    private CompoundButton mCheckBoxUpperCase;
    private CompoundButton mCheckBoxNumbers;
    private CompoundButton mCheckBoxSpecialCharacters;
    private ArrayList<Integer> mAdditions;
    private Button mGeneratePasswordButton;
    private SeekBar mSeekBar;

    private ImageView mQuality;
    private int mLength;
    private StringBuilder mTextSeekBarString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLength = MIN_LENGTH;

        mTextSeekBarString = new StringBuilder();

        mAdditions = new ArrayList<>(4);
        mAdditions.add(0);

        mHelper = new PasswordsHelper(
                getResources().getStringArray(R.array.russian),
                getResources().getStringArray(R.array.english)
        );


        mResultTextView = findViewById(R.id.result_text);
        mSourceTextView = findViewById(R.id.source_text);
        mSeekBarHelperTextView = findViewById(R.id.text_seekBar_helper);
        mGeneratedPasswordText = findViewById(R.id.result_text_generated_password);

        mCopyButton = findViewById(R.id.button_copy);
        mCopyGeneratedPasswordButton = findViewById(R.id.button_copy_generated_password);
        mGeneratePasswordButton = findViewById(R.id.button_generated_password);

        mQuality = findViewById(R.id.quality);
        mQualityText = findViewById(R.id.quality_text);

        mCheckBoxUpperCase = findViewById(R.id.check_uppercase);
        mCheckBoxNumbers = findViewById(R.id.check_numbers);
        mCheckBoxSpecialCharacters = findViewById(R.id.check_special_characters);
        mSeekBar = findViewById(R.id.seekBar);

        mSeekBarHelperTextView.setText(PASSWORD_LENGTH);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTextSeekBarString.delete(0, mTextSeekBarString.length());
                mTextSeekBarString.append(getResources().getString(R.string.length_format, MIN_LENGTH, i,
                        getResources().getQuantityString(R.plurals.symbols_plurals, i), MIN_LENGTH + i, getResources().getQuantityString(R.plurals.symbols_plurals, MIN_LENGTH + i)));
                mSeekBarHelperTextView.setText(mTextSeekBarString.toString());
                mLength = MIN_LENGTH;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mLength += mSeekBar.getProgress();
            }
        });

        mCopyButton.setEnabled(false);
        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                assert manager != null;
                manager.setPrimaryClip(ClipData.newPlainText(
                        MainActivity.this.getString(R.string.clipboard_title),
                        mResultTextView.getText()
                ));
                Toast.makeText(MainActivity.this, "Скопировано в буффер обмена", Toast.LENGTH_SHORT).show();
            }
        });
        mCopyGeneratedPasswordButton.setEnabled(false);
        mCopyGeneratedPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                assert manager != null;
                manager.setPrimaryClip(ClipData.newPlainText(
                        MainActivity.this.getString(R.string.clipboard_title),
                        mGeneratedPasswordText.getText()
                ));
                Toast.makeText(MainActivity.this, "Скопировано в буффер обмена", Toast.LENGTH_SHORT).show();
            }
        });
        mSourceTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mResultTextView.setText(mHelper.convert(charSequence));
                mCopyButton.setEnabled(charSequence.length() > 0);
                int mGetQuality = mHelper.getQuality(charSequence);
                mQuality.setImageLevel((mGetQuality * 1000));
                mQualityText.setText(getResources().getStringArray(R.array.qualities)[mGetQuality]);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mCheckBoxUpperCase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mAdditions.indexOf(1) == -1) {
                    mAdditions.add(1);
                }
            }
        });
        mCheckBoxNumbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mAdditions.indexOf(2) == -1) {
                    mAdditions.add(2);
                }
            }
        });
        mCheckBoxSpecialCharacters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mAdditions.indexOf(3) == -1) {
                    mAdditions.add(3);
                }
            }

        });
        mGeneratePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mCheckBoxNumbers.isChecked() && mAdditions.size() > 0) {
                    mAdditions.remove((Integer) 2);
                }
                if (!mCheckBoxSpecialCharacters.isChecked() && mAdditions.size() > 0) {
                    mAdditions.remove((Integer) 3);
                }
                if (!mCheckBoxUpperCase.isChecked() && mAdditions.size() > 0) {
                    mAdditions.remove((Integer) 1);
                }

                mGeneratedPasswordText.setText(mHelper.passwordGenerate(mLength, mAdditions));
                mCopyGeneratedPasswordButton.setEnabled(mGeneratedPasswordText.length() > 0);
            }
        });

    }

}
