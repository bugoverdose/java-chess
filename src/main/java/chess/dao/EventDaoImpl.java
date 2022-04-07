package chess.dao;

import chess.domain.event.Event;
import chess.domain.event.EventType;
import chess.domain.event.MoveCommand;
import java.util.ArrayList;
import java.util.List;

public class EventDaoImpl implements EventDao {

    private static final String PROD_TABLE_NAME = "event";

    private final String table;

    EventDaoImpl(String table) {
        this.table = table;
    }

    public static EventDaoImpl ofProd() {
        return new EventDaoImpl(PROD_TABLE_NAME);
    }

    public List<Event> findAllByGameId(int gameId) {
        final String sql = addTable("SELECT type, description FROM %s WHERE game_id = ?");
        final ResultReader reader = new StatementExecutor(sql).setInt(gameId)
                .executeQuery();

        return readAllEvents(reader);
    }

    private List<Event> readAllEvents(ResultReader reader) {
        List<Event> pieces = new ArrayList<>();
        while (reader.hasNextRow()) {
            String eventType = reader.readStringAt("type");
            String description = reader.readStringAt("description");
            Event event = Event.of(eventType, description);
            pieces.add(event);
        }
        return pieces;
    }

    public void saveMove(int gameId, MoveCommand moveCommand) {
        final String sql = addTable("INSERT INTO %s (game_id, type, description) VALUES (?, ?, ?)");

        new StatementExecutor(sql).setInt(gameId)
                .setString(EventType.MOVE)
                .setString(moveCommand.toDescription())
                .executeCommand();
    }

    private String addTable(String sql) {
        return String.format(sql, table);
    }
}
