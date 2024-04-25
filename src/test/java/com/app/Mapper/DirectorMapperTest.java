package com.app.Mapper;

import com.app.DTO.DirectorDTO;
import com.app.Entity.Director;
import com.app.Entity.Nationality;

import java.time.LocalDate;

class DirectorMapperTest {
    public static void main(String[] args){
        DirectorDTO directorDTO = new DirectorDTO();
        directorDTO.setFirstName("first");
        directorDTO.setLastName("last");
        directorDTO.setBirthDate(LocalDate.ofYearDay(2002,24));
        directorDTO.setNationality(String.valueOf(Nationality.USA));

        Director director = DirectorMapper.INSTANCE.mapDirectorDtoToDirector(directorDTO);
        System.out.println(director.toString());
    }
}