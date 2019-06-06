package org.akop.crosswordtest.view;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.akop.ararat.core.Crossword;
import org.akop.ararat.view.CrosswordView;
import org.akop.crosswordtest.R;
import org.akop.crosswordtest.controller.MainController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.functions.Function1;

/**
 * @author Anton Vlasov - whalemare
 * @since 2019
 */
public class MainActivity
    extends AppCompatActivity
    implements MainView {

    private MainController controller = new MainController();
    private CrosswordView crosswordView;
    private TextView textHint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crosswordView = findViewById(R.id.crossword);
        textHint = findViewById(R.id.hint);
        controller.attach(this);

        crosswordView.setOnLongPressListener(new CrosswordView.OnLongPressListener() {
            @Override
            public void onCellLongPressed(@NotNull CrosswordView view, @NotNull Crossword.Word word, int cell) {
                controller.onLongPress(cell, word);
            }
        });
        crosswordView.setInputValidator(new Function1<String, Boolean>() {
            @Override
            public Boolean invoke(String s) {
                return !Character.isISOControl(s.charAt(0));
            }
        });

        crosswordView.setOnSelectionChangeListener(new CrosswordView.OnSelectionChangeListener() {
            @Override
            public void onSelectionChanged(@NotNull CrosswordView view, @Nullable Crossword.Word word, int position) {
                controller.onSelectionChange(word, position);
            }
        });
        crosswordView.setUndoMode(CrosswordView.UNDO_NONE);
        crosswordView.setMarkerDisplayMode(CrosswordView.MARKER_CHEAT);
        crosswordView.setOnStateChangeListener(new CrosswordView.OnStateChangeListener() {
            @Override
            public void onCrosswordChanged(@NotNull CrosswordView view) {
            }

            @Override
            public void onCrosswordSolved(@NotNull CrosswordView view) {
                controller.onCrosswordSuccess();
            }

            @Override
            public void onCrosswordUnsolved(@NotNull CrosswordView view) {
                controller.onCrosswordFailed();
            }
        });

        // initial selection
        controller.onSelectionChange(
            crosswordView.getSelectedWord(),
            crosswordView.getSelectedCell()
        );
    }

    @Override
    public void renderCrossword(Crossword crossword) {
        String title = getString(R.string.title_by_author, crossword.getTitle(), crossword.getAuthor());
        crosswordView.setCrossword(crossword);
    }

    @Override
    public void showBackgroundAnimation() {
        AnimationDrawable animDrawable = (AnimationDrawable) findViewById(R.id.rootLayout).getBackground();
        animDrawable.setEnterFadeDuration(10);
        animDrawable.setExitFadeDuration(5000);
        animDrawable.start();
    }

    @Override
    public void showHint(String text) {
        textHint.setText(text);
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
