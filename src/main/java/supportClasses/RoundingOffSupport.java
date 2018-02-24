package supportClasses;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class RoundingOffSupport {
    public Double roundingOffSupport(Double money){
        MathContext mathContext = new MathContext(4, RoundingMode.HALF_DOWN);
        money= new BigDecimal(money).round(mathContext).doubleValue();
        return money;
    }
}
