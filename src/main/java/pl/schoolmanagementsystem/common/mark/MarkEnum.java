package pl.schoolmanagementsystem.common.mark;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public enum MarkEnum {

    A(6, "6"),
    A_MINUS(5.75, "6-"),
    B_PLUS(5.25, "5+"),
    B(5.0, "5"),
    B_MINUS(4.75, "5-"),
    C_PLUS(4.25, "4+"),
    C(4.0, "4"),
    C_MINUS(3.75, "4-"),
    D_PLUS(3.25, "3+"),
    D(3.0, "3"),
    D_MINUS(2.75, "3-"),
    E_PLUS(2.25, "2+"),
    E(2.0, "2"),
    E_MINUS(1.75, "2-"),
    F_PLUS(1.25, "1+"),
    F(1.0, "1");

    public final double value;
    public final String name;

    public static Optional<Double> getValueByName(String name) {
        return Optional.of(map.get(name));
    }

    private static final Map<String, Double> map;

    static {
        map = new HashMap<>();
        for (MarkEnum m : MarkEnum.values()) {
            map.put(m.name, m.value);
        }
    }

}
