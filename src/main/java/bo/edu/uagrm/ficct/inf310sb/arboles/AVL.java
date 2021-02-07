package bo.edu.uagrm.ficct.inf310sb.arboles;

public class AVL <K extends  Comparable<K>, V> extends ArbolBinarioBusqueda<K,V>{
    private static final byte DIFERENCIA_MAXIMA = 1;

    @Override
    public void insertar (K claveAInsertar, V valorAInsertar) {
        if (claveAInsertar == null){
            throw  new IllegalArgumentException("La clave a insertar no puede ser nula");
        }

        if (valorAInsertar == null){
            throw new IllegalArgumentException("El valor a insertar no puede ser nulo");
        }

        super.raiz = this.insertar(this.raiz, claveAInsertar, valorAInsertar);

    }

    private NodoBinario<K,V> insertar(NodoBinario<K,V> nodoActual, K claveAInsertar, V valorAInsertar){

        if (NodoBinario.esNodoVacio(nodoActual)){
            NodoBinario<K,V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAInsertar);
            return nuevoNodo;
        }

        K claveActual = nodoActual.getClave();
        if (claveAInsertar.compareTo(claveActual) > 0){
            NodoBinario<K,V> supuestoNuevoHijoDerecho =
                    insertar(nodoActual.getHijoDerecho(), claveAInsertar, valorAInsertar);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return balancear(nodoActual);
        }
        if (claveAInsertar.compareTo(claveActual) < 0){
            NodoBinario<K,V> supuestoNuevoHijoIzquierdo =
                    insertar(nodoActual.getHijoIzquierdo(), claveAInsertar, valorAInsertar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return balancear(nodoActual);
        }

        //si llegamos aqui en el nodoActual esta la clave a insertar
        //entonces reemplazamos el valor al nodoActual

        nodoActual.setValor(valorAInsertar);
        return nodoActual;
    }

    private NodoBinario<K,V> balancear(NodoBinario<K,V> nodoActual) {

        int alturaRamaIzq = altura(nodoActual.getHijoIzquierdo());
        int alturaRamaDer = altura(nodoActual.getHijoDerecho());
        int diferencia = alturaRamaIzq - alturaRamaDer;
        if (diferencia > DIFERENCIA_MAXIMA){
            //hay que balanzear
            NodoBinario<K,V> hijoIzquierdo = nodoActual.getHijoIzquierdo();
            alturaRamaIzq = altura(hijoIzquierdo.getHijoIzquierdo());
            alturaRamaDer = altura(hijoIzquierdo.getHijoDerecho());

            if (alturaRamaDer > alturaRamaIzq) {
                return this.rotacionDobleADerecha(nodoActual);
            } else {
                return this.rotacionSimpleADerecha(nodoActual);
            }

        } else if (diferencia < -DIFERENCIA_MAXIMA) {

            //AQUI HAY QUE REVISAR QUE SE HAGA LO CORRECTO
            NodoBinario<K,V> hijoDerecho = nodoActual.getHijoDerecho();
            alturaRamaIzq = altura(hijoDerecho.getHijoIzquierdo());
            alturaRamaDer = altura(hijoDerecho.getHijoDerecho());

            if (alturaRamaDer > alturaRamaIzq) {
                return this.rotacionSimpleAIzquierda(nodoActual);

            } else {
                return this.rotacionDobleAIzquierda(nodoActual);
            }


        }

        return nodoActual;
    }

    private NodoBinario<K,V> rotacionSimpleAIzquierda(NodoBinario<K,V> nodoActual) {
        NodoBinario<K,V> nodoQueRota = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(nodoQueRota.getHijoIzquierdo());
        nodoQueRota.setHijoIzquierdo(nodoActual);
        return nodoQueRota;
    }

    private NodoBinario<K,V> rotacionDobleAIzquierda(NodoBinario<K,V> nodoActual) {
        nodoActual.setHijoDerecho(rotacionSimpleADerecha(nodoActual.getHijoDerecho()));
        return this.rotacionSimpleAIzquierda(nodoActual);
    }

    private NodoBinario<K,V> rotacionSimpleADerecha(NodoBinario<K,V> nodoActual) {
        NodoBinario<K,V> nodoQueRota = nodoActual.getHijoIzquierdo();
        nodoActual.setHijoIzquierdo(nodoQueRota.getHijoDerecho());
        nodoQueRota.setHijoDerecho(nodoActual);
        return nodoQueRota;
    }

    private NodoBinario<K, V> rotacionDobleADerecha(NodoBinario<K, V> nodoActual) {

        nodoActual.setHijoIzquierdo(rotacionSimpleAIzquierda(nodoActual.getHijoIzquierdo()));
        return  this.rotacionSimpleADerecha(nodoActual);
    }


    /**
     * 8.  mplemente el método eliminar de un árbol AVL
     * @param claveAEliminar
     * @return
     */

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
            return balancear(nodoActual);
        }
        if (claveAEliminar.compareTo(claveActual) < 0){
            NodoBinario<K,V> posibleNuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(posibleNuevoHijoIzquierdo);
            return balancear(nodoActual);
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

        return balancear(nodoActual);
    }
}
