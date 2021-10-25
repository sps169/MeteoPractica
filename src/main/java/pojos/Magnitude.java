package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 * Class that models a magnitude
 * @author sps169, FedericoTB
 */
public class Magnitude {
    /**
     * Stores a numeric code representing the magnitude
     */
    private String codMagnitude;

    /**
     * Description of the magnitude
     */
    private String description;

    /**
     * Unit of the magnitude
     */
    private String unit;
}
