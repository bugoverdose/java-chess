package chess.domain.piece.position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PositionUtil {

    private static final int BOARD_SIZE = 8;
    public static final int RANKS_TOTAL_SIZE = BOARD_SIZE;
    public static final int FILES_TOTAL_SIZE = BOARD_SIZE;
    private static final int ASCII_DIFFERENCE = 48;
    private static final String INVALID_POSITION_RANGE_EXCEPTION_MESSAGE = "존재하지 않는 포지션입니다. (a1~h8)";

    private static final List<Character> validFiles = toCharacters("abcdefgh");
    private static final List<Character> validRanks = toCharacters("12345678");

    private static final Map<Character, Integer> fileMap = new HashMap<>(FILES_TOTAL_SIZE);
    private static final Map<Character, Integer> rankMap = new HashMap<>(RANKS_TOTAL_SIZE);

    static {
        IntStream.range(0, BOARD_SIZE)
                .peek(PositionUtil::initializeRankMapValue)
                .forEach(PositionUtil::initializeFileMapValue);
    }

    private PositionUtil() {
    }

    private static void initializeRankMapValue(int idx) {
        Character key = validRanks.get(idx);
        rankMap.put(key, idx);
    }

    private static void initializeFileMapValue(int idx) {
        Character key = validFiles.get(idx);
        fileMap.put(key, idx);
    }

    private static List<Character> toCharacters(String value) {
        return value.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
    }

    public static void validatePosition(String position) {
        char[] positionInfo = position.toCharArray();
        boolean isInvalidFile = fileMap.get(positionInfo[0]) == null;
        boolean isInvalidRank = rankMap.get(positionInfo[1]) == null;

        if (isInvalidFile || isInvalidRank) {
           throw new IllegalArgumentException(INVALID_POSITION_RANGE_EXCEPTION_MESSAGE);
        }
    }

    public static int rankToIdx(char rank) {
        Integer intValue = rankMap.get(rank);
        validateRange(intValue);
        return intValue;
    }

    public static int fileToIdx(char file) {
        Integer intValue = fileMap.get(file);
        validateRange(intValue);
        return intValue;
    }

    public static int charToMatchingInt(char value) {
        return value - ASCII_DIFFERENCE;
    }

    private static void validateRange(Integer intValue) {
        if (intValue == null) {
            throw new IllegalArgumentException(INVALID_POSITION_RANGE_EXCEPTION_MESSAGE);
        }
    }
}
