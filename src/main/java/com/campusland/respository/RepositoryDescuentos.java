package com.campusland.respository;





import java.util.List;

import com.campusland.respository.models.Descuento;


public interface RepositoryDescuentos {

    void agregarDescuento(Descuento descuento);

    Descuento obtenerDescuentoPorId(int id);

    List<Descuento> obtenerTodosLosDescuentos();

}


    


