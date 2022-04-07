package chess.dao;

import static chess.util.DatabaseUtil.getConnection;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.event.Event;
import chess.domain.event.EventType;
import chess.domain.event.MoveCommand;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class EventDaoTest {

    private static final String TEST_TABLE = "event_test";
    private static final List<String> SETUP_TEST_DB_SQL = List.of(
            String.format("INSERT INTO %s (game_id, type, description) VALUES (1, 'MOVE', 'a2 a4')", TEST_TABLE),
            String.format("INSERT INTO %s (game_id, type, description) VALUES (1, 'MOVE', 'a7 a5')", TEST_TABLE),
            String.format("INSERT INTO %s (game_id, type, description) VALUES (2, 'MOVE', 'a2 a3')", TEST_TABLE));

    private static final String CLEANSE_TEST_DB_SQL = String.format("TRUNCATE TABLE %s", TEST_TABLE);

    private final EventDao dao = new EventDao(TEST_TABLE);

    @BeforeEach
    void setUp() {
        cleanUp();
        try (final Connection connection = getConnection()) {
            for (String sql : SETUP_TEST_DB_SQL) {
                connection.prepareStatement(sql)
                        .executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void cleanUp() {
        try (final Connection connection = getConnection()) {
            connection.prepareStatement(CLEANSE_TEST_DB_SQL)
                    .executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findAllByGameId_메서드는_특정_gameId에_해당되는_모든_이벤트를_조회한다() {
        List<Event> actual = dao.findAllByGameId(1);
        List<Event> expected = List.of(
                new Event(EventType.MOVE, "a2 a4"),
                new Event(EventType.MOVE, "a7 a5"));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveMove_메서드는_새로운_이벤트를_저장한다() {
        dao.saveMove(1, MoveCommand.ofEventDescription("b2 b4"));

        List<Event> actual = dao.findAllByGameId(1);

        List<Event> expected = List.of(
                new Event(EventType.MOVE, "a2 a4"),
                new Event(EventType.MOVE, "a7 a5"),
                new Event(EventType.MOVE, "b2 b4"));

        assertThat(actual).isEqualTo(expected);
    }
}