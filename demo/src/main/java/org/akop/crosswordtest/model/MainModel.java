package org.akop.crosswordtest.model;

import android.content.res.Resources;
import android.support.annotation.RawRes;

import org.akop.ararat.core.Crossword;
import org.akop.ararat.core.CrosswordKt;
import org.akop.ararat.io.WSJFormatter;
import org.akop.crosswordtest.R;

import java.io.IOException;
import java.io.InputStream;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * @author Matvey Starodubov
 * @since 2019
 */
public class MainModel {

    /**
     * Публичный метод для возможности получения кроссворда из базы данных
     */
    public Crossword getCrossword(Resources resources) {
        return readPuzzle(resources, R.raw.wsj);
    }

    /**
     * Функция парсинга файла кроссворда, который пришел из БД и преобразование его в объект Crossword
     */
    private Crossword readPuzzle(Resources resources, @RawRes int resourceId) {
        final InputStream inputStream = resources.openRawResource(resourceId);
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

    /**
     * Генерирует текст подсказки в зависимости от того какое слово выбрано
     */
    public String generateTextDependOnDirection(Resources resources, Crossword.Word word) {
        switch (word.getDirection()) {
            case Crossword.Word.DIR_ACROSS:
                return resources.getString(R.string.across, word.getNumber(), word.getHint());
            case Crossword.Word.DIR_DOWN:
                return resources.getString(R.string.down, word.getNumber(), word.getHint());
            default:
                return "";
        }
    }

    /**
     * Увеличивает номер пройденного шага, чтобы пользователь видел сколько действий он совершил
     */
    public int incrementStepIfNeeded(int current, Crossword.Word word) {
        return current + 1;
    }
}
