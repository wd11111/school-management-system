package pl.schoolmanagementsystem.common.model;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public enum MarkEnum {

    A(BigDecimal.valueOf(6)),
    A_MINUS(BigDecimal.valueOf(5.75)),
    B_PLUS(BigDecimal.valueOf(5.25)),
    B(BigDecimal.valueOf(5.25)),
    B_MINUS(BigDecimal.valueOf(4.75)),
    C_PLUS(BigDecimal.valueOf(4.25)),
    C(BigDecimal.valueOf(4.0)),
    C_MINUS(BigDecimal.valueOf(3.75)),
    D_PLUS(BigDecimal.valueOf(3.25)),
    D(BigDecimal.valueOf(3.0)),
    D_MINUS(BigDecimal.valueOf(2.75)),
    E_PLUS(BigDecimal.valueOf(2.25)),
    E(BigDecimal.valueOf(2.0)),
    E_MINUS(BigDecimal.valueOf(1.75)),
    F_PLUS(BigDecimal.valueOf(1.25)),
    F(BigDecimal.valueOf(1.0));

    public final BigDecimal value;

}
