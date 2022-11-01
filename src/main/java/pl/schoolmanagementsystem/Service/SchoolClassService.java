package pl.schoolmanagementsystem.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.Model.SchoolClass;
import pl.schoolmanagementsystem.Repository.SchoolClassRepository;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    public SchoolClass getClassByName(String name) {
        return schoolClassRepository.findByName(name).orElseThrow();
    }
}
