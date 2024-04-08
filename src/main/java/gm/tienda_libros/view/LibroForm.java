package gm.tienda_libros.view;

import gm.tienda_libros.model.Libro;
import gm.tienda_libros.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Component
public class LibroForm extends JFrame {
    LibroService libroServicio;
    private JPanel panel;
    private JTextField idTexto;
    private JTable tablaLibros;
    private JTextField libroText;
    private JTextField autorText;
    private JTextField precioText;
    private JTextField cantText;
    private JButton agregarBtn;
    private JButton modificarButton;
    private JButton eliminarButton;

    private DefaultTableModel tablaModeloLibros;
    @Autowired
    public LibroForm(LibroService libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarBtn.addActionListener(e -> agregarLibro());
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e -> modificarLibro());
    }

    private void iniciarForma(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(1280,720);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamanioPantalla = toolkit.getScreenSize();
        int x = (tamanioPantalla.width - getWidth()/ 2);
        int y = (tamanioPantalla.height = getHeight() / 2);
        setLocation(x, y);
    }

    private void cargarLibroSeleccionado(){
        // Inicia en 0 las columnas
        var fila = tablaLibros.getSelectedRow();
        if(fila != -1){
            String idLibro = tablaLibros.getModel().getValueAt(fila,0).toString();
            idTexto.setText(idLibro);
            String nombreLibro = tablaLibros.getModel().getValueAt(fila,1).toString();
            libroText.setText(nombreLibro);
            String autor = tablaLibros.getModel().getValueAt(fila,2).toString();
            autorText.setText(autor);
            String precioLibro = tablaLibros.getModel().getValueAt(fila,3).toString();
            precioText.setText(precioLibro);
            String cantLibro = tablaLibros.getModel().getValueAt(fila,4).toString();
            cantText.setText(cantLibro);

        }
    }
    private void agregarLibro(){

        if(libroText.getText().isEmpty()){
            mostrarMensaje("Proporciona el nombre del libro");
            libroText.requestFocusInWindow();
            return;
        }

        var nombreLibro =libroText.getText();
        var autor = autorText.getText();
        var precio = Double.parseDouble(precioText.getText());
        var cant = Integer.parseInt(cantText.getText());

        var libro = new Libro(null,nombreLibro,autor,precio,cant);


       libroServicio.guardarLibro(libro);
        mostrarMensaje("Se guardo el libro");
        cleanForm();
        listarLibros();
    }

    private void modificarLibro(){

        if(this.idTexto.getText().isEmpty()){
            mostrarMensaje("Debe seleccionar un registro");
        }else {
            if (libroText.getText().isEmpty()) {
                mostrarMensaje("Proporciona el nombre del libro");
                libroText.requestFocusInWindow();
                return;
            }

            Integer idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroText.getText();
            var autor = autorText.getText();
            var precio = Double.parseDouble(precioText.getText());
            var cant = Integer.parseInt(cantText.getText());
            var libro = new Libro(idLibro,nombreLibro,autor,precio,cant);

            libroServicio.guardarLibro(libro);
            mostrarMensaje("Se modifico el libro");
            cleanForm();
            listarLibros();
        }
    }

    private void cleanForm(){
        libroText.setText("");
        autorText.setText("");
        precioText.setText("");
        cantText.setText("");
    }
    public void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        // Crear elemento idTexto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tablaModeloLibros = new DefaultTableModel(0,5);
        String [] cabeceras = {"Id", "Libro", "Autor", "Precio", "Cantidad Disponible"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceras);

        this.tablaLibros = new JTable(tablaModeloLibros);
        listarLibros();

    }
    private void listarLibros(){

        tablaModeloLibros.setRowCount(0);

        var libros = libroServicio.listarLibros();

        libros.forEach((libro)-> {
            Object[] filasLibros = {
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getCantidad()
            };
            this.tablaModeloLibros.addRow(filasLibros);
        });
    }
}
