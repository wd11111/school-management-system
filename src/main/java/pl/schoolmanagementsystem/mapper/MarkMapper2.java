package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.dto.MarkDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarkMapper2 {
    // todo zmieniłem nazwe, żeby się zmieściło do tego samego pakietu.
    //  Ogólnie nie powinieneś używać tych samych nazw nawet w różnych miejscach w projekcie.
    //  Da się, ale to nie wskazane

    public static Map<String, List<Byte>> mapToListOfBytesInMapStructure(Map<String, List<MarkDto>> mapToTransform) {
        return mapToTransform.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        m -> m.getValue().stream()
                                .map(MarkDto::getMark)
                                .collect(Collectors.toList())));
    }
}
