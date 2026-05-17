import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TestParse {
    public static void main(String[] args) {
        try {
            System.out.println("07:00 -> " + LocalTime.parse("07:00"));
        } catch (DateTimeParseException e) {
            System.out.println("07:00 failed: " + e.getMessage());
        }
    }
}
