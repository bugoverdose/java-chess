package chess;

import static chess.view.InputView.requestValidMoveInput;
import static chess.view.InputView.requestStartOrEndInput;
import static chess.view.InputView.requestStatusOrEndInput;
import static chess.view.OutputView.printBoard;
import static chess.view.OutputView.printGameInstructions;
import static chess.view.OutputView.printGameOverInstructions;
import static chess.view.OutputView.printStatus;

import chess.domain.ChessGame;
import chess.dto.BoardDto;

public class Application {

    public static void main(String[] args) {
        printGameInstructions();
        if (!requestStartOrEndInput()) {
            return;
        }

        ChessGame game = new ChessGame();
        printBoard(new BoardDto(game));
        while(!game.isEnd()) {
            game.moveChessmen(requestValidMoveInput());
            printBoard(new BoardDto(game));
        }
        printGameOverInstructions();
        while(requestStatusOrEndInput()) {
            printStatus(game.getGameResult());
        }
    }
}
