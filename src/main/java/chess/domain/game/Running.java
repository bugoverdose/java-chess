package chess.domain.game;

import static chess.domain.board.piece.PieceType.KING;

import chess.domain.board.Board;
import chess.domain.board.piece.Color;
import chess.domain.board.position.Position;
import chess.domain.event.MoveCommand;

abstract class Running extends Started {

    private static final int ONGOING_GAME_KING_COUNT = 2;

    Running(Board board) {
        super(board);
    }

    @Override
    public final Game moveChessmen(MoveCommand moveCommand) {
        Position from = moveCommand.getSource();
        Position to = moveCommand.getTarget();

        board.movePiece(from, to, getCurrentTurnColor());
        return moveResult();
    }

    private Game moveResult() {
        if (board.countByType(KING) < ONGOING_GAME_KING_COUNT) {
            return new GameOver(board);
        }
        return continueGame();
    }

    protected abstract Color getCurrentTurnColor();

    protected abstract Game continueGame();

    @Override
    public final boolean isEnd() {
        return false;
    }

    @Override
    public final GameResult getResult() {
        throw new UnsupportedOperationException("아직 종료되지 않은 게임입니다.");
    }
}
