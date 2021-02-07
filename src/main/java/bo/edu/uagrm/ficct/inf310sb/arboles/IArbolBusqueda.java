package bo.edu.uagrm.ficct.inf310sb.arboles;

import java.util.List;

public interface IArbolBusqueda<K extends  Comparable<K>, V> {

    void vaciar();
    boolean esArbolVacio();
    int size();
    int altura();
    int nivel();
    K minimo();
    K maximo();
    void insertar(K clave, V valor);
    V eliminar(K clave);
    boolean contiene(K clave);
    V buscar(K clave);
    List<K> recorridoEnInOrden();
    List<K> recorridoEnPreOrden();
    List<K> recorridoEnPostOrden();
    List<K> recorridoPorNiveles();

}
