package bo.edu.uagrm.ficct.inf310sb.arboles;

import bo.edu.uagrm.ficct.inf310sb.arboles.Excepciones.ExcepcionClaveNoExiste;
import bo.edu.uagrm.ficct.inf310sb.arboles.Excepciones.ExcepcionOrdenInvalido;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ArbolMViasBusqueda <K extends  Comparable<K>,V>
        implements IArbolBusqueda<K,V>{

    protected  NodoMVias<K,V> raiz;
    protected  int orden;
    protected  int POSICION_INVALIDA = -1;

    public ArbolMViasBusqueda () {
        this.orden = 4;
    }

    public ArbolMViasBusqueda (int orden) throws ExcepcionOrdenInvalido {

        if (orden < 3) {
            throw new ExcepcionOrdenInvalido();
        }
        this.orden = orden;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }

    @Override
    public int size() {

        if (this.esArbolVacio()){
            return 0;
        }

        int cantidad = 0;
        Queue<NodoMVias<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()){
            NodoMVias<K,V> nodoActual = colaDeNodos.poll();
            cantidad++;
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }

            if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacias())){
                colaDeNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()));
            }
         }

        return cantidad;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    private int altura(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int alturaMayor = 0;
        for (int i = 0; i < orden; i++){
            int alturaHijo = altura(nodoActual.getHijo(i));
            if (alturaHijo > alturaMayor) {
                alturaMayor = alturaHijo;
            }
        }

        return alturaMayor + 1;
    }

    @Override
    public int nivel() {
        return nivel(this.raiz);
    }

    private int nivel(NodoMVias<K,V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return -1;
        }
        int nivelMayor = -1;
        for (int i = 0; i < orden; i++) {
            int nivelActual = nivel(nodoActual.getHijo(i));
            if (nivelActual > nivelMayor) {
                nivelMayor = nivelActual;
            }
        }
        return nivelMayor +1;

    }

    @Override
    public K minimo() {
        if (this.esArbolVacio()){
            return null;
        }
        NodoMVias<K,V> nodoActual = this.raiz;
        NodoMVias<K,V> nodoAnterior = (NodoMVias<K,V>)NodoMVias.nodoVacio();
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijo(0);
        }
        return nodoAnterior.getClave(0);
    }

    @Override
    public K maximo() {
        if (this.esArbolVacio()){
            return null;
        }

        NodoMVias<K,V> nodoActual = this.raiz;
        NodoMVias<K,V> nodoAnterior = (NodoMVias<K, V>) NodoMVias.nodoVacio();
        while (!NodoMVias.esNodoVacio(nodoActual)){
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias());
        }

        return nodoAnterior.getClave(nodoAnterior.cantidadDeClavesNoVacias() - 1);
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (this.esArbolVacio()){
            this.raiz = new NodoMVias<>(orden, claveAInsertar,valorAInsertar);
            return;
        }

        NodoMVias<K,V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)){
            int posicionDatoExiste = existeClaveEnNodo(nodoActual, claveAInsertar);
            if (posicionDatoExiste != POSICION_INVALIDA) {
                nodoActual.setValor(posicionDatoExiste, valorAInsertar);
                nodoActual = NodoMVias.nodoVacio();
            } else {

                if (nodoActual.esHoja()) {
                    if (nodoActual.estanClavesLLenas()) {
                        int posicionPorDondeBajar = this.posicionPorDondeBajar(nodoActual, claveAInsertar);
                        NodoMVias<K, V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                    } else {
                        this.insertarDatosOrdenadosEnNodo(nodoActual, claveAInsertar, valorAInsertar);
                    }
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    int posicionPorDondeBajar = this.posicionPorDondeBajar(nodoActual, claveAInsertar);
                    if (nodoActual.esHijoVacio(posicionPorDondeBajar)){
                        NodoMVias<K,V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                        nodoActual = NodoMVias.nodoVacio();
                    } else {
                        nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                    }
                }
            }
        }



    }

    protected void insertarDatosOrdenadosEnNodo(NodoMVias<K, V> nodoActual, K claveAInsertar, V valorAInsertar) {

        int posicionUltima = nodoActual.cantidadDeClavesNoVacias();

        while (posicionUltima > 0) {
            K claveActual = nodoActual.getClave(posicionUltima - 1);
            if (claveAInsertar.compareTo(claveActual) > 0) {
                nodoActual.setClave(posicionUltima, claveAInsertar);
                nodoActual.setValor(posicionUltima, valorAInsertar);
                return;
            }else{
                nodoActual.setClave(posicionUltima,nodoActual.getClave(posicionUltima - 1));
                nodoActual.setValor(posicionUltima, nodoActual.getValor(posicionUltima - 1));
            }
            posicionUltima--;
        }
        nodoActual.setClave(0, claveAInsertar);
        nodoActual.setValor(0, valorAInsertar);

    }

    protected int posicionPorDondeBajar(NodoMVias<K,V> nodoActual, K claveAInsertar) {

        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++){
            K claveActual = nodoActual.getClave(i);
            if (claveAInsertar.compareTo(claveActual) < 0) {
                return i;
            }
        }
        return nodoActual.cantidadDeClavesNoVacias();
    }

    protected int existeClaveEnNodo(NodoMVias<K,V> nodoActual, K claveABuscar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveActual.compareTo(claveABuscar) == 0) {
                return i;
            }
        }

        return POSICION_INVALIDA;
    }

    @Override
    public V eliminar(K claveAEliminar) {
        if (claveAEliminar == null){
            throw new IllegalArgumentException("clave a eliminar no puede ser nula");
        }
        V valorARetornar = this.buscar(claveAEliminar);
        if (valorARetornar == null) {
            throw new ExcepcionClaveNoExiste("la clave no existe en el arbol");
        }
        this.raiz = eliminar(this.raiz, claveAEliminar);
        return valorARetornar;
    }

    private NodoMVias<K,V> eliminar(NodoMVias<K,V> nodoActual, K claveAEliminar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveActual) == 0) {

                //caso 1
                // en el caso de que el nodo sea hoja
                if (nodoActual.esHoja()){
                    this.eliminarDatosDeNodo(nodoActual, i);
                    if (nodoActual.cantidadDeClavesNoVacias() == 0) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }

                //si llegamos hasta aqui la clave a eliminar esta en un nodo no hoja
                K claveReemplazo;
                if (this.hayHijosMasAdelante(nodoActual, i)){
                    claveReemplazo = this.buscarClaveSucesoraInOrden(nodoActual, claveAEliminar,i);
                } else {
                    claveReemplazo = this.buscarClavePredecesoraInOrden(nodoActual, claveAEliminar,i);
                }
                V valorDeReemplazo = this.buscar(claveReemplazo);
                nodoActual = eliminar(nodoActual, claveReemplazo);
                nodoActual.setClave(i, claveReemplazo);
                nodoActual.setValor(i, valorDeReemplazo);
                return nodoActual;


            }

            if (claveAEliminar.compareTo(claveActual) < 0) {
                NodoMVias<K,V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }//fin del for

        NodoMVias<K,V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()),
                claveAEliminar);
        nodoActual.setHijo(nodoActual.cantidadDeClavesNoVacias(), supuestoNuevoHijo);
        return nodoActual;

    }

    private K buscarClavePredecesoraInOrden(NodoMVias<K,V> nodoActual, K claveAEliminar, int posicionAEliminar) {
        if (!nodoActual.esHijoVacio(posicionAEliminar)) {
            NodoMVias<K,V> nodoAnterior = nodoActual.getHijo(posicionAEliminar);
            return nodoAnterior.getClave(nodoAnterior.cantidadDeClavesNoVacias() - 1);
        }
        return nodoActual.getClave(posicionAEliminar - 1);

    }

    private K buscarClaveSucesoraInOrden(NodoMVias<K,V> nodoActual, K claveAEliminar, int posicionAEliminar) {
        if (!nodoActual.esHijoVacio(posicionAEliminar + 1)){
            NodoMVias<K,V> nodoSiguiente  = nodoActual.getHijo(posicionAEliminar + 1);
            return nodoSiguiente.getClave(0);
        }
        return nodoActual.getClave(posicionAEliminar + 1);
    }

    private boolean hayHijosMasAdelante(NodoMVias<K,V> nodoActual, int posicionInicialABuscar) {
        for (int i = posicionInicialABuscar + 1; i < orden; i++) {
            if (!nodoActual.esHijoVacio(i)) {
                return true;
            }
        }
        return false;
    }

    protected void eliminarDatosDeNodo(NodoMVias<K,V> nodoActual, int poisicionAEliminar) {
       
        for (int i = poisicionAEliminar; i < orden - 1 && !nodoActual.esClaveVacia( i+ 1); i++ ) {
            nodoActual.setClave(i, nodoActual.getClave(i + 1));
            nodoActual.setValor(i, nodoActual.getValor(i+1));
        }
        nodoActual.setValor(nodoActual.cantidadDeClavesNoVacias() - 1,(V)NodoMVias.datoVacio());
        nodoActual.setClave(nodoActual.cantidadDeClavesNoVacias() - 1,(K)NodoMVias.datoVacio());
        
    }


    @Override
    public boolean contiene(K clave) {
        return this.buscar(clave) != null;
    }

    @Override
    public V buscar(K claveABuscar) {
        NodoMVias<K,V> nodoActual = this.raiz;

        while (!NodoMVias.esNodoVacio(nodoActual)){
            boolean huboCambioDeNodoActual = false;
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias() && !huboCambioDeNodoActual; i++ ) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0){
                    return nodoActual.getValor(i);
                }
                if (claveABuscar.compareTo(claveActual) < 0){
                    huboCambioDeNodoActual = true;
                    nodoActual = nodoActual.getHijo(i);

                }
            }
            if (!huboCambioDeNodoActual){
                nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias());
            }
        }

        return (V) NodoMVias.datoVacio();
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrden(NodoMVias<K,V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorridoEnInOrden(nodoActual.getHijo(i), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
        recorridoEnInOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), recorrido);
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnPreOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPreOrden(NodoMVias<K,V> nodoActual, List<K> recorrido) {

        if (NodoMVias.esNodoVacio(nodoActual)){
            return;
        }
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorrido.add(nodoActual.getClave(i));
            recorridoEnPreOrden(nodoActual.getHijo(i), recorrido);
        }
        recorridoEnPreOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), recorrido);

    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnPostOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPostOrden(NodoMVias<K,V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)){
            return;
        }

        recorridoEnPostOrden(nodoActual.getHijo(0), recorrido);
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++){
            recorridoEnPostOrden(nodoActual.getHijo(i + 1), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }


    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new LinkedList<>();

        if (this.esArbolVacio()){
            return recorrido;
        }

        Queue<NodoMVias<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);

        while (!colaDeNodos.isEmpty()){
            NodoMVias<K,V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++){
                recorrido.add(nodoActual.getClave(i));

                if (!nodoActual.esHijoVacio(i)){
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }

            if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacias())) {
                colaDeNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()));
            }
        }

        return recorrido;
    }

    //metodo que retorne la cantidad de datos no vacios que hay en un arbol m vias

    public int cantidadDatosVacios() {
        return cantidadDatosVacios(this.raiz);
    }

    private int cantidadDatosVacios(NodoMVias<K,V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int cantidad = 0;

        for (int i = 0; i < orden - 1; i++) {
            cantidad += cantidadDatosVacios(nodoActual.getHijo(i));
            if (nodoActual.esClaveVacia(i)) {
                cantidad++;
            }
        }

        cantidad += cantidadDatosVacios(nodoActual.getHijo(orden - 1));
        return cantidad;

    }

    //implemente un metodo que retorne cuantas hojas tiene un arbol m vias

    public int cantidadDeHojas() {
        return cantidadDeHojas(this.raiz);
    }

    private int cantidadDeHojas(NodoMVias<K,V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        if (nodoActual.esHoja()) {
            return 1;
        }

        int cantidad = 0;
        for (int i = 0; i < orden; i++){
            cantidad += cantidadDeHojas(nodoActual.getHijo(i));
        }

        return cantidad;
    }

    //implemente un metodo que retorne la cantidad de nodos hojas a partir del nivel N

    public int cantidadDeHojasDesdeNivel(int nivelBase) {

        return cantidadDeHojasDesdeNivel(this.raiz, nivelBase ,0);
    }

    private int cantidadDeHojasDesdeNivel(NodoMVias<K,V> nodoActual, int nivelBase, int nivelActual) {
        if(NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        if (nivelActual >= nivelBase) {
            if (nodoActual.esHoja()) {
                return 1;
            }
        } else {
            if (nodoActual.esHoja()) {
                return 0;
            }
        }

        int cantidad = 0;

        for (int i = 0; i < orden; i++) {
            cantidad += cantidadDeHojasDesdeNivel(nodoActual.getHijo(i), nivelBase, nivelActual + 1);
        }

        return cantidad;
        
    }

    /**
     * 2. Implemente un método recursivo que retorne el nivel en que se encuentra una clave
     * que se recibe como parámetro. Devolver -1 si la clave no está en el árbol
     */

    public int nivelDeClave(K claveABuscar){
        return nivelDeClave(this.raiz, claveABuscar, 0);
    }

    private int nivelDeClave(NodoMVias<K,V> nodoActual, K claveABuscar, int nivel ) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return -1;
        }

        for (int i=0; i < nodoActual.cantidadDeClavesNoVacias(); i++){
            if ((nodoActual.getClave(i)).compareTo(claveABuscar) == 0) {
                return nivel;
            }

            if (nodoActual.getClave(i).compareTo(claveABuscar) < 0) {
                nivelDeClave(nodoActual.getHijo(i), claveABuscar, nivel + 1);
            }
        }

        return 0;
    }

    /**
     * 3. Implemente un método recursivo que retorne la cantidad de datos no vacíos que hay
     *    en el nivel N de un árbol m-vias de búsqueda
     */

    public int cantidadDeDatosNoVaciosEnNivel(int nivelObjetivo) {
        return cantidadDeDatosNoVaciosEnNivel(this.raiz, nivelObjetivo, 0);
    }

    private int cantidadDeDatosNoVaciosEnNivel(NodoMVias<K, V> nodoActual, int nivelObjetivo, int nivelActual) {

        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidad = 0;
        for (int i = 0; i < orden - 1; i++){
            cantidad += this.cantidadDeDatosNoVaciosEnNivel(nodoActual.getHijo(i), nivelObjetivo, nivelActual + 1);
            if (nivelActual == nivelObjetivo) {
                if (!nodoActual.esClaveVacia(i)) {
                    cantidad++;
                }
            }
        }

        cantidad += this.cantidadDeDatosNoVaciosEnNivel(nodoActual.getHijo(this.orden - 1), nivelObjetivo, nivelActual + 1);

        return cantidad;
    }

    /**
     * 4. Implemente un método recursivo que retorne la cantidad de nodos hojas en un
     *    árbol m vías de búsqueda, pero solo después del nivel N
     */

    public int cantidadDeHojasDespuesDelNivel(int nivelObjetivo) {
        return cantidadDeHojasDespuesDelNivel(this.raiz, nivelObjetivo, 0);
    }

    private int cantidadDeHojasDespuesDelNivel(NodoMVias<K, V> nodoActual, int nivelObjetivo, int nivelActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (nivelActual > nivelObjetivo) {
            if (nodoActual.esHoja()) {
                return 1;
            }
        }

        int cantidad = 0;
        for (int i = 0; i < orden ; i++) {
            cantidad += this.cantidadDeHojasDespuesDelNivel(nodoActual.getHijo(i), nivelObjetivo, nivelActual + 1);
        }

        return cantidad;
    }


    /**
     * 5. Implemente un método iterativo que retorne la cantidad de datos vacios y no vacíos en un
     * árbol b, pero solo antes del nivel N
     */







    /**
     * 6. Implemente un método que retorne verdadero si solo hay hojas en el último nivel
     * de un árbol m-vias de búsqueda. Falso en caso contrario.
     */

    public boolean hayHojasSoloEnUltimoNivel() {

        NodoMVias<K,V> nodoActual = this.raiz;
        int nivelDelArbol = this.nivel() - 1;

        for (int i = 0; i < orden - 1; i++) {
            int nivelDeHijoSiguiente = this.nivel(nodoActual.getHijo(i));
            if (nivelDeHijoSiguiente >=0) {
                if (nivelDelArbol != nivelDeHijoSiguiente) {
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * 7. Implemente un método que retorne verdadero si un árbol m-vias esta balanceado
     *    según las reglas de un árbol B. Falso en caso contrario.
     */

    public boolean estaBalanceado () {
        if (!this.hayHojasSoloEnUltimoNivel()) {
            return false;
        }
        Queue<NodoMVias<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while ( !colaDeNodos.isEmpty()) {
            NodoMVias<K,V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++ ) {
                if (nodoActual.cantidadDeHijosNoVacios() < nodoActual.cantidadDeClavesNoVacias() &&
                        !nodoActual.esHoja()) {
                    return false;
                }
                if (nodoActual.esHoja()) {
                    if (nodoActual.cantidadDeClavesNoVacias() < ((orden - 1)/ 2)) {
                        return false;
                    }
                }
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }

            if (!nodoActual.esHijoVacio(orden - 1)) {
                colaDeNodos.offer(nodoActual.getHijo(orden - 1));
            }
        }

        return true;
    }


    /**
     * 8. Implemente un método privado que reciba un dato como parámetro y que retorne cual
     * sería el sucesor inorden de dicho dato, sin realizar el recorrido en inOrden.
     */







}
