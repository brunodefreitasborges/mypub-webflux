package com.bruno.mypub.common.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrinkQuery {

    @Builder.Default
    private String id = "0";
    @Builder.Default
    private String name = "";

}
