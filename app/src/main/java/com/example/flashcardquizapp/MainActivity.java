package com.example.flashcardquizapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textViewCard;
    private Button btnShowAnswer, btnNext, btnPrevious, btnAdd, btnEdit, btnDelete;

    private ArrayList<Flashcard> flashcards = new ArrayList<>();
    private int currentIndex = 0;
    private boolean showingAnswer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCard = findViewById(R.id.textViewCard);
        btnShowAnswer = findViewById(R.id.btnShowAnswer);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // Default flashcard
        flashcards.add(new Flashcard("What is Android?", "Android is a mobile operating system."));
        updateCard();

        btnShowAnswer.setOnClickListener(v -> toggleAnswer());

        btnNext.setOnClickListener(v -> {
            if (currentIndex < flashcards.size() - 1) {
                currentIndex++;
                showingAnswer = false;
                updateCard();
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showingAnswer = false;
                updateCard();
            }
        });

        btnAdd.setOnClickListener(v -> showAddEditDialog(false));
        btnEdit.setOnClickListener(v -> showAddEditDialog(true));

        btnDelete.setOnClickListener(v -> {
            if (!flashcards.isEmpty()) {
                flashcards.remove(currentIndex);
                if (currentIndex > 0) currentIndex--;
                updateCard();
            }
        });
    }

    private void toggleAnswer() {
        showingAnswer = !showingAnswer;
        updateCard();
    }

    private void updateCard() {
        if (flashcards.isEmpty()) {
            textViewCard.setText("No Flashcards Available");
            return;
        }

        Flashcard card = flashcards.get(currentIndex);
        textViewCard.setText(showingAnswer ? card.getAnswer() : card.getQuestion());
    }

    private void showAddEditDialog(boolean isEdit) {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_edit, null);
        EditText editQuestion = view.findViewById(R.id.editQuestion);
        EditText editAnswer = view.findViewById(R.id.editAnswer);

        if (isEdit && !flashcards.isEmpty()) {
            editQuestion.setText(flashcards.get(currentIndex).getQuestion());
            editAnswer.setText(flashcards.get(currentIndex).getAnswer());
        }

        new AlertDialog.Builder(this)
                .setTitle(isEdit ? "Edit Flashcard" : "Add Flashcard")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String question = editQuestion.getText().toString();
                    String answer = editAnswer.getText().toString();

                    if (isEdit) {
                        flashcards.get(currentIndex).setQuestion(question);
                        flashcards.get(currentIndex).setAnswer(answer);
                    } else {
                        flashcards.add(new Flashcard(question, answer));
                        currentIndex = flashcards.size() - 1;
                    }

                    showingAnswer = false;
                    updateCard();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
