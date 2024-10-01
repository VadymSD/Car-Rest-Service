package vadym.carrestservice.service;


import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CommonService<T, D> {
    List<T> findAll(Pageable pageable);

    Optional<T> findById(Long id);

    T save(D dto);

    T update(Long id, D dto);

    void delete(Long id);
}
