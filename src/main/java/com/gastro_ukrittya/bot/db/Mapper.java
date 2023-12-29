package com.gastro_ukrittya.bot.db;

public interface Mapper<D,E> {
    E toEntity(D dto);
}
