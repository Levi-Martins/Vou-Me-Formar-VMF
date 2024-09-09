package com.smd.ufccursos.domain.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PaginationTO {

    private int page;

    private int size;

    public PaginationTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    private Map<String, Object> params;

}
