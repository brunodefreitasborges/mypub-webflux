package com.bruno.mypub.common.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleFormatter {

    private DoubleFormatter() {

    }

    public static Double formatNumber(Double numberToFormat) {
        return BigDecimal.valueOf(numberToFormat).setScale(2, RoundingMode.UP).doubleValue();
    }

}
