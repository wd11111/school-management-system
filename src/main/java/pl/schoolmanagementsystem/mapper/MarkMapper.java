package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.Mark;
import pl.schoolmanagementsystem.model.dto.MarkDto;

public class MarkMapper {

    public static Mark createMarkEntity(MarkDto markDto, long studentId) {// todo mapToMark / toMark będzie lepszą nazwą, bo tak naprawdę po prostu mapujesz jeden obiekt na drugi
        return Mark.builder()
                .mark(markDto.getMark())
                .studentId(studentId)
                .subject(markDto.getSubject())
                .build();
    } //todo DO WSZYSTKICH Maperów / Cretorów, warto je połączyć ze sobą. Nie powinny istnieć klasy typu MarkMapper2
}
