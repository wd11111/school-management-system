package pl.schoolmanagementsystem.common.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.schoolmanagementsystem.BaseContainerTest;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchoolClassRepositoryTest extends BaseContainerTest implements Samples {

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Test
    void should_return_all_school_classes() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<SchoolClassDto> result = schoolClassRepository.findAllClasses(pageable);

        assertThat(result).extracting("schoolClassName").containsAll(List.of("1A", "1B"));
    }
}