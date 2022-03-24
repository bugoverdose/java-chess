package chess.domain.game;

import static chess.domain.piece.Color.BLACK;
import static chess.domain.piece.Color.WHITE;
import static chess.domain.position.util.PositionUtil.FILES_TOTAL_SIZE;

import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.position.Position;
import chess.dto.GameResultDto;
import chess.dto.MovePositionCommandDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChessGame {

    private static final int TOTAL_KING_COUNT = 2;

    private final List<Piece> chessmen = new ArrayList<>(32);

    public ChessGame() {
        chessmen.addAll(initStrongmen(BLACK));
        chessmen.addAll(initPawnsOf(BLACK));
        chessmen.addAll(initPawnsOf(WHITE));
        chessmen.addAll(initStrongmen(WHITE));
    }

    private List<Piece> initPawnsOf(Color color) {
        return IntStream.range(0, FILES_TOTAL_SIZE)
                .mapToObj(fileIdx -> Pawn.of(color, fileIdx))
                .collect(Collectors.toList());
    }

    private List<Piece> initStrongmen(Color color) {
        return List.of(new King(color), new Queen(color),
                Rook.ofLeft(color), Rook.ofRight(color),
                Bishop.ofLeft(color), Bishop.ofRight(color),
                Knight.ofLeft(color), Knight.ofRight(color));
    }

    public void moveChessmen(MovePositionCommandDto dto) {
        Piece sourcePiece = extractPiece(Position.of(dto.source()));
        Position toPosition = Position.of(dto.target());

        if (checkOccupied(toPosition)) {
            Piece targetPiece = extractPiece(toPosition);
            sourcePiece.kill(targetPiece);
            chessmen.remove(targetPiece);
            return;
        }

        sourcePiece.move(toPosition);
    }

    private Piece extractPiece(Position position) {
        return chessmen.stream()
                .filter(piece -> piece.isAt(position))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(""));
    }

    private boolean checkOccupied(Position position) {
        return chessmen.stream()
                .anyMatch(piece -> piece.isAt(position));
    }

    public boolean isEnd() {
        int kingCount = (int) chessmen.stream()
                .filter(Piece::isKing)
                .count();

        return kingCount < TOTAL_KING_COUNT;
    }

    public GameResultDto getGameResult() {
        double whiteScore = calculateScore(WHITE);
        double blackScore = calculateScore(BLACK);

        return new GameResultDto(whiteScore, blackScore);
    }

    private double calculateScore(Color color) {
        List<Piece> sameColorPieces = extractPiecesOf(color);
        List<Position> pawnPositions = extractPawnPositions(sameColorPieces);

        double defaultScore = sameColorPieces.stream()
                .map(Piece::score)
                .reduce(0.0, Double::sum);

        return defaultScore - (calculateSameFilePawnCount(pawnPositions) / 2);
    }

    private double calculateSameFilePawnCount(List<Position> pawnPositions) {
        return IntStream.range(0, FILES_TOTAL_SIZE)
                .map(fileIdx -> extractSameFilePositionCount(pawnPositions, fileIdx))
                .filter(count -> count > 1)
                .reduce(0, Integer::sum);
    }

    private List<Piece> extractPiecesOf(Color color) {
        return chessmen.stream()
                .filter(piece -> piece.hasColorOf(color))
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Position> extractPawnPositions(List<Piece> sameColorPieces) {
        return sameColorPieces.stream()
                .filter(Piece::isPawn)
                .map(Piece::position)
                .collect(Collectors.toUnmodifiableList());
    }

    private int extractSameFilePositionCount(List<Position> positions, int fileIdx) {
        return (int) positions.stream()
                .filter(position -> position.hasSameFileIdx(fileIdx))
                .count();
    }

    public List<Piece> getChessmen() {
        return chessmen;
    }

    @Override
    public String toString() {
        return "ChessGame{" + "chessmen=" + chessmen + '}';
    }
}
