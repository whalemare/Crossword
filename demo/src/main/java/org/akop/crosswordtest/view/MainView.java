package org.akop.crosswordtest.view;

import org.akop.ararat.core.Crossword;

/**
 * @author Anton Vlasov - whalemare
 * @since 2019
 */
public interface MainView {
    void showBackgroundAnimation();

    void renderCrossword(Crossword crossword);

    void showHint(String text);

    void showMessage(String text);
}
