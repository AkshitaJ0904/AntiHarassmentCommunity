package com.example.game;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;

public class LevelOneActivity extends BaseActivity {
    private ImageView characterImage;
    private TextView dialogueText;
    private TextToSpeech textToSpeech;
    private boolean ttsReady = false;
    private static final String FINAL_UTTERANCE_ID = "final_message";
    private SoundManager soundManager;

    // Variables for animated dialogue
    private Handler animationHandler = new Handler();
    private Runnable animationRunnable;
    private List<String> currentWords = new ArrayList<>();
    private int currentWordIndex = 0;
    private int visibleWordCount = 0;
    private int maxWordsPerBox = 15; // Adjust based on your UI
    private boolean isAnimating = false;

    // Speech rate in words per minute
    private float speechRate = 150f; // Adjust as needed
    private long wordDelay = 60000 / Math.round(speechRate); // milliseconds per word

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_level_one);
        hideSystemUI();

        // Initialize SoundManager
        soundManager = SoundManager.getInstance(this);
        // Start background music
        soundManager.startMusic();

        characterImage = findViewById(R.id.characterImage);
        dialogueText = findViewById(R.id.dialogueText);

        // Initialize Text-to-Speech
        initializeTTS();

        // Make sure we use Button instead of View for proper visibility control
        Button headButton = findViewById(R.id.headButton);
        Button chestButton = findViewById(R.id.chestButton);
        Button privatepartButton = findViewById(R.id.privatepartButton);

        // Initially show the head button but disable others
        headButton.setVisibility(View.VISIBLE);
        chestButton.setVisibility(View.VISIBLE);
        privatepartButton.setVisibility(View.VISIBLE);

        ImageButton back = findViewById(R.id.btnBackMap);
        back.setOnClickListener(v -> {
            AppUtils.playClickSound(this);
            finish();
        });
        // Initially only the head button is enabled
        headButton.setEnabled(true);
        chestButton.setEnabled(false);
        privatepartButton.setEnabled(false);

        showAnimatedDialogue("Tap the head to learn about good touch!", "intro_message");

        headButton.setOnClickListener(v -> {
            // Play button click sound
            AppUtils.playClickSound(this);
            cancelCurrentAnimation(); // Cancel any ongoing animation
            showAnimatedDialogue("A pat on the head is a good touch. It shows care and love. Now tap the chest.", "head_message");
            headButton.setEnabled(false);
            chestButton.setEnabled(true);
        });

        chestButton.setOnClickListener(v -> {
            // Play button click sound
            AppUtils.playClickSound(this);
            cancelCurrentAnimation(); // Cancel any ongoing animation
            showAnimatedDialogue("The chest is a private area. No one should touch you there except for parents or doctors when helping you bathe or during check-ups. Now tap the private area.", "chest_message");
            chestButton.setEnabled(false);
            privatepartButton.setEnabled(true);
        });

        privatepartButton.setOnClickListener(v -> {
            // Play button click sound
            AppUtils.playClickSound(this);
            cancelCurrentAnimation(); // Cancel any ongoing animation
            showAnimatedDialogue("Your private area is very special and private. Nobody should touch you there except parents or doctors when helping you clean or during check-ups. If anyone else tries, say 'NO!' and tell a trusted adult right away.\n\nLevel Complete!", FINAL_UTTERANCE_ID);
            privatepartButton.setEnabled(false);
            // We will finish the activity when speech completes in the utterance listener
        });
    }

    private void initializeTTS() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Handle language not supported
                } else {
                    ttsReady = true;
                    textToSpeech.setSpeechRate(speechRate / 175f); // Normalize to TTS expected range
                    // If we want to immediately speak the first dialogue
                    if (!currentWords.isEmpty()) {
                        startSpeaking("intro_message");
                    }
                }
            } else {
                // Handle TTS initialization failure
            }
        });

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // Speech started - pause background music
                runOnUiThread(() -> soundManager.pauseMusic());
            }

            @Override
            public void onDone(String utteranceId) {
                // Speech finished - resume background music
                runOnUiThread(() -> {
                    soundManager.startMusic();

                    if (utteranceId.equals(FINAL_UTTERANCE_ID)) {
                        // Only finish the activity if it was the final message
                        // Delay just a bit to ensure UI feels natural
                        new Handler().postDelayed(() -> finish(), 500);
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {
                // If there's an error, resume music and handle the error
                runOnUiThread(() -> {
                    soundManager.startMusic();

                    // If there's an error with the final utterance, still finish the activity
                    if (utteranceId.equals(FINAL_UTTERANCE_ID)) {
                        finish();
                    }
                });
            }
        });
    }

    private void showAnimatedDialogue(String message, String utteranceId) {
        // Clear any existing text and animation
        dialogueText.setText("");

        // Split message into words
        String cleanMessage = message.replace("\n", " ");
        currentWords = Arrays.asList(cleanMessage.split("\\s+"));
        currentWordIndex = 0;
        visibleWordCount = 0;

        // Start the animation
        startWordAnimation(utteranceId);
    }

    private void startWordAnimation(String utteranceId) {
        isAnimating = true;

        // Clear the text view initially
        dialogueText.setText("");
        visibleWordCount = 0;

        // Start speaking
        startSpeaking(utteranceId);

        // Begin word-by-word animation
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentWordIndex < currentWords.size()) {
                    // Add next word
                    if (visibleWordCount >= maxWordsPerBox) {
                        // Clear text when we hit the limit
                        dialogueText.setText("");
                        visibleWordCount = 0;
                    }

                    // Build the current visible text
                    StringBuilder visibleText = new StringBuilder();
                    int startIdx = Math.max(0, currentWordIndex - visibleWordCount);
                    for (int i = startIdx; i <= currentWordIndex; i++) {
                        visibleText.append(currentWords.get(i)).append(" ");
                    }

                    dialogueText.setText(visibleText.toString().trim());
                    visibleWordCount++;
                    currentWordIndex++;

                    // Schedule next word
                    animationHandler.postDelayed(this, wordDelay);
                } else {
                    isAnimating = false;
                }
            }
        };

        // Start the animation chain
        animationHandler.post(animationRunnable);
    }

    private void startSpeaking(String utteranceId) {
        if (ttsReady) {
            // Stop any ongoing speech
            if (textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }

            // Combine all words for speech
            StringBuilder fullText = new StringBuilder();
            for (String word : currentWords) {
                fullText.append(word).append(" ");
            }

            // Create params with utterance ID
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);

            // Music will be paused in the onStart listener
            textToSpeech.speak(fullText.toString().trim(), TextToSpeech.QUEUE_FLUSH, params);
        }
    }

    private void cancelCurrentAnimation() {
        if (isAnimating) {
            animationHandler.removeCallbacks(animationRunnable);
            isAnimating = false;
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start background music when the activity is resumed, unless TTS is speaking
        if (!textToSpeech.isSpeaking()) {
            soundManager.startMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Cancel any ongoing animation
        cancelCurrentAnimation();
        // Pause background music when the activity is paused
        soundManager.pauseMusic();
    }

    @Override
    protected void onDestroy() {
        cancelCurrentAnimation();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}