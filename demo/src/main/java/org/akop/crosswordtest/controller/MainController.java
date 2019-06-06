package org.akop.crosswordtest.controller;

import android.content.Context;

import org.akop.ararat.core.Crossword;
import org.akop.crosswordtest.model.MainModel;
import org.akop.crosswordtest.view.MainView;

/**
 * @author Matvey Starodubov
 * @since 2019
 */
public class MainController {

    private MainView view;
    private MainModel model = new MainModel();
    private int stepCount = 0;

    /**
     * Прикрепляет пришедшую вью к контроллеру
     * @param mainView
     */
    public void attach(MainView mainView) {
        this.view = mainView;
        view.showBackgroundAnimation();
        invalidateCrossword();
    }

    /**
     * Отправляет команду на вью отрисовать кроссворд, запрашивая его у слоя бизнес-логики (модели)
     */
    private void invalidateCrossword() {
        Crossword crossword = model.getCrossword(((Context) view).getResources());
        view.renderCrossword(crossword);
    }

    /**
     * Обрабатывает долгое нажатие на ячейку
     */
    public void onLongPress(int cell, Crossword.Word word) {
        view.showMessage("Подсказка: " + word.getHint());
    }

    /**
     * Обрабатывает изменения в веденном слове, запрашивает подсказку у слоя модели и просит вью отобразить ее
     */
    public void onSelectionChange(Crossword.Word word, int position) {
        String text = model.generateTextDependOnDirection(((Context) view).getResources(), word);
        view.showHint(text);
        stepCount = model.incrementStepIfNeeded(stepCount, word);
    }

    /**
     * Отправляет на вью событие об успешном решении кроссворда
     */
    public void onCrosswordSuccess() {
        view.showMessage("Поздравляю! Вы решили кроссворд за " + stepCount + " ходов");
    }

    /**
     * Отправляет на вью событие о проваленом решении кроссворда
     */
    public void onCrosswordFailed() {
        view.showMessage("Что-ж, повезет в другой раз!");
    }
}
