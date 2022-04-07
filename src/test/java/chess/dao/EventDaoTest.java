package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.event.Event;
import chess.domain.event.MoveCommand;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class EventDaoTest {

    private static final String TEST_TABLE = "event_test";
    private static final String SETUP_TEST_DB_SQL = String.format("INSERT INTO %s (game_id, type, description) "
            + "VALUES (1, 'MOVE', 'a2 a4'), (1, 'MOVE', 'a7 a5'), (2, 'MOVE', 'a2 a3')", TEST_TABLE);

    private static final String CLEANSE_TEST_DB_SQL = String.format("TRUNCATE TABLE %s", TEST_TABLE);

    private final EventDao dao = new EventDaoImpl(TEST_TABLE);

    @BeforeEach
    void setUp() {
        cleanUp();
        new StatementExecutor(SETUP_TEST_DB_SQL).executeCommand();
    }

    @AfterEach
    void cleanUp() {
        new StatementExecutor(CLEANSE_TEST_DB_SQL).executeCommand();
    }

    @Test
    void findAllByGameId_메서드는_특정_gameId에_해당되는_모든_이벤트를_조회한다() {
        List<Event> actual = dao.findAllByGameId(1);
        List<Event> expected = List.of(
                Event.ofMove("a2 a4"),
                Event.ofMove("a7 a5"));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveMove_메서드는_새로운_이벤트를_저장한다() {
        dao.saveMove(1, MoveCommand.ofEventDescription("b2 b4"));

        List<Event> actual = dao.findAllByGameId(1);

        List<Event> expected = List.of(Event.ofMove("a2 a4"), Event.ofMove("a7 a5"), Event.ofMove("b2 b4"));

        assertThat(actual).isEqualTo(expected);
    }
}
