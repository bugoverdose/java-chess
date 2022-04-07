package chess.dto;

public class CreateGameDto {

    private final int id;

    public CreateGameDto(int id) {
        this.id = id;
    }

    public String toJson() {
        return "{\"gameId\":" + id + "}";
    }
}
