package bo.edu.uagrm.ficct.inf310sb.arboles;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ArbolBinarioBusqueda <K extends  Comparable<K>,V> implements IArbolBusqueda<K, V> {

    protected NodoBinario<K,V> raiz;

    public  ArbolBinarioBusqueda (){

    }

    public ArbolBinarioBusqueda (List<K> clavesInOrden, List<V> valoresInOrden,
                                 List<K> clavesNoInOrden, List<V> valoresNoInOrden,
                                 boolean usandoPreOrden){
        if (clavesInOrden == null ||
                clavesNoInOrden == null ||
                valoresInOrden == null ||
                valoresNoInOrden == null){
            throw  new IllegalArgumentException("Los Parametros no pueden ser nulos");
        }

        if (clavesInOrden.isEmpty() ||
            clavesNoInOrden.isEmpty() ||
            valoresInOrden.isEmpty() ||
            valoresNoInOrden.isEmpty()){
            throw  new IllegalArgumentException("Los Parametros no pueden ser vacios");
        }

        if (clavesInOrden.size() != clavesNoInOrden.size() ||
                clavesInOrden.size() != valoresInOrden.size() ||
                clavesInOrden.size() != valoresNoInOrden.size()){
            throw  new IllegalArgumentException("Los Parametros no pueden  tener diferentes tamaños");
        }
        if (usandoPreOrden){
            this.raiz = reconstruirConPreOrden (clavesInOrden,valoresInOrden, clavesNoInOrden, valoresNoInOrden);
        }else{
            this.raiz = reconstruirConPostOrden(clavesInOrden,valoresInOrden, clavesNoInOrden, valoresNoInOrden);
        }
    }


    private NodoBinario<K,V> reconstruirConPreOrden(List<K> clavesInOrden, List<V> valoresInOrden,
                                        List<K> clavesEnPreOrden, List<V> valoresEnPreOrden) {
        if (clavesInOrden.isEmpty()){
            return (NodoBinario<K, V>) NodoBinario.nodoVacio();
        }

        int posicionDeClavePadreEnPreOrden = 0;
        K clavePadre = clavesEnPreOrden.get(posicionDeClavePadreEnPreOrden);
        V valorePadre = valoresEnPreOrden.get(posicionDeClavePadreEnPreOrden);
        int posicionDeClavePadreEnInOrden = this.posicionDeClaves(clavePadre, clavesInOrden);


        //para armar la rama izquierda
        List<K> clavesInOrdenPorIzquierda = clavesInOrden.subList(0, posicionDeClavePadreEnInOrden);
        List<V> valoresInOrdenPorIzquierda = valoresInOrden.subList(0,posicionDeClavePadreEnInOrden);
        List<K> clavePreOrdenPorIzquierda = clavesEnPreOrden.subList(1,posicionDeClavePadreEnInOrden + 1);
        List<V> valoresPreOrdenPorIzquierda = valoresEnPreOrden.subList(1,posicionDeClavePadreEnInOrden + 1);
        NodoBinario<K,V> hijoIzquierdo = reconstruirConPreOrden(clavesInOrdenPorIzquierda,valoresInOrdenPorIzquierda,
                clavePreOrdenPorIzquierda,valoresPreOrdenPorIzquierda);

        //para armar la rama derecha
        List<K> clavesInOrdenPorDerecha = clavesInOrden.subList(posicionDeClavePadreEnInOrden + 1, clavesInOrden.size());
        List<V> valoresInOrdenPorDerecha = valoresInOrden.subList(posicionDeClavePadreEnInOrden + 1, valoresInOrden.size());
        List<K> clavePreOrdenPorDerecha = clavesEnPreOrden.subList(posicionDeClavePadreEnInOrden + 1, clavesInOrden.size());
        List<V> valoresPreOrdenPorDerecha = valoresEnPreOrden.subList(posicionDeClavePadreEnInOrden + 1, clavesInOrden.size() );
        NodoBinario<K,V> hijoDerecho = reconstruirConPreOrden(clavesInOrdenPorDerecha,valoresInOrdenPorDerecha,
                clavePreOrdenPorDerecha,valoresPreOrdenPorDerecha);


        //armar el nodo actual
        NodoBinario<K,V> nodoActual = new NodoBinario<>(clavePadre, valorePadre);
        nodoActual.setHijoIzquierdo(hijoIzquierdo);
        nodoActual.setHijoDerecho(hijoDerecho);

        return nodoActual;

    }

    private NodoBinario<K,V> reconstruirConPostOrden(List<K> clavesInOrden, List<V> valoresInOrden,
                                        List<K> clavesEnPostOrden, List<V> valoresEnPostOrden) {
        if (clavesInOrden.isEmpty()){
            return (NodoBinario<K, V>) NodoBinario.nodoVacio();
        }

        int posicionClavePadreEnPostOrden = clavesEnPostOrden.size() - 1;
        K clavePadre = clavesEnPostOrden.get(posicionClavePadreEnPostOrden);
        V valorPadre = valoresEnPostOrden.get(posicionClavePadreEnPostOrden);
        int posicionClavePadreEnInOrden = this.posicionDeClaves(clavePadre, clavesInOrden);
        System.out.println(clavePadre);

        //rama izquierda
        List<K> clavesInOrdenPorIzquierda = clavesInOrden.subList(0, posicionClavePadreEnInOrden);
        List<V> valoresInOrdenPorIzquierda = valoresInOrden.subList(0, posicionClavePadreEnInOrden);
        List<K> clavesPostOrdenPorIzquierda = clavesEnPostOrden.subList(0,posicionClavePadreEnInOrden);
        List<V> valoresPostOrdenPorIzquierda = valoresEnPostOrden.subList(0, posicionClavePadreEnInOrden);
        NodoBinario<K,V> hijoIzquierdo = reconstruirConPostOrden(clavesInOrdenPorIzquierda, valoresInOrdenPorIzquierda,
                clavesPostOrdenPorIzquierda, valoresPostOrdenPorIzquierda);

        //rama derecha
        List<K> clavesInOrdenPorDerecha = clavesInOrden.subList(posicionClavePadreEnInOrden + 1, clavesInOrden.size());
        List<V> valoresInOrdenPorDerecha = valoresInOrden.subList(posicionClavePadreEnInOrden + 1, clavesInOrden.size());
        List<K> clavesPostOrdenPorDerecha = clavesEnPostOrden.subList(posicionClavePadreEnInOrden, clavesInOrden.size() - 1);
        List<V> valoresPostOrdenPorDerecha = valoresEnPostOrden.subList(posicionClavePadreEnInOrden, clavesInOrden.size() - 1);
        NodoBinario<K,V> hijoDerecho = reconstruirConPostOrden(clavesInOrdenPorDerecha, valoresInOrdenPorDerecha,
                clavesPostOrdenPorDerecha, valoresPostOrdenPorDerecha);


        //nodo
        NodoBinario<K,V> nodoActual = new NodoBinario<>(clavePadre, valorPadre);
        nodoActual.setHijoIzquierdo(hijoIzquierdo);
        nodoActual.setHijoDerecho(hijoDerecho);

        return nodoActual;
    }

    // buscamos la posicion de la claveABuscar en la lista y retornamos su posicion dentro de la lista
    // o caso contrario retornamos -1 como posicion no valida

    private int posicionDeClaves(K claveABuscar, List<K> listaDeClaves){
        for (int i = 0; i < listaDeClaves.size(); i++){
            K claveActual = listaDeClaves.get(i);
            if (claveActual.compareTo(claveABuscar) == 0){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void vaciar() {
        this.raiz = (NodoBinario<K, V>) NodoBinario.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(this.raiz);
    }

    @Override
    public int size() {
        if (this.esArbolVacio()){
            return 0;
        }
        int cantidadDeNodos = 0;
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(this.raiz);
        while (!pilaDeNodos.isEmpty()){
            NodoBinario<K,V> nodoActual = pilaDeNodos.pop();
            cantidadDeNodos++;
            if (!nodoActual.esVacioHijoDerecho()){
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esVacioHijoIzquierdo()){
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }
        }
        return cantidadDeNodos;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    protected int altura(NodoBinario<K,V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        int alturaIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaDerecha = altura(nodoActual.getHijoDerecho());

        if (alturaIzquierda > alturaDerecha){
            return alturaIzquierda + 1;
        }
        return alturaDerecha + 1;

    }

    //altura utilizando el recorrido por niveles modificado
    //modificamos en la parte del while utilizando el size de la cola
    //luego incrementamos otro bucle que es controlado con un contador que
    // se compara con el size de la cola

    public int alturaIt (){
        if (this.esArbolVacio()){
            return 0;
        }
        int alturaDelArbol = 0;
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()){
            int cantidadDeNodosEnLaCola = colaDeNodos.size();
            int i = 0;
            while (i < cantidadDeNodosEnLaCola){
                NodoBinario<K,V> nodoActual = colaDeNodos.poll();
                if (!nodoActual.esVacioHijoIzquierdo()){
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esVacioHijoDerecho()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                i++;
            }
            alturaDelArbol++;
        }
        return alturaDelArbol;

    }

    @Override
    public int nivel() {
        return nivel(this.raiz);
    }

    private int nivel(NodoBinario<K,V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)){
            return -1;
        }

        int nivelPorIzquierda = nivel(nodoActual.getHijoIzquierdo());
        int nivelPorDerecha = nivel(nodoActual.getHijoDerecho());

        return nivelPorIzquierda > nivelPorDerecha ? nivelPorIzquierda + 1 : nivelPorDerecha + 1;
    }

    @Override
    public K minimo() {

        if (this.esArbolVacio()){
            return null;
        }
        NodoBinario<K,V> nodoAnterior = (NodoBinario<K, V>) NodoBinario.nodoVacio();
        NodoBinario<K,V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)){
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }

        return nodoAnterior.getClave();
    }

    @Override
    public K maximo() {

        if (this.esArbolVacio()){
            return null;
        }

        NodoBinario<K,V> nodoAnterior = (NodoBinario<K, V>) NodoBinario.nodoVacio();
        NodoBinario<K,V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)){
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoDerecho();
        }

        return nodoAnterior.getClave();
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {

        if (claveAInsertar == null){
            throw new IllegalArgumentException("clave no puede ser nula");
        }

        if (valorAInsertar == null){
            throw new IllegalArgumentException("valor no puede ser nulo");
        }

        if (this.esArbolVacio()){
            this.raiz = new NodoBinario<>(claveAInsertar, valorAInsertar);
            return;
        }

        NodoBinario<K,V> nodoActual = this.raiz;
        NodoBinario<K,V> nodoAnterior = (NodoBinario<K, V>) NodoBinario.nodoVacio();

        while (!NodoBinario.esNodoVacio(nodoActual)){
            K claveActual = nodoActual.getClave();
            nodoAnterior = nodoActual;
            if (claveAInsertar.compareTo(claveActual) < 0){
                nodoActual = nodoActual.getHijoIzquierdo();
            }else {
                if (claveAInsertar.compareTo(claveActual) > 0) {
                    nodoActual = nodoActual.getHijoDerecho();
                } else {
                    //la clave ya exíste, entonces remplazamos el valor
                    nodoActual.setValor(valorAInsertar);
                    return;
                }
            }
        }
        //si llegamos a este punto, quiere decir que la clave no existe en el arbol,
        //entonces creamos un nuevo nodo con la clave y el valor a insertar
        // y el nodo anterior es el padre de este nuevo nodo

        NodoBinario<K,V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAInsertar);
        K claveDelPadre = nodoAnterior.getClave();
        if (claveAInsertar.compareTo(claveDelPadre) < 0){
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        }else{
            nodoAnterior.setHijoDerecho(nuevoNodo);
        }

    }

    @Override
    public V eliminar(K claveAEliminar) {
        if (claveAEliminar == null){
            throw  new IllegalArgumentException("la clave a eliminar no puede ser nula");
        }

        V valorARetornar = this.buscar(claveAEliminar);
        if (valorARetornar == null){
            throw new IllegalArgumentException("La clave no existe en el arbol");
        }

        this.raiz = eliminar(this.raiz, claveAEliminar);

        return valorARetornar;
    }

    private NodoBinario<K,V> eliminar(NodoBinario<K,V> nodoActual, K claveAEliminar){
        K claveActual = nodoActual.getClave();
        if (claveAEliminar.compareTo(claveActual) > 0){
            NodoBinario<K,V> posibleNuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(posibleNuevoHijoDerecho);
            return nodoActual;
        }
        if (claveAEliminar.compareTo(claveActual) < 0){
            NodoBinario<K,V> posibleNuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(posibleNuevoHijoIzquierdo);
            return nodoActual;
        }
        // si llegamos a este punto quiere decir que ya encontramos el nodo donde se encuentra la clave a eliminar

        //caso 1
        if (nodoActual.esHoja()){
            return (NodoBinario<K, V>) NodoBinario.nodoVacio();
        }

        //Caso 2

        if (!nodoActual.esVacioHijoIzquierdo() && nodoActual.esVacioHijoDerecho()) {
            return nodoActual.getHijoIzquierdo();
        }

        if (!nodoActual.esVacioHijoDerecho() && nodoActual.esVacioHijoIzquierdo()) {
            return nodoActual.getHijoDerecho();
        }

        //Caso 3

        NodoBinario<K,V> nodoReemplazo = this.buscarNodoSucesor(nodoActual.getHijoDerecho());
        NodoBinario<K,V> posibleNuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(), nodoReemplazo.getClave());
        nodoActual.setClave(nodoReemplazo.getClave());
        nodoActual.setValor(nodoReemplazo.getValor());

        return nodoActual;
    }

    protected NodoBinario<K,V> buscarNodoSucesor(NodoBinario<K,V> nodoActual) {
        NodoBinario<K,V> nodoAnterior;
        do {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        while (!NodoBinario.esNodoVacio(nodoActual));

        return  nodoAnterior;
    }

    @Override
    public boolean contiene(K clave) {
        return this.buscar(clave) != null;
    }

    @Override
    public V buscar(K claveABuscar) {
        if (claveABuscar == null){
            throw new IllegalArgumentException("clave no nula");
        }

        if (this.esArbolVacio()){
            return null;
        }

        NodoBinario<K,V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)){
            K claveActual = nodoActual.getClave();
            if (claveABuscar.compareTo(claveActual) == 0){
                return nodoActual.getValor();
            } else if (claveABuscar.compareTo(claveActual) < 0){
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
        //si se llega a este punto quiere decir que la claveABuscar no se encuentra en el arbol
        //y retornamos nulo
        return null;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrden(NodoBinario<K,V> nodoActual, List<K> recorrido) {
        //simulamos un caso base
        if (NodoBinario.esNodoVacio(nodoActual)){
            return;
        }

        recorridoEnInOrden(nodoActual.getHijoIzquierdo(), recorrido);
        recorrido.add(nodoActual.getClave());
        recorridoEnInOrden(nodoActual.getHijoDerecho(), recorrido);
    }

    @Override
    public List<K> recorridoEnPreOrden() {

        List<K> recorrido = new LinkedList<>();

        if (this.esArbolVacio()){
            return recorrido;
        }

        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(this.raiz);

        while (!pilaDeNodos.isEmpty()){
            NodoBinario<K,V> nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esVacioHijoDerecho()){
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esVacioHijoIzquierdo()){
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }
        }

        return recorrido;
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()){
            return recorrido;
        }

        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;
        meterPilaParaPostOrden(pilaDeNodos, nodoActual);



        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!pilaDeNodos.isEmpty()){
                NodoBinario<K,V> nodoDelTope = pilaDeNodos.peek();
                if (!nodoDelTope.esVacioHijoDerecho() && nodoDelTope.getHijoDerecho() != nodoActual){
                    meterPilaParaPostOrden(pilaDeNodos, nodoDelTope.getHijoDerecho());

                }
            }
        }
        return recorrido;
    }

    private void meterPilaParaPostOrden(Stack<NodoBinario<K, V>> pilaDeNodos, NodoBinario<K, V> nodoActual) {
        while (!NodoBinario.esNodoVacio(nodoActual)){
            pilaDeNodos.push(nodoActual);
            if (!nodoActual.esVacioHijoIzquierdo()){
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
    }

    @Override
    public List<K> recorridoPorNiveles() {

        List<K> recorrido = new LinkedList<>();

        if (this.esArbolVacio()){
            return recorrido;
        }

        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);

        while (!colaDeNodos.isEmpty()){
            NodoBinario<K,V> nodoActual = colaDeNodos.poll();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esVacioHijoIzquierdo()){
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }

        return recorrido;
    }


    //cantidad de hijos derechos
    public int cantidadDeHijosDerechos (){
        return cantidadDeHijosDerechos(this.raiz);
    }

    private int cantidadDeHijosDerechos (NodoBinario<K,V> nodoActual){
        if (NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }

        int hdPorRamaIzquierda = cantidadDeHijosDerechos(nodoActual.getHijoIzquierdo());
        int hdPorRamaDerecha = cantidadDeHijosDerechos(nodoActual.getHijoDerecho());

        if (NodoBinario.esNodoVacio(nodoActual.getHijoDerecho())){
            return hdPorRamaDerecha + hdPorRamaIzquierda + 1;
        }
        return hdPorRamaIzquierda + hdPorRamaDerecha;

    }

    //implementar un metodo que retorne si un arbol tiene nodos completos,
    //es decir nodos que tengan sus dos hijos distintos de vacios en el nivel n

    public boolean tieneNodosCompletosEnNivel(int nivelObjetivo){
        return tieneNodosCompletosEnNivel(this.raiz, nivelObjetivo, 0);
    }

    private boolean tieneNodosCompletosEnNivel(NodoBinario<K,V> nodoActual, int nivelObjetivo, int nivelActual) {
        if (NodoBinario.esNodoVacio(nodoActual)){
            return false;
        }

        if (nivelActual == nivelObjetivo){
            return !nodoActual.esVacioHijoIzquierdo() && !nodoActual.esVacioHijoDerecho();
        }

        boolean completoPorIzq = this.tieneNodosCompletosEnNivel(nodoActual.getHijoIzquierdo(),
                nivelObjetivo, nivelActual + 1);
        boolean completoPorDer = this.tieneNodosCompletosEnNivel(nodoActual.getHijoDerecho(),
                nivelObjetivo, nivelActual + 1);
        return completoPorIzq && completoPorDer;
    }


    /**
     * 2.- implemente un metodo recusirvo que retorne la cantidad de nodos que solo tienen
     * hijo izquierdo no vacioen un arbol binario
     */

    public int cantidadDeNodosConHijoIzquierdo(){
        return cantidadDeNodosConHijoIzquierdo(this.raiz);
    }

    private int cantidadDeNodosConHijoIzquierdo(NodoBinario<K, V> nodoActual){
        if (NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        int cantidad = 0;
        cantidad += this.cantidadDeNodosConHijoIzquierdo(nodoActual.getHijoIzquierdo());
        cantidad += this.cantidadDeNodosConHijoIzquierdo(nodoActual.getHijoDerecho());
        if (nodoActual.esVacioHijoDerecho() && !nodoActual.esVacioHijoIzquierdo()) {
            return cantidad + 1;
        }

        return cantidad;
    }

    /**
     * 3. Implemente un método iterativo que retorne la cantidad nodos que tienen solo
     * hijo izquierdo no vacío en un árbol binario
     * @return
     */
    public int cantidadDeNodosConSoloHijoIzq() {

        if (this.esArbolVacio()){
            return 0;
        }

        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;
        meterPilaParaPostOrden(pilaDeNodos, nodoActual);
        int cantidad = 0;
        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            if (nodoActual.esVacioHijoDerecho() && !nodoActual.esVacioHijoIzquierdo()){
                cantidad++;
            }
            if (!pilaDeNodos.isEmpty()){
                NodoBinario<K,V> nodoDelTope = pilaDeNodos.peek();
                if (!nodoDelTope.esVacioHijoDerecho() && nodoDelTope.getHijoDerecho() != nodoActual){
                    meterPilaParaPostOrden(pilaDeNodos, nodoDelTope.getHijoDerecho());
                }
            }
        }
        return cantidad;
    }

    /**
     * 4. Implemente un método recursivo que retorne la cantidad nodos que tienen solo
     * hijo izquierdo no vacío en un árbol binario, pero solo en el nivel N
     */

    public int cantidadDeHijosIzquierdoEnNivel(int nivelObjetivo) {
        return cantidadDeHijosIzquierdoEnNivel(this.raiz, nivelObjetivo, 0);
    }

    private int cantidadDeHijosIzquierdoEnNivel(NodoBinario<K,V> nodoActual, int nivelObjetivo, int nivelActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }

        if (nodoActual.esVacioHijoDerecho() && !nodoActual.esVacioHijoIzquierdo() && nivelActual == nivelObjetivo){
            return 1;
        }
        int cantidadIzq = cantidadDeHijosIzquierdoEnNivel(nodoActual.getHijoIzquierdo(),nivelObjetivo, nivelActual + 1);
        int cantidadDer = cantidadDeHijosIzquierdoEnNivel(nodoActual.getHijoDerecho(), nivelObjetivo, nivelActual + 1);

        return cantidadDer + cantidadIzq;
    }

    /**
     * 5. Implemente un método iterativo que retorne la cantidad nodos que tienen solo
     * hijo izquierdo no vacío en un árbol binario, pero solo después del nivel N
     */

    public int cantidadDeHijosIzqDespuesDelNivel(int nivelObjetivo) {

        if (this.esArbolVacio()){
            return 0;
        }
        int nivelActual = -1;
        int cantidad = 0;
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;

        while (!NodoBinario.esNodoVacio(nodoActual)){
            pilaDeNodos.push(nodoActual);
            nivelActual++;
            if (!nodoActual.esVacioHijoIzquierdo()){
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }

        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            if (nivelActual > nivelObjetivo){
                if (nodoActual.esVacioHijoDerecho() && !nodoActual.esVacioHijoIzquierdo()){
                    cantidad++;
                }
            }
            nivelActual--;
            if (!pilaDeNodos.isEmpty()){
                NodoBinario<K,V> nodoDelTope = pilaDeNodos.peek();

                if (!nodoDelTope.esVacioHijoDerecho() && nodoDelTope.getHijoDerecho() != nodoActual){
                    nodoActual = nodoDelTope.getHijoDerecho();

                    while (!NodoBinario.esNodoVacio(nodoActual)){
                        pilaDeNodos.push(nodoActual);
                        nivelActual++;
                        if (!nodoActual.esVacioHijoIzquierdo()){
                            nodoActual = nodoActual.getHijoIzquierdo();
                        } else {
                            nodoActual = nodoActual.getHijoDerecho();
                        }
                    }
                }
            }
        }
        return cantidad;

    }


    /**
     * 6. Implemente un método iterativo que retorne la cantidad nodos que tienen solo
     * hijo izquierdo no vacío en un árbol binario, pero solo antes del nivel N
     */


    public int cantidadDeHijosIzqAntesDelNivel(int nivelObjetivo) {

        if (this.esArbolVacio()){
            return 0;
        }
        int nivelActual = -1;
        int cantidad = 0;
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;

        while (!NodoBinario.esNodoVacio(nodoActual)){
            pilaDeNodos.push(nodoActual);
            nivelActual++;
            if (!nodoActual.esVacioHijoIzquierdo()){
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }

        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            if (nivelActual < nivelObjetivo){
                if (nodoActual.esVacioHijoDerecho() && !nodoActual.esVacioHijoIzquierdo()){
                    cantidad++;
                }
            }
            nivelActual--;
            if (!pilaDeNodos.isEmpty()){
                NodoBinario<K,V> nodoDelTope = pilaDeNodos.peek();

                if (!nodoDelTope.esVacioHijoDerecho() && nodoDelTope.getHijoDerecho() != nodoActual){
                    nodoActual = nodoDelTope.getHijoDerecho();

                    while (!NodoBinario.esNodoVacio(nodoActual)){
                        pilaDeNodos.push(nodoActual);
                        nivelActual++;
                        if (!nodoActual.esVacioHijoIzquierdo()){
                            nodoActual = nodoActual.getHijoIzquierdo();
                        } else {
                            nodoActual = nodoActual.getHijoDerecho();
                        }
                    }
                }
            }
        }
        return cantidad;

    }


    /**
     * 7.
     * Implemente un método recursivo que reciba como parámetro otro árbol binario de
     * búsqueda que retorne verdadero, si el árbol binario es similar al árbol binario recibido como
     * parámetro, falso en caso contrario.
     */



    /**
     * 9. Para un árbol binario implemente un método que retorne la cantidad de nodos
     * que tienen ambos hijos desde el nivel N.
     */

    public int cantidadDeNodosConAmbosHijos(int nivelObjetivo) {
        return cantidadDeNodosConAmbosHijos(this.raiz, nivelObjetivo, 0);
    }

    private int cantidadDeNodosConAmbosHijos(NodoBinario<K, V> nodoActual, int nivelObjetivo, int nivelActual) {

        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }

        int cantidadIzq = cantidadDeNodosConAmbosHijos(nodoActual.getHijoIzquierdo(), nivelObjetivo, nivelActual + 1);
        int cantidadDer = cantidadDeNodosConAmbosHijos(nodoActual.getHijoDerecho(), nivelObjetivo, nivelActual + 1);

        if (nivelActual >= nivelObjetivo) {
            if (!nodoActual.esVacioHijoIzquierdo() && !nodoActual.esVacioHijoDerecho()) {
                return cantidadDer + cantidadIzq + 1;
            }
        }


        return cantidadDer + cantidadIzq;
    }

    /**
     * 10. Implementar un método que retorne un nuevo árbol binario de búsqueda invertido.
     */


    /**
     * 11. Implementar un método que retorne verdadero si un árbol binario esta lleno.
     */


}
