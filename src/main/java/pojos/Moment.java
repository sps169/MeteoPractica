package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor

/**
 * Class that models a measure and the instant it was measured at.
 * @author sps169, FedericoTB
 */
public class Moment {
    private LocalDateTime date;
    private Float value;
}
