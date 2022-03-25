package chess.domain.piece;

import static chess.domain.piece.Color.BLACK;
import static chess.domain.piece.Color.WHITE;
import static chess.fixture.StrategyFixture.CLEAR_PATH_STRATEGY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class KingTest {

    @DisplayName("매개변수에 해당되는 색깔의 위치에 킹을 생성한다.")
    @Test
    void init() {
        King actual = new King(BLACK);

        King expected = new King(BLACK, Position.of("e8"));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("킹은 상하좌우 혹은 대각선 방향으로 한칸 이동할 수 있다.")
    @Test
    void move() {
        King king = new King(WHITE, Position.of("e1"));
        king.move(Position.of("d2"), CLEAR_PATH_STRATEGY);

        King expected = new King(WHITE, Position.of("d2"));

        assertThat(king).isEqualTo(expected);
    }

    @DisplayName("킹은 두칸 이상 이동하려는 경우 예외가 발생한다.")
    @Test
    void move_exception() {
        King king = new King(WHITE, Position.of("e1"));

        assertThatCode(() -> king.move(Position.of("c3"), CLEAR_PATH_STRATEGY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이동할 수 없는 위치입니다.");
    }

    @DisplayName("색과 위치가 동일한 King 인스턴스는 서로 동일하다고 간주된다.")
    @Test
    void equals_sameOnSameColorAndPosition() {
        King actual = new King(BLACK, Position.of("e1"));
        King expected = new King(BLACK, Position.of("e1"));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("다른 색의 King 인스턴스는 위치가 같더라도 서로 다른 것으로 간주된다.")
    @Test
    void equals_differentOnPositionDifference() {
        King blackKing = new King(BLACK, Position.of("e1"));
        King whiteKing = new King(WHITE, Position.of("e1"));

        assertThat(blackKing).isNotEqualTo(whiteKing);
    }

    @DisplayName("같은 색과 위치의 King 인스턴스의 해쉬코드 값은 서로 같다.")
    @Test
    void hashCode_sameOnSameColorAndPosition() {
        int actual = new King(BLACK, Position.of("e1")).hashCode();
        int expected = new King(BLACK, Position.of("e1")).hashCode();

        assertThat(actual).isEqualTo(expected);
    }
}
