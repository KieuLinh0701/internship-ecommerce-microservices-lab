package com.teamsolution.lab.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {
  public static Pageable toPageable(int page, int size, String sortBy, String direction) {
    Sort sort =
        "asc".equalsIgnoreCase(direction)
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
    return PageRequest.of(page, size, sort);
  }
}
