package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 * Class that models a data magnitude.
 * @serialField codMagnitude String code that represents the magnitud.
 * @serialField description String description of the magnitude represented.
 * @serialField unit String unit in which the magnitude is measured.
 */
public class Magnitude {
    private String codMagnitude;
    private String description;
    private String unit;
}
