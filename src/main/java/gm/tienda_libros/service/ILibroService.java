package gm.tienda_libros.service;

import gm.tienda_libros.model.Libro;

import java.util.List;

public interface ILibroService {

    public List<Libro> listarLibros();

    public Libro buscarLibroPorId(Integer idLibro);

    public void guardarLibro(Libro libro);

    public void eliminarLibro(Libro libro);

}
