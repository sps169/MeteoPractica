package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Moment {
    private LocalDateTime date;
    private Float value;
}
