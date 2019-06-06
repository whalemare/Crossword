package org.akop.crosswordtest.controller;

import android.content.Context;

import org.akop.ararat.core.Crossword;
import org.akop.crosswordtest.model.MainModel;
import org.akop.crosswordtest.view.MainView;

/**
 * @author Anton Vlasov - whalemare
 * @since 2019
 */
public class MainController {

    private MainView view;
    private MainModel model = new MainModel();
    private int stepCount = 0;

    public void attach(MainView mainView) {
        this.view = mainView;
        view.showBackgroundAnimation();
        invalidateCrossword();
    }

    private void invalidateCrossword() {
        Crossword crossword = model.getCrossword(((Context) view).getResources());
        view.renderCrossword(crossword);
    }

    public void onLongPress(int cell, Crossword.Word word) {
        view.showMessage("Подсказка: " + word.getCitation());
    }

    public void onSelectionChange(Crossword.Word word, int position) {
        String text = model.generateTextDependOnDirection(((Context) view).getResources(), word);
        view.showHint(text);
        stepCount = model.incrementStepIfNeeded(stepCount, word);
    }

    public void onCrosswordSuccess() {
        view.showMessage("Поздравляю! Вы решили кроссворд за " + stepCount + " ходов");
    }

    public void onCrosswordFailed() {
        view.showMessage("Что-ж, повезет в другой раз!");
    }
}
