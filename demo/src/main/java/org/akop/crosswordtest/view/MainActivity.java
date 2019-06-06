package org.akop.crosswordtest.view;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.akop.ararat.core.Crossword;
import org.akop.ararat.core.CrosswordKt;
import org.akop.ararat.io.WSJFormatter;
import org.akop.ararat.view.CrosswordView;
import org.akop.crosswordtest.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * @author Anton Vlasov - whalemare
 * @since 2019
 */
public class MainActivity
    extends AppCompatActivity
    implements CrosswordView.OnLongPressListener,
    CrosswordView.OnStateChangeListener,
    CrosswordView.OnSelectionChangeListener,
    MainView {

    private CrosswordView crosswordView;
    private TextView hint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnimationDrawable animDrawable = (AnimationDrawable) findViewById(R.id.rootLayout).getBackground();
        animDrawable.setEnterFadeDuration(10);
        animDrawable.setExitFadeDuration(5000);
        animDrawable.start();

        crosswordView = findViewById(R.id.crossword);
        hint = findViewById(R.id.hint);

        Crossword crossword = readPuzzle(R.raw.wsj);

        String title = getString(R.string.title_by_author, crossword.getTitle(), crossword.getAuthor());

        crosswordView.setCrossword(crossword);
        crosswordView.setOnLongPressListener(this);
        crosswordView.setOnStateChangeListener(this);
        crosswordView.setOnSelectionChangeListener(this);
        crosswordView.setInputValidator(new Function1<String, Boolean>() {
            @Override
            public Boolean invoke(String s) {
                return !Character.isISOControl(s.charAt(0));
            }
        });
        crosswordView.setUndoMode(CrosswordView.UNDO_NONE);
        crosswordView.setMarkerDisplayMode(CrosswordView.MARKER_CHEAT);

        onSelectionChanged(
            crosswordView,
            crosswordView.getSelectedWord(),
            crosswordView.getSelectedCell()
        );
    }

    private final Crossword readPuzzle(@RawRes int resourceId) {
        final InputStream inputStream = this.getResources().openRawResource(resourceId);
        return CrosswordKt.buildCrossword(new Function1<Crossword.Builder, Unit>() {
            @Override
            public Unit invoke(Crossword.Builder builder) {
                try {
                    new WSJFormatter().read(builder, inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    @Override
    public void onCrosswordChanged(@NotNull CrosswordView view) {

    }

    @Override
    public void onCrosswordSolved(@NotNull CrosswordView view) {
        Toast.makeText(this, R.string.youve_solved_the_puzzle,
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCrosswordUnsolved(@NotNull CrosswordView view) {
        Toast.makeText(this, R.string.youve_solved_the_puzzle,
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectionChanged(@NotNull CrosswordView view, @Nullable Crossword.Word word, int position) {
        String text = "";
        switch (word.getDirection()) {
            case Crossword.Word.DIR_ACROSS:
                text = getString(R.string.across, word.getNumber(), word.getHint());
                break;
            case Crossword.Word.DIR_DOWN:
                text = getString(R.string.down, word.getNumber(), word.getHint());
                break;
            default:
                text = "";
        }
        hint.setText(text);
    }

    @Override
    public void onCellLongPressed(@NotNull CrosswordView view, @NotNull Crossword.Word word, int cell) {
        Toast.makeText(this, "Подсказка: " + word.getHint(),
            Toast.LENGTH_SHORT).show();
    }

}
