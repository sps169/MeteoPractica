package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;

@Data
@AllArgsConstructor

/**
 * 
 */
public class Moment {
    private LocalDateTime date;
    private Float value;

    public static Comparator getMomentComparator() {
        return Comparator.comparing(Moment::getValue);
    }
}
