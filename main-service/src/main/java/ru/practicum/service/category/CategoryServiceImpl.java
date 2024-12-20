package ru.practicum.service.category;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.model.Category;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {

    final CategoryRepository categoryRepository;
    final CategoryMapper categoryMapper;

    static final String CATEGORY_NOT_FOUND = "Категория с id = %d не найдена";

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.dtoToCategory(newCategoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(savedCategory);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void delete(Long id) {
        validateId(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto update(Long id, NewCategoryDto newCategoryDto) {
        Category existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND, "Объект не найден"));
        existCategory.setName(newCategoryDto.getName());

        Category updateCategory = categoryRepository.save(existCategory);
        return categoryMapper.toCategoryDto(updateCategory);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryMapper.toCategoriesDto(categoryRepository.getCategories(from, size));
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND, "Объект не найден"));
        return categoryMapper.toCategoryDto(existCategory);
    }

    private void validateId(Long id) {
        if (!categoryRepository.existsById(id)) {
            log.error("Указана несуществующая категория с id: {}", id);
            throw new NotFoundException(CATEGORY_NOT_FOUND, "Объект не существует");
        }
    }


}
